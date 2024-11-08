package com.harrishjoshi.todo.domain;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

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
            SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
            FROM Todo t
            WHERE UPPER(t.title) = UPPER(:title) AND (:id IS NULL OR t.id != :id)
            """)
    boolean existsByTitleIgnoreCaseAndIdIsNot(String title, Long id);

    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = LockOptions.SKIP_LOCKED + "")})
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Todo> findTop10ByStatusOrderByCreatedAtDesc(TodoStatus status);
}
