package com.tmq.behavioral.memento;

public class MessageDraft {
    private Message message;

    public MessageDraft(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
    public void setMessage(Message message) {
        this.message = message;
    }

    public MessageDraftMemento saveDraft() {
        return new MessageDraftMemento(this.message);
    }

    public void restoreDraft(MessageDraftMemento memento) {
        this.message = memento.getState();
    }

    @Override
    public String toString() {
        return this.message.toString();
    }
}
