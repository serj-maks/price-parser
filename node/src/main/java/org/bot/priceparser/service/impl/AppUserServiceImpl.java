package org.bot.priceparser.service.impl;

import lombok.RequiredArgsConstructor;

import org.bot.priceparser.entity.AppUser;
import org.bot.priceparser.entity.enums.TUserState;
import org.bot.priceparser.repository.AppUserRepository;
import org.bot.priceparser.service.AppUserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    @Override
    public AppUser getByTelegramUserId(Update update) {
        User telegramUser = update.getMessage().getFrom();
        AppUser persistentAppUser = appUserRepository.findByTelegramUserId(telegramUser.getId());
        if(persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO: изменить значение по-умолчанию после добавления регистрации
                    .isActive(true)
                    //TODO: сделать значение BASIC по-умолчанию
                    .state(TUserState.BASIC)
                    .build();
            return appUserRepository.save(transientAppUser);
        }
        return persistentAppUser;
    }
}
