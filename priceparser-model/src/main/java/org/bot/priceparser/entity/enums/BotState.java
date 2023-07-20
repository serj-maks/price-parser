package org.bot.priceparser.entity.enums;

public enum BotState {
    BASIC("basic"),
    WAIT_FOR_EMAIL("wait for email"),
    WAIT_FOR_LINK("wait for link"),
    HELP("help"),
    START("start"),
    REGISTRATION("registration"),
    CANCEL("cancel");

    private final String state;

    BotState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }
}
