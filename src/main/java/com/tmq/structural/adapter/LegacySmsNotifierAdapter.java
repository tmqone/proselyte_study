package com.tmq.structural.adapter;

public class LegacySmsNotifierAdapter implements SmsNotifier {
    private LegacySmsNotifier smsNotifier;

    public LegacySmsNotifierAdapter(LegacySmsNotifier legacySmsNotifier) {
        this.smsNotifier = legacySmsNotifier;
    }

    @Override
    public void sendSms() {
        smsNotifier.printMessage();
    }
}
