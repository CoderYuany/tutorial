package com.github.dqqzj.security.model.response;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;

/**
 * @author qinzhongjian
 * @date created in 2018/7/31 10:52
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class RestResponse {
    private Object var0;
    private Object var1;
    private Object var2;
    private Object var3;

    public static RestResponse ok(@Null Object var0,@Null Object var1,@Null Object var2,@Null Object var3){
        RestResponse restResponse = new RestResponse(var0,var1,var2,var3);
        return restResponse;
    }

    public RestResponse(Object var0, Object var1, Object var2, Object var3) {
        this.var0 = var0;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
    }
}
