package aunmag.shooter.core.math;

import aunmag.shooter.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollisionCCTest {

    private BodyCircle a = new BodyCircle(0, 0, 0, 1);
    private BodyCircle b = new BodyCircle(0, 0, 0, 1);

    @Test
    void testIsTrue() {
        a.position.set(0, 0);
        b.position.set(0, 0);
        assertTrue(new CollisionCC(a, b).isTrue());

        a.position.set(0, 0);
        b.position.set(0, 2 - TestData.PRECISION);
        assertTrue(new CollisionCC(a, b).isTrue());

        a.position.set(0, 0);
        b.position.set(0, 2);
        assertFalse(new CollisionCC(a, b).isTrue());

        a.position.set(0, 0);
        b.position.set(0, 100);
        assertFalse(new CollisionCC(a, b).isTrue());
    }

    @Test
    void testIsWithZeros() {
        BodyCircle a = new BodyCircle(0, 0, 0, 0);
        BodyCircle b = new BodyCircle(0, 0, 0, 0);
        assertFalse(new CollisionCC(a, b).isTrue());

        b.radius = 1;
        assertTrue(new CollisionCC(a, b).isTrue());
    }

    @Test
    void testResolve() {
        a.position.set(5, 0);
        b.position.set(0, 0);
        CollisionCC collision = new CollisionCC(a, b);
        assertFalse(collision.isTrue());
        collision.resolve();
        assertFalse(new CollisionCC(a, b).isTrue());
        assertEquals(5, a.position.x);
        assertEquals(0, a.position.y);
        assertEquals(0, b.position.x);
        assertEquals(0, b.position.y);

        a.position.set(2, 0);
        b.position.set(3, 0);
        collision = new CollisionCC(a, b);
        assertTrue(collision.isTrue());
        collision.resolve();
        assertFalse(new CollisionCC(a, b).isTrue());
        assertEquals(1.5, a.position.x, TestData.PRECISION);
        assertEquals(0.0, a.position.y, TestData.PRECISION);
        assertEquals(3.5, b.position.x, TestData.PRECISION);
        assertEquals(0.0, b.position.y, TestData.PRECISION);

        a.position.set(5, 0);
        b.position.set(5, 0);
        collision = new CollisionCC(a, b);
        assertTrue(collision.isTrue());
        collision.resolve();
        assertFalse(new CollisionCC(a, b).isTrue());
        assertEquals(6, a.position.x);
        assertEquals(0, a.position.y);
        assertEquals(4, b.position.x);
        assertEquals(0, b.position.y);
    }

    @Test
    void testResolveWithDifferentRadius() {
        BodyCircle a = new BodyCircle(10, 0, 0, 1);
        BodyCircle b = new BodyCircle(10, 0, 0, 3);

        CollisionCC collision = new CollisionCC(a, b);
        assertTrue(collision.isTrue());

        collision.resolve();
        assertFalse(new CollisionCC(a, b).isTrue());
        assertEquals(12, a.position.x);
        assertEquals(0, a.position.y);
        assertEquals(8, b.position.x);
        assertEquals(0, b.position.y);
    }

}
