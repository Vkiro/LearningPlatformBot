package com.recollect.dao;

import com.recollect.domain.Note;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.telegram.telegrambots.logging.BotLogger;

public enum NoteDAO {
  INSTANCE;

  public List<Note> getFirstOrderByDateNotSend() throws ExceptionDAO {
    int thisYear = Year.now().getValue();
    int thisMonth = YearMonth.now().getMonthValue();
    int thisDay = MonthDay.now().getDayOfMonth();
    int thisHour = LocalTime.now().getHour();
    BotLogger.config(""+thisHour, "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
    int thisMinute = LocalTime.now().getMinute();
    Session session = DBConnection.INSTANCE.openSession();
    Query query = session.createQuery("" +
        "FROM Note " +
        "WHERE " +
        "year(date) =:thisYear AND " +
        "month(date) =:thisMonth AND " +
        "day(date) =:thisDay AND " +
        "hour(date) =:thisHour AND " +
        "minute(date) =:thisMinute AND " +
        "isSent = false " +
        "ORDER BY date");
    query.setParameter("thisYear", thisYear);
    query.setParameter("thisMonth", thisMonth);
    query.setParameter("thisDay", thisDay);
    query.setParameter("thisHour", thisHour);
    query.setParameter("thisMinute", thisMinute);

    return (List<Note>) query.getResultList();
  }

  public void create(Note note) throws ExceptionDAO {
    Session session = DBConnection.INSTANCE.openTransactionSession();
    session.save(note);
    DBConnection.INSTANCE.closeTransactionSession(session);
  }

  public void update(Note note) {
    Session session = DBConnection.INSTANCE.openTransactionSession();
    session.update(note);
    DBConnection.INSTANCE.closeTransactionSession(session);
  }
}
