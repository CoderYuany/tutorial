package com.github.dqqzj.security.enums;

import com.google.common.collect.ImmutableMap;

/**
 * @author qinzhongjian
 * @date created in 2019-08-10 13:53
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public enum  RoleEnum {
    ADMIN(1,"ROLE_ADMIN"), USER(2,"ROLE_USER"),BRONZE(3,"ROLE_BRONZE"),SILVER(4,"ROLE_SILVER");
    private final Integer index;
    private final String role;

    private static final ImmutableMap<Integer, RoleEnum> CACHE;

    static {
        final ImmutableMap.Builder<Integer, RoleEnum> builder = ImmutableMap.builder();
        for (RoleEnum roleEnum : values()) {
            builder.put(roleEnum.getIndex(), roleEnum);
        }
        CACHE = builder.build();
    }

    public static RoleEnum valueOfCode(Integer code) {
        final RoleEnum status = CACHE.get(code);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + code + "]");
        }
        return status;
    }

    RoleEnum(Integer index, String role) {
        this.index = index;
        this.role = role;
    }

    public Integer getIndex() {
        return index;
    }

    public String getRole() {
        return role;
    }
}
