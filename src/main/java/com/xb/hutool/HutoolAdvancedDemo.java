package com.xb.hutool;

import com.xb.hutool.http.*;
import com.xb.hutool.config.*;
import com.xb.hutool.cache.*;
import com.xb.hutool.cron.*;
import com.xb.hutool.auth.*;
import com.xb.hutool.aop.*;
import com.xb.hutool.tree.*;
import com.xb.hutool.excel.*;
import com.xb.hutool.util.*;
import com.xb.hutool.crypto.*;
import com.xb.hutool.validation.*;

/**
 * HutoolAdvancedDemo - Hutool 高级用法综合演示入口
 *
 * 本项目通过 14+ 个独立 Demo 模块，系统演示 Hutool 工具库在生产级场景中的应用。
 * 涵盖 HTTP、缓存、JWT 鉴权、AOP、加密、定时任务、Excel、树结构等核心能力。
 * 运行 main 方法即可依次执行所有 Demo，适合教学演示和学习参考。
 *
 * @author ibqy
 */
public class HutoolAdvancedDemo {

    /**
     * 程序入口，依次调用所有 Demo 模块的 demo() 方法
     *
     * @param args 命令行参数（本示例未使用）
     * @throws Exception 任何 Demo 执行过程中的异常
     */
    public static void main(String[] args) throws Exception {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   Hutool Advanced Demo              ║");
        System.out.println("║   覆盖 14+ 高级场景                   ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();

        HttpUtilDemo.demo();
        SettingDemo.demo();
        CacheDemo.demo();
        CacheAdvancedDemo.demo();
        CronDemo.demo();
        JwtDemo.demo();
        JwtRefreshDemo.demo();
        CryptoDemo.demo();
        AopDemo.demo();
        TreeDemo.demo();
        ExcelDemo.demo();
        MoreUtilDemo.demo();
        ValidatorDemo.demo();

        System.out.println("\n✅ 全部 Demo 执行完毕");
    }
}