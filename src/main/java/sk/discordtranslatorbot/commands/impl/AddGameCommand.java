package sk.discordtranslatorbot.commands.impl;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import sk.discordtranslatorbot.commands.Command;
import sk.discordtranslatorbot.data.GameStorage;

public class AddGameCommand implements Command {

    private final GameStorage storage;

    public AddGameCommand(GameStorage storage) {
        this.storage = storage;
    }

    @Override
    public void execute(MessageReceivedEvent event, String argument) {
        if (argument == null || argument.isEmpty()) {
            event.getChannel().sendMessage("Použi: !pridaj <názov hry>").queue();
            return;
        }

        if (storage.get(argument) != null) {
            event.getChannel().sendMessage("⚠️ Hra už existuje").queue();
            return;
        }

        storage.addGame(argument);
        event.getChannel().sendMessage("✅ Pridaná hra: " + argument).queue();
    }
}
