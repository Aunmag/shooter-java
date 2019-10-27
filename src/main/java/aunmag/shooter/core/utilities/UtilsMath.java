package aunmag.shooter.core.utilities;

import org.joml.Vector2f;

public final class UtilsMath {

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

    public static float distance(float x1, float y1, float x2, float y2) {
        var powX = Math.pow(x1 - x2, 2);
        var powY = Math.pow(y1 - y2, 2);
        return (float) Math.sqrt(powX + powY);
    }

    public static boolean closerThan(Vector2f a, Vector2f b, float distance) {
        return a.distanceSquared(b) < distance * distance;
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

    public static float oscillateSaw(double time, double rate) {
        var value = 2 * oscillateTriangle(time, rate);

        if (value > 1) {
            value = 2 - value;
        }

        return value;
    }

    public static float oscillateTriangle(double time, double rate) {
        return (float) (time / rate) % 1.0f;
    }

    public static float bound(float value, float bound) {
        return value * bound + bound / 2.0f;
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
