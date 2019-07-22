package aunmag.shooter.core.utilities;

import org.joml.Vector2f;

import java.util.Random;

public final class UtilsMath {

    public static final Random random = new Random();
    public static final double PIx0_5 = Math.PI / 2.0;
    public static final double PIx1_5 = Math.PI + PIx0_5;
    public static final double PIx2 = Math.PI * 2.0;

    private UtilsMath() {}

    public static float correctRadians(double radians) {
        return (float) (radians % PIx2);
    }

    public static float radiansDifference(double a, double b) {
        a = correctRadians(a);
        b = correctRadians(b);
        var difference = b - a;

        if (Math.abs(difference) > Math.PI) {
            if (a < b) {
                difference -= PIx2;
            } else {
                difference += PIx2;
            }
        }

        return correctRadians(difference);
    }

    public static int randomizeBetween(int a, int b) {
        if (a == b) {
            return a;
        }

        int min;
        int max;

        if (a < b) {
            min = a;
            max = b;
        } else {
            min = b;
            max = a;
        }

        return random.nextInt((max - min) + 1) + min;
    }

    public static float randomizeBetween(float a, float b) {
        if (a == b) {
            return a;
        }

        float min;
        float max;

        if (a < b) {
            min = a;
            max = b;
        } else {
            min = b;
            max = a;
        }

        return random.nextFloat() * (max - min) + min;
    }

    public static float randomizeFlexibly(float middle, float offset) {
        if (offset == 0) {
            return middle;
        }

        var flex = 0.5f;
        var offsetMin = offset * randomizeBetween(0, flex);
        var offsetMax = offset * randomizeBetween(flex, 1);
        var offsetRandom = randomizeBetween(offsetMin, offsetMax);

        var resultMin = middle - offsetRandom;
        var resultMax = middle + offsetRandom;

        return randomizeBetween(resultMin, resultMax);
    }

    public static boolean chance(float probability) {
        return UtilsMath.random.nextFloat() <= probability;
    }

    public static float distance(float x1, float y1, float x2, float y2) {
        var powX = Math.pow(x1 - x2, 2);
        var powY = Math.pow(y1 - y2, 2);
        return (float) Math.sqrt(powX + powY);
    }

    public static float angle(Vector2f a, Vector2f b) {
        return angle(a.x, a.y, b.x, b.y);
    }

    public static float angle(float x1, float y1, float x2, float y2) {
        return (float) Math.atan2(y1 - y2, x1 - x2);
    }

    public static float round(float value, float round) {
        return Math.round(value * round) / round;
    }

    public static boolean inRange(float number, float min, float max) {
        return min <= number && number <= max;
    }

    public static float limit(float number, float min, float max) {
        if (number < min) {
            number = min;
        } else if (number > max) {
            number = max;
        }

        return number;
    }

    /**
     * Predicts the meet point of two moving objects.
     */
    public static Vector2f lead(
            Vector2f sourcePosition,
            Vector2f sourceVelocity,
            Vector2f targetPosition,
            Vector2f targetVelocity
    ) {
        if (targetVelocity.x == 0 || targetVelocity.y == 0) {
            return targetPosition;
        }

        var distance = targetPosition.distanceSquared(sourcePosition);
        var velocity = new Vector2f()
                .add(targetVelocity)
                .sub(sourceVelocity)
                .lengthSquared();

        var advance = Math.sqrt(distance / velocity);

        return new Vector2f(targetVelocity)
                .mul((float) advance)
                .add(targetPosition);
    }

}
