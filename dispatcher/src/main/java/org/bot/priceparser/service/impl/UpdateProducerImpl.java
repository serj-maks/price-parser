package org.bot.priceparser.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.bot.priceparser.service.UpdateProducer;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
public class UpdateProducerImpl implements UpdateProducer {

    @Override
    public void produce(String rabbitQueue, Update update) {
        //TODO: create produce method
        log.debug(update.getMessage().getText());
    }
}
