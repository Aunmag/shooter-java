package aunmag.shooter.core.utilities;

import org.junit.jupiter.api.Test;

import static aunmag.shooter.TestData.PRECISION;
import static aunmag.shooter.core.utilities.UtilsMath.PIx0_5;
import static aunmag.shooter.core.utilities.UtilsMath.PIx1_5;
import static aunmag.shooter.core.utilities.UtilsMath.PIx2;
import static aunmag.shooter.core.utilities.UtilsMath.distance;
import static aunmag.shooter.core.utilities.UtilsMath.inRange;
import static aunmag.shooter.core.utilities.UtilsMath.correctRadians;
import static aunmag.shooter.core.utilities.UtilsMath.limit;
import static aunmag.shooter.core.utilities.UtilsMath.radiansDifference;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsMathTest {

    @Test
    void testCorrectRadians() {
        assertEquals(0, correctRadians(0));
        assertEquals(1, correctRadians(1));
        assertEquals(-1, correctRadians(-1));
        assertEquals(Math.PI, correctRadians(Math.PI), PRECISION);
        assertEquals(0, correctRadians(Math.PI * 2));
        assertEquals(0, correctRadians(Math.PI * 4));

        double radians = Math.PI * 2.0;
        assertEquals(0.1, correctRadians(radians + 0.1), PRECISION);
        assertEquals(radians - 0.1, correctRadians(radians - 0.1), PRECISION);
    }

    @Test
    void testRadiansDifference() {
        assertEquals(0, radiansDifference(0, 0), PRECISION);
        assertEquals(0, radiansDifference(Math.PI, Math.PI));
        assertEquals(0, radiansDifference(PIx2, PIx2 * 6f), PRECISION);

        assertEquals(+PIx0_5, radiansDifference(PIx1_5, 0), PRECISION);
        assertEquals(-PIx0_5, radiansDifference(0, PIx1_5), PRECISION);
    }

    @Test
    void testDistance() {
        assertEquals(0, distance(0, 0, 0, 0));

        float n = 312346.848729800778264f;
        assertEquals(n, distance(n, 0, 0, 0));
        assertEquals(n, distance(0, n, 0, 0));
        assertEquals(n, distance(0, 0, n, 0));
        assertEquals(n, distance(0, 0, 0, n));
        assertEquals(n, distance(0, 0, 0, -n));
        assertEquals(n * 2, distance(0, n, 0, -n), PRECISION);

        assertEquals(
                33.203915431767984,
                distance(548.25f, -1.5f, 537.75f, 30),
                PRECISION
        );
    }

    @Test
    void testInRange() {
        assertTrue(inRange(0, 0, 0));
        assertTrue(inRange(1, 1, 1));
        assertTrue(inRange(1, -1, 1));
        assertTrue(inRange(-1, -1, 1));
        assertTrue(inRange(0.5f, 0, 1));

        assertFalse(inRange(1, 0, 0));
        assertFalse(inRange(1, -1, 0));
        assertFalse(inRange(0.5f, -1, 0));
    }

    @Test
    void testLimit() {
        assertEquals(0, limit(0, 0, 0));
        assertEquals(0, limit(0, 0, 1));
        assertEquals(1, limit(0, 1, 1));
        assertEquals(-1, limit(-1.5f, -1, 1));
        assertEquals(+1, limit(+1.5f, -1, 1));
    }

}
