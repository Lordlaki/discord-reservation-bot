package sk.discordtranslatorbot.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.User;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.data.Game;
import sk.discordtranslatorbot.data.HybridStorage;

public class ReserveGameCommand implements Command {

    private final HybridStorage storage;

    public ReserveGameCommand(HybridStorage storage) {
        this.storage = storage;
    }

    @Override
    public String getName() {
        return "!rezervuj";
    }

    @Override
    public String getDescription() {
        return "Rezervuje hru pre používateľa. Použitie: !rezervuj <názov_hry>";
    }

    @Override
    public void execute(MessageReceivedEvent event, String argument) {
        if (argument == null || argument.isEmpty()) {
            event.getChannel().sendMessage("❌ Použitie: !rezervuj <názov_hry>").queue();
            return;
        }

        String gameName = argument.trim();
        User user = event.getAuthor();
        String userName = user.getName();

        Game game = storage.get(gameName);
        if (game == null) {
            event.getChannel().sendMessage("❌ Hra **" + gameName + "** nie je v zozname.").queue();
            return;
        }

        if (game.isReserved()) {
            event.getChannel().sendMessage(
                    "⚠️ Hra **" + gameName + "** je už rezervovaná používateľom **" + game.getReservedBy() + "**."
            ).queue();
            return;
        }

        game.setReservedBy(userName);
        game.setReservedById(user.getId());

        storage.save(game);

        event.getChannel().sendMessage(
                "✅ Hra **" + gameName + "** bola úspešne rezervovaná používateľom **" + userName + "**."
        ).queue();
    }
}
