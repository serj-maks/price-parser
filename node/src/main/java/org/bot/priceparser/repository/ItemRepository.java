package org.bot.priceparser.repository;

import org.bot.priceparser.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
