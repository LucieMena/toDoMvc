package com.example.todo.infrastructure.config;

import com.example.todo.application.ToDoServiceImpl;
import com.example.todo.domain.ports.ToDoRepositoryPort;
import com.example.todo.domain.ports.ToDoServicePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public ToDoServicePort toDoService(ToDoRepositoryPort repositoryPort) {
        return new ToDoServiceImpl(repositoryPort);
    }
}
