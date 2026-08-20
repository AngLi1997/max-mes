package com.bmos.lims2.server.inspect.parameter.mapper;

import com.bmos.lims2.server.inspect.parameter.entity.InspectMethodOperateBind;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 方法-操作规程 绑定关系Mapper
 * @Author: yigaohui
 * @Date: 2025/11/03 00:00
 */
@Mapper
public interface InspectMethodOperateBindMapper extends BaseMapperX<InspectMethodOperateBind> {

    default void deleteByRecordId(Long recordId) {
        delete(new LambdaQueryWrapperX<InspectMethodOperateBind>()
            .eq(InspectMethodOperateBind::getRecordId, recordId));
    }

    default void deleteByOperateId(Long operateId) {
        delete(new LambdaQueryWrapperX<InspectMethodOperateBind>()
            .eq(InspectMethodOperateBind::getOperateId, operateId));
    }

    default boolean saveBatch(List<InspectMethodOperateBind> list) {
        return Db.saveBatch(list);
    }

    default List<Long> listOperateIdsByRecordId(Long recordId) {
        return selectList(new LambdaQueryWrapperX<InspectMethodOperateBind>()
                .eq(InspectMethodOperateBind::getRecordId, recordId))
            .stream().map(InspectMethodOperateBind::getOperateId).collect(Collectors.toList());
    }

    default List<Long> listRecordIdsByOperateId(Long operateId) {
        return selectList(new LambdaQueryWrapperX<InspectMethodOperateBind>()
                .eq(InspectMethodOperateBind::getOperateId, operateId))
            .stream().map(InspectMethodOperateBind::getRecordId).collect(Collectors.toList());
    }

    /**
     * 批量按操作规程ID查询绑定关系列表
     */
    default List<InspectMethodOperateBind> listByOperateIds(List<Long> operateIds) {
        if (operateIds == null || operateIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<InspectMethodOperateBind>()
                .in(InspectMethodOperateBind::getOperateId, operateIds));
    }
}


