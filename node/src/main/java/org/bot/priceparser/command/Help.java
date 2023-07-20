package org.bot.priceparser.command;

import lombok.Getter;
import org.bot.priceparser.command.enums.TelegramCommands;
import org.bot.priceparser.entity.enums.BotState;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Help implements Command {

    private final BotState BOT_STATE = BotState.HELP;

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
    public String toString() {
        return TelegramCommands.HELP.toString();
    }
}
