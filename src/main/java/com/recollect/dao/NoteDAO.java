package com.recollect.dao;

import com.recollect.domain.Note;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
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

    public List<Note> getFirstOrderByDateNotSend() throws Exception {
        int thisYear = Year.now().getValue();
        int thisMonth = YearMonth.now().getMonthValue();
        int thisDay = MonthDay.now().getDayOfMonth();
        int thisHour = LocalTime.now().getHour();
        int thisMinute = LocalTime.now().getMinute();
        Session session = DBConnection.INSTANCE.getSession();
        //TODO CHANGE concat and logic
        Query query = session.createQuery("from Note where " +
                "year(date) =:thisYear AND " +
                "month(date) =:thisMonth AND " +
                "day(date) =:thisDay AND " +
                "hour(date) =:thisHour AND " +
                "minute(date) =:thisMinute AND " +
                "isSent = false " +
                "order by date");
        query.setParameter("thisYear", thisYear);
        query.setParameter("thisMonth", thisMonth);
        query.setParameter("thisDay", thisDay);
        query.setParameter("thisHour", thisHour);
        query.setParameter("thisMinute", thisMinute);
        List<Note> notes = query.getResultList();

        return notes;
    }

    public void setSent(List<Note> notes) throws ExceptionDAO {
        Session session = DBConnection.INSTANCE.getSession();
        session.beginTransaction();
        for (Note note : notes) {
            note.setSent(true);
            session.update(note);
        }
        session.getTransaction().commit();
        session.close();
    }

    public void create(Note note) throws ExceptionDAO {
        Session session = DBConnection.INSTANCE.getSession();
        session.beginTransaction();
        session.save(note);
        session.getTransaction().commit();
        session.close();
    }
}
