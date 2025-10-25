package sk.discordtranslatorbot.commands.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.data.Game;
import sk.discordtranslatorbot.data.HybridStorage;

import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListGamesCommand implements Command {

    private final HybridStorage storage;
    private final Pattern steamIdPattern = Pattern.compile("https?://store\\.steampowered\\.com/app/(\\d+)");

    public ListGamesCommand(HybridStorage storage) {
        this.storage = storage;
    }

    @Override
    public String getName() {
        return "zoznam"; // bez !!
    }

    @Override
    public String getDescription() {
        return "Zobrazí všetky hry, ich stav a Steam link (graficky).";
    }

    @Override
    public void execute(MessageReceivedEvent event, String argument) {
        List<Game> games = storage.getAll();

        if (games.isEmpty()) {
            event.getChannel().sendMessage("📭 Zoznam hier je prázdny.").queue();
            return;
        }

        for (Game g : games) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(g.getName());
            embed.setColor(Color.CYAN);

            String status = g.isReserved() ? "🔒 rezervované" : "🟢 voľná";
            String linkText = g.getSteamLink() != null ? "[Steam link](" + g.getSteamLink() + ")" : "-";

            embed.addField("Status", status, false);
            embed.addField("Steam", linkText, false);

            // pridanie obrázku z Steam
            if (g.getSteamLink() != null) {
                Matcher matcher = steamIdPattern.matcher(g.getSteamLink());
                if (matcher.find()) {
                    String appId = matcher.group(1);
                    String imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/" + appId + "/header.jpg";
                    embed.setThumbnail(imageUrl);
                }
            }

            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        }
    }
}
