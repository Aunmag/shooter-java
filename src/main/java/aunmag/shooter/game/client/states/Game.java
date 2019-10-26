package aunmag.shooter.game.client.states;

import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.game.client.Context;
import aunmag.shooter.game.client.player.CameraShaker;
import aunmag.shooter.game.client.player.Player;
import aunmag.shooter.game.environment.World;
import aunmag.shooter.game.scenarios.Scenario;
import aunmag.shooter.game.scenarios.ScenarioEncircling;
import org.lwjgl.glfw.GLFW;

public class Game extends Operative {

    public final Player player = new Player();
    private World world = new World();
    private Scenario scenario = new ScenarioEncircling(world);

    public Game() {
        player.setActor(scenario.createPlayableActor());
    }

    public void resume() {
        world.playSounds();
    }

    public void suspend() {
        world.pauseSounds();
    }

    @Override
    public void update() {
        player.updateInput();
        world.update();
        scenario.update();
        player.update();
        CameraShaker.update();

        if (Context.main.getInput().keyboard.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            Context.main.application.setPause(true);
        }
    }

    @Override
    public void render() {
        world.render();
        player.render();
        scenario.render();
    }

    @Override
    protected void onRemove() {
        world.remove();
        scenario.remove();
        player.remove();
        super.onRemove();
    }

    public World getWorld() {
        return world;
    }

}
