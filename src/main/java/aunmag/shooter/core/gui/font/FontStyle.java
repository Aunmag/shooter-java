package aunmag.shooter.core.gui.font;

public class FontStyle {

    public static final FontStyle LABEL = new FontStyle(Font.TITLE, 3, true);
    public static final FontStyle LABEL_LIGHT = new FontStyle(Font.DEFAULT, 2, true);
    public static final FontStyle SIMPLE = new FontStyle(Font.DEFAULT, 2, false);

    public final Font font;
    public final float size;
    public final float spaceX;
    public final float spaceY;
    public final boolean isCentred;

    public FontStyle(Font font, float size, boolean isCentred) {
        this.font = font;
        this.size = size;
        this.spaceX = font.spaceX * size;
        this.spaceY = font.spaceY * size;
        this.isCentred = isCentred;
    }

}
