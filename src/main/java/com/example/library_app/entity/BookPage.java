package com.example.library_app.entity;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@RequiredArgsConstructor
@NoArgsConstructor
public class BookPage {
    @NonNull
    private String text;
}
