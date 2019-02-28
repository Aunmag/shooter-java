package aunmag.shooter.game.environment.projectile;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.math.BodyLine;
import aunmag.shooter.core.math.CollisionCL;
import aunmag.shooter.core.math.Kinetics;
import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.game.environment.World;
import aunmag.shooter.game.environment.actor.Actor;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

/**
 * TODO: It changed. Complete it
 */
public class Projectile extends Operative {

    private static final float VELOCITY_MIN = 0.5f;
    private static final float VELOCITY_FACTOR = 1f / 5f;

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
                type.weight,
                velocity * (float) Math.cos(radians),
                velocity * (float) Math.sin(radians)
        );
        kinetics.restrictionFactor = type.velocityRecessionFactor;
        kinetics.restitutionFactor = 1f;
    }

    public void update() {
        if (isStopped()) {
            remove();
            return;
        }

        kinetics.update((float) world.getTime().getDelta());
        updatePosition();
        updateCollision();
        updateVelocity();

        if (type.velocityRecessionFactor <= 0) {
            stop();
        }
    }

    private void updatePosition() {
        float velocityFactor = VELOCITY_FACTOR * (float) world.getTime().getDelta();

        body.pullUpTail();
        body.position.add(
                kinetics.velocity.x * velocityFactor,
                kinetics.velocity.y * velocityFactor
        );
    }

    private void updateCollision() {
        Actor actor = null;
        float distance = 0;
        CollisionCL collision = null;

        for (Actor testActor: world.getActors().all) {
            CollisionCL testCollision = new CollisionCL(testActor.body, body);
            if (testCollision.isTrue()) {
                float testDistance = body.position.distance(testActor.body.position);
                if (actor == null || testDistance > distance) {
                    actor = testActor;
                    distance = testDistance;
                    collision = testCollision;
                }
            }
        }

        if (actor != null) {
            float energy = kinetics.velocity.length() * type.weight;
            actor.hit(energy, shooter);
            collision.resolveLineLength();

            float bulletPushFactor = 20f;

            float deviation = collision.provideDetails().deviation;
            float spinFactor = bulletPushFactor * bulletPushFactor;
            actor.kinetics.velocityRadians += deviation
                    * (energy / actor.type.weight)
                    * spinFactor;

            float initialWeight = kinetics.weight;
            kinetics.weight = initialWeight * bulletPushFactor;
            Kinetics.interact(kinetics, actor.kinetics);
            kinetics.weight = initialWeight;

            stop();
        }
    }

    private void updateVelocity() {
        Vector2f velocity = kinetics.velocity;

        if (Math.abs(velocity.x) + Math.abs(velocity.y) < VELOCITY_MIN) {
            stop();
        }
    }

    private void stop() {
        kinetics.velocity.set(0, 0);
    }

    public void render() {
        GL11.glLineWidth(type.size * Application.getCamera().getScaleFull());
        body.render();
    }

    /* Getters */

    public boolean isStopped() {
        return kinetics.velocity.x == 0 && kinetics.velocity.y == 0;
    }

}
