package com.hwas.auth.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Date;

/**
 * Created by huashe on 2020/3/2.
 */
public class JWTUtil {
    public static final long TOKEN_EXPIRE_TIME = 5 * 60 * 1000; //token过期时间
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 10 * 60 * 1000; //refreshToken过期时间
    private static final String ISSUER = "hwas"; //签发人

    /**
     * 生成签名
     */
    public static String generateToken(String username, String salt) {
        Date now = new Date();
        Algorithm algorithm = Algorithm.HMAC256(salt); //算法

        String token = JWT.create()
                .withIssuer(ISSUER) //签发人
                .withIssuedAt(now) //签发时间
                .withExpiresAt(new Date(now.getTime() + TOKEN_EXPIRE_TIME)) //过期时间
                .withSubject(username) //保存身份标识
                .sign(algorithm);
        return token;
    }

    /**
     * 验证token
     */
    public static boolean verify(String token,String secretKey) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey); //算法
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 从token获取username
     */
    public static String getUsername(String token) {
        try {
            return JWT.decode(token).getClaim("username").asString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}