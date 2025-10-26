package sk.discordtranslatorbot.commands.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import sk.discordtranslatorbot.commands.Command;

import java.awt.*;
import java.util.List;

public class CommandsListCommand implements Command {

    private final List<Command> commands;

    public CommandsListCommand(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String getName() {
        return "commands";
    }

    @Override
    public String getDescription() {
        return "Zobrazí všetky dostupné príkazy bota graficky s emoji.";
    }

    @Override
    public void execute(MessageReceivedEvent event, String args) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("📋 Dostupné príkazy bota");
        embed.setColor(new Color(0, 191, 255)); // svetlomodrá farba
        embed.setDescription("Nižšie nájdeš všetky príkazy, ktoré môžeš použiť:");

        for (Command c : commands) {
            String emoji = getEmoji(c.getName());
            // špeciálne pre complet – zobrazíme všetky jazykové varianty
            if (c.getName().equalsIgnoreCase("complet")) {
                embed.addField(emoji + " !complet <názov hry> sk <verzia>", c.getDescription(), false);
                embed.addField(emoji + " !complet <názov hry> cz <verzia>", c.getDescription(), false);
                embed.addField(emoji + " !complet <názov hry> both <verzia>", c.getDescription(), false);
            } else {
                String category = getCategory(c.getName());
                embed.addField(emoji + " !" + c.getName() + category, c.getDescription(), false);
            }
        }

        event.getChannel().sendMessageEmbeds(embed.build()).queue();
    }

    private String getEmoji(String commandName) {
        return switch (commandName.toLowerCase()) {
            case "pridaj" -> "🟢";        // zelené pre pridanie
            case "zrus" -> "❌";
            case "zoznam" -> "🎮";
            case "rezervuj" -> "🔒";
            case "info" -> "ℹ️";
            case "commands" -> "📜";
            case "complet" -> "✅";       // zelené plus pre complet
            default -> "💡";
        };
    }

    // voliteľne – priradíme kategóriu alebo symbol k príkazu
    private String getCategory(String commandName) {
        return switch (commandName.toLowerCase()) {
            case "pridaj", "complet" -> " (úpravy/hotové)";
            case "rezervuj", "zrus" -> " (rezervácie)";
            case "zoznam", "info" -> " (informácie)";
            default -> "";
        };
    }
}
