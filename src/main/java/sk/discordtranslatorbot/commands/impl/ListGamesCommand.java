package sk.discordtranslatorbot.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.data.GameStorage;

public class ListGamesCommand implements Command {

    private final GameStorage storage;

    public ListGamesCommand(GameStorage storage) {
        this.storage = storage;
    }

    @Override
    public void execute(MessageReceivedEvent event, String argument) {
        if (storage.getAll().isEmpty()) {
            event.getChannel().sendMessage("Žiadne hry zatiaľ.").queue();
            return;
        }

        StringBuilder sb = new StringBuilder("🎮 Zoznam hier:\n");
        storage.getAll().forEach(g -> {
            sb.append("- ").append(g.getName());
            if (g.isReserved()) sb.append(" (rezervované ").append(g.getReservedBy()).append(")");
            sb.append("\n");
        });

        event.getChannel().sendMessage(sb.toString()).queue();
    }
}
