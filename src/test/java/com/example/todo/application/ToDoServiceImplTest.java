package com.example.todo.application;

import com.example.todo.domain.ToDo;
import com.example.todo.domain.ports.ToDoRepositoryPort;
import com.example.todo.exception.DuplicateToDoException;
import com.example.todo.exception.ToDoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToDoServiceImplTest {

    private ToDoRepositoryPort repository;
    private ToDoServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(ToDoRepositoryPort.class);
        service = new ToDoServiceImpl(repository);
    }

    @Test
    void testGetAllToDos() {
        ToDo todo = ToDo.builder().id(1L).title("Task 1").completed(false).order(1).build();
        when(repository.findAll()).thenReturn(List.of(todo));

        List<ToDo> result = service.getAllToDos();

        assertEquals(1, result.size());
        assertEquals("Task 1", result.getFirst().getTitle());
    }

    @Test
    void testGetToDoById_found() {
        ToDo todo = ToDo.builder().id(1L).title("Task 1").completed(false).order(1).build();
        when(repository.findById(1L)).thenReturn(Optional.of(todo));

        ToDo result = service.getToDoById(1L);

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
        ToDo todo = ToDo.builder().title("New Task").completed(false).build();

        when(repository.existsByTitle("New Task")).thenReturn(false);
        when(repository.findAll()).thenReturn(List.of(ToDo.builder().order(1).build()));
        when(repository.save(any(ToDo.class))).thenAnswer(invocation -> {
            ToDo t = invocation.getArgument(0);
            t.setId(2L);
            return t;
        });

        ToDo result = service.createToDo(todo);

        assertNotNull(result.getId());
        assertEquals("New Task", result.getTitle());
        assertEquals(2, result.getOrder());
    }

    @Test
    void testCreateToDo_duplicate() {
        ToDo todo = ToDo.builder().title("Duplicate").build();
        when(repository.existsByTitle("Duplicate")).thenReturn(true);

        assertThrows(DuplicateToDoException.class, () -> service.createToDo(todo));
    }

    @Test
    void testUpdateToDo_success() {
        ToDo existing = ToDo.builder().id(1L).title("Old Title").completed(false).order(1).build();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByTitleAndIdNot("Updated Title", 1L)).thenReturn(false);
        when(repository.save(any(ToDo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ToDo update = ToDo.builder().title("Updated Title").completed(true).build();
        ToDo result = service.updateToDo(1L, update);

        assertEquals("Updated Title", result.getTitle());
        assertTrue(result.isCompleted());
    }

    @Test
    void testUpdateToDo_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        ToDo update = ToDo.builder().title("X").build();

        assertThrows(ToDoNotFoundException.class, () -> service.updateToDo(1L, update));
    }

    @Test
    void testUpdateToDo_duplicateTitle() {
        ToDo existing = ToDo.builder().id(1L).title("Old").completed(false).order(1).build();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByTitleAndIdNot("Duplicate", 1L)).thenReturn(true);

        ToDo update = ToDo.builder().title("Duplicate").build();
        assertThrows(DuplicateToDoException.class, () -> service.updateToDo(1L, update));
    }

    @Test
    void testDeleteToDo_success() {
        ToDo todo = ToDo.builder().id(1L).title("Task").build();
        when(repository.findById(1L)).thenReturn(Optional.of(todo));

        service.deleteToDo(1L);

        verify(repository).delete(todo);
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
