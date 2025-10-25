package sk.discordtranslatorbot.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.data.Game;
import sk.discordtranslatorbot.data.HybridStorage;

import java.util.Collection;

public class ListGamesCommand implements Command {

    private final HybridStorage storage;

    public ListGamesCommand(HybridStorage storage) {
        this.storage = storage;
    }

    @Override
    public String getName() {
        return "!zoznam";
    }

    @Override
    public String getDescription() {
        return "Zobrazí všetky hry a ich stav.";
    }

    @Override
    public void execute(MessageReceivedEvent event, String argument) {
        Collection<Game> games = storage.getAll();

        if (games.isEmpty()) {
            event.getChannel().sendMessage("📭 Zoznam hier je prázdny.").queue();
            return;
        }

        StringBuilder sb = new StringBuilder("🎮 **Zoznam hier:**\n");
        for (Game g : games) {
            if (g.isReserved()) {
                sb.append("🔒 ").append(g.getName())
                        .append(" — rezervoval: ").append(g.getReservedBy()).append("\n");
            } else {
                sb.append("🟢 ").append(g.getName()).append(" — voľná\n");
            }
        }

        event.getChannel().sendMessage(sb.toString()).queue();
    }
}
