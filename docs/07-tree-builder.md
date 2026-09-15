# 树结构工具

> 对应 Demo：`tree/TreeDemo.java`

## 概述

Hutool 的 `TreeUtil` 能将平铺列表转换为树形结构，常用于组织架构树、分类目录、评论嵌套、路由菜单等场景。

## 核心概念

### TreeNode

每个树节点是一个 `TreeNode` 对象，包含以下核心属性：

```java
public class TreeNode<T> {
    private T id;            // 节点 ID
    private T parentId;      // 父节点 ID（根节点的 parentId 通常为 "0" 或 null）
    private String name;     // 节点名称
    private int weight;      // 排序权重（值越小越靠前）
    private Object extra;    // 附加数据（可存放原始业务对象）
}
```

### 数据准备

从数据库读取的通常是平铺列表，例如部门表：

| id | parentId | name |
|----|----------|------|
| 1  | 0        | 总公司 |
| 2  | 1        | 技术部 |
| 3  | 1        | 产品部 |
| 4  | 2        | 后端组 |
| 5  | 2        | 前端组 |

转换为 Hutool 的 `TreeNode`：

```java
List<TreeNode<String>> nodeList = deptList.stream().map(dept -> {
    TreeNode<String> node = new TreeNode<>();
    node.setId(dept.getId());
    node.setParentId(dept.getParentId());
    node.setName(dept.getName());
    node.setWeight(dept.getSortOrder());
    return node;
}).toList();
```

## 基础用法

### 构建树

```java
// "0" 是根节点的 parentId
List<Tree<String>> treeList = TreeUtil.build(nodeList, "0");
```

`build()` 返回 `Tree` 对象列表，每个 `Tree` 继承自 `TreeNode` 并增加了父子关系：

```java
treeList.forEach(tree -> {
    System.out.println(tree.getName());  // "总公司"
    tree.getChildren().forEach(child -> {
        System.out.println("  ├─ " + child.getName()); // "技术部"
    });
});
```

### 递归遍历

```java
// 递归打印整棵树（带缩进）
printTree(treeList, 0);

private void printTree(List<Tree<String>> nodes, int depth) {
    for (Tree<String> node : nodes) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "├─ " + node.getName());
        if (node.hasChild()) {
            printTree(node.getChildren(), depth + 1);
        }
    }
}
```

### 节点操作

```java
// 获取指定节点
Tree<String> node = TreeUtil.getNode(treeList, "5");

// 获取节点路径（从根到当前节点的名称列表）
List<CharSequence> path = node.getParentsName(true);
// → ["总公司", "技术部", "后端组"]

// 获取父节点
Tree<String> parent = node.getParent();

// 判断是否有子节点
boolean hasChildren = node.hasChild();

// 获取子节点列表
List<Tree<String>> children = node.getChildren();
```

## 进阶配置

### 自定义 TreeNode 配置

当数据库字段名与 TreeNode 默认属性不匹配时，使用 `TreeNodeConfig`：

```java
TreeNodeConfig config = new TreeNodeConfig();
config.setIdKey("deptId");       // ID 字段名，默认 "id"
config.setParentIdKey("pid");    // 父 ID 字段名，默认 "parentId"
config.setNameKey("deptName");   // 名称字段名，默认 "name"
config.setWeightKey("sort");     // 权重字段名，默认 "weight"
config.setDeep(3);               // 最大递归深度（0 = 不限）

// 配合 Map 数据使用
List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql);
List<Tree<Object>> tree = TreeUtil.build(dataList, "0", config);
```

### 附加业务数据

在 `TreeNode.extra` 中存放原始对象，供业务逻辑使用：

```java
node.setExtra(deptEntity); // deptEntity 包含部门负责人、电话等字段

// 使用时取出
DeptEntity dept = (DeptEntity) node.getExtra();
System.out.println(dept.getManager());
```

## 递归构建原理

```
build(nodeList, rootParentId):
  1. 过滤出 parentId == rootParentId 的所有节点
  2. 对每个节点，递归调用 build(nodeList, 当前节点.id)
  3. 将子节点赋值到 children 属性
  4. 返回根节点列表
```

**时间复杂度**：O(n²)，每次递归遍历整个列表。对于深层大列表，建议在数据库层用递归 CTE 或一次查出后在内存做 Map 索引优化。

## 生产场景

| 场景 | 配置要点 |
|------|---------|
| 组织架构树 | 部门表 → TreeNode，parentId="0" 为根 |
| 分类目录 | 无限级分类，设置 deep 限制展示深度 |
| 评论嵌套 | 评论表自关联 parentId |
| 路由菜单 | weight 控制菜单排序，extra 存放路由 path/icon |

## 注意事项

1. **节点 ID 唯一性**：同一棵树中所有节点 ID 必须唯一，否则 `getNode()` 返回不准确
2. **循环引用**：`build()` 不做环检测，如果数据中存在 A→B→A 的循环引用，会栈溢出
3. **性能优化**：大数据量时先按 parentId 分组成 Map，再递归构建，将 O(n²) 降到 O(n)
4. **空值处理**：根节点的 parentId 可以传 null（自动转为 "0"），也可以传 "0"
