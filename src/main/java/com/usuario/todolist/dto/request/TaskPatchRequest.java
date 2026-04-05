package com.usuario.todolist.dto.request;

import jakarta.validation.constraints.Size;

public record TaskPatchRequest(

        @Size(max = 140, message = "La tarea no puede tener más de 140 caracteres.")
        String description,

        Boolean completed
) {
    public TaskPatchRequest {
        if (description != null && !description.isBlank()) {
            description = description.strip().replaceAll("\\s+", " ");
        }
    }
}
