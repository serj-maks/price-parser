package org.bot.priceparser.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.bot.priceparser.service.ConsumerService;
import org.bot.priceparser.service.MainService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.bot.priceparser.RabbitQueue.*;

@Slf4j
@Service
public class ConsumerServiceImpl implements ConsumerService {

    private final MainService mainService;

    public ConsumerServiceImpl(MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdate(Update update) {
        log.debug("NODE: text message is received");
        mainService.processTextMessage(update);
    }
}
