package com.bmos.mes.service.storage.log.mapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.common.enums.storage.StorageOperateTypeShowEnum;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogPageQuery;
import com.bmos.mes.service.storage.log.model.StorageMaterialPositionLog;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 货位日志mapper
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/18 14:24
 */
@Mapper
public interface IStorageMaterialPositionLogMapper extends BaseMapperX<StorageMaterialPositionLog> {

    /**
     * 根据暂存间id查询货位日志
     *
     * @param storageId 暂存间id
     * @return 货位日志列表
     */
    default List<StorageMaterialPositionLog> queryListByStorageId(Long storageId) {
        return selectList(Wrappers.lambdaQuery(StorageMaterialPositionLog.class)
                .eq(StorageMaterialPositionLog::getStorageId, storageId)
        );
    }

    /**
     * 根据货位id查询货位日志
     *
     * @param cargoPositionId 货位id
     * @return
     */
    default List<StorageMaterialPositionLog> queryListByCargoPositionId(Long cargoPositionId) {
        return selectList(Wrappers.lambdaQuery(StorageMaterialPositionLog.class)
                .eq(StorageMaterialPositionLog::getMaterialPositionId, cargoPositionId)
        );
    }

    default List<StorageMaterialPositionLog> queryPage(StorageMaterialPositionLogPageQuery dto, Collection<Long> positionIds) {
        if (CollectionUtil.isEmpty(positionIds)) {
            return new ArrayList<>();
        }
        List<StorageOperateTypeEnum> opTypes = new ArrayList<>();
        if (StrUtil.isNotBlank(dto.getOperationType())){
            opTypes = Optional.ofNullable(StorageOperateTypeShowEnum.getByName(dto.getOperationType()))
                    .map(StorageOperateTypeShowEnum::getOperate)
                    .map(StorageOperateTypeEnum::getByName)
                    .orElse(new ArrayList<>());
            if (CollectionUtil.isEmpty(opTypes)) {
                return new ArrayList<>();
            }
        }
        return selectList(Wrappers.lambdaQuery(StorageMaterialPositionLog.class)
                .like(StrUtil.isNotBlank(dto.getMaterialBatchNo()), StorageMaterialPositionLog::getMaterialBatchNo, dto.getMaterialBatchNo())
                .in(StorageMaterialPositionLog::getMaterialPositionId, positionIds)
                .like(StrUtil.isNotBlank(dto.getProductBatchNo()), StorageMaterialPositionLog::getProductBatchNo, dto.getProductBatchNo())
                .like(StrUtil.isNotBlank(dto.getMaterialNo()), StorageMaterialPositionLog::getMaterialNo, dto.getMaterialNo())
                .like(StrUtil.isNotBlank(dto.getProductName()), StorageMaterialPositionLog::getProductName, dto.getProductName())
                .like(StrUtil.isNotBlank(dto.getProductCode()), StorageMaterialPositionLog::getProductCode, dto.getProductCode())
                .in(CollectionUtil.isNotEmpty(opTypes), StorageMaterialPositionLog::getOperationType, opTypes)
                .between(dto.getStartDate() != null && dto.getEndDate() != null, StorageMaterialPositionLog::getOperateTime, dto.getStartDate().atStartOfDay(), dto.getEndDate().plusDays(1).atStartOfDay().plusSeconds(-1))
                .and(StrUtil.isNotBlank(dto.getMaterialKeyWords()), i -> i
                        .like(StorageMaterialPositionLog::getMaterialCode, dto.getMaterialKeyWords())
                        .or()
                        .like(StorageMaterialPositionLog::getMaterialName, dto.getMaterialKeyWords()))
                .orderByDesc(StrUtil.isEmpty(dto.getOrderBy()), StorageMaterialPositionLog::getCreateTime)
                .orderByDesc(StrUtil.isEmpty(dto.getOrderBy()), StorageMaterialPositionLog::getMaterialId)
                .orderByDesc(StrUtil.isEmpty(dto.getOrderBy()), StorageMaterialPositionLog::getMaterialNo)
        );
    }
}
