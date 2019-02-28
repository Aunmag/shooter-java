package aunmag.shooter.game.client;

import java.io.InputStream;
import java.util.Properties;

public final class Constants {

    public static final String TITLE = "A Zombie Shooter Game";
    public static final String DEVELOPER = "Aunmag";
    public static final String VERSION;

    static {
        String filename = "/shooter.properties";
        String version = "X.X.X";

        try {
            InputStream inputStream = Constants.class.getResourceAsStream(filename);
            Properties properties = new Properties();
            properties.load(inputStream);
            version = properties.getProperty("version");
            inputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        VERSION = version;
    }

    private Constants() {}

}
