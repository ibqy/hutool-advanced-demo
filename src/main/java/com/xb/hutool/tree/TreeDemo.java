package com.xb.hutool.tree;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import java.util.*;

/**
 * TreeDemo - 演示 Hutool TreeUtil 树结构的构建与遍历
 *
 * 树形结构（如组织架构、菜单、分类）是业务中的常见需求。本类演示如何用 TreeNode 定义节点、
 * TreeUtil.build() 一行代码构建树，以及递归查找和格式化打印。
 *
 * @author ibqy
 */
public class TreeDemo {

    /**
     * 树结构演示入口：构建组织架构树并演示查找与打印
     */
    public static void demo() {
        System.out.println("═══ TreeUtil ═══");

        List<TreeNode<String>> list = new ArrayList<>();
        list.add(new TreeNode<>("1", "0", "总公司", 0));
        list.add(new TreeNode<>("2", "1", "技术部", 1));
        list.add(new TreeNode<>("3", "1", "市场部", 2));
        list.add(new TreeNode<>("4", "2", "前端组", 1));
        list.add(new TreeNode<>("5", "2", "后端组", 2));
        list.add(new TreeNode<>("6", "3", "推广组", 1));

        // "0" 是根节点的 parentId，TreeUtil 会自动把 parentId="0" 的节点作为树根
        List<Tree<String>> treeList = TreeUtil.build(list, "0");

        System.out.println("  组织架构树:");
        printTree(treeList, "    ");

        Tree<String> root = treeList.get(0);
        Tree<String> dept2 = findNode(root, "2");
        if (dept2 != null) {
            System.out.println("  技术部下有 " + dept2.getChildren().size() + " 个子部门");
        }

        System.out.println();
    }

    /**
     * 递归查找指定 ID 的节点
     *
     * 深度优先遍历整棵树，找到第一个匹配的节点即返回。
     *
     * @param node 当前搜索的根节点
     * @param id   要查找的节点 ID
     * @return 匹配的节点，未找到返回 null
     */
    private static Tree<String> findNode(Tree<String> node, String id) {
        if (id.equals(node.getId())) return node;
        if (node.getChildren() != null) {
            for (Tree<String> child : node.getChildren()) {
                Tree<String> found = findNode(child, id);
                if (found != null) return found;
            }
        }
        return null;
    }

    /**
     * 以树形格式打印节点列表，用缩进表示层级关系
     *
     * @param list   当前层级的节点列表
     * @param prefix 打印前缀（用于缩进展示层级）
     */
    static void printTree(List<Tree<String>> list, String prefix) {
        for (Tree<String> t : list) {
            System.out.println(prefix + "├─ " + t.getName() + " (id=" + t.getId() + ")");
            if (t.getChildren() != null && !t.getChildren().isEmpty()) {
                printTree(t.getChildren(), prefix + "│  ");
            }
        }
    }
}
