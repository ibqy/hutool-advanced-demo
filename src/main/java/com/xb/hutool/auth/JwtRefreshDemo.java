package com.xb.hutool.auth;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JWT 刷新令牌机制 —— 双 Token 实战
 *
 * <p>作者：xb | 日期：2026-09-17</p>
 *
 * <p><b>高阶知识点</b>：
 * <ul>
 *     <li>Access Token（短命）：用于 API 鉴权，5 分钟过期</li>
 *     <li>Refresh Token（长命）：用于换取新 Access Token，7 天过期</li>
 *     <li>Token 黑名单：用内存缓存模拟 Redis 黑名单，实现主动失效</li>
 *     <li>Token 轮换：每次刷新都签发新的 Refresh Token，旧 token 加入黑名单</li>
 * </ul>
 *
 * <p><b>生产场景</b>：黑名单应使用 Redis SET，支持分布式共享和 TTL 自动过期。
 * 此处用 ConcurrentHashMap 教学，原理一致。</p>
 *
 * @author ibqy
 */
public class JwtRefreshDemo {

    // WARNING: 教学演示用，生产环境密钥必须从环境变量或配置中心读取，绝不能硬编码！
    private static final String ACCESS_KEY = "access-secret-2026";
    private static final String REFRESH_KEY = "refresh-secret-2026";

    private static final long ACCESS_TTL_MINUTES = 5;
    private static final long REFRESH_TTL_DAYS = 7;

    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    /**
     * 创建 Access Token（短命，用于 API 鉴权）
     *
     * @param uid  用户 ID
     * @param role 用户角色
     * @return 签名后的 JWT 字符串
     */
    public String createAccessToken(int uid, String role) {
        return JWT.create()
                .setPayload("uid", uid)
                .setPayload("role", role)
                .setPayload("type", "access")
                .setIssuedAt(new Date())
                .setExpiresAt(offsetMinutes(ACCESS_TTL_MINUTES))
                .setKey(ACCESS_KEY.getBytes())
                .sign();
    }

    /**
     * 创建 Refresh Token（长命，用于换取新 Access Token）
     *
     * @param uid 用户 ID
     * @return 签名后的 JWT 字符串
     */
    public String createRefreshToken(int uid) {
        return JWT.create()
                .setPayload("uid", uid)
                .setPayload("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiresAt(offsetDays(REFRESH_TTL_DAYS))
                .setKey(REFRESH_KEY.getBytes())
                .sign();
    }

    /**
     * 验证 Access Token：先查黑名单，再校验日期和签名
     *
     * @param token 待验证的 JWT 字符串
     * @return true 表示有效，false 表示无效或已过期
     */
    public boolean validateAccessToken(String token) {
        if (blacklist.containsKey(token)) {
            return false;
        }
        try {
            JWTValidator.of(token).validateDate();
            return JWTUtil.verify(token, ACCESS_KEY.getBytes());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 用 Refresh Token 换取新的 Access Token
     *
     * 采用 Token 轮换策略：刷新后旧 Refresh Token 加入黑名单，防止重放攻击。
     *
     * @param refreshToken 有效的 Refresh Token
     * @return 新签发的 Access Token
     * @throws SecurityException 当 Token 无效、过期或已在黑名单中时抛出
     */
    public String refreshAccessToken(String refreshToken) {
        if (blacklist.containsKey(refreshToken)) {
            throw new SecurityException("Refresh Token 已失效");
        }
        if (!JWTUtil.verify(refreshToken, REFRESH_KEY.getBytes())) {
            throw new SecurityException("Refresh Token 验签失败");
        }
        try {
            JWTValidator.of(refreshToken).validateDate();
        } catch (Exception e) {
            throw new SecurityException("Refresh Token 已过期");
        }

        JWT parsed = JWTUtil.parseToken(refreshToken);
        int uid = ((Number) parsed.getPayload("uid")).intValue();

        // Token 轮换：旧 Refresh Token 用过后立即失效，防止被重放
        blacklist.put(refreshToken, System.currentTimeMillis());

        return createAccessToken(uid, "user");
    }

    /**
     * 同时刷新 Access Token 和 Refresh Token（双 Token 轮换）
     *
     * @param oldRefreshToken 旧的 Refresh Token
     * @return 包含新 accessToken 和新 refreshToken 的 Map
     */
    public Map<String, String> refreshTokenPair(String oldRefreshToken) {
        String newAccessToken = refreshAccessToken(oldRefreshToken);
        JWT parsed = JWTUtil.parseToken(newAccessToken);
        int uid = ((Number) parsed.getPayload("uid")).intValue();
        String newRefreshToken = createRefreshToken(uid);
        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.containsKey(token);
    }

    public int blacklistSize() {
        return blacklist.size();
    }

    private static Date offsetMinutes(long minutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, (int) minutes);
        return cal.getTime();
    }

    private static Date offsetDays(long days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, (int) days);
        return cal.getTime();
    }

    /**
     * JWT 刷新令牌演示入口：展示双 Token 的创建、验证、刷新和轮换机制
     */
    public static void demo() {
        System.out.println("═══ JWT 刷新令牌 ═══");

        JwtRefreshDemo auth = new JwtRefreshDemo();

        String accessToken = auth.createAccessToken(1001, "admin");
        String refreshToken = auth.createRefreshToken(1001);
        System.out.println("  Access Token → " + accessToken.substring(0, 40) + "…");
        System.out.println("  Refresh Token → " + refreshToken.substring(0, 40) + "…");

        System.out.println("  验证 Access → " + (auth.validateAccessToken(accessToken) ? "通过" : "失败"));

        Map<String, String> newPair = auth.refreshTokenPair(refreshToken);
        System.out.println("  刷新后新 Access → " + newPair.get("accessToken").substring(0, 40) + "…");
        System.out.println("  旧 Refresh 已加入黑名单 → " + auth.isBlacklisted(refreshToken));

        try {
            auth.refreshAccessToken(refreshToken);
            System.out.println("  重用旧 Refresh → 未拦截（异常）");
        } catch (SecurityException e) {
            System.out.println("  重用旧 Refresh → 已拦截：" + e.getMessage());
        }

        System.out.println();
    }
}
