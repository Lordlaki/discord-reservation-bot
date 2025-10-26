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
    public String getName() { return "complet"; }

    @Override
    public String getDescription() {
        return "Označí hru ako dokončenú a pridá verziu češtiny, slovenčiny alebo obe. Použitie: !complet <názov hry> <jazyk:cz/sk/both> <verzia>";
    }

    @Override
    public void execute(MessageReceivedEvent event, String args) {
        if (args == null || args.isBlank()) {
            event.getChannel().sendMessage("⚠️ Použitie: !complet <názov hry> <jazyk:cz/sk/both> <verzia>").queue();
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
            if (i > 0) nameBuilder.append(" ");
            nameBuilder.append(parts[i]);
        }
        String gameName = nameBuilder.toString();

        Game game = storage.get(gameName);
        if (game == null) {
            event.getChannel().sendMessage("❌ Hra **" + gameName + "** neexistuje.").queue();
            return;
        }

        String userId = event.getAuthor().getId();
        boolean isOwner = false;

        switch (lang) {
            case "cz" -> isOwner = userId.equals(game.getReservedByIdCz());
            case "sk" -> isOwner = userId.equals(game.getReservedByIdSk());
            case "both" -> isOwner = userId.equals(game.getReservedByIdCz()) || userId.equals(game.getReservedByIdSk());
            default -> {
                event.getChannel().sendMessage("⚠️ Neplatný jazyk. Použi: cz, sk alebo both").queue();
                return;
            }
        }

        if (!isOwner) {
            event.getChannel().sendMessage("🚫 Túto hru môže dokončiť len používateľ, ktorý ju rezervoval pre daný jazyk.").queue();
            return;
        }

        // Nastavenie verzie a vymazanie rezervácie pre príslušný jazyk
        switch (lang) {
            case "cz" -> {
                game.setCzechVersion(version); // E
                game.setReservedByCz(null);    // B
                game.setReservedByIdCz(null);
            }
            case "sk" -> {
                game.setSlovakVersion(version); // F
                game.setReservedBySk(null);     // C
                game.setReservedByIdSk(null);
            }
            case "both" -> {
                if (userId.equals(game.getReservedByIdCz())) {
                    game.setCzechVersion(version);
                    game.setReservedByCz(null);
                    game.setReservedByIdCz(null);
                }
                if (userId.equals(game.getReservedByIdSk())) {
                    game.setSlovakVersion(version);
                    game.setReservedBySk(null);
                    game.setReservedByIdSk(null);
                }
            }
        }

        game.setCompleted(true); // G
        storage.save(game);

        event.getChannel().sendMessage("✅ Hra **" + game.getName() + "** označená ako hotová.\n"
                + "🇨🇿 " + (game.getCzechVersion() != null ? game.getCzechVersion() : "-") + " | "
                + "🇸🇰 " + (game.getSlovakVersion() != null ? game.getSlovakVersion() : "-")).queue();
    }
}
