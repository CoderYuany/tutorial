package com.github.dqqzj.mockito.controller;

import com.github.dqqzj.mockito.domain.Flower;
import com.github.dqqzj.mockito.service.FlowerService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: qinzhongjian
 * @since: JDK 1.8.0_212-b10
 * @date: created in 2019/12/14 22:47
 * @description: TODO
 */
@RestController
public class FlowerController {
    @Autowired
    FlowerService flowerService;
    @GetMapping("get/{id}")
    public ResponseEntity<Flower> get(@NonNull @PathVariable Long id) {
        Flower flower = flowerService.findById(id);
        return ResponseEntity.ok().body(flower);
    }
    @PostMapping("save")
    public ResponseEntity<Flower> save(@NonNull Flower flower) {
        if (StringUtils.isEmpty(flower.getName())) {
            return ResponseEntity.ok().build();
        }
        Flower returnFlower = flowerService.save(flower);
        if (returnFlower == null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok().body(flower);
    }
}
