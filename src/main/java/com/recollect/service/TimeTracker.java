package com.recollect.service;

import com.recollect.controller.MessageController;
import com.recollect.dao.NoteDAO;
import com.recollect.domain.Note;
import java.util.List;
import org.telegram.telegrambots.logging.BotLogger;

public class TimeTracker implements Runnable {

  private static final int ONE_MINUTE = 60_000;

  @Override
  public void run() {
    while (true) {
      try {
        List<Note> notes = NoteDAO.INSTANCE.getFirstOrderByDateNotSend();
        if (!notes.isEmpty()) {
          for (Note note : notes) {
            MessageController.INSTANCE.sendResponse(note.getText(), note.getChatId());
          }
          NoteService.INSTANCE.setSend(notes);
        }
        Thread.sleep(ONE_MINUTE);
      } catch (InterruptedException e) {
        BotLogger.error(e.getMessage(), "Thread TimeTracker error.");
      }
    }
  }
}
