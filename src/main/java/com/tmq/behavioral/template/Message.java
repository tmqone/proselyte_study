package com.tmq.behavioral.template;

public record Message (
        String from,
        String to,
        String subject,
        String content
) {

}
