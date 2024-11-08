package com.harrishjoshi.todo.config;

import com.harrishjoshi.todo.domain.TodoRepository;
import com.harrishjoshi.todo.domain.TodoStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class TodoJob {

    private static final Logger log = LoggerFactory.getLogger(TodoJob.class);
    private final TransactionTemplate transactionTemplate;
    private final TodoRepository todoRepository;

    public TodoJob(TransactionTemplate transactionTemplate, TodoRepository todoRepository) {
        this.transactionTemplate = transactionTemplate;
        this.todoRepository = todoRepository;
    }

    @Scheduled(cron = "0 */5 * * * *")
    void execute() {
        log.info("Todo Job started...");
        var pending = true;
        while (pending) {
            pending = Boolean.TRUE.equals(transactionTemplate.execute(txnStatus -> {
                var todos = todoRepository.findTop50ByStatusOrderByCreatedAtDesc(TodoStatus.PENDING);
                if (todos.isEmpty()) {
                    return false;
                }

                todos.forEach(todo -> {
                    log.info("Processing todo: {}", todo.getTitle());
                    todo.setStatus(TodoStatus.IN_PROGRESS);
                    todoRepository.save(todo);
                });
                return true;
            }));
        }

        log.info("Todo Job completed...");
    }
}
