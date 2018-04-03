package com.recollect.controller;

import com.recollect.controller.MessageController;
import com.recollect.dao.ExceptionDAO;
import com.recollect.dao.NoteDAO;
import com.recollect.domain.Note;
import com.recollect.service.NoteService;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.logging.BotLogger;

public class TimeController implements Runnable {

  private static final int ONE_MINUTE = 60_000;

  @Override
  public void run() {
    List<Note> notes = new ArrayList<>();
    while (true) {
      try {
        notes = NoteDAO.INSTANCE.getFirstOrderByDateNotSend();
      } catch (Exception e) {

      }
      if (notes.isEmpty()) {
        try {
          Thread.sleep(ONE_MINUTE);
        } catch (InterruptedException e) {
          BotLogger.error("Error in TimeTracker thread.", e);
        }
      } else {
        notes.forEach(MessageController.INSTANCE::send);
        NoteService.INSTANCE.setSend(notes);
      }
    }
  }
}
