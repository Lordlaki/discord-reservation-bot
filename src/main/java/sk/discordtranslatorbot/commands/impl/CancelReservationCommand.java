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
        return "zrus";
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

        String userId = event.getAuthor().getId();
        boolean canceled = false;

        if (userId.equals(g.getReservedByIdCz())) {
            g.setReservedByCz(null);
            g.setReservedByIdCz(null);
            canceled = true;
        }

        if (userId.equals(g.getReservedByIdSk())) {
            g.setReservedBySk(null);
            g.setReservedByIdSk(null);
            canceled = true;
        }

        if (!canceled) {
            event.getChannel().sendMessage("⚠️ Hru môže zrušiť len používateľ, ktorý ju rezervoval").queue();
            return;
        }

        storage.save(g);
        event.getChannel().sendMessage("✅ Rezervácia zrušená").queue();
    }
}
