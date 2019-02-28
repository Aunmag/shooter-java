package aunmag.shooter.core.utilities;

import aunmag.shooter.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FrameRateTest {

    @Test
    void testInitialize() {
        FrameRate frameRate = new FrameRate(60);
        assertEquals(60, frameRate.getFrequency());
        assertEquals(1.0 / 60.0, frameRate.getDeltaMin());
    }

    @Test
    void testSetFrequency() {
        FrameRate frameRate = new FrameRate(60);

        frameRate.setFrequency(10);
        assertEquals(10, frameRate.getFrequency());
        assertEquals(0.1, frameRate.getDeltaMin());

        frameRate.setFrequency(1);
        assertEquals(1, frameRate.getFrequency());
        assertEquals(1.0, frameRate.getDeltaMin());

        frameRate.setFrequency(0);
        assertEquals(0, frameRate.getFrequency());
        assertEquals(0.0, frameRate.getDeltaMin());

        frameRate.setFrequency(-10);
        assertEquals(-10, frameRate.getFrequency());
        assertEquals(-0.1, frameRate.getDeltaMin());
    }

    @Test
    void testTryTick() {
        FrameRate frameRate = new FrameRate(10);
        assertEquals(0.0, frameRate.getDelta());
        assertEquals(0.1, frameRate.getDeltaMin());
        assertEquals(0.0, frameRate.getLastTickTime());

        double timeCurrent = 0.0;
        assertFalse(frameRate.tryTick(timeCurrent));
        assertEquals(0.0, frameRate.getDelta());
        assertEquals(0.0, frameRate.getLastTickTime());

        timeCurrent = 0.05; // + 0.05 seconds
        assertFalse(frameRate.tryTick(timeCurrent));
        assertEquals(0.0, frameRate.getDelta());
        assertEquals(0.0, frameRate.getLastTickTime());

        timeCurrent = 0.1; // + 0.05 seconds
        assertTrue(frameRate.tryTick(timeCurrent));
        assertEquals(0.1, frameRate.getDelta());
        assertEquals(0.1, frameRate.getLastTickTime());

        timeCurrent = 0.2; // + 0.1 seconds
        assertTrue(frameRate.tryTick(timeCurrent));
        assertEquals(0.1, frameRate.getDelta());
        assertEquals(0.2, frameRate.getLastTickTime());

        timeCurrent = 0.35; // + 0.15 seconds
        assertTrue(frameRate.tryTick(timeCurrent));
        assertEquals(0.15, frameRate.getDelta(), TestData.PRECISION);
        assertEquals(0.35, frameRate.getLastTickTime(), TestData.PRECISION);

        timeCurrent = 0.20; // - 0.15 seconds
        assertFalse(frameRate.tryTick(timeCurrent));
        assertEquals(0.0, frameRate.getDelta());
        assertEquals(0.35, frameRate.getLastTickTime(), TestData.PRECISION);
    }

    @Test
    void testTryTickWithZeroFrequency() {
        FrameRate frameRate = new FrameRate(0);
        assertEquals(0.0, frameRate.getDelta());
        assertEquals(0.0, frameRate.getDeltaMin());
        assertEquals(0.0, frameRate.getLastTickTime());

        double timeCurrent = 0.0;
        assertTrue(frameRate.tryTick(timeCurrent));
        assertEquals(0.0, frameRate.getDelta());
        assertEquals(0.0, frameRate.getLastTickTime());

        timeCurrent = 0.1; // + 0.1 seconds
        assertTrue(frameRate.tryTick(timeCurrent));
        assertEquals(0.1, frameRate.getDelta());
        assertEquals(0.1, frameRate.getLastTickTime());

        timeCurrent = 0.1; // + 0.0 seconds
        assertTrue(frameRate.tryTick(timeCurrent));
        assertEquals(0.0, frameRate.getDelta());
        assertEquals(0.1, frameRate.getLastTickTime());

        timeCurrent = 0.0; // - 0.1 seconds
        assertFalse(frameRate.tryTick(timeCurrent));
        assertEquals(0.0, frameRate.getDelta());
        assertEquals(0.1, frameRate.getLastTickTime());
    }

    @Test
    void testTryTickLater() {
        FrameRate frameRate = new FrameRate(10);
        assertEquals(0.0, frameRate.getDelta());
        assertEquals(0.1, frameRate.getDeltaMin());
        assertEquals(1.0, frameRate.getDeltaMax());
        assertEquals(0.0, frameRate.getLastTickTime());

        double timeCurrent = 1000.0; // + 1000.0 seconds
        assertTrue(frameRate.tryTick(timeCurrent));
        assertEquals(1.0, frameRate.getDelta());
        assertEquals(1000.0, frameRate.getLastTickTime());

        timeCurrent = 1000.1; // + 0.1 seconds
        assertTrue(frameRate.tryTick(timeCurrent));
        assertEquals(0.1, frameRate.getDelta(), TestData.PRECISION);
        assertEquals(1000.1, frameRate.getLastTickTime());

        timeCurrent = 1002.1; // + 2.0 seconds
        assertTrue(frameRate.tryTick(timeCurrent));
        assertEquals(1.0, frameRate.getDelta());
        assertEquals(1002.1, frameRate.getLastTickTime());
    }

}
