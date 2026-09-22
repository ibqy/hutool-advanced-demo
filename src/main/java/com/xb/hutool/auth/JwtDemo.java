package com.xb.hutool.auth;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import java.util.*;

/**
 * JWT 高级用法
 * - 生成/解析 Token
 * - 自定义 Payload
 * - HS256 / RS256 签名
 * - 过期验证
 */
public class JwtDemo {
    // WARNING: 教学演示用，生产环境密钥必须从环境变量或配置中心读取，绝不能硬编码！
    static final String KEY = "ibqy-secret-key-2024";

    public static void demo() {
        System.out.println("═══ JWT ═══");

        // 1. 生成 Token
        String token = JWT.create()
            .setPayload("uid", 1001)
            .setPayload("role", "admin")
            .setPayload("name", "ibqy")
            .setIssuedAt(new Date())
            .setExpiresAt(DateUtil.offsetHour(new Date(), 1))
            .setKey(KEY.getBytes())
            .sign();
        System.out.println("  Token → " + token.substring(0, 50) + "…");

        // 2. 解析
        JWT jwt = JWTUtil.parseToken(token);
        System.out.println("  uid → " + jwt.getPayload("uid"));
        System.out.println("  role → " + jwt.getPayload("role"));

        // 3. 验证
        boolean valid = JWTUtil.verify(token, KEY.getBytes());
        System.out.println("  验签 → " + (valid ? "✅ 通过" : "❌ 失败"));

        // 4. 用 HS256 签名器显式签名
        String token2 = JWT.create()
            .setPayload("sub", "hutool-demo")
            .setSigner(JWTSignerUtil.hs256(KEY.getBytes()))
            .sign();
        System.out.println("  HS256 Token → " + token2.substring(0, 40) + "…");

        // 5. 过期验证
        boolean expired = false;
        try {
            JWTValidator.of(token).validateDate();
        } catch (Exception e) {
            expired = true;
        }
        System.out.println("  是否过期 → " + (expired ? "❌ 是" : "✅ 否"));

        System.out.println();
    }

    static class DateUtil {
        static Date offsetHour(Date d, int h) {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.HOUR, h);
            return c.getTime();
        }
    }
}