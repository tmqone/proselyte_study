package com.tmq.structural.composite;

import java.util.ArrayList;
import java.util.List;

public class GroupRecipient implements Recipient {
    private String name;
    private Long id;
    private List<Recipient> recipients;

    public GroupRecipient(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void addRecipient(Recipient recipient) {
        if (recipients == null) {
            recipients = new ArrayList<>();
        }
        recipients.add(recipient);
    }

    public void removeRecipient(Recipient recipient) {
        if (recipients != null) {
            recipients.remove(recipient);
        }
    }

    public void printInfo(int level) {
        System.out.printf("\t".repeat(level) + "Group(%s, %d):\n", name, id);
        if (recipients != null) {
            for (Recipient recipient : recipients) {
                recipient.printInfo(level + 1);
            }
        }

    }
}
