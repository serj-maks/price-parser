package org.bot.priceparser.service.messagebroker.rabbitmq;

import org.bot.priceparser.service.MainMenuService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;

import static org.bot.priceparser.RabbitQueue.*;

@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final MainMenuService mainMenuService;

    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consume(Update update) {
        mainMenuService.process(update);
    }
}
