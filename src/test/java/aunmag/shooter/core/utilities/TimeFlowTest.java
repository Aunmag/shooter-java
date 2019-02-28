package aunmag.shooter.core.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeFlowTest {

    @Test
    void testInitialize() {
        TimeFlow time = new TimeFlow();
        assertEquals(0.0, time.getCurrent());
        assertEquals(0.0, time.getDelta());
        assertEquals(1.0, time.getSpeed());
    }

    @Test
    void testAdd() {
        TimeFlow time = new TimeFlow();
        assertEquals(0.0, time.getCurrent());
        assertEquals(0.0, time.getDelta());

        time.add(1.0, true);
        assertEquals(1.0, time.getCurrent());
        assertEquals(1.0, time.getDelta());

        time.add(1.5, true);
        assertEquals(2.5, time.getCurrent());
        assertEquals(1.5, time.getDelta());

        time.add(-2.0, true);
        assertEquals(0.5, time.getCurrent());
        assertEquals(-2.0, time.getDelta());
    }

    @Test
    void testSetCurrent() {
        TimeFlow time = new TimeFlow();

        time.setCurrent(1.5, true);
        assertEquals(1.5, time.getCurrent());
        assertEquals(1.5, time.getDelta());

        time.setCurrent(1.0, true);
        assertEquals(1.0, time.getCurrent());
        assertEquals(-0.5, time.getDelta());

        time.setCurrent(-1.0, true);
        assertEquals(-1.0, time.getCurrent());
        assertEquals(-2.0, time.getDelta());

        time.setCurrent(2.0, false);
        assertEquals(2.0, time.getCurrent());
        assertEquals(0.0, time.getDelta());

        time.setCurrent(0.5, false);
        assertEquals(0.5, time.getCurrent());
        assertEquals(0.0, time.getDelta());
    }

    @Test
    void testSetSpeed() {
        TimeFlow time = new TimeFlow();

        time.add(1.0, true);
        assertEquals(1.0, time.getCurrent());
        assertEquals(1.0, time.getDelta());
        assertEquals(1.0, time.getSpeed());

        time.setCurrent(0.0, false);
        time.setSpeed(2.0);
        time.add(1.0, true);
        assertEquals(2.0, time.getCurrent());
        assertEquals(2.0, time.getDelta());
        assertEquals(2.0, time.getSpeed());

        time.setCurrent(0.0, false);
        time.setSpeed(2.0);
        time.add(1.0, false);
        assertEquals(1.0, time.getCurrent());
        assertEquals(1.0, time.getDelta());
        assertEquals(2.0, time.getSpeed());

        time.setCurrent(0.0, false);
        time.setSpeed(-0.5);
        time.add(3.0, true);
        assertEquals(-1.5, time.getCurrent());
        assertEquals(-1.5, time.getDelta());
        assertEquals(-0.5, time.getSpeed());
    }

}
