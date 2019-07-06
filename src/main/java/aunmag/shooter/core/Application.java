package aunmag.shooter.core;

import aunmag.shooter.core.audio.Listener;
import aunmag.shooter.core.audio.Sample;
import aunmag.shooter.core.audio.Source;
import aunmag.shooter.core.gui.Parameter;
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

    private static boolean isInitialized = false;
    private static boolean isRunning = false;
    private static Window window;
    private static Camera camera;
    private static ShaderTextured shader;
    private static Listener listener;
    public static final FrameRate frameRate = new FrameRate(60);
    public static final TimeFlow time = new TimeFlow();

    public Application() {
        if (isInitialized) {
            throw new IllegalStateException("Engine is already initialized!");
        }

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW!");
        }

        isInitialized = true;

        window = new Window();
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        GLFW.glfwSetScrollCallback(window.id, Input.mouse.wheel::onScroll);

        camera = new Camera();
        shader = new ShaderTextured();
        listener = new Listener();
    }

    public final void run() {
        isRunning = true;

        while (isRunning) {
            double timeCurrent = (double) System.currentTimeMillis() / 1000.0;

            if (frameRate.tryTick(timeCurrent)) {
                engineUpdate();
                engineRender();
            }
        }

        engineTerminate();
    }

    private void engineUpdate() {
        time.add(frameRate.getDelta(), true);
        Input.update();
        GLFW.glfwPollEvents();
        Parameter.PULSE.update();

        gameUpdate();
        camera.update();

        if (GLFW.glfwWindowShouldClose(window.id)) {
            stopRunning();
        }
    }

    private void engineRender() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        gameRender();
        Text.manager.renderAll();
        GLFW.glfwSwapBuffers(window.id);
    }

    private void engineTerminate() {
        gameTerminate();

        Text.manager.removeAll();
        Texture.cleanUp();
        Model.cleanUp();
        Shader.cleanUp();
        Source.all.remove();
        Sample.manger.clear();
        listener.remove();

        GLFW.glfwSetWindowShouldClose(window.id, true);
        GLFW.glfwTerminate();
    }

    protected abstract void gameUpdate();

    protected abstract void gameRender();

    protected abstract void gameTerminate();

    public static void stopRunning() {
        isRunning = false;
    }

    /* Getters */

    public static boolean isInitialized() {
        return isInitialized;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public static Window getWindow() {
        return window;
    }

    public static Camera getCamera() {
        return camera;
    }

    public static ShaderTextured getShader() {
        return shader;
    }

    public static Listener getListener() {
        return listener;
    }

}
