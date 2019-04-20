package aunmag.shooter.game.client;

import aunmag.shooter.game.client.states.Game;
import aunmag.shooter.game.environment.actor.Actor;
import org.jetbrains.annotations.Nullable;

public final class Context {

    public static Context main = null;

    public final App application;

    public Context(App application) {
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
            return game.getPlayer().getActor();
        }
    }

}
