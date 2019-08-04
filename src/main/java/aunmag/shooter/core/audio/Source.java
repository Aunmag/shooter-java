package aunmag.shooter.core.audio;

import aunmag.shooter.core.utilities.OperativeManager;
import org.lwjgl.openal.AL10;

public class Source extends AudioObject {

    public static final OperativeManager<Source> all = new OperativeManager<>();

    public Source() {
        super(AL10.alGenSources());
        all.all.add(this);
    }

    public void play() {
        AL10.alSourcePlay(id);
    }

    public void pause() {
        AL10.alSourcePause(id);
    }

    public void stop() {
        AL10.alSourceStop(id);
    }

    @Override
    protected void onRemove() {
        stop();
        AL10.alDeleteSources(id);
    }

    public void setVolume(float volume) {
        AL10.alSourcef(id, AL10.AL_GAIN, volume);
    }

    public void setSample(Sample sample) {
        stop();
        AL10.alSourcei(id, AL10.AL_BUFFER, sample.id);
    }

    public void setPosition(float x, float y) {
        AL10.alSource3f(id, AL10.AL_POSITION, x, y, 0);
    }

    public void setLooped(boolean isLooped) {
        AL10.alSourcei(id, AL10.AL_LOOPING, isLooped ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    public boolean isPlaying() {
        return AL10.alGetSourcei(id, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

}
