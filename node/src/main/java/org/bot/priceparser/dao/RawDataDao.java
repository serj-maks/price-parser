package org.bot.priceparser.dao;

import org.bot.priceparser.entity.RawData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawDataDao extends JpaRepository<RawData, Long> {
}
