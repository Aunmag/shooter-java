package aunmag.shooter.game.environment.projectile;

import aunmag.shooter.core.Context;
import aunmag.shooter.core.math.BodyLine;
import aunmag.shooter.core.math.CollisionCL;
import aunmag.shooter.core.math.Kinetics;
import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.game.environment.World;
import aunmag.shooter.game.environment.actor.Actor;
import org.lwjgl.opengl.GL11;

public class Projectile extends Operative {

    private static final float VELOCITY_MIN = 5;
    private static final float VELOCITY_FACTOR = 1f / 5f;
    private static final float PUSH_FACTOR = 30;
    private static final float PUSH_FACTOR_SPIN = 8;

    public final BodyLine body;
    public final Kinetics kinetics;
    public final World world;
    public final ProjectileType type;
    public final Actor shooter;

    public Projectile(
            World world,
            ProjectileType type,
            float x,
            float y,
            float radians,
            float velocity,
            Actor shooter
    ) {
        body = new BodyLine(x, y, x, y);
        body.radians = radians;
        body.color.set(type.color);
        this.world = world;
        this.type = type;
        this.shooter = shooter;

        kinetics = new Kinetics(
                type.mass,
                velocity * (float) Math.cos(radians),
                velocity * (float) Math.sin(radians)
        );
        kinetics.restrictionFactor = type.velocityRecessionFactor;
        kinetics.restitution = 0f;
    }

    @Override
    public void update() {
        kinetics.update((float) world.time.getDelta());
        updatePosition();
        updateCollision();
        updateVelocity();

        if (type.velocityRecessionFactor <= 0) {
            stop();
        }
    }

    private void updatePosition() {
        var velocityFactor = VELOCITY_FACTOR * (float) world.time.getDelta();

        body.pullUpTail();
        body.position.add(
                kinetics.velocity.x * velocityFactor,
                kinetics.velocity.y * velocityFactor
        );
    }

    private void updateCollision() {
        var actor = (Actor) null;
        var distance = 0.0f;
        var collision = (CollisionCL) null;

        for (var testActor: world.actors.all) {
            var testCollision = new CollisionCL(testActor.body, body);
            if (testCollision.isTrue()) {
                var testDistance = body.position.distanceSquared(testActor.body.position);
                if (actor == null || testDistance > distance) {
                    actor = testActor;
                    distance = testDistance;
                    collision = testCollision;
                }
            }
        }

        if (actor != null) {
            collision.resolveLineLength();
            actor.hit(kinetics.getMomentum(), shooter);

            var deviation = collision.provideDetails().deviation;
            kinetics.velocity.mul(PUSH_FACTOR);
            actor.shake(kinetics.getMomentum() * deviation * PUSH_FACTOR_SPIN, false);
            Kinetics.interact(kinetics, actor.kinetics);
            stop();
        }
    }

    private void updateVelocity() {
        if (kinetics.velocity.lengthSquared() < VELOCITY_MIN * VELOCITY_MIN) {
            stop();
        }
    }

    private void stop() {
        kinetics.velocity.set(0, 0);
    }

    @Override
    public void render() {
        GL11.glLineWidth(Context.main.getCamera().toPixels(type.size));
        body.render();
    }

    public boolean isStopped() {
        return kinetics.velocity.x == 0 && kinetics.velocity.y == 0;
    }

    @Override
    public boolean isActive() {
        return super.isActive() && !isStopped();
    }

}
