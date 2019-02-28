package aunmag.shooter.core.utilities;

import org.junit.jupiter.api.Test;

import static aunmag.shooter.TestData.PRECISION;
import static aunmag.shooter.core.utilities.UtilsMath.PIx0_5;
import static aunmag.shooter.core.utilities.UtilsMath.PIx1_5;
import static aunmag.shooter.core.utilities.UtilsMath.PIx2;
import static aunmag.shooter.core.utilities.UtilsMath.calculateDistanceBetween;
import static aunmag.shooter.core.utilities.UtilsMath.calculateIsNumberInsideRange;
import static aunmag.shooter.core.utilities.UtilsMath.correctRadians;
import static aunmag.shooter.core.utilities.UtilsMath.limitNumber;
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
    void testCalculateDistanceBetween() {
        assertEquals(0, calculateDistanceBetween(0, 0, 0, 0));

        float n = 312346.848729800778264f;
        assertEquals(n, calculateDistanceBetween(n, 0, 0, 0));
        assertEquals(n, calculateDistanceBetween(0, n, 0, 0));
        assertEquals(n, calculateDistanceBetween(0, 0, n, 0));
        assertEquals(n, calculateDistanceBetween(0, 0, 0, n));
        assertEquals(n, calculateDistanceBetween(0, 0, 0, -n));
        assertEquals(n * 2, calculateDistanceBetween(0, n, 0, -n), PRECISION);

        assertEquals(
                33.203915431767984,
                calculateDistanceBetween(548.25f, -1.5f, 537.75f, 30),
                PRECISION
        );
    }

    @Test
    void testCalculateIsNumberInsideRange() {
        assertTrue(calculateIsNumberInsideRange(0, 0, 0));
        assertTrue(calculateIsNumberInsideRange(1, 1, 1));
        assertTrue(calculateIsNumberInsideRange(1, -1, 1));
        assertTrue(calculateIsNumberInsideRange(-1, -1, 1));
        assertTrue(calculateIsNumberInsideRange(0.5f, 0, 1));

        assertFalse(calculateIsNumberInsideRange(1, 0, 0));
        assertFalse(calculateIsNumberInsideRange(1, -1, 0));
        assertFalse(calculateIsNumberInsideRange(0.5f, -1, 0));
    }

    @Test
    void testLimitNumber() {
        assertEquals(0, limitNumber(0, 0, 0));
        assertEquals(0, limitNumber(0, 0, 1));
        assertEquals(1, limitNumber(0, 1, 1));
        assertEquals(-1, limitNumber(-1.5f, -1, 1));
        assertEquals(+1, limitNumber(+1.5f, -1, 1));
    }

}
