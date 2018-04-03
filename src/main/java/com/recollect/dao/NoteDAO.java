package com.recollect.dao;

import com.recollect.domain.Note;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public enum NoteDAO {
  INSTANCE;

  public List<Note> getFirstOrderByDateNotSend() throws ExceptionDAO {
    int thisYear = Year.now().getValue();
    int thisMonth = YearMonth.now().getMonthValue();
    int thisDay = MonthDay.now().getDayOfMonth();
    int thisHour = LocalTime.now().getHour() + 3; // TODO FIX TIMEZONE BUG
    int thisMinute = LocalTime.now().getMinute();
    String query = "SELECT id, date, isSent, text, chatId FROM Note "
        + "WHERE "
        + "year(date) = ? AND "
        + "month(date) = ? AND "
        + "day(date) = ? AND "
        + "hour(date) = ? AND "
        + "minute(date) = ? AND "
        + "isSent = FALSE "
        + "ORDER BY date";
    List<Note> notes = new ArrayList<>();
    try (Connection connection = DBConnection.INSTANCE.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, thisYear);
      statement.setInt(2, thisMonth);
      statement.setInt(3, thisDay);
      statement.setInt(4, thisHour);
      statement.setInt(5, thisMinute);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          Note note = new Note();
          note.setId(resultSet.getLong("id"));
          note.setDate(resultSet.getTimestamp("date"));
          note.setSent(resultSet.getBoolean("isSent"));
          note.setText(resultSet.getString("text"));
          note.setChatId(resultSet.getLong("chatId"));
          notes.add(note);
        }
      } catch (SQLException sqle) {
        throw new ExceptionDAO("Can`t get resultSet of all notes.", sqle);
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new ExceptionDAO("Can`t create connection with database.", e);
    }
    return notes;
  }

  public void create(Note note) throws ExceptionDAO {
    String query = "INSERT INTO Note (date, isSent, text, chatId) VALUES (?, ?, ?, ?)";
    try (Connection connection = DBConnection.INSTANCE.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setTimestamp(1, note.getDate());
      statement.setBoolean(2, note.getSent());
      statement.setString(3, note.getText());
      statement.setLong(4, note.getChatId());
      statement.executeUpdate();
    } catch (SQLException | ClassNotFoundException e) {
      throw new ExceptionDAO("Can`t create note.", e);
    }
  }

  public void update(Note note) {
    String query = "UPDATE Note SET date = ?, isSent = ?, text = ?, chatId = ? WHERE id = ?";
    try (Connection connection = DBConnection.INSTANCE.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setTimestamp(1, note.getDate());
      statement.setBoolean(2, note.getSent());
      statement.setString(3, note.getText());
      statement.setLong(4, note.getChatId());
      statement.setLong(5, note.getId());
      statement.executeUpdate();
    } catch (SQLException | ClassNotFoundException e) {
      throw new ExceptionDAO("Can`t update note.", e);
    }
  }
}
