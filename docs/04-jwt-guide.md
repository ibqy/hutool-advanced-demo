# JWT 鉴权实战

> 对应 Demo：`auth/JwtDemo.java`

## 概述

Hutool 的 JWT 模块提供完整的 Token 生成、签名、解析和验证流程，支持 HS256（HMAC-SHA256）、RS256（RSA-SHA256）等签名算法，无需引入额外的 JWT 库。

## JWT 结构

```
Header.Payload.Signature
```

- **Header**：签名算法类型
- **Payload**：业务数据（uid、role、过期时间等）
- **Signature**：对前两部分的签名，防篡改

## 基础用法

### Token 生成

```java
// 创建 JWT Token（HS256 签名）
String secret = "my-secret-key-at-least-256-bits-long!!";

String token = JWT.create()
    .setPayload("uid", 1001)
    .setPayload("role", "admin")
    .setPayload("username", "zhangsan")
    .setIssuedAt(new Date())                             // 签发时间
    .setExpiresAt(DateUtil.offsetHour(new Date(), 1))    // 1 小时后过期
    .setKey(secret.getBytes(StandardCharsets.UTF_8))
    .sign();
```

### Token 解析与验证

```java
// 解析 Token（不验证签名，仅读取）
JWT jwt = JWTUtil.parseToken(token);
Integer uid = (Integer) jwt.getPayload("uid");     // 1001
String role = (String) jwt.getPayload("role");     // "admin"

// 验证签名
boolean valid = JWTUtil.verify(token, secret.getBytes(StandardCharsets.UTF_8));

// 验证签名 + 有效期 + 指定时间点之前有效
boolean allValid = JWTUtil.verify(token, secret.getBytes(StandardCharsets.UTF_8),
    DateUtil.date());
```

### HS256 签名

```java
// 显式指定 HS256 签名器
String token = JWT.create()
    .setSigner(JWTSignerUtil.hs256(secret.getBytes(StandardCharsets.UTF_8)))
    .setPayload("uid", 1001)
    .sign();
```

### RS256 签名（RSA 非对称加密）

HS256 的密钥（secret）需要双方共享，存在泄漏风险。RS256 使用**私钥签名、公钥验签**，更适合分布式的微服务场景。

```java
// 加载 RSA 密钥对
KeyPair keyPair = SecureUtil.generateKeyPair("RSA");
PrivateKey privateKey = keyPair.getPrivate();
PublicKey publicKey = keyPair.getPublic();

// 使用私钥签名
String token = JWT.create()
    .setSigner(JWTSignerUtil.rs256(privateKey))
    .setPayload("uid", 1001)
    .sign();

// 使用公钥验签（其他服务只需持有公钥）
JWT jwt = JWTUtil.parseToken(token);
boolean verified = jwt.setSigner(JWTSignerUtil.rs256(publicKey)).verify();
```

## 核心 API 速查

```java
// 生成
JWT.create()                     // 创建 JWT 构建器
    .setSigner(signer)           // 指定签名器（HS256/RS256/ES256 等）
    .setPayload(key, value)      // 添加自定义 payload 字段
    .setIssuedAt(date)           // 签发时间
    .setExpiresAt(date)          // 过期时间
    .setNotBefore(date)          // 生效时间
    .setKey(secretBytes)         // 简化的 HMAC 签名方式
    .sign();                     // 生成 Token 字符串

// 解析与验证
JWTUtil.parseToken(token)        // 解析 Token
JWTUtil.verify(token, key)       // 验证签名
jwt.getPayload(key)              // 读取 payload 字段
jwt.getPayloads()                // 读取所有 payload
```

## 生产建议

1. **密钥管理**：Secret 不应硬编码，从环境变量或配置中心读取
2. **RS256 优于 HS256**：微服务架构中私钥签名、公钥验签更安全
3. **Payload 精简**：不要放敏感信息（密码等），Payload 仅 Base64 编码，非加密
4. **短期 Token + Refresh Token**：Access Token 设置短过期（15 分钟），搭配 Refresh Token 续期
5. **算法约束**：显式指定 `setSigner()`，避免算法降级攻击
