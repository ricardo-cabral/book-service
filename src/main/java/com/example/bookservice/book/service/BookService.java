package com.example.bookservice.book.service;

import com.example.bookservice.book.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BookService {
  List<Book> findAll();

  Book findById(final int id);

  List<Book> findBooksHigherThan(final int amount);

  List<Book> findBooksLowerThan(final int amount);

  List<Book> findItemsByAuthorIgnoreCase(final String author);

  Book insertNewBook(Book book);

  boolean removeBook(int id);
}
