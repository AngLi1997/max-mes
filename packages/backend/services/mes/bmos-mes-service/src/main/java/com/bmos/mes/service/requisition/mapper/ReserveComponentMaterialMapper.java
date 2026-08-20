package com.bmos.mes.service.requisition.mapper;

import com.bmos.mes.service.record.business.model.StorageMaterialDetailInfo;
import com.bmos.mes.service.requisition.model.ReserveComponentMaterial;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReserveComponentMaterialMapper extends BaseMapperX<ReserveComponentMaterial> {


    default List<ReserveComponentMaterial> selectByStorageMaterialIdAndInstanceId(Long storageMaterialId, Long instanceId) {
        return selectList(new LambdaQueryWrapperX<ReserveComponentMaterial>()
                .eq(ReserveComponentMaterial::getStorageMaterialId, storageMaterialId)
                .eq(ReserveComponentMaterial::getInstanceId, instanceId));
    }

    default List<ReserveComponentMaterial> selectByInstanceId(Long componentInstanceId) {
        return selectList(new LambdaQueryWrapperX<ReserveComponentMaterial>()
                .eq(ReserveComponentMaterial::getInstanceId, componentInstanceId));
    }

    List<StorageMaterialDetailInfo> selectBatchListByInstanceId(@Param("instanceId") Long instanceId, @Param("productPlanId") Long productPlanId);

}
