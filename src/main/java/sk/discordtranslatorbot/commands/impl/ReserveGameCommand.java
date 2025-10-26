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
    public String getName() { return "rezervuj"; }

    @Override
    public String getDescription() {
        return "Rezervuje hru pre používateľa. Použitie: !rezervuj <názov_hry> <jazyk:cz/sk/both>";
    }

    @Override
    public void execute(MessageReceivedEvent event, String argument) {
        if (argument == null || argument.isBlank()) {
            event.getChannel().sendMessage("❌ Použitie: !rezervuj <názov_hry> <jazyk:cz/sk/both>").queue();
            return;
        }

        String[] parts = argument.trim().split("\\s+");
        if (parts.length < 2) {
            event.getChannel().sendMessage("❌ Zadaj názov hry a jazyk: cz, sk alebo both").queue();
            return;
        }

        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            if (i > 0) nameBuilder.append(" ");
            nameBuilder.append(parts[i]);
        }
        String gameName = nameBuilder.toString().trim();
        String lang = parts[parts.length - 1].toLowerCase();

        Game game = storage.get(gameName);
        if (game == null) {
            event.getChannel().sendMessage("❌ Hra **" + gameName + "** nie je v zozname.").queue();
            return;
        }

        User user = event.getAuthor();
        String userName = user.getName();
        String userId = user.getId();

        switch (lang) {
            case "cz" -> {
                if (game.isReserved("cz")) {
                    event.getChannel().sendMessage("⚠️ Hra **" + gameName + "** (CZ) je už rezervovaná používateľom **" + game.getReservedByCz() + "**.").queue();
                    return;
                }
                game.setReservedByCz(userName); // B
                game.setReservedByIdCz(userId);
            }
            case "sk" -> {
                if (game.isReserved("sk")) {
                    event.getChannel().sendMessage("⚠️ Hra **" + gameName + "** (SK) je už rezervovaná používateľom **" + game.getReservedBySk() + "**.").queue();
                    return;
                }
                game.setReservedBySk(userName); // C
                game.setReservedByIdSk(userId);
            }
            case "both" -> {
                if (game.isReserved("cz") || game.isReserved("sk")) {
                    event.getChannel().sendMessage("⚠️ Hra **" + gameName + "** už je rezervovaná pre CZ alebo SK.").queue();
                    return;
                }
                game.setReservedByCz(userName);
                game.setReservedByIdCz(userId);
                game.setReservedBySk(userName);
                game.setReservedByIdSk(userId);
            }
            default -> {
                event.getChannel().sendMessage("⚠️ Neplatný jazyk. Použi: cz, sk alebo both").queue();
                return;
            }
        }

        storage.save(game);
        event.getChannel().sendMessage("✅ Hra **" + gameName + "** rezervovaná pre **" + lang.toUpperCase() + "** používateľom **" + userName + "**.").queue();
    }
}
