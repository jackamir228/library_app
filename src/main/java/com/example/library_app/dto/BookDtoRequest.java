package com.example.library_app.dto;


import com.example.library_app.entity.BookPage;

import java.math.BigDecimal;
import java.util.List;

public record BookDtoRequest(
        String name,
        String author,
        List<BookPage> bookPages,
        BigDecimal price
) {
}
