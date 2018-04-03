package com.recollect.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Note {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Lob
  private String text;

  private Date date;

  private Boolean isSent = false;

  @ManyToOne
  @JoinColumn(name = "chat_id")
  private Chat chat;

  public Note() {
  }

  public Note(String text, Date date, Chat chat) {
    this.text = text;
    this.date = date;
    this.chat = chat;
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

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Boolean getSent() {
    return isSent;
  }

  public void setSent(Boolean sent) {
    isSent = sent;
  }

  public Chat getChat() {
    return chat;
  }

  public void setChat(Chat chat) {
    this.chat = chat;
  }
}
