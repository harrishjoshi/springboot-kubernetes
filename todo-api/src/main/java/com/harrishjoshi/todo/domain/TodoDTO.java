package com.harrishjoshi.todo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TodoDTO {

    private Long id;
    private String title;
    private String description;
    private TodoStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
