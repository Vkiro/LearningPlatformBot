package com.recollect;

import com.recollect.commands.Command;
import com.recollect.commands.Start;
import com.recollect.dao.ChatDAO;
import com.recollect.dao.NoteDAO;
import com.recollect.domain.Chat;
import com.recollect.domain.Note;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MessageController {
    INSTANCE;

    private Map<String, Command> commands = new HashMap<>();

    MessageController() {
        initCommands();
    }

    private void initCommands() {
        commands.put(Start.COMMAND, Start.INSTANCE);
    }

    public void handleIncomingMessage(Message message) {
        if (message.isCommand()) {
            Command command = commands.get(message.getText());
            command.execute(message);
        } else {
            createNote(message);
        }
    }

    public void sendListMessages(List<Note> notes) {
        notes.forEach(this::send);
    }

    public void send(Note note) {
        SendMessage message = new SendMessage();
        message.setChatId(note.getChat().getId());
        message.setText(note.getText());
        try {
            RecollectBot.BOT.execute(message);
        } catch (TelegramApiException e) {
            BotLogger.error("Cannot send note.", e);
        }
    }

    public void createNote(Message message) {
        String noteText = message.getText();
        Date date;
        try {
            date = getDate(noteText);
            Chat chat = ChatDAO.INSTANCE.getById(message.getChatId());
            Note note = new Note(noteText, date, chat);
            NoteDAO.INSTANCE.create(note);
        } catch (ParseException e) {
            BotLogger.error("Cannot parse date from the message", e);
        }
    }

    public Date getDate(String noteText) throws ParseException {
        String datePattern = "(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](2)\\d\\d\\d";
        String timePattern = "(0[0-9]|1[0-9]|2[0-3])[:]([0-5][0-9])";
        String date = parse(datePattern, noteText);
        String time = parse(timePattern, noteText);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy H:m");
        Date remindDate = dateFormat.parse(date + " " + time);
        return remindDate;
    }

    public String parse(String pattern, String message) {
        String result = "";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(message);
        while (m.find()) {
            result = message.substring(m.start(), m.end());
        }
        return result;
    }
}
