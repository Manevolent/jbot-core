package com.github.manevolent.jbot.command;

import com.github.manevolent.jbot.chat.ChatMessage;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;
import com.github.manevolent.jbot.command.executor.AliasedCommandExecutor;
import com.github.manevolent.jbot.command.executor.CommandExecutor;

public abstract class CommandManager {
    public abstract Registration registerExecutor(String label, CommandExecutor executor);
    public abstract void unregisterExecutor(String label);
    public abstract void execute(ChatMessage chatMessage) throws CommandExecutionException;

    public class Registration {
        private final CommandExecutor executor;
        private final String label;

        public Registration(CommandExecutor executor, String label) {
            this.executor = executor;
            this.label = label;
        }

        public Registration alias(String alias) {
            return registerExecutor(alias, new AliasedCommandExecutor(executor, label));
        }
    }
}
