package com.bmos.platform.service.equipment.convert;

import com.bmos.platform.service.equipment.controller.vo.CategoryTreeNodeVO;
import com.bmos.platform.service.equipment.model.EquipmentCategory;
import com.bmos.platform.service.equipment.service.dto.CategorySaveDTO;
import com.bmos.platform.service.equipment.service.dto.CategoryUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface EquipmentCategoryConvert {

    EquipmentCategoryConvert INSTANCE = Mappers.getMapper(EquipmentCategoryConvert.class);

    EquipmentCategory convertToCategory(CategorySaveDTO dto);

    EquipmentCategory convertToUpdateCategory(CategoryUpdateDTO dto);

    List<CategoryTreeNodeVO> convertToTreeVo(List<EquipmentCategory> list);
}
