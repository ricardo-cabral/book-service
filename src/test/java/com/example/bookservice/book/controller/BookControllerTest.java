package com.example.bookservice.book.controller;

import com.example.bookservice.book.controller.dto.BookDTO;
import com.example.bookservice.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.Resource;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//I Have added order here just because I am inserting and removing data in the same unit of tests
//re al world I would have a separated tests for each of those
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
  @Order(1)
  public void shouldFillAllBooks_andMatchResourceFile() throws Exception {
    List<BookDTO> books = Arrays.asList(mapper.readValue(booksJson.getFile(), BookDTO[].class));

    assertEquals(5, books.size());

    mvc.perform((get(URL)).contentType(APPLICATION_JSON)).andExpect(status().isOk()).andDo(print())
      .andExpect(jsonPath("$", hasSize(5)));

  }

  @Test
  @Order(2)
  public void shouldFindByAmountLowerThan29_returningFourBooksWithIds1_2_4_5() throws Exception {
    mvc.perform((get(URL + "/lower")).contentType(APPLICATION_JSON).param("amount", "29"))
      .andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$", hasSize(4)));
  }

  @Test
  @Order(3)
  public void shouldFindByAmountHigherThan25_returningFourBooksWithIds3_4() throws Exception {
    mvc.perform((get(URL + "/higher")).contentType(APPLICATION_JSON).param("amount", "25"))
      .andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  @Order(4)
  public void shouldGetBookById() throws Exception {
    mvc.perform(get(URL + "/1")).andExpect(status().isOk())
      .andDo(print()).andExpect(jsonPath("$.author", is("Dan Simmons")));
  }

  @Test
  @Order(5)
  public void shouldFindBooksByVernorVingeAuthor_andReturnBooksWithId2and5() throws Exception {
    mvc
      .perform((get(URL + "/author")).contentType(APPLICATION_JSON).param("author", "vernor vinge"))
      .andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  @Order(6)
  public void shouldCreateANewBook_returningCreatedStatus() throws Exception {

    BookDTO dto = getBookDTO(0);
    String data = mapper.writeValueAsString(dto);
    mvc.perform(post(URL).contentType(APPLICATION_JSON).accept(APPLICATION_JSON).content(data))
      .andExpect(status().isCreated());
  }


  @Test
  @Order(7)
  public void shouldUpdateExistingBook_returningAcceptedAndNewValue() throws Exception {
    BookDTO dto = getBookDTO(1);
    String data = mapper.writeValueAsString(dto);

    mvc.perform(put(URL + "/" + dto.getId()).contentType(APPLICATION_JSON).content(data))
      .andExpect(status().isAccepted());
  }

  @Test
  @Order(8)
  public void shouldDeleteAnExistentBook_returningNoContent() throws Exception {

    BookDTO dto = getBookDTO(0);
    String data = mapper.writeValueAsString(dto);
    MvcResult result =
      mvc.perform(post(URL).contentType(APPLICATION_JSON).accept(APPLICATION_JSON).content(data))
        .andExpect(status().isCreated()).andReturn();
    BookDTO dtoResult = mapper.readValue(result.getResponse().getContentAsString(), BookDTO.class);


    mvc.perform(delete(URL + "/" + dtoResult.getId()).contentType(APPLICATION_JSON))
      .andExpect(status().isNoContent());
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

