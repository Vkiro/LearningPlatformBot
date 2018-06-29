package com.recollect.domain;

public class User {

  private Long id;

  private String firstName;

  private Boolean isBot;

  private String lastName;

  private String userName;

  private String languageCode;

  public User() {
  }

  public User(org.telegram.telegrambots.api.objects.User user) {
    this.id = Long.valueOf(user.getId());
    this.firstName = user.getFirstName();
    this.isBot = user.getBot();
    this.lastName = user.getLastName();
    this.userName = user.getUserName();
    this.languageCode = user.getLanguageCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass() || o.hashCode() != hashCode()) {
      return false;
    }

    User user = (User) o;

    return id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public Boolean getBot() {
    return isBot;
  }

  public void setBot(Boolean bot) {
    isBot = bot;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getLanguageCode() {
    return languageCode;
  }

  public void setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
  }
}
