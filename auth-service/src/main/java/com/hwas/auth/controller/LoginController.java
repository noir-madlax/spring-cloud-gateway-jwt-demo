package com.hwas.auth.controller;

import com.hwas.auth.dto.AuthResult;
import com.hwas.auth.utils.JWTUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Steven on 2019/10/27.
 */
@RestController
public class LoginController {


    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 登录认证
     *
     * @param username 用户名
     * @param password 密码
     */
    @GetMapping("/login")
    public AuthResult login(@RequestParam String username, @RequestParam String password) {

        if ("admin".equals(username) && "admin".equals(password)) {

            //密钥
            String salt = BCrypt.gensalt();

            //生成token
            String token = JWTUtil.generateToken(username, salt);

            //生成refreshToken
            String refreshToken = UUID.randomUUID().toString();

            //数据放入redis
            redisTemplate.opsForValue().set("token:" + username, salt, JWTUtil.TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
            redisTemplate.opsForValue().set("refreshToken:" + refreshToken, username, JWTUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

            return new AuthResult(0, "success", token, refreshToken);
        } else {
            return new AuthResult(1001, "username or password error");
        }
    }

    /**
     * 刷新token
     */
    @GetMapping("/refreshToken")
    public AuthResult refreshToken(@RequestParam String refreshToken) {
        String username =  redisTemplate.opsForValue().get("refreshToken:" + refreshToken);
        if (StringUtils.isEmpty(username)) {
            return new AuthResult(1003, "refreshToken error");
        }

        //生成新的token
        String salt = BCrypt.gensalt();
        String newToken = JWTUtil.generateToken(username, salt);
        String newRefreshToken = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set("token:" + username, salt, JWTUtil.TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        redisTemplate.delete("refreshToken:" + refreshToken);
        redisTemplate.opsForValue().set("refreshToken:" + newRefreshToken, username, JWTUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        return new AuthResult(0, "success", newToken, newRefreshToken);
    }

    @GetMapping("/")
    public String index() {
        return "auth-service: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}