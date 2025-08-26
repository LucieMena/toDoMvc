package com.example.todo.domain.ports;

import com.example.todo.domain.ToDo;

import java.util.List;

public interface ToDoServicePort {
    List<ToDo> getAllToDos();

    ToDo getToDoById(Long id);

    ToDo createToDo(ToDo toDo);

    ToDo updateToDo(Long id, ToDo toDo);

    void deleteToDo(Long id);

    void deleteAllToDos();

    void deleteAllCompletedToDos();
}
