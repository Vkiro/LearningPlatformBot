package com.recollect.dao;

import com.recollect.domain.User;
import org.hibernate.Session;

public enum UserDAO {
  INSTANCE;

  public void create(User user) throws ExceptionDAO {
    Session session = DBConnection.INSTANCE.openTransactionSession();
    session.save(user);
    DBConnection.INSTANCE.closeTransactionSession(session);
  }
}
