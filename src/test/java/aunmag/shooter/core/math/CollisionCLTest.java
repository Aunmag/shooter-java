package aunmag.shooter.core.math;

import aunmag.shooter.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollisionCLTest {

    @Test
    void testIsTrueWithVerticalCross() {
        BodyCircle circle = new BodyCircle(0, 0, 0, 1);
        BodyLine line = new BodyLine(0, 5, 0, -5);
        assertTrue(new CollisionCL(circle, line).isTrue());

        line.position.add(0.5f, 0);
        line.positionTail.add(0.5f, 0);
        assertTrue(new CollisionCL(circle, line).isTrue());

        line.position.add(1, 0);
        line.positionTail.add(1, 0);
        assertFalse(new CollisionCL(circle, line).isTrue());
    }

    @Test
    void testIsTrueWithDiagonalCross() {
        BodyCircle circle = new BodyCircle(0, 0, 0, 1);
        BodyLine line = new BodyLine(5, 5, -5, -5);
        assertTrue(new CollisionCL(circle, line).isTrue());

        line.position.add(0.5f, 0);
        line.positionTail.add(0.5f, 0);
        assertTrue(new CollisionCL(circle, line).isTrue());

        line.position.add(1, 0);
        line.positionTail.add(1, 0);
        assertFalse(new CollisionCL(circle, line).isTrue());
    }

    @Test
    void testIsTrueWithTouch() {
        BodyCircle circle = new BodyCircle(0, 0, 0, 1);
        BodyLine line = new BodyLine(0, +5, 0, -5);
        assertTrue(new CollisionCL(circle, line).isTrue());

        line.position.add(1, 0);
        line.positionTail.add(1, 0);
        assertFalse(new CollisionCL(circle, line).isTrue());

        line.position.sub(TestData.PRECISION, 0);
        line.positionTail.sub(TestData.PRECISION, 0);
        assertTrue(new CollisionCL(circle, line).isTrue());
    }

    @Test
    void testIsTrueWithinCircle() {
        BodyCircle circle = new BodyCircle(0, 0, 0, 10);
        BodyLine line = new BodyLine(+1, +1, -1, -1);
        assertTrue(new CollisionCL(circle, line).isTrue());
    }

    @Test
    void testIsTrueWithZeros() {
        BodyCircle circle = new BodyCircle(0, 0, 0, 0);
        BodyLine line = new BodyLine(0, 0, 0, 0);
        assertFalse(new CollisionCL(circle, line).isTrue());

        circle.radius = 1;
        assertTrue(new CollisionCL(circle, line).isTrue());
    }

}
