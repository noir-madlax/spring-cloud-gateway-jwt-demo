package com.hwas.gateway.dto;

import lombok.Data;

/**
 * Created by huashe on 2020/3/2.
 */
@Data
public class Response {

    Integer code;
    String msg;

    public Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
