package com.xb.hutool;

import com.xb.hutool.auth.JwtRefreshDemo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JWT 刷新令牌机制测试")
class JwtRefreshDemoTest {

    private JwtRefreshDemo auth;

    @BeforeEach
    void setUp() {
        auth = new JwtRefreshDemo();
    }

    @Nested
    @DisplayName("Access Token")
    class AccessToken {

        @Test
        @DisplayName("创建后可验证通过")
        void createAndValidate() {
            String token = auth.createAccessToken(1001, "admin");
            assertNotNull(token);
            assertTrue(auth.validateAccessToken(token));
        }

        @Test
        @DisplayName("错误密钥签名的 token 验证失败")
        void wrongKeyFails() {
            String token = auth.createAccessToken(1001, "admin");
            assertFalse(auth.validateAccessToken(token + "tampered"));
        }
    }

    @Nested
    @DisplayName("Refresh Token")
    class RefreshToken {

        @Test
        @DisplayName("刷新后返回新 Access Token")
        void refreshReturnsNewAccessToken() {
            String refreshToken = auth.createRefreshToken(1001);
            String newAccess = auth.refreshAccessToken(refreshToken);
            assertNotNull(newAccess);
            assertTrue(auth.validateAccessToken(newAccess));
        }

        @Test
        @DisplayName("刷新后旧 Refresh Token 加入黑名单")
        void oldRefreshBlacklisted() {
            String refreshToken = auth.createRefreshToken(1001);
            auth.refreshAccessToken(refreshToken);
            assertTrue(auth.isBlacklisted(refreshToken));
        }

        @Test
        @DisplayName("重用旧 Refresh Token 抛异常")
        void reuseOldRefreshThrows() {
            String refreshToken = auth.createRefreshToken(1001);
            auth.refreshAccessToken(refreshToken);
            assertThrows(SecurityException.class, () -> auth.refreshAccessToken(refreshToken));
        }

        @Test
        @DisplayName("refreshTokenPair 返回新的双 token 对")
        void refreshTokenPairReturnsNewPair() {
            String refreshToken = auth.createRefreshToken(1001);
            Map<String, String> pair = auth.refreshTokenPair(refreshToken);
            assertNotNull(pair.get("accessToken"));
            assertNotNull(pair.get("refreshToken"));
            assertTrue(auth.validateAccessToken(pair.get("accessToken")));
        }
    }

    @Nested
    @DisplayName("黑名单")
    class Blacklist {

        @Test
        @DisplayName("黑名单中的 token 验证失败")
        void blacklistedTokenFails() {
            String token = auth.createAccessToken(1001, "user");
            String refresh = auth.createRefreshToken(1001);
            auth.refreshAccessToken(refresh);
            assertEquals(1, auth.blacklistSize());
            assertTrue(auth.validateAccessToken(token));
        }
    }
}
