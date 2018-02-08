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
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
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

    public void createNote(Message message) {
        String userText = message.getText();
        Date date;
        try {
            date = getDate(userText);
            Chat chat = ChatDAO.INSTANCE.getById(message.getChatId());
            String noteText = getNoteTextWithoutDate(userText);
            Note note = new Note(noteText, date, chat);
            NoteDAO.INSTANCE.create(note);
            send("OK, I will remind you: \n\n" + noteText + "\n\nat " + date, message.getChatId());
        } catch (ParseException e) {
            BotLogger.error("Cannot parse date from the message.", e);
            send("Cannot parse date from the message.", message.getChatId());
        }
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

    public void send(String text, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            RecollectBot.BOT.execute(message);
        } catch (TelegramApiException e) {
            BotLogger.error("Cannot send text.", e);
        }
    }

    public Date getDate(String noteText) throws ParseException {
        String fullDatePattern = "(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](2)\\d\\d\\d (0[0-9]|1[0-9]|2[0-3])[:]([0-5][0-9])";
        String datePattern = "(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](2)\\d\\d\\d";
        String timePattern = "(0[0-9]|1[0-9]|2[0-3])[:]([0-5][0-9])";
        String fullDate = parseLastCharacters(fullDatePattern, noteText);
        String date = parseLastCharacters(datePattern, noteText);
        String time = parseLastCharacters(timePattern, noteText);
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yyyy H:m");
        if (!fullDate.isEmpty()) {
            return fullDateFormat.parse(fullDate);
        } else if (!date.isEmpty()) {
            return fullDateFormat.parse(date + " 00:00");
        } else if (!time.isEmpty()) {
            String timeDate = LocalDate.now().getDayOfMonth() + "." + LocalDate.now().getMonthValue() + "." + LocalDate.now().getYear() + " " + time;
            return fullDateFormat.parse(timeDate);
        } else {
            throw new ParseException("Cannot parse date.", 1);
        }
    }

    //TODO duplicate code and magic numbers
    public String getNoteTextWithoutDate(String userText) throws ParseException {
        String fullDatePattern = "(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](2)\\d\\d\\d (0[0-9]|1[0-9]|2[0-3])[:]([0-5][0-9])";
        String datePattern = "(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](2)\\d\\d\\d";
        String timePattern = "(0[0-9]|1[0-9]|2[0-3])[:]([0-5][0-9])";
        String fullDate = parseLastCharacters(fullDatePattern, userText);
        String date = parseLastCharacters(datePattern, userText);
        String time = parseLastCharacters(timePattern, userText);
        if (!fullDate.isEmpty()) {
            return userText.substring(0, userText.length() - 16);
        } else if (!date.isEmpty()) {
            return userText.substring(0, userText.length() - 10);
        } else if (!time.isEmpty()) {
            return userText.substring(0, userText.length() - 5);
        } else {
            throw new ParseException("Cannot parse date.", 1);
        }
    }

    public String parseLastCharacters(String pattern, String message) {
        String result = "";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(message);
        while (m.find()) {
            if (m.end() == message.length()) { // Check if date is the last characters in the message
                result = message.substring(m.start(), m.end());
            }
        }
        return result;
    }
}
