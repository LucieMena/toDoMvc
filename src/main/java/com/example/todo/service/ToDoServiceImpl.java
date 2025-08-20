package com.example.todo.service;

import com.example.todo.dto.ToDoDto;
import com.example.todo.exception.DuplicateToDoException;
import com.example.todo.exception.ToDoNotFoundException;
import com.example.todo.persistence.entity.ToDoEntity;
import com.example.todo.persistence.mapper.ToDoMapper;
import com.example.todo.persistence.repository.ToDoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;

    public ToDoServiceImpl(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @Override
    public List<ToDoDto> getAllToDos() {
        return toDoRepository.findAllByOrderByOrderAsc()
                .stream()
                .map(ToDoMapper::toDomain)
                .map(ToDoMapper::toDto)
                .toList();
    }

    @Override
    public ToDoDto getToDoById(Long id) {
        ToDoEntity entity = toDoRepository.findById(id)
                .orElseThrow(() -> new ToDoNotFoundException(id));
        return ToDoMapper.toDto(ToDoMapper.toDomain(entity));
    }

    @Override
    public ToDoDto createToDo(ToDoDto dto) {

        if (toDoRepository.existsByTitle(dto.getTitle())) {
            throw new DuplicateToDoException(dto.getTitle());
        }

        Integer maxOrder = toDoRepository.findAll()
                .stream()
                .map(ToDoEntity::getOrder)
                .max(Integer::compareTo)
                .orElse(0);

        ToDoEntity newEntity = ToDoMapper.toEntity(ToDoMapper.toDomainFromDto(dto));
        newEntity.setOrder(maxOrder + 1);

        ToDoEntity saved = toDoRepository.save(newEntity);
        return ToDoMapper.toDto(ToDoMapper.toDomain(saved));
    }

    @Override
    public ToDoDto updateToDo(Long id, ToDoDto dto) {
        ToDoEntity entity = toDoRepository.findById(id)
                .orElseThrow(() -> new ToDoNotFoundException(id));

        boolean titleExists = toDoRepository.existsByTitleAndIdNot(dto.getTitle(), id);

        if (titleExists) {
            throw new DuplicateToDoException(dto.getTitle());
        }

        entity.setTitle(dto.getTitle());
        entity.setCompleted(dto.isCompleted());

        ToDoEntity updated = toDoRepository.save(entity);
        return ToDoMapper.toDto(ToDoMapper.toDomain(updated));
    }

    @Override
    public void deleteToDo(Long id) {
        ToDoEntity entity = toDoRepository.findById(id)
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
