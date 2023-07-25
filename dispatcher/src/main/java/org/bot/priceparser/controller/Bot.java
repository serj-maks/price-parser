package org.bot.priceparser.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class Bot extends TelegramWebhookBot {

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.url}")
    private String url;

    @PostConstruct
    public void init() {
        SetWebhook setWebhook = SetWebhook.builder()
                .url(url)
                .build();
        try {
            this.setWebhook(setWebhook);
            log.info("bot started with name: {}, token: {}", name, token);
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotPath() {
        return "/update";
    }

    //не понадобился, поэтому оставил без реализации
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
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
