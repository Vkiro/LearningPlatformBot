package com.recollect.dao;

import org.junit.Test;

public class NoteDAOTest {

  @Test
  public void getFirstOrderByDateNotSend() {
    NoteDAO.INSTANCE.getFirstOrderByDateNotSend();
  }
}
