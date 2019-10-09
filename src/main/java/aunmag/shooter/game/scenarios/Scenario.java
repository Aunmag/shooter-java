package aunmag.shooter.game.scenarios;

import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.game.environment.World;
import aunmag.shooter.game.environment.actor.Actor;
import aunmag.shooter.game.environment.actor.ActorType;
import org.jetbrains.annotations.Nullable;

public abstract class Scenario extends Operative {

    public static ActorType selectedActor = null;
    public final World world;

    public Scenario(World world) {
        this.world = world;
    }

    @Nullable
    public abstract Actor createPlayableActor();

}
