package com.bmos.wms.service.businessLog.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.wms.service.businessLog.dto.PositionLogPageQuery;
import com.bmos.wms.service.businessLog.model.PositionLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 18:19
 */
@Mapper
public interface IPositionLogMapper extends BaseMapperX<PositionLog> {

    default List<PositionLog> queryPage(PositionLogPageQuery pageQuery, List<Long> positionIds) {
        if (positionIds != null && positionIds.isEmpty()) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(PositionLog.class)
                .eq(pageQuery.getCargoId() != null, PositionLog::getCargoId, pageQuery.getCargoId())
                .eq(StrUtil.isNotBlank(pageQuery.getInventoryBatchNo()), PositionLog::getInventoryBatchNo, pageQuery.getInventoryBatchNo())
                .eq(StrUtil.isNotBlank(pageQuery.getInventoryNo()), PositionLog::getInventoryNo, pageQuery.getInventoryNo())
                .eq(pageQuery.getOperateType() != null, PositionLog::getOperateType, pageQuery.getOperateType())
                .between(pageQuery.getStartDate() != null && pageQuery.getEndDate() != null, PositionLog::getOperateTime, pageQuery.getStartDate(), pageQuery.getEndDate())
                .in(positionIds != null && !positionIds.isEmpty(), PositionLog::getPositionId, positionIds)
                .orderByDesc(PositionLog::getOperateTime)
        );
    }

    default boolean existByCargoPositionId(Long positionId){
        if (positionId == null){
            return false;
        }
        return exists(Wrappers.lambdaQuery(PositionLog.class)
                .eq(PositionLog::getPositionId, positionId)
        );
    }
}
