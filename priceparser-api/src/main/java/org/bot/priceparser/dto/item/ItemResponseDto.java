package org.bot.priceparser.dto.item;

public record ItemResponseDto(Long id,
                              String link,
                              Long startPrice,
                              Long currentPrice) {
}
