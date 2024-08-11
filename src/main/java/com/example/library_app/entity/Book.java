package com.example.library_app.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Book {
    private String id;

    @NonNull
    private String name;
    @NonNull
    private String author;
    @NonNull
    private List<BookPage> bookPages;
    @NonNull
    private BigDecimal price;
}
