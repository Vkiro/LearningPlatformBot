package com.recollect.dao;

import com.recollect.domain.Chat;
import org.hibernate.Session;

public enum ChatDAO {
  INSTANCE;

  public void create(Chat chat) {
    Session session = DBConnection.INSTANCE.openTransactionSession();
    session.save(chat);
    DBConnection.INSTANCE.closeTransactionSession(session);
  }

  public Chat getById(Long chatId) {
    Session session = DBConnection.INSTANCE.openSession();
    Chat chat = session.get(Chat.class, chatId);
    DBConnection.INSTANCE.closeSession(session);
    return chat;
  }
}
