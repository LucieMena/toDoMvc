package com.example.todo.domain.exception;

public class DuplicateToDoException extends RuntimeException {
    public DuplicateToDoException(String title) {
        super("ToDo with title '" + title + "' already exists");
    }
}
