package org.bot.priceparser.command;

import lombok.Getter;
import org.bot.priceparser.command.enums.TelegramCommands;
import org.bot.priceparser.entity.enums.BotState;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Registration implements Command {

    private final BotState BOT_STATE = BotState.REGISTRATION;

    //TODO: добавить функционал: регистрация
    @Override
    public String execute() {
        return "temporary unavailable";
    }

    @Override
    public String toString() {
        return TelegramCommands.REGISTRATION.toString();
    }
}
