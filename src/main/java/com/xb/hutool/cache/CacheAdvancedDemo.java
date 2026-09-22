package com.xb.hutool.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.RandomUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * 缓存高级策略 —— 击穿/穿透/雪崩防护
 *
 * <p>作者：xb | 日期：2026-09-17</p>
 *
 * <p><b>高阶知识点</b>：
 * <ul>
 *     <li>缓存穿透（Penetration）：查询不存在的数据，每次都打到数据源。
 *         解法：缓存空值，设置短 TTL</li>
 *     <li>缓存击穿（Breakdown）：热点 key 过期瞬间大量请求穿透。
 *         解法：互斥锁（synchronized / ReentrantLock），只允许一个线程回源</li>
 *     <li>缓存雪崩（Avalanche）：大批 key 同时过期。
 *         解法：TTL 加随机抖动，避免同时失效</li>
 * </ul>
 *
 * @author ibqy
 */
public class CacheAdvancedDemo {

    private final TimedCache<String, String> cache;
    private final AtomicLong dbHitCount = new AtomicLong();

    /**
     * @param ttlMs 缓存过期时间（毫秒）
     */
    public CacheAdvancedDemo(long ttlMs) {
        this.cache = CacheUtil.newTimedCache(ttlMs);
    }

    /**
     * 带空值保护的缓存查询 —— 防止缓存穿透
     *
     * 当数据源返回 null 时，缓存一个空字符串（短 TTL），避免重复查询数据源。
     *
     * @param key    缓存 key
     * @param loader 数据源加载函数，key 作为入参
     * @return 缓存值或数据源返回值，数据源返回 null 则返回 null
     */
    public String getWithNullProtection(String key, Function<String, String> loader) {
        String cached = cache.get(key);
        if (cached != null) {
            return cached.isEmpty() ? null : cached;
        }

        String value = loader.apply(key);
        dbHitCount.incrementAndGet();

        if (value == null) {
            // 缓存空字符串 5 秒，用空串区分"真的没数据"和"未缓存"
            cache.put(key, "", 5000);
        } else {
            cache.put(key, value);
        }
        return value;
    }

    /**
     * 互斥锁缓存查询 —— 防止缓存击穿
     *
     * 使用 synchronized 保证同一时刻只有一个线程回源查询，其余线程等待缓存结果。
     * 适合热点 key 过期瞬间大量请求并发的场景。
     *
     * @param key    缓存 key
     * @param loader 数据源加载函数
     * @return 缓存值
     */
    public synchronized String getWithMutex(String key, Function<String, String> loader) {
        String cached = cache.get(key);
        if (cached != null) {
            return cached;
        }

        String value = loader.apply(key);
        dbHitCount.incrementAndGet();
        cache.put(key, value);
        return value;
    }

    /**
     * 带随机抖动的缓存写入 —— 防止缓存雪崩
     *
     * 实际 TTL = baseTtlMs + random(0, jitterMs)，避免大批 key 同时过期。
     *
     * @param key       缓存 key
     * @param value     缓存值
     * @param baseTtlMs 基础过期时间（毫秒）
     * @param jitterMs  随机抖动上限（毫秒）
     */
    public void putWithJitter(String key, String value, long baseTtlMs, long jitterMs) {
        long ttl = baseTtlMs + RandomUtil.randomLong(0, jitterMs);
        cache.put(key, value, ttl);
    }

    public long getDbHitCount() {
        return dbHitCount.get();
    }

    public String rawGet(String key) {
        return cache.get(key);
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public void put(String key, String value, long ttlMs) {
        cache.put(key, value, ttlMs);
    }

    public int size() {
        return cache.size();
    }

    /**
     * 缓存高级策略演示入口：展示穿透防护和雪崩防护
     */
    public static void demo() {
        System.out.println("═══ 缓存高级策略 ═══");

        CacheAdvancedDemo demo = new CacheAdvancedDemo(10000);

        System.out.println("  --- 缓存穿透防护 ---");
        String result1 = demo.getWithNullProtection("nonexistent", k -> null);
        System.out.println("  第一次查不存在的 key → " + result1 + "，DB 命中 " + demo.getDbHitCount());
        String result2 = demo.getWithNullProtection("nonexistent", k -> null);
        System.out.println("  第二次查不存在的 key → " + result2 + "，DB 命中 " + demo.getDbHitCount() + "（空值缓存命中）");

        System.out.println("  --- 缓存雪崩防护 ---");
        CacheAdvancedDemo jitterDemo = new CacheAdvancedDemo(60000);
        for (int i = 0; i < 5; i++) {
            jitterDemo.putWithJitter("key-" + i, "value-" + i, 5000, 3000);
        }
        System.out.println("  5 个 key 写入，TTL = 5000 + random(0,3000)ms，避免同时过期");

        System.out.println();
    }
}
