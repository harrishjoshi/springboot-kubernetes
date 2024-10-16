package com.harrishjoshi.todo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "todo")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    @Id
    @SequenceGenerator(name = "todo_id_seq_gen", sequenceName = "todo_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_id_seq_gen")
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
