package com.bmos.platform.service.equipment.mapper;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import com.bmos.platform.service.equipment.model.EquipmentStatusLog;
import com.bmos.platform.service.equipment.service.dto.EquipmentStatusLogPageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;

@Mapper
public interface EquipmentStatusLogMapper extends BaseMapperX<EquipmentStatusLog> {
    default List<EquipmentStatusLog> selectPageByParam(EquipmentStatusLogPageDTO dto){
        LambdaQueryWrapperX<EquipmentStatusLog> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.orderByDesc(EquipmentStatusLog::getOperateTime);
        if (StrUtil.isNotEmpty(dto.getEquipmentName())){
            wrapperX.like(EquipmentStatusLog::getEquipmentName,dto.getEquipmentName());
        }
        if (StrUtil.isNotEmpty(dto.getEquipmentCode())){
            wrapperX.like(EquipmentStatusLog::getEquipmentCode,dto.getEquipmentCode());
        }
        if (StrUtil.isNotEmpty(dto.getOperateName())){
            wrapperX.like(EquipmentStatusLog::getOperateName,dto.getOperateName());
        }
        if (StrUtil.isNotEmpty(dto.getChangeType())){
            wrapperX.eq(EquipmentStatusLog::getChangeType, CommonEnum.getEnumByValue(EquipmentStatusLogChangeType.class, dto.getChangeType()));
        }
        if (Objects.nonNull(dto.getOperateBeginTime())){
            wrapperX.ge(EquipmentStatusLog::getOperateTime, dto.getOperateBeginTime() + " 00:00:00");
        }
        if (Objects.nonNull(dto.getOperateEndTime())){
            wrapperX.le(EquipmentStatusLog::getOperateTime, dto.getOperateEndTime() + " 23:59:59");
        }
        return selectList(wrapperX);
    }
}
