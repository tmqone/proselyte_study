package com.tmq.behavioral.interpreter;

public class Main {
    static void main() {
        Message message = new Message(Priority.LOW, "Иван", "Артем",
                "Мониторинг алерт", "Упала Кафка");
        PriorityEqualsExpression priorityEqualsExpression = new PriorityEqualsExpression(Priority.LOW);
        SubjectContainsExpression subjectContainsExpression = new SubjectContainsExpression("Алерт");

        AndExpression andExpression = new AndExpression(priorityEqualsExpression, subjectContainsExpression);
        System.out.println(andExpression.interpret(message));
    }
}
