package com.bmos.platform.service.equipment.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.equipment.model.EquipmentTagProperty;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 设备标签属性持久层
 */
@Mapper
public interface EquipmentTagPropertyMapper extends BaseMapperX<EquipmentTagProperty> {

    /**
     * 根据tagid查询设备标签属性
     * @param tagIdList
     * @return
     */
    default List<EquipmentTagProperty> selectByTagIdList(List<Long> tagIdList){
        return selectList(new LambdaQueryWrapperX<EquipmentTagProperty>()
                .in(EquipmentTagProperty::getTagId, tagIdList));
    }

    /**
     * 判断属性code是否存在
     * @param propertyCodeSet
     * @return
     */
    default boolean existsByCodeList(Collection<String> propertyCodeSet){
        return exists(new LambdaQueryWrapperX<EquipmentTagProperty>()
                .in(EquipmentTagProperty::getCode, propertyCodeSet));
    }
}
