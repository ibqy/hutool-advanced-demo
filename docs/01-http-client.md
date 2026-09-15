# HTTP 客户端实战

> 对应 Demo：`http/HttpUtilDemo.java`

## 概述

Hutool 对 JDK 的 `HttpURLConnection` 做了轻量级封装，提供链式调用、文件上传下载、超时控制等能力，无需引入第三方 HTTP 库即可满足大部分微服务间调用场景。

## 基础用法

### GET 请求

```java
// 链式 GET — 设置超时和自定义 Header
String body = HttpRequest.get("https://api.github.com/zen")
    .timeout(5000)
    .header("User-Agent", "Hutool-Demo")
    .execute()
    .body();

// 带查询参数的 GET
HttpUtil.get("https://api.github.com/search/repositories",
    MapUtil.of("q", "hutool"));
```

### POST 请求

```java
// POST JSON — 设置 Content-Type
String res = HttpRequest.post("https://httpbin.org/post")
    .body("{\"msg\":\"hello\"}")
    .contentType(ContentType.JSON.getValue())
    .execute()
    .body();

// POST 表单
HttpUtil.post("https://httpbin.org/post",
    MapUtil.of("username", "admin", "password", "123456"));
```

### 文件下载

```java
// 下载到本地文件
HttpUtil.downloadFile(url, FileUtil.file("/tmp/demo.png"));

// 带进度的文件下载
HttpRequest.get(url)
    .execute()
    .writeBody(FileUtil.file("/tmp/output.zip"));
```

## 进阶配置

### 超时与重试

```java
HttpRequest.get(url)
    .timeout(3000)                     // 连接超时 3 秒
    .setConnectionTimeout(5000)        // 获取连接超时
    .setReadTimeout(10000)             // 读取超时
    .execute();
```

Hutool 的 HTTP 客户端**不自带重试**机制。生产环境如需重试，建议结合 Hutool 的 `RetryUtil` 或在业务层封装：

```java
// 业务层重试示例
String result = RetryUtil.retry(
    () -> HttpUtil.get("https://api.example.com/data"),
    3,                    // 最大重试次数
    1000,                 // 重试间隔 ms
    Exception.class       // 触发重试的异常
);
```

> 注：Hutool 5.8.x 中 `RetryUtil` 位于 `cn.hutool.core.thread.RetryUtil`。

### 代理配置

```java
// 通过 ProxySelector 设置代理
HttpRequest.get(url)
    .setProxy(new Proxy(Proxy.Type.HTTP,
        new InetSocketAddress("proxy.company.com", 8080)))
    .execute();
```

### 请求拦截（Filter）

```java
// 添加全局拦截器（如统一日志、认证 Token）
HttpGlobalConfig.setDecodeUrl(false); // 关闭自动 URL 解码

HttpRequest.get(url)
    .addRequestInterceptor(req -> {
        req.header("Authorization", "Bearer " + getToken());
        req.header("X-Trace-Id", IdUtil.fastSimpleUUID());
    })
    .execute();
```

## 生产场景

| 场景 | 推荐方式 |
|------|---------|
| 微服务间调用 | `HttpRequest` 链式调用 + 超时配置 |
| 第三方 API 对接 | `HttpRequest` + 自定义 Header + JSON Body |
| 文件下载 | `HttpUtil.downloadFile()` 或 `writeBody()` |
| 大文件上传 | `HttpRequest.post().form("file", file)` |
| 需要连接池/HTTP2 | 建议改用 OkHttp 或 Apache HttpClient |

## 注意事项

1. Hutool HTTP 基于 `HttpURLConnection`，不支持 HTTP/2 和连接池复用
2. 高并发场景建议切换到 OkHttp 或 Spring 的 `RestClient`
3. 默认自动处理 301/302 重定向，可通过 `.disableRedirect()` 关闭
4. HTTPS 证书校验默认关闭（生产环境需手动开启验证）
