package com.tmq.structural.proxy;

import java.util.concurrent.ExecutionException;

public class Main {
    static void main(){
        ProxyRateLimitEmailNotifier proxyRateLimitEmailNotifier = new ProxyRateLimitEmailNotifier();
        EmailNotifier emailNotifier = new EmailNotifier();
        Message message = new Message("Иван", "Артем", "Мониторинг",
                "Алерт по мониторингу");
        System.out.println("""
                Обычный класс
                -------------------------------------------------------------
                """);
        emailNotifier.send(message);

        System.out.println("""
                \nПрокси класс
                -------------------------------------------------------------
                """);
        proxyRateLimitEmailNotifier.send(message);
    }
}
