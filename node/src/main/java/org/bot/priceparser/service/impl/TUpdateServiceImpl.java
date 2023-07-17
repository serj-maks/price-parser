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
        stateCheck(update);
    }

    private void stateCheck(Update update) {
        AppUser appUser = appUserService.getByTelegramUserId(update);
        TUserState tUserState = appUser.getState();
        Long chatId = update.getMessage().getChatId();
        String commandFromUserChat = update.getMessage().getText();
        String answerToUserChat = "";

        if (TelegramCommands.CANCEL.equals(commandFromUserChat)) {
            answerToUserChat = cancel(appUser);
        }

        if (TUserState.BASIC.equals(tUserState)) {
            answerToUserChat = processServiceCommand(commandFromUserChat);
        } else if (TUserState.WAIT_FOR_EMAIL.equals(tUserState)) {
            //TODO: добавить функционал: обработка e-mail
        } else {
            //TODO: завернуть в метод
            log.error("unknown user state" + tUserState);
            answerToUserChat = "unknown error! Enter /cancel command and try again";
        }

        sendAnswer(answerToUserChat, chatId);
    }

    private String processServiceCommand(String command) {
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

    private String cancel(AppUser appUser) {
        appUser.setState(TUserState.BASIC);
        appUserRepository.save(appUser);
        //TODO: english grammar check
        return "command was been canceled";
    }

    //возвращает output с текстом отвата обратно в чат пользователю
    private void sendAnswer(String answerToUserChat, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(answerToUserChat);
        producerService.produceAnswer(sendMessage);
    }

    private void save(Update update) {
        //TODO: можно ли здесь сделать mapper ?
        TUpdate telegramUpdate = TUpdate.builder()
                .update(update)
                .build();
        tUpdateRepository.save(telegramUpdate);
    }
}
