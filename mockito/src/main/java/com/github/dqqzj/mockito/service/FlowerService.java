package com.github.dqqzj.mockito.service;

import com.github.dqqzj.mockito.domain.Flower;

/**
 * @author: qinzhongjian
 * @since: JDK 1.8.0_212-b10
 * @date: created in 2019/12/14 22:49
 * @description: TODO
 */
public interface FlowerService {
    /**
     * @author: qinzhongjian
     * @date: 2019/12/14 22:56
     * @param id 🌹
     * @return: com.github.dqqzj.mockito.domain.Flower
     * @description: 根据ID查找
     */
    Flower findById(long id);
    /**
     * @author: qinzhongjian
     * @date: 2019/12/14 22:56
     * @param flower
     * @return: com.github.dqqzj.mockito.domain.Flower
     * @description: 保存
     */
    Flower save(Flower flower);
}
