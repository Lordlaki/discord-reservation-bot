package sk.discordtranslatorbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
    void execute(MessageReceivedEvent event, String argument);
}
