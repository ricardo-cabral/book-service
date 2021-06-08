package com.example.bookservice.book.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

  private int id;
  @NotBlank(message = "Name is required")
  private String name;
  @NotBlank(message = "Author is required")
  private String author;
  @Min(message = "Price cannot be zero", value = 1)
  private int price;
}
