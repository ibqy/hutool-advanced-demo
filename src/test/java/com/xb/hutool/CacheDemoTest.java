package com.xb.hutool;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CacheDemoTest - 缓存组件单元测试
 *
 * 验证 TimedCache 过期机制、FIFOCache 淘汰策略和手动清理行为。
 *
 * @author ibqy
 */
@DisplayName("Hutool Cache 缓存测试")
class CacheDemoTest {

    @Test
    @DisplayName("TimedCache - 过期后返回 null")
    void timedCache_expiresAfterTtl() {
        TimedCache<String, String> cache = CacheUtil.newTimedCache(100);
        cache.put("key", "value");

        assertEquals("value", cache.get("key"));

        ThreadUtil.sleep(150);
        assertNull(cache.get("key"));
    }

    @Test
    @DisplayName("FIFOCache - 超过容量淘汰最早放入的")
    void fifoCache_evictsOldest() {
        FIFOCache<String, Integer> cache = CacheUtil.newFIFOCache(3);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);

        assertEquals(3, cache.size());

        cache.put("d", 4);
        assertEquals(3, cache.size());
        assertNull(cache.get("a"));
        assertEquals(4, cache.get("d"));
    }

    @Test
    @DisplayName("TimedCache - 手动清理")
    void timedCache_manualRemove() {
        TimedCache<String, String> cache = CacheUtil.newTimedCache(60000);
        cache.put("key", "value");
        assertEquals("value", cache.get("key"));

        cache.remove("key");
        assertNull(cache.get("key"));
    }

    @Test
    @DisplayName("FIFOCache - 更新已有 key 不增加容量")
    void fifoCache_updateExisting() {
        FIFOCache<String, Integer> cache = CacheUtil.newFIFOCache(2);
        cache.put("a", 1);
        cache.put("a", 2);

        assertEquals(1, cache.size());
        assertEquals(2, cache.get("a"));
    }
}
