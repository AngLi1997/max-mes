package com.bmos.mes.service.operate.convert;

import com.bmos.mes.service.operate.dto.SaveOperateRuleDTO;
import com.bmos.mes.service.operate.model.OperateRule;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author renjinguang
 */
@Mapper
public interface OperateRuleConvert {
    OperateRuleConvert INSTANCE = Mappers.getMapper(OperateRuleConvert.class);

    OperateRule convertToSave(SaveOperateRuleDTO dto);
}
