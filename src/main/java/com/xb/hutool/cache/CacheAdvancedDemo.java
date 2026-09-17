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
 */
public class CacheAdvancedDemo {

    private final TimedCache<String, String> cache;
    private final AtomicLong dbHitCount = new AtomicLong();

    public CacheAdvancedDemo(long ttlMs) {
        this.cache = CacheUtil.newTimedCache(ttlMs);
    }

    public String getWithNullProtection(String key, Function<String, String> loader) {
        String cached = cache.get(key);
        if (cached != null) {
            return cached.isEmpty() ? null : cached;
        }

        String value = loader.apply(key);
        dbHitCount.incrementAndGet();

        if (value == null) {
            cache.put(key, "", 5000);
        } else {
            cache.put(key, value);
        }
        return value;
    }

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
