package com.bmos.mes.service.plan.template.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.plan.template.convert.PlanTemplateConverter;
import com.bmos.mes.service.plan.template.dto.*;
import com.bmos.mes.service.plan.template.mapper.PlanTemplateBatchMapper;
import com.bmos.mes.service.plan.template.mapper.PlanTemplateMapper;
import com.bmos.mes.service.plan.template.model.PlanTemplate;
import com.bmos.mes.service.plan.template.model.PlanTemplateBatch;
import com.bmos.mes.service.plan.template.model.PlanTemplateBatchProcedure;
import com.bmos.mes.service.plan.template.service.PlanTemplateService;
import com.bmos.mes.service.plan.template.vo.*;
import com.bmos.mes.service.process.mapper.ProcessMapper;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.product.vo.ProcessProductVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanTemplateServiceImpl implements PlanTemplateService {

    @Resource
    private PlanTemplateMapper planTemplateMapper;

    @Resource
    private PlanTemplateBatchMapper planTemplateBatchMapper;

    @Resource
    private ProductMaterialService productMaterialService;

    @Resource
    private UnitCache unitCache;

    @Resource
    private ProcessMapper processMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePlanTemplate(PlanTemplateSaveDTO dto) {
        // 名称重复校验
        PlanTemplate existed = planTemplateMapper.selectByName(dto.getName());
        if (existed != null) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_TEMPLATE_NAME_EXISTED);
        }
        PlanTemplate template = insertPlanTemplate(dto);
        List<PlanTemplateBatchDTO> batchDtoList = dto.getBatchList();
        insertTemplateBatch(batchDtoList, template.getId());
    }

    private void insertTemplateBatch(List<PlanTemplateBatchDTO> batchDtoList, Long templateId) {
        Set<Long> processIds = CollectionUtils.convertSet(batchDtoList, PlanTemplateBatchDTO::getProcessId);
        List<ProcessProductVO> products = productMaterialService.getByProcessIds(processIds);
        Map<Long, ProcessProductVO> processProductMap = CollectionUtils.convertMap(products, ProcessProductVO::getProcessId);
        List<PlanTemplateBatch> batchList = batchDtoList.stream().map(e -> {
            PlanTemplateBatch batch = PlanTemplateConverter.INSTANCE.convertTemplateBatch(e);
            batch.setPlanTemplateId(templateId);
            List<PlanTemplateProcedureConfigDTO> procedureDurationList = e.getProcedureDurationList();
            // 沿用批号为0且仅关联1个批次时沿用批号
            if (e.isReuseBatchNumber() && CollUtil.size(e.getRelationBatchSortList()) == 1) {
                batch.setFollowBatchSort(CollUtil.getFirst(e.getRelationBatchSortList()));
            }
            batch.setProcedureConfig(JsonUtils.toJsonString(procedureDurationList));
            ProcessProductVO processProductVO = processProductMap.get(e.getProcessId());
            if (processProductVO == null) {
                throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
            }
            batch.setProductId(processProductVO.getId());
            batch.setProductName(processProductVO.getName());
            batch.setProductMergeCode(processProductVO.getMergeCode());
            batch.setProductSpecification(processProductVO.getSpecification());
            batch.setInnerPackingSpecification(processProductVO.getInnerPackingSpecification());
            batch.setPackingSpecification(processProductVO.getPackingSpecification());
            batch.setProductMark(processProductVO.getProductMark());
            batch.setRelationProcessesList(JsonUtils.toJsonString(e.getRelationProcessesList()));
            return batch;
        }).collect(Collectors.toList());
        planTemplateBatchMapper.insertBatch(batchList);
    }

    @Override
    public CommonPage<PlanTemplatePageVO> queryPlanTemplatePage(PlanTemplatePageQueryDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<PlanTemplate> list = planTemplateMapper.queryPage(dto);
        CommonPage<PlanTemplate> commonPage = CommonPage.convertPage(list);
        return PlanTemplateConverter.INSTANCE.convertTemplatePage(commonPage);
    }

    @Override
    public void changePlanTemplateState(PlanTemplateChangeStateDTO dto) {
        PlanTemplate planTemplate = planTemplateMapper.selectById(dto.getId());
        if (planTemplate == null) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_TEMPLATE_NOT_EXISTS);
        }
        planTemplate.setOperatorUserId(SysUserHolder.getUser().getUserId());
        planTemplate.setState(dto.getState());
        planTemplate.setOperationTime(LocalDateTime.now());
        planTemplateMapper.updateById(planTemplate);
    }

    @Override
    public void deletePlanTemplate(Long id) {
        planTemplateMapper.deleteById(id);
    }

    @Override
    public PlanTemplateDetailVO getPlanTemplateDetail(Long id) {
        PlanTemplate planTemplate = planTemplateMapper.selectById(id);
        if (planTemplate == null) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_TEMPLATE_NOT_EXISTS);
        }
        PlanTemplateDetailVO result = new PlanTemplateDetailVO();
        result.setName(planTemplate.getName());
        result.setId(id);
        List<PlanTemplateDetailBatchVO> batchList = planTemplateBatchMapper.selectDetailByPlanTemplateId(id);
        for (PlanTemplateDetailBatchVO batch : batchList) {
            String procedureConfig = batch.getProcedureConfig();
            List<PlanTemplateBatchProcedure> planTemplateBatchProcedure = JsonUtils.parseArray(procedureConfig, PlanTemplateBatchProcedure.class);
            Map<Long, PlanTemplateBatchProcedure> map = CollectionUtils.convertMap(planTemplateBatchProcedure, PlanTemplateBatchProcedure::getProcedureId);
            if (CollUtil.isNotEmpty(batch.getProcedureList())) {
                // 处理当前生效工艺版本对应的工步的配置
                for (PlanTemplateProcedureVO procedureVO : batch.getProcedureList()) {
                    PlanTemplateBatchProcedure config = map.getOrDefault(procedureVO.getProcedureId(),
                            new PlanTemplateBatchProcedure());
                    procedureVO.setIntervalDuration(config.getIntervalDuration());
                    procedureVO.setExecutionDuration(config.getExecutionDuration());
                }
            }
            batch.setUnitName(unitCache.getGlobalUnitName(batch.getUnitId()));
            batch.getProcedureList().sort(Comparator.comparing(PlanTemplateProcedureVO::getSort,
                    Comparator.nullsLast(Comparator.naturalOrder())));
        }
        result.setTemplateBatchList(batchList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPlanTemplate(PlanTemplateEditDTO dto) {
        PlanTemplate planTemplate = planTemplateMapper.selectById(dto.getId());
        if (planTemplate == null) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_TEMPLATE_NOT_EXISTS);
        }
        // 更新配置
        boolean versionMatching = validateProcessVersion(dto.getBatchList());
        planTemplateBatchMapper.deleteByPlanTemplateId(dto.getId());
        insertTemplateBatch(dto.getBatchList(), dto.getId());
        planTemplate.setOperationTime(LocalDateTime.now());
        planTemplate.setOperatorUserId(SysUserHolder.getUser().getUserId());
        planTemplate.setConfirmed(versionMatching);
        planTemplate.setState(false);
        planTemplateMapper.updateById(planTemplate);
    }

    /**
     * 校验当前保存工艺生效版本是否匹配
     * @param batchList
     * @return
     */
    private boolean validateProcessVersion(List<PlanTemplateBatchDTO> batchList) {
        List<Long> processIds = CollectionUtils.convertList(batchList, PlanTemplateBatchDTO::getProcessId);
        if (CollUtil.isEmpty(processIds)) {
            throw new BmosException(MesResponseCode.ISSUE_BATCH_LIST_EMPTY);
        }
        List<Process> processes = processMapper.selectBatchIds(processIds);
        Map<Long, String> versionMap = CollectionUtils.convertMap(processes, Process::getId, Process::getActiveVersion);
        return batchList.stream().allMatch(e-> Objects.equals(versionMap.get(e.getProcessId()), e.getProcessVersion()));
    }



    @Override
    public List<PlanTemplateListVO> getEnablePlanTemplateList() {
        List<PlanTemplate> list = planTemplateMapper.selectEnableTemplateList();
        return PlanTemplateConverter.INSTANCE.convert2TemplateListVO(list);
    }

    @Override
    public PlanTemplate getById(Long planTemplateId) {
        return planTemplateMapper.selectById(planTemplateId);
    }

    @Override
    public void updateTemplateConfirmStatus(List<Process> processes) {
        if (CollUtil.isEmpty(processes)) {
            return;
        }
        List<PlanTemplateBatch> batchList =
                planTemplateBatchMapper.selectByProcessIdList(CollectionUtils.convertList(processes, Process::getId));
        List<Long> needUpdateTemplate = batchList.stream().map(PlanTemplateBatch::getPlanTemplateId).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(needUpdateTemplate)) {
            return;
        }
        planTemplateMapper.batchConfirmTemplate(needUpdateTemplate);
        planTemplateMapper.batchCancelConfirmTemplate(needUpdateTemplate);
    }

    @Override
    public Boolean validateProcessVersionMatch(Long planTemplateId) {
        PlanTemplate template = planTemplateMapper.selectById(planTemplateId);
        if (template == null) {
            throw new BmosException(MesResponseCode.PRODUCTION_PLAN_TEMPLATE_NOT_EXISTS);
        }
        List<PlanTemplateBatch> batchList = planTemplateBatchMapper.selectByPlanTemplateId(template.getId());
        List<Long> processIds =
                batchList.stream().map(PlanTemplateBatch::getProcessId).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollUtil.isEmpty(processIds)) {
            throw new BmosException(MesResponseCode.PLAN_TEMPLATE_ERROR);
        }
        List<Process> processes = processMapper.selectBatchIds(processIds);
        Map<Long, Process> versionMap = CollectionUtils.convertMap(processes, Process::getId);
        return batchList.stream().allMatch(e-> Objects.equals(e.getProcessVersion(),
                Optional.ofNullable(versionMap.get(e.getProcessId())).orElse(new Process()).getActiveVersion()));
    }

    private PlanTemplate insertPlanTemplate(PlanTemplateSaveDTO dto) {
        PlanTemplate template = new PlanTemplate();
        boolean versionMatch = validateProcessVersion(dto.getBatchList());
        template.setConfirmed(versionMatch);
        template.setState(versionMatch);
        template.setName(dto.getName());
        template.setOperatorUserId(SysUserHolder.getUser().getUserId());
        template.setOperationTime(LocalDateTime.now());
        planTemplateMapper.insert(template);
        return template;
    }


}
