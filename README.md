<p align="center">
  <strong style="font-size: 28px;">📚 Hutool Advanced Demo</strong><br>
  <span style="color: #656D76;">Hutool 工具库高级用法示例集 · 覆盖 10+ 生产场景</span>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-2266EE?style=flat-square" alt="Java 17">
  <img src="https://img.shields.io/badge/Hutool-5.8.34-2266EE?style=flat-square" alt="Hutool 5.8.34">
  <img src="https://img.shields.io/github/license/ibqy/hutool-advanced-demo?style=flat-square" alt="License">
  <img src="https://img.shields.io/github/stars/ibqy/hutool-advanced-demo?style=flat-square" alt="Stars">
</p>

<br>

---

## 简介

[Hutool](https://hutool.cn) 是 Java 生态中最受欢迎的国产工具库之一。本项目通过**真实可运行的代码示例**，展示 Hutool 在项目开发中的高阶用法，帮助开发者快速掌握生产级实践。

**项目结构**：每个 Demo 独立一个类，main 入口 `HutoolAdvancedDemo.java` 按顺序调用全部示例。

<br>

---

## 模块总览

| 模块 | 文件 | 覆盖内容 |
|------|------|---------|
| 🌐 HTTP | `http/HttpUtilDemo.java` | 链式请求、POST JSON、文件下载、参数拼接 |
| ⚙️ 配置 | `config/SettingDemo.java` | 分组配置、自动加载、配置持久化 |
| 💾 缓存 | `cache/CacheDemo.java` | 定时过期、FIFO/LRU/LFU、自动清理 |
| ⏰ 定时 | `cron/CronDemo.java` | 秒级任务、动态调度、启动/停止 |
| 🔐 JWT | `auth/JwtDemo.java` | Token 生成、HS256 签名、过期验证 |
| 🎯 AOP | `aop/AopDemo.java` | JDK 动态代理、前置/后置/异常拦截 |
| 🌿 树结构 | `tree/TreeDemo.java` | 多级树构建、节点路径、遍历打印 |
| 📊 Excel | `excel/ExcelDemo.java` | 带样式导出、别名映射、读取解析 |
| 🔧 更多工具 | `util/MoreUtilDemo.java` | 加密解密、脱敏、ID 生成、类型转换、Base64 |

<br>

---

## 快速开始

```bash
# 克隆
git clone https://github.com/ibqy/hutool-advanced-demo.git
cd hutool-advanced-demo

# 编译（仅静态检查，无需运行）
mvn compile

# 运行全部 Demo
mvn exec:java -Dexec.mainClass="com.ibqy.hutool.HutoolAdvancedDemo"
```

> 或直接运行 `src/main/java/com/ibqy/hutool/HutoolAdvancedDemo.java` 中的 main 方法。

<br>

---

## Demo 详情

### 🌐 HTTP — HttpUtilDemo

展示 Hutool 的 HTTP 客户端高级功能：

```java
// 链式 GET
String body = HttpRequest.get("https://api.github.com/zen")
    .timeout(5000)
    .header("User-Agent", "Hutool-Demo")
    .execute()
    .body();

// POST JSON
String res = HttpRequest.post("https://httpbin.org/post")
    .body("{\"msg\":\"hello\"}")
    .contentType(ContentType.JSON.getValue())
    .execute()
    .body();

// 带参数 GET + 文件下载
HttpUtil.get("https://api.github.com/search/repositories",
    MapUtil.of("q", "hutool"));
HttpUtil.downloadFile(url, FileUtil.file("/tmp/demo.png"));
```

**生产场景**：微服务间调用、第三方 API 对接、文件资源下载。

<br>

### ⚙️ 配置 — SettingDemo

Hutool 的 `Setting` 是对 `.setting` / `.properties` 配置文件的增强封装：

```java
Setting setting = new Setting("app.setting", true); // 自动加载
String url = setting.getStr("db.url");
int poolSize = setting.getInt("pool.maxActive");

// 分组配置
setting.getStr("server.port");     // 默认组
setting.getStr("pool.maxActive");  // [pool] 组

// 持久化
setting.store("backup.setting");
```

**生产场景**：多环境配置管理、配置热更新、系统参数持久化。

<br>

### 💾 缓存 — CacheDemo

提供多种缓存策略实现：

```java
// 定时缓存（2秒过期）
TimedCache<String, String> cache = CacheUtil.newTimedCache(2000);
cache.put("token", "abc123");
Thread.sleep(2500);
cache.get("token"); // → null

// FIFO 缓存（容量3，超出淘汰最早）
FIFOCache<String, Integer> fifo = CacheUtil.newFIFOCache(3);

// 自动清理（每1秒检查过期）
TimedCache<String, String> auto = CacheUtil.newTimedCache(3000);
auto.schedulePrune(1000);
```

**生产场景**：接口响应缓存、Session 管理、令牌存储。

<br>

### ⏰ 定时 — CronDemo

Hutool 的 Cron 模块支持秒级精度：

```java
CronUtil.schedule("job1", "* * * * * ?", (Task) () ->
    System.out.println("每秒执行"));

CronUtil.setMatchSecond(true); // 开启秒匹配
CronUtil.start();
// ...
CronUtil.stop();
```

**生产场景**：数据同步、报表生成、定时清理任务。

<br>

### 🔐 JWT — JwtDemo

完整的 JWT 生成、签名、验证流程：

```java
// 生成 Token
String token = JWT.create()
    .setPayload("uid", 1001)
    .setPayload("role", "admin")
    .setIssuedAt(new Date())
    .setExpiresAt(DateUtil.offsetHour(new Date(), 1))
    .setKey(secret.getBytes())
    .sign();

// 解析
JWT jwt = JWTUtil.parseToken(token);
jwt.getPayload("uid");

// 验签
JWTUtil.verify(token, secret.getBytes());

// HS256 显式签名
JWT.create()
    .setSigner(JWTSignerUtil.hs256(secret.getBytes()))
    .sign();
```

**生产场景**：用户认证、API 鉴权、单点登录。

<br>

### 🎯 AOP — AopDemo

基于 JDK 动态代理的方法拦截：

```java
BizService proxy = ProxyUtil.proxy(new BizServiceImpl(), new SimpleAspect() {
    public boolean before(...) { /* 前置日志 */ return true; }
    public boolean after(...)  { /* 后置记录 */ return true; }
    public boolean afterException(...) { /* 异常上报 */ return true; }
});

proxy.doWork("test"); // 自动触发 before → after
```

**生产场景**：日志切面、性能监控、权限校验、事务管理。

<br>

### 🌿 树结构 — TreeDemo

将平铺列表转换为树形结构：

```java
List<Tree<String>> treeList = TreeUtil.build(nodeList, "0");

// 获取节点路径
Tree<String> node = TreeUtil.getNode(treeList, "5");
node.getParentsName(true); // → ["总公司", "技术部", "后端组"]
```

**生产场景**：组织架构树、分类目录、评论嵌套、路由菜单。

<br>

### 📊 Excel — ExcelDemo

带样式的 Excel 导出与解析：

```java
ExcelWriter writer = ExcelUtil.getWriter("output.xlsx");
writer.merge(4, "报表标题");
writer.addHeaderAlias("name", "姓名");
writer.write(dataList);
writer.setColumnWidth(0, 20);
writer.close();

ExcelReader reader = ExcelUtil.getReader("output.xlsx");
List<List<Object>> all = reader.read();
```

**生产场景**：数据导出报表、批量导入、财务对账。

<br>

### 🔧 更多工具 — MoreUtilDemo

```java
// ID 生成
IdUtil.fastSimpleUUID();
IdUtil.nanoId(12);

// 加密
SecureUtil.md5("password");
SecureUtil.aes(key.getBytes());

// 脱敏
DesensitizedUtil.mobilePhone("13812345678"); // 138****5678
DesensitizedUtil.email("test@example.com");   // t***@example.com

// 类型转换
Convert.toInt("42");
Convert.toDate("2024-12-01");

// 日期
DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
DateUtil.between(now, yesterday, DateUnit.HOUR);
```

**生产场景**：通用工具类封装、数据清洗、日志处理。

<br>

---

## 依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| hutool-all | 5.8.34 | 全部 Hutool 核心模块 |
| junit | 4.13.2 | 测试（可选） |

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