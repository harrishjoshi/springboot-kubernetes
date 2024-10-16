package com.harrishjoshi.todo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
public class TodosDTO {

    private List<TodoDTO> data;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    @JsonProperty("isFirst")
    private boolean isFirst;
    @JsonProperty("isLast")
    private boolean isLast;
    private boolean hasNext;
    private boolean hasPrevious;

    public TodosDTO(Page<TodoDTO> todoPage) {
        this.setData(todoPage.getContent());
        this.setTotalElements(todoPage.getTotalElements());
        this.setTotalPages(todoPage.getTotalPages());
        this.setCurrentPage(todoPage.getNumber() + 1);
        this.setFirst(todoPage.isFirst());
        this.setLast(todoPage.isLast());
        this.setHasNext(todoPage.hasNext());
        this.setHasPrevious(todoPage.hasPrevious());
    }
}
