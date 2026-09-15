# 定时任务深入

> 对应 Demo：`cron/CronDemo.java`

## 概述

Hutool 的 `CronUtil` 是对 cron 表达式的轻量实现，支持**秒级精度**的定时任务调度。它是全局单例调度器，适合中小型项目的后台任务管理。

## Cron 表达式

标准格式（6 位或 7 位）：

```
秒 分 时 日 月 星期 [年]
```

| 位置 | 取值范围 | 特殊字符 |
|------|---------|---------|
| 秒 | 0-59 | `, - * /` |
| 分 | 0-59 | `, - * /` |
| 时 | 0-23 | `, - * /` |
| 日 | 1-31 | `, - * ? / L W` |
| 月 | 1-12 | `, - * /` |
| 星期 | 1-7 (1=周日) | `, - * ? / L #` |
| 年（可选） | 1970-2099 | `, - * /` |

### 常用表达式

| 表达式 | 含义 |
|--------|------|
| `* * * * * ?` | 每秒执行 |
| `0 * * * * ?` | 每分钟执行 |
| `0 0 * * * ?` | 每小时执行 |
| `0 0 8 * * ?` | 每天 8:00 |
| `0 0 8 1 * ?` | 每月 1 号 8:00 |
| `0 30 9 * * 1-5` | 工作日 9:30 |
| `*/5 * * * * ?` | 每 5 秒执行 |
| `0 0/30 * * * ?` | 每 30 分钟执行 |

## 代码示例

### 基本用法

```java
// 注册任务
CronUtil.schedule("job1", "* * * * * ?", (Task) () ->
    System.out.println("每秒执行"));

CronUtil.schedule("dailyReport", "0 0 8 * * ?", (Task) () ->
    System.out.println("每天 8:00 执行报表"));

// 开启秒匹配（默认关闭，只支持到分钟）
CronUtil.setMatchSecond(true);

// 启动调度器
CronUtil.start();

// 停止
CronUtil.stop();
```

### 动态调度 — CronPattern

生产场景中常常需要动态调整 cron 表达式，`CronPattern` 提供了运行时解析能力：

```java
// 启动时从配置读取
String configExpr = "0 */5 * * * ?"; // 可来自数据库/配置中心

CronPattern pattern = new CronPattern(configExpr);
// 验证表达式合法性
boolean valid = pattern.match(
    DateUtil.parse("2024-12-01 08:00:00").toLocalDateTime(),
    TimeZone.getDefault(),
    true
);

// 动态重新调度
CronUtil.remove("myJob");
CronUtil.schedule("myJob", newConfigExpr, task);
```

### CronUtil 二次封装

实际项目中建议对 CronUtil 做一层封装，统一管理任务的启停和监控：

```java
public class SchedulerManager {
    
    /** 注册任务（如果存在则覆盖） */
    public static void register(String id, String cronExpr, Runnable task) {
        CronUtil.remove(id);
        CronUtil.schedule(id, cronExpr, (Task) task::run);
        log.info("任务注册: id={}, cron={}", id, cronExpr);
    }

    /** 获取所有已注册任务 */
    public static List<String> listJobs() {
        return CronUtil.getScheduler().getTaskTable().keys()
            .stream().map(Object::toString).toList();
    }

    /** 立即执行一次 */
    public static void triggerOnce(String id) {
        CronUtil.getScheduler().getTask(id).execute();
    }
}

// 使用
SchedulerManager.register("syncData", "0 */10 * * * ?", this::syncData);
```

## 生产注意事项

1. **全局单例**：`CronUtil` 是 JVM 级别的单例，多模块共用同一调度器
2. **线程池小**：默认线程池大小为 10，密集任务需调整 `CronUtil.setScheduler()`
3. **异常处理**：任务内抛异常不会影响其他任务，但会被静默吞掉 — 务必在 Task 内自行 catch
4. **分布式**：CronUtil 是进程内的，多实例会重复执行，需加分布式锁
5. **秒级精度**：默认 `setMatchSecond(false)`，如需秒级任务必须设为 `true`
