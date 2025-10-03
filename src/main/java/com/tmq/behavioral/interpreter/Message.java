package com.tmq.behavioral.interpreter;

public record Message(Priority priority, String from, String to, String subject, String body) {
}
