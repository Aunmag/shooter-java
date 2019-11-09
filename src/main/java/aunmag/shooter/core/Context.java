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

    public Application getApplication() {
        return application;
    }

    public Window getWindow() {
        return getApplication().window;
    }

    public Camera getCamera() {
        return getApplication().camera;
    }

    public ShaderTextured getShader() {
        return getApplication().shader;
    }

    public Listener getListener() {
        return getApplication().listener;
    }

    public Input getInput() {
        return getApplication().input;
    }

    public TimeFlow getTime() {
        return getApplication().time;
    }

    public float getDelta() {
        return (float) getTime().getDelta();
    }

}
