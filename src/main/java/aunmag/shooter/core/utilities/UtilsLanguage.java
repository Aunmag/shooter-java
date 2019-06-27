package aunmag.shooter.core.utilities;

import org.joml.Vector4f;

import java.awt.*;
import java.util.List;

public final class UtilsLanguage {

    private UtilsLanguage() {}

    public static float[] toArray(List<Float> list) {
        float[] array = new float[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    public static Vector4f toVector(Color color) {
        Vector4f vector = new Vector4f(
                1f + color.getRed(),
                1f + color.getGreen(),
                1f + color.getBlue(),
                1f + color.getAlpha()
        );

        return vector.div(256);
    }

}
