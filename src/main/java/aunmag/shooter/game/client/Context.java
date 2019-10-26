package aunmag.shooter.game.client;

import aunmag.shooter.game.client.states.Game;
import aunmag.shooter.game.environment.actor.Actor;
import org.jetbrains.annotations.Nullable;

public class Context extends aunmag.shooter.core.Context {

    public static Context main = null;

    public final App application;

    public Context(App application) {
        super(application);
        this.application = application;
    }

    public boolean isDebug() {
        return application.isDebug();
    }

    @Nullable
    public Game getGame() {
        return application.getGame();
    }

    @Nullable
    public Actor getPlayerActor() {
        var game = getGame();

        if (game == null) {
            return null;
        } else {
            return game.player.getActor();
        }
    }

}
