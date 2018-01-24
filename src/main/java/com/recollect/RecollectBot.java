package com.recollect;

import com.recollect.dao.NoteDAO;
import com.recollect.dao.UserDAO;
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

import java.util.Date;
import java.util.List;

public class RecollectBot extends TelegramLongPollingBot {

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
        notes.forEach(note->sendMessage(note, message));
    }

    private void sendMessage(Note note, Message message) {
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
        User user = new User(new Long(message.getFrom().getId()), message.getFrom().getFirstName(), message.getFrom().getBot(), message.getFrom().getLastName(), message.getFrom().getLanguageCode(), message.getFrom().getLanguageCode());
        try {
            UserDAO.INSTANCE.create(user);
        } catch (Exception e) {
            System.out.println("This user is already exists");
        }
        Note note = new Note(message.getText(), new Date(), user);
        NoteDAO.INSTANCE.create(note);
    }

    public String getBotUsername() {
        return "RecollectBot";
    }
}
