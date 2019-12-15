package com.github.dqqzj.mockito;

import com.github.dqqzj.mockito.controller.FlowerController;
import com.github.dqqzj.mockito.domain.Flower;
import com.github.dqqzj.mockito.repository.FlowerRepository;
import com.github.dqqzj.mockito.service.FlowerService;
import com.github.dqqzj.mockito.service.impl.FlowerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@WebMvcTest(FlowerController.class)
class MockitoApplicationTests {
	/*
	@InjectMocks
	FlowerService flowerService = new FlowerServiceImpl();

	@Mock
	FlowerRepository flowerRepository;*/

	@Autowired
	MockMvc mvc;

	@MockBean
	FlowerService flowerService;

	@MockBean
	FlowerRepository flowerRepository;

	@Test
	public void contextLoads() throws Exception {
		given(this.flowerService.findById(anyLong()))
				.willReturn(Flower.builder().id(0L).name("xxxxx").price(88D).build());
		/*this.mvc.perform(get("/get/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("{\"id\":0,\"name\":\"xxxxx\",\"price\":88.0}"));
		when(flowerRepository.findById(anyLong())).thenReturn(Optional.of(Flower.builder().id(0L).name("xxxxx").price(66D).build()));*/

	}

	@BeforeEach
	public void before(){
		System.out.println(13333);
	}

}
