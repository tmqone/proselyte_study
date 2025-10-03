package com.tmq.behavioral.template;
public abstract class NotifierTemplate {
    public void send(Message message) {
        printTypeOfMessage(message);
        System.out.printf("От:%s%nКому:%s%nТема:%s%nТекст:%s%n",
                message.from(), message.to(), message.subject(), message.content());
    }

    public abstract void printTypeOfMessage(Message message);
}
