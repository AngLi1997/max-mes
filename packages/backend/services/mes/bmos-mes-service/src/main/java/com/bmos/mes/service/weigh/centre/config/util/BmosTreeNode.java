package com.bmos.mes.service.weigh.centre.config.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 11:23
 */
public interface BmosTreeNode<T, K, S> {

    K getId();

    K getParentId();

    void addChild(BmosTreeNode<T, K, S> child);

    @JsonIgnore
    S getSortBy();

    List<? extends BmosTreeNode<T, K, S>> getChildren();
}
