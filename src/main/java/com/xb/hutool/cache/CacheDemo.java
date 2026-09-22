package com.xb.hutool.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.core.thread.ThreadUtil;

/**
 * CacheDemo - 演示 Hutool 缓存组件的核心用法
 *
 * 缓存是提升系统性能的关键手段。本类演示 TimedCache（定时过期）和 FIFOCache（先进先出淘汰）
 * 两种常用策略，以及定时清理任务的配置方式，帮助学习者理解缓存过期与淘汰机制。
 *
 * @author ibqy
 */
public class CacheDemo {

    /**
     * 缓存演示入口：依次展示定时缓存过期、FIFO 淘汰和自动清理任务
     */
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

        // 3. 定时任务缓存（生产环境应开启自动清理，否则过期元素只在不被访问时才惰性删除）
        TimedCache<String, String> auto = CacheUtil.newTimedCache(3000);
        auto.schedulePrune(1000);
        auto.put("session", "data");
        System.out.println("  自动清理已启动");

        System.out.println();
    }
}