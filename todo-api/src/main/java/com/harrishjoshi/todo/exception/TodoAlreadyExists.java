package com.harrishjoshi.todo.exception;

public class TodoAlreadyExists extends Exception {

    public TodoAlreadyExists(String message) {
        super(message);
    }
}