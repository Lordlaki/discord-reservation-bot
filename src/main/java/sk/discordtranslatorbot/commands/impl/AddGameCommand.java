package sk.discordtranslatorbot.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.data.Game;
import sk.discordtranslatorbot.data.HybridStorage;

public class AddGameCommand implements Command {

    private final HybridStorage storage;

    public AddGameCommand(HybridStorage storage) {
        this.storage = storage;
    }

    @Override
    public String getName() {
        return "!pridaj";
    }

    @Override
    public String getDescription() {
        return "Pridá novú hru do zoznamu. Použitie: !pridaj <názov_hry>";
    }

    @Override
    public void execute(MessageReceivedEvent event, String argument) {
        if (argument == null || argument.isEmpty()) {
            event.getChannel().sendMessage("❌ Použitie: !pridaj <názov_hry>").queue();
            return;
        }

        String gameName = argument.trim();

        Game existing = storage.get(gameName);
        if (existing != null) {
            event.getChannel().sendMessage("⚠️ Hra **" + gameName + "** už existuje!").queue();
            return;
        }

        try {
            storage.addGame(gameName);
            event.getChannel().sendMessage("✅ Hra **" + gameName + "** bola pridaná do zoznamu.").queue();
        } catch (Exception e) {
            event.getChannel().sendMessage(
                    "❌ Nepodarilo sa pridať hru **" + gameName + "**: " + e.getMessage()
            ).queue();
            e.printStackTrace();
        }
    }
}
