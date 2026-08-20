package com.bmos.mes.service.plan.production.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.util.id.IdUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.plan.CodeRuleTypeEnum;
import com.bmos.mes.common.enums.plan.ProductPlanInstructStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import com.bmos.mes.service.plan.info.dto.PlanSaveDTO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.production.dto.*;
import com.bmos.mes.service.plan.production.model.ProductionPlan;
import com.bmos.mes.service.plan.production.model.ProductionPlanItem;
import com.bmos.mes.service.plan.production.vo.*;
import com.bmos.mes.service.plan.template.vo.PlanTemplateDetailBatchVO;
import com.bmos.mes.service.plan.template.vo.PlanTemplateProcedureVO;
import com.bmos.mes.service.process.vo.ProcessDetailVO;
import com.bmos.mes.service.product.vo.ProductMaterialDetailVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Mapper
public interface ProductionPlanConverter {

    ProductionPlanConverter INSTANCE = Mappers.getMapper(ProductionPlanConverter.class);

    CommonPage<ProductionPlanPageVO> convertToPageVo(CommonPage<ProductionPlan> plans);

    List<PlanBatchNextNoVO> convertToBatchNextNo(List<PlanBatchNoDTO> dto);

    default List<PlanBatchNextNoVO> convertToBatchNextNoList(List<PlanBatchNoDTO> dto, Map<Long, Map<CodeRuleTypeEnum, String>> codeRuleMap) {
        List<PlanBatchNextNoVO> vos = convertToBatchNextNo(dto);
        AtomicInteger sort = new AtomicInteger(1);
        vos.forEach(item -> {
            item.setKey(sort.getAndIncrement());
            Map<CodeRuleTypeEnum, String> code = codeRuleMap.get(item.getProcessId());
            if (CollUtil.isEmpty(code)) {
                return;
            }
            String planNoCode = code.get(CodeRuleTypeEnum.PRODUCT_PLAN_NO);
            item.setPlanNoCode(Optional.ofNullable(planNoCode).orElse(null));
            //不等于复用
            String batchPlanNo = code.get(CodeRuleTypeEnum.PRODUCT_PLAN_BATCH_NO);
            item.setBatchNoCode(Optional.ofNullable(batchPlanNo).orElse(null));
           /* int relationNumber = item.getRelationBatchSortList().size();
            if (CollUtil.isNotEmpty(item.getBatchNoList())) {
                relationNumber = relationNumber + item.getBatchNoList().size();
            }
            List<PlanBatchNoDTO> dtoList = CollectionUtils.filterList(dto, items -> item.getRelationBatchSortList().contains(items.getSort()));
            if (!item.getReuseBatchNumber() || relationNumber != 1 || CollUtil.isEmpty(dtoList)){
                item.setBatchNoCode(Optional.ofNullable(batchPlanNo).orElse(null));
                return;
            }*/
            item.setIsFlay(false);
        });
        return vos;
    }

    /**
     * 构建工艺数据
     *
     * @param template      模板详情
     * @param batchNumber   批量
     * @param duration      间隔时长
     * @param planFirstDate 首批生产日期
     * @return
     */
    default ProductionPlanItemDetailVO convertToPlanItemDetail(PlanTemplateDetailBatchVO template, Integer batchNumber, Integer duration,
                                                               LocalDate planFirstDate) {
        ProductionPlanItemDetailVO detailVO = convertToVo(template);
        detailVO.setProductionLineId(template.getProductionLineId());
        detailVO.setProductionLineName(template.getProductionLineName());
        detailVO.setProductionLineCode(template.getProductionLineCode());
        //计算开始日期：首批生产日期+第几批*间隔时长+工艺开始时间间隔时长
        LocalDate startTime = planFirstDate.plusDays(batchNumber * duration + template.getIntervalDuration());
        detailVO.setStartTime(startTime);
        //计算结束日期
        detailVO.setEndTime(detailVO.getStartTime().plusDays(template.getExecutionDuration() == 0 ? 0 : template.getExecutionDuration() - 1));
        detailVO.setGroupNumber(batchNumber);
        return detailVO;
    }

    ProductionPlanItemDetailVO convertToVo(PlanTemplateDetailBatchVO template);

    /**
     * 构建工序数据
     *
     * @param procedureList 模板工序数据
     * @param startTime     工艺计划开始时间
     * @return
     */
    default List<ProcedureDetailVO> convertToProcedureDetail(List<PlanTemplateProcedureVO> procedureList, LocalDate startTime) {
        return procedureList.stream().map(item -> {
            ProcedureDetailVO vo = new ProcedureDetailVO();
            vo.setProcedureName(item.getName());
            vo.setProcedureId(item.getProcedureId());
            vo.setStartTime(startTime.plusDays(item.getIntervalDuration()));
            vo.setEndTime(vo.getStartTime().plusDays(item.getExecutionDuration() == 0 ? item.getExecutionDuration() : item.getExecutionDuration() - 1));
            return vo;
        }).collect(Collectors.toList());
    }

    ProductionPlan convert2ProductionPlan(ProductionPlanIssueDTO dto);

    ProductionPlanItem convert2ProductionPlanItem(ProductionPlanItemSaveDTO e);

    default List<PlanSaveDTO> convert2PlanSaveDTO(List<ProductionPlanItemVO> planVOs, List<ProductionPlanItemSaveDTO> list, String planType) {
        List<PlanSaveDTO> result = new ArrayList<>();
        for (int i = 0; i < planVOs.size(); i++) {
            PlanSaveDTO saveDTO = convert2PlanSaveDTO(planVOs.get(i));
            ProductionPlanItemSaveDTO sourceDTO = list.get(i);
            saveDTO.setSort(sourceDTO.getSort());
            saveDTO.setRelationPlanList(sourceDTO.getRelationList());
            saveDTO.setType(planType);
            saveDTO.setProductPlanType(sourceDTO.getProductPlanType());
            saveDTO.setId(IdUtils.getSnowflake());
            saveDTO.setPlanNoCodeApplyTime(sourceDTO.getPlanNoCodeApplyTime());
            saveDTO.setPlanNoCode(sourceDTO.getPlanNoCode());
            saveDTO.setBatchNoCode(sourceDTO.getBatchNoCode());
            result.add(saveDTO);
        }
        return result;
    }

    PlanSaveDTO convert2PlanSaveDTO(ProductionPlanItemVO planVO);

    DirectlyCreateBuildNoDTO convert2DirectlyCreateBuildNoDTO(DirectlyCreatePlanDTO dto);

    default Plan convert2Plan(DirectlyCreatePlanDTO dto, ProcessDetailVO detail, ProductMaterialDetailVO material) {
        Plan plan = new Plan();
        plan.setPlanNo(dto.getPlanNo());
        plan.setBatchNo(dto.getBatchNo());
        plan.setProductDate(LocalDate.now());
        plan.setProductId(detail.getProductId());
        plan.setProductName(material.getName());
        plan.setProductMergeCode(material.getMergeCode());
        plan.setProductSpecification(material.getSpecification());
        plan.setInnerPackingSpecification(material.getInnerPackingSpecification());
        plan.setPackingSpecification(material.getPackingSpecification());
        plan.setProcessId(dto.getProcessId());
        plan.setProcessName(detail.getName());
        plan.setProcessVersion(dto.getProcessVersion());
        plan.setProcessNum(detail.getProcedures().size());
        plan.setStatus(ProductPlanStatusEnum.CONFIRM);
        plan.setInstructStatus(ProductPlanInstructStatusEnum.WAIT_DECOMPOSE);
        plan.setBatchQuantity(dto.getBatchQuantity());
        plan.setProductionLineId(dto.getProductionLineId());
        plan.setUnitId(dto.getUnitId());
        plan.setType(CommonEnum.getEnumByValue(ProductPlanTypeEnum.class, dto.getProductPlanType()));
        return plan;
    }

    default ProductionPlanItem convert2DirectlyCreatedPlanItem(Plan plan, ProcessDetailVO detail, FactoryLineFeignVO line) {
        ProductionPlanItem item = new ProductionPlanItem();
        item.setProductionPlanId(0L);
        item.setTemplateBatchId(0L);
        item.setStartTime(LocalDate.now());
        item.setEndTime(LocalDate.now());
        item.setProductionLineId(plan.getProductionLineId());
        item.setProductionLineName(line.getName());
        item.setProductionLineCode(line.getCode());
        item.setPlanNo(plan.getPlanNo());
        item.setBatchNo(plan.getBatchNo());
        item.setBatchQuantity(plan.getBatchQuantity());
        item.setGroupNumber(0);
        item.setProcessNum(detail.getProcedures().size());
        List<ProcedureDetailDTO> procedures = detail.getProcedures().stream().map(e -> {
            ProcedureDetailDTO vo = new ProcedureDetailDTO();
            vo.setStartTime(LocalDate.now());
            vo.setEndTime(LocalDate.now());
            vo.setProcedureId(e.getProcedureId());
            vo.setProcedureName(e.getName());
            return vo;
        }).collect(Collectors.toList());
        item.setProcedureList(JsonUtils.toJsonString(procedures));
        return item;
    }
}
