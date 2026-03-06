package com.usuario.todolist.dto;

import java.time.LocalDate;

public record TaskFilterDTO(
        String description,
        LocalDate minDate,
        LocalDate maxDate,
        Boolean completed
) {
}
