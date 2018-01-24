package com.recollect.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public enum DBConnection {
    INSTANCE;

    private Configuration configuration = new Configuration().configure();
    private SessionFactory sessionFactory = configuration.buildSessionFactory();

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
