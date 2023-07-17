package org.bot.priceparser.dto.telegramupdate;

import org.telegram.telegrambots.meta.api.objects.Update;

public record TUpdateDto(Long id,
                                Update update) {
}
