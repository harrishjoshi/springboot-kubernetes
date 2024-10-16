package com.harrishjoshi.todo.domain;

import org.springframework.stereotype.Component;

@Component
public class TodoMapper {

    public TodoDTO toDTO(Todo todo) {
        return new TodoDTO(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getStatus(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }
}
