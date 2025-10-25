package sk.discordtranslatorbot.util;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static String loadToken() {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("❌ config.properties nenájdený v resources");
                return null;
            }
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty("DISCORD_TOKEN");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}