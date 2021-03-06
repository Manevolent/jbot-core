package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import io.manebot.command.executor.chained.AnnotatedCommandExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CommandArgumentLabel extends CommandArgument {
    private final String label;

    public CommandArgumentLabel(String label) {
        if (label == null || label.length() <= 0) throw new IllegalArgumentException("invalid label");
        this.label = label;
    }

    public CommandArgumentLabel(Argument argument) {
        this.label = argument.label();
    }

    @Override
    public String getHelpString() {
        return label;
    }


    @Override
    public ChainPriority cast(ChainState state) {
        String next = state.next();
        if (next == null) return ChainPriority.NONE;
        if (next.equalsIgnoreCase(label)) {
            state.extend(1, label);
            return ChainPriority.HIGH;
        } else return ChainPriority.NONE;
    }

    @Override
    public boolean canExtend(CommandArgument b) {
        return true; // anything can extend this
    }

    @Override
    public boolean canCoexist(CommandArgument b) {
        return true;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @AnnotatedCommandExecutor.Argument(type = CommandArgumentLabel.class)
    public @interface Argument {
        String label();
    }
}
