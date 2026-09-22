package com.xb.hutool.util;

import cn.hutool.core.util.*;
import cn.hutool.core.date.*;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.AES;
import java.util.*;

/**
 * MoreUtilDemo - 演示 Hutool 各类工具函数的综合用法
 *
 * Hutool 提供了大量开箱即用的工具类。本类集中演示 UUID/NanoId 生成、
 * MD5/AES 加解密、数据脱敏、类型转换、Map 操作、Base64、日期处理和空值判断等功能。
 *
 * @author ibqy
 */
public class MoreUtilDemo {

    /**
     * 工具综合演示入口：依次展示 8 类常用工具函数
     */
    public static void demo() {
        System.out.println("═══ 更多工具 ═══");

        // 1. 唯一 ID
        String id = IdUtil.fastSimpleUUID();
        System.out.println("  UUID → " + id);
        System.out.println("  nanoId → " + IdUtil.nanoId(12));

        // 2. 加密
        String pwd = "hutool@2024";
        String md5 = SecureUtil.md5(pwd);
        System.out.println("  MD5 → " + md5);

        // AES 密钥长度必须是 16/24/32 字节，这里截取 UUID 前 16 位
        AES aes = SecureUtil.aes(IdUtil.simpleUUID().substring(0, 16).getBytes());
        String enc = aes.encryptBase64(pwd);
        String dec = aes.decryptStr(enc);
        System.out.println("  AES 加密 → " + enc.substring(0, 20) + "…");
        System.out.println("  AES 解密 → " + dec);

        // 3. 脱敏
        System.out.println("  手机号脱敏 → " + DesensitizedUtil.mobilePhone("13812345678"));
        System.out.println("  邮箱脱敏 → " + DesensitizedUtil.email("ibqy@example.com"));
        System.out.println("  密码脱敏 → " + DesensitizedUtil.password(pwd));

        // 4. 类型转换
        int num = Convert.toInt("42");
        String str = Convert.toStr(3.14);
        Date date = Convert.toDate("2024-12-01");
        System.out.println("  Convert → int=" + num + " str=" + str + " date=" + date);

        // 5. Map 工具
        Map<String, Object> map = MapUtil.<String, Object>builder("name", "ibqy")
            .put("age", 25)
            .put("lang", new String[]{"Java", "Python"})
            .build();
        String json = MapUtil.join(map, ",", "=");
        System.out.println("  Map → " + map);
        System.out.println("  join → " + json);

        // 6. Base64
        String b64 = Base64.encode("Hello Hutool");
        System.out.println("  Base64 → " + b64 + " / decode → " + Base64.decodeStr(b64));

        // 7. 日期时间
        DateTime now = DateUtil.date();
        String formatted = DateUtil.format(now, "yyyy-MM-dd HH:mm:ss");
        DateTime yesterday = DateUtil.yesterday();
        long between = DateUtil.between(now, yesterday, DateUnit.HOUR);
        System.out.println("  当前 → " + formatted);
        System.out.println("  距昨天 → " + Math.abs(between) + " 小时");

        // 8. 对象工具
        Object nil = null;
        System.out.println("  ObjUtil.isEmpty(null) → " + ObjectUtil.isEmpty(nil));
        System.out.println("  ObjUtil.defaultIfNull → " + ObjectUtil.defaultIfNull(nil, "默认值"));

        System.out.println();
    }
}