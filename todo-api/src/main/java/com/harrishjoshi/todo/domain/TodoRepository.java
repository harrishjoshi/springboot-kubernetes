package com.harrishjoshi.todo.domain;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("""
    SELECT new com.harrishjoshi.todo.domain.TodoDTO(
    id, title, description, status, createdAt, updatedAt
    )
    FROM Todo
    """)
    Page<TodoDTO> findBy(Pageable pageable);

    @Query("""
            SELECT new com.harrishjoshi.todo.domain.TodoDTO(
            id, title, description, status, createdAt, updatedAt
            )
            FROM Todo
            WHERE UPPER(title) LIKE UPPER(CONCAT('%', :query, '%'))
            """)
    Page<TodoDTO> searchTodos(String query, Pageable pageable);

    @Query("""
            SELECT CASE WHEN COUNT(*) > 0 THEN 'TRUE' ELSE 'FALSE' END
            FROM Todo
            WHERE UPPER(title) = UPPER(:title) AND (:id IS NULL OR id != :id)
            """)
    boolean existsByTitleIgnoreCaseAndIdIsNot(String title, Long id);
}
