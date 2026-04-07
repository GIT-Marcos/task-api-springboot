package com.usuario.todolist.dto.request;

import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

public record CursorPageRequest(
        Long lastId,
        LocalDateTime lastDate,
        Integer size,
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
