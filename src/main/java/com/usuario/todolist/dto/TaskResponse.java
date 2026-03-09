package com.usuario.todolist.dto;

import com.usuario.todolist.entity.Task;

import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String description,
        Boolean completed,
        LocalDateTime creationDate
) {

    public TaskResponse(Task task) {
        this(
                task.getId(),
                task.getDescription(),
                task.getCompleted(),
                task.getDate()
        );
    }

}
