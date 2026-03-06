package com.usuario.todolist.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedTaskException extends DomainException {

    public DuplicatedTaskException(String duplicatedTaskName) {
        super("Ya existe una tarea con el nombre '" + duplicatedTaskName + "'", HttpStatus.CONFLICT);
    }
}
