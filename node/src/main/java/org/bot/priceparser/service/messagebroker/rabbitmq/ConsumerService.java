package org.bot.priceparser.service.messagebroker.rabbitmq;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {

    void consume(Update update);
}
