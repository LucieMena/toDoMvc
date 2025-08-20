package com.example.todo.service;

import com.example.todo.dto.ToDoDto;
import com.example.todo.exception.DuplicateToDoException;
import com.example.todo.exception.ToDoNotFoundException;
import com.example.todo.persistence.entity.ToDoEntity;
import com.example.todo.persistence.repository.ToDoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ToDoServiceImplTest {
    private ToDoRepository repository;
    private ToDoServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(ToDoRepository.class);
        service = new ToDoServiceImpl(repository);
    }

    @Test
    void testGetAllToDos() {
        ToDoEntity entity = ToDoEntity.builder()
                .id(1L).title("Task 1").completed(false).order(1).build();

        when(repository.findAllByOrderByOrderAsc()).thenReturn(List.of(entity));

        List<ToDoDto> result = service.getAllToDos();

        assertEquals(1, result.size());
        assertEquals("Task 1", result.getFirst().getTitle());
    }

    @Test
    void testGetToDoById_found() {
        ToDoEntity entity = ToDoEntity.builder()
                .id(1L).title("Task 1").completed(false).order(1).build();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        ToDoDto result = service.getToDoById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Task 1", result.getTitle());
    }

    @Test
    void testGetToDoById_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ToDoNotFoundException.class, () -> service.getToDoById(99L));
    }

    @Test
    void testCreateToDo_success() {
        ToDoDto dto = ToDoDto.builder().title("New Task").completed(false).build();

        when(repository.existsByTitle("New Task")).thenReturn(false);
        when(repository.findAll()).thenReturn(List.of(
                ToDoEntity.builder().id(1L).title("Old").order(1).build()
        ));
        when(repository.save(any(ToDoEntity.class)))
                .thenAnswer(invocation -> {
                    ToDoEntity e = invocation.getArgument(0);
                    e.setId(2L);
                    return e;
                });

        ToDoDto result = service.createToDo(dto);

        assertNotNull(result.getId());
        assertEquals("New Task", result.getTitle());
        assertEquals(2, result.getOrder());
    }

    @Test
    void testCreateToDo_duplicate() {
        ToDoDto dto = ToDoDto.builder().title("Duplicate").build();

        when(repository.existsByTitle("Duplicate")).thenReturn(true);

        assertThrows(DuplicateToDoException.class, () -> service.createToDo(dto));
    }

    @Test
    void testUpdateToDo_success() {
        ToDoEntity existing = ToDoEntity.builder()
                .id(1L).title("Old Title").completed(false).order(1).build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByTitleAndIdNot("Updated Title", 1L)).thenReturn(false);
        when(repository.save(any(ToDoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ToDoDto dto = ToDoDto.builder()
                .title("Updated Title").completed(true).build();

        ToDoDto result = service.updateToDo(1L, dto);

        assertEquals("Updated Title", result.getTitle());
        assertTrue(result.isCompleted());
    }

    @Test
    void testUpdateToDo_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ToDoDto dto = ToDoDto.builder().title("X").build();

        assertThrows(ToDoNotFoundException.class, () -> service.updateToDo(1L, dto));
    }

    @Test
    void testUpdateToDo_duplicateTitle() {
        ToDoEntity existing = ToDoEntity.builder()
                .id(1L).title("Old").completed(false).order(1).build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByTitleAndIdNot("Duplicate", 1L)).thenReturn(true);

        ToDoDto dto = ToDoDto.builder().title("Duplicate").build();

        assertThrows(DuplicateToDoException.class, () -> service.updateToDo(1L, dto));
    }

    @Test
    void testDeleteToDo_success() {
        ToDoEntity entity = ToDoEntity.builder().id(1L).title("Task").build();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.deleteToDo(1L);

        verify(repository).delete(entity);
    }

    @Test
    void testDeleteToDo_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ToDoNotFoundException.class, () -> service.deleteToDo(99L));
    }

    @Test
    void testDeleteAllToDos() {
        service.deleteAllToDos();
        verify(repository).deleteAll();
    }

    @Test
    void testDeleteAllCompletedToDos() {
        service.deleteAllCompletedToDos();
        verify(repository).deleteAllCompleted();
    }
}
