package com.example.bookservice.book.repository;

import com.example.bookservice.book.controller.dto.BookDTO;
import com.example.bookservice.book.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Log
public final class BookRepository {
  private final List<Book> database = new ArrayList<>();
  private final Resource booksJson;
  private final ObjectMapper mapper;

  public BookRepository(@Value("classpath:books.json") Resource booksJson, ObjectMapper mapper) {
    this.booksJson = booksJson;
    this.mapper = mapper;

    try {
      List<BookDTO> loadedList =
        Arrays.asList(mapper.readValue(booksJson.getFile(), BookDTO[].class));
      database.addAll(loadedList.stream()
        .map(dto -> new Book(dto.getId(), dto.getName(), dto.getAuthor(), dto.getPrice()))
        .collect(Collectors.toList()));

    } catch (IOException e) {
      e.printStackTrace();
      log.severe("DATABASE NOT INITIALIZED!!!!!!!" + e.getCause());
    }
  }

  public List<Book> getAllBooks() {
    return this.database;
  }

  public boolean insertBook(Book book) {
    return this.database.add(book);
  }


  public boolean removeBook(Book book) {
    return this.database.remove(book);
  }

  public Book findById(int id) {

    return this.database.stream().filter(b -> b.getId() == id).findFirst()
      .orElseThrow(() -> new RuntimeException());
  }
}
