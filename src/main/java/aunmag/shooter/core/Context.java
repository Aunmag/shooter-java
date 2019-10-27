package aunmag.shooter.core;

import aunmag.shooter.core.audio.Listener;
import aunmag.shooter.core.input.Input;
import aunmag.shooter.core.shaders.ShaderTextured;
import aunmag.shooter.core.utilities.TimeFlow;

public class Context {

    public static Context main = null;

    public final Application application;

    public Context(Application application) {
        this.application = application;
    }

    public Window getWindow() {
        return application.window;
    }

    public Camera getCamera() {
        return application.camera;
    }

    public ShaderTextured getShader() {
        return application.shader;
    }

    public Listener getListener() {
        return application.listener;
    }

    public Input getInput() {
        return application.input;
    }

    public TimeFlow getTime() {
        return application.time;
    }

    public float getDelta() {
        return (float) getTime().getDelta();
    }

}
