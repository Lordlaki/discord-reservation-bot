package sk.discordtranslatorbot.bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.commands.impl.AddGameCommand;
import sk.discordtranslatorbot.commands.impl.ListGamesCommand;
import sk.discordtranslatorbot.commands.impl.ReserveGameCommand;
import sk.discordtranslatorbot.commands.impl.CancelReservationCommand;
import sk.discordtranslatorbot.commands.impl.InfoCommand;
import sk.discordtranslatorbot.data.GameStorage;

import java.util.HashMap;
import java.util.Map;

public class ReservationBot extends ListenerAdapter {

    private final GameStorage storage; // jednotné úložisko
    private final Map<String, Command> commands = new HashMap<>();

    public ReservationBot(GameStorage storage) {
        this.storage = storage;

        // registrácia príkazov
        commands.put("pridaj", new AddGameCommand(storage));
        commands.put("zoznam", new ListGamesCommand(storage));
        commands.put("rezervuj", new ReserveGameCommand(storage));
        commands.put("zrus", new CancelReservationCommand(storage));
        commands.put("info", new InfoCommand(storage));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String msg = event.getMessage().getContentRaw().trim();
        if (!msg.startsWith("!")) return;

        String[] parts = msg.substring(1).split("\\s+", 2);
        String cmd = parts[0].toLowerCase();
        String arg = parts.length > 1 ? parts[1] : null;

        Command command = commands.get(cmd);
        if (command != null) {
            command.execute(event, arg);
        } else {
            event.getChannel().sendMessage("❌ Neznámy príkaz: " + cmd).queue();
        }
    }
}