package org.bot.priceparser.service.messagebroker.rabbitmq.impl;

import org.bot.priceparser.service.TUpdateService;
import org.bot.priceparser.service.messagebroker.rabbitmq.ConsumerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;

import static org.bot.priceparser.RabbitQueue.*;

@Service
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

    private final TUpdateService tUpdateService;

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consume(Update update) {
        tUpdateService.process(update);
    }
}
