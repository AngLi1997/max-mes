package com.bmos.platform.service.equipment.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.equipment.model.EquipmentPropertyInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface EquipmentPropertyInfoMapper extends BaseMapperX<EquipmentPropertyInfo> {

    /**
     * 判断设备id下的属性code是否重复
     *
     * @param propertyCodeSet
     * @param equipmentId
     * @return
     */
    default boolean selectExistByPropertyCodeList(Collection<String> propertyCodeSet, Long equipmentId) {
        return exists(new LambdaQueryWrapperX<EquipmentPropertyInfo>()
                .eq(EquipmentPropertyInfo::getEquipmentId, equipmentId)
                .in(EquipmentPropertyInfo::getPropertyCode, propertyCodeSet));
    }

    default List<EquipmentPropertyInfo> selectByEquipmentId(Long equipmentId, Integer propertyType) {
        return selectList(new LambdaQueryWrapperX<EquipmentPropertyInfo>()
                .eq(EquipmentPropertyInfo::getEquipmentId, equipmentId)
                .eq(EquipmentPropertyInfo::getPropertyType, propertyType));
    }

    /**
     * 删除设备与属性之间的关联关系
     *
     * @param equipmentId
     */
    default void deleteByEquipmentId(Long equipmentId) {
        delete(new LambdaQueryWrapperX<EquipmentPropertyInfo>()
                .eq(EquipmentPropertyInfo::getEquipmentId, equipmentId));
    }

    default List<EquipmentPropertyInfo> selectPropertyInfoListByEquipmentId(Long equipmentId) {
        return selectList(new LambdaQueryWrapperX<EquipmentPropertyInfo>()
                .eq(EquipmentPropertyInfo::getEquipmentId, equipmentId));
    }

    default List<EquipmentPropertyInfo> queryEquipmentPropertyByEquipmentIdListAndType(List<Long> equipmentIdList, Integer propertyType){
        return selectList(new LambdaQueryWrapperX<EquipmentPropertyInfo>()
                .in(EquipmentPropertyInfo::getEquipmentId, equipmentIdList)
                .eq(EquipmentPropertyInfo::getPropertyType, propertyType)
                .groupBy(EquipmentPropertyInfo::getPropertyCode));
    }

    default List<EquipmentPropertyInfo> selectAllByFinisehd(Integer propertyType, Boolean finishStatus){
        return selectList(new LambdaQueryWrapperX<EquipmentPropertyInfo>()
                .eq(EquipmentPropertyInfo::getFinishStatus, finishStatus)
                .eq(EquipmentPropertyInfo::getPropertyType, propertyType));
    }
}
