package com.ibqy.hutool;

import com.ibqy.hutool.http.*;
import com.ibqy.hutool.config.*;
import com.ibqy.hutool.cache.*;
import com.ibqy.hutool.cron.*;
import com.ibqy.hutool.auth.*;
import com.ibqy.hutool.aop.*;
import com.ibqy.hutool.tree.*;
import com.ibqy.hutool.excel.*;
import com.ibqy.hutool.util.*;

/**
 * Hutool 高级用法综合演示入口
 * 运行 main 即可依次执行所有 Demo
 */
public class HutoolAdvancedDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   Hutool Advanced Demo              ║");
        System.out.println("║   覆盖 10+ 高级场景                   ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();

        HttpUtilDemo.demo();
        SettingDemo.demo();
        CacheDemo.demo();
        CronDemo.demo();
        JwtDemo.demo();
        AopDemo.demo();
        TreeDemo.demo();
        ExcelDemo.demo();
        MoreUtilDemo.demo();

        System.out.println("\n✅ 全部 Demo 执行完毕");
    }
}