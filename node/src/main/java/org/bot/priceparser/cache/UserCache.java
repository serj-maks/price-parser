package org.bot.priceparser.cache;

import org.bot.priceparser.entity.enums.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserCache {

    private final Map<Long, BotState> userBotStates = new HashMap<>();

    public void setBotState(long chatId, BotState botState) {
        userBotStates.put(chatId, botState);
    }

    public BotState getBotState(long chatId) {
        BotState botState = userBotStates.get(chatId);

        if (botState == null) {
            botState = BotState.START;
        }

        return botState;
    }
}
