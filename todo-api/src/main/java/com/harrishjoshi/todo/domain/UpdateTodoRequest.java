package com.harrishjoshi.todo.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record UpdateTodoRequest(
        @NotEmpty(message = "Title is required")
        String title,
        @NotEmpty(message = "Description is required")
        String description,
        @NotEmpty(message = "Status is required")
        @Pattern(regexp = "PENDING|IN_PROGRESS|COMPLETED|CANCELLED", message = "Invalid status")
        String status
) {
}
