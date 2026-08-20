package com.bmos.platform.service.equipment.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.equipment.model.EquipmentTagUseTemplate;
import com.bmos.platform.service.equipment.service.dto.EquipmentTagUseTemplateDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipmentUseTemplateMapper extends BaseMapperX<EquipmentTagUseTemplate> {

   default List<EquipmentTagUseTemplate> selectByTagIds(List<Long> tagIdList){
       LambdaQueryWrapper<EquipmentTagUseTemplate> lambda = new QueryWrapper<EquipmentTagUseTemplate>().lambda();
       lambda.in(EquipmentTagUseTemplate::getTagId, tagIdList);
       return selectList(lambda);
   }
}
