package org.bot.priceparser.entity.enums;

public enum TUserState {
    BASIC("basic"),
    WAIT_FOR_EMAIL("wait for email"),
    WAIT_FOR_LINK("wait for link");

    private final String state;

    TUserState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }
}
