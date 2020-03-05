package com.hwas.auth.dto;

/**
 * Created by huashe on 2020/3/2.
 */
public class AuthResult {
    Integer code;
    String msg;
    String token;
    String refreshToken;

    public AuthResult(Integer code, String msg, String token, String refreshToken) {
        this.code = code;
        this.msg = msg;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public AuthResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
