package com.tmq.behavioral.memento;

public class Main {
    static void main() {
        Message message = new Message("Иван", "Мониторинг", "Алёрт");

        MessageDraft messageDraft = new MessageDraft(message);
        MessageDraftMemento messageDraftMemento = messageDraft.saveDraft();
        System.out.println(messageDraft);

        messageDraft.setMessage(new Message("Владимир", "Кубер", "Прописать значения в конфигмапе"));
        System.out.println(messageDraft);

        messageDraft.restoreDraft(messageDraftMemento);
        System.out.println(messageDraft);
    }
}
