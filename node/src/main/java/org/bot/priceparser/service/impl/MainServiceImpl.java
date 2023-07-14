package org.bot.priceparser.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.bot.priceparser.dao.AppUserDao;
import org.bot.priceparser.dao.TelegramUpdateDao;
import org.bot.priceparser.entity.AppUser;
import org.bot.priceparser.entity.TelegramUpdate;
import org.bot.priceparser.entity.enums.TelegramUserState;
import org.bot.priceparser.service.MainService;
import org.bot.priceparser.telegram.enums.TelegramCommands;
import org.bot.priceparser.service.messagebroker.rabbitmq.ProducerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
@Service
//TODO: разбить на сервисы, как в проекте Ильи: каждому entity свой сервис
//можно сделать фасад для сервисов
public class MainServiceImpl implements MainService {
    private final TelegramUpdateDao telegramUpdateDao;
    private final ProducerService producerService;
    private final AppUserDao appUserDao;

    public MainServiceImpl(TelegramUpdateDao updateDao, ProducerService producerService, AppUserDao appUserDao) {
        this.telegramUpdateDao = updateDao;
        this.producerService = producerService;
        this.appUserDao = appUserDao;
    }

    @Override
    public void processTextMessage(org.telegram.telegrambots.meta.api.objects.Update update) {
        save(update);
        AppUser appUser = getByTelegramUserId(update);
        TelegramUserState userState = appUser.getState();
        String command = update.getMessage().getText();
        String output = "";

        if (TelegramCommands.CANCEL.equals(command)) {
            //TODO: подумать над названием  метода
            output = cancelCommand(appUser);
        } else if (TelegramUserState.BASIC.equals(userState)) {
            //TODO: подумать над названием  метода
            output = processServiceCommand(appUser, command);
        } else if (TelegramUserState.WAIT_FOR_EMAIL.equals(userState)) {
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
            return startCommand();
        } else if (TelegramCommands.HELP.equals(command)) {
            return helpCommand();
        } else if (TelegramCommands.REGISTRATION.equals(command)) {
            //TODO: добавить функционал: регистрация
            return "temporary unavailable";
        } else {
            //TODO: когда отправляю боту текст, приходит это соообщение. См. dispatcher.UpdateController
            return "unknown command! Enter /help command to see all available commands";
        }
    }

    private String startCommand() {
        return "hello! Enter /help command to see all available commands";
    }

    private String helpCommand() {
        return """
                commands list:
                /start
                /cancel
                /registration
                /help""";
    }

    private String cancelCommand(AppUser appUser) {
        appUser.setState(TelegramUserState.BASIC);
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
    private AppUser getByTelegramUserId(org.telegram.telegrambots.meta.api.objects.Update update) {
        User telegramUser = update.getMessage().getFrom();
        AppUser persistentAppUser = appUserDao.findByTelegramUserId(telegramUser.getId());
        if(persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO: изменить значение по-умолчанию после добавления регистрации
                    .isActive(true)
                    //TODO: сделать значение BASIC по-умолчанию
                    .state(TelegramUserState.BASIC)
                    .build();
            return appUserDao.save(transientAppUser);
        }
        return persistentAppUser;
    }

    private void save(Update update) {
        TelegramUpdate telegramUpdate = TelegramUpdate.builder()
                .update(update)
                .build();
        telegramUpdateDao.save(telegramUpdate);
    }
}
