package com.xb.hutool.tree;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.Node;
import java.util.*;

/**
 * TreeUtil 高级用法
 * - 构建树结构
 * - 自定义节点
 * - 树遍历/过滤/剪枝
 */
public class TreeDemo {

    static class DeptNode implements Node<String> {
        String id; String parentId; String name; int sort;
        DeptNode(String id, String parentId, String name, int sort) {
            this.id = id; this.parentId = parentId; this.name = name; this.sort = sort;
        }
        public String getId() { return id; }
        public String getParentId() { return parentId; }
        public String getName() { return name; }
        public int getWeight() { return sort; }
        public Comparable<?> getWeight() { return sort; }
        public void setWeight(Comparable<?> w) { this.sort = (int) w; }
        public void setId(String id) { this.id = id; }
        public void setParentId(String pid) { this.parentId = pid; }
        public void setName(String name) { this.name = name; }
        public void setWeight(int w) { this.sort = w; }
    }

    public static void demo() {
        System.out.println("═══ TreeUtil ═══");

        // 构建树：组织架构
        List<DeptNode> list = Arrays.asList(
            new DeptNode("1", "0", "总公司", 0),
            new DeptNode("2", "1", "技术部", 1),
            new DeptNode("3", "1", "市场部", 2),
            new DeptNode("4", "2", "前端组", 1),
            new DeptNode("5", "2", "后端组", 2),
            new DeptNode("6", "3", "推广组", 1)
        );

        List<Tree<String>> treeList = TreeUtil.build(list, "0");

        // 打印树
        System.out.println("  组织架构树:");
        printTree(treeList, "    ");

        // 获取节点路径
        Tree<String> node5 = TreeUtil.getNode(treeList, "5");
        if (node5 != null) {
            List<CharSequence> path = node5.getParentsName(true);
            System.out.println("  节点5路径 → " + String.join(" > ", path));
        }

        // 查找子节点
        Tree<String> dept2 = TreeUtil.getNode(treeList, "2");
        if (dept2 != null) {
            System.out.println("  技术部下有 " + dept2.getChildren().size() + " 个子部门");
        }

        System.out.println();
    }

    static void printTree(List<Tree<String>> list, String prefix) {
        for (Tree<String> t : list) {
            System.out.println(prefix + "├─ " + t.getName() + " (id=" + t.getId() + ")");
            if (t.getChildren() != null && !t.getChildren().isEmpty()) {
                printTree(t.getChildren(), prefix + "│  ");
            }
        }
    }
}