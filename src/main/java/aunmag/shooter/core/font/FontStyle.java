package aunmag.shooter.core.font;

public final class FontStyle {

    final Font font;
    final float size;
    final float spaceWidth;
    final float lineHeight;
    final boolean isCentred;

    public FontStyle(Font font, float size, boolean isCentred) {
        this.font = font;
        this.size = size;
        this.isCentred = isCentred;

        spaceWidth = font.spaceWidth * size;
        lineHeight = Font.LINE_HEIGHT * size;
    }

}
