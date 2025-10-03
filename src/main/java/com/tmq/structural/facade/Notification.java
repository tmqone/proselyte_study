package com.tmq.structural.facade;

public record Notification(
        String subject,
        String messsage,
        String fromUser,
        String toUser
) {
}
