package org.bot.priceparser.repository;

import org.bot.priceparser.entity.TUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TUpdateRepository extends JpaRepository<TUpdate, Long> {
}
