package com.recollect.controller.commands;

import com.recollect.service.ChatService;
import com.recollect.service.UserService;
import org.telegram.telegrambots.api.objects.Message;

/**
 * This command performs only when user just have joined to the RecollectBot.
 * It creates new user and chat in the DB.
 * If the user writes this command one more time, the application will log that he exists in the DB.
 */
public enum Start implements Command {
  INSTANCE;

  public static final String COMMAND = "/start";

  @Override
  public void execute(Message message) {
    org.telegram.telegrambots.api.objects.User telegramUser = message.getFrom();
    UserService.INSTANCE.createUser(telegramUser);
    long chatId = message.getChatId();
    long userId = telegramUser.getId();
    ChatService.INSTANCE.createChat(chatId, userId);
  }
}
