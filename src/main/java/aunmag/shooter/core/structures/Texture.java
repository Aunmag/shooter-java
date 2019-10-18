package aunmag.shooter.core.structures;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.basics.BaseQuad;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.image.BufferedImage;

public class Texture extends BaseQuad {

    public static final TextureManager manager = new TextureManager();
    public static final Texture empty = new Texture(
        new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB),
        false,
        false,
        1,
        1
    );

    private int id;
    private Model model;

    protected Texture(
            BufferedImage bufferedImage,
            boolean isNearest,
            boolean useMipMapping,
            float modelSizeX,
            float modelSizeY
    ) {
        super(modelSizeX, modelSizeY);

        var width = bufferedImage.getWidth();
        var height = bufferedImage.getHeight();

        int[] pixelsRaw = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
        var pixelsBuffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                var pixel = pixelsRaw[i * width + j];
                pixelsBuffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                pixelsBuffer.put((byte) ((pixel >> 8) & 0xFF)); // Green
                pixelsBuffer.put((byte) (pixel & 0xFF)); // Blue
                pixelsBuffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
            }
        }

        pixelsBuffer.flip();

        id = GL11.glGenTextures();
        bind();

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
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    @Override
    public void render() {
        model.render();
    }

    public void renderOnWorld(float x, float y, float radians) {
        var camera = Application.getCamera();
        var projection = camera.toViewProjection(x, y, radians);
        Application.getShader().setUniformProjection(projection);

        bind();
        render();
    }

    @Override
    protected void onRemove() {
        GL11.glDeleteTextures(id);
        super.onRemove();
    }

    protected void setSize(float width, float height) {
        super.setSize(width, height);
        model = Model.createFromQuad(this);
    }

}
