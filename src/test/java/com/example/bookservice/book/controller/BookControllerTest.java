package com.example.bookservice.book.controller;

import com.example.bookservice.book.controller.dto.BookDTO;
import com.example.bookservice.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerTest {
  private static final String URL = "/book";

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @Value("classpath:books.json")
  private Resource booksJson;

  @Autowired
  private BookService service;

  @Test
  public void shouldCreateANewBook_returningCreatedStatus() throws Exception {

    BookDTO dto = getBookDTO(0);
    String data = mapper.writeValueAsString(dto);
    mvc.perform(post(URL).contentType(APPLICATION_JSON).accept(APPLICATION_JSON).content(data))
      .andExpect(status().isCreated());
  }


  @Test
  public void shouldUpdateExistingBook_returningAcceptedAndNewValue() throws Exception {
    BookDTO dto = getBookDTO(1);
    String data = mapper.writeValueAsString(dto);

    mvc.perform(put(URL + "/" + dto.getId()).contentType(APPLICATION_JSON).content(data))
      .andExpect(status().isAccepted());
  }

  @Test
  public void shouldDeleteAnExistentBook_returningNoContent() throws Exception {
    BookDTO dto = getBookDTO(1);
    mvc.perform(delete(URL + "/" + dto.getId()).contentType(APPLICATION_JSON))
      .andExpect(status().isNoContent());
  }

  @Test
  public void shouldFillAllBooks_andMatchResourceFile() throws Exception {
    List<BookDTO> books = Arrays.asList(mapper.readValue(booksJson.getFile(), BookDTO[].class));

    assertEquals(5, books.size());

    mvc.perform((get(URL)).contentType(APPLICATION_JSON)).andExpect(status().isOk()).andDo(print())
      .andExpect(jsonPath("$", hasSize(5)));

  }


  private BookDTO getBookDTO(int id) {
    BookDTO dto = new BookDTO();
    if (id > 0) {
      dto.setId(id);
    }
    dto.setName("Hyperion");
    dto.setAuthor("Dan Simmons");
    dto.setPrice(20);
    return dto;
  }
}

