package com.bmos.wms.service.storage.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.wms.service.storage.model.Storage;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 存储区域 mapper
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/18 11:00
 */
@Mapper
public interface IStorageMapper extends BaseMapper<Storage> {

    /**
     * 查询同级存储区域是否重名
     *
     * @param parentId 父级id
     * @param name     存储区域名称
     * @return 是否存在 true 存在 false 不存在
     */
    default boolean exist(Long parentId, String name) {
        return exists(Wrappers.lambdaQuery(Storage.class)
                .isNull(parentId == null, Storage::getParentId)
                .eq(parentId != null, Storage::getParentId, parentId)
                .eq(Storage::getName, name));
    }

    /**
     * 根据父级id查询列表
     *
     * @param parentId 父级id
     * @return
     */
    default List<Storage> queryListByParentId(Long parentId) {
        return selectList(Wrappers.lambdaQuery(Storage.class)
                .isNull(parentId == null, Storage::getParentId)
                .eq(parentId != null, Storage::getParentId, parentId)
        );
    }

    /**
     * 根据父级id查询子节点（单层）
     *
     * @param parentIds 父级id列表
     * @return
     */
    default List<Storage> queryListByParentId(Collection<Long> parentIds) {
        if (CollectionUtil.isEmpty(parentIds)) {
            return new ArrayList<>();
        }
        // 判断是否包含null
        if (parentIds.contains(null)) {
            // 查询所有
            return selectList(Wrappers.lambdaQuery());
        }
        return selectList(Wrappers.lambdaQuery(Storage.class)
                .in(Storage::getParentId, parentIds)
        );
    }

    /**
     * 根据父级id递归查询所有子节点
     *
     * @param parentIds 父级id列表
     * @return
     */
    default List<Storage> queryAllChildren(List<Long> parentIds) {
        if (CollectionUtil.isEmpty(parentIds)) {
            return new ArrayList<>();
        }
        if (parentIds.contains(null)) {
            // 查询所有
            return selectList(Wrappers.lambdaQuery());
        }
        List<Storage> children = queryListByParentId(parentIds);
        if (CollectionUtil.isEmpty(children)) {
            return new ArrayList<>(queryListByIds(parentIds));
        }
        List<Long> childrenIds = children.stream().map(Storage::getId).collect(Collectors.toList());
        List<Storage> allChildren = queryAllChildren(childrenIds);
        children.addAll(allChildren);
        children.addAll(queryListByIds(parentIds));
        return children;
    }

    default List<Storage> queryAllChildren(Long parentId) {
        if (parentId == null) {
            return selectList(Wrappers.lambdaQuery());
        }
        List<Long> list = new ArrayList<>();
        list.add(parentId);
        return queryAllChildren(list);
    }

    default String getStoragePath(Long id) {
        List<Long> path = new ArrayList<>();
        Storage storage = selectById(id);
        while (storage != null) {
            path.add(storage.getId());
            storage = selectById(storage.getParentId());
        }
        CollectionUtil.reverse(path);
        return CollectionUtil.join(path, ",");
    }

    default List<Storage> queryListByIds(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(Storage.class)
                .in(Storage::getId, ids)
        );
    }
}
