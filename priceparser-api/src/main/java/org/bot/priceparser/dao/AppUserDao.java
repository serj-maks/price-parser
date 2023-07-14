package org.bot.priceparser.dao;

import org.bot.priceparser.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserDao extends JpaRepository<AppUser, Long> {
    AppUser findByTelegramUserId(Long id);
}
