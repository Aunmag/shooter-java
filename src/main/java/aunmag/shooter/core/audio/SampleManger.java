package aunmag.shooter.core.audio;

import aunmag.shooter.core.utilities.ResourceManager;
import aunmag.shooter.core.utilities.UtilsFile;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

import java.io.IOException;

public final class SampleManger extends ResourceManager<Sample> {

    @Override
    public Sample load(String path) throws IOException {
        var bytes = UtilsFile.readByteBuffer(path);

        MemoryStack.stackPush();
        var channelsBuffer = MemoryStack.stackMallocInt(1);

        MemoryStack.stackPush();
        var frequencyBuffer = MemoryStack.stackMallocInt(1);

        @Nullable
        var data = STBVorbis.stb_vorbis_decode_memory(
                bytes,
                channelsBuffer,
                frequencyBuffer
        );

        var channels = channelsBuffer.get();
        var frequency = frequencyBuffer.get();

        MemoryStack.stackPop();
        MemoryStack.stackPop();

        if (data == null) {
            throw new IOException("Failed to read audio data");
        }

        var sample = new Sample(channels > 1, data, frequency);

        LibCStdlib.free(data);

        return sample;
    }

    @Override
    public String toPath(String name) {
        return "/" + name + ".ogg";
    }

}
