package com.github.manevolent.jbot.chat;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DefaultChatSender implements ChatSender {
    private final List<String> lines = new LinkedList<>();
    private final Object bufferLock = new Object();
    private volatile boolean buffered = false;

    private final String username, displayName;
    private final Chat chat;

    public DefaultChatSender(String username, String displayName, Chat chat) {
        this.username = username;
        this.displayName = displayName;
        this.chat = chat;
    }

    public DefaultChatSender(String username, Chat chat) {
        this(username, username, chat);
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Chat getChat() {
        return chat;
    }



    /**
     * Opens the command buffer.
     * @return true if the buffer was opened, false if no changes were made.
     */
    public boolean begin() {
        synchronized (bufferLock) {
            if (buffered) return false;
            else return buffered = true;
        }
    }

    /**
     * Adds a message to the command buffer or sends a message.
     * @param message message to add.
     */
    public void sendMessage(String message) {
        message = getDisplayName().trim() + " -> " + formatMessage(message);
        synchronized (bufferLock) {
            if (buffered) {

                lines.add(message);

            } else getChat().sendMessage(message);
        }
    }

    /**
     * Ends the command buffer.
     * @return number of lines sent.
     */
    public int end() {
        int c = 0;
        StringBuilder builder = new StringBuilder();

        synchronized (bufferLock) {
            if (!buffered) return 0;

            Iterator<String> iterator = lines.iterator();

            while (iterator.hasNext()) {
                builder.append(iterator.next());
                if (iterator.hasNext()) builder.append("\n");
                c++;
                iterator.remove();
            }

            buffered = false;
        }

        String result = builder.toString();
        if (result.length() > 0) getChat().sendMessage(result);

        return c;
    }

    /**
     * Sends several messages to the remote.
     * @param messages Messages to send.
     */
    public void sendMessage(String... messages) {
        for (String s : messages) sendMessage(s);
    }

    /**
     * Flushes the buffer.
     */
    public int flush() {
        int c;

        synchronized (bufferLock) {
            if (buffered) {
                c = end();
                begin();
            } else {
                c = 0;
            }
        }

        return c;
    }

    public static final String formatMessage(String message) {
        return message
                .replace("\n", " ")
                .replace("\r", " ")
                .replace("\t", " ");
    }
}