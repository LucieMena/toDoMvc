package com.example.todo.controller;

import com.example.todo.dto.ToDoDto;
import com.example.todo.service.ToDoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ToDoController.class)
public class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ToDoService toDoService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // Initialize mocks before each test
    }

    @Test
    void testGetToDoById() throws Exception {
        ToDoDto todo = new ToDoDto(1L, "Sample Task", false, 1);

        when(toDoService.getToDoById(1L)).thenReturn(todo);

        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Sample Task")))
                .andExpect(jsonPath("$.completed", is(false)));
    }

    @Test
    void testCreateToDo() throws Exception {
        ToDoDto dto = new ToDoDto(null, "New Task", false, 1);
        ToDoDto created = new ToDoDto(1L, "New Task", false, 2);

        when(toDoService.createToDo(any(ToDoDto.class))).thenReturn(created);

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
        ToDoDto updated = new ToDoDto(1L, "Updated Task", true, 1);

        when(toDoService.updateToDo(eq(1L), any(ToDoDto.class))).thenReturn(updated);

        mockMvc.perform(put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Task")))
                .andExpect(jsonPath("$.completed", is(true)));
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
