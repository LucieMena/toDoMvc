package com.example.todo.infrastructure.persistence.mapper;

import com.example.todo.domain.ToDo;
import com.example.todo.infrastructure.persistence.entity.ToDoEntity;

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
}
