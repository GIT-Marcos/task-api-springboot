package com.usuario.todolist.exception;

import org.springframework.http.HttpStatus;

public class TaskNotFoundException extends RuntimeException {

    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public TaskNotFoundException(Long taskId) {
        super("No se ha encontrado la tarea nro: " + taskId);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
