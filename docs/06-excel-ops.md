# Excel 操作

> 对应 Demo：`excel/ExcelDemo.java`

## 概述

Hutool 基于 Apache POI 封装了 `ExcelWriter` 和 `ExcelReader`，简化 Excel 读写操作。支持 Bean 映射、别名、样式、合并单元格和流式导出。

## 基础用法

### 写入 Excel

#### 简单写入

```java
// 准备数据
List<User> userList = List.of(
    new User("张三", 25, "技术部"),
    new User("李四", 30, "产品部")
);

// 写入 Excel
ExcelWriter writer = ExcelUtil.getWriter("users.xlsx");
writer.write(userList, true);  // true = 写入列标题
writer.close();
```

#### Bean 方式写入（带别名）

```java
ExcelWriter writer = ExcelUtil.getWriter("users.xlsx");

// 设置列别名（中文标题映射到 Bean 属性）
writer.addHeaderAlias("name", "姓名");
writer.addHeaderAlias("age", "年龄");
writer.addHeaderAlias("deptName", "部门");

// 只导出指定字段
writer.setOnlyAlias(true);

// 写入数据（自动使用别名生成表头）
writer.write(userList, true);

// 设置列宽
writer.setColumnWidth(0, 15);
writer.setColumnWidth(1, 10);
writer.setColumnWidth(2, 20);

// 设置样式
StyleSet style = writer.getStyleSet();
style.setAlign(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

writer.close();
```

#### 带标题和合并单元格

```java
ExcelWriter writer = ExcelUtil.getWriter("report.xlsx");

// 合并第一行作为大标题
writer.merge(3, "2024 年销售报表");
writer.setCurrentRowTo(1); // 跳过标题行，从第 2 行开始写数据

// 写入数据
writer.write(dataList);
writer.close();
```

### 读取 Excel

```java
// 读取全部数据
ExcelReader reader = ExcelUtil.getReader("users.xlsx");
List<List<Object>> all = reader.readAll();

// Bean 方式读取（字段名对应表头）
reader.addHeaderAlias("姓名", "name");
reader.addHeaderAlias("年龄", "age");
List<User> users = reader.readAll(User.class);

// 指定 sheet 读取
ExcelReader sheet2 = ExcelUtil.getReader("data.xlsx", 1); // 第 2 个 sheet
```

## 大数据流式导出

当数据量较大（10 万+ 行）时，一次性将所有数据加载到内存会导致 OOM。此时应使用 `BigExcelWriter`：

```java
// 流式写入（默认 100 条刷新一次）
BigExcelWriter writer = ExcelUtil.getBigWriter("large.xlsx");
writer.addHeaderAlias("name", "姓名");
writer.addHeaderAlias("age", "年龄");

// 分批从数据库读取并写入
int pageSize = 5000;
int offset = 0;
List<User> batch;
while (!(batch = userRepo.findPage(offset, pageSize)).isEmpty()) {
    writer.write(batch);
    writer.flush();  // 刷盘
    offset += pageSize;
}

writer.close();
```

需额外引入依赖：

```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.5</version>
</dependency>
```

## 核心 API 速查

```java
// 创建
ExcelUtil.getWriter(filePath)        // 写入（.xlsx）
ExcelUtil.getBigWriter(filePath)     // 流式写入
ExcelUtil.getReader(filePath)        // 读取

// Writer
writer.addHeaderAlias(field, alias)  // 字段别名
writer.setOnlyAlias(true)            // 仅导出别名字段
writer.merge(count, title)           // 合并单元格
writer.setColumnWidth(index, width)  // 列宽
writer.setCurrentRowTo(row)          // 指定当前行
writer.passRows(n)                   // 跳过前 n 行
writer.passCurrentRow()              // 跳过当前行
writer.flush()                       // 刷盘
writer.close()                       // 关闭（必须）

// Reader
reader.addHeaderAlias(alias, field)  // 别名映射
reader.readAll()                     // 读取所有行（List<List<Object>>）
reader.readAll(Bean.class)           // Bean 方式读取
reader.read(row, col)                // 读取指定单元格
```

## 生产建议

1. **xls vs xlsx**：Hutool 会根据文件后缀自动选择 HSSF(.xls) 或 XSSF(.xlsx)；推荐 xlsx，行数上限更高
2. **流式导出必须**：超过 1 万行用 `BigExcelWriter`，默认缓存 100 条，可调 `setFlushSize()`
3. **数字精度**：读取 Excel 时数字默认转为 double，可能丢失精度，建议用 `reader.setCellEditor()`
4. **强制刷盘**：流式写入结束后必须 `flush()` + `close()`，否则文件不完整
5. **模板导出**：可用 `ExcelUtil.getWriter(templatePath)` 基于模板文件写入数据
