package com.bmos.wms.service.inspect.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.wms.service.inspect.model.InspectResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * WMS 检验结论 Mapper。
 */
@Mapper
public interface InspectResultMapper extends BaseMapperX<InspectResult> {

    default List<InspectResult> selectByInspectId(Long inspectId) {
        return selectList(new LambdaQueryWrapperX<InspectResult>()
                .eq(InspectResult::getInspectId, inspectId));
    }

    default int deleteByInspectId(Long inspectId) {
        return delete(new LambdaQueryWrapperX<InspectResult>()
                .eq(InspectResult::getInspectId, inspectId));
    }
}
