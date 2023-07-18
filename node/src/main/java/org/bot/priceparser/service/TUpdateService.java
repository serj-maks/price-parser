package org.bot.priceparser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bot.priceparser.entity.TUpdate;
import org.bot.priceparser.repository.TUpdateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
@RequiredArgsConstructor
public class TUpdateService {

    private final TUpdateRepository tUpdateRepository;

    @Transactional
    public void save(Update update) {
        //TODO: можно ли здесь сделать mapper ?
        TUpdate tUpdate = TUpdate.builder()
                .update(update)
                .build();
        tUpdateRepository.save(tUpdate);
    }
}
