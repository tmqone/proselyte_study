package com.tmq.structural.adapter;

public class Main {
    static void main() {
        SmsNotifier smsNotifier = new LegacySmsNotifierAdapter(new LegacySmsNotifier("message"));
        smsNotifier.sendSms();
    }
}
