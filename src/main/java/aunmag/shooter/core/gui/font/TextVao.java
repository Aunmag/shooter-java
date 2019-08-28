package aunmag.shooter.core.gui.font;

import aunmag.shooter.core.utilities.Operative;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextVao extends Operative {

    private final int id = GL30.glGenVertexArrays();
    private final int idVertices = GL15.glGenBuffers();
    private final int idTextureCoordinates = GL15.glGenBuffers();
    public final String message;
    public final float sizeX;
    public final float sizeY;
    public final int vertices;

    public TextVao(String message, FontStyle style) {
        this.message = message;

        var vertices = new ArrayList<Float>();
        var textureCoordinates = new ArrayList<Float>();
        var cursorX = 0f;
        var cursorY = 0f;
        var sizeX = 0f;
        var lines = 1;

        for (var i = 0; i < message.length(); i++) {
            var ascii = message.charAt(i);

            if (ascii == ' ') {
                cursorX += style.spaceX;
            } else if (ascii == '\n') {
                cursorX = 0;
                cursorY -= style.spaceY;
                lines++;
            } else {
                var character = style.font.toCharacter(ascii);

                if (character != null) {
                    var x1 = cursorX + style.size * character.offsetX;
                    var y1 = cursorY - style.size * character.offsetY;
                    var x2 = x1 + style.size * character.sizeX;
                    var y2 = y1 - style.size * character.sizeY;

                    vertices.add(x1);
                    vertices.add(y1);
                    vertices.add(x1);
                    vertices.add(y2);
                    vertices.add(x2);
                    vertices.add(y2);
                    vertices.add(x2);
                    vertices.add(y2);
                    vertices.add(x2);
                    vertices.add(y1);
                    vertices.add(x1);
                    vertices.add(y1);

                    cursorX += character.offsetAdvance * style.size;
                    Collections.addAll(textureCoordinates, character.textureCoordinates);
                }
            }

            if (sizeX < cursorX) {
                sizeX = cursorX;
            }
        }

        bind();
        storeAttributeData(idVertices, 0, vertices);
        storeAttributeData(idTextureCoordinates, 1, textureCoordinates);
        unbind();

        this.sizeX = sizeX;
        this.sizeY = lines * style.spaceY;
        this.vertices = vertices.size() / 2;
    }

    private void storeAttributeData(int id, int attribute, List<Float> data) {
        var buffer = BufferUtils.createFloatBuffer(data.size());

        for (var value: data) {
            buffer.put(value);
        }

        buffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attribute, 2, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void bind() {
        if (isActive()) {
            GL30.glBindVertexArray(id);
        }
    }

    public static void unbind() {
        GL30.glBindVertexArray(0);
    }

    @Override
    protected void onRemove() {
        GL30.glDeleteVertexArrays(id);
        GL15.glDeleteBuffers(idVertices);
        GL15.glDeleteBuffers(idTextureCoordinates);
        super.onRemove();
    }

}
