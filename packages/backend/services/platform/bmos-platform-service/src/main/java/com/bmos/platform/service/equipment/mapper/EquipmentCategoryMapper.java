package com.bmos.platform.service.equipment.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.equipment.model.EquipmentCategory;
import com.bmos.platform.service.equipment.model.EquipmentInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipmentCategoryMapper extends BaseMapperX<EquipmentCategory> {

    default List<EquipmentCategory> selectCategoryList() {
        return selectList(new LambdaQueryWrapperX<>());
    }

    default Boolean saveOrUpdateCategory(EquipmentCategory category){
        return Db.saveOrUpdate(category);
    }

    default Boolean deleteCategoryById(Long id){
        return Db.removeById(id, EquipmentCategory.class);
    }
}
