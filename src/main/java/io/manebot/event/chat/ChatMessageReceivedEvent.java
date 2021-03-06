package io.manebot.event.chat;


import io.manebot.chat.ChatMessage;

public class ChatMessageReceivedEvent extends ChatEvent {
    private final ChatMessage message;

    public ChatMessageReceivedEvent(Object sender, ChatMessage chatMessage) {
        super(sender, chatMessage.getSender().getChat());

        this.message = chatMessage;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
