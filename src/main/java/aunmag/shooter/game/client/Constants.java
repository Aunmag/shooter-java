package aunmag.shooter.game.client;

import java.io.IOException;
import java.util.Properties;

public final class Constants {

    public static final String TITLE = "A Zombie Shooter Game";
    public static final String DEVELOPER = "Aunmag";
    public static final String VERSION;

    static {
        var filename = "/shooter.properties";
        var version = "X.X.X";

        try {
            var inputStream = Constants.class.getResourceAsStream(filename);
            var properties = new Properties();
            properties.load(inputStream);
            version = properties.getProperty("version");
            inputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        VERSION = version;
    }

    private Constants() {}

}
