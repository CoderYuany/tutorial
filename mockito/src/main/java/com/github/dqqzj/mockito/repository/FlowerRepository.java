package com.github.dqqzj.mockito.repository;

import com.github.dqqzj.mockito.domain.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: qinzhongjian
 * @since: JDK 1.8.0_212-b10
 * @date: created in 2019/12/14 22:37
 * @description: TODO
 */
@Repository
public interface FlowerRepository extends JpaRepository<Flower, Long> {
}
