package com.bmos.lims2.server.eln.record.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponentDetail;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface BatchRecordComponentDetailMapper extends BaseMapperX<BatchRecordComponentDetail> {

    default void deleteFormula(Long componentId){
        update(null, new LambdaUpdateWrapper<BatchRecordComponentDetail>()
                .eq(BatchRecordComponentDetail::getId, componentId)
                .set(BatchRecordComponentDetail::getFormulaField, null)
                .set(BatchRecordComponentDetail::getFormulaConfig, null));
    }
}
