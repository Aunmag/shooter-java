package aunmag.shooter.core.input;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.Window;

/**
 * TODO: Do not use static.
 */
public final class Input {

    public static final Mouse mouse = new Mouse();
    public static final Keyboard keyboard = new Keyboard();

    private Input() {}

    public static void update() {
        mouse.update();
        keyboard.update();
    }

    public static boolean isAvailable() {
        return Application.getWindow().id != Window.UNDEFINED_ID;
    }

}
