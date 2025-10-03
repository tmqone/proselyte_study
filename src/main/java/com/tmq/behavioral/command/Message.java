package com.tmq.behavioral.command;

public record Message (String to, String from, String subject, String message) {
}
