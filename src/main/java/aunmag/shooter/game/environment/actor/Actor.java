package aunmag.shooter.game.environment.actor;

import aunmag.shooter.core.audio.Sample;
import aunmag.shooter.core.audio.Source;
import aunmag.shooter.core.math.BodyCircle;
import aunmag.shooter.core.math.CollisionCC;
import aunmag.shooter.core.math.Kinetics;
import aunmag.shooter.core.utilities.FluidToggle;
import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.Timer;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.core.utilities.UtilsRandom;
import aunmag.shooter.game.client.Context;
import aunmag.shooter.game.client.player.CameraShaker;
import aunmag.shooter.game.environment.World;
import aunmag.shooter.game.environment.weapon.Weapon;
import org.jetbrains.annotations.Nullable;

public class Actor extends Operative {

    public static final float VELOCITY_FACTOR_ASIDE = 0.6f;
    public static final float VELOCITY_FACTOR_BACK = 0.8f;
    public static final float AIMING_TIME = 0.25f;
    public static final float AIMING_FLEX = 1.25f;
    public static final float AIMING_VELOCITY_AFFECT = 0.5f;
    public static final float AIMING_STAMINA_COST = 0.5f;
    public static final float WALKING_STAMINA_COST = 0.7f;
    public static final float SPRINT_STAMINA_COST = 1.8f;
    public static final float RELOADING_STAMINA_COST = 0.2f;
    public static final float PAIN_THRESHOLD = 0.005f;
    public static final float DECAY_TIME = 0.5f;

    private static final Sample[] samples = new Sample[6];

    public final World world;
    public final ActorType type;
    public final BodyCircle body;
    public final Kinetics kinetics;
    private float health = 1.0f;
    public final Stamina stamina;
    private int kills = 0;
    private Weapon weapon = null;
    public final Hands hands;
    private Source audioSource = new Source();
    public final Control control = new Control();
    public final FluidToggle isAiming;
    private final Timer decayTimer;

    static {
        for (int i = 0; i < samples.length; i++) {
            var sampleName = "sounds/actors/human_hurt_" + (i + 1);
            samples[i] = Sample.manger.provide(sampleName);
        }
    }

    public Actor(ActorType type, World world, float x, float y, float radians) {
        this.type = type;
        this.world = world;
        body = new BodyCircle(x, y, radians, type.radius);
        hands = new Hands(this);
        stamina = new Stamina(this);
        decayTimer = new Timer(world.time, DECAY_TIME);

        isAiming = new FluidToggle(world.time, AIMING_TIME);
        isAiming.setFlexDegree(AIMING_FLEX);

        kinetics = new Kinetics(type.mass);

        audioSource.setVolume(5);
    }

    @Override
    public void update() {
        if (!isAlive()) {
            control.reset();
        }

        updateStamina();
        updateAiming();
        walk();
        updateKinetics();
        updateCollision();
        hands.update();
        updateWeapon();
        updateAudioSource();

        control.reset();
    }

    private void updateStamina() {
        stamina.update();
        var spend = AIMING_STAMINA_COST * isAiming.getCurrent();

        if (control.isWalking()) {
            spend += WALKING_STAMINA_COST;
            if (control.isSprinting()) {
                spend += SPRINT_STAMINA_COST;
            }
        }

        if (weapon != null && weapon.magazine.isReloading()) {
            spend += RELOADING_STAMINA_COST;
        }

        if (spend != 0.0) {
            stamina.spend(spend);
        }
    }

    protected void updateKinetics() {
        var timeDelta = (float) world.time.getDelta();
        kinetics.update(timeDelta);

        var velocityX = kinetics.velocity.x * timeDelta;
        var velocityY = kinetics.velocity.y * timeDelta;
        body.position.add(velocityX, velocityY);
        body.radians += kinetics.spin * timeDelta;
    }

    private void updateCollision() {
        for (var opponent: world.actors.all) {
            if (opponent != this) {
                var collision = new CollisionCC(body, opponent.body);

                if (collision.isTrue()) {
                    Kinetics.interact(
                            kinetics,
                            opponent.kinetics,
                            body.position,
                            opponent.body.position
                    );
                    collision.resolve();
                }
            }
        }
    }

    private void updateWeapon() {
        if (weapon == null) {
            return;
        }

        weapon.body.radians = body.radians;

        var offset = Hands.DISTANCE;
        offset += weapon.type.length / 2;
        offset -= weapon.type.gripOffset;

        weapon.body.positionTail.set(
                body.position.x + offset * (float) Math.cos(body.radians),
                body.position.y + offset * (float) Math.sin(body.radians)
        );

        if (control.isAttacking()) {
            weapon.trigger.pressBy(this);
        } else {
            weapon.trigger.release();
        }

        weapon.update();
    }

    private void updateAudioSource() {
        audioSource.setPosition(body.position.x, body.position.y);
    }

    private void updateAiming() {
        if (control.isAiming()) {
            isAiming.on();
        } else {
            isAiming.off();
        }

        isAiming.update();
    }

    private void walk() {
        turn();

        if (control.isWalkingForward()) {
            move(type.velocity, 0);
        }

        if (control.isWalkingBack()) {
            move(type.velocity * VELOCITY_FACTOR_BACK, -Math.PI);
        }

        if (control.isWalkingLeft()) {
            move(type.velocity * VELOCITY_FACTOR_ASIDE, +UtilsMath.PIx0_5);
        }

        if (control.isWalkingRight()) {
            move(type.velocity * VELOCITY_FACTOR_ASIDE, -UtilsMath.PIx0_5);
        }
    }

    private void turn() {
        var turningTo = control.getTurningTo();
        if (turningTo == null) {
            return;
        }

        var velocity = type.velocityRotation;
        var distance = UtilsMath.radiansDifference(body.radians, turningTo);

        if (Math.abs(distance) < 0.0001) {
            return;
        }

        if (distance < 0) {
            velocity = -velocity;
        }

        var velocityCurrent = kinetics.spin;
        var velocityFuture = velocity + velocityCurrent;
        var distanceFuture = velocityFuture / kinetics.restrictionFactorSpin;
        var distanceExcess = distanceFuture / distance;

        if (distanceExcess > 1) {
            velocity /= distanceExcess;
        }

        kinetics.addEnergy(0, 0, velocity, (float) world.time.getDelta());
    }

    private void move(double velocity, double radiansTurn) {
        if (control.isSprinting() && control.isWalkingForward()) {
            var efficiency = this.stamina.getEfficiency();
            velocity *= type.velocityFactorSprint * efficiency + (1 - efficiency);
        }

        velocity -= velocity * isAiming.getCurrent() * AIMING_VELOCITY_AFFECT;
        velocity *= health;

        var moveX = (float) (velocity * Math.cos(body.radians + radiansTurn));
        var moveY = (float) (velocity * Math.sin(body.radians + radiansTurn));
        var timeDelta = (float) world.time.getDelta();
        kinetics.addEnergy(moveX, moveY, 0, timeDelta);
    }

    public void hit(float damage, @Nullable Actor attacker) {
        if (isAlive()) {
            addHealth(-damage / type.strength);

            if (attacker != null && !isAlive()) {
                attacker.increaseKills();
            }
        }
    }

    public void shake(float force, boolean randomizeDirection) {
        if (randomizeDirection && UtilsRandom.RANDOM.nextBoolean()) {
            force = -force;
        }

        kinetics.push(0, 0, force);

        if (this == Context.main.getPlayerActor()) {
            CameraShaker.shake(kinetics.compensateMomentum(force));
        }
    }

    @Override
    public void render() {
        if (weapon != null) {
            weapon.render();
        }

        if (Context.main.isDebug()) {
            body.render();
            hands.coverage.render();
        } else {
            type.texture.renderOnWorld(
                    body.position.x,
                    body.position.y,
                    body.radians
            );
        }
    }

    private void soundHurt() {
        if (type != ActorType.human) {
            return;
        }

        if (audioSource.isPlaying()) {
            return;
        }

        UtilsRandom.chose(samples).ifPresent(sample -> {
            audioSource.setSample(sample);
            audioSource.play();
        });
    }

    private void increaseKills() {
        kills++;
    }

    private void addHealth(float addHealth) {
        var wasAlive = isAlive();

        health = UtilsMath.limit(health + addHealth, 0, 1);

        if (wasAlive) {
            if (isAlive()) {
                if (addHealth < -PAIN_THRESHOLD) {
                    soundHurt();
                }
            } else {
                decayTimer.next();
            }
        }
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public boolean isActive() {
        return super.isActive() && (isAlive() || !decayTimer.isDone());
    }

    public float getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean hasWeapon() {
        return weapon != null;
    }

    public int getKills() {
        return kills;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public float getDirectionDesired() {
        var direction = control.getTurningTo();

        if (direction == null) {
            direction = body.radians;
        }

        return direction;
    }

}
