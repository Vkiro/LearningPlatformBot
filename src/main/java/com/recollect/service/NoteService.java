package com.recollect.service;

import com.recollect.dao.NoteDAO;
import com.recollect.domain.Note;
import java.util.List;

public enum NoteService {
  INSTANCE;

  public void setSend(List<Note> notes) {
    for (Note note : notes) {
      note.setSent(true);
      NoteDAO.INSTANCE.update(note);
    }
  }

}
