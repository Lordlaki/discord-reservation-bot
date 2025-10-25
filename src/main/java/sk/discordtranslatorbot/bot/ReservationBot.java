package sk.discordtranslatorbot.bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.commands.CommandRegistry;
import sk.discordtranslatorbot.data.HybridStorage;

import java.util.HashMap;
import java.util.Map;

public class ReservationBot extends ListenerAdapter {

    private final Map<String, Command> commands = new HashMap<>();

    public ReservationBot(HybridStorage storage, CommandRegistry registry) {
        // Naplníme mapu všetkými príkazmi z registry
        for (Command cmd : registry.getCommands()) {
            commands.put(cmd.getName().toLowerCase(), cmd);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String msg = event.getMessage().getContentRaw().trim();
        if (!msg.startsWith("!")) return;

        String[] parts = msg.substring(1).split("\\s+", 2);
        String cmdName = parts[0].toLowerCase();
        String arg = parts.length > 1 ? parts[1] : null;

        Command cmd = commands.get(cmdName);
        if (cmd != null) {
            cmd.execute(event, arg);
        } else {
            event.getChannel().sendMessage("❌ Neznámy príkaz: " + cmdName).queue();
        }
    }
}
