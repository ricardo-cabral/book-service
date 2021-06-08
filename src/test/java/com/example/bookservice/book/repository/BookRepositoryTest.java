package com.example.bookservice.book.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookRepositoryTest {

  @Autowired
  BookRepository repository;

  @Test
  public void shouldGetAllBooks(){
    assertEquals(5, repository.getAllBooks().size());
  }
}
