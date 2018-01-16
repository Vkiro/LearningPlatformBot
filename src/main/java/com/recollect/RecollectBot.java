package com.recollect;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.File;

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
        sendMessage(message);
    }

    public void sendMessage(Message message) {
        File file = new File("C:/image.jpg");
        SendMessage sendMessage = new SendMessage();
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId());
        sendPhoto.setNewPhoto(file);
        sendMessage.setChatId(message.getChatId());
        String s = "Hello, " + message.getFrom().getFirstName() + '\n' + message.getFrom().getLastName();
        sendMessage.setText(s);
        try {
            sendMessage(sendMessage);
            sendPhoto(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "RecollectBot";
    }
}
