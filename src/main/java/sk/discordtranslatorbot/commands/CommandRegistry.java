package sk.discordtranslatorbot.commands;

import sk.discordtranslatorbot.commands.impl.*;
import sk.discordtranslatorbot.data.HybridStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * Registrácia všetkých príkazov bota.
 */
public class CommandRegistry {

    private final List<Command> commands = new ArrayList<>();

    public CommandRegistry(HybridStorage storage) {
        // Registrácia všetkých príkazov
        commands.add(new AddGameCommand(storage));      // !pridaj
        commands.add(new CancelReservationCommand(storage));   // !zrus
        commands.add(new ListGamesCommand(storage));    // !zoznam
        commands.add(new ReserveGameCommand(storage));  // !rezervuj
        commands.add(new InfoCommand(storage));         // !info
        commands.add(new CompleteCommand(storage));  // !complet
        commands.add(new CommandsListCommand(commands)); // !commands
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Command getByName(String name) {
        for (Command cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(name)) return cmd;
        }
        return null;
    }
}
