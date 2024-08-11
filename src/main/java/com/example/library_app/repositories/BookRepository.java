package com.example.library_app.repositories;

import com.example.library_app.entity.Book;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookRepository {
    private final Map<String, Book> books = new HashMap<>();

    /**
     * Create (CREATE)
     */
    public Book add(Book book) {
        String uuid = UUID.randomUUID().toString().substring(0, 5);
        book.setId(uuid);
        books.put(uuid, book);
        return book;
    }

    /**
     * Get all
     */
    @SneakyThrows
    public List<Book> getAll() {
        Thread.sleep(2000);
        return new ArrayList<>(books.values());
    }

    /**
     * find unique Book (READ)
     */
    public Optional<Book> getByUniqueKey(String uniqueKey) {
       return Optional.ofNullable(books.get(uniqueKey));
    }

    /**
     * UPDATE
     */
    public void updateByKey(String key, Book newBook) {
        if (!books.containsKey(key)) {
            throw new RuntimeException("book is not exists");
        }
        books.put(key, newBook);
    }

    /**
     * Delete unique Book (READ)
     */
    public boolean deleteByUniqueKey(String key) {
       return books.remove(key) != null;
    }

    /**
     * Delete all (DELETE)
     */
    public boolean deleteAll() {
         books.clear();
         return books.isEmpty();
    }
}
