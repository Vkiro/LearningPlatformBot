package com.recollect.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DBConnection {
  INSTANCE;

  public static final String URL = "jdbc:mysql://us-cdbr-iron-east-05.cleardb.net/heroku_c769909265655ba?autoReconnect=true&useSSL=false";
  private static final String LOGIN = "b6337687f25bdc";
  private static final String PASSWORD = "b043b04a";

  public Connection getConnection() throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.jdbc.Driver");
    return DriverManager.getConnection(URL, LOGIN, PASSWORD);
  }
}