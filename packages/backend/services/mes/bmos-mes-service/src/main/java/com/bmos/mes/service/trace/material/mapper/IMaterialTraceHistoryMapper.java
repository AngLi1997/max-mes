package com.bmos.mes.service.trace.material.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.plan.info.dto.PlanRetraceInfoPageDTO;
import com.bmos.mes.service.trace.material.entity.MaterialTraceHistoryDO;
import com.bmos.mes.service.trace.material.enums.MaterialTraceType;
import com.bmos.mybatis.mapper.BaseMapperX;
import io.jsonwebtoken.lang.Collections;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/21 10:17
 */
@Mapper
public interface IMaterialTraceHistoryMapper extends BaseMapperX<MaterialTraceHistoryDO> {

    default List<MaterialTraceHistoryDO> queryPage(PlanRetraceInfoPageDTO dto){
        return selectList(new LambdaQueryWrapper<MaterialTraceHistoryDO>()
                .eq(MaterialTraceHistoryDO::getProductPlanId, dto.getPlanId())
                .orderByAsc(MaterialTraceHistoryDO::getOperateTime)
        );
    }

    List<MaterialTraceHistoryDO> queryTraceHistory(@Param("productPlanIds") Collection<Long> productPlanIds,
                                                   @Param("procedureStepIds") Collection<Long> procedureStepIds,
                                                   @Param("materialIds") Collection<Long> materialIds);

    default List<MaterialTraceHistoryDO> selectOutputHistoryByStorageMaterialIds(List<Long> storageMaterialIdList){
        if (Collections.isEmpty(storageMaterialIdList)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<MaterialTraceHistoryDO>()
                .in(MaterialTraceHistoryDO::getStorageMaterialId, storageMaterialIdList)
                .eq(MaterialTraceHistoryDO::getTraceType, MaterialTraceType.OUTPUT)
        );
    }
}
