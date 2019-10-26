package aunmag.shooter.core.input;

public class Input {

    public long window;
    public final Mouse mouse;
    public final Keyboard keyboard;

    public Input(long window) {
        this.window = window;
        this.mouse = new Mouse(this);
        this.keyboard = new Keyboard(this);
    }

    public void update() {
        mouse.update();
        keyboard.update();
    }

}
