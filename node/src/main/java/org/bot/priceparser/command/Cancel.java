package org.bot.priceparser.command;

import lombok.Getter;
import org.bot.priceparser.command.enums.TelegramCommands;
import org.bot.priceparser.entity.enums.BotState;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Cancel implements Command {

    private final BotState BOT_STATE = BotState.CANCEL;

    @Override
    public String execute() {
        //TODO: add bot state changing
        return "command was been canceled";
    }

    @Override
    public String toString() {
        return TelegramCommands.CANCEL.toString();
    }
}
