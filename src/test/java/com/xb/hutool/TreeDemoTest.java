package com.xb.hutool;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TreeDemoTest - 树结构单元测试
 *
 * 验证树的构建、节点查找、多层级结构、空列表处理和节点排序。
 *
 * @author ibqy
 */
@DisplayName("Hutool TreeUtil 树结构测试")
class TreeDemoTest {

    @Test
    @DisplayName("构建简单树结构")
    void buildSimpleTree() {
        List<TreeNode<String>> nodes = new ArrayList<>();
        nodes.add(new TreeNode<>("1", "0", "根节点", 0));
        nodes.add(new TreeNode<>("2", "1", "子节点1", 1));
        nodes.add(new TreeNode<>("3", "1", "子节点2", 2));

        List<Tree<String>> tree = TreeUtil.build(nodes, "0");

        assertEquals(1, tree.size());
        assertEquals(2, tree.get(0).getChildren().size());
    }

    @Test
    @DisplayName("查找节点")
    void findNode() {
        List<TreeNode<String>> nodes = new ArrayList<>();
        nodes.add(new TreeNode<>("1", "0", "根", 0));
        nodes.add(new TreeNode<>("2", "1", "A", 1));
        nodes.add(new TreeNode<>("3", "2", "B", 2));

        List<Tree<String>> tree = TreeUtil.build(nodes, "0");
        Tree<String> node = findNode(tree.get(0), "3");

        assertNotNull(node);
        assertEquals("B", node.getName());
    }

    @Test
    @DisplayName("多层级树结构")
    void multiLevelTree() {
        List<TreeNode<String>> nodes = new ArrayList<>();
        nodes.add(new TreeNode<>("1", "0", "总公司", 0));
        nodes.add(new TreeNode<>("2", "1", "技术部", 1));
        nodes.add(new TreeNode<>("3", "1", "市场部", 2));
        nodes.add(new TreeNode<>("4", "2", "前端组", 1));
        nodes.add(new TreeNode<>("5", "2", "后端组", 2));

        List<Tree<String>> tree = TreeUtil.build(nodes, "0");

        assertEquals(1, tree.size());
        Tree<String> root = tree.get(0);
        assertEquals(2, root.getChildren().size());

        Tree<String> tech = findNode(root, "2");
        assertNotNull(tech);
        assertEquals(2, tech.getChildren().size());
    }

    @Test
    @DisplayName("空列表构建空树")
    void emptyTree() {
        List<TreeNode<String>> nodes = new ArrayList<>();
        List<Tree<String>> tree = TreeUtil.build(nodes, "0");
        assertTrue(tree == null || tree.isEmpty());
    }

    @Test
    @DisplayName("节点排序")
    void nodeSortOrder() {
        List<TreeNode<String>> nodes = new ArrayList<>();
        nodes.add(new TreeNode<>("1", "0", "根", 0));
        nodes.add(new TreeNode<>("2", "1", "B", 2));
        nodes.add(new TreeNode<>("3", "1", "A", 1));

        List<Tree<String>> tree = TreeUtil.build(nodes, "0");
        List<Tree<String>> children = tree.get(0).getChildren();

        assertEquals("A", children.get(0).getName());
        assertEquals("B", children.get(1).getName());
    }

    private Tree<String> findNode(Tree<String> node, String id) {
        if (id.equals(node.getId())) return node;
        if (node.getChildren() != null) {
            for (Tree<String> child : node.getChildren()) {
                Tree<String> found = findNode(child, id);
                if (found != null) return found;
            }
        }
        return null;
    }
}
