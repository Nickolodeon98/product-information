package com.example.productinformation.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.productinformation.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class ItemControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ProductService productService;

}