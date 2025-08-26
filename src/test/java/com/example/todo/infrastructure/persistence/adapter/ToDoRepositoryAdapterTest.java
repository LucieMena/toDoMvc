package com.example.todo.infrastructure.persistence.adapter;

import com.example.todo.domain.ToDo;
import com.example.todo.infrastructure.persistence.entity.ToDoEntity;
import com.example.todo.infrastructure.persistence.repository.ToDoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToDoRepositoryAdapterTest {

    private ToDoRepository repository;
    private ToDoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(ToDoRepository.class);
        adapter = new ToDoRepositoryAdapter(repository);
    }

    @Test
    void testFindAll() {
        ToDoEntity entity = ToDoEntity.builder().id(1L).title("Task 1").completed(false).order(1).build();
        when(repository.findAllByOrderByOrderAsc()).thenReturn(List.of(entity));

        List<ToDo> result = adapter.findAll();

        assertEquals(1, result.size());
        assertEquals("Task 1", result.getFirst().getTitle());
        verify(repository).findAllByOrderByOrderAsc();
    }

    @Test
    void testFindById_found() {
        ToDoEntity entity = ToDoEntity.builder().id(1L).title("Task 1").completed(false).order(1).build();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<ToDo> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Task 1", result.get().getTitle());
        verify(repository).findById(1L);
    }

    @Test
    void testFindById_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<ToDo> result = adapter.findById(99L);

        assertFalse(result.isPresent());
        verify(repository).findById(99L);
    }

    @Test
    void testSave() {
        ToDo toDo = ToDo.builder()
                .title("New Task")
                .completed(false)
                .order(1)
                .build();

        ToDoEntity savedEntity = ToDoEntity.builder()
                .id(1L)
                .title("New Task")
                .completed(false)
                .order(1)
                .build();

        when(repository.save(argThat(e ->
                e.getTitle().equals(toDo.getTitle()) &&
                        e.isCompleted() == toDo.isCompleted() &&
                        e.getOrder().equals(toDo.getOrder())
        ))).thenReturn(savedEntity);

        ToDo result = adapter.save(toDo);

        assertNotNull(result.getId());
        assertEquals("New Task", result.getTitle());
        assertFalse(result.isCompleted());
        assertEquals(1, result.getOrder());

        verify(repository).save(argThat(e ->
                e.getTitle().equals(toDo.getTitle()) &&
                        e.isCompleted() == toDo.isCompleted() &&
                        e.getOrder().equals(toDo.getOrder())
        ));
    }


    @Test
    void testDelete() {
        ToDo toDo = ToDo.builder()
                .id(1L)
                .title("Task")
                .completed(false)
                .order(1)
                .build();

        adapter.delete(toDo);

        verify(repository).delete(argThat(e ->
                e.getId().equals(toDo.getId()) &&
                        e.getTitle().equals(toDo.getTitle()) &&
                        e.isCompleted() == toDo.isCompleted() &&
                        e.getOrder().equals(toDo.getOrder())
        ));
    }

    @Test
    void testDeleteAll() {
        adapter.deleteAll();
        verify(repository).deleteAll();
    }

    @Test
    void testDeleteAllCompleted() {
        adapter.deleteAllCompleted();
        verify(repository).deleteAllCompleted();
    }

    @Test
    void testExistsByTitle() {
        when(repository.existsByTitle("Task")).thenReturn(true);
        boolean exists = adapter.existsByTitle("Task");
        assertTrue(exists);
        verify(repository).existsByTitle("Task");
    }

    @Test
    void testExistsByTitleAndIdNot() {
        when(repository.existsByTitleAndIdNot("Task", 1L)).thenReturn(true);
        boolean exists = adapter.existsByTitleAndIdNot("Task", 1L);
        assertTrue(exists);
        verify(repository).existsByTitleAndIdNot("Task", 1L);
    }
}
