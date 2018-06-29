package com.recollect.service;

import com.recollect.dao.ExceptionDAO;
import com.recollect.dao.UserDAO;
import com.recollect.domain.User;
import org.telegram.telegrambots.logging.BotLogger;

public enum UserService {
  INSTANCE;

  public void createUser(org.telegram.telegrambots.api.objects.User telegramUser) {
    User user = new User(telegramUser);
    try {
      UserDAO.INSTANCE.create(user);
    } catch (ExceptionDAO edao) {
      BotLogger.info("This user is already exists.", edao);
    }
  }
}
