package com.bmos.mes.service.storage.manage.mapper;

import com.bmos.mes.common.enums.storage.ChargeRecycleTypeEnum;
import com.bmos.mes.service.storage.manage.model.StorageMaterialChargeRecycle;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface IStorageMaterialChargeRecycleMapper extends BaseMapperX<StorageMaterialChargeRecycle> {

    default List<StorageMaterialChargeRecycle> selectByChargeRecycleId(Long id,
                                                                       ChargeRecycleTypeEnum chargeRecycleType) {
        return selectList(new LambdaQueryWrapperX<StorageMaterialChargeRecycle>()
                .eq(StorageMaterialChargeRecycle::getChargeRecycleComponentId, id)
                .eqIfPresent(StorageMaterialChargeRecycle::getOperationType, chargeRecycleType)
                .orderByAsc(StorageMaterialChargeRecycle::getStorageMaterialNo));
    }

    default List<StorageMaterialChargeRecycle> selectByChargeRecycleIds(List<Long> ids,
                                                                       ChargeRecycleTypeEnum chargeRecycleType) {
        return selectList(new LambdaQueryWrapperX<StorageMaterialChargeRecycle>()
                .in(StorageMaterialChargeRecycle::getChargeRecycleComponentId, ids)
                .eqIfPresent(StorageMaterialChargeRecycle::getOperationType, chargeRecycleType)
                .orderByAsc(StorageMaterialChargeRecycle::getStorageMaterialNo));
    }

    default BigDecimal selectChargeQuantity(Long chargeRecycleComponentId, Long materialBatchId) {
        List<StorageMaterialChargeRecycle> chargeRecycleList =
                selectList(new LambdaQueryWrapperX<StorageMaterialChargeRecycle>()
                        .eq(StorageMaterialChargeRecycle::getChargeRecycleComponentId, chargeRecycleComponentId)
                        .eq(StorageMaterialChargeRecycle::getMaterialBatchId, materialBatchId)
                        .eq(StorageMaterialChargeRecycle::getOperationType, ChargeRecycleTypeEnum.CHARGE));
        return chargeRecycleList.stream().map(StorageMaterialChargeRecycle::getQuantity).reduce(BigDecimal.ZERO,
                BigDecimal::add);
    }

    default BigDecimal selectRecycleQuantity(Long chargeRecycleComponentId, Long materialBatchId) {
        List<StorageMaterialChargeRecycle> chargeRecycleList =
                selectList(new LambdaQueryWrapperX<StorageMaterialChargeRecycle>()
                        .eq(StorageMaterialChargeRecycle::getChargeRecycleComponentId, chargeRecycleComponentId)
                        .eq(StorageMaterialChargeRecycle::getMaterialBatchId, materialBatchId)
                        .eq(StorageMaterialChargeRecycle::getOperationType, ChargeRecycleTypeEnum.RECYCLE));
        return chargeRecycleList.stream().map(StorageMaterialChargeRecycle::getQuantity).reduce(BigDecimal.ZERO,
                BigDecimal::add);
    }

    default boolean existedChargedMaterial(Long chargeRecycleId, String no){
        return exists(new LambdaQueryWrapperX<StorageMaterialChargeRecycle>()
                .eq(StorageMaterialChargeRecycle::getChargeRecycleComponentId, chargeRecycleId)
                .eq(StorageMaterialChargeRecycle::getStorageMaterialNo, no)
                .eq(StorageMaterialChargeRecycle::getOperationType, ChargeRecycleTypeEnum.CHARGE)
        );
    }
}
