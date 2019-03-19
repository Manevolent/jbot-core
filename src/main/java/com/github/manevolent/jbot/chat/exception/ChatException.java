package com.github.manevolent.jbot.chat.exception;

public class ChatException extends Exception {
    public ChatException(Exception cause) {
        super(cause);
    }

    public ChatException(String message, Throwable ex) {
        super(message, ex);
    }

    public ChatException(String message) {
        super(message);
    }
}
