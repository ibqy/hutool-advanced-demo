package com.ibqy.hutool.config;

import cn.hutool.setting.Setting;
import cn.hutool.core.io.FileUtil;
import java.nio.charset.StandardCharsets;

/**
 * Setting 高级用法
 * - 分组配置
 * - 自动加载
 * - 配置变更监听
 */
public class SettingDemo {
    public static void demo() {
        System.out.println("═══ Setting ═══");

        // 先写一个示例配置文件
        String cfg = "# demo config\ndb.url=jdbc:mysql://localhost:3306/test\ndb.user=root\ndb.pass=123456\n" +
            "server.port=8080\nserver.host=0.0.0.0\n" +
            "[pool]\nmaxActive=20\nmaxIdle=10\ntimeout=30000\n";
        FileUtil.writeString(cfg, "/tmp/hutool.setting", StandardCharsets.UTF_8);

        // 读取配置
        Setting setting = new Setting("/tmp/hutool.setting", true);
        String url = setting.getStr("db.url");
        int port = setting.getInt("server.port");
        int maxActive = setting.getInt("pool.maxActive");
        System.out.println("  db.url → " + url);
        System.out.println("  server.port → " + port);
        System.out.println("  pool.maxActive → " + maxActive);

        // 遍历分组
        System.out.print("  所有 key: ");
        setting.keySet().forEach(k -> System.out.print(k + " "));
        System.out.println();

        // store 到新文件
        setting.store("/tmp/hutool_copy.setting");
        System.out.println("  配置已备份");

        System.out.println();
    }
}