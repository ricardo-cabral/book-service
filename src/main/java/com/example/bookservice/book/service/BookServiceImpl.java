package com.example.bookservice.book.service;

import com.example.bookservice.book.model.Book;
import com.example.bookservice.book.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
class BookServiceImpl implements BookService {

  private final BookRepository repository;

  BookServiceImpl(BookRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Book> findAll() {
    return repository.getAllBooks();
  }

  @Override
  public Book findById(int id) {
    return repository.findById(id);
  }

  @Override
  public List<Book> findBooksHigherThan(int amount) {
    BiPredicate<Integer, Integer> higherFilter = (p, s) -> p >= s;

    return this.findAll().stream().filter(p -> higherFilter.test(p.getPrice(), amount))
      .collect(Collectors.toList());
  }

  @Override
  public List<Book> findBooksLowerThan(int amount) {
    BiPredicate<Integer, Integer> lowerFilter = (p, s) -> p <= s;

    return this.findAll().stream().filter(p -> lowerFilter.test(p.getPrice(), amount))
      .collect(Collectors.toList());
  }

  @Override
  public List<Book> findItemsByAuthorIgnoreCase(String author) {
    return this.findAll().stream()
      .filter(p -> p.getAuthor() != null && p.getAuthor().equalsIgnoreCase(author))
      .collect(Collectors.toList());

  }

  @Override
  public boolean insertNewBook(Book book) {
    return repository.insertBook(book);
  }

  @Override
  public boolean removeBook(int id) {
    Book book =  repository.findById(id);
    return repository.removeBook(book);
  }


}
