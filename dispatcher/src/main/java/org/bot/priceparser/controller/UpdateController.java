package org.bot.priceparser.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.priceparser.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateController {

    private final MessageUtils messageUtils;

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("received update is null");
            return;
        }
        distributeUpdateByType(update);
    }

    private void distributeUpdateByType(Update update) {
        if (update.hasMessage() || update.hasEditedMessage()) {
            Message message = update.getMessage();
            messageUtils.processMessage(message);
        } else {
            log.error("received unsupported message type: " + update);
        }
    }

//    private void setTextMessageReceivedView(Update update) {
//        SendMessage sendMessage = messageUtils.generateSendMessageWithText(update, "Message has been received, please wait...");
//        setView(sendMessage);
//    }
}
