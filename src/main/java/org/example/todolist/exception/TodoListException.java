package org.example.todolist.exception;

public class TodoListException {
    public static void NotFoundException() {
        throw new RuntimeException("This Todo not existed");
    }
}
