package aunmag.shooter.game;

import aunmag.shooter.core.Configs;
import aunmag.shooter.game.client.Application;
import aunmag.shooter.game.client.Context;

public final class Main {

    public static void main(String[] args) {
        Configs.setFullscreen(true);
        Context.main = new Context(new Application());
        Context.main.application.run();
    }

    private Main() {}

}
