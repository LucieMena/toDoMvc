package com.example.todo.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDo {

    private Long id;
    private String title;
    private boolean completed;
    private Integer order;
}
