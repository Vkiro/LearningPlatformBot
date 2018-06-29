package com.recollect.domain;

import java.sql.Timestamp;

public class Note {

  private Long id;

  private String text;

  private Timestamp date;

  private Boolean isSent = false;

  private Long chatId;

  public Note() {
  }

  public Note(Long id, String text, Timestamp date, Boolean isSent, Long chatId) {
    this.id = id;
    this.text = text;
    this.date = date;
    this.isSent = isSent;
    this.chatId = chatId;
  }

  public Note(String noteText, java.util.Date date, Chat chat) {
    this.text = noteText;
    this.date = new Timestamp(date.getTime());
    this.chatId = chat.getId();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Timestamp getDate() {
    return date;
  }

  public void setDate(Timestamp date) {
    this.date = date;
  }

  public Boolean getSent() {
    return isSent;
  }

  public void setSent(Boolean sent) {
    isSent = sent;
  }

  public Long getChatId() {
    return chatId;
  }

  public void setChatId(Long chatId) {
    this.chatId = chatId;
  }

}
