package com.xb.hutool.tree;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import java.util.*;

/**
 * TreeUtil 高级用法
 * - 构建树结构
 * - 自定义节点
 * - 树遍历/过滤/剪枝
 */
public class TreeDemo {

    public static void demo() {
        System.out.println("═══ TreeUtil ═══");

        List<TreeNode<String>> list = new ArrayList<>();
        list.add(new TreeNode<>("1", "0", "总公司", 0));
        list.add(new TreeNode<>("2", "1", "技术部", 1));
        list.add(new TreeNode<>("3", "1", "市场部", 2));
        list.add(new TreeNode<>("4", "2", "前端组", 1));
        list.add(new TreeNode<>("5", "2", "后端组", 2));
        list.add(new TreeNode<>("6", "3", "推广组", 1));

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

    static void printTree(List<Tree<String>> list, String prefix) {
        for (Tree<String> t : list) {
            System.out.println(prefix + "├─ " + t.getName() + " (id=" + t.getId() + ")");
            if (t.getChildren() != null && !t.getChildren().isEmpty()) {
                printTree(t.getChildren(), prefix + "│  ");
            }
        }
    }
}
