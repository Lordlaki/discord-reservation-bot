package sk.discordtranslatorbot.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.data.Game;
import sk.discordtranslatorbot.data.HybridStorage;

public class InfoCommand implements Command {

    private final HybridStorage storage;

    public InfoCommand(HybridStorage storage) {
        this.storage = storage;
    }

    @Override
    public String getName() {
        return "!info";
    }

    @Override
    public String getDescription() {
        return "Zobrazí, kto má rezervovanú hru.";
    }

    @Override
    public void execute(MessageReceivedEvent event, String argument) {
        if (argument == null || argument.isEmpty()) {
            event.getChannel().sendMessage("Použi: !info <hra>").queue();
            return;
        }

        Game g = storage.get(argument.trim());
        if (g == null) {
            event.getChannel().sendMessage("❌ Hra neexistuje").queue();
            return;
        }

        if (!g.isReserved()) {
            event.getChannel().sendMessage("Hra `" + g.getName() + "` nie je rezervovaná").queue();
        } else {
            event.getChannel().sendMessage("Hru `" + g.getName() + "` má rezervovanú: " + g.getReservedBy()).queue();
        }
    }
}