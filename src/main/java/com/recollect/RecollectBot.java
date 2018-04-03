package com.recollect;

import com.recollect.controller.MessageController;
import com.recollect.controller.TimeController;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.logging.BotLogger;

public class RecollectBot extends TelegramLongPollingBot {

  public static final RecollectBot BOT;

  static {
    ApiContextInitializer.init();
    BOT = new RecollectBot();
  }

  public RecollectBot() {
    super();
    Thread timeTracker = new Thread(new TimeController());
    timeTracker.start();
  }

  public static void main(String[] args) {
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    try {
      telegramBotsApi.registerBot(RecollectBot.BOT);
    } catch (TelegramApiRequestException tare) {
      BotLogger.error("Cannot create bot.", tare);
    }
  }

  public String getBotToken() {
    return "497884366:AAF6IKFeAINcTjahTr-uAMkURIbPVxvNWCQ";
  }

  public void onUpdateReceived(Update update) {
    if (update.hasMessage()) {
      Message message = update.getMessage();
      MessageController.INSTANCE.handleIncomingMessage(message);
    }
  }

  public String getBotUsername() {
    return "RecollectBot";
  }
}
