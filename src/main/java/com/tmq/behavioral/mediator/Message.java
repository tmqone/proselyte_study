package com.tmq.behavioral.mediator;

public record Message (User user, String message){
    @Override
    public String toString(){
        return "[MESSAGE] От:%s Сообщение: %s\n".formatted(user, message);
    }
}
