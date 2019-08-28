package aunmag.shooter.core.utilities;

import aunmag.shooter.core.audio.Sample;
import aunmag.shooter.core.audio.Source;
import org.jetbrains.annotations.Nullable;

public final class UtilsAudio {

    private UtilsAudio() {}

    @Nullable
    public static Source provideSound(String name) {
        var sample = Sample.manger.provide(name);

        if (sample == null) {
            return null;
        } else {
            return sample.toSource();
        }
    }

}
