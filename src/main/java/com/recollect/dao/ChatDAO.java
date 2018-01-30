package com.recollect.dao;

import com.recollect.domain.Chat;
import org.hibernate.Session;

public enum ChatDAO {
    INSTANCE;

    public void create(Chat chat) throws ExceptionDAO {
        Session session = DBConnection.INSTANCE.getSession();
        session.beginTransaction();
        session.save(chat);
        session.getTransaction().commit();
        session.close();
    }
}
