package org.bot.priceparser.service;

import lombok.RequiredArgsConstructor;

import org.bot.priceparser.entity.AppUser;
import org.bot.priceparser.entity.enums.BotState;
import org.bot.priceparser.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    @Transactional
    public AppUser getByTelegramUserId(Update update) {
        User telegramUser = update.getMessage().getFrom();
        AppUser persistentAppUser = appUserRepository.findByTelegramUserId(telegramUser.getId());

        if (persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO: изменить значение по-умолчанию после добавления регистрации
                    .isActive(true)
                    //TODO: сделать значение BASIC по-умолчанию
                    .state(BotState.BASIC)
                    .build();
            return appUserRepository.save(transientAppUser);
        }
        return persistentAppUser;
    }

    @Transactional
    public void save(AppUser appUser) {
        appUserRepository.save(appUser);
    }
}
