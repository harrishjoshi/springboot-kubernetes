package com.harrishjoshi.todo.api;

import com.harrishjoshi.todo.domain.Todo;
import com.harrishjoshi.todo.domain.TodoRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

import static com.harrishjoshi.todo.domain.TodoStatus.PENDING;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:17-alpine:///todo",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
})
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAllInBatch();
        List<Todo> todos = List.of(
                new Todo(null, "First Todo", "First Description", PENDING, Instant.now(), null),
                new Todo(null, "Second Todo", "Second Description", PENDING, Instant.now(), null),
                new Todo(null, "Third Todo", "Third Description", PENDING, Instant.now(), null),
                new Todo(null, "Fourth Todo", "Fourth Description", PENDING, Instant.now(), null),
                new Todo(null, "Fifth Todo", "Fifth Description", PENDING, Instant.now(), null),
                new Todo(null, "Sixth Todo", "Sixth Description", PENDING, Instant.now(), null),
                new Todo(null, "Seventh Todo", "Seventh Description", PENDING, Instant.now(), null),
                new Todo(null, "Eighth Todo", "Eighth Description", PENDING, Instant.now(), null),
                new Todo(null, "Ninth Todo", "Ninth Description", PENDING, Instant.now(), null),
                new Todo(null, "Tenth Todo", "Tenth Description", PENDING, Instant.now(), null),
                new Todo(null, "Eleventh Todo", "Eleventh Description", PENDING, Instant.now(), null),
                new Todo(null, "Twelfth Todo", "Twelfth Description", PENDING, Instant.now(), null)
        );

        todoRepository.saveAll(todos);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 12, 2, 1, true, false, true, false",
            "2, 12, 2, 2, false, true, false, true"
    })
    void shouldGetBookmarks(
            int pageNumber, int totalElements, int totalPages, int currentPage,
            boolean isFirst, boolean isLast, boolean hasNext, boolean hasPrevious
    ) throws Exception {
        mockMvc.perform(get("/api/v1/todo?page=" + pageNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", CoreMatchers.equalTo(totalElements)))
                .andExpect(jsonPath("$.totalPages", CoreMatchers.equalTo(totalPages)))
                .andExpect(jsonPath("$.currentPage", CoreMatchers.equalTo(currentPage)))
                .andExpect(jsonPath("$.isFirst", CoreMatchers.equalTo(isFirst)))
                .andExpect(jsonPath("$.isLast", CoreMatchers.equalTo(isLast)))
                .andExpect(jsonPath("$.hasNext", CoreMatchers.equalTo(hasNext)))
                .andExpect(jsonPath("$.hasPrevious", CoreMatchers.equalTo(hasPrevious)));
    }

    @Test
    void shouldCreateTodo() throws Exception {
        mockMvc.perform(
                        post("/api/v1/todo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "Thirteenth Todo",
                                            "description": "Thirteenth Description"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", CoreMatchers.equalTo("Thirteenth Todo")))
                .andExpect(jsonPath("$.description", CoreMatchers.equalTo("Thirteenth Description")));
    }

    @Test
    void shouldFailCreateTodoIfAlreadyPresent() throws Exception {
        mockMvc.perform(
                        post("/api/v1/todo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "First Todo",
                                            "description": "First Description"
                                        }
                                        """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title", CoreMatchers.equalTo("Todo Already Exists")))
                .andExpect(jsonPath("$.detail", CoreMatchers.equalTo("Todo with title [First Todo] already exists.")));
    }

    @Test
    void shouldFailUpdateTodoIfNotFound() throws Exception {
        mockMvc.perform(
                        put("/api/v1/todo/33")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "Non-existing Todo",
                                            "status": "COMPLETED",
                                            "description": "Some Description"
                                        }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", CoreMatchers.equalTo("Todo Not Found")))
                .andExpect(jsonPath("$.detail", CoreMatchers.equalTo("Todo with id [33] not found.")));
    }

    @Test
    void shouldUpdateExistingTodo() throws Exception {
        var createResult = mockMvc.perform(
                        post("/api/v1/todo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "Original Todo",
                                            "description": "Original Description"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", CoreMatchers.equalTo("Original Todo")))
                .andExpect(jsonPath("$.description", CoreMatchers.equalTo("Original Description")))
                .andReturn();

        // Extract the ID from the creation response
        var createResponseBody = createResult.getResponse().getContentAsString();
        var todoId = new ObjectMapper().readTree(createResponseBody).get("id").asLong();

        mockMvc.perform(
                        put("/api/v1/todo/" + todoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "Updated Todo",
                                            "description": "Updated Description",
                                            "status": "IN_PROGRESS"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.equalTo(Math.toIntExact(todoId))))
                .andExpect(jsonPath("$.title", CoreMatchers.equalTo("Updated Todo")))
                .andExpect(jsonPath("$.description", CoreMatchers.equalTo("Updated Description")))
                .andExpect(jsonPath("$.status", CoreMatchers.equalTo("IN_PROGRESS")));
    }
}