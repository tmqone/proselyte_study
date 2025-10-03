package com.tmq.structural.proxy;

import java.util.concurrent.*;

public class ProxyRateLimitEmailNotifier implements Notifier{
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private EmailNotifier emailNotifier = new EmailNotifier();

    @Override
    public void send(Message message){
        System.out.println("Сообщение будет отправлено через 10 секунд");
        executorService.schedule(() -> {
            emailNotifier.send(message);
        }, 10, TimeUnit.SECONDS);
    }
}
