package aunmag.shooter.core.math;

import aunmag.shooter.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BodyLineTest {

    @Test
    void testUpdateRadians() {
        BodyLine line = new BodyLine(0, 0, 0, 0);
        assertEquals(0, line.radians);

        line.position.set(+1, 0);
        line.positionTail.set(-1, 0);
        line.updateRadians();
        assertEquals(0, line.radians);

        line.position.set(-1, 0);
        line.positionTail.set(+1, 0);
        line.updateRadians();
        assertEquals(Math.PI, line.radians, TestData.PRECISION);
    }

    @Test
    void testPullUpHead() {
        BodyLine line = new BodyLine(0, 0, 5, 5);
        assertEquals(0, line.position.x);
        assertEquals(0, line.position.y);
        assertEquals(5, line.positionTail.x);
        assertEquals(5, line.positionTail.y);

        line.pullUpHead();
        assertEquals(5, line.position.x);
        assertEquals(5, line.position.y);
        assertEquals(5, line.positionTail.x);
        assertEquals(5, line.positionTail.y);
    }

    @Test
    void testPullUpTail() {
        BodyLine line = new BodyLine(5, 5, 0, 0);
        assertEquals(5, line.position.x);
        assertEquals(5, line.position.y);
        assertEquals(0, line.positionTail.x);
        assertEquals(0, line.positionTail.y);

        line.pullUpTail();
        assertEquals(5, line.position.x);
        assertEquals(5, line.position.y);
        assertEquals(5, line.positionTail.x);
        assertEquals(5, line.positionTail.y);
    }

}
