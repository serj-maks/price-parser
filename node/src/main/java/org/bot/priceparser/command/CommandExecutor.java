package org.bot.priceparser.command;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {

    private final List<Command> commands = new ArrayList<>();

    public String executeCommand(Command command) {
        commands.add(command);
        return command.execute();
    }
}
