package org.bot.priceparser.dao;

import org.bot.priceparser.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDao extends JpaRepository<Item, Long> {
}
