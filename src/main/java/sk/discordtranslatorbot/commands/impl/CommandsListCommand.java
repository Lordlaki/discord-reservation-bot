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
            embed.addField(emoji + " !" + c.getName(), c.getDescription(), false);
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
            default -> "💡";
        };
    }
}
