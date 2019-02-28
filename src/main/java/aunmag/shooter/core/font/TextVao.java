package aunmag.shooter.core.font;

import aunmag.shooter.core.utilities.Operative;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class TextVao extends Operative {

    private final int id = GL30.glGenVertexArrays();
    private final int idVertices = GL15.glGenBuffers();
    private final int idTextureCoordinates = GL15.glGenBuffers();
    final int vertexCount;
    final String message;
    private float width = 0;
    final float height;

    TextVao(String message, FontStyle style) {
        this.message = message;

        List<Line> lines = createLines(style);
        List<Float> vertices = new ArrayList<>();
        List<Float> textureCoordinates = new ArrayList<>();

        float cursorX = 0f;
        float cursorY = 0f;

        for (Line line: lines) {
            List<Word> words = line.getWordsCopy();
            for (Word word: words) {
                List<Character> characters = word.getCharactersCopy();
                for (Character character: characters) {
                    addCharacterVertices(
                            style.size, cursorX, cursorY, character, vertices
                    );

                    textureCoordinates.addAll(
                            Arrays.asList(character.textureCoordinates)
                    );

                    cursorX += character.offsetAdvanceX * style.size;
                }
                cursorX += style.spaceWidth;
            }
            cursorX = 0;
            cursorY -= style.lineHeight;
        }

        bind();
        storeDataInAttributeList(idVertices, 0, vertices);
        storeDataInAttributeList(idTextureCoordinates, 1, textureCoordinates);
        unbind();

        vertexCount = vertices.size() / 2;
        height = Font.LINE_HEIGHT * style.size * lines.size();
    }

    private List<Line> createLines(FontStyle style) {
        List<Line> lines = new ArrayList<>();
        Word word = new Word(style.size);
        Line line = new Line(style.spaceWidth);

        for (int i = 0; i < message.length(); i++) {
            int ascii = message.charAt(i);
            int iNext = i + 1;
            boolean isEndText = message.length() == iNext;
            boolean isEndLine = isEndText || message.charAt(iNext) == '\n';
            boolean isEndWord = isEndLine || message.charAt(iNext) == ' ';

            if (ascii != ' ') {
                Character character = style.font.getCharacterByAscii(ascii);
                word.addCharacter(character);
            }

            if (isEndWord) {
                line.addWord(word);

                if (width < line.getWidth()) {
                    width = line.getWidth();
                }

                word = new Word(style.size);
            }

            if (isEndLine) {
                lines.add(line);
                line = new Line(style.spaceWidth);
            }
        }

        if (!line.isEmpty()) {
            lines.add(line);
        }

        return lines;
    }

    private void addCharacterVertices(
            float size,
            float cursorX,
            float cursorY,
            Character character,
            List<Float> vertices
    ) {
        final float aX = cursorX + size * character.offsetX;
        final float aY = cursorY - size * character.offsetY;
        final float bX = aX + size * character.sizeX;
        final float bY = aY - size * character.sizeY;

        vertices.add(aX);
        vertices.add(aY);
        vertices.add(aX);
        vertices.add(bY);
        vertices.add(bX);
        vertices.add(bY);
        vertices.add(bX);
        vertices.add(bY);
        vertices.add(bX);
        vertices.add(aY);
        vertices.add(aX);
        vertices.add(aY);
    }

    private void storeDataInAttributeList(int id, int attributeNumber, List<Float> data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.size());

        for (float value: data) {
            buffer.put(value);
        }

        buffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, 2, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    void bind() {
        if (isRemoved()) {
            return;
        }

        GL30.glBindVertexArray(id);
    }

    static void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void remove() {
        if (isRemoved()) {
            return;
        }

        GL30.glDeleteVertexArrays(id);
        GL15.glDeleteBuffers(idVertices);
        GL15.glDeleteBuffers(idTextureCoordinates);
        super.remove();
    }

    /* Getters */

    public float getWidth() {
        return width;
    }

}
