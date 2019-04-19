package aunmag.shooter.game.ai.memory;

import aunmag.shooter.game.ai.Ai;

public abstract class Record {

    public final Ai ai;

    public Record(Ai ai) {
        this.ai = ai;
    }

    public abstract void refresh();

}
