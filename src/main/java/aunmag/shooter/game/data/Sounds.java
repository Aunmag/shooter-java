package aunmag.shooter.game.data;

import aunmag.shooter.core.audio.Source;
import aunmag.shooter.core.utilities.UtilsAudio;
import org.jetbrains.annotations.Nullable;

public final class Sounds {

    @Nullable public static final Source soundAmbiance;
    @Nullable public static final Source soundAtmosphere;
    @Nullable public static final Source soundGameOver;

    static {
        soundAmbiance = UtilsAudio.provideSound("sounds/ambiance/birds");
        if (soundAmbiance != null) {
            soundAmbiance.setVolume(0.4f);
            soundAmbiance.setLooped(true);
        }

        soundAtmosphere = UtilsAudio.provideSound("sounds/music/gameplay_atmosphere");
        if (soundAtmosphere != null) {
            soundAtmosphere.setVolume(0.06f);
            soundAtmosphere.setLooped(true);
        }

        soundGameOver = UtilsAudio.provideSound("sounds/music/death");
        if (soundGameOver != null) {
            soundGameOver.setVolume(0.6f);
        }
    }

    private Sounds() {}

}
