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
        return "Pridá hru do zoznamu. Syntax: !pridaj <názov hry> [Steam link]";
    }

    @Override
    public void execute(MessageReceivedEvent event, String argument) {
        if (argument == null || argument.isBlank()) {
            event.getChannel().sendMessage("❌ Použitie: !pridaj <názov hry> [Steam link]").queue();
            return;
        }

        String name = argument;
        String link = null;

        // Rozdelenie názvu a linku, ak posledné slovo začína "http" alebo "https"
        String[] parts = argument.split("\\s+");
        String lastPart = parts[parts.length - 1];
        if (lastPart.startsWith("http://") || lastPart.startsWith("https://")) {
            link = lastPart;
            name = argument.substring(0, argument.lastIndexOf(lastPart)).trim();
        }

        // Vytvorenie objektu hry a uloženie Steam linku
        Game g = new Game(name);
        if (link != null) g.setSteamLink(link);

        // Uloženie do HybridStorage
        storage.addOrUpdateGame(g);

        // Správa na Discord: len názov hry, Steam link sa nezobrazuje
        String msg = "✅ Hra pridaná: **" + name + "**";
        event.getChannel().sendMessage(msg).queue();
    }
}
