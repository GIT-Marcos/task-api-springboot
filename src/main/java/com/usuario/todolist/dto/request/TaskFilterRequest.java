package com.usuario.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record TaskFilterRequest(
        @Schema(description = "Texto contenido en la descripción", example = "comprar pan")
        String description,

        @Schema(description = "Fecha inicial (inclusive)", example = "2026-04-06T18:44:33", minimum = "uuuu-MM-dd'T'HH:mm")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime minDate,

        @Schema(description = "Fecha final (inclusive)", example = "2027-04-07T10:00:00", minimum = "uuuu-MM-dd'T'HH:mm")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime maxDate,

        @Schema(description = "Estado de la tarea", example = "false")
        Boolean completed
) {
    public TaskFilterRequest {
        if (description != null) {
            description = description.strip().replaceAll("\\s+", " ");
        }
    }

    public TaskFilterRequest(String description, LocalDate minDate, LocalDate maxDate, Boolean completed) {
        this(
                description,
                (minDate != null) ? minDate.atStartOfDay() : null,
                (maxDate != null) ? maxDate.atTime(LocalTime.MAX) : null,
                completed
        );
    }
}
