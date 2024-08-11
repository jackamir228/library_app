package com.example.library_app.services;

import com.example.library_app.dto.BookDtoRequest;
import com.example.library_app.dto.BookDtoResponse;
import com.example.library_app.dto.BookPageDtoResponse;
import com.example.library_app.dto.PageDto;
import com.example.library_app.entity.Book;
import com.example.library_app.entity.BookPage;
import com.example.library_app.repositories.BookRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    @NonNull
    private final BookRepository bookRepository;

    public BookDtoResponse create(BookDtoRequest body) {
        Book createdBook = convertBookRequestToEntity(body);
        bookRepository.add(createdBook);
        return convertToBookResponse(createdBook, LocalDateTime.now());
    }

    public List<BookDtoResponse> getAll() {
        return bookRepository.getAll().stream()
                .map(this::convertToBookResponse)
                .toList();
    }

    public BookDtoResponse getByKey(String key) {
        return bookRepository.getByUniqueKey(key)
                .map(this::convertToBookResponse)
                .orElseThrow(() -> new RuntimeException("Book with this id not found"));
    }

    public void updateByKey(String key, BookDtoRequest body) {
        bookRepository.updateByKey(key, convertBookRequestToEntity(body));
    }

    public BookDtoResponse patch(String key, BookDtoRequest updateDto) {
        Book oldBook = bookRepository.getByUniqueKey(key).orElseThrow();
        oldBook.setName(updateDto.name() != null ? updateDto.name() : oldBook.getName());
        oldBook.setAuthor(updateDto.author() != null ? updateDto.author() : oldBook.getAuthor());
        oldBook.setPrice(updateDto.price() != null ? updateDto.price() : oldBook.getPrice());
        oldBook.setBookPages(updateDto.bookPages() != null ? updateDto.bookPages() : oldBook.getBookPages());
        bookRepository.updateByKey(key, oldBook);
        return convertToBookResponse(oldBook);
    }

    public boolean deleteByKey(String key) {
        return bookRepository.deleteByUniqueKey(key);
    }

    public boolean deleteAll() {
        return bookRepository.deleteAll();
    }


    private Book convertBookRequestToEntity(BookDtoRequest body) {
        return Book.builder()
                .name(checkName(body))
                .author(checkAuthor(body))
                .bookPages(checkPages(body))
                .price(checkPrice(body))
                .build();
    }

    private BookDtoResponse convertToBookResponse(Book book) {
        return convertToBookResponse(book, null);
    }

    private BookDtoResponse convertToBookResponse(Book book, LocalDateTime timeApply) {
        return new BookDtoResponse(
                book.getName(),
                book.getAuthor(),
                book.getBookPages(),
                book.getPrice(),
                timeApply,
                book.getId()
        );
    }

    private String checkName(BookDtoRequest body) {
        return body.name() == null ? "Без названия" : body.name();
    }

    private String checkAuthor(BookDtoRequest body) {
        return body.name() == null ? "Неизвестен" : body.author();
    }

    private List<BookPage> checkPages(BookDtoRequest body) {
        return body.bookPages() == null ? List.of(new BookPage("текст страницы")) : body.bookPages();
    }

    private BigDecimal checkPrice(BookDtoRequest body) {
        return body.price() == null ? BigDecimal.ZERO : body.price();
    }

    public PageDto<List<BookPageDtoResponse>> getPages(String key, Long startIndex, Long pageSize) {
        Book book = bookRepository.getByUniqueKey(key).orElseThrow();
        long totalCountPages = book.getBookPages().size();
        List<BookPageDtoResponse> pagesText = book.getBookPages().stream()
                .map(e -> new BookPageDtoResponse(e.getText(), book.getName()))
                .skip(startIndex)
                .limit(pageSize)
                .toList();
        return new PageDto<>((long)pagesText.size(),
                totalCountPages,
                startIndex,
                startIndex + pageSize,
                pagesText
        );
    }
}
