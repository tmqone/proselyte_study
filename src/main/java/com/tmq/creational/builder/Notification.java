package com.tmq.creational.builder;

import java.time.LocalDateTime;

public class Notification {
    private String title;
    private String content;
    private LocalDateTime time;
    private Status status;
    private Priority priority;

    private Notification(NotificationBuilder builder) {
        this.title = builder.title;
        this.content = builder.content;
        this.time = builder.time;
        this.status = builder.status;
        this.priority = builder.priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }

    @Override
    public String toString() {
        return "Notification{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }

    public static final class NotificationBuilder {
        private String title;
        private String content;
        private LocalDateTime time;
        private Status status;
        private Priority priority;

        public NotificationBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NotificationBuilder content(String content) {
            this.content = content;
            return this;
        }

        public NotificationBuilder time(LocalDateTime time) {
            this.time = time;
            return this;
        }

        public NotificationBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public NotificationBuilder priority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}
