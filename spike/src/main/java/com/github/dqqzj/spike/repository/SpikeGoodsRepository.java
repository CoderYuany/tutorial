package com.github.dqqzj.spike.repository;

import com.github.dqqzj.spike.po.SpikeGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public interface SpikeGoodsRepository extends JpaRepository<SpikeGoods, Long> {

    /**
     * @author qinzhongjian
     * @date 2019-08-04
     * @param: count 秒杀商品数量
     * @param: id 秒杀商品id
     * @return java.lang.Integer
     * @description: 会锁表
     */
    @Modifying
    @Transactional
    @Query("UPDATE SpikeGoods SET stock = stock - ?1 WHERE id = ?2 AND stock > ?1")
    Integer updateByLockTable(Integer count, Long id);

    @Modifying
    @Transactional
    @Query("SELECT * FROM SpikeGoods WHERE id=?1 FOR UPDATE")
    SpikeGoods queryByLockTable(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE SpikeGoods  SET stock=stock-?1,version=version+1 WHERE id=?2 AND version = ?3")
    Integer updateOptimistic(Integer count, Long id, Integer version);


}
