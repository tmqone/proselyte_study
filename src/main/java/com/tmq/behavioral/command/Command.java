package com.tmq.behavioral.command;

public interface Command {
    void execute();
    void undo();
    void undo(String messageId);
}
