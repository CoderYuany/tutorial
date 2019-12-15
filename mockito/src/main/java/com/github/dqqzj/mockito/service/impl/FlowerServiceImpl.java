package com.github.dqqzj.mockito.service.impl;

import com.github.dqqzj.mockito.domain.Flower;
import com.github.dqqzj.mockito.repository.FlowerRepository;
import com.github.dqqzj.mockito.service.FlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author: qinzhongjian
 * @since: JDK 1.8.0_212-b10
 * @date: created in 2019/12/14 22:51
 * @description: TODO
 */
@Service
public class FlowerServiceImpl implements FlowerService {
    @Autowired
    FlowerRepository flowerRepository;
    @Override
    public Flower findById(long id) {
        Flower flower = Flower.builder().id(0L).name("Ramat").price(88D).build();
        return flowerRepository.findById(id).orElseGet(() -> flower);
    }

    @Override
    public Flower save(Flower flower) {
        try {
           return flowerRepository.save(flower);
        }catch (Exception e) {
            return null;
        }
    }
}
