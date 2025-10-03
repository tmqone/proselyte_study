package com.tmq.behavioral.command;

public class MessageCommand implements Command {
    private Notifier notifier;
    private Message message;
    private String lastSentMessage = null;

    public MessageCommand(Notifier notifier, Message message) {
        this.notifier = notifier;
        this.message = message;
    }

    @Override
    public void execute() {
        lastSentMessage = notifier.sendMessage(message);
    }

    @Override
    public void undo() {
        if (lastSentMessage != null) {
            notifier.cancelMessage(lastSentMessage);
        } else {
            System.out.println("Последнее сообщение не найдено");
        }
    }

    public void undo(String messageId){
        notifier.cancelMessage(messageId);
    }
}
