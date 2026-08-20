package com.bmos.mes.service.inspect.mapper;

import com.bmos.mes.service.inspect.model.InspectConfigData;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 请验单配置数据表(BmInspectConfigData)实体类
 *
 * @author makejava
 * @since 2025-02-13 13:42:21
 */
@Mapper
public interface InspectConfigDataMapper extends BaseMapperX<InspectConfigData> {

    /**
     * 根据请验单id删除请验单配置数据
     * @param configId
     */
    default void deleteByConfigId(Long configId){
        delete(new LambdaQueryWrapperX<InspectConfigData>()
                .eq(InspectConfigData::getConfigId, configId));
    }

    /**
     * 根据请验单id查询请验单配置数据
     * @param configId
     * @return
     */
    default List<InspectConfigData> selectByConfigId(Long configId){
        return selectList(new LambdaQueryWrapperX<InspectConfigData>()
                .eq(InspectConfigData::getConfigId, configId));
    }
}

