package com.recollect;

import com.recollect.dao.NoteDAO;
import com.recollect.domain.Note;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.List;

public class TimeTracker implements Runnable {
    private static final int ONE_MINUTE = 60_000;

    @Override
    public void run() {
        while (true) {
            List<Note> notes = NoteDAO.INSTANCE.getFirstOrderByDateNotSend();
            if (notes.isEmpty()) {
                try {
                    Thread.sleep(ONE_MINUTE);
                } catch (InterruptedException e) {
                    BotLogger.error("Error in TimeTracker thread.", e);
                }
            } else {
                for (Note note : notes) {
                    MessageController.INSTANCE.send(note);
                }
                NoteDAO.INSTANCE.setSent(notes);
            }
        }
    }
}
