package org.bot.priceparser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.priceparser.command.enums.TelegramCommands;
import org.bot.priceparser.service.messagebroker.rabbitmq.ProducerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramFacade {

    private final ProducerService producerService;

    public void handleMessage(Message message) {
        String inputMessage = message.getText();
        Long chatId = message.getChatId();
        String outputMessage = processServiceCommand(inputMessage);
        sendAnswer(outputMessage, chatId);
    }

    private String processServiceCommand(String command) {
        if (TelegramCommands.START.equals(command)) {
            return startCommand();
        } else if (TelegramCommands.CANCEL.equals(command)) {
            return cancelCommand();
        } else if (TelegramCommands.HELP.equals(command)) {
            return helpCommand();
        } else if (TelegramCommands.REGISTRATION.equals(command)) {
            return registrationCommand();
        } else {
            //TODO: когда отправляю боту текст, приходит это соообщение. См. dispatcher.UpdateController
            return "unknown command! Enter /help command to see all available commands";
        }
    }

    private String registrationCommand() {
        return "temporary unavailable";
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

    public String cancelCommand() {
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
