package org.bot.priceparser.service.impl;

import org.bot.priceparser.dao.AppUserDao;
import org.bot.priceparser.dao.RawDataDao;
import org.bot.priceparser.entity.AppUser;
import org.bot.priceparser.entity.RawData;
import org.bot.priceparser.entity.enums.UserState;
import org.bot.priceparser.service.MainService;
import org.bot.priceparser.service.ProducerService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class MainServiceImpl implements MainService {
    private final RawDataDao rawDataDao;
    private final ProducerService producerService;
    private final AppUserDao appUserDao;

    public MainServiceImpl(RawDataDao rawDataDao, ProducerService producerService, AppUserDao appUserDao) {
        this.rawDataDao = rawDataDao;
        this.producerService = producerService;
        this.appUserDao = appUserDao;
    }

    @Override
    public void processTextMessage(Update update) {
        save(update);
        Message textMessage = update.getMessage();
        User telegramUser = textMessage.getFrom();
        AppUser appUser = findOrSaveAppUser(telegramUser);

        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("hello from node");
        producerService.produceAnswer(sendMessage);
    }

    //TODO: разделить метод на два
    private AppUser findOrSaveAppUser(User telegramUser) {
        AppUser persistentAppUser = appUserDao.findAppUserByTelegramUserId(telegramUser.getId());
        if(persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO: изменить значение по-умолчанию после добавления регистрации
                    .isActive(true)
                    //TODO: сделать значение BASIC по-умолчанию
                    .state(UserState.BASIC)
                    .build();
            return appUserDao.save(transientAppUser);
        }
        return persistentAppUser;
    }

    private void save(Update update) {
        RawData rawData = RawData.builder()
                .update(update)
                .build();
        rawDataDao.save(rawData);
    }
}
