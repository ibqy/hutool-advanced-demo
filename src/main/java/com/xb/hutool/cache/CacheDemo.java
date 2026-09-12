package com.xb.hutool.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.core.thread.ThreadUtil;

/**
 * Cache 高级用法
 * - 定时缓存（过期自动移除）
 * - FIFO/LRU/LFU 缓存
 * - 缓存监听器
 */
public class CacheDemo {
    public static void demo() {
        System.out.println("═══ Cache ═══");

        // 1. 定时缓存（2秒过期）
        TimedCache<String, String> timed = CacheUtil.newTimedCache(2000);
        timed.put("token", "abc123");
        System.out.println("  写入 → token = " + timed.get("token"));
        ThreadUtil.sleep(2500);
        System.out.println("  2.5s 后 → " + (timed.get("token") == null ? "已过期" : timed.get("token")));

        // 2. FIFO 缓存（容量3）
        FIFOCache<String, Integer> fifo = CacheUtil.newFIFOCache(3);
        fifo.put("a", 1); fifo.put("b", 2); fifo.put("c", 3);
        System.out.println("  FIFO 满 → " + fifo);
        fifo.put("d", 4);
        System.out.println("  加 d 后 → " + fifo + " (a 被淘汰)");

        // 3. 定时任务缓存
        TimedCache<String, String> auto = CacheUtil.newTimedCache(3000);
        auto.schedulePrune(1000);
        auto.put("session", "data");
        System.out.println("  自动清理已启动");

        System.out.println();
    }
}