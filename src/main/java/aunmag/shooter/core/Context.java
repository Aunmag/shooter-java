package aunmag.shooter.core;

import aunmag.shooter.core.input.Input;

public class Context {

    public static Context main = null;

    public final Application application;

    public Context(Application application) {
        this.application = application;
    }

    public Input getInput() {
        return application.input;
    }

}
