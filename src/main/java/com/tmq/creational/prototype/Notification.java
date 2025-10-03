package com.tmq.creational.prototype;

import com.tmq.creational.builder.Priority;
import com.tmq.creational.builder.Status;

import java.time.LocalDateTime;

public class Notification implements Copyable {
    private String title;
    private String content;
    private LocalDateTime time;
    private Status status;
    private Priority priority;

    public Notification(String title, String content, LocalDateTime time, Status status, Priority priority) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.status = status;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
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

    @Override
    public Object copy() {
        return new Notification(this.title, this.content, this.time, this.status, this.priority);
    }
}
