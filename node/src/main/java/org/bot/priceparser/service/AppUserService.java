package org.bot.priceparser.service;

import org.bot.priceparser.entity.AppUser;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface AppUserService {

    public AppUser getByTelegramUserId(Update update);
}
