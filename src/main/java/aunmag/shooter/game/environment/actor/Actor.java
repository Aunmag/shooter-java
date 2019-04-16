package aunmag.shooter.game.environment.actor;

import aunmag.shooter.core.audio.AudioSample;
import aunmag.shooter.core.audio.AudioSource;
import aunmag.shooter.core.math.BodyCircle;
import aunmag.shooter.core.math.CollisionCC;
import aunmag.shooter.core.math.Kinetics;
import aunmag.shooter.core.utilities.FluidToggle;
import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.Config;
import aunmag.shooter.game.client.App;
import aunmag.shooter.game.client.graphics.CameraShaker;
import aunmag.shooter.game.client.states.ScenarioStatus;
import aunmag.shooter.game.data.LinksKt;
import aunmag.shooter.game.environment.World;
import aunmag.shooter.game.environment.weapon.Weapon;

public class Actor extends Operative {

    @Config public static final float VELOCITY_FACTOR_ASIDE = 0.6f;
    @Config public static final float VELOCITY_FACTOR_BACK = 0.8f;
    @Config public static final float AIMING_TIME = 0.25f;
    @Config public static final float AIMING_FLEX = 1.25f;
    @Config public static final float AIMING_VELOCITY_AFFECT = 0.5f;
    @Config public static final float AIMING_STAMINA_COST = 0.5f;
    @Config public static final float WALKING_STAMINA_COST = 0.7f;
    @Config public static final float SPRINT_STAMINA_COST = 1.8f;
    @Config public static final float RELOADING_STAMINA_COST = 0.2f;
    @Config public static final float PAIN_THRESHOLD = 0.005f;

    private static final int[] samples = new int[6];

    public final World world;
    public final ActorType type;
    public final BodyCircle body;
    public final Kinetics kinetics;
    private float health = 1.0f;
    public final Stamina stamina;
    private int kills = 0;
    private Weapon weapon = null;
    public final Hands hands;
    private AudioSource audioSource = new AudioSource();
    public final Control control = new Control();
    public final FluidToggle isAiming;
    public boolean currentIsPressAiming;
    public boolean currentIsHoldAiming;

    static {
        for (int i = 0; i < samples.length; i++) {
            String sampleName = "sounds/actors/human_hurt_" + (i + 1);
            samples[i] = AudioSample.getOrCreate(sampleName);
        }
    }

    public Actor(ActorType type, World world, float x, float y, float radians) {
        this.type = type;
        this.world = world;
        body = new BodyCircle(x, y, radians, type.radius);
        hands = new Hands(this);
        stamina = new Stamina(this);

        isAiming = new FluidToggle(world.getTime(), AIMING_TIME);
        isAiming.setFlexDegree(AIMING_FLEX);

        currentIsPressAiming = false;
        currentIsHoldAiming = false;

        kinetics = new Kinetics(type.weight);

        audioSource.setVolume(5);
    }

    public void update() {
        if (isAlive()) {
            updateStamina();
            updateAiming();
            walk();
            updateKinetics();
            updateCollision();
            hands.update();
            updateWeapon();
            updateAudioSource();
            if (
                    type != ActorType.human
                    && type != ActorType.humanCowboy
                    && health < 0.15f
            ) {
                health -= 0.00005f;
            }
        } else {
            remove();
        }

        control.reset();
    }

    private void updateStamina() {
        stamina.update();
        float spend = AIMING_STAMINA_COST * isAiming.getCurrent();

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
        float timeDelta = (float) world.getTime().getDelta();
        kinetics.update(timeDelta);

        float velocityX = kinetics.velocity.x * timeDelta;
        float velocityY = kinetics.velocity.y * timeDelta;
        body.position.add(velocityX, velocityY);
        body.radians += kinetics.velocityRadians * timeDelta;
    }

    private void updateCollision() {
        for (Actor opponent: world.getActors().all) {
            if (opponent != this) {
                CollisionCC collision = new CollisionCC(body, opponent.body);

                if (collision.isTrue()) {
                    Kinetics.interact(kinetics, opponent.kinetics);
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

        float offset = Hands.DISTANCE;
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
        if (ScenarioStatus.crosshairControl == ScenarioStatus.CrosshairControl.PRESS) {
            if (control.isPressAiming() && !currentIsPressAiming) {
                isAiming.on();
                currentIsPressAiming = true;
            } else if (!control.isPressAiming() && currentIsPressAiming) {
                isAiming.off();
                currentIsPressAiming = false;
            }
        } else {
            if (control.isHoldAiming() && !currentIsHoldAiming) {
                isAiming.on();
                currentIsHoldAiming = true;
            } else if (!control.isHoldAiming() && currentIsHoldAiming) {
                isAiming.off();
                currentIsHoldAiming = false;
            }
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

        var velocityCurrent = kinetics.velocityRadians;
        var velocityFuture = velocity + velocityCurrent;
        var distanceFuture = velocityFuture / kinetics.radiansRestrictionFactor;
        var distanceExcess = distanceFuture / distance;

        if (distanceExcess > 1) {
            velocity /= distanceExcess;
        }

        kinetics.addEnergy(0, 0, velocity, (float) world.getTime().getDelta());
    }

    private void move(double velocity, double radiansTurn) {
        if (weapon != null && weapon.magazine.isReloading()) {
            velocity *= 0.7;
        }
        if (control.isSprinting() && control.isWalkingForward()) {
            float efficiency = this.stamina.calculateEfficiency();
            velocity *= type.velocityFactorSprint * efficiency + (1 - efficiency);
        }

        velocity -= velocity * isAiming.getCurrent() * AIMING_VELOCITY_AFFECT;
        velocity *= health;

        float moveX = (float) (velocity * Math.cos(body.radians + radiansTurn));
        float moveY = (float) (velocity * Math.sin(body.radians + radiansTurn));
        float timeDelta = (float) world.getTime().getDelta();
        kinetics.addEnergy(moveX, moveY, 0, timeDelta);
    }

    public void hit(float damage, Actor attacker) {
        damage /= type.strength;

        boolean wasAlive = isAlive();

        addHealth(-damage);

        if (wasAlive && !isAlive() && attacker != null) {
            attacker.increaseKills();
        }

        push(UtilsMath.random.nextBoolean() ? damage : -damage);
    }

    public void push(float force) {
        kinetics.velocityRadians += force * 8f;

        if (this == LinksKt.getPlayer()) {
            CameraShaker.shake(force);
        }
    }

    public void render() {
        if (weapon != null) {
            weapon.render();
        }

        if (App.main.isDebug()) {
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

        int sample = samples[UtilsMath.random.nextInt(samples.length)];
        audioSource.setSample(sample);
        audioSource.play();
    }

    private void increaseKills() {
        kills++;
    }

    /* Setters */

    private void addHealth(float addHealth) {
        health = UtilsMath.limitNumber(health + addHealth, 0, 1);

        if (!isAlive()) {
            remove();
        } else if (addHealth < -PAIN_THRESHOLD) {
            soundHurt();
        }
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /* Getters */

    public World getWorld() {
        return world;
    }

    public float getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean getHasWeapon() {
        return weapon != null;
    }

    public int getKills() {
        return kills;
    }

    public Weapon getWeapon() {
        return weapon;
    }

}
