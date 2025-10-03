package com.tmq.behavioral.command;

import java.util.HashMap;
import java.util.Map;

public class Notifier {
    private int id = 0;
    private Map<String, Message> cache = new HashMap<>();

    public String sendMessage(Message message) {
        String messageId = "message-" + id;
        id++;
        cache.put(messageId, message);
        System.out.printf("[MESSAGE id = %s]\nОт:%s\nКому:%s\nТема:%s\nТекст:%s\n",
                messageId, message.from(), message.to(), message.subject(), message.message());
        return messageId;
    }

    public void cancelMessage(String messageId) {
        Message message = cache.get(messageId);
        if (message != null) {
            System.out.printf("[CANCEL id = %s]\nОт:%s\nКому:%s\nТема:%s\nТекст:%s\n",
                    messageId, message.from(), message.to(), message.subject(), message.message());
            cache.remove(messageId);
        } else {
            System.out.println("Сообщение не найдено");
        }
    }
}
