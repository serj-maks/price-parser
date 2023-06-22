package org.bot.priceparser.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            SendMessage sendMessage = new SendMessage();
            Long chatId = message.getChatId();
            sendMessage.setChatId(String.valueOf(chatId));
            String text = message.getText();
            sendMessage.setText(text);
            sendAnswerMessage(sendMessage);
        }
    }

    @PostConstruct
    public void init() {
        log.info("bot started with name: {}, token: {}", name, token);
    }

    public void sendAnswerMessage(SendMessage sendMessage) {
        if (sendMessage != null) {
            try {
                execute(sendMessage);
                log.info("Sent message: \"{}\" to chatId: {}", sendMessage.getText(), sendMessage.getChatId());
            } catch (TelegramApiException e) {
                log.error("Failed to send message: \"{}\" to chatId: {} due to error: {}", sendMessage.getText(), sendMessage.getChatId(), e.getMessage());
            }
        }
    }
}
