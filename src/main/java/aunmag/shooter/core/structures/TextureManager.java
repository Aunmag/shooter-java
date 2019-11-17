package aunmag.shooter.core.structures;

import aunmag.shooter.core.Configs;
import aunmag.shooter.core.Context;
import aunmag.shooter.core.utilities.ResourceManager;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.io.IOException;

public class TextureManager extends ResourceManager<Texture> {

    public enum Type { SIMPLE, SPRITE, FONT, WALLPAPER, COVER }
    @Nullable private Type withType = null;
    @Nullable private Float withModelSizeX = null;
    @Nullable private Float withModelSizeY = null;

    public TextureManager asSprite() {
        return withType(Type.SPRITE);
    }

    public TextureManager asFont() {
        return withType(Type.FONT);
    }

    public TextureManager asWallpaper() {
        return withType(Type.WALLPAPER);
    }

    public TextureManager asCover() {
        return withType(Type.COVER);
    }

    public TextureManager withType(Type withType) {
        this.withType = withType;
        return this;
    }

    public TextureManager withModelSize(float withModelSizeX, float withModelSizeY) {
        this.withModelSizeX = withModelSizeX;
        this.withModelSizeY = withModelSizeY;
        return this;
    }

    @Override
    public Texture load(String path) throws IOException {
        var image = ImageIO.read(TextureManager.class.getResourceAsStream(path));

        var type = withType;
        if (type == null) {
            type = Type.SIMPLE;
        }

        var modelSizeX = withModelSizeX;
        var modelSizeY = withModelSizeY;

        if (modelSizeX == null || modelSizeY == null) {
            float sizeX = image.getWidth();
            float sizeY = image.getHeight();
            float widowSizeX = Context.main.getWindow().getSizeX();
            float widowSizeY = Context.main.getWindow().getSizeY();

            if (type == Type.SPRITE) {
                sizeX /= Configs.getPixelsPerMeter();
                sizeY /= Configs.getPixelsPerMeter();
            } else if (type == Type.COVER) {
                sizeX = widowSizeX;
                sizeY = widowSizeY;
            } else if (type == Type.WALLPAPER) {
                var aspectRatio = sizeX / sizeY;

                if (aspectRatio < Context.main.getWindow().getAspectRatio()) {
                    sizeX = widowSizeX;
                    sizeY = sizeX / aspectRatio;
                } else {
                    sizeY = widowSizeY;
                    sizeX = sizeY * aspectRatio;
                }
            }

            modelSizeX = sizeX;
            modelSizeY = sizeY;
        }

        return new Texture(
                image,
                type != Type.FONT,
                type == Type.SPRITE,
                modelSizeX,
                modelSizeY
        );
    }

    @Override
    public void resetSettings() {
        super.resetSettings();

        withType = null;
        withModelSizeX = null;
        withModelSizeY = null;
    }

    @Override
    public String toPath(String name) {
        return "/" + name + ".png";
    }

}
