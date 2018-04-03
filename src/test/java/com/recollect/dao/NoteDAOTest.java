package com.recollect.dao;

import static org.junit.Assert.*;

import com.recollect.domain.Note;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import org.junit.Test;

public class NoteDAOTest {

  @Test
  public void getFirstOrderByDateNotSend() throws Exception {
    NoteDAO.INSTANCE.getFirstOrderByDateNotSend();
  }

  @Test
  public void create() throws Exception {
    Note note = new Note();
    note.setChatId(333899786L);
    note.setText("BlaBlaCar");
    note.setSent(false);
    note.setDate(new Timestamp(System.currentTimeMillis()));
    NoteDAO.INSTANCE.create(note);
  }

  @Test
  public void update() throws Exception {
    Note note = new Note();
    note.setId(61L);
    note.setChatId(333899786L);
    note.setText("AAAAAAAAAAAAaaaaaaaaaaaaaa");
    note.setSent(false);
    note.setDate(new Timestamp(System.currentTimeMillis()));
    NoteDAO.INSTANCE.update(note);
  }

}