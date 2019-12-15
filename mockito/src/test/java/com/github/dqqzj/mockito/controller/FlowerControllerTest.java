package com.github.dqqzj.mockito.controller;

import com.github.dqqzj.mockito.domain.Flower;
import com.github.dqqzj.mockito.service.FlowerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlowerController.class)
class FlowerControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    FlowerService flowerService;

    @Test
    public void contextLoads() throws Exception {
        when(flowerService.findById(anyLong())).thenReturn(Flower.builder().id(0L).name("Rose").price(88D).build());
        this.mvc.perform(get("/find/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":0,\"name\":\"Rose\",\"price\":88.0}"));
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
}