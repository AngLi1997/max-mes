package com.bmos.mes.service.weigh.centre.config.util;

import cn.hutool.core.collection.CollectionUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 树结构构建工具
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 11:22
 */
public class BmosTreeUtil {

    public static <T, K, S extends Comparable<S>> void buildTree(List<? extends BmosTreeNode<T, K, S>> list, K rootNodeId) {
        LinkedHashMap<K, ? extends BmosTreeNode<T, K, S>> kMap = list.stream()
                .collect(Collectors.toMap(BmosTreeNode::getId, node -> node, (a, b) -> a, LinkedHashMap::new));
        Set<K> ids = new HashSet<>();
        kMap.forEach((key, node) -> {
            if (!Objects.equals(node.getParentId(), rootNodeId)) {
                BmosTreeNode<T, K, S> parentNode = kMap.get(node.getParentId());
                if (parentNode != null) {
                    parentNode.addChild(node);
                    ids.add(node.getId());
                }
            }
        });
        list.removeIf(node -> ids.contains(node.getId()));
        sort(list);
    }


    private static <T, K, S extends Comparable<S>> void sort(List<? extends BmosTreeNode<T, K, S>> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        if (list.get(0).getSortBy() == null){
            return;
        }
        Comparator<BmosTreeNode<T, K, S>> comparator = Comparator.comparing(BmosTreeNode::getSortBy);
        list.sort(comparator);
        for (BmosTreeNode<T, K, S> tksBmosTreeNode : list) {
            sort(tksBmosTreeNode.getChildren());
        }
    }
}
