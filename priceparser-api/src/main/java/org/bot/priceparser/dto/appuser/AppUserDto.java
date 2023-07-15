package org.bot.priceparser.dto.appuser;

public record AppUserDto(Long id,
                         Long telegramUserId,
                         String firstName,
                         String lastName) {
}
