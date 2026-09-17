package com.xb.hutool;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Hutool JWT 鉴权测试")
class JwtDemoTest {

    private static final String KEY = "test-secret-key-2024";

    @Test
    @DisplayName("生成并解析 JWT Token")
    void createAndParseJwt() {
        String token = JWT.create()
            .setPayload("uid", 1001)
            .setPayload("role", "admin")
            .setKey(KEY.getBytes())
            .sign();

        assertNotNull(token);
        assertTrue(token.contains("."));

        JWT jwt = JWTUtil.parseToken(token);
        assertEquals(1001, ((Number) jwt.getPayload("uid")).intValue());
        assertEquals("admin", jwt.getPayload("role"));
    }

    @Test
    @DisplayName("JWT 验签通过")
    void jwtVerifySuccess() {
        String token = JWT.create()
            .setPayload("sub", "test-user")
            .setKey(KEY.getBytes())
            .sign();

        assertTrue(JWTUtil.verify(token, KEY.getBytes()));
    }

    @Test
    @DisplayName("JWT 验签失败 - 错误密钥")
    void jwtVerifyFail() {
        String token = JWT.create()
            .setPayload("sub", "test-user")
            .setKey(KEY.getBytes())
            .sign();

        assertFalse(JWTUtil.verify(token, "wrong-key".getBytes()));
    }

    @Test
    @DisplayName("HS256 签名器")
    void hs256Signer() {
        String token = JWT.create()
            .setPayload("sub", "hutool-demo")
            .setSigner(JWTSignerUtil.hs256(KEY.getBytes()))
            .sign();

        assertNotNull(token);
        assertTrue(JWTUtil.verify(token, KEY.getBytes()));
    }

    @Test
    @DisplayName("未过期 Token 验证通过")
    void notExpiredToken() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);

        String token = JWT.create()
            .setExpiresAt(cal.getTime())
            .setKey(KEY.getBytes())
            .sign();

        assertDoesNotThrow(() -> JWTValidator.of(token).validateDate());
    }

    @Test
    @DisplayName("已过期 Token 验证失败")
    void expiredToken() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);

        String token = JWT.create()
            .setExpiresAt(cal.getTime())
            .setKey(KEY.getBytes())
            .sign();

        assertThrows(Exception.class, () -> JWTValidator.of(token).validateDate());
    }
}
