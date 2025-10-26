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
        return "Označí hru ako dokončenú a pridá verziu češtiny, slovenčiny alebo obe. Použitie: !complet <názov hry> <jazyk:cz/sk/both> <verzia>";
    }

    @Override
    public void execute(MessageReceivedEvent event, String args) {
        if (args == null || args.isBlank()) {
            event.getChannel().sendMessage("⚠️ Použitie: `!complet <názov hry> <jazyk:cz/sk/both> <verzia>`").queue();
            return;
        }

        String[] parts = args.trim().split("\\s+");
        if (parts.length < 3) {
            event.getChannel().sendMessage("⚠️ Musíš zadať názov hry, jazyk a verziu.").queue();
            return;
        }

        String version = parts[parts.length - 1];
        String lang = parts[parts.length - 2].toLowerCase();

        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < parts.length - 2; i++) {
            nameBuilder.append(parts[i]);
            if (i < parts.length - 3) nameBuilder.append(" ");
        }
        String gameName = nameBuilder.toString();

        Game game = storage.get(gameName);
        if (game == null) {
            event.getChannel().sendMessage("❌ Hra **" + gameName + "** neexistuje.").queue();
            return;
        }

        String userId = event.getAuthor().getId();
        if (!userId.equals(game.getReservedById())) {
            event.getChannel().sendMessage("🚫 Túto hru môže dokončiť len používateľ, ktorý ju rezervoval.").queue();
            return;
        }

        // nastavenie verzie podľa jazyka
        switch (lang) {
            case "cz" -> game.setCzechVersion(version);
            case "sk" -> game.setSlovakVersion(version);
            case "both" -> {
                game.setCzechVersion(version);
                game.setSlovakVersion(version);
            }
            default -> {
                event.getChannel().sendMessage("⚠️ Neplatný jazyk. Použi: cz, sk alebo both").queue();
                return;
            }
        }

        game.setCompleted(true);
        storage.save(game); // uloží aj do Google Sheets

        event.getChannel().sendMessage("✅ Hra **" + game.getName() + "** označená ako hotová.\n"
                + "🇨🇿 " + (game.getCzechVersion() != null ? game.getCzechVersion() : "-") + " | "
                + "🇸🇰 " + (game.getSlovakVersion() != null ? game.getSlovakVersion() : "-")).queue();
    }
}
