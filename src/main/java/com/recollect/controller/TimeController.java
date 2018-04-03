package com.recollect.controller;

import com.recollect.controller.MessageController;
import com.recollect.dao.ExceptionDAO;
import com.recollect.dao.NoteDAO;
import com.recollect.domain.Note;
import com.recollect.service.NoteService;
import java.util.List;
import org.telegram.telegrambots.logging.BotLogger;

public class TimeController implements Runnable {

  private static final int ONE_MINUTE = 60_000;

  @Override
  public void run() {
    while (true) {
      try {
        List<Note> notes = NoteDAO.INSTANCE.getFirstOrderByDateNotSend();
        if (notes.isEmpty()) {
          Thread.sleep(ONE_MINUTE);
        } else {
          notes.forEach(MessageController.INSTANCE::send);
          NoteService.INSTANCE.setSend(notes);
        }
      } catch (ExceptionDAO edao) {
        BotLogger.error("TimeController error getting notes from DB.", edao);
      } catch (InterruptedException ie) {
        BotLogger.error("TimeController error with thread.", ie);
      }
    }
  }
}
