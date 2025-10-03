package com.tmq.behavioral.state;

public class Message {
    private String from;
    private String to;
    private String content;
    private State state;

    public Message(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.state = new DraftState();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                ", state=" + state +
                '}';
    }

    public void send() {
        state.send(this);
    }
}
