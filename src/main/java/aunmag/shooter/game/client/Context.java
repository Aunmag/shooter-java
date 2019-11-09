package aunmag.shooter.game.client;

import aunmag.shooter.game.client.player.Player;
import aunmag.shooter.game.client.states.Game;
import aunmag.shooter.game.environment.actor.Actor;

import java.util.Optional;

public class Context extends aunmag.shooter.core.Context {

    public static Context main = null;

    public final Application application;

    public Context(Application application) {
        super(application);
        this.application = application;
    }

    @Override
    public Application getApplication() {
        return application;
    }

    public boolean isDebug() {
        return getApplication().isDebug();
    }

    public Optional<Game> getGame() {
        return Optional.ofNullable(getApplication().getGame());
    }

    public Optional<Player> getPlayer() {
        return getGame().map(game -> game.player);
    }

    public Optional<Actor> getPlayerActor() {
        return getPlayer().flatMap(player -> Optional.ofNullable(player.getActor()));
    }

}
