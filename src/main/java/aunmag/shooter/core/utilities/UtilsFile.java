package aunmag.shooter.core.utilities;

import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public final class UtilsFile {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    private UtilsFile() {}

    public static Path toPath(String path, Class cls) throws IOException {
        try {
            return Paths.get(cls.getResource(path).toURI());
        } catch (URISyntaxException | NullPointerException e) {
            throw new IOException(e);
        }
    }

    public static Path toPath(String path) throws IOException {
        return toPath(path, UtilsFile.class);
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

    public static String read(String path, Class cls) throws IOException {
        return new String(Files.readAllBytes(toPath(path, cls)), CHARSET);
    }

    public static String read(String path) throws IOException {
        return read(path, UtilsFile.class);
    }

    public static void readByLine(
            InputStream stream,
            Consumer<String> consumer
    ) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(stream, CHARSET));

        while (true) {
            var line = reader.readLine();

            if (line == null) {
                break;
            }

            consumer.accept(line);
        }

        reader.close();
    }

    public static void readByLine(
            String path,
            Consumer<String> consumer
    ) throws IOException {
        readByLine(UtilsFile.class.getResourceAsStream(path), consumer);
    }

    public static void printReadError(String path) {
        System.err.println(String.format("Failed to read file from \"%s\"", path));
    }

}
