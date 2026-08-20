package com.bmos.mes.service.plan.info.convert;

import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import com.bmos.mes.service.exception.dto.BatchExceptionQueryDTO;
import com.bmos.mes.service.exception.vo.ExceptionPageVO;
import com.bmos.mes.service.plan.info.dto.*;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.vo.*;
import com.bmos.mes.service.platform.plan.dto.BatchConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.plan.dto.ConfirmNextUseCodeDTO;
import com.bmos.mes.service.workflow.dto.StartWorkflowDTO;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mapper
public interface PlanConverter {
    PlanConverter INSTANCE = Mappers.getMapper(PlanConverter.class);

    Plan convertDO(PlanSaveDTO dto);

    Plan convertDO(PlanBatchSaveDTO dto);

    Plan convertDO(PlanUpdateDTO dto);

    PlanDetailVO convertVO(Plan plan);

    @Mapping(target = "productPlanId", source = "id")
    StartWorkflowDTO convertVO2(Plan plan);

    default Function<PlanBatchSaveDetailDTO, Plan> getNewPlan(Plan plan) {
        return detail -> plan
            .withPlanNo(detail.getPlanNo())
            .withBatchNo(detail.getBatchNo())
            .withProductDate(detail.getProductDate())
            .withType(ProductPlanTypeEnum.valueOf(detail.getType()))
            .withBatchQuantity(detail.getBatchQuantity())
            .withUnitId(detail.getUnitId());
    }

    default List<Plan> convertListDO(PlanBatchSaveDTO dto) {
        Plan plan = convertDO(dto);
        return dto.getDetails().stream()
            .map(getNewPlan(plan))
            .collect(Collectors.toList());
    }

    PlanCodeRuleVO convertVO(PlanSaveDTO dto);

    PlanCodeRuleVO convertVO(PlanBatchSaveDTO dto);

    @Mapping(target = "productPlanId", source = "id")
    ProductPlanRelationListVO convertVO3(Plan dto);

    default List<PlanAuditPageVO> convertList(List<TaskListResp> taskListResps, Map<String, Plan> planMapKeyIsProcessInstanceId) {
        return taskListResps.stream()
            .map(element -> convertVO(element, planMapKeyIsProcessInstanceId.get(element.getProcessInstanceId())))
            .collect(Collectors.toList());
    }

    default PlanAuditPageVO convertVO(TaskListResp taskListResp, Plan plan) {
        PlanAuditPageVO planAuditPageVO = convertVO(taskListResp);
        convertVO(planAuditPageVO, plan);
        return planAuditPageVO;
    }

    PlanAuditPageVO convertVO(TaskListResp taskListResp);

    void convertVO(@MappingTarget PlanAuditPageVO vo, Plan plan);

    List<PlanEasyInfoVO> convert2EasyVO(List<Plan> planList);

    List<PlanSimpleVO> convertToSimpleVO(List<Plan> plans);

    List<PlanListVO> convert2PlanListVO(List<Plan> plans);

    List<Plan> convertDO(List<PlanSaveDTO> saveDTOS);

    List<PlanEasyInfoVO> convert2PlanEasyInfoVO(List<Plan> plans);

    @Mapping(target = "productPlanId", source = "planId")
    BatchExceptionQueryDTO convert2BatchExceptionQueryDTO(PlanRetraceInfoPageDTO dto);

    CommonPage<PlanRetraceDeviationPageVO> convert2DeviationPageVO(CommonPage<ExceptionPageVO> batchExceptionPage);

    PlanTraceablePageDTO convert2PlanTraceablePageDTO(ProductPlanBatchDTO dto);

    CommonPage<ProductPlanBatchPageVO> convert2BatchRetracePage(CommonPage<PlanPageVO> planPageVOCommonPage);

    PlanRetraceInfoVO convert2PlanRetraceInfoVO(Plan plan);
}
