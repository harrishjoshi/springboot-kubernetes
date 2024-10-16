package com.harrishjoshi.todo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TodoDTO {

    private Long id;
    private String title;
    private String description;
    private TodoStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
