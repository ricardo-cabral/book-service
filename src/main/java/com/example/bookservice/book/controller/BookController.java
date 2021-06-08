package com.example.bookservice.book.controller;


import com.example.bookservice.book.controller.dto.BookDTO;
import com.example.bookservice.book.model.Book;
import com.example.bookservice.book.service.BookService;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@RestController
@RequestMapping("/book")
@Validated
public class BookController {

  private final BookService service;

  public BookController(BookService service){
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<Book> insert(@RequestBody BookDTO dto){

    return  ResponseEntity.created(URI.create("")).body(new Book(dto.getName(), dto.getAuthor(), dto.getPrice()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Book> update(@PathVariable String id, @RequestBody BookDTO dto){

    return ResponseEntity.accepted().body(new Book(dto.getName(), dto.getAuthor(), dto.getPrice()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Book> delete(@PathVariable String id) {
      return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<Book>> findAll(){
    return ResponseEntity.ok().body(service.findAll());
  }
}
