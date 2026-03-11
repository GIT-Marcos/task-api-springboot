package com.usuario.todolist.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record TaskFilterRequest(
        String description,
        LocalDateTime minDate,
        LocalDateTime maxDate,
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
