package com.example.todo.exception;

public class ToDoNotFoundException extends RuntimeException {
    public ToDoNotFoundException(Long id) {
        super("ToDo with ID " + id + " not found");
    }
}
