package com.example.todo.persistence.mapper;

import com.example.todo.domain.ToDo;
import com.example.todo.dto.ToDoDto;
import com.example.todo.persistence.entity.ToDoEntity;

public class ToDoMapper {

    public static ToDo toDomain(ToDoEntity entity) {
        return ToDo.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .completed(entity.isCompleted())
                .order(entity.getOrder())
                .build();
    }

    public static ToDoEntity toEntity(ToDo domain) {
        return ToDoEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .completed(domain.isCompleted())
                .order(domain.getOrder())
                .build();
    }

    public static ToDoDto toDto(ToDo domain) {
        return ToDoDto.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .completed(domain.isCompleted())
                .order(domain.getOrder())
                .build();
    }

    public static ToDo toDomainFromDto(ToDoDto Dto) {
        return ToDo.builder()
                .id(Dto.getId())
                .title(Dto.getTitle())
                .completed(Dto.isCompleted())
                .order(Dto.getOrder())
                .build();
    }
}
