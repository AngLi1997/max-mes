package com.bmos.mes.service.plan.template.convert;

import com.bmos.mes.service.plan.template.dto.PlanTemplateBatchDTO;
import com.bmos.mes.service.plan.template.dto.PlanTemplateProcedureConfigDTO;
import com.bmos.mes.service.plan.template.model.PlanTemplate;
import com.bmos.mes.service.plan.template.model.PlanTemplateBatch;
import com.bmos.mes.service.plan.template.model.PlanTemplateBatchProcedure;
import com.bmos.mes.service.plan.template.vo.PlanTemplateListVO;
import com.bmos.mes.service.plan.template.vo.PlanTemplatePageVO;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PlanTemplateConverter {

    PlanTemplateConverter INSTANCE = Mappers.getMapper(PlanTemplateConverter.class);


    @Mapping(target = "relationProcessesList",ignore = true)
    PlanTemplateBatch convertTemplateBatch(PlanTemplateBatchDTO e);

    List<PlanTemplateBatchProcedure> convertTemplateBatchProcedure(List<PlanTemplateProcedureConfigDTO> procedureDurationList);

    List<PlanTemplatePageVO> convertTemplatePage(List<PlanTemplate> list);

    List<PlanTemplateListVO> convert2TemplateListVO(List<PlanTemplate> list);

    CommonPage<PlanTemplatePageVO> convertTemplatePage(CommonPage<PlanTemplate> commonPage);
}
