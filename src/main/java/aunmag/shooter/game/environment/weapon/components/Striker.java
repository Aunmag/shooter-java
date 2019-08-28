package aunmag.shooter.game.environment.weapon.components;

import aunmag.shooter.core.utilities.Timer;
import aunmag.shooter.game.environment.World;

public class Striker {

    public final World world;
    private final Timer nextShotTime;

    public Striker(World world, int shotsPerMinute) {
        this.world = world;
        nextShotTime = new Timer(world.time, 60f / (float) shotsPerMinute);
    }

    public boolean isCocked() {
        boolean isCocked = nextShotTime.isDone();
        nextShotTime.next(true);
        return isCocked;
    }

}
