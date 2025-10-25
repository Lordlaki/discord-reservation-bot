package sk.discordtranslatorbot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import sk.discordtranslatorbot.bot.ReservationBot;
import sk.discordtranslatorbot.data.GameStorage;
import sk.discordtranslatorbot.data.GoogleSheetsStorage;
import sk.discordtranslatorbot.data.HybridStorage;
import sk.discordtranslatorbot.util.Config;

import java.util.EnumSet;

public class Main {
    public static void main(String[] args) throws Exception {
        // Načítanie tokenu z env alebo config.properties
        String token = System.getenv("DISCORD_TOKEN");
        if (token == null || token.isEmpty()) token = Config.loadToken();

        if (token == null || token.isEmpty()) {
            System.err.println("❌ Zadaj token cez env DISCORD_TOKEN alebo v config.properties");
            System.exit(1);
        }

        // Lokálne a vzdialené storage
        GameStorage local = new GameStorage();
        String sheetId = Config.loadSheetId(); // Načítanie ID tabuľky z configu
        GoogleSheetsStorage remote = new GoogleSheetsStorage(
                "src/main/resources/credentials.json",
                sheetId
        );
        HybridStorage storage = new HybridStorage(local, remote);

        JDABuilder.createDefault(token, EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
                .addEventListeners(new ReservationBot(storage))
                .build();

        System.out.println("✅ Bot beží...");
    }
}
