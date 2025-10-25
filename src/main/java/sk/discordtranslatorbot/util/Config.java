package sk.discordtranslatorbot.util;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static Properties props;

    private static void load() throws Exception {
        if (props == null) {
            props = new Properties();
            try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (in == null) throw new RuntimeException("config.properties nenájdený!");
                props.load(in);
            }
        }
    }

    public static String loadToken() throws Exception {
        load();
        return props.getProperty("DISCORD_TOKEN");
    }

    public static String loadSheetId() throws Exception {
        load();
        return props.getProperty("SHEET_ID");
    }
}