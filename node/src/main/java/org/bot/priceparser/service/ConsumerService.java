package org.bot.priceparser.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {

    void consumeTextMessageUpdate(Update update);
}
