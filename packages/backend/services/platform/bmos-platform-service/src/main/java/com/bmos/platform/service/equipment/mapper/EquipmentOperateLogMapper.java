package com.bmos.platform.service.equipment.mapper;

import cn.hutool.core.util.StrUtil;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.common.enums.equipment.OperateLogFillingStatusEnum;
import com.bmos.platform.service.equipment.model.EquipmentOperateLog;
import com.bmos.platform.service.equipment.service.dto.EquipmentOperateLogPageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;

@Mapper
public interface EquipmentOperateLogMapper extends BaseMapperX<EquipmentOperateLog> {
    default List<EquipmentOperateLog> selectPageByParam(EquipmentOperateLogPageDTO dto){
        LambdaQueryWrapperX<EquipmentOperateLog> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.orderByDesc(EquipmentOperateLog::getBeginTime);
        if (StrUtil.isNotEmpty(dto.getEquipmentName())){
            wrapperX.like(EquipmentOperateLog::getEquipmentName, dto.getEquipmentName());
        }
        if (StrUtil.isNotEmpty(dto.getEquipmentCode())){
            wrapperX.like(EquipmentOperateLog::getEquipmentCode, dto.getEquipmentCode());
        }
        if (StrUtil.isNotEmpty(dto.getBatchNo())){
            wrapperX.like(EquipmentOperateLog::getBatchNo, dto.getBatchNo());
        }

        if (StrUtil.isNotEmpty(dto.getProductName())){
            wrapperX.like(EquipmentOperateLog::getProductName, dto.getProductName());
        }
        if (Objects.nonNull(dto.getOperateBeginTime())){
            wrapperX.ge(EquipmentOperateLog::getBeginTime, dto.getOperateBeginTime() + " 00:00:00");
        }
        if (Objects.nonNull(dto.getOperateEndTime())){
            wrapperX.le(EquipmentOperateLog::getBeginTime, dto.getOperateEndTime() + " 23:59:59");
        }
        if (StrUtil.isNotEmpty(dto.getChangeType())){
            wrapperX.eq(EquipmentOperateLog::getChangeType, dto.getChangeType());
        }
        return selectList(wrapperX);
    }

    default EquipmentOperateLog selectIncompleteFillingLog(Long equipmentId){
        return selectOne(new LambdaQueryWrapperX<EquipmentOperateLog>()
                .eq(EquipmentOperateLog::getEquipmentId, equipmentId)
                .eq(EquipmentOperateLog::getFillStatus, OperateLogFillingStatusEnum.INCOMPLETE_FILLING));
    }
}
