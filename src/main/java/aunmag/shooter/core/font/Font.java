package aunmag.shooter.core.font;

import aunmag.shooter.core.structures.Texture;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Font {

    private static HashMap<String, Font> all = new HashMap<>();
    static final float LINE_HEIGHT = 0.03f;
    public static final Font fontTitle = Font.getOrCreate("ubuntu-bold");
    public static final Font fontDefault = Font.getOrCreate("ubuntu");

    public static Font getOrCreate(String name) {
        if (all.containsKey(name)) {
            return all.get(name);
        } else {
            Font font = new FontLoader(name).build();
            all.put(name, font);
            return font;
        }
    }

    private final Map<Integer, Character> characters;
    final Texture texture;
    final float spaceWidth;

    Font(Texture texture, Map<Integer, Character> characters, float spaceWidth) {
        this.texture = texture;
        this.characters = Collections.unmodifiableMap(characters);
        this.spaceWidth = spaceWidth;
    }

    Character getCharacterByAscii(int ascii) {
        return characters.get(ascii);
    }

}
