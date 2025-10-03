package com.tmq.structural.composite;

public class Main {
    static void main() {
        GroupRecipient devGroup = new GroupRecipient("Developers", 2L);
        GroupRecipient testGroup = new GroupRecipient("Testers", 3L);
        devGroup.addRecipient(new RecipientImpl("John Smith", 1L));
        devGroup.addRecipient(new RecipientImpl("Ivan Ivanov", 2L));
        testGroup.addRecipient(new RecipientImpl("Nikolay Ivanov", 3L));
        testGroup.addRecipient(new RecipientImpl("Alex Ivanov", 4L));

        GroupRecipient allGroup = new GroupRecipient("All", 1L);
        allGroup.addRecipient(devGroup);
        allGroup.addRecipient(testGroup);

        GroupRecipient javaDevelopers = new GroupRecipient("Java Developers", 3L);
        javaDevelopers.addRecipient(new RecipientImpl("Artem Validzhanov", 5L));
        javaDevelopers.addRecipient(new RecipientImpl("Pavel Ivanov", 6L));
        devGroup.addRecipient(javaDevelopers);
        javaDevelopers.addRecipient(new GroupRecipient("Junior Java Developers", 7L));
        allGroup.printInfo();


    }
}
