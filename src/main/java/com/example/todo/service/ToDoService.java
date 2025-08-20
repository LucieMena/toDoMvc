package com.example.todo.service;

import com.example.todo.dto.ToDoDto;

import java.util.List;

public interface ToDoService {
    List<ToDoDto> getAllToDos();

    ToDoDto getToDoById(Long id);

    ToDoDto createToDo(ToDoDto dto);

    ToDoDto updateToDo(Long id, ToDoDto dto);

    void deleteToDo(Long id);

    void deleteAllToDos();

    void deleteAllCompletedToDos();
}
