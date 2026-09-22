package com.xb.hutool.aop;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.aop.aspects.SimpleAspect;
import cn.hutool.core.date.DateUtil;
import java.lang.reflect.Method;

/**
 * AopDemo - 演示 Hutool AOP 面向切面编程能力
 *
 * 通过动态代理实现对业务方法的透明拦截，演示前置/后置/异常三种通知类型。
 * 核心要点：ProxyUtil.proxy() 创建代理，SimpleAspect 定义切面逻辑。
 * 适用场景：日志记录、性能监控、权限校验等非侵入式增强。
 *
 * @author ibqy
 */
public class AopDemo {

    /**
     * 业务接口，模拟真实服务层
     */
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

    /**
     * AOP 演示入口：创建代理对象并调用业务方法
     *
     * 代理对象在方法执行前后自动触发通知，异常时走 afterException 回调。
     */
    public static void demo() {
        System.out.println("═══ AOP ═══");

        // 创建代理：带性能监控 + 日志
        // 返回 true 表示继续执行目标方法，返回 false 则中断执行
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