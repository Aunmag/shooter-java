package aunmag.shooter.core.font;

import aunmag.shooter.core.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TextManager {

    private final Map<Font, List<Text>> texts = new HashMap<>();

    TextManager() {}

    void add(Text text) {
        List<Text> textsByFont = texts.get(text.style.font);
        if (textsByFont == null) {
            textsByFont = new ArrayList<>();
            texts.put(text.style.font, textsByFont);
        }

        textsByFont.add(text);
    }

    public void renderAll() {
        Application.getShader().bind();

        for (Font font: texts.keySet()) {
            font.texture.bind();
            List<Text> textsByFont = texts.get(font);
            List<Text> textsToDelete = new ArrayList<>();

            for (Text text: textsByFont) {
                if (text.isRemoved()) {
                    textsToDelete.add(text);
                } else {
                    text.render();
                }
            }

            textsByFont.removeAll(textsToDelete);
        }

        Application.getShader().setUniformColourDefault();
        TextVao.unbind();
    }

    public void removeAll() {
        for (Font font: texts.keySet()) {
            List<Text> textsByFont = texts.get(font);

            for (Text text: textsByFont) {
                text.remove();
            }

            textsByFont.clear();
        }
    }

    public int countTextsQuantity() {
        int quantity = 0;

        for (List<Text> textsByFont: texts.values()) {
            quantity += textsByFont.size();
        }

        return quantity;
    }

    public int getFontsQuantity() {
        return texts.size();
    }

}
