package sk.discordtranslatorbot.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.data.Game;
import sk.discordtranslatorbot.data.HybridStorage;

public class CompleteCommand implements Command {

    private final HybridStorage storage;

    public CompleteCommand(HybridStorage storage) {
        this.storage = storage;
    }

    @Override
    public String getName() {
        return "complet";
    }

    @Override
    public String getDescription() {
        return "Označí hru ako dokončenú a pridá verziu češtiny. Použitie: !complet <názov hry> <verzia>";
    }

    @Override
    public void execute(MessageReceivedEvent event, String args) {
        if (args == null || args.isBlank()) {
            event.getChannel().sendMessage("⚠️ Použitie: `!complet <názov hry> <verzia>`").queue();
            return;
        }

        String[] parts = args.trim().split("\\s+");
        if (parts.length < 2) {
            event.getChannel().sendMessage("⚠️ Musíš zadať aj názov hry aj verziu.").queue();
            return;
        }

        String version = parts[parts.length - 1];
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            nameBuilder.append(parts[i]);
            if (i < parts.length - 2) nameBuilder.append(" ");
        }
        String gameName = nameBuilder.toString();

        Game game = storage.get(gameName); // použitie get() z HybridStorage

        if (game == null) {
            event.getChannel().sendMessage("❌ Hra **" + gameName + "** neexistuje.").queue();
            return;
        }

        String userId = event.getAuthor().getId();
        if (!userId.equals(game.getReservedById())) {
            event.getChannel().sendMessage("🚫 Túto hru môže dokončiť len používateľ, ktorý ju rezervoval.").queue();
            return;
        }

        game.setCzechVersion(version);
        game.setCompleted(true);
        storage.save(game); // uloží aj do Google Sheets

        event.getChannel().sendMessage("✅ Hra **" + game.getName() + "** bola označená ako hotová ✅\n"
                + "📦 Verzia češtiny: `" + version + "`").queue();
    }
}
