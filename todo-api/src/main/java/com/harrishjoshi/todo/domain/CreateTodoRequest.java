package com.harrishjoshi.todo.domain;

import jakarta.validation.constraints.NotEmpty;

public record CreateTodoRequest(
        @NotEmpty(message = "Title is required")
        String title,
        @NotEmpty(message = "Description is required")
        String description
) {
}
