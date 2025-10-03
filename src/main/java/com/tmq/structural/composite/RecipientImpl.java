package com.tmq.structural.composite;

public class RecipientImpl implements Recipient {

    private String name;
    private Long id;

    public RecipientImpl(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void printInfo(int level){
        System.out.printf("\t".repeat(level) + "Recipient: (%s, %d)\n", name, id);
    }
}
