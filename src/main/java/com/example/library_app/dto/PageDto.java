package com.example.library_app.dto;

public record PageDto<T>(
        Long size,
        Long totalCountElements,
        Long startElementIndex,
        Long endElementIndex,
        T content
) {
}
