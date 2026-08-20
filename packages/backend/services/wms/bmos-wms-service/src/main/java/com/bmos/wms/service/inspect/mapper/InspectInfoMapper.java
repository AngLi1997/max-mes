package com.bmos.wms.service.inspect.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.wms.service.inspect.model.InspectInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * WMS 请验单字段值 Mapper。
 */
@Mapper
public interface InspectInfoMapper extends BaseMapperX<InspectInfo> {

    default List<InspectInfo> selectByInspectId(Long inspectId) {
        return selectList(new LambdaQueryWrapperX<InspectInfo>()
                .eq(InspectInfo::getInspectId, inspectId)
                .orderByAsc(InspectInfo::getSort));
    }

    default List<InspectInfo> selectByInspectIds(Collection<Long> inspectIds) {
        return selectList(new LambdaQueryWrapperX<InspectInfo>()
                .in(InspectInfo::getInspectId, inspectIds));
    }
}
