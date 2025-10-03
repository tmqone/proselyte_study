package com.tmq.structural.proxy;

public class EmailNotifier implements Notifier {
    @Override
    public void send(Message message) {
        System.out.printf("[MESSAGE]\nОт:%s\nКому:%S\nТема:%s\nСообщение:%s\n",
                message.getFrom(), message.getTo(), message.getSubject(), message.getBody());
    }


}
