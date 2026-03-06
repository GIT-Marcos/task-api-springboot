package com.usuario.todolist.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long taskId) {
        super("No se ha encontrado la tarea nro: " + taskId);
    }
}
