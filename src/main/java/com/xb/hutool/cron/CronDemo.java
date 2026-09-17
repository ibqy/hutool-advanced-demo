package com.xb.hutool.cron;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.date.DateUtil;

/**
 * CronUtil 高级用法
 * - 定时任务
 * - 动态添加/移除
 * - 任务监听
 */
public class CronDemo {
    public static void demo() {
        System.out.println("═══ CronUtil ═══");

        // 1. 匿名任务：每秒输出
        CronUtil.schedule("job1", "* * * * * ?", (Task) () ->
            System.out.println("  🔔 job1 tick → " + DateUtil.now()));

        // 2. 带 ID 的任务
        CronUtil.schedule("job2", "*/2 * * * * ?", (Task) () ->
            System.out.println("  🔔 job2 (2s) → " + DateUtil.now()));

        // 3. 启动（非守护）
        CronUtil.setMatchSecond(true);
        CronUtil.start(false);
        System.out.println("  定时任务已启动（3秒后自动停止）");

        ThreadUtil.sleep(3000);
        CronUtil.stop();
        System.out.println("  定时任务已停止");

        System.out.println();
    }
}