package aunmag.shooter.core.gui.font;

import aunmag.shooter.core.structures.Texture;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Font {

    private static final Map<String, Font> ALL = new HashMap<>();
    public static final Font DEFAULT = provide("ubuntu");
    public static final Font TITLE = provide("ubuntu-bold");
    public static final Font EMPTY = new Font(
            new HashMap<>(), Texture.createEmpty(), 0, 0
    );

    public static Font provide(String name) {
        return ALL.computeIfAbsent(name, n -> {
            var font = EMPTY;

            try {
                font = new FontLoader().load(n);
            } catch (Exception e) {
                System.err.println(String.format("Failed to load \"%s\" font!", n));
                e.printStackTrace();
            }

            return font;
        });
    }

    private final Map<Integer, Character> characters;
    public final Texture texture;
    public final float spaceX;
    public final float spaceY;

    public Font(
            Map<Integer, Character> characters,
            Texture texture,
            float spaceX,
            float spaceY
    ) {
        this.characters = Collections.unmodifiableMap(characters);
        this.texture = texture;
        this.spaceX = spaceX;
        this.spaceY = spaceY;
    }

    @Nullable
    public Character toCharacter(int ascii) {
        return characters.get(ascii);
    }

}
