package sk.discordtranslatorbot.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.data.Game;
import sk.discordtranslatorbot.data.HybridStorage;

import java.util.List;

public class InfoCommand implements Command {

    private final HybridStorage storage;

    public InfoCommand(HybridStorage storage) {
        this.storage = storage;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "!info <názov hry> - zobrazí informácie o hre";
    }

    @Override
    public void execute(MessageReceivedEvent event, String args) {
        if (args == null || args.isBlank()) {
            event.getChannel().sendMessage("❌ Zadaj názov hry: !info <názov>").queue();
            return;
        }

        String name = args.trim();
        Game g = storage.get(name);

        if (g == null) {
            event.getChannel().sendMessage("❌ Hra nenájdená: " + name).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ℹ️ Informácie o hre: **").append(g.getName()).append("**\n");
        if (g.getReservedBy() != null) {
            sb.append("Rezervovaná: ").append(g.getReservedBy()).append("\n");
        } else {
            sb.append("Nie je rezervovaná\n");
        }
        if (g.getSteamLink() != null && !g.getSteamLink().isBlank()) {
            sb.append("Steam link: ").append(g.getSteamLink());
        }

        event.getChannel().sendMessage(sb.toString()).queue();
    }
}
