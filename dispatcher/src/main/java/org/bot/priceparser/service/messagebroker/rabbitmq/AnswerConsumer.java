package org.bot.priceparser.service.messagebroker.rabbitmq;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface AnswerConsumer {

    void consume(SendMessage sendMessage);
}
