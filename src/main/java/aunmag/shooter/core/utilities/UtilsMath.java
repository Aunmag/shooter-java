package aunmag.shooter.core.utilities;

import aunmag.shooter.core.basics.BaseObject;
import org.joml.Vector2f;

import java.util.Random;

public final class UtilsMath {

    public static final Random random = new Random();
    public static final double PIx0_5 = Math.PI / 2.0;
    public static final double PIx1_5 = Math.PI + PIx0_5;
    public static final double PIx2 = Math.PI * 2.0;

    private UtilsMath() {}

    public static float correctRadians(double value) {
        return (float) (value % PIx2);
    }

    public static float radiansDifference(double a, double b) {
        a = correctRadians(a);
        b = correctRadians(b);
        double difference = b - a;

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

        int difference = max - min;
        return random.nextInt(difference + 1) + min;
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

        float difference = max - min;
        return random.nextFloat() * difference + min;
    }

    public static float randomizeFlexibly(float middle, float offset) {
        if (offset == 0f) {
            return middle;
        }

        float flex = 0.5f;
        float offsetMin = offset * randomizeBetween(0, flex);
        float offsetMax = offset * randomizeBetween(flex, 1);
        float offsetRandom = randomizeBetween(offsetMin, offsetMax);

        float resultMin = middle - offsetRandom;
        float resultMax = middle + offsetRandom;

        return randomizeBetween(resultMin, resultMax);
    }

    public static float calculateDistanceBetween(float x1, float y1, float x2, float y2) {
        double powX = Math.pow(x1 - x2, 2);
        double powY = Math.pow(y1 - y2, 2);
        return (float) Math.sqrt(powX + powY);
    }

    public static float calculateRadiansBetween(BaseObject a, BaseObject b) {
        return calculateRadiansBetween(
                a.getPosition().x(),
                a.getPosition().y(),
                b.getPosition().x(),
                b.getPosition().y()
        );
    }

    public static float calculateRadiansBetween(Vector2f a, Vector2f b) {
        return calculateRadiansBetween(a.x(), a.y(), b.x(), b.y());
    }

    public static float calculateRadiansBetween(float x1, float y1, float x2, float y2) {
        float differenceX = x1 - x2;
        float differenceY = y1 - y2;
        return (float) Math.atan2(differenceY, differenceX);
    }

    public static float calculateRoundValue(float value, float round) {
        return Math.round(value * round) / round;
    }

    public static boolean calculateIsNumberInsideRange(
            float number,
            float min,
            float max
    ) {
        return min <= number && number <= max;
    }

    public static float limitNumber(float number, float min, float max) {
        if (number < min) {
            number = min;
        } else if (number > max) {
            number = max;
        }

        return number;
    }

}
