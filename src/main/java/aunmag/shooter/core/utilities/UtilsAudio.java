package aunmag.shooter.core.utilities;

import aunmag.shooter.core.audio.AudioSample;
import aunmag.shooter.core.audio.AudioSampleType;
import aunmag.shooter.core.audio.AudioSource;

public final class UtilsAudio {

    private UtilsAudio() {}

    public static AudioSource getOrCreateSoundOgg(String name) {
        AudioSource audioSource = new AudioSource();
        audioSource.setSample(AudioSample.getOrCreate(name, AudioSampleType.OGG));
        return audioSource;
    }

    public static AudioSource getOrCreateSoundWav(String name) {
        AudioSource audioSource = new AudioSource();
        audioSource.setSample(AudioSample.getOrCreate(name, AudioSampleType.WAV));
        return audioSource;
    }

}
