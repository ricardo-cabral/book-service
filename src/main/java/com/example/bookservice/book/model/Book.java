package com.example.bookservice.book.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@EqualsAndHashCode
@ToString
public class Book {

  private int id;
  @NotBlank(message = "Name is required")
  private String name;
  @NotBlank(message = "Author is required")
  private String author;
  @Min(message = "Price cannot be zero", value = 1)
  private int price;


  public Book(int id, String name, String author, int price){
    this.id = id;
    this.name = name;
    this.author = author;
    this.price = price;
  }

  public Book(String name, String author, int price){
    new Book(0, name, author, price );
  }


}
