package com.recollect.service;

import com.recollect.dao.NoteDAO;
import com.recollect.domain.Chat;
import com.recollect.domain.Note;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.logging.BotLogger;

public enum NoteService {
  INSTANCE;

  private static final String FULL_DATE_PATTERN = "(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])"
      + "[.](2)\\d\\d\\d (0[0-9]|1[0-9]|2[0-3])[:]([0-5][0-9])";
  private static final String DATE_PATTERN = "(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](2)"
      + "\\d\\d\\d";
  private static final String TIME_PATTERN = "(0[0-9]|1[0-9]|2[0-3])[:]([0-5][0-9])";
  private static final int FULL_DATE_LENGTH = 16;
  private static final int DATE_LENGTH = 10;
  private static final int TIME_LENGTH = 5;

  public String createNote(Message message) {
    String userMessage = message.getText();
    Date date;
    try {
      String stringDate = getStringDate(userMessage);
      date = getDate(stringDate);
      int timeZoneHours = TimeZoneTracker.getHours(message.getFrom().getLanguageCode());
      date.setHours(date.getHours() - timeZoneHours); // Make time GMT + 0
      Chat chat = ChatService.INSTANCE.getChat(message.getChatId());
      String messageWithoutDate = userMessage.replace(stringDate, "");
      Note note = new Note(messageWithoutDate, date, chat);
      NoteDAO.INSTANCE.create(note);
      return ("OK, I will remind you: \n\n" + messageWithoutDate + "\n\nat " + stringDate);
    } catch (ParseException e) {
      BotLogger.error("Cannot parse date from the message.", e);
      return ("Cannot parse date from the message.");
    }
  }

  private Date getDate(String stringDate) throws ParseException {
    SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yyyy H:m");
    if (FULL_DATE_LENGTH == stringDate.length()) {
      return fullDateFormat.parse(stringDate);
    } else if (DATE_LENGTH == stringDate.length()) {
      return fullDateFormat.parse(stringDate + " 00:00");
    } else if (TIME_LENGTH == stringDate.length()) {
      String timeDate =
          LocalDate.now().getDayOfMonth() + "." + LocalDate.now().getMonthValue() + "." + LocalDate
              .now().getYear() + " " + stringDate;
      return fullDateFormat.parse(timeDate);
    } else {
      throw new ParseException("Cannot parse date.", 1);
    }
  }

  private String getStringDate(String message) throws ParseException {
    String fullDate = parseLastCharacters(FULL_DATE_PATTERN, message);
    String date = parseLastCharacters(DATE_PATTERN, message);
    String time = parseLastCharacters(TIME_PATTERN, message);
    if (!fullDate.isEmpty()) {
      return fullDate;
    } else if (!date.isEmpty()) {
      return date;
    } else if (!time.isEmpty()) {
      return time;
    } else {
      throw new ParseException("Cannot parse date.", 1);
    }
  }

  private String parseLastCharacters(String pattern, String message) {
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

  public int calculateTimeZoneHours(String languageCode){
    return TimeZoneTracker.getHours(languageCode);
  }

  public void setSend(List<Note> notes) {
    for (Note note : notes) {
      note.setSent(true);
      NoteDAO.INSTANCE.update(note);
    }
  }
}
