package com.xb.hutool.config;

import cn.hutool.setting.Setting;
import cn.hutool.core.io.FileUtil;
import java.nio.charset.StandardCharsets;

/**
 * SettingDemo - 演示 Hutool Setting 配置文件的读取与操作
 *
 * Setting 是 Hutool 提供的轻量级配置管理工具，支持分组配置、类型安全读取和配置持久化。
 * 本类演示如何动态创建配置文件、按 key/分组读取、遍历所有配置项以及备份配置文件。
 *
 * @author ibqy
 */
public class SettingDemo {

    /**
     * 配置演示入口：创建示例配置文件并演示读取、遍历、备份操作
     */
    public static void demo() {
        System.out.println("═══ Setting ═══");

        // 先写一个示例配置文件
        String cfg = "# demo config\ndb.url=jdbc:mysql://localhost:3306/test\ndb.user=root\ndb.pass=123456\n" +
            "server.port=8080\nserver.host=0.0.0.0\n" +
            "[pool]\nmaxActive=20\nmaxIdle=10\ntimeout=30000\n";
        FileUtil.writeString(cfg, "/tmp/hutool.setting", StandardCharsets.UTF_8);

        // 读取配置
        // 第二个参数 true 表示开启自动加载，文件变更时自动重新读取
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