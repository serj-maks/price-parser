package org.bot.priceparser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.priceparser.command.enums.TelegramCommands;
import org.bot.priceparser.entity.AppUser;
import org.bot.priceparser.entity.enums.TUserState;
import org.bot.priceparser.service.messagebroker.rabbitmq.ProducerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainMenuService {

    private final TUpdateService tUpdateService;
    private final AppUserService appUserService;
    private final ProducerService producerService;

    @Transactional
    public void process(Update update) {
        AppUser appUser = appUserService.getByTelegramUserId(update);
        TUserState tUserState = appUser.getState();
        Long chatId = update.getMessage().getChatId();
        String commandFromUserChat = update.getMessage().getText();
        String answerToUserChat = "";

        if (TUserState.BASIC.equals(tUserState)) {
            answerToUserChat = processServiceCommand(appUser, commandFromUserChat);
        } else if (TUserState.WAIT_FOR_EMAIL.equals(tUserState)) {
            //TODO: добавить функционал: обработка e-mail
        } else {
            //TODO: завернуть в метод
            log.error("unknown user state" + tUserState);
            answerToUserChat = "unknown error! Enter /cancel command and try again";
        }

        //TODO: команда должна отвечать, а не метод process
        sendAnswer(answerToUserChat, chatId);
    }

    private String processServiceCommand(AppUser appUser, String command) {
        if (TelegramCommands.START.equals(command)) {
            return startCommand();
        } else if (TelegramCommands.CANCEL.equals(command)) {
            return cancelCommand(appUser);
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

    @Transactional
    public String cancelCommand(AppUser appUser) {
        appUser.setState(TUserState.BASIC);
        appUserService.save(appUser);
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
}
