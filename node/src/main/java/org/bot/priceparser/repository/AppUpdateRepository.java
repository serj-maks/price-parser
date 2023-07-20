package org.bot.priceparser.repository;

import org.bot.priceparser.entity.AppUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUpdateRepository extends JpaRepository<AppUpdate, Long> {
}
