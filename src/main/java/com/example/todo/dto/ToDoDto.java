package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoDto {

    private Long id;

    @NotBlank(message = "Title is required and cannot be empty")
    private String title;

    private boolean completed = false;

    private Integer order;
}
