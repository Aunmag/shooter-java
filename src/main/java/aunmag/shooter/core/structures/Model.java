package aunmag.shooter.core.structures;

import aunmag.shooter.core.basics.BaseQuad;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private static List<Integer> allVerticesId = new ArrayList<>();
    private static List<Integer> allTexturesId = new ArrayList<>();
    private static List<Integer> allIndicesId = new ArrayList<>();

    private int drawQuantity;
    private int verticesId;
    private int texturesId;
    private int indicesId;

    public static Model createFromQuad(BaseQuad quad) {
        float[] vertices = new float[] {
                -quad.getCenterX(), +quad.getCenterY(), 0,
                -quad.getCenterX(), -quad.getCenterY(), 0,
                +quad.getCenterX(), -quad.getCenterY(), 0,
                +quad.getCenterX(), +quad.getCenterY(), 0,
        };

        float[] texture = new float[] {
                0, 0,
                0, 1,
                1, 1,
                1, 0,
        };

        int[] indices = new int[] {
                0, 1, 3,
                3, 1, 2
        };

        return new Model(vertices, texture, indices);
    }

    private static FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static IntBuffer createBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public Model(float[] vertices, float[] textureCoordinates, int[] indices) {
        drawQuantity = indices.length;

        verticesId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, verticesId);
        GL15.glBufferData(
                GL15.GL_ARRAY_BUFFER,
                createBuffer(vertices),
                GL15.GL_STATIC_DRAW
        );
        allVerticesId.add(verticesId);

        texturesId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texturesId);
        GL15.glBufferData(
                GL15.GL_ARRAY_BUFFER,
                createBuffer(textureCoordinates),
                GL15.GL_STATIC_DRAW
        );
        allTexturesId.add(texturesId);

        indicesId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesId);
        GL15.glBufferData(
                GL15.GL_ELEMENT_ARRAY_BUFFER,
                createBuffer(indices),
                GL15.GL_STATIC_DRAW
        );
        allIndicesId.add(indicesId);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public static void cleanUp() {
        for (int verticesId: allVerticesId) {
            GL15.glDeleteBuffers(verticesId);
        }

        for (int texturesId: allTexturesId) {
            GL15.glDeleteBuffers(texturesId);
        }

        for (int indicesId: allIndicesId) {
            GL15.glDeleteBuffers(indicesId);
        }
    }

    public void render() {
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, verticesId);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texturesId);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesId);
        GL11.glDrawElements(GL11.GL_TRIANGLES, drawQuantity, GL11.GL_UNSIGNED_INT, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
    }

}
