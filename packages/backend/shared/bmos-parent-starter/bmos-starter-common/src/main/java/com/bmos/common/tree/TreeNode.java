package com.bmos.common.tree;

import java.time.LocalDateTime;
import java.util.List;

public interface TreeNode<T, K, O> {

    K getId();

    K getParentId();

    List<T> getChildren();

    void setChildren(List<T> children);


    O sort();

    default int compareTo(TreeNode<T, K, O> node, Boolean desc) {
        O sort = this.sort();
        Integer result = null;
        if (sort instanceof String) {
            result = ((String) sort).compareTo((String) node.sort());
        }
        if (sort instanceof LocalDateTime) {
            result = ((LocalDateTime) sort).compareTo((LocalDateTime) node.sort());
        }
        if (sort instanceof Long) {
            result = ((Long) sort).compareTo((Long) node.sort());
        }
        if (result == null) {
            throw new IllegalArgumentException(String.format("unSupported sortField Class: %s", sort.getClass()));
        }
        return desc != null && desc ? -result : result;
    }

}
