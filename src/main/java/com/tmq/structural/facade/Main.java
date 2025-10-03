package com.tmq.structural.facade;

public class Main {
    public static void main(String[] args) {
        NotificationFacade notificationFacade = new NotificationFacade();
        notificationFacade.sendNotification("Мониторинг",
                "Необходимо поставить ИС на мониторинг",
                "Начальник",
                "Подчиненный");
    }
}
