package com.harrishjoshi.todo.domain;

import com.harrishjoshi.todo.exception.TodoAlreadyExists;
import com.harrishjoshi.todo.exception.TodoNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static com.harrishjoshi.todo.domain.TodoStatus.PENDING;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    public TodoService(TodoRepository todoRepository, TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
    }

    @Transactional(readOnly = true)
    public TodosDTO getTodos(Integer page) {
        var pageable = getPageRequest(page);
        var todoPage = todoRepository.findBy(pageable);

        return new TodosDTO(todoPage);
    }

    @Transactional(readOnly = true)
    public TodosDTO searchTodos(String query, Integer page) {
        var pageable = getPageRequest(page);
        var todoPage = todoRepository.searchTodos(query, pageable);

        return new TodosDTO(todoPage);
    }

    public TodoDTO createTodo(CreateTodoRequest request) throws TodoAlreadyExists {
        var isTitleExists = todoRepository.existsByTitleIgnoreCaseAndIdIsNot(request.title(), null);
        if (isTitleExists) {
            throw new TodoAlreadyExists("Todo with title [" + request.title() + "] already exists.");
        }

        var todo = new Todo(null, request.title(), request.description(), PENDING,
                Instant.now(), null
        );
        var savedTodo = todoRepository.save(todo);

        return todoMapper.toDTO(savedTodo);
    }

    public TodoDTO findById(Long id) throws TodoNotFoundException {
        return todoRepository.findById(id)
                .map(todoMapper::toDTO)
                .orElseThrow(() -> new TodoNotFoundException("Todo with id [" + id + "] not found."));
    }

    public TodoDTO updateTodo(Long id, UpdateTodoRequest request) throws TodoNotFoundException, TodoAlreadyExists {
        var todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo with id [" + id + "] not found."));

        var isTitleExists = todoRepository.existsByTitleIgnoreCaseAndIdIsNot(request.title(), id);
        if (isTitleExists) {
            throw new TodoAlreadyExists("Todo with title [" + request.title() + "] already exists.");
        }

        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setStatus(TodoStatus.valueOf(request.status()));
        todo.setUpdatedAt(Instant.now());

        var updatedTodo = todoRepository.save(todo);

        return todoMapper.toDTO(updatedTodo);
    }

    public void deleteById(Long id) throws TodoNotFoundException {
        var todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo with id [" + id + "] not found."));

        todoRepository.delete(todo);
    }

    private static PageRequest getPageRequest(Integer page) {
        var pageNumber = page < 1 ? 0 : page - 1;
        return PageRequest.of(pageNumber, 10, Sort.Direction.DESC, "createdAt");
    }
}
