package org.bot.priceparser.controller;

import lombok.extern.slf4j.Slf4j;
import org.bot.priceparser.service.UpdateProducer;
import org.bot.priceparser.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.bot.priceparser.RabbitQueue.*;

@Slf4j
@Component
public class UpdateController {
    private Bot bot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(Bot bot) {
        this.bot = bot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("received update is null");
            return;
        }
        if (update.hasMessage() || update.hasEditedMessage()) {
            distributeMessageByType(update);
        } else {
            log.error("received unsupported message type: " + update);
        }
    }

    private void distributeMessageByType(Update update) {
        Message message = update.getMessage();
        if (message.hasText()) {
            processTextMessageType(update);
        } else {
            setUnsupportedMessageType(update);
        }
    }

    private void processTextMessageType(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
        setTextMessageReceivedView(update);
    }

    private void setTextMessageReceivedView(Update update) {
        SendMessage sendMessage = messageUtils.generateSendMessageWithText(update, "Message has been received, please wait...");
        setView(sendMessage);
    }

    private void setUnsupportedMessageType(Update update) {
        SendMessage sendMessage = messageUtils.generateSendMessageWithText(update, "Unsupported message type, please sent only text messages");
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        bot.sendAnswerMessage(sendMessage);
    }
}
