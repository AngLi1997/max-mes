package com.bmos.lims2.server.operate.convert;

import com.bmos.lims2.server.operate.dto.SaveCategoryDTO;
import com.bmos.lims2.server.operate.dto.UpdateCategoryDTO;
import com.bmos.lims2.server.operate.model.OperateRuleCategory;
import com.bmos.lims2.server.operate.vo.OperateRuleCategoryVO;
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
