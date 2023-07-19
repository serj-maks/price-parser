package org.bot.priceparser.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Help implements Command {

    private final MainMenu mainMenu;

    @Override
    public String execute() {
        return mainMenu.help();
    }
}
