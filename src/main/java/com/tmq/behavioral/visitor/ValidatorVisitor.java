package com.tmq.behavioral.visitor;

public class ValidatorVisitor implements Visitor {
    @Override
    public void visit(EmailMessage message) {
        System.out.println(message.to.contains("@") ? "Почта валидна" : "Почта не валидна");
    }

    @Override
    public void visit(SmsMessage message) {
        System.out.println(message.to.startsWith("+7") ? "Номер телефона валиден" : "Номер телефона не валиден");
    }
}
