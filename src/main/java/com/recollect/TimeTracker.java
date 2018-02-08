package com.recollect;

import com.recollect.dao.NoteDAO;
import com.recollect.domain.Note;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.List;

public class TimeTracker implements Runnable {
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
                    MessageController.INSTANCE.send("lol", 333899786);
                } catch (InterruptedException e) {
                    BotLogger.error("Error in TimeTracker thread.", e);
                }
            } else {
                notes.forEach(MessageController.INSTANCE::send);
                NoteDAO.INSTANCE.setSent(notes);
            }
        }
    }
}
