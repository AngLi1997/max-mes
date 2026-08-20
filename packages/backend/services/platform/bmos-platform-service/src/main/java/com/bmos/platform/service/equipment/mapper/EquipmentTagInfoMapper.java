package com.bmos.platform.service.equipment.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.equipment.model.EquipmentTagInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipmentTagInfoMapper extends BaseMapperX<EquipmentTagInfo> {
    default List<EquipmentTagInfo> selectByEquipmentId(Long equipmentId){
        return selectList(new LambdaQueryWrapperX<EquipmentTagInfo>()
                .eq(EquipmentTagInfo::getEquipmentId, equipmentId));
    }

    default List<EquipmentTagInfo> selectByTagCode(Long tagId){
        return selectList(new LambdaQueryWrapperX<EquipmentTagInfo>()
                .eq(EquipmentTagInfo::getTagId, tagId));
    }

    /**
     * 删除与标签之间的关联关系
     * @param equipmentId
     */
    default void deleteByEquipmentId(Long equipmentId){
        delete(new LambdaQueryWrapperX<EquipmentTagInfo>().eq(EquipmentTagInfo::getEquipmentId, equipmentId));
    }
}
