package com.recollect.dao;

import com.recollect.domain.Chat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public enum ChatDAO {
  INSTANCE;

  public void create(Chat chat) {
    String query = "INSERT INTO Chat (id, userId) VALUES (?, ?)";
    try (Connection connection = DBConnection.INSTANCE.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setLong(1, chat.getId());
      statement.setLong(2, chat.getUserId());
      statement.executeUpdate();
    } catch (SQLException | ClassNotFoundException e) {
      throw new ExceptionDAO("Can`t create new chat.", e);
    }
  }

  public Chat getById(Long chatId) {
    String query = "SELECT id, userId FROM Chat WHERE id = ?";
    Chat chat;
    try (Connection connection = DBConnection.INSTANCE.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setLong(1, chatId);
      try (ResultSet resultSet = statement.executeQuery()) {
        resultSet.next();
        chat = new Chat();
        chat.setId(resultSet.getLong("id"));
        chat.setUserId(resultSet.getLong("userId"));
      } catch (SQLException sqle) {
        throw new ExceptionDAO("Can`t get resultSet of one chat.", sqle);
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new ExceptionDAO("Can`t create connection with database.", e);
    }
    return chat;
  }
}
