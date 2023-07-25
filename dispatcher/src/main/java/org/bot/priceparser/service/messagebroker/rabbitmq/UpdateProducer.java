package org.bot.priceparser.service.messagebroker.rabbitmq;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface UpdateProducer {

    void produce(String rabbitQueue, Message message);
}
