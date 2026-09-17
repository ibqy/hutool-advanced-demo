<p align="center">
  <strong style="font-size: 28px;">📚 Hutool Advanced Demo</strong><br>
  <span style="color: #656D76;">Hutool 工具库高级用法示例集 · 覆盖 14+ 生产场景</span>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-2266EE?style=flat-square" alt="Java 21">
  <img src="https://img.shields.io/badge/Hutool-5.8.34-2266EE?style=flat-square" alt="Hutool 5.8.34">
  <img src="https://img.shields.io/github/license/ibqy/hutool-advanced-demo?style=flat-square" alt="License">
  <img src="https://img.shields.io/github/stars/ibqy/hutool-advanced-demo?style=flat-square" alt="Stars">
</p>

<br>

---

## 简介

[Hutool](https://hutool.cn) 是 Java 生态中最受欢迎的国产工具库之一。本项目通过**真实可运行的代码示例**，展示 Hutool 在项目开发中的高阶用法。

**项目结构**：每个 Demo 独立一个类，main 入口 `HutoolAdvancedDemo.java` 按顺序调用全部示例。

<br>

---

## 快速开始

```bash
git clone https://github.com/ibqy/hutool-advanced-demo.git
cd hutool-advanced-demo

# 编译
mvn compile

# 运行全部 Demo
mvn exec:java -Dexec.mainClass="com.xb.hutool.HutoolAdvancedDemo"
```

> 或直接在 IDE 中运行 `HutoolAdvancedDemo.java` 的 main 方法。

<br>

---

## 模块总览

| 模块 | 源文件 | 文档 |
|------|--------|------|
| 🌐 HTTP | `http/HttpUtilDemo.java` | [HTTP 客户端实战](docs/01-http-client.md) |
| 💾 缓存 | `cache/CacheDemo.java` | [缓存策略对比](docs/02-cache-strategy.md) |
| 🛡️ 缓存防护 | `cache/CacheAdvancedDemo.java` | 穿透/击穿/雪崩三重防护 |
| ⏰ 定时 | `cron/CronDemo.java` | [定时任务深入](docs/03-cron-deep-dive.md) |
| 🔐 JWT | `auth/JwtDemo.java` | [JWT 鉴权实战](docs/04-jwt-guide.md) |
| 🔄 JWT 刷新 | `auth/JwtRefreshDemo.java` | 双 Token + 黑名单 + 轮换机制 |
| 🔑 加解密 | `crypto/CryptoDemo.java` | RSA 非对称加密 + 数字签名 |
| 🎯 AOP | `aop/AopDemo.java` | [AOP 代理与切面](docs/05-aop-proxy.md) |
| 📊 Excel | `excel/ExcelDemo.java` | [Excel 操作指南](docs/06-excel-ops.md) |
| 🌿 树结构 | `tree/TreeDemo.java` | [树结构工具](docs/07-tree-builder.md) |
| ✅ 数据校验 | `validation/ValidatorDemo.java` | 链式校验器 + 正则规则 |
| ⚙️ 配置 | `config/SettingDemo.java` | 分组配置、自动加载、持久化 |
| 🔧 更多 | `util/MoreUtilDemo.java` | 加密、脱敏、ID 生成、类型转换 |

<br>

---

## 依赖

```xml
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.8.34</version>
</dependency>
```

<br>

---

## 实现边界

### ✅ 已实现

| 功能 | 说明 |
|------|------|
| HTTP 客户端 | GET/POST、文件上传、代理配置 |
| 缓存策略 | TimedCache 过期、FIFO/LRU 淘汰 |
| 缓存防护 | 穿透（空值缓存）、击穿（互斥锁）、雪崩（TTL 抖动） |
| 定时任务 | CronUtil 动态添加/移除任务 |
| JWT 鉴权 | 生成/解析/验签、HS256 签名、过期验证 |
| JWT 刷新机制 | 双 Token（access + refresh）、黑名单、Token 轮换 |
| RSA 加解密 | 非对称加密（公钥加密/私钥解密）、数字签名与验签 |
| AOP 代理 | JDK 动态代理、CGLIB 代理 |
| Excel 操作 | 写入（带样式）、读取、大数据流式处理 |
| 树结构 | TreeUtil 构建、节点查找、排序 |
| 数据校验 | 链式校验器、手机号/邮箱/身份证/中文名正则校验 |
| 加密解密 | MD5、AES、Base64 |
| 数据脱敏 | 手机号、邮箱、密码脱敏 |
| ID 生成 | UUID、NanoId |
| 类型转换 | Convert 工具、Map 工具 |

### 🎓 教学简化

| 场景 | 简化内容 | 生产环境建议 |
|------|---------|-------------|
| 缓存持久化 | 仅内存缓存 | 集成 Redis/Caffeine |
| JWT 密钥管理 | 硬编码密钥 | 使用 KMS 或配置文件 |
| JWT 黑名单 | ConcurrentHashMap | 集成 Redis SET + TTL |
| RSA 密钥管理 | 运行时生成 | 持久化到密钥管理系统 |
| 定时任务 | 单机 Cron | 分布式任务调度（XXL-Job） |
| Excel | 小文件处理 | 大数据量用 SXSSFWorkbook |
| 异常处理 | 简化输出 | 统一异常处理 + 日志记录 |

### ❌ 未实现

- HTTP 连接池配置
- Excel 模板导出
- 分布式锁/限流
- 响应式/异步缓存加载

<br>

---

## 测试覆盖

| 测试类 | 测试数 | 覆盖内容 |
|--------|--------|---------|
| `CacheDemoTest` | 4 | TimedCache 过期、FIFO 淘汰、手动清理、更新 |
| `CacheAdvancedDemoTest` | 4 | 穿透防护、击穿防护、雪崩防护（TTL 抖动） |
| `JwtDemoTest` | 6 | 生成解析、验签、HS256、过期验证 |
| `JwtRefreshDemoTest` | 7 | 双 Token 生成、刷新、轮换、黑名单 |
| `CryptoDemoTest` | 8 | RSA 密钥对生成、加解密、数字签名与验签 |
| `ValidatorDemoTest` | 7 | 手机号/邮箱/身份证/中文名校验、链式校验器 |
| `TreeDemoTest` | 5 | 构建、查找、多层级、空树、排序 |
| `UtilDemoTest` | 12 | UUID、MD5、AES、脱敏、转换、Base64 |
| **合计** | **53** | **全部通过** |

```bash
# 运行测试
mvn test
```

<br>

---

## 相关链接

- [Hutool 官方文档](https://hutool.cn/docs)
- [Hutool GitHub](https://github.com/dromara/hutool)
- [回到我的主页](https://github.com/ibqy)
- [查看作品集](https://ibqy.github.io)

<br>

---

<p align="center" style="color: #8B949E; font-size: 13px;">
  Built with ❤️ using <a href="https://hutool.cn">Hutool</a>
</p>
