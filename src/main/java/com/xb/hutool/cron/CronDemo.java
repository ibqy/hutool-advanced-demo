package com.xb.hutool.cron;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.date.DateUtil;

/**
 * CronDemo - 演示 Hutool CronUtil 定时任务调度
 *
 * 定时任务是后台系统的常见需求。本类演示如何用 CronUtil 注册带 Cron 表达式的任务、
 * 动态启停调度器，帮助学习者理解 Hutool 内置的轻量级调度方案（无需引入 Quartz）。
 *
 * @author ibqy
 */
public class CronDemo {

    /**
     * 定时任务演示入口：注册两个不同频率的任务并观察输出
     */
    public static void demo() {
        System.out.println("═══ CronUtil ═══");

        // 1. 匿名任务：每秒输出
        CronUtil.schedule("job1", "* * * * * ?", (Task) () ->
            System.out.println("  🔔 job1 tick → " + DateUtil.now()));

        // 2. 带 ID 的任务
        CronUtil.schedule("job2", "*/2 * * * * ?", (Task) () ->
            System.out.println("  🔔 job2 (2s) → " + DateUtil.now()));

        // 3. 启动（非守护）
        // 开启秒级匹配，默认只支持分钟级；"* * * * * ?" 表示每秒执行
        CronUtil.setMatchSecond(true);
        CronUtil.start(false);
        System.out.println("  定时任务已启动（3秒后自动停止）");

        ThreadUtil.sleep(3000);
        CronUtil.stop();
        System.out.println("  定时任务已停止");

        System.out.println();
    }
}