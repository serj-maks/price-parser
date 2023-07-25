package org.bot.priceparser.utils;

import lombok.RequiredArgsConstructor;
import org.bot.priceparser.controller.Bot;
import org.bot.priceparser.service.messagebroker.rabbitmq.UpdateProducer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.bot.priceparser.RabbitQueue.TEXT_MESSAGE_UPDATE;

@Component
@RequiredArgsConstructor
public class MessageUtils {

    private final UpdateProducer updateProducer;
    private final Bot bot;

    public void processMessage(Message message) {
        if (message.hasText()) {
            processTextMessageType(message);
        } else {
            precessUnsupportedMessageType(message);
        }
    }

    private void processTextMessageType(Message message) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, message);

    }

    private void precessUnsupportedMessageType(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Unsupported message type, please sent only text messages");
        bot.sendAnswerMessage(sendMessage);
    }
}
