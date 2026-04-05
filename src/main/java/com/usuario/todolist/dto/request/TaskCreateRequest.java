package com.usuario.todolist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskCreateRequest(

        @NotNull(message = "La descripción no puede ser nula.")
        @NotBlank(message = "La descripción de la tarea es obligatoria.")
        @Size(max = 140, message = "La tarea no puede tener más de 140 caracteres.")
        String description
) {
    public TaskCreateRequest {
        if (description != null) {
            description = description.strip().replaceAll("\\s+", " ");
        }
    }
}
