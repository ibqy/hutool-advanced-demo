package com.xb.hutool;

import com.xb.hutool.cache.CacheAdvancedDemo;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("缓存高级策略测试")
class CacheAdvancedDemoTest {

    @Nested
    @DisplayName("缓存穿透防护")
    class NullProtection {

        @Test
        @DisplayName("查询不存在的 key，第一次回源、第二次命中空值缓存")
        void nullValueCached() {
            CacheAdvancedDemo demo = new CacheAdvancedDemo(60000);
            String r1 = demo.getWithNullProtection("missing", k -> null);
            assertNull(r1);
            assertEquals(1, demo.getDbHitCount());

            String r2 = demo.getWithNullProtection("missing", k -> null);
            assertNull(r2);
            assertEquals(1, demo.getDbHitCount());
        }

        @Test
        @DisplayName("存在的 key 正常缓存")
        void existingValueCached() {
            CacheAdvancedDemo demo = new CacheAdvancedDemo(60000);
            String r1 = demo.getWithNullProtection("key1", k -> "value1");
            assertEquals("value1", r1);
            assertEquals(1, demo.getDbHitCount());

            String r2 = demo.getWithNullProtection("key1", k -> "should-not-load");
            assertEquals("value1", r2);
            assertEquals(1, demo.getDbHitCount());
        }
    }

    @Nested
    @DisplayName("缓存击穿防护（互斥锁）")
    class MutexProtection {

        @Test
        @DisplayName("互斥锁模式：第一次回源，后续命中缓存")
        void mutexCachesAfterFirstLoad() {
            CacheAdvancedDemo demo = new CacheAdvancedDemo(60000);
            String r1 = demo.getWithMutex("hot-key", k -> "hot-value");
            assertEquals("hot-value", r1);
            assertEquals(1, demo.getDbHitCount());

            String r2 = demo.getWithMutex("hot-key", k -> "should-not-load");
            assertEquals("hot-value", r2);
            assertEquals(1, demo.getDbHitCount());
        }
    }

    @Nested
    @DisplayName("缓存雪崩防护（TTL 抖动）")
    class JitterProtection {

        @Test
        @DisplayName("带抖动的写入不会同时过期")
        void jitteredWriteDoesNotExpireSimultaneously() {
            CacheAdvancedDemo demo = new CacheAdvancedDemo(60000);
            for (int i = 0; i < 5; i++) {
                demo.putWithJitter("k-" + i, "v-" + i, 100, 200);
            }
            assertEquals(5, demo.size());

            ThreadUtil.sleep(350);
            for (int i = 0; i < 5; i++) {
                assertNull(demo.rawGet("k-" + i), "key-" + i + " should have expired");
            }
        }
    }
}
