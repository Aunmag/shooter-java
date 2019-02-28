package aunmag.shooter.core;

public final class Configs {

    private static boolean isFullscreen = false;
    private static boolean isSamplesLoadingEnabled = true;
    private static int antialiasing = 2;
    private static float pixelsPerMeter = 32f;

    private Configs() {}

    /* Getters */

    public static boolean isFullscreen() {
        return isFullscreen;
    }

    public static boolean isSamplesLoadingEnabled() {
        return isSamplesLoadingEnabled;
    }

    public static int getAntialiasing() {
        return antialiasing;
    }

    public static float getPixelsPerMeter() {
        return pixelsPerMeter;
    }

    /* Setters */

    public static void setFullscreen(boolean isFullscreen) {
        if (Application.isInitialized()) {
            String message = "Unable to change screen mode after engine initialization";
            System.err.println(message);
        } else {
            Configs.isFullscreen = isFullscreen;
        }
    }

    public static void setSamplesLoadingEnabled(boolean isSamplesLoadingEnabled) {
        if (Application.isInitialized()) {
            String message = "Warning: "
                    + "The all samples loaded already will continue to be available";
            System.err.println(message);
        }

        Configs.isSamplesLoadingEnabled = isSamplesLoadingEnabled;
    }

    public static void setAntialiasing(int antialiasing) {
        if (Application.isInitialized()) {
            String message = "Unable to change antialiasing after engine initialization";
            System.err.println(message);
        } else {
            Configs.antialiasing = antialiasing;
        }
    }

    public static void setPixelsPerMeter(float pixelsPerMeter) {
        if (Application.isInitialized()) {
            String message = "Unable to change metrics after engine initialization";
            System.err.println(message);
        } else {
            Configs.pixelsPerMeter = pixelsPerMeter;
        }
    }

}
