# 缓存策略

> 对应 Demo：`cache/CacheDemo.java`

## 概述

Hutool 提供了四种轻量级内存缓存实现：FIFO、LFU、LRU 和 TimedCache。无需引入外部缓存中间件，适合单机场景下的接口响应缓存、Session 管理、令牌存储。

## 四种策略对比

| 策略 | 淘汰规则 | 适用场景 | 线程安全 |
|------|---------|---------|---------|
| **FIFO** | 先进先出，按写入顺序淘汰 | 日志缓冲、消息队列 | ✅ |
| **LFU** | 淘汰使用频率最低的 | 热点数据缓存 | ✅ |
| **LRU** | 淘汰最久未使用的 | Session、Token 存储 | ✅ |
| **TimedCache** | 按过期时间淘汰 | 验证码、临时授权 | ✅ |

## 代码示例

### TimedCache — 定时过期

最常用的缓存类型，适合验证码、Token 等有时效性的数据。

```java
// 创建 2 秒过期的缓存
TimedCache<String, String> cache = CacheUtil.newTimedCache(2000);
cache.put("token", "abc123");

// 2.5 秒后缓存自动过期
Thread.sleep(2500);
cache.get("token"); // → null

// 开启自动清理线程（每秒检查一次过期数据）
TimedCache<String, String> auto = CacheUtil.newTimedCache(3000);
auto.schedulePrune(1000);
```

### FIFO — 先进先出

```java
// 容量 3，超出淘汰最早放入的
FIFOCache<String, Integer> fifo = CacheUtil.newFIFOCache(3);
fifo.put("a", 1);
fifo.put("b", 2);
fifo.put("c", 3);
fifo.put("d", 4); // 触发淘汰，移除 "a"
```

### LRU — 最近最少使用

```java
// 容量 3，按访问频率淘汰
LRUCache<String, Integer> lru = CacheUtil.newLRUCache(3);
lru.put("a", 1);
lru.put("b", 2);
lru.put("c", 3);
lru.get("a");      // 刷新 "a" 的访问时间
lru.put("d", 4);   // 淘汰 "b"（最久未访问）
```

### LFU — 最少频率使用

```java
// 容量 3，淘汰访问次数最少的
LFUCache<String, Integer> lfu = CacheUtil.newLFUCache(3);
lfu.put("a", 1);
lfu.put("b", 2);
lfu.put("c", 3);
lfu.get("a"); lfu.get("a"); // "a" 访问 2 次
lfu.get("b");               // "b" 访问 1 次
lfu.put("d", 4);            // 淘汰 "c"（访问 0 次）
```

## 核心 API

```java
// 创建
CacheUtil.newTimedCache(long timeout);    // 毫秒级过期
CacheUtil.newFIFOCache(int capacity);
CacheUtil.newLFUCache(int capacity);
CacheUtil.newLRUCache(int capacity);

// 操作
cache.put(key, value);
cache.get(key);
cache.remove(key);
cache.clear();
cache.size();
cache.containsKey(key);
cache.isFull();

// 监听器
cache.setListener((key, cachedObject) -> {
    System.out.println("缓存被移除: " + key);
});
```

## 生产建议

1. **分布式场景**：这些是进程内缓存，多实例不共享，需搭配 Redis
2. **TimedCache 必开 schedulePrune**：否则过期 key 只在访问时才清理，可能导致内存泄漏
3. **缓存穿透**：Hutool 缓存不处理 null 值防护，需业务层自行判断
4. **选择策略**：90% 场景用 TimedCache，有容量限制时用 LRU
