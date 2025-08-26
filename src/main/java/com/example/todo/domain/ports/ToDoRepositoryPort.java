package com.example.todo.domain.ports;

import com.example.todo.domain.ToDo;

import java.util.List;
import java.util.Optional;

public interface ToDoRepositoryPort {
    List<ToDo> findAll();

    Optional<ToDo> findById(Long id);

    ToDo save(ToDo toDo);

    void delete(ToDo toDo);

    void deleteAll();

    void deleteAllCompleted();

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);
}
