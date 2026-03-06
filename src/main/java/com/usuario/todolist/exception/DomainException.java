package com.usuario.todolist.exception;

import org.springframework.http.HttpStatus;

/**
 * Para excepciones de negocio
 */
public abstract class DomainException extends RuntimeException {

    private final HttpStatus status;

    public DomainException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
