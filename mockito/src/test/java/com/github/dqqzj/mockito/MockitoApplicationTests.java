package com.github.dqqzj.mockito;

import com.github.dqqzj.mockito.domain.Flower;
import com.github.dqqzj.mockito.repository.FlowerRepository;
import com.github.dqqzj.mockito.service.impl.FlowerServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class MockitoApplicationTests {

	@InjectMocks
	FlowerServiceImpl flowerService;

	@Mock
	FlowerRepository flowerRepository;

	@Test
	public void contextLoads() {
		Flower flower = Flower.builder().id(0L).name("xxxxx").price(66D).build();
		given(this.flowerRepository.findById(anyLong()))
				.willReturn(Optional.of(flower));
		Flower findFlower = this.flowerService.findById(0L);
		Assert.assertEquals(findFlower,flower);
	}

}
