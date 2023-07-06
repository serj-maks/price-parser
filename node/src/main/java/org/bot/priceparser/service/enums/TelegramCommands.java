package org.bot.priceparser.service.enums;

public enum TelegramCommands {
    START("/start"),
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel");

    private final String command;

    TelegramCommands(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

    public boolean equals(String command) {
        return this.toString().equals(command);
    }
}

