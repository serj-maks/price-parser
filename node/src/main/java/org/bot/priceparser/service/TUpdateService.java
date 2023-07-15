package org.bot.priceparser.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TUpdateService {

    void process(Update update);
}
