package com.recollect.dao;

import com.recollect.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.hibernate.Session;

public enum UserDAO {
  INSTANCE;

  public void create(User user) throws ExceptionDAO {
    String query = "INSERT INTO User (id, firstName, isBot, languageCode, lastName, userName) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection connection = DBConnection.INSTANCE.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setLong(1, user.getId());
      statement.setString(2, user.getFirstName());
      statement.setBoolean(3, user.getBot());
      statement.setString(4, user.getLanguageCode());
      statement.setString(5, user.getLastName());
      statement.setString(6, user.getUserName());
      statement.executeUpdate();
    } catch (SQLException | ClassNotFoundException e) {
      throw new ExceptionDAO("Can`t create user", e);
    }
  }
}
