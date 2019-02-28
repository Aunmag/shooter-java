package aunmag.shooter.core.structures;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.Camera;
import aunmag.shooter.core.Configs;
import aunmag.shooter.core.basics.BaseQuad;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class Texture extends BaseQuad {

    public enum Type { SIMPLE, SPRITE, FONT, WALLPAPER, STRETCHED }
    private static HashMap<String, Texture> all = new HashMap<>();
    private int id;
    private Model model;

    public static Texture getOrCreate(String name, Type type) {
        return getOrCreate(name, type, null, null, null, null);
    }

    public static Texture getOrCreate(
            String name,
            Type type,
            @Nullable Boolean isNearest,
            @Nullable Boolean useMipMapping,
            @Nullable Float modelSizeX,
            @Nullable Float modelSizeY
    ) {
        if (all.containsKey(name)) {
            return all.get(name);
        }

        if (isNearest == null) {
            isNearest = type != Type.FONT;
        }

        if (useMipMapping == null) {
            useMipMapping = type == Type.SPRITE;
        }

        BufferedImage image = loadImage(name);

        if (modelSizeX == null || modelSizeY == null) {
            float sizeX = image.getWidth();
            float sizeY = image.getHeight();
            float widowSizeX = Application.getWindow().getWidth();
            float widowSizeY = Application.getWindow().getHeight();

            if (type == Type.SPRITE) {
                sizeX /= Configs.getPixelsPerMeter();
                sizeY /= Configs.getPixelsPerMeter();
            } else if (type == Type.STRETCHED) {
                sizeX = widowSizeX;
                sizeY = widowSizeY;
            } else if (type == Type.WALLPAPER) {
                float aspectRatio = sizeX / sizeY;

                if (aspectRatio < Application.getWindow().getAspectRatio()) {
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

        Texture texture = new Texture(
                image,
                isNearest,
                useMipMapping,
                modelSizeX,
                modelSizeY
        );
        all.put(name, texture);
        return texture;
    }

    private static BufferedImage loadImage(String name) {
        String path = "/" + name + ".png";
        BufferedImage bufferedImage;

        try {
            bufferedImage = ImageIO.read(Texture.class.getResourceAsStream(path));
        } catch (Exception e) {
            bufferedImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
            String message = String.format("Can't load image from \"%s\"!", path);
            System.err.println(message);
        }

        return bufferedImage;
    }

    protected Texture(
            BufferedImage bufferedImage,
            boolean isNearest,
            boolean useMipMapping,
            float modelSizeX,
            float modelSizeY
    ) {
        super(modelSizeX, modelSizeY);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int[] pixelsRaw = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
        ByteBuffer pixelsBuffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = pixelsRaw[i * width + j];
                pixelsBuffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                pixelsBuffer.put((byte) ((pixel >> 8) & 0xFF)); // Green
                pixelsBuffer.put((byte) (pixel & 0xFF)); // Blue
                pixelsBuffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
            }
        }

        pixelsBuffer.flip();

        id = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glTexParameterf(
                GL11.GL_TEXTURE_2D,
                GL11.GL_TEXTURE_MIN_FILTER,
                isNearest ? GL11.GL_NEAREST : GL11.GL_LINEAR
        );
        GL11.glTexParameterf(
                GL11.GL_TEXTURE_2D,
                GL11.GL_TEXTURE_MAG_FILTER,
                isNearest ? GL11.GL_NEAREST : GL11.GL_LINEAR
        );
        GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                GL11.GL_RGBA,
                width,
                height,
                0,
                GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE,
                pixelsBuffer
        );

        if (useMipMapping) {
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(
                    GL11.GL_TEXTURE_2D,
                    GL11.GL_TEXTURE_MIN_FILTER,
                    GL11.GL_NEAREST_MIPMAP_LINEAR
            );
        }
    }

    public void bind() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0); // TODO: Do I need this?
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    public void render() {
        model.render();
    }

    public void renderOnWorld(float x, float y, float radians) {
        Camera camera = Application.getCamera();
        Matrix4f projection = camera.calculateViewProjection(x, y, radians);
        Application.getShader().setUniformProjection(projection);
        Application.getShader().setUniformColourDefault();

        bind();
        render();
    }

    public static void cleanUp() {
        for (Texture texture: all.values()) {
            GL11.glDeleteTextures(texture.id);
        }
    }

    /* Setters */

    protected void setSize(float width, float height) {
        super.setSize(width, height);
        model = Model.createFromQuad(this);
    }

}
