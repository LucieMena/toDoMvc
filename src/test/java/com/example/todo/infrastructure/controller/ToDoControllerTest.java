package com.example.todo.infrastructure.controller;

import com.example.todo.domain.ToDo;
import com.example.todo.domain.ports.ToDoServicePort;
import com.example.todo.infrastructure.dto.ToDoDto;
import com.example.todo.infrastructure.mapper.ToDoDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ToDoController.class)
@ExtendWith(SpringExtension.class)
class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ToDoServicePort toDoService;

    @BeforeEach
    void setUp() {
        Mockito.reset(toDoService);
    }

    @Test
    void testGetAllToDos() throws Exception {
        ToDo todo1 = ToDoDtoMapper.toDomain(new ToDoDto(1L, "Task 1", false, 1));
        ToDo todo2 = ToDoDtoMapper.toDomain(new ToDoDto(2L, "Task 2", true, 2));

        when(toDoService.getAllToDos()).thenReturn(List.of(todo1, todo2));

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Task 1")))
                .andExpect(jsonPath("$[1].title", is("Task 2")));
    }

    @Test
    void testGetToDoById() throws Exception {
        ToDo todo = ToDoDtoMapper.toDomain(new ToDoDto(1L, "Sample Task", false, 1));

        when(toDoService.getToDoById(1L)).thenReturn(todo);

        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Sample Task")))
                .andExpect(jsonPath("$.completed", is(false)));
    }

    @Test
    void testCreateToDo() throws Exception {
        ToDoDto dto = new ToDoDto(null, "New Task", false, 1);
        ToDo created = ToDoDtoMapper.toDomain(new ToDoDto(1L, "New Task", false, 2));

        when(toDoService.createToDo(any())).thenReturn(created);

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("New Task")));
    }

    @Test
    void testUpdateToDo() throws Exception {
        ToDo updated = ToDoDtoMapper.toDomain(new ToDoDto(1L, "Updated Task", true, 1));

        when(toDoService.updateToDo(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Task")))
                .andExpect(jsonPath("$.completed", is(true)));
    }

    @Configuration
    static class TestConfig {
        @Bean
        public ToDoServicePort toDoService() {
            return Mockito.mock(ToDoServicePort.class);
        }

        @Bean
        public ToDoController toDoController(ToDoServicePort service) {
            return new ToDoController(service);
        }
    }

    @Test
    void testDeleteToDo() throws Exception {
        mockMvc.perform(delete("/todos/1"))
                .andExpect(status().isNoContent());
        verify(toDoService).deleteToDo(1L);
    }

    @Test
    void testDeleteAllToDos() throws Exception {
        mockMvc.perform(delete("/todos"))
                .andExpect(status().isNoContent());
        verify(toDoService).deleteAllToDos();
    }

    @Test
    void testDeleteAllCompletedToDos() throws Exception {
        mockMvc.perform(delete("/todos/completed"))
                .andExpect(status().isNoContent());
        verify(toDoService).deleteAllCompletedToDos();
    }
}
