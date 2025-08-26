package com.example.todo.infrastructure.persistence.adapter;

import com.example.todo.domain.ToDo;
import com.example.todo.domain.ports.ToDoRepositoryPort;
import com.example.todo.infrastructure.persistence.entity.ToDoEntity;
import com.example.todo.infrastructure.persistence.mapper.ToDoMapper;
import com.example.todo.infrastructure.persistence.repository.ToDoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ToDoRepositoryAdapter implements ToDoRepositoryPort {

    private final ToDoRepository toDoRepository;

    public ToDoRepositoryAdapter(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public List<ToDo> findAll() {
        return toDoRepository.findAllByOrderByOrderAsc()
                .stream().map(ToDoMapper::toDomain).toList();
    }

    @Override
    public Optional<ToDo> findById(Long id) {
        return toDoRepository.findById(id).map(ToDoMapper::toDomain);
    }

    @Override
    public ToDo save(ToDo toDo) {
        ToDoEntity saved = toDoRepository.save(ToDoMapper.toEntity(toDo));
        return ToDoMapper.toDomain(saved);
    }

    @Override
    public void delete(ToDo toDo) {
        toDoRepository.delete(ToDoMapper.toEntity(toDo));
    }

    @Override
    public void deleteAll() {
        toDoRepository.deleteAll();
    }

    @Override
    public void deleteAllCompleted() {
        toDoRepository.deleteAllCompleted();
    }

    @Override
    public boolean existsByTitle(String title) {
        return toDoRepository.existsByTitle(title);
    }

    @Override
    public boolean existsByTitleAndIdNot(String title, Long id) {
        return toDoRepository.existsByTitleAndIdNot(title, id);
    }
}
