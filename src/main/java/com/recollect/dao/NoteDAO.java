package com.recollect.dao;

import com.recollect.domain.Note;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public enum NoteDAO {
    INSTANCE;

    public List<Note> getAllByUserId(int id) throws ExceptionDAO {
        Session session = DBConnection.INSTANCE.getSession();
        //TODO CHANGE concat
        Query query = session.createQuery("from Note where user.id = " + id);
        List<Note> notes = query.getResultList();

        return notes;
    }

    public void create(Note note) throws ExceptionDAO {
        Session session = DBConnection.INSTANCE.getSession();
        session.beginTransaction();
        session.save(note);
        session.getTransaction().commit();
        session.close();
    }
}
