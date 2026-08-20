package com.bmos.mes.service.preparation.measure.mapper;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.preparation.measure.dto.LiquidMeasureLogPageQueryDTO;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureLog;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;

@Mapper
public interface LiquidPreparationMeasureLogMapper extends BaseMapperX<LiquidPreparationMeasureLog> {


    default List<LiquidPreparationMeasureLog> selectPageByParam(LiquidMeasureLogPageQueryDTO dto) {
        LambdaQueryWrapperX<LiquidPreparationMeasureLog> query = new LambdaQueryWrapperX<>();
        query.orderByDesc(LiquidPreparationMeasureLog::getMeasureTime);
        query.eq(dto.getCategoryType() != null, LiquidPreparationMeasureLog::getMaterialType, dto.getCategoryType());
        query.eq(dto.getMaterialId() != null, LiquidPreparationMeasureLog::getMaterialId, dto.getMaterialId());
        query.like(StrUtil.isNotEmpty(dto.getMaterialBatchNo()), LiquidPreparationMeasureLog::getMaterialBatchNo,
                dto.getMaterialBatchNo());
        query.like(StrUtil.isNotEmpty(dto.getMaterialNo()), LiquidPreparationMeasureLog::getMaterialNo,
                dto.getMaterialNo());
        query.eq(dto.getMeasureType() != null , LiquidPreparationMeasureLog::getMeasureType, dto.getMeasureType());
        query.like(StrUtil.isNotEmpty(dto.getProductInfo()), LiquidPreparationMeasureLog::getProductMergeCode, dto.getProductInfo());
        query.like(StrUtil.isNotEmpty(dto.getProductInfo()), LiquidPreparationMeasureLog::getProductName, dto.getProductInfo());
        query.gt(StrUtil.isNotEmpty(dto.getStartTime()), LiquidPreparationMeasureLog::getMeasureTime, dto.getStartTime() + " 00:00:00");
        query.lt(StrUtil.isNotEmpty(dto.getEndTime()), LiquidPreparationMeasureLog::getMeasureTime, dto.getStartTime() + " 23:59:59") ;
        return selectList(query);
    }
}
