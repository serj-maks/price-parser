package org.bot.priceparser.dao;

import org.bot.priceparser.entity.TelegramUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramUpdateDao extends JpaRepository<TelegramUpdate, Long> {
}
