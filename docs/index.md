---
layout: home

hero:
  name: Hutool 高级用法
  text: 13 大模块生产场景示例
  tagline: 不只背 API：HTTP 客户端、缓存策略、Cron 定时、JWT 鉴权、AOP 代理、Excel 操作、树结构——每个示例对应真实生产场景，零外部依赖直接跑
  actions:
    - theme: brand
      text: 开始学习 →
      link: /01-http-client
    - theme: alt
      text: GitHub 源码
      link: https://github.com/ibqy/hutool-advanced-demo

features:
  - icon: 🌐
    title: HTTP 客户端
    details: HttpUtil / HttpReader 的生产级用法与超时配置
  - icon: 💾
    title: 缓存策略
    details: FIFO / LRU / LFU / Timed 四种缓存选型场景
  - icon: ⏰
    title: Cron 定时
    details: CronUtil 表达式细节与 Spring 调度对照
  - icon: 🔑
    title: JWT 鉴权
    details: 签发、校验、过期处理完整链路
  - icon: 🎭
    title: AOP 代理
    details: ProxyUtil 动态代理，理解切面本质
  - icon: 📊
    title: Excel 操作
    details: ExcelWriter / ExcelReader 大数据量读写
  - icon: 🌳
    title: 树结构
    details: TreeBuilder 一行代码组装部门/菜单树
  - icon: ▶️
    title: 直接运行
    details: 无外部依赖，mvn exec:java 逐个 Demo 跑起来看输出
---

## 课程结构

| 章节 | 主题 | 对应代码 |
|:---|:---|:---|
| [01 · HTTP 客户端](/01-http-client) | 链式请求、超时、文件下载、请求拦截 | [`HttpUtilDemo`](../src/main/java/com/xb/hutool/http/HttpUtilDemo.java) |
| [02 · 缓存策略](/02-cache-strategy) | FIFO / LRU / LFU / TimedCache 选型 | [`CacheDemo`](../src/main/java/com/xb/hutool/cache/CacheDemo.java) / [`CacheAdvancedDemo`](../src/main/java/com/xb/hutool/cache/CacheAdvancedDemo.java) |
| [03 · 定时任务深入](/03-cron-deep-dive) | Cron 表达式、动态调度、二次封装 | [`CronDemo`](../src/main/java/com/xb/hutool/cron/CronDemo.java) |
| [04 · JWT 鉴权实战](/04-jwt-guide) | 签发、验签、RS256、刷新令牌 | [`JwtDemo`](../src/main/java/com/xb/hutool/auth/JwtDemo.java) / [`JwtRefreshDemo`](../src/main/java/com/xb/hutool/auth/JwtRefreshDemo.java) |
| [05 · AOP 代理与切面](/05-aop-proxy) | JDK 动态代理、切面生命周期 | [`AopDemo`](../src/main/java/com/xb/hutool/aop/AopDemo.java) |
| [06 · Excel 操作](/06-excel-ops) | 别名映射、合并单元格、流式导出 | [`ExcelDemo`](../src/main/java/com/xb/hutool/excel/ExcelDemo.java) |
| [07 · 树结构工具](/07-tree-builder) | 平铺列表转树、递归遍历、深度限制 | [`TreeDemo`](../src/main/java/com/xb/hutool/tree/TreeDemo.java) |

## 代码地图

| 包 | 关键类 | 职责 |
|:---|:---|:---|
| 根包 | [`HutoolAdvancedDemo`](../src/main/java/com/xb/hutool/HutoolAdvancedDemo.java) | 启动入口，串起各模块的调用顺序 |
| `http` | [`HttpUtilDemo`](../src/main/java/com/xb/hutool/http/HttpUtilDemo.java) | HttpRequest 链式调用、超时、下载 |
| `cache` | [`CacheDemo`](../src/main/java/com/xb/hutool/cache/CacheDemo.java) / [`CacheAdvancedDemo`](../src/main/java/com/xb/hutool/cache/CacheAdvancedDemo.java) | 四种缓存策略与监听器 |
| `cron` | [`CronDemo`](../src/main/java/com/xb/hutool/cron/CronDemo.java) | CronUtil 注册、启动、动态调度 |
| `auth` | [`JwtDemo`](../src/main/java/com/xb/hutool/auth/JwtDemo.java) / [`JwtRefreshDemo`](../src/main/java/com/xb/hutool/auth/JwtRefreshDemo.java) | HS256/RS256 签发验签、双 Token 续期 |
| `aop` | [`AopDemo`](../src/main/java/com/xb/hutool/aop/AopDemo.java) | ProxyUtil + SimpleAspect 织入切面 |
| `excel` | [`ExcelDemo`](../src/main/java/com/xb/hutool/excel/ExcelDemo.java) | ExcelWriter / ExcelReader / BigExcelWriter |
| `tree` | [`TreeDemo`](../src/main/java/com/xb/hutool/tree/TreeDemo.java) | TreeUtil.build 与 TreeNodeConfig |
| `crypto` | [`CryptoDemo`](../src/main/java/com/xb/hutool/crypto/CryptoDemo.java) | 对称/非对称加密、摘要 |
| `config` | [`SettingDemo`](../src/main/java/com/xb/hutool/config/SettingDemo.java) | Setting 配置文件读取 |
| `util` | [`MoreUtilDemo`](../src/main/java/com/xb/hutool/util/MoreUtilDemo.java) | 集合、日期、字符串等高频工具 |
| `validation` | [`ValidatorDemo`](../src/main/java/com/xb/hutool/validation/ValidatorDemo.java) | Validator 链式校验与正则 |

## 在路线中的位置

> **Java × AI 进阶路线** · 阶段 0「工程地基」第 1 站  
> **Hutool 高级用法** → 下一站：[MyBatis-Plus 实战](https://ibqy.github.io/mybatis-plus-demo/)  
> 完整路线：[ibqy.github.io/curriculum](https://ibqy.github.io/curriculum)

## 运行方式

```bash
mvn exec:java -Dexec.mainClass=com.xb.hutool.HutoolAdvancedDemo
# 单独跑某个 Demo：-Dexec.mainClass=com.xb.hutool.tree.TreeDemo
```

> 零外部依赖，不需要数据库和中间件。


