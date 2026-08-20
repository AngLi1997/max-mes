package com.bmos.mes.service.inspect.mapper;

import com.bmos.mes.service.inspect.model.InspectConfigMaterial;
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
public interface InspectConfigMaterialMapper extends BaseMapperX<InspectConfigMaterial> {

    /**
     * 接触物料绑定关系
     * @param configId
     */
    default void deleteByConfigId(Long configId){
        delete(new LambdaQueryWrapperX<InspectConfigMaterial>()
                .eq(InspectConfigMaterial::getConfigId,configId));
    }

    /**
     * 删除物料绑定关系
     * @param materialIds
     */
    default void deleteByMaterialIds(List<Long> materialIds){
        delete(new LambdaQueryWrapperX<InspectConfigMaterial>()
                .in(InspectConfigMaterial::getMaterialId,materialIds));
    }

    default List<InspectConfigMaterial> selectBindMaterial(Long configId){
        return selectList(new LambdaQueryWrapperX<InspectConfigMaterial>()
                .eq(InspectConfigMaterial::getConfigId,configId));
    }

    default InspectConfigMaterial selectByMaterialId(Long materialId){
        return selectOne(new LambdaQueryWrapperX<InspectConfigMaterial>()
                .eq(InspectConfigMaterial::getMaterialId,materialId));
    }

    default List<InspectConfigMaterial> selectByConfigId(Long inspectConfigId){
        return selectList(new LambdaQueryWrapperX<InspectConfigMaterial>()
                .eq(InspectConfigMaterial::getConfigId,inspectConfigId));
    }
}

