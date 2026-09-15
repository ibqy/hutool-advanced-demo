# AOP 代理与切面

> 对应 Demo：`aop/AopDemo.java`

## 概述

Hutool 提供了基于 JDK 动态代理的轻量级 AOP 支持，通过 `ProxyUtil` 和 `SimpleAspect` 实现方法拦截，无需引入 Spring 或 AspectJ。

## JDK 动态代理 vs Cglib

| 维度 | JDK 动态代理 | Cglib |
|------|-------------|-------|
| 要求 | 目标类必须实现接口 | 无需接口，通过子类继承 |
| 性能 | 反射调用，稍慢 | 字节码生成，稍快 |
| 适用范围 | 仅接口方法 | 所有非 final 方法 |
| Hutool 支持 | `ProxyUtil.proxy()` | 需 Spring 环境 |

Hutool 的 `ProxyUtil.proxy()` 基于 JDK 动态代理，要求目标对象实现接口。

## 基础用法

```java
// 定义业务接口
public interface BizService {
    String doWork(String task);
}

// 实现类
public class BizServiceImpl implements BizService {
    public String doWork(String task) {
        System.out.println("执行业务: " + task);
        return "done";
    }
}

// 创建代理 — 织入切面逻辑
BizService proxy = ProxyUtil.proxy(new BizServiceImpl(), new SimpleAspect() {
    @Override
    public boolean before(Object target, Method method, Object[] args) {
        System.out.println("[前置] 调用 " + method.getName() + "，参数: " +
            Arrays.toString(args));
        return true; // 返回 true 继续执行，false 拦截
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        System.out.println("[后置] 方法返回: " + returnVal);
        return true;
    }

    @Override
    public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
        System.err.println("[异常] " + method.getName() + " 异常: " + e.getMessage());
        return true; // true = 吞掉异常不抛出
    }
});

proxy.doWork("test");
// 输出顺序：before → 执行业务 → after
```

## 切面生命周期

```
调用方法
  │
  ├─ before()  →  return false?  →  拦截，不执行原方法
  │      │
  │    return true
  │      │
  │      ├─ 执行原方法 ──┬── 正常返回 → after()
  │      │               │
  │      │               └── 抛异常   → afterException()
  │      │
  │      └─ 返回调用方
```

## 实战封装

将代理创建和切面逻辑封装为工具类，简化使用：

```java
public class AopUtil {

    /** 为目标对象添加日志切面 */
    @SuppressWarnings("unchecked")
    public static <T> T withLogging(T target, Class<T> interfaceClass) {
        return (T) ProxyUtil.proxy(target, new SimpleAspect() {
            private long start;
            
            @Override
            public boolean before(Object target, Method method, Object[] args) {
                start = System.currentTimeMillis();
                return true;
            }

            @Override
            public boolean after(Object target, Method method, Object[] args,
                                 Object returnVal) {
                long cost = System.currentTimeMillis() - start;
                System.out.printf("[%s] %dms%n", method.getName(), cost);
                return true;
            }
        });
    }
}
```

## Spring 环境中的 Cglib 代理检测

在 Spring 项目中，Bean 可能被 Cglib 代理。以下方法可以检测当前对象是 Cglib 代理还是原始对象：

```java
// 检测 Cglib 代理
if (Enhancer.isEnhanced(target.getClass())) {
    System.out.println("这是 Cglib 代理对象");
}

// 获取原始类（如果是 Cglib 代理，返回原始类名不含 $$ 后缀）
public static Class<?> getRealClass(Object obj) {
    Class<?> clazz = obj.getClass();
    while (clazz.getName().contains("$$")) {
        clazz = clazz.getSuperclass();
    }
    return clazz;
}
```

## 生产场景

| 场景 | 实现方式 |
|------|---------|
| 日志记录 | `before` 记录入参 + `after` 记录耗时 |
| 性能监控 | `after` 中计算 `System.currentTimeMillis() - start` |
| 权限校验 | `before` 中验证权限 → 返回 false 拦截 |
| 异常告警 | `afterException` 中发送告警通知 |
| 缓存注解 | `before` 查缓存命中也跳过，`after` 写缓存 |

## 注意事项

1. **仅限接口代理**：JDK 动态代理要求目标类实现接口；如无接口，需用 Cglib（Spring 环境）
2. **this 调用不触发代理**：目标方法内调用 `this.otherMethod()` 不会走切面
3. **性能开销小**：JDK 代理的反射调用开销在纳秒级，99% 场景无需担心
4. **替代方案**：Spring 项目直接用 `@Aspect` + `@Around` 更灵活
