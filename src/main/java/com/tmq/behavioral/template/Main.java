package com.tmq.behavioral.template;

public class Main {
    static void main() {
        NotifierTemplate template = new EmailMessageNotifierTemplate();
        Message message = new Message("Иван", "Артем", "Мониторинг", "Алерт");
        template.send(message);
    }
}
