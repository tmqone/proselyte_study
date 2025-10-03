package com.tmq.behavioral.template;

public class EmailMessageNotifierTemplate extends NotifierTemplate{
    @Override
    public void printTypeOfMessage(Message message) {
        System.out.println("[EMAIL]");
    }
}
