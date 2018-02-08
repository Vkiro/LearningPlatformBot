package com.recollect.dao;

import com.recollect.domain.Chat;
import org.hibernate.Session;
import org.hibernate.query.Query;

public enum ChatDAO {
    INSTANCE;

    public void create(Chat chat) {
        Session session = DBConnection.INSTANCE.getSession();
        session.beginTransaction();
        session.save(chat);
        session.getTransaction().commit();
        session.close();
    }

    public Chat getById(Long chatId) {
        Session session = DBConnection.INSTANCE.getSession();
        session.beginTransaction();
        Query query = session.createQuery("from Chat where id = " + chatId);
        Chat chat = (Chat) query.getSingleResult();
        session.getTransaction().commit();
        session.close();

        return chat;
    }
}
