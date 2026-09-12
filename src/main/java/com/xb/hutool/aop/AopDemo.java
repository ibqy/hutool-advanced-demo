package com.xb.hutool.aop;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.aop.aspects.SimpleAspect;
import cn.hutool.core.date.DateUtil;
import java.lang.reflect.Method;

/**
 * AOP 高级用法
 * - 动态代理
 * - 方法拦截（前置/后置/异常）
 * - 性能监控
 */
public class AopDemo {

    interface BizService {
        String doWork(String input);
        void failTask();
    }

    static class BizServiceImpl implements BizService {
        public String doWork(String input) {
            return "处理结果: [" + input.toUpperCase() + "]";
        }

        public void failTask() {
            throw new RuntimeException("模拟异常");
        }
    }

    public static void demo() {
        System.out.println("═══ AOP ═══");

        // 创建代理：带性能监控 + 日志
        BizService proxy = ProxyUtil.proxy(new BizServiceImpl(), new SimpleAspect() {
            @Override
            public boolean before(Object target, Method method, Object[] args) {
                System.out.println("  ⏳ 前置 → " + method.getName()
                    + " | 参数=" + java.util.Arrays.toString(args)
                    + " | 时间=" + DateUtil.now());
                return true;
            }

            @Override
            public boolean after(Object target, Method method, Object[] args, Object res) {
                System.out.println("  ✅ 后置 → " + method.getName() + " | 结果=" + res);
                return true;
            }

            @Override
            public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
                System.out.println("  ❌ 异常 → " + method.getName() + " | " + e.getMessage());
                return true;
            }
        });

        // 调用
        String res = proxy.doWork("hutool aop");
        System.out.println("  返回 → " + res);

        try {
            proxy.failTask();
        } catch (Exception ignored) {}

        System.out.println();
    }
}