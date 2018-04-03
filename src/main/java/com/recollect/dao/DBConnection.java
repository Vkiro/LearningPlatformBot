package com.recollect.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public enum DBConnection {
  INSTANCE;

  private Configuration configuration = new Configuration().configure();
  private SessionFactory sessionFactory = configuration.buildSessionFactory();

  public Session openSession() {
    return sessionFactory.openSession();
  }

  public Session openTransactionSession() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    return session;
  }

  public void closeSession(Session session) {
    session.close();
  }

  public void closeTransactionSession(Session session) {
    session.getTransaction().commit();
    session.close();
  }
}
