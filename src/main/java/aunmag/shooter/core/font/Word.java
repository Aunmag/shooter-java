package aunmag.shooter.core.font;

import java.util.ArrayList;
import java.util.List;

class Word {

    private List<Character> characters = new ArrayList<>();
    private final float fontSize;
    private float width;

    Word(float fontSize) {
        this.fontSize = fontSize;
    }

    void addCharacter(Character character) {
        characters.add(character);
        width += character.offsetAdvanceX * fontSize;
    }

    /* Getters */

    List<Character> getCharactersCopy() {
        return new ArrayList<>(characters);
    }

    public float getWidth() {
        return width;
    }

}
