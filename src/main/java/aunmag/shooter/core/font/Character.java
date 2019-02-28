package aunmag.shooter.core.font;

class Character {

    final int asciiId;
    final float offsetX;
    final float offsetY;
    final float offsetAdvanceX;
    final float sizeX;
    final float sizeY;
    final Float[] textureCoordinates;

    Character(
            int asciiId,
            float x1,
            float y1,
            float textureSizeX,
            float textureSizeY,
            float offsetX,
            float offsetY,
            float offsetAdvanceX,
            float sizeX,
            float sizeY
    ) {
        this.asciiId = asciiId;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetAdvanceX = offsetAdvanceX;
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
