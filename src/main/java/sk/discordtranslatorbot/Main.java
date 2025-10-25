package sk.discordtranslatorbot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import sk.discordtranslatorbot.bot.ReservationBot;
import sk.discordtranslatorbot.data.GameStorage;
import sk.discordtranslatorbot.util.Config;

import java.util.EnumSet;

public class Main {
    public static void main(String[] args) throws Exception {
        String token = System.getenv("DISCORD_TOKEN");
        if (token == null || token.isEmpty()) token = Config.loadToken();

        if (token == null || token.isEmpty()) {
            System.err.println("❌ Zadaj token cez env DISCORD_TOKEN alebo v config.properties");
            System.exit(1);
        }

        GameStorage storage = new GameStorage(); // jednotné úložisko

        JDABuilder.createDefault(token, EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
                .addEventListeners(new ReservationBot(storage))
                .build();

        System.out.println("✅ Bot beží...");
    }
}
