package com.example.bookservice.book.service;

import com.example.bookservice.book.model.Book;
import com.example.bookservice.book.repository.BookRepository;
import com.example.bookservice.config.JacksonConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookServiceImpl.class, BookRepository.class, JacksonConfig.class})
public class BookServiceTest {

  @Autowired
  BookRepository repository;

  @Autowired
  BookService service;

  @Test
  public void findItemsWherePriceIsHigherThan() {

    final int amount = 25;
    List<Book> books = service.findBooksHigherThan(amount);
    assertEquals(2, books.size());

  }

  @Test
  public void findItemsWherePriceIsLowerThan() {

    final int amount = 29;
    List<Book> books = service.findBooksLowerThan(amount);
    assertEquals(4, books.size());

  }

  @Test
  public void findItemsWherePriceIsHigherOrEqualsFiveAndReturnEmpty() {

    final int amount = 5;
    List<Book> books = service.findBooksLowerThan(amount);
    assertEquals(0, books.size());
    System.out.println(Arrays.asList(books));
  }

  @Test
  public void shouldTryToFindDanSimmonsAuthor_AndReturnBookWithId1() {

    final String author = "dan simmons";
    List<Book> books =
      service.findAll().stream().filter(p -> p.getAuthor() != null && p.getAuthor().equalsIgnoreCase(author))
        .collect(Collectors.toList());

    assertEquals(1, books.size());
    System.out.println(Arrays.asList(books));
  }

  @Test
  public void shouldTryToFindVernorVingeAuthor_AndReturnBookWithId2And5() {

    final String author = "vernor vinge";
    List<Book> books = service.findItemsByAuthorIgnoreCase(author);

    assertEquals(2, books.size());

  }

  @Test
  public void shouldTryToFindVingeAuthor_AndReturnEmpty() {

    final String author = "vinge";
    List<Book> books = service.findItemsByAuthorIgnoreCase(author);

    assertEquals(0, books.size());
    System.out.println(Arrays.asList(books));
  }

}
