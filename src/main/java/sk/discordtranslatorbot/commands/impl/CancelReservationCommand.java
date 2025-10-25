package sk.discordtranslatorbot.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.data.Game;
import sk.discordtranslatorbot.data.HybridStorage;

public class CancelReservationCommand implements Command {

    private final HybridStorage storage;

    public CancelReservationCommand(HybridStorage storage) {
        this.storage = storage;
    }

    @Override
    public String getName() {
        return "zrus";  // bez !!
    }

    @Override
    public String getDescription() {
        return "Zruší rezerváciu hry, ktorú má používateľ rezervovanú.";
    }

    @Override
    public void execute(MessageReceivedEvent event, String argument) {
        if (argument == null || argument.isEmpty()) {
            event.getChannel().sendMessage("Použi: !zrus <hra>").queue();
            return;
        }

        String gameName = argument.trim();
        Game g = storage.get(gameName);

        if (g == null) {
            event.getChannel().sendMessage("❌ Hra neexistuje").queue();
            return;
        }

        if (!g.isReserved()) {
            event.getChannel().sendMessage("⚠️ Hra nie je rezervovaná").queue();
            return;
        }

        if (!g.getReservedById().equals(event.getAuthor().getId())) {
            event.getChannel().sendMessage("⚠️ Hru môže zrušiť len rezervujúci").queue();
            return;
        }

        g.setReservedBy(null);
        g.setReservedById(null);
        storage.save(g);

        event.getChannel().sendMessage("✅ Rezervácia zrušená").queue();
    }
}
