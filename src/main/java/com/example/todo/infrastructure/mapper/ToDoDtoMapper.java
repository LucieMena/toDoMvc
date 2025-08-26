package com.example.todo.infrastructure.mapper;

import com.example.todo.domain.ToDo;
import com.example.todo.infrastructure.dto.ToDoDto;

public class ToDoDtoMapper {

    public static ToDoDto toDto(ToDo domain) {
        return ToDoDto.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .completed(domain.isCompleted())
                .order(domain.getOrder())
                .build();
    }

    public static ToDo toDomain(ToDoDto dto) {
        return ToDo.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .completed(dto.isCompleted())
                .order(dto.getOrder())
                .build();
    }
}
