package org.bot.priceparser.command;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommandService {

    //TODO: чекнуть, что лучше, TUserState или String
    private final Map<String, Command> commandMap;

    public CommandService(List<Command> list) {
        this.commandMap = list.stream().collect(Collectors.toMap(Command::getTelegramCommand, Function.identity()));
    }

    public void runCommand(Command command) {
        //TODO: почему не могу использовать command ?
        Command command1 = commandMap.get(command.getTelegramCommand());
        command1.execute();
    }
}
