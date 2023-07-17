package org.bot.priceparser.repository;

import org.bot.priceparser.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByTelegramUserId(Long id);
}
