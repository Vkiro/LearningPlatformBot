package com.recollect;

import com.recollect.dao.ChatDAO;
import com.recollect.dao.NoteDAO;
import com.recollect.dao.UserDAO;
import com.recollect.domain.Chat;
import com.recollect.domain.Note;
import com.recollect.domain.User;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecollectBot extends TelegramLongPollingBot {

    public RecollectBot() {
        super();

        Thread myThready = new Thread(() -> {
            while (true) {
                List<Note> notes = NoteDAO.INSTANCE.getFirstOrderByDateNotSend();
                if (notes.isEmpty()) {
                    try {
                        Thread.sleep(60000); // one minute
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (Note note : notes) {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(note.getUser().getChat().getId());
                        sendMessage.setText(note.getText());
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    //TODO make isSent = true
                    NoteDAO.INSTANCE.setSent(notes);
                }
            }
        });
        myThready.start();
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(new RecollectBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public String getBotToken() {
        return "497884366:AAF6IKFeAINcTjahTr-uAMkURIbPVxvNWCQ";
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message.isCommand()) {
            List<Note> notes = NoteDAO.INSTANCE.getAllByUserId(message.getFrom().getId());
            sendListMessages(notes, message);
        } else {
            saveMessage(message);
        }
    }

    private void sendListMessages(List<Note> notes, Message message) {
        notes.forEach(note -> send(note, message));
    }

    private void send(Note note, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(note.getText());
        try {
            //TODO change to not deprecated
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void saveMessage(Message message) {
        //TODO change constructor and new user save in DB - old user continue
        Chat chat = new Chat();
        chat.setId(message.getChatId());
        User user = new User(new Long(message.getFrom().getId()), message.getFrom().getFirstName(), message.getFrom().getBot(), message.getFrom().getLastName(), message.getFrom().getUserName(), message.getFrom().getLanguageCode(), chat);
        try {
            ChatDAO.INSTANCE.create(chat);
            UserDAO.INSTANCE.create(user);
        } catch (Exception e) {
            System.out.println("This user is already exists");
        }

        // TODO export into another method
        String pattern = "(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](2)\\d\\d\\d";
        String text = message.getText();
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String textDate = text.substring(m.start(), m.end());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            try {
                Date date = dateFormat.parse(textDate);
                Note note = new Note(message.getText().replace(textDate, ""), date, user);
                NoteDAO.INSTANCE.create(note);
            } catch (Exception e) {
                System.out.println("Cannot parse date");
            }
        }
    }

    public String getBotUsername() {
        return "RecollectBot";
    }
}
