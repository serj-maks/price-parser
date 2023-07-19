package org.bot.priceparser.command;

import lombok.RequiredArgsConstructor;
import org.bot.priceparser.command.enums.TelegramCommands;
import org.bot.priceparser.entity.enums.TUserState;

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
