package org.bot.priceparser.command;

import org.bot.priceparser.command.enums.TelegramCommands;
import org.springframework.stereotype.Component;

@Component
public class Help implements Command {

    @Override
    public String execute() {
        return """
                commands list:
                /start
                /cancel
                /registration
                /help""";
    }

    @Override
    public String getTelegramCommand() {
        return TelegramCommands.HELP.toString();
    }
}
