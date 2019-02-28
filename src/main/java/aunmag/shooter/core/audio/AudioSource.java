package aunmag.shooter.core.audio;

import org.lwjgl.openal.AL10;

import java.util.ArrayList;

public class AudioSource {

    private static ArrayList<Integer> ids = new ArrayList<>();

    public static void cleanUp() {
        for (int id: ids) {
            AL10.alSourceStop(id);
            AL10.alDeleteSources(id);
        }

        ids.clear();
    }

    private final int id;
    private float volume = 1;

    public AudioSource() {
        id = AL10.alGenSources();
        ids.add(id);
    }

    public void play() {
        AL10.alSourcePlay(id);
        setVolume(volume);
    }

    public void pause() {
        AL10.alSourcePause(id);
    }

    public void stop() {
        AL10.alSourceStop(id);
    }

    /* Setters */

    public void setVolume(float volume) {
        this.volume = volume;
        AL10.alSourcef(id, AL10.AL_GAIN, volume);
    }

    public void setSample(int sample) {
        stop();
        AL10.alSourcei(id, AL10.AL_BUFFER, sample);
    }

    public void setPitch(float pitch) {
        AL10.alSourcef(id, AL10.AL_PITCH, pitch);
    }

    public void setPosition(float x, float y) {
        AL10.alSource3f(id, AL10.AL_POSITION, x, y, 0);
    }

    public void setVelocity(float x, float y) {
        AL10.alSource3f(id, AL10.AL_VELOCITY, x, y, 0);
    }

    public void setIsLooped(boolean isLooped) {
        AL10.alSourcei(id, AL10.AL_LOOPING, isLooped ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    /* Getters */

    public boolean isPlaying() {
        return AL10.alGetSourcei(id, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

}
