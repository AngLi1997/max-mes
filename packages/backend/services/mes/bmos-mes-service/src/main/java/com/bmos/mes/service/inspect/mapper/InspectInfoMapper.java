package com.bmos.mes.service.inspect.mapper;

import com.bmos.mes.service.inspect.model.InspectInfo;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 请验单信息表(BmInspectInfo)表数据库访问层
 *
 * @author makejava
 * @since 2025-02-17 18:26:44
 */
@Mapper
public interface InspectInfoMapper extends BaseMapperX<InspectInfo> {

    default List<InspectInfo> selectByInspectId(Long inspectId){
        return selectList(new LambdaQueryWrapperX<InspectInfo>()
                .eq(InspectInfo::getInspectId, inspectId));
    }

    default void deleteByInspectId(Long inspectId){
        delete(new LambdaQueryWrapperX<InspectInfo>()
                .eq(InspectInfo::getInspectId, inspectId));
    }
}

