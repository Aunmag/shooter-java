package aunmag.shooter.game.scenarios;

import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.game.environment.World;
import aunmag.shooter.game.environment.actor.Actor;
import org.jetbrains.annotations.Nullable;

public abstract class Scenario extends Operative {

    public final World world;

    public Scenario(World world) {
        this.world = world;
    }

    @Nullable
    public abstract Actor createPlayableActor();

}
