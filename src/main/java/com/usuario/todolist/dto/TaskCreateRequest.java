package com.usuario.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskCreateRequest(

        @NotNull(message = "La descripción no puede ser nula.")
        @NotBlank(message = "La descripción de la tarea es obligatoria.")
        @Size(max = 60, message = "La tarea no puede tener más de 60 caracteres.")
        String description
) {
    public TaskCreateRequest {
        if (description != null) {
            description = description.strip().replaceAll("\\s+", " ");
        }
    }
}
