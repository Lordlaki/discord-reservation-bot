package sk.discordtranslatorbot.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.User;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.data.Game;
import sk.discordtranslatorbot.data.GameStorage;

public class ReserveGameCommand implements Command {

    private final GameStorage storage;

    public ReserveGameCommand(GameStorage storage) {
        this.storage = storage;
    }

    @Override
    public void execute(MessageReceivedEvent event, String argument) {
        if (argument == null || argument.isEmpty()) {
            event.getChannel().sendMessage("Použi: !rezervuj <hra>").queue();
            return;
        }

        Game g = storage.get(argument);
        if (g == null) {
            event.getChannel().sendMessage("❌ Hra neexistuje").queue();
            return;
        }

        if (g.isReserved()) {
            event.getChannel().sendMessage("⚠️ Už rezervoval " + g.getReservedBy()).queue();
            return;
        }

        User u = event.getAuthor();
        g.setReservedBy(u.getName());
        g.setReservedById(u.getId());
        storage.save();
        event.getChannel().sendMessage("🎯 Rezervované: " + u.getName()).queue();
    }
}
