package org.bot.priceparser.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bot.priceparser.entity.AppUser;
import org.bot.priceparser.entity.TUpdate;
import org.bot.priceparser.entity.enums.TUserState;
import org.bot.priceparser.repository.AppUserRepository;
import org.bot.priceparser.repository.TUpdateRepository;
import org.bot.priceparser.service.AppUserService;
import org.bot.priceparser.service.TUpdateService;
import org.bot.priceparser.service.messagebroker.rabbitmq.ProducerService;
import org.bot.priceparser.command.enums.TelegramCommands;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
@RequiredArgsConstructor
public class TUpdateServiceImpl implements TUpdateService {

    private final TUpdateRepository tUpdateRepository;
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
    private final ProducerService producerService;

    @Override
    public void process(Update update) {
        save(update);
        AppUser appUser = appUserService.getByTelegramUserId(update);
        TUserState userState = appUser.getState();
        String command = update.getMessage().getText();
        String output = "";

        if (TelegramCommands.CANCEL.equals(command)) {
            //TODO: подумать над названием  метода
            output = cancelCommand(appUser);
        } else if (TUserState.BASIC.equals(userState)) {
            //TODO: подумать над названием  метода
            output = processServiceCommand(appUser, command);
        } else if (TUserState.WAIT_FOR_EMAIL.equals(userState)) {
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
        appUser.setState(TUserState.BASIC);
        appUserRepository.save(appUser);
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

    private void save(Update update) {
        TUpdate telegramUpdate = TUpdate.builder()
                .update(update)
                .build();
        tUpdateRepository.save(telegramUpdate);
    }
}
