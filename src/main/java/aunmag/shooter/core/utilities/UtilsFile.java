package aunmag.shooter.core.utilities;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class UtilsFile {

    private UtilsFile() {}

    public static Path toPath(String path) throws IOException {
        try {
            return Paths.get(UtilsFile.class.getResource(path).toURI());
        } catch (URISyntaxException | NullPointerException e) {
            throw new IOException(e.getMessage(), e.getCause());
        }
    }

    public static ByteBuffer readByteBuffer(String path) throws IOException {
        var channel = Files.newByteChannel(toPath(path));
        var buffer = BufferUtils.createByteBuffer((int) channel.size() + 1);

        while (true) {
            if (channel.read(buffer) == -1) {
                break;
            }
        }

        channel.close();
        buffer.flip();

        return buffer;
    }

    public static void printReadError(String path) {
        System.err.println(String.format("Failed to read file from \"%s\"", path));
    }

}
