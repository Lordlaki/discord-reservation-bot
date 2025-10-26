package sk.discordtranslatorbot.bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.commands.CommandRegistry;
import sk.discordtranslatorbot.data.HybridStorage;

import java.util.HashMap;
import java.util.Map;

/**
 * Hlavný handler správ pre Discord bota.
 * Načíta všetky príkazy z CommandRegistry a spracováva ich podľa textu.
 */
public class ReservationBot extends ListenerAdapter {

    private final Map<String, Command> commandMap = new HashMap<>();

    public ReservationBot(HybridStorage storage, CommandRegistry registry) {
        // Registrácia všetkých príkazov z registry do mapy pre rýchly prístup
        for (Command cmd : registry.getCommands()) {
            commandMap.put(cmd.getName().toLowerCase(), cmd);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Ignoruj správy od botov
        if (event.getAuthor().isBot()) return;

        String rawMessage = event.getMessage().getContentRaw().trim();
        if (!rawMessage.startsWith("!")) return; // musí začínať '!' aby sa spracovalo

        // Rozdelenie príkazu a argumentov
        String[] parts = rawMessage.substring(1).split("\\s+", 2);
        String commandName = parts[0].toLowerCase();
        String argument = (parts.length > 1) ? parts[1] : null;

        // Vyhľadanie príkazu
        Command command = commandMap.get(commandName);

        if (command != null) {
            try {
                command.execute(event, argument);
            } catch (Exception e) {
                event.getChannel().sendMessage("⚠️ Nastala chyba pri spracovaní príkazu: " + e.getMessage()).queue();
                e.printStackTrace();
            }
        } else {
            event.getChannel().sendMessage("❌ Neznámy príkaz: `" + commandName + "`").queue();
        }
    }
}
