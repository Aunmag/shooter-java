package aunmag.shooter.core.gui.font;

public class Character {

    public final int ascii;
    public final float offsetX;
    public final float offsetY;
    public final float offsetAdvance;
    public final float sizeX;
    public final float sizeY;
    public final Float[] textureCoordinates;

    public Character(
            int ascii,
            float x1,
            float y1,
            float textureSizeX,
            float textureSizeY,
            float offsetX,
            float offsetY,
            float offsetAdvance,
            float sizeX,
            float sizeY
    ) {
        this.ascii = ascii;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetAdvance = offsetAdvance;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        float x2 = x1 + textureSizeX;
        float y2 = y1 + textureSizeY;

        textureCoordinates = new Float[] {
            x1, y1,
            x1, y2,
            x2, y2,
            x2, y2,
            x2, y1,
            x1, y1,
        };
    }

}
