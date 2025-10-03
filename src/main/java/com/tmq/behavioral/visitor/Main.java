package com.tmq.behavioral.visitor;

public class Main {
    static void main() {
        Visitor visitor = new ValidatorVisitor();
        MessageCompaign messageCompaign = new MessageCompaign();

        messageCompaign.addMessage(new EmailMessage("ivan@gmail.com", "Алерт"));
        messageCompaign.addMessage(new SmsMessage("+79999999999", "Алерт"));
        messageCompaign.addMessage(new EmailMessage("ivangmail.com", "Алерт"));
        messageCompaign.addMessage(new SmsMessage("+38599999999", "Алерт"));

        messageCompaign.accept(visitor);
    }
}
