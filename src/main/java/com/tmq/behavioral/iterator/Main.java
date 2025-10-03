package com.tmq.behavioral.iterator;

import com.tmq.behavioral.iterator.Message;

public class Main {
    static void main() {
        Message message = new Message("Иван", "Артем", "Василий");

        Iterator<String> iterator = message.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
