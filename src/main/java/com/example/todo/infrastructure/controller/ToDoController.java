package com.example.todo.infrastructure.controller;

import com.example.todo.domain.ToDo;
import com.example.todo.domain.ports.ToDoServicePort;
import com.example.todo.infrastructure.dto.ToDoDto;
import com.example.todo.infrastructure.mapper.ToDoDtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/todos")
@CrossOrigin(origins = "*")
public class ToDoController {

    private final ToDoServicePort toDoService;

    public ToDoController(ToDoServicePort toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping
    public List<ToDoDto> getAllToDos() {
        return toDoService.getAllToDos().stream()
                .map(ToDoDtoMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ToDoDto getToDoById(@PathVariable Long id) {
        ToDo toDo = toDoService.getToDoById(id);
        return ToDoDtoMapper.toDto(toDo);
    }

    @PostMapping
    public ResponseEntity<ToDoDto> createToDo(@Valid @RequestBody ToDoDto dto) {

        ToDo created = toDoService.createToDo(ToDoDtoMapper.toDomain(dto));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(ToDoDtoMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ToDoDto updateToDo(@PathVariable Long id, @Valid @RequestBody ToDoDto dto) {
        ToDo updated = toDoService.updateToDo(id, ToDoDtoMapper.toDomain(dto));
        return ToDoDtoMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToDo(@PathVariable Long id) {
        toDoService.deleteToDo(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllToDos() {
        toDoService.deleteAllToDos();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/completed")
    public ResponseEntity<Void> deleteAllCompletedToDos() {
        toDoService.deleteAllCompletedToDos();
        return ResponseEntity.noContent().build();
    }

}
