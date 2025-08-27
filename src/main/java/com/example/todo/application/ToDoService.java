package com.example.todo.application;

import com.example.todo.domain.ToDo;
import com.example.todo.domain.exception.DuplicateToDoException;
import com.example.todo.domain.exception.ToDoNotFoundException;
import com.example.todo.domain.ports.ToDoRepositoryPort;
import com.example.todo.domain.ports.ToDoServicePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService implements ToDoServicePort {

    private final ToDoRepositoryPort toDoRepository;

    public ToDoService(ToDoRepositoryPort toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public List<ToDo> getAllToDos() {
        return toDoRepository.findAll();
    }

    @Override
    public ToDo getToDoById(Long id) {
        return toDoRepository.findById(id)
                .orElseThrow(() -> new ToDoNotFoundException(id));
    }

    @Override
    public ToDo createToDo(ToDo toDo) {

        if (toDoRepository.existsByTitle(toDo.getTitle())) {
            throw new DuplicateToDoException(toDo.getTitle());
        }

        Integer maxOrder = toDoRepository.findAll()
                .stream()
                .map(ToDo::getOrder)
                .max(Integer::compareTo)
                .orElse(0);

        toDo.setOrder(maxOrder + 1);

        return toDoRepository.save(toDo);
    }

    @Override
    public ToDo updateToDo(Long id, ToDo toDo) {
        ToDo entity = toDoRepository.findById(id)
                .orElseThrow(() -> new ToDoNotFoundException(id));

        boolean titleExists = toDoRepository.existsByTitleAndIdNot(toDo.getTitle(), id);

        if (titleExists) {
            throw new DuplicateToDoException(toDo.getTitle());
        }

        entity.setTitle(toDo.getTitle());
        entity.setCompleted(toDo.isCompleted());

        return toDoRepository.save(entity);
    }

    @Override
    public void deleteToDo(Long id) {
        ToDo entity = toDoRepository.findById(id)
                .orElseThrow(() -> new ToDoNotFoundException(id));
        toDoRepository.delete(entity);
    }

    @Override
    public void deleteAllToDos() {
        toDoRepository.deleteAll();
    }

    @Override
    public void deleteAllCompletedToDos() {
        toDoRepository.deleteAllCompleted();
    }
}
