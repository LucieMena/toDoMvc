package com.example.todo.infrastructure.persistence.mapper;

import com.example.todo.domain.ToDo;
import com.example.todo.infrastructure.persistence.entity.ToDoEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoMapperTest {
    @Test
    void testToDomainFromEntity() {
        ToDoEntity entity = ToDoEntity.builder()
                .id(1L)
                .title("Task 1")
                .completed(true)
                .order(5)
                .build();

        ToDo domain = ToDoMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals("Task 1", domain.getTitle());
        assertTrue(domain.isCompleted());
        assertEquals(5, domain.getOrder());
    }

    @Test
    void testToEntityFromDomain() {
        ToDo domain = ToDo.builder()
                .id(2L)
                .title("Task 2")
                .completed(false)
                .order(10)
                .build();

        ToDoEntity entity = ToDoMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Task 2", entity.getTitle());
        assertFalse(entity.isCompleted());
        assertEquals(10, entity.getOrder());
    }
}
