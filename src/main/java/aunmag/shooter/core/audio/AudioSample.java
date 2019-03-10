package aunmag.shooter.core.audio;

import aunmag.shooter.core.Configs;
import aunmag.shooter.core.utilities.UtilsFile;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public final class AudioSample {

    public static final int EMPTY = 0;
    private static HashMap<String, Integer> samples = new HashMap<>();

    private AudioSample() {}

    public static int getOrCreate(String name) {
        if (Configs.isSamplesLoadingEnabled()) {
            return samples.computeIfAbsent(name, AudioSample::load2);
        } else {
            return EMPTY;
        }
    }

    private static int load2(String name) {
        var path = "/" + name + ".ogg";
        ByteBuffer bytes;

        try {
            bytes = UtilsFile.readByteBuffer(path);
        } catch (IOException e) {
            UtilsFile.printReadError(path);
            return EMPTY;
        }

        MemoryStack.stackPush();
        var channelsBuffer = MemoryStack.stackMallocInt(1);

        MemoryStack.stackPush();
        var sampleRateBuffer = MemoryStack.stackMallocInt(1);

        var bufferRaw = STBVorbis.stb_vorbis_decode_memory(
                bytes,
                channelsBuffer,
                sampleRateBuffer
        );

        var channels = channelsBuffer.get();
        var sampleRate = sampleRateBuffer.get();

        MemoryStack.stackPop();
        MemoryStack.stackPop();

        var buffer = EMPTY;

        if (bufferRaw != null) {
            buffer = AL10.alGenBuffers();
            AL10.alBufferData(buffer, toFormat(channels), bufferRaw, sampleRate);
            LibCStdlib.free(bufferRaw);
        } else {
            UtilsFile.printReadError(path);
        }

        return buffer;
    }

    private static int toFormat(int channels) {
        if (channels == 1) {
            return AL10.AL_FORMAT_MONO16;
        } else if (channels == 2) {
            return AL10.AL_FORMAT_STEREO16;
        } else {
            return -1;
        }
    }

    public static void cleanUp() {
        for (int sample: samples.values()) {
            AL10.alDeleteBuffers(sample);
        }

        samples.clear();
    }

}
