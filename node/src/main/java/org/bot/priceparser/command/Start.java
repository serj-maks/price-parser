package org.bot.priceparser.command;

import lombok.Getter;
import org.bot.priceparser.command.enums.TelegramCommands;
import org.bot.priceparser.entity.enums.BotState;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Start implements Command {

    private final BotState BOT_STATE = BotState.START;

    @Override
    public String execute() {
        return "hello! Enter /help command to see all available commands";
    }

    @Override
    public String toString() {
        return TelegramCommands.START.toString();
    }
}
