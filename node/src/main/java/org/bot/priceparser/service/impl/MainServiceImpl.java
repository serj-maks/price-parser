package org.bot.priceparser.service.impl;

import org.bot.priceparser.dao.RawDataDao;
import org.bot.priceparser.entity.RawData;
import org.bot.priceparser.service.MainService;
import org.bot.priceparser.service.ProducerService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MainServiceImpl implements MainService {
    private final RawDataDao rawDataDao;
    private final ProducerService producerService;

    public MainServiceImpl(RawDataDao rawDataDao, ProducerService producerService) {
        this.rawDataDao = rawDataDao;
        this.producerService = producerService;
    }

    @Override
    public void processTextMessage(Update update) {
        save(update);
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("hello from node");
        producerService.produceAnswer(sendMessage);
    }

    private void save(Update update) {
        RawData rawData = RawData.builder()
                .update(update)
                .build();
        rawDataDao.save(rawData);
    }
}
