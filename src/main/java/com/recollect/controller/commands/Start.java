package com.recollect.controller.commands;

import com.recollect.dao.ChatDAO;
import com.recollect.dao.ExceptionDAO;
import com.recollect.dao.UserDAO;
import com.recollect.domain.Chat;
import com.recollect.domain.User;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.logging.BotLogger;

public enum Start implements Command {
  INSTANCE;

  public static final String COMMAND = "/start";

  @Override
  public void execute(Message message) {
    User user = new User(message.getFrom());
    Chat chat = new Chat();
    chat.setId(message.getChatId());
    chat.setUser(user);
    try {
      UserDAO.INSTANCE.create(user);
      ChatDAO.INSTANCE.create(chat);
    } catch (ExceptionDAO edao) {
      BotLogger.info("This user or chat is already exists.", edao);
    }
  }
}
