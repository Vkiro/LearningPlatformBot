package com.recollect.controller;

import com.recollect.RecollectBot;
import com.recollect.controller.commands.Command;
import com.recollect.controller.commands.Start;
import com.recollect.service.NoteService;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.HashMap;
import java.util.Map;

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
      String responseMessage = NoteService.INSTANCE.createNote(message);
      sendResponse(responseMessage, message.getChatId());
    }
  }

  public void sendResponse(String text, long chatId) {
    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText(text);
    try {
      RecollectBot.BOT.execute(message);
    } catch (TelegramApiException e) {
      BotLogger.error("Cannot sendResponse text.", e);
    }
  }
}
