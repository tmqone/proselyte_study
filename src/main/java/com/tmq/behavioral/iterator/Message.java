package com.tmq.behavioral.iterator;

import java.io.Serializable;

public class Message {
    private String from;
    private String[] to;

    public Message(String from, String... to) {
        this.from = from;
        this.to = to;
    }

    public Iterator<String> iterator() {
        Iterator<String> iterator = new MessageIterator();
        return iterator;
    }

    public class MessageIterator implements Iterator<String> {
        int index = 0;

        @Override
        public boolean hasNext() {
            return (to != null && index < to.length);
        }

        @Override
        public String next() {
            if (hasNext()) {
                return to[index++];
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
    }
}
