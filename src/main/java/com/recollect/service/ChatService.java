package com.recollect.service;

import com.recollect.dao.ChatDAO;
import com.recollect.dao.ExceptionDAO;
import com.recollect.domain.Chat;
import org.telegram.telegrambots.logging.BotLogger;

public enum ChatService {
  INSTANCE;

  public void createChat(long chatId, long userId) {
    Chat chat = new Chat();
    chat.setId(chatId);
    chat.setUserId(userId);
    try {
      ChatDAO.INSTANCE.create(chat);
    } catch (ExceptionDAO edao) {
      BotLogger.info("This chat is already exists.", edao);
    }
  }

  public Chat getChat(long chatId) {
    return ChatDAO.INSTANCE.getById(chatId);
  }
}
