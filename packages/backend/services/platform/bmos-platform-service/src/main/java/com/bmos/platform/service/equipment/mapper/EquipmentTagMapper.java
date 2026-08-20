package com.bmos.platform.service.equipment.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.equipment.model.EquipmentTag;
import com.bmos.platform.service.equipment.model.EquipmentTagInfo;
import com.bmos.platform.service.equipment.service.dto.EquipmentTagDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper
public interface EquipmentTagMapper extends BaseMapperX<EquipmentTag> {

    /**
     * 查询所有标签
     * @return
     */
    default List<EquipmentTag> selectAllTag(){
        return selectList(new LambdaQueryWrapperX<>());
    }

    /**
     * 根据id集合查询
     * @param idList
     * @return
     */
    default List<EquipmentTag> selectByIdList(Collection<Long> idList){
        if (CollectionUtil.isEmpty(idList)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<EquipmentTag>().in(EquipmentTag::getId, idList));
    }

    default EquipmentTag selectByTagCode(String tagCode){
        return selectOne(new LambdaQueryWrapperX<EquipmentTag>().eq(EquipmentTag::getCode, tagCode));
    }

    /**
     * 递归查询所有子级
     * @param id 当前级别的id
     * @return 查询结果
     */
    List<EquipmentTag> selectChildren(@Param("parentId") Long id);
}
