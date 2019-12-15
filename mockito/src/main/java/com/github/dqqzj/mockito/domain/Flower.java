package com.github.dqqzj.mockito.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author: qinzhongjian
 * @since: JDK 1.8.0_212-b10
 * @date: created in 2019/12/14 22:43
 * @description: TODO
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flower implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Double price;
}
