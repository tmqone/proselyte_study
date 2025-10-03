package com.tmq.behavioral.strategy;

import com.tmq.behavioral.state.DraftState;
import com.tmq.behavioral.state.State;

public class Message {
    private String from;
    private String to;
    private String content;
    private Strategy strategy;

    public Message(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.strategy = new DraftStrategy();
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                ", strategy=" + strategy +
                '}';
    }

    public void send() {
        strategy.send(this);
    }
}
