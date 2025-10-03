package com.tmq.behavioral.template;

public class SmsMessageNotifierTemplate extends NotifierTemplate{
    @Override
    public void printTypeOfMessage(Message message) {
        System.out.println("[SMS]");
    }
}
