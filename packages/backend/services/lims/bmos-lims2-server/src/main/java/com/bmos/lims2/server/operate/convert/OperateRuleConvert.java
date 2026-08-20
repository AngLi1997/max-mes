package com.bmos.lims2.server.operate.convert;

import com.bmos.lims2.server.operate.dto.SaveOperateRuleDTO;
import com.bmos.lims2.server.operate.model.OperateRule;
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
