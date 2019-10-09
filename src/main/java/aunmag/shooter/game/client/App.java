package aunmag.shooter.game.client;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.input.Input;
import aunmag.shooter.game.client.states.Game;
import aunmag.shooter.game.client.states.Pause;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public final class App extends Application {

    private boolean isDebug = false;
    private boolean isPause = true;
    public final Pause pause = new Pause();
    private Game game;

    public App() {
        pause.resume();
    }

    public void endGame() {
        if (game != null) {
            game.remove();
        }
        game = null;
    }

    public void newGame() {
        endGame();
        game = new Game();
        setPause(false);
    }

    @Override
    public void gameUpdate() {
        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_BACKSPACE)) {
            isDebug = !isDebug;
        }

        if (isPause) {
            pause.update();
        } else if (game != null) {
            game.update();
        }
    }

    @Override
    public void gameRender() {
        if (isPause) {
            pause.render();
        } else if (game != null) {
            game.render();
        }
    }

    @Override
    public void gameTerminate() {
        endGame();
    }

    public boolean isDebug() {
        return isDebug;
    }

    public boolean isPause() {
        return isPause;
    }

    @Nullable
    public Game getGame() {
        return game;
    }

    public void setPause(boolean isPause) {
        if (this.isPause == isPause || !isPause && game == null) {
            return;
        }

        this.isPause = isPause;
        Application.getWindow().setCursorGrabbed(!isPause);

        if (isPause) {
            pause.resume();
            if (game != null) {
                game.suspend();
            }
        } else {
            pause.suspend();
            if (game != null) {
                game.resume();
            }
        }
    }

}
