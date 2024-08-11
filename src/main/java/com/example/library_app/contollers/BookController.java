package com.example.library_app.contollers;

import com.example.library_app.dto.BookDtoRequest;
import com.example.library_app.dto.BookDtoResponse;
import com.example.library_app.dto.BookPageDtoResponse;
import com.example.library_app.dto.PageDto;
import com.example.library_app.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@CacheConfig(cacheNames = "findAll")
public class BookController {
    private final BookService bookService;

    //GET http://localhost:8080/book - получение всех книг
    // response entity по умолчанию 200
    @Cacheable(cacheNames = "findAll")
    @GetMapping()
    @CachePut(cacheNames = "findAll", condition = "#refresh==true")
    public List<BookDtoResponse> getAll(@RequestParam(required = false) boolean refresh) {
        return bookService.getAll();
    }

    //GET http://localhost:8080/book/key
    @GetMapping("/{key}")
    public BookDtoResponse getByKey(@PathVariable String key) {
        return bookService.getByKey(key);
    }

    //POST http://localhost:8080/book - создание новой книги
    @PostMapping()
    @CacheEvict(cacheNames = "findAll,", allEntries = true)
    public ResponseEntity<BookDtoResponse> createBook(@RequestBody BookDtoRequest dtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(dtoRequest));
    }

    //PUT http://localhost:8080/book/key - обновление книги по ключу (все поля в dto должны быть заполнены)
    @PutMapping("/{key}")
    public ResponseEntity<?> updateBook(@PathVariable String key, @RequestBody BookDtoRequest body) {
        bookService.updateByKey(key, body);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PatchMapping("/{key}")
    public ResponseEntity<BookDtoResponse> patch(@PathVariable String key, @RequestBody BookDtoRequest body) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(bookService.patch(key, body));
    }

    //DELETE http://localhost:8080/book/key - удаление книги (по уникальному значению) по ключу
    @DeleteMapping("/{key}")
    public ResponseEntity<?> deleteById(@PathVariable String key) {
        boolean result = bookService.deleteByKey(key);
        return result
                ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //DELETE http://localhost:8080/book/ - удаление всех книг
    @DeleteMapping()
    public ResponseEntity<?> deleteAll() {
        boolean result = bookService.deleteAll();
        return result
                ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{bookKey}/page")
    public PageDto<List<BookPageDtoResponse>> getPages(
            @PathVariable String bookKey,
            @RequestParam Long startIndex,
            @RequestParam Long pageSize
    ) {
        return bookService.getPages(bookKey, startIndex, pageSize);
    }


}
