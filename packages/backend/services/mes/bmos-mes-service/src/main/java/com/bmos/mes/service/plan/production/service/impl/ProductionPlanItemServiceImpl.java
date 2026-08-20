package com.bmos.mes.service.plan.production.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.common.enums.plan.ProductPlanStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.operation.history.annotation.OperationHistory;
import com.bmos.mes.service.operation.history.aspect.OperationHistoryContext;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.plan.info.dto.PlanSaveDTO;
import com.bmos.mes.service.plan.info.dto.ProductPlanRelationDTO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.plan.production.convert.ProductionPlanConverter;
import com.bmos.mes.service.plan.production.dto.ProductionPlanCalendarChangeDTO;
import com.bmos.mes.service.plan.production.dto.ProductionPlanCalendarQueryDTO;
import com.bmos.mes.service.plan.production.dto.ProductionPlanItemSaveDTO;
import com.bmos.mes.service.plan.production.dto.ProductionPlanMonthsCalendarQueryDTO;
import com.bmos.mes.service.plan.production.mapper.ProductionPlanItemMapper;
import com.bmos.mes.service.plan.production.model.ProductionPlanItem;
import com.bmos.mes.service.plan.production.service.ProductionPlanItemService;
import com.bmos.mes.service.plan.production.vo.ProcedureDetailVO;
import com.bmos.mes.service.plan.production.vo.ProductionPlanCalendarVO;
import com.bmos.mes.service.plan.production.vo.ProductionPlanItemDetailVO;
import com.bmos.mes.service.plan.production.vo.ProductionPlanItemVO;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.vo.ProcessDetailVO;
import com.bmos.mes.service.workflow.vo.ProcedureTimeVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Service
public class ProductionPlanItemServiceImpl implements ProductionPlanItemService {

    @Resource
    private ProductionPlanItemMapper itemMapper;

    @Resource
    private PlanService planService;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private FactoryFeign factoryFeign;

    @Override
    public void checkStartingPlanList(Long id) {
        List<ProductionPlanItem> itemList = itemMapper.queryByProductionPlanId(id);
        List<Long> planItemIdS = CollectionUtils.convertList(itemList, ProductionPlanItem::getId);
        if (CollUtil.isEmpty(planItemIdS)){
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_ERROR);
        }
        List<Plan> planList = planService.queryByProductionPlanIdS(planItemIdS);
        List<Plan> auditPlan = CollectionUtils.filterList(planList, item -> ProductPlanStatusEnum.AUDIT.equals(item.getStatus()));
        if (CollUtil.isNotEmpty(auditPlan)){
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_START_ERROR,CollectionUtils.getFirst(auditPlan).getBatchNo());
        }
        List<Plan> activePlan = CollectionUtils.filterList(planList, item -> ProductPlanStartEnum.WAIT != item.getStart() &&
                ProductPlanStartEnum.TERMINATION != item.getStart());
        if (CollUtil.isNotEmpty(activePlan)){
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_START_ERROR,CollectionUtils.getFirst(activePlan).getBatchNo());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void nullifyPlan(Long id) {
        List<ProductionPlanItem> itemList = itemMapper.queryByProductionPlanId(id);
        List<Long> planItemIdS = CollectionUtils.convertList(itemList, ProductionPlanItem::getId);
        List<Plan> planList = planService.queryByProductionPlanIdS(planItemIdS);
        if (CollUtil.isNotEmpty(planList)){
            planList.forEach(item->{
                if ((ProductPlanStatusEnum.DISCARD.equals(item.getStatus()))){
                    return;
                }
                planService.discard(item.getId());
            });
        }
    }

    @Override
    public List<List<ProductionPlanItemDetailVO>> selectDetailByProductionPlanId(Long id) {
        List<List<ProductionPlanItemDetailVO>> list = new ArrayList<>();
        List<ProductionPlanItemDetailVO> itemList = itemMapper.selectDetailByProductionPlanId(id);
        if (CollUtil.isEmpty(itemList)){
            return Collections.emptyList();
        }
        Map<Integer, List<ProductionPlanItemDetailVO>> itemMap = CollectionUtils.convertMultiMap(itemList, ProductionPlanItemDetailVO::getGroupNumber);
        List<Plan> planList = planService.selectByProductionPlanItemId(CollectionUtils.convertList(itemList, ProductionPlanItemDetailVO::getId));
        Map<Long, Plan> planMap = CollectionUtils.convertMap(planList, Plan::getProductionPlanItemId);
        itemMap.forEach((key,value)->{
            value.forEach(item-> {
                item.setProcedureListDetail(JsonUtils.parseArray(item.getProcedureListItem(), ProcedureDetailVO.class));
                Plan plan = planMap.get(item.getId());
                item.setPlanNo(plan.getPlanNo());
                item.setBatchNo(plan.getBatchNo());
            });
            list.add(value);
        });
        return list;
    }

    @Override
    @OperationHistory(module = BusinessModule.PRODUCT_PLAN, operationType = OperationType.CHANGE_CALENDAR, businessId = "#getId")
    public void changeProductionItemCalendar(ProductionPlanCalendarChangeDTO dto) {
        ProductionPlanItem productionPlanItem = itemMapper.selectById(dto.getProductionPlanItemId());
        if (productionPlanItem == null) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_ERROR);
        }
        if (dto.getProcedureId() != null) {
            changeProcedureCalendar(dto, productionPlanItem);
        } else {
            changeProcessCalendar(dto, productionPlanItem);
        }
        Plan plan = planService.getPlanByPoductionPlanItemId(dto.getProductionPlanItemId());
        OperationHistoryContext.putVariable(plan, Plan::getId);
        itemMapper.updateById(productionPlanItem);
    }

    /**
     * 修改工艺日历
     * @param dto
     * @param productionPlanItem
     */
    private static void changeProcessCalendar(ProductionPlanCalendarChangeDTO dto, ProductionPlanItem productionPlanItem) {
        // 工艺开始时间的前移后移天数
        long days = ChronoUnit.DAYS.between(productionPlanItem.getStartTime(), dto.getStartTime());
        List<ProcedureDetailVO> procedureDetailVOS = JsonUtils.parseArray(productionPlanItem.getProcedureList(),
                ProcedureDetailVO.class);
        // 调整工艺时 其下工序也要修改
        procedureDetailVOS.forEach(procedure -> {
            procedure.setStartTime(procedure.getStartTime().plusDays(days));
            procedure.setEndTime(procedure.getEndTime().plusDays(days));
            if (procedure.getEndTime().isAfter(dto.getEndTime())) {
                throw new BmosException(MesResponseCode.PROCEDURE_ENDTIME_AFTER_PROCESS_ENDTIME);
            }
        });
        productionPlanItem.setStartTime(dto.getStartTime());
        productionPlanItem.setEndTime(dto.getEndTime());
        productionPlanItem.setProcedureList(JsonUtils.toJsonString(procedureDetailVOS));
    }

    /**
     * 修改工序日历
     * @param dto
     * @param productionPlanItem
     */
    private static void changeProcedureCalendar(ProductionPlanCalendarChangeDTO dto, ProductionPlanItem productionPlanItem) {
        List<ProcedureDetailVO> procedureDetailVOS = JsonUtils.parseArray(productionPlanItem.getProcedureList(),
                ProcedureDetailVO.class);
        procedureDetailVOS.forEach(e->{
            if (Objects.equals(e.getProcedureId(), dto.getProcedureId())) {
                e.setStartTime(dto.getStartTime());
                e.setEndTime(dto.getEndTime());
            }
            // 如果工序开始时间早于原工艺开始时间 则抛出异常
            if (e.getStartTime().isBefore(productionPlanItem.getStartTime())) {
                throw new BmosException(MesResponseCode.PROCEDURE_STARTTIME_BEFORE_PROCESS_STARTTIME);
            }
            if (e.getStartTime().isAfter(productionPlanItem.getEndTime())) {
                throw new BmosException(MesResponseCode.PROCEDURE_STARTTIME_AFTER_PROCESS_ENDTIME);
            }
            // 如果工序结束时间在工艺计划结束时间之后 更新工艺计划结束时间
            if (e.getEndTime().isAfter(productionPlanItem.getEndTime())) {
                productionPlanItem.setEndTime(e.getEndTime());
            }
        });
        productionPlanItem.setProcedureList(JsonUtils.toJsonString(procedureDetailVOS));
    }

    @Override
    public List<ProductionPlanCalendarVO> getProductionPlanCalendar(ProductionPlanCalendarQueryDTO dto) {
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<Long> deptIds = platformApiAdaptor.deptIds();
            if (CollUtil.isEmpty(deptIds)) {
                return new ArrayList<>();
            }
            dto.setDeptIds(deptIds);
        }

        return itemMapper.selectProductionPlanCalendar(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issueProductionPlanItem(List<ProductionPlanItemSaveDTO> list, Long id, String planType) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<ProductionPlanItem> itemList = list.stream().map(e -> {
            ProductionPlanItem item = ProductionPlanConverter.INSTANCE.convert2ProductionPlanItem(e);
            item.setProcedureList(JsonUtils.toJsonString(e.getProcedureListDetail()));
            item.setProductionPlanId(id);
            item.setProcessNum(e.getProcedureListDetail().size());
            return item;
        }).collect(Collectors.toList());
        itemMapper.insertBatch(itemList);
        // 组装指令单生成所需信息
        List<PlanSaveDTO> saveDTOS = getPlanSaveDTOS(itemList, planType, list);
        // 批量保存生产指令单
        planService.batchSave(saveDTOS);
    }


    private List<PlanSaveDTO> getPlanSaveDTOS(List<ProductionPlanItem> itemList, String planType,
                                              List<ProductionPlanItemSaveDTO> list) {
        List<Long> idList = CollectionUtils.convertList(itemList, ProductionPlanItem::getId);
        List<ProductionPlanItemVO> planVOs = itemMapper.selectProductPlanInfoByItemIdList(idList);
        List<PlanSaveDTO> saveDTOS = ProductionPlanConverter.INSTANCE.convert2PlanSaveDTO(planVOs, list, planType);
        Map<Integer, PlanSaveDTO> sortMap = CollectionUtils.convertMap(saveDTOS, PlanSaveDTO::getSort);
        for (int i = 0; i < saveDTOS.size(); i++) {
            PlanSaveDTO saveDTO = saveDTOS.get(i);
            ProductionPlanItemSaveDTO sourceDTO = list.get(i);
            saveDTO.setSort(sourceDTO.getSort());
            saveDTO.setRelationPlanList(sourceDTO.getRelationList());
            if (CollUtil.isNotEmpty(sourceDTO.getCurrentRelationList())) {
                // 处理当前计划关联批次
                List<ProductPlanRelationDTO> collect = sourceDTO.getCurrentRelationList().stream().map(e -> {
                    ProductPlanRelationDTO relationDTO = new ProductPlanRelationDTO();
                    relationDTO.setProcessId(e.getProcessId());
                    relationDTO.setPlanIds(e.getPlanIds().stream()
                            .map(s-> {
                                PlanSaveDTO planSaveDTO = sortMap.get(s.intValue());
                                if (planSaveDTO == null) {
                                    throw new BmosException(MesResponseCode.PRODUCTION_PLAN_RELATION_ERROR);
                                }
                                return planSaveDTO.getId();
                            })
                            .collect(Collectors.toList()));
                    return relationDTO;
                }).collect(Collectors.toList());
                saveDTO.getRelationPlanList().addAll(collect);
            }
        }
        return saveDTOS;
    }

    @Override
    public List<ProcedureTimeVO> selectProcedureConfigByPlanIds(Set<Long> productionPlanItemId) {
        if (CollUtil.isEmpty(productionPlanItemId)){
            return null;
        }
        return itemMapper.selectProcedureConfigByPlanIds(productionPlanItemId);
    }

    @Override
    public List<ProductionPlanCalendarVO> getProductionPlanMonthsCalendar(ProductionPlanMonthsCalendarQueryDTO dto) {
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<Long> deptIds = platformApiAdaptor.deptIds();
            if (CollUtil.isEmpty(deptIds)) {
                return new ArrayList<>();
            }
            dto.setDeptIds(deptIds);
        }

        return itemMapper.selectProductionPlanMonthsCalendar(dto);
    }

    @Override
    public List<ProductionPlanItem> queryListByProductionPlanIdS(List<Long> productionPlanIds) {
        if (CollUtil.isEmpty(productionPlanIds)) {
            return new ArrayList<>();
        }
        return itemMapper.queryListByProductionPlanIdS(productionPlanIds);
    }

    @Override
    public ProductionPlanItem SaveDirectlyCreatedPlanItem(Plan plan, ProcessDetailVO detail) {
        List<FactoryLineFeignVO> data = FeignUtils.handleRequest(factoryFeign::queryLineListByLineIds, Collections.singletonList(plan.getProductionLineId())).getData();
        ProductionPlanItem item = ProductionPlanConverter.INSTANCE.convert2DirectlyCreatedPlanItem(plan, detail, CollUtil.getFirst(data));
        itemMapper.insert(item);
        return item;
    }
}
