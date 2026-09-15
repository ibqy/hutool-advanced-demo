<p align="center">
  <strong style="font-size: 28px;">📚 Hutool Advanced Demo</strong><br>
  <span style="color: #656D76;">Hutool 工具库高级用法示例集 · 覆盖 10+ 生产场景</span>
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
| ⏰ 定时 | `cron/CronDemo.java` | [定时任务深入](docs/03-cron-deep-dive.md) |
| 🔐 JWT | `auth/JwtDemo.java` | [JWT 鉴权实战](docs/04-jwt-guide.md) |
| 🎯 AOP | `aop/AopDemo.java` | [AOP 代理与切面](docs/05-aop-proxy.md) |
| 📊 Excel | `excel/ExcelDemo.java` | [Excel 操作指南](docs/06-excel-ops.md) |
| 🌿 树结构 | `tree/TreeDemo.java` | [树结构工具](docs/07-tree-builder.md) |
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
