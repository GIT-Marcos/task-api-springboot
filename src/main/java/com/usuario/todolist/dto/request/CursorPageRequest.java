package com.usuario.todolist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record CursorPageRequest(
        @Schema(description = "ID de la última tarea de la página anterior", example = "34")
        Long lastId,

        @Schema(description = "Fecha de la última tarea de la página anterior (con precisión de nanos)",
                example = "2026-04-06T18:44:33.531869", minimum = "uuuu-MM-dd'T'HH:mm:ss", maximum = "uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime lastDate,

        @Schema(description = "Elementos por página", defaultValue = "15", minimum = "1", maximum = "100")
        Integer size,

        @Schema(description = "Dirección del ordenamiento", defaultValue = "DESC")
        Sort.Direction direction
) {

    private static final int DEFAULT_SIZE = 15;
    private static final int MAX_SIZE = 100;
    private static final int MIN_SIZE = 1;
    private static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.DESC;

    public CursorPageRequest {
        size = normalizeSize(size);
        direction = direction == null ? DEFAULT_DIRECTION : direction;
    }

    private static int normalizeSize(Integer size) {
        if (size == null) {
            return DEFAULT_SIZE;
        }

        if (size < MIN_SIZE || size > MAX_SIZE) {
            return DEFAULT_SIZE;
        }

        return size;
    }
}
