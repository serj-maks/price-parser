package org.bot.priceparser.service;

import org.bot.priceparser.command.Command;
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
        this.commandMap = list.stream().collect(Collectors.toMap(Command::toString, Function.identity()));
    }

    public String runCommand(Command command) {
        Command command1 = commandMap.get(command.toString());
        return command1.execute();
    }
}
