package com.example.library_app.dto;

import com.example.library_app.entity.BookPage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BookDtoResponse(
        String name,
        String author,
        List<BookPage> bookPages,
        BigDecimal price,
        LocalDateTime timeApply,
        String key
) {
}
