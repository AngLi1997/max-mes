package com.bmos.mes.service.trace.material.convert;

import com.bmos.mes.service.trace.material.entity.MaterialTraceTemplateProcedureStepDO;
import com.bmos.mes.service.trace.material.vo.MaterialTraceTemplateDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/20 17:40
 */
@Mapper
public interface MaterialTraceTemplateProcedureStepConverter {

    MaterialTraceTemplateProcedureStepConverter INSTANCE = Mappers.getMapper(MaterialTraceTemplateProcedureStepConverter.class);

    MaterialTraceTemplateDetailVO.ProcedureStepVO convertToVO(MaterialTraceTemplateProcedureStepDO procedureStepDO);
}
