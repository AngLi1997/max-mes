package com.bmos.mes.service.operate.convert;

import com.bmos.mes.service.operate.dto.SaveCategoryDTO;
import com.bmos.mes.service.operate.dto.UpdateCategoryDTO;
import com.bmos.mes.service.operate.model.OperateRuleCategory;
import com.bmos.mes.service.operate.vo.OperateRuleCategoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface OperateRuleCategoryConvert {
    OperateRuleCategoryConvert INSTANCE = Mappers.getMapper(OperateRuleCategoryConvert.class);

    List<OperateRuleCategoryVO> convertToVoList(List<OperateRuleCategory> list);

    OperateRuleCategory convertToSave(SaveCategoryDTO dto);

    OperateRuleCategory convertToUpdate(UpdateCategoryDTO dto);
}
