package aunmag.shooter.core.utilities;

import org.joml.Vector4f;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsLanguageTest {

    @Test
    void testListToArray() {
        // TODO: Use collection factory methods when Java 9 will be used:
        List<Float> list = new ArrayList<>();
        list.add(3f);
        list.add(5f);
        list.add(1f);

        assertArrayEquals(new float[]{3, 5, 1}, UtilsLanguage.listToArray(list));
    }

    @Test
    void testColorToVector() {
        Vector4f result = UtilsLanguage.colorToVector(new Color(255, 127, 63, 127));
        assertEquals(1.0f, result.x());
        assertEquals(0.5f, result.y());
        assertEquals(0.25f, result.z());
        assertEquals(0.5f, result.w());
    }

}
