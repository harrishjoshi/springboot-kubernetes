package com.harrishjoshi.todo.config;

import com.harrishjoshi.todo.domain.Todo;
import com.harrishjoshi.todo.domain.TodoRepository;
import com.harrishjoshi.todo.domain.TodoStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
public class SampleJob {

    private static final Logger log = LoggerFactory.getLogger(SampleJob.class);
    private final TransactionTemplate transactionTemplate;
    private final TodoRepository todoRepository;

    public SampleJob(TransactionTemplate transactionTemplate, TodoRepository todoRepository) {
        this.transactionTemplate = transactionTemplate;
        this.todoRepository = todoRepository;
    }

    @Scheduled(cron = "0 */5 * * * *")
    void execute() {
        System.out.println("Sample Job started...");
        transactionTemplate.execute(status -> {
            List<Todo> todos = todoRepository.findTop10ByStatusOrderByCreatedAtDesc(TodoStatus.PENDING);
            todos.forEach(todo -> log.info("Processing todo: {}", todo.getTitle()));
            return null;
        });
        log.info("Sample Job completed...");
    }
}
