package com.usuario.todolist.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record TaskFilterDTO(
        String description,
        LocalDateTime minDate,
        LocalDateTime maxDate,
        Boolean completed
) {
    public TaskFilterDTO {
        if (description != null) {
            description = description.strip().replaceAll("\\s+", " ");
        }
    }

    public TaskFilterDTO(String description, LocalDate minDate, LocalDate maxDate, Boolean completed) {
        this(
                description,
                (minDate != null) ? minDate.atStartOfDay() : null,
                (maxDate != null) ? maxDate.atTime(LocalTime.MAX) : null,
                completed
        );
    }
}
