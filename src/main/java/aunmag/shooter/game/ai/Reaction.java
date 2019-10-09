package aunmag.shooter.game.ai;

import aunmag.shooter.core.utilities.TimeFlow;
import aunmag.shooter.core.utilities.Timer;

public class Reaction {

    private static final int PHASE_QUICK = 1;
    private static final int PHASE_SLOW = 8;
    private static final float DEVIATION_FACTOR = 0.5f;

    private final Timer timer;
    private boolean isActive = false;
    private int phase = 0;

    public Reaction(TimeFlow time, float reaction) {
        timer = new Timer(time, reaction, DEVIATION_FACTOR);
    }

    public void update() {
        isActive = timer.isDone();

        if (isActive) {
            phase++;

            if (phase > PHASE_SLOW) {
                phase = 0;
            }

            timer.next();
        }
    }

    private boolean isPhase(int phase) {
        return isActive && this.phase % phase == 0;
    }

    public boolean isQuickPhase() {
        return isPhase(PHASE_QUICK);
    }

    public boolean isSlowPhase() {
        return isPhase(PHASE_SLOW);
    }

}
