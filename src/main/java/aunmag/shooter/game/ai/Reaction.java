package aunmag.shooter.game.ai;

import aunmag.shooter.core.utilities.TimeFlow;
import aunmag.shooter.core.utilities.Timer;

public class Reaction {

    public static final int FIRST = 1;
    public static final int EIGHTH = 8;
    public static final float DEVIATION_FACTOR = 0.5f;

    private final Timer timer;
    private int counter = 0;

    public Reaction(TimeFlow time, float reaction) {
        timer = new Timer(time, reaction, DEVIATION_FACTOR);
    }

    public void update() {
        if (timer.isDone()) {
            counter++;

            if (counter > EIGHTH) {
                counter = 0;
            }
        }
    }

    public void next() {
        timer.next(true);
    }

    public boolean isPhase(int fraction) {
        return counter % fraction == 0;
    }

    public boolean isFirstPhase() {
        return isPhase(FIRST);
    }

    public boolean isEighthPhase() {
        return isPhase(EIGHTH);
    }

}
