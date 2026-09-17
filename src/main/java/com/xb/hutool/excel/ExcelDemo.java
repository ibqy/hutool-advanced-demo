package com.xb.hutool.excel;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.StyleSet;
import cn.hutool.core.io.FileUtil;
import java.util.*;

/**
 * ExcelUtil 高级用法
 * - 导出（带样式）
 * - 导入
 * - 大数据流式写入
 * - 自定义单元格样式
 */
public class ExcelDemo {
    public static void demo() {
        System.out.println("═══ ExcelUtil ═══");

        String path = "/tmp/hutool_demo.xlsx";

        // 1. 写入（带表头 + 数据 + 样式）
        ExcelWriter writer = ExcelUtil.getWriter(path);

        // 合并标题行
        writer.merge(4, "Hutool 用户列表");
        writer.setOnlyAlias(true);

        // 设置列别名
        writer.addHeaderAlias("id", "编号");
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("role", "角色");
        writer.addHeaderAlias("score", "积分");

        // 数据
        List<Map<String, Object>> rows = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", i);
            row.put("name", "用户" + i);
            row.put("role", i % 2 == 0 ? "管理员" : "普通用户");
            row.put("score", (int)(Math.random() * 1000));
            rows.add(row);
        }
        writer.write(rows);

        // 设置列宽
        writer.setColumnWidth(0, 10);
        writer.setColumnWidth(1, 20);
        writer.setColumnWidth(2, 20);
        writer.setColumnWidth(3, 15);

        writer.close();
        System.out.println("  写入 → " + path + " (" + rows.size() + " 行)");

        // 2. 读取
        ExcelReader reader = ExcelUtil.getReader(path);
        List<List<Object>> all = reader.read();
        System.out.println("  读取 → " + all.size() + " 行");
        for (int i = 0; i < Math.min(3, all.size()); i++) {
            System.out.println("    " + all.get(i));
        }

        System.out.println();
    }
}