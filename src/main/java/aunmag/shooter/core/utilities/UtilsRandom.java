package aunmag.shooter.core.utilities;

import java.util.Optional;
import java.util.Random;

public final class UtilsRandom {

    public static final Random RANDOM = new Random();

    private UtilsRandom() {}

    public static int between(int a, int b) {
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

        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public static float between(float a, float b) {
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

        return RANDOM.nextFloat() * (max - min) + min;
    }

    public static float deviation(float middle, float offset, float flex) {
        if (offset == 0) {
            return middle;
        }

        var offsetMin = offset * between(0, flex);
        var offsetMax = offset * between(flex, 1);
        var offsetRandom = between(offsetMin, offsetMax);

        var resultMin = middle - offsetRandom;
        var resultMax = middle + offsetRandom;

        return between(resultMin, resultMax);
    }

    public static float deviation(float middle, float offset) {
        return deviation(middle, offset, 0.5f);
    }

    public static boolean chance(float probability) {
        return RANDOM.nextFloat() <= probability;
    }

    public static <T> Optional<T> chose(T[] array) {
        if (array.length == 0) {
            return Optional.empty();
        } else {
            return Optional.of(array[RANDOM.nextInt(array.length)]);
        }
    }

    public static <T> T chose(T first, T... values) {
        var index = RANDOM.nextInt(values.length + 1);

        if (index == values.length) {
            return first;
        } else {
            return values[index];
        }
    }

}
