package com.github.dqqzj.security.repository;

import com.github.dqqzj.security.bean.UserDo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public interface UserRepository extends JpaRepository<UserDo, String> {

}
