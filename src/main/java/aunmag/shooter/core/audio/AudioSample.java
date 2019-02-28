package aunmag.shooter.core.audio;

import aunmag.shooter.core.Configs;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.OggData;
import org.newdawn.slick.openal.OggDecoder;
import org.newdawn.slick.openal.WaveData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.HashMap;

public final class AudioSample {

    private static HashMap<String, Integer> samples = new HashMap<>();

    private AudioSample() {}

    public static int getOrCreate(String name, AudioSampleType type) {
        if (!Configs.isSamplesLoadingEnabled()) {
            return 0;
        }

        if (samples.containsKey(name)) {
            return samples.get(name);
        } else {
            int sample;

            if (type == AudioSampleType.OGG) {
                sample = loadOggSample(name);
            } else {
                sample = loadWavSample(name);
            }

            samples.put(name, sample);
            return sample;
        }
    }

    private static int loadOggSample(String name) {
        String path = "/" + name + ".ogg";
        InputStream inputStream = AudioMaster.class.getResourceAsStream(path);
        OggData oggData;

        try {
            oggData = new OggDecoder().getData(inputStream);
        } catch (IOException e) {
            String message = String.format("Can't load audio file at \"%s\"!", path);
            System.err.println(message);
            return 0;
        }

        IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
        AL10.alGenBuffers(intBuffer);
        int buffer = intBuffer.get(0);
        int format;

        if (oggData.channels > 1) {
            format = AL10.AL_FORMAT_STEREO16;
        } else {
            format = AL10.AL_FORMAT_MONO16;
        }

        AL10.alBufferData(buffer, format, oggData.data, oggData.rate);

        return buffer;
    }

    private static int loadWavSample(String name) {
        String path = "/" + name + ".wav";
        InputStream inputStream = AudioMaster.class.getResourceAsStream(path);
        WaveData waveData = WaveData.create(inputStream);

        int buffer = AL10.alGenBuffers();
        AL10.alBufferData(buffer, waveData.format, waveData.data, waveData.samplerate);
        waveData.dispose();
        return buffer;
    }

    public static void cleanUp() {
        for (int sample: samples.values()) {
            AL10.alDeleteBuffers(sample);
        }

        samples.clear();
    }

}
