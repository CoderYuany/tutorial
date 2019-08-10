package com.github.dqqzj.security.service;

import com.github.dqqzj.security.bean.UserDo;
import com.github.dqqzj.security.enums.RestStatusEnum;
import com.github.dqqzj.security.exception.RestStatusException;
import com.github.dqqzj.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Slf4j
@Service
@Primary
public class BaseUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public BaseUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * 注册用户
     * @param userDo
     * @return
     */
    public boolean register(UserDo userDo) {
        String username = userDo.getUsername();
        String password = userDo.getPassword();
        if (exist(username)){
            log.info("该用户已存在，无需注册！");
            throw new RestStatusException(RestStatusEnum.ACCOUNT_EXISTED.getCode().toString());
        }
        userDo.setPassword(bCryptPasswordEncoder.encode(password));
        this.userRepository.save(userDo);
        userDo.setPassword(password);
        return true;
    }

    /**
     * @author qinzhongjian
     * @date 2019-08-10
     * @param: username
     * @return boolean
     * @description: 判断用户是否存在
     */
    private boolean exist(String username){
        Optional<UserDo> optional = userRepository.findById(username);
        return (optional.isPresent());
    }

}
