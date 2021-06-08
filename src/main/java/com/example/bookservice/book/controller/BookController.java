package com.example.bookservice.book.controller;


import com.example.bookservice.book.controller.dto.BookDTO;
import com.example.bookservice.book.model.Book;
import com.example.bookservice.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;



@RestController
@RequestMapping("/book")
@Validated
public class BookController {

  private final BookService service;

  public BookController(BookService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<BookDTO> insert(@RequestBody BookDTO dto) {

    Book book = new Book(dto.getName(), dto.getAuthor(), dto.getPrice());
    Book persisted = service.insertNewBook(book);


    return ResponseEntity.created(URI.create("/" + persisted.getId())).body(
      new BookDTO(persisted.getId(), persisted.getName(), persisted.getAuthor(),
        persisted.getPrice()));
  }

  @PutMapping("/{id}")
  //TODO not enough time to complete
  public ResponseEntity<Book> update(@PathVariable Integer id, @RequestBody BookDTO dto) {

    return ResponseEntity.accepted().body(new Book(dto.getName(), dto.getAuthor(), dto.getPrice()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Book> delete(@PathVariable Integer id) {
    if (service.removeBook(id)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping
  public ResponseEntity<List<Book>> findAll() {
    return ResponseEntity.ok().body(service.findAll());
  }

  @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable int id){
      return ResponseEntity.ok().body(service.findById(id));
    }

  @GetMapping("/lower")
  public ResponseEntity<List<Book>> findByAmountLowerThan(@RequestParam("amount") int amount) {
    return ResponseEntity.ok(service.findBooksLowerThan(amount));
  }

  @GetMapping("/higher")
  public ResponseEntity<List<Book>> findByAmountHigherThan(int amount) {
    return ResponseEntity.ok(service.findBooksHigherThan(amount));
  }

  @GetMapping("/author")
  public ResponseEntity<List<Book>> findByAuthor(String author) {
    return ResponseEntity.ok(service.findItemsByAuthorIgnoreCase(author));
  }
}
