package aunmag.shooter.core.audio;

import aunmag.shooter.core.utilities.Operative;

public abstract class AudioObject extends Operative {

    public final int id;

    public AudioObject(int id) {
        this.id = id;
    }

    @Override
    protected abstract void onRemove();

}
