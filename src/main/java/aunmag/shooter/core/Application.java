package aunmag.shooter.core;

import aunmag.shooter.core.audio.Listener;
import aunmag.shooter.core.audio.Sample;
import aunmag.shooter.core.audio.Source;
import aunmag.shooter.core.gui.font.Text;
import aunmag.shooter.core.input.Input;
import aunmag.shooter.core.shaders.ShaderTextured;
import aunmag.shooter.core.structures.Model;
import aunmag.shooter.core.structures.Shader;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.core.utilities.FrameRate;
import aunmag.shooter.core.utilities.TimeFlow;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public abstract class Application {

    private boolean isRunning = false;
    public final Window window;
    public final Camera camera;
    public final Input input;
    public final ShaderTextured shader;
    public final Listener listener;
    public final FrameRate frameRate = new FrameRate(60);
    public final TimeFlow time = new TimeFlow();

    public Application() {
        if (isInitialized()) {
            throw new IllegalStateException("Engine is already initialized!");
        }

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW!");
        }

        Context.main = new Context(this);

        window = new Window();

        GL.createCapabilities();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);

        camera = new Camera();
        shader = new ShaderTextured();
        listener = new Listener();
        input = new Input(window.id);
    }

    public final void run() {
        isRunning = true;

        while (isRunning) {
            var timeCurrent = (double) System.currentTimeMillis() / 1000.0;

            if (frameRate.tryTick(timeCurrent)) {
                updateCore();
                renderCore();
            }
        }

        onTerminate();
    }

    private void updateCore() {
        time.add(frameRate.getDelta(), true);
        input.update();
        GLFW.glfwPollEvents();

        update();
        camera.update();

        if (GLFW.glfwWindowShouldClose(window.id)) {
            stop();
        }
    }

    private void renderCore() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        render();
        Text.manager.renderAll();
        GLFW.glfwSwapBuffers(window.id);
    }

    protected void onTerminate() {
        Text.manager.removeAll();
        Texture.manager.clear();
        Model.cleanUp();
        Shader.cleanUp();
        Source.all.remove();
        Sample.manger.clear();
        listener.remove();

        GLFW.glfwSetWindowShouldClose(window.id, true);
        GLFW.glfwTerminate();
    }

    protected abstract void update();

    protected abstract void render();

    public void stop() {
        isRunning = false;
    }

    // TODO: Remove
    public static boolean isInitialized() {
        return Context.main != null;
    }

}
