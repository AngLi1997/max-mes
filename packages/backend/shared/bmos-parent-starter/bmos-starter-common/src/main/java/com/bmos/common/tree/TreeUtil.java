package com.bmos.common.tree;

import cn.hutool.core.collection.CollUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TreeUtil {

    /**
     * 顶层父节点id
     */
    public static final Long parentId = 0L;

    public static <T extends TreeNode<T, K, O>, K extends Serializable, O> List<T> buildTree(List<T> originData,
                                                                                             Boolean desc) {
        return buildTree(originData, e -> Objects.equals(e.getParentId(), parentId), desc);
    }

    /**
     * 构造树的入口方法
     *
     * @param originData    数据列表
     * @param rootPredicate 根节点条件
     * @param <T>           implements {@link TreeNode}
     * @param <K>           id 的类型 implements Serializable
     * @param desc          是否反序
     * @return 树形结结构
     */
    public static <T extends TreeNode<T, K, O>, K extends Serializable, O> List<T> buildTree(List<T> originData,
                                                                                             Predicate<T> rootPredicate,
                                                                                             Boolean desc) {
        if (CollUtil.isEmpty(originData) || Objects.isNull(rootPredicate)) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>(CollUtil.size(originData));

        List<T> roots = originData.stream().filter(rootPredicate).sorted((o1, o2) -> o1.compareTo(o2, desc)).collect(Collectors.toList());

        if (CollUtil.isEmpty(roots)) {
            return Collections.emptyList();
        }

        // 删除根节点，避免重复遍历
        originData.removeAll(roots);

        roots.forEach(r -> result.add(buildTreeNode(r, originData, desc)));
        return result;
    }

    /**
     * 构建子节点
     *
     * @param parentNode 父节点
     * @param originData 数据列表
     * @param <T>        implements TreeNode
     * @param <K>        implements Serializable
     * @return 节点
     */
    private static <T extends TreeNode<T, K, O>, K extends Serializable, O> T buildTreeNode(T parentNode, List<T> originData, Boolean desc) {
        List<T> childrenNode = new ArrayList<>();

        List<T> children = filterByParentId(parentNode.getId(), originData, desc);

        if (CollUtil.isNotEmpty(children)) {
            // 删除节点，避免重复遍历
            originData.removeAll(children);
        }
        for (T child : children) {
            childrenNode.add(buildTreeNode(child, originData, desc));
        }
        List<T> existChildren = parentNode.getChildren();
        if (CollUtil.isNotEmpty(existChildren)) {
            existChildren.addAll(childrenNode);
        } else {
            parentNode.setChildren(childrenNode);
        }
        return parentNode;
    }


    /**
     * @param parentId   父节点ID
     * @param originData 数据列表
     * @param <T>        implements TreeNode
     * @param <K>        implements Serializable
     * @return children
     */
    public static <T extends TreeNode<T, K, O>, K extends Serializable, O> List<T> filterByParentId(K parentId, List<T> originData, Boolean desc) {
        return originData.stream()
                .filter(node -> Objects.equals(parentId, node.getParentId()))
                .sorted((o1, o2) -> o1.compareTo(o2, desc))
                .collect(Collectors.toList());

    }


}
