package org.bot.priceparser.service.messagebroker.rabbitmq.impl;

import lombok.extern.slf4j.Slf4j;
import org.bot.priceparser.service.messagebroker.rabbitmq.UpdateProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Service
public class UpdateProducerImpl implements UpdateProducer {

    private final RabbitTemplate rabbitTemplate;

    public UpdateProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String rabbitQueue, Message message) {
        rabbitTemplate.convertAndSend(rabbitQueue, message);
        log.debug(message.getText());
    }
}
