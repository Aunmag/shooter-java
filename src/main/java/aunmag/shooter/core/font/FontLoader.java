package aunmag.shooter.core.font;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.structures.Texture;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FontLoader {

    private static final int PADDING = 3;

    private Map<String, String> values = new HashMap<>();
    private Map<Integer, Character> characters = new HashMap<>();

    private BufferedReader bufferedReader;
    private Texture texture;
    private final int textureAtlasWidth;
    private int paddingWidth;
    private int paddingHeight;
    private float paddingX;
    private float paddingY;
    private int paddingDoubleX;
    private int paddingDoubleY;
    private float lineStretchY;
    private float lineStretchX;
    private float spaceWidth;

    FontLoader(String name) {
        fileOpen(name);
        loadPadding();
        loadLineScales();
        textureAtlasWidth = parseVariable("scaleW");
        loadCharacters();
        fileClose();
        texture = Texture.getOrCreate("fonts/" + name, Texture.Type.FONT);
    }

    Font build() {
        return new Font(texture, characters, spaceWidth);
    }

    private void fileOpen(String name) {
        String path = "/fonts/" + name + ".fnt";

        try {
            InputStream inputStream = getClass().getResourceAsStream(path);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
            String message = String.format("Can't read font file at \"%s\"!", path);
            System.err.println(message);
        }
    }

    private void loadPadding() {
        loadNextLine();

        String[] paddingNumbers = values.get("padding").split(",");

        int paddingTop = Integer.parseInt(paddingNumbers[0]);
        int paddingLeft = Integer.parseInt(paddingNumbers[1]);
        int paddingBottom = Integer.parseInt(paddingNumbers[2]);
        int paddingRight = Integer.parseInt(paddingNumbers[3]);

        paddingWidth = paddingLeft + paddingRight;
        paddingHeight = paddingTop + paddingBottom;

        paddingX = paddingLeft - PADDING;
        paddingY = paddingTop - PADDING;
        paddingDoubleX = paddingWidth - PADDING * 2;
        paddingDoubleY = paddingHeight - PADDING * 2;
    }

    private void loadLineScales() {
        loadNextLine();
        int lineHeight = parseVariable("lineHeight") - paddingHeight;
        lineStretchY = Font.LINE_HEIGHT / (float) lineHeight;
        lineStretchX = lineStretchY / Application.getWindow().getAspectRatio();
    }

    private void loadCharacters() {
        loadNextLine();
        loadNextLine();
        loadNextLine();

        while (!values.isEmpty()) {
            Character character = loadCharacter();
            if (character != null) {
                characters.put(character.asciiId, character);
            }

            loadNextLine();
        }
    }

    private void loadNextLine() {
        values.clear();
        @Nullable String line;

        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            line = null;
        }

        if (line == null) {
            return;
        }

        for (String part: line.split(" ")) {
            String[] valuePairs = part.split("=");
            if (valuePairs.length == 2) {
                values.put(valuePairs[0], valuePairs[1]);
            }
        }
    }

    private int parseVariable(String variable) {
        return Integer.parseInt(values.get(variable));
    }

    @Nullable
    private Character loadCharacter() {
        int asciiId = parseVariable("id");

        if (asciiId == ' ') {
            this.spaceWidth = (parseVariable("xadvance") - paddingWidth) * lineStretchX;
            return null;
        }

        float x = (parseVariable("x") + paddingX) / textureAtlasWidth;
        float y = (parseVariable("y") + paddingY) / textureAtlasWidth;
        int width = parseVariable("width") - paddingDoubleX;
        int height = parseVariable("height") - paddingDoubleY;
        float textureSizeX = (float) width / textureAtlasWidth;
        float textureSizeY = (float) height / textureAtlasWidth;
        float offsetX = (parseVariable("xoffset") + paddingX) * lineStretchX;
        float offsetY = (parseVariable("yoffset") + paddingY) * lineStretchY;
        float offsetAdvanceX = (parseVariable("xadvance") - paddingWidth) * lineStretchX;
        float stretchX = width * lineStretchX;
        float stretchY = height * lineStretchY;

        return new Character(
                asciiId,
                x,
                y,
                textureSizeX,
                textureSizeY,
                offsetX,
                offsetY,
                offsetAdvanceX,
                stretchX,
                stretchY
        );
    }

    private void fileClose() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
