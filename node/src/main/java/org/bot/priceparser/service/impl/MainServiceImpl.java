package org.bot.priceparser.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.bot.priceparser.dao.AppUserDao;
import org.bot.priceparser.dao.RawDataDao;
import org.bot.priceparser.entity.AppUser;
import org.bot.priceparser.entity.RawData;
import org.bot.priceparser.entity.enums.UserState;
import org.bot.priceparser.service.MainService;
import org.bot.priceparser.service.ProducerService;
import org.bot.priceparser.service.enums.TelegramCommands;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
@Service
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
        AppUser appUser = findOrSaveAppUser(update);
        UserState userState = appUser.getState();
        String command = update.getMessage().getText();
        String output = "";

        if (TelegramCommands.CANCEL.equals(command)) {
            //TODO: подумать над названием  метода
            output = cancelProcess(appUser);
        } else if (UserState.BASIC.equals(userState)) {
            //TODO: подумать над названием  метода
            output = processServiceCommand(appUser, command);
        } else if (UserState.WAIT_FOR_EMAIL.equals(userState)) {
            //TODO: добавить функционал: обработка e-mail
        } else {
            //TODO: завернуть в метод
            log.error("unknown user state" + userState);
            output = "unknown error! Enter /cancel command and try again";
        }

        Long chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    private String processServiceCommand(AppUser appUser, String command) {
        if (TelegramCommands.START.equals(command)) {
            return start();
        } else if (TelegramCommands.HELP.equals(command)) {
            return help();
        } else if (TelegramCommands.REGISTRATION.equals(command)) {
            //TODO: добавить функционал: регистрация
            return "temporary unavailable";
        } else {
            //TODO: когда отправляю боту текст, приходит это соообщение. См. dispatcher.UpdateController
            return "unknown command! Enter /help command to see all available commands";
        }
    }

    private String start() {
        return "hello! Enter /help command to see all available commands";
    }

    private String help() {
        return """
                commands list:
                /start
                /cancel
                /registration
                /help""";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(UserState.BASIC);
        appUserDao.save(appUser);
        //TODO: english grammar check
        return "command was been canceled";
    }

    //возвращает output с текстом отвата обратно в чат пользователю
    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.produceAnswer(sendMessage);
    }

    //TODO: разделить метод на два
    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
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
