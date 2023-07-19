package org.bot.priceparser.command;

public interface Command {

    String execute();

    String getTelegramCommand();
}
