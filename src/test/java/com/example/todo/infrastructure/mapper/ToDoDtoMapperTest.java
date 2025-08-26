package com.example.todo.infrastructure.mapper;

import com.example.todo.domain.ToDo;
import com.example.todo.infrastructure.dto.ToDoDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ToDoDtoMapperTest {

    @Test
    void testToDto() {
        // Given
        ToDo domain = ToDo.builder()
                .id(1L)
                .title("Test Task")
                .completed(true)
                .order(5)
                .build();

        // When
        ToDoDto dto = ToDoDtoMapper.toDto(domain);

        // Then
        assertNotNull(dto);
        assertEquals(domain.getId(), dto.getId());
        assertEquals(domain.getTitle(), dto.getTitle());
        assertEquals(domain.isCompleted(), dto.isCompleted());
        assertEquals(domain.getOrder(), dto.getOrder());
    }

    @Test
    void testToDomain() {
        // Given
        ToDoDto dto = ToDoDto.builder()
                .id(2L)
                .title("DTO Task")
                .completed(false)
                .order(3)
                .build();

        // When
        ToDo domain = ToDoDtoMapper.toDomain(dto);

        // Then
        assertNotNull(domain);
        assertEquals(dto.getId(), domain.getId());
        assertEquals(dto.getTitle(), domain.getTitle());
        assertEquals(dto.isCompleted(), domain.isCompleted());
        assertEquals(dto.getOrder(), domain.getOrder());
    }

    @Test
    void testRoundTrip() {
        // Given
        ToDo original = ToDo.builder()
                .id(10L)
                .title("RoundTrip Task")
                .completed(true)
                .order(7)
                .build();

        // When
        ToDoDto dto = ToDoDtoMapper.toDto(original);
        ToDo result = ToDoDtoMapper.toDomain(dto);

        // Then
        assertEquals(original.getId(), result.getId());
        assertEquals(original.getTitle(), result.getTitle());
        assertEquals(original.isCompleted(), result.isCompleted());
        assertEquals(original.getOrder(), result.getOrder());
    }
}
