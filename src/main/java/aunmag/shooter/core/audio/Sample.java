package aunmag.shooter.core.audio;

import org.lwjgl.openal.AL10;

import java.nio.ShortBuffer;

public class Sample extends AudioObject {

    public static final SampleManger manger = new SampleManger();

    public Sample(boolean isStereo, ShortBuffer data, int frequency) {
        super(AL10.alGenBuffers());

        AL10.alBufferData(
                id,
                isStereo ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16,
                data,
                frequency
        );
    }

    public Source toSource() {
        var source = new Source();
        source.setSample(this);
        return source;
    }

    @Override
    protected void onRemove() {
        AL10.alDeleteBuffers(id);
    }

}
