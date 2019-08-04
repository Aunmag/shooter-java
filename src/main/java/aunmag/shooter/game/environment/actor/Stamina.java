package aunmag.shooter.game.environment.actor;

import aunmag.shooter.core.utilities.UtilsMath;

public class Stamina {

    private static final float MIN = 0.01f;
    private static final float MAX = 1;
    private static final float RECURRENCE_STEP = 0.07f;

    private final Actor actor;
    private float stamina = MAX;

    public Stamina(Actor actor) {
        this.actor = actor;
    }

    public void update() {
        if (stamina < MAX) {
            stamina += RECURRENCE_STEP
                    * actor.world.time.getDelta()
                    * actor.getHealth();
        }
    }

    public void spend(float stepFactor) {
        var delta = (float) actor.world.time.getDelta();

        stamina = UtilsMath.limit(
                stamina - RECURRENCE_STEP * stepFactor * delta,
                MIN,
                MAX
        );
    }

    public float get() {
        return stamina;
    }

    public float getEfficiency() {
        return MAX - (float) Math.pow(MAX - stamina, 2);
    }

}
