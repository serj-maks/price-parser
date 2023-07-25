package org.bot.priceparser.service.messagebroker.rabbitmq.impl;

import lombok.RequiredArgsConstructor;
import org.bot.priceparser.controller.Bot;
import org.bot.priceparser.service.messagebroker.rabbitmq.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bot.priceparser.RabbitQueue.*;

@Service
@RequiredArgsConstructor
public class AnswerConsumerImpl implements AnswerConsumer {

    private final Bot bot;

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        bot.sendAnswerMessage(sendMessage);
    }
}
