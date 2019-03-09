package aunmag.shooter.core.gui.font;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.structures.Texture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FontLoader {

    private static final float LINE_HEIGHT = 0.03f;
    private Map<Integer, Character> characters = new HashMap<>();
    private Map<String, String> meta = new HashMap<>();
    private List<HashMap<String, String>> charactersMeta = new ArrayList<>();

    public Font load(String name) throws Exception {
        read(name);

        var padding = meta.get("padding").split(",");
        var paddingX = toFloat(padding[1]) + toFloat(padding[3]);
        var paddingY = toFloat(padding[0]) + toFloat(padding[2]);

        var lineHeight = toFloat(meta.get("lineHeight")) - paddingY;
        var lineStretchY = LINE_HEIGHT / lineHeight;
        var lineStretchX = lineStretchY / Application.getWindow().getAspectRatio();

        var textureScale = toFloat(meta.get("scaleW"));
        var spaceWidth = 0f;

        for (var character : charactersMeta) {
            var ascii = toInt(character.get("id"));
            var sizeAdvance = toFloat(character.get("xadvance")) - paddingX;

            if (ascii == ' ') {
                spaceWidth = lineStretchX * sizeAdvance;
            } else {
                var sizeX = toFloat(character.get("width"));
                var sizeY = toFloat(character.get("height"));

                characters.put(ascii, new Character(
                        ascii,
                        toFloat(character.get("x")) / textureScale,
                        toFloat(character.get("y")) / textureScale,
                        sizeX / textureScale,
                        sizeY / textureScale,
                        lineStretchX * toFloat(character.get("xoffset")),
                        lineStretchY * toFloat(character.get("yoffset")),
                        lineStretchX * sizeAdvance,
                        sizeX * lineStretchX,
                        sizeY * lineStretchY
                ));
            }
        }

        return new Font(
                characters,
                Texture.getOrCreate("fonts/" + name, Texture.Type.FONT),
                spaceWidth,
                LINE_HEIGHT
        );
    }

    private void read(String name) throws IOException {
        var reader = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/fonts/" + name + ".fnt"),
                        StandardCharsets.UTF_8
                )
        );

        while (true) {
            var line = reader.readLine();

            if (line == null) {
                break;
            }

            var type = (String) null;
            var data = new HashMap<String, String>();

            for (var column: line.split(" ")) {
                if (type == null) {
                    type = column;
                } else {
                    var property = column.split("=");

                    if (property.length == 2) {
                        data.put(property[0], property[1]);
                    }
                }
            }

            if (type != null) {
                if (type.equals("char")) {
                    charactersMeta.add(data);
                } else {
                    meta.putAll(data);
                }
            }
        }

        reader.close();
    }

    private int toInt(String string) {
        return Integer.parseInt(string);
    }

    private float toFloat(String string) {
        return Float.parseFloat(string);
    }

}
