package com.harrishjoshi.todo.api;

import com.harrishjoshi.todo.domain.*;
import com.harrishjoshi.todo.exception.TodoAlreadyExists;
import com.harrishjoshi.todo.exception.TodoNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/todo")
@CrossOrigin
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public TodosDTO getTodos(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "") String query) {
        if (ObjectUtils.isEmpty(query)) {
            return todoService.getTodos(page);
        }

        return todoService.searchTodos(query, page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDTO createTodo(@RequestBody @Valid CreateTodoRequest request) throws TodoAlreadyExists {
        return todoService.createTodo(request);
    }

    @GetMapping("/{id}")
    public TodoDTO findById(@PathVariable Long id) throws TodoNotFoundException {
        return todoService.findById(id);
    }

    @PutMapping("/{id}")
    public TodoDTO updateTodo(@PathVariable Long id, @RequestBody @Valid UpdateTodoRequest request) throws TodoNotFoundException, TodoAlreadyExists {
        return todoService.updateTodo(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) throws TodoNotFoundException {
        todoService.deleteById(id);
    }
}
