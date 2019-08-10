package com.github.dqqzj.security.bean;

import com.github.dqqzj.security.enums.RoleEnum;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:50
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Entity
@Table
@Data
public class UserDo implements UserDetails {
    @Id
    private String username;
    private Long phone;
    private String email;
    private String password;
    private String role = RoleEnum.USER.getRole();
    private Double balance;
    private LocalDateTime lastLoginTime = LocalDateTime.now();
    private LocalDateTime registerTime = LocalDateTime.now();
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return AuthorityUtils.commaSeparatedStringToAuthorityList(role);
    }
}
