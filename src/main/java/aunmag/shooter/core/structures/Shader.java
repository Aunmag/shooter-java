package aunmag.shooter.core.structures;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;
import org.joml.Vector4fc;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Shader {

    private static List<Shader> all = new ArrayList<>();

    private final int id;
    private final int programVertexId;
    private final int programFragmentId;

    @Nullable
    private static String readFile(Class resourceClass, String type) {
        String filename = resourceClass.getSimpleName() + '.' + type;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            InputStream inputStream = resourceClass.getResourceAsStream(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }

            bufferedReader.close();
        } catch (IOException | NullPointerException ex) {
            String message = String.format(
                    "Can't find shader \"%s\" near \"%s\" class!",
                    filename,
                    resourceClass.getName()
            );
            System.err.println(message);
            return null;
        }

        return stringBuilder.toString();
    }

    protected Shader(Class resourceClass) {
        String programVertex = readFile(resourceClass, "vert");
        String programFragment = readFile(resourceClass, "frag");

        if (programVertex == null || programFragment == null) {
            id = 0;
            programVertexId = 0;
            programFragmentId = 0;
            return;
        } else {
            id = GL20.glCreateProgram();
            programVertexId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
            programFragmentId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        }

        GL20.glShaderSource(programVertexId, programVertex);
        GL20.glCompileShader(programVertexId);
        if (GL20.glGetShaderi(programVertexId, GL20.GL_COMPILE_STATUS) != 1) {
            System.err.println(GL20.glGetShaderInfoLog(programVertexId));
            System.exit(1);
        }

        GL20.glShaderSource(programFragmentId, programFragment);
        GL20.glCompileShader(programFragmentId);
        if (GL20.glGetShaderi(programFragmentId, GL20.GL_COMPILE_STATUS) != 1) {
            System.err.println(GL20.glGetShaderInfoLog(programFragmentId));
            System.exit(1);
        }

        GL20.glAttachShader(id, programVertexId);
        GL20.glAttachShader(id, programFragmentId);

        GL20.glBindAttribLocation(id, 0, "vertices");
        GL20.glBindAttribLocation(id, 1, "textures");

        GL20.glLinkProgram(id);
        if (GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) != 1) {
            System.err.println(GL20.glGetProgramInfoLog(id));
            System.exit(1);
        }

        GL20.glValidateProgram(id);
        if (GL20.glGetProgrami(id, GL20.GL_VALIDATE_STATUS) != 1) {
            System.err.println(GL20.glGetProgramInfoLog(id));
            System.exit(1);
        }

        all.add(this);
    }

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(id, uniformName);
    }

    public void bind() {
        GL20.glUseProgram(id);
    }

    public static void cleanUp() {
        GL20.glUseProgram(0);

        for (Shader shader: all) {
            GL20.glDetachShader(shader.id, shader.programVertexId);
            GL20.glDetachShader(shader.id, shader.programFragmentId);
            GL20.glDeleteShader(shader.programVertexId);
            GL20.glDeleteShader(shader.programFragmentId);
            GL20.glDeleteProgram(shader.id);
        }
    }

    /* Setters */

    protected static void setUniform(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    protected static void setUniform(int location, float x, float y, float z, float w) {
        GL20.glUniform4f(location, x, y, z, w);
    }

    protected static void setUniform(int location, Vector4fc vector) {
        setUniform(location, vector.x(), vector.y(), vector.z(), vector.w());
    }

    protected static void setUniform(int location, Matrix4fc matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);
        GL20.glUniformMatrix4fv(location, false, buffer);
    }

}
