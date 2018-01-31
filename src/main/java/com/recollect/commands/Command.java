package com.recollect.commands;

import org.telegram.telegrambots.api.objects.Message;

public interface Command {
    void execute(Message message);
}
