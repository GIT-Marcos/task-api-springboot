package com.usuario.todolist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskUpdateRequest(

        @NotNull(message = "La descripción no puede ser nula.")
        @NotBlank(message = "La descripción de la tarea es obligatoria.")
        @Size(max = 60, message = "La tarea no puede tener más de 60 caracteres.")
        String description,

        @NotNull(message = "El estado no puede ser nulo.")
        Boolean completed
) {
    public TaskUpdateRequest {
        if (description != null) {
            description = description.strip().replaceAll("\\s+", " ");
        }
    }
}
