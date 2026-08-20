package com.bmos.mes.service.trace.material.service.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.output.finished.model.FinishedProductOutput;
import com.bmos.mes.service.output.finished.model.FinishedProductOutputResult;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.product.mapper.ProductMaterialCategoryMapper;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.model.ProductMaterialCategory;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialVO;
import com.bmos.mes.service.trace.material.dto.MaterialTraceHistoryDTO;
import com.bmos.mes.service.trace.material.entity.MaterialTraceHistoryDO;
import com.bmos.mes.service.trace.material.enums.MaterialTraceOperateType;
import com.bmos.mes.service.trace.material.enums.MaterialTraceType;
import com.bmos.mes.service.trace.material.mapper.IMaterialTraceHistoryMapper;
import com.bmos.mes.service.trace.material.service.IMaterialTraceHistoryService;
import com.bmos.unit.service.UnitCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/21 15:00
 */
@Service
@Slf4j
public class MaterialTraceHistoryServiceImpl implements IMaterialTraceHistoryService {

    private static final String LOG_PREFIX = "[物料追溯历史]";

    @Resource
    private IMaterialTraceHistoryMapper materialTraceHistoryMapper;

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Resource
    private UnitCache unitCache;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Resource
    private ProductMaterialCategoryMapper materialCategoryMapper;

    @Resource
    private ProductMaterialMapper productMaterialMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTraceHistory(MaterialTraceHistoryDTO historyTrace) {
        this.saveTraceHistory(CollectionUtils.singleton(historyTrace));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTraceHistory(Collection<MaterialTraceHistoryDTO> historyTraces) {
        if (historyTraces == null || historyTraces.isEmpty()) {
            return;
        }
        log.info("{} 保存物料追溯历史:{}", LOG_PREFIX, historyTraces);
        List<MaterialTraceHistoryDO> list = new ArrayList<>();
        List<Long> storageMaterialIdList = CollectionUtils.convertList(historyTraces, MaterialTraceHistoryDTO::getStorageMaterialId);
        List<Long> productPlanIdList = CollectionUtils.convertList(historyTraces, MaterialTraceHistoryDTO::getProductPlanId);
        List<Long> procedureStepModelIdList = CollectionUtils.convertList(historyTraces, MaterialTraceHistoryDTO::getProcedureStepModelId);
        Map<Long, StorageMaterialVO> storageMaterialMap = CollectionUtils.convertMap(storageMaterialService.queryInfoByIds(storageMaterialIdList), StorageMaterialVO::getId);

        List<MaterialTraceHistoryDO> materialHistories = materialTraceHistoryMapper.selectOutputHistoryByStorageMaterialIds(storageMaterialIdList);
        Map<Long, Long> outputMap = materialHistories.stream().collect(Collectors.toMap(MaterialTraceHistoryDO::getStorageMaterialId, MaterialTraceHistoryDO::getProductPlanId, (v1, v2) -> v1));
        productPlanIdList.addAll(outputMap.values());
        Map<Long, Plan> planMap = CollectionUtils.convertMap(planMapper.selectBatchIds(productPlanIdList), Plan::getId);
        Map<Long, ProcedureStepModel> procedureStepModelMap = CollectionUtils.convertMap(procedureStepModelMapper.selectBatchIds(procedureStepModelIdList), ProcedureStepModel::getId);
        for (MaterialTraceHistoryDTO historyTrace : historyTraces) {
            StorageMaterialVO storageMaterial = storageMaterialMap.get(historyTrace.getStorageMaterialId());
            if (storageMaterial == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
            }
            Plan plan = planMap.get(historyTrace.getProductPlanId());
            if (plan == null) {
                throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
            }
            ProcedureStepModel procedureStepModel = procedureStepModelMap.get(historyTrace.getProcedureStepModelId());
            if (procedureStepModel == null) {
                throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
            }
            MaterialTraceHistoryDO history = new MaterialTraceHistoryDO();
            history.setMaterialId(storageMaterial.getMaterialId());
            history.setMaterialName(storageMaterial.getMaterialName());
            history.setMaterialCategoryId(storageMaterial.getMaterialCategoryId());
            history.setMaterialCategoryName(storageMaterial.getMaterialCategoryName());
            history.setMaterialCategoryType(storageMaterial.getMaterialCategoryType());
            history.setMergeCode(storageMaterial.getMergeCode());
            history.setMaterialSpecification(storageMaterial.getMaterialSpecification());
            history.setStorageMaterialId(storageMaterial.getId());
            history.setStorageMaterialNo(storageMaterial.getMaterialNo());
            history.setStorageMaterialBatchId(storageMaterial.getMaterialBatchId());
            history.setStorageMaterialBatchNo(storageMaterial.getMaterialBatchNo());
            history.setQuantity(historyTrace.getQuantity());
            history.setUnitId(historyTrace.getUnitId());
            history.setUnitName(unitCache.getGlobalUnitName(historyTrace.getUnitId()));
            history.setProductPlanId(plan.getId());
            history.setBatchNo(plan.getBatchNo());
            history.setProcessId(procedureStepModel.getProcessId());
            history.setProcessVersion(procedureStepModel.getProcessVersion());
            history.setProcedureId(procedureStepModel.getProcedureId());
            history.setProcedureStepId(procedureStepModel.getProcedureStepId());
            history.setOperateType(historyTrace.getOperateType());
            history.setOperateUserId(SysUserHolder.getUser().getUserId());
            history.setOperateTime(LocalDateTime.now());
            history.setTraceType(historyTrace.getOperateType().getTraceType());
            if (history.getTraceType() == MaterialTraceType.CONSUME){
                Optional.ofNullable(storageMaterial.getId())
                        .map(outputMap::get)
                        .map(planMap::get)
                        .ifPresent(outPlan -> {
                            history.setSourceProductPlanId(outPlan.getId());
                            history.setSourceBatchNo(outPlan.getBatchNo());
                        });
            }else {
                history.setSourceBatchNo(plan.getBatchNo());
                history.setSourceProductPlanId(plan.getId());
            }

            list.add(history);
        }
        materialTraceHistoryMapper.insertBatch(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTraceHistory(Long procedureStepModelId, FinishedProductOutput finishedProductOutput, List<FinishedProductOutputResult> finishedProductOutputResults) {

        Long productId = finishedProductOutput.getProductId();
        ProductMaterial product = productMaterialMapper.selectById(productId);
        if (product == null) {
            return;
        }
        ProductMaterialCategory productMaterialCategory = materialCategoryMapper.selectById(product.getMaterialCategoryId());
        if (productMaterialCategory == null) {
            return;
        }
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(procedureStepModelId);
        if (procedureStepModel == null) {
            return;
        }
        List<MaterialTraceHistoryDO> list = new ArrayList<>();
        for (FinishedProductOutputResult finishedProductOutputResult : finishedProductOutputResults) {
            MaterialTraceHistoryDO history = new MaterialTraceHistoryDO();
            history.setMaterialId(finishedProductOutput.getProductId());
            history.setMaterialName(finishedProductOutput.getProductName());
            history.setMaterialCategoryId(productMaterialCategory.getId());
            history.setMaterialCategoryName(productMaterialCategory.getName());
            history.setMaterialCategoryType(CategoryInfoTypeEnum.getEnumByValue(productMaterialCategory.getCategoryType()));
            history.setMergeCode(finishedProductOutput.getProductMergeCode());
            history.setMaterialSpecification(product.getSpecification());
            history.setQuantity(finishedProductOutputResult.getSingleQuantity());
            history.setUnitId(finishedProductOutputResult.getUnitId());
            history.setUnitName(unitCache.getGlobalUnitName(history.getUnitId()));
            history.setProductPlanId(finishedProductOutput.getProductPlanId());
            history.setBatchNo(finishedProductOutput.getProductBatchNo());
            // 成品批次暂时使用成品生产批次
            history.setStorageMaterialBatchNo(finishedProductOutputResult.getProductBatchNo());
            history.setProcessId(procedureStepModel.getProcessId());
            history.setProcessVersion(procedureStepModel.getProcessVersion());
            history.setProcedureId(procedureStepModel.getProcedureId());
            history.setProcedureStepId(procedureStepModel.getProcedureStepId());
            history.setTraceType(MaterialTraceType.OUTPUT);
            history.setOperateType(MaterialTraceOperateType.PRODUCT_OUTPUT);
            history.setOperateUserId(SysUserHolder.getUser().getUserId());
            history.setOperateTime(LocalDateTime.now());
            history.setSourceProductPlanId(finishedProductOutput.getProductPlanId());
            history.setSourceBatchNo(finishedProductOutput.getProductBatchNo());
            list.add(history);
        }
        if (!CollectionUtils.isAnyEmpty(list)) {
            materialTraceHistoryMapper.insertBatch(list);
        }
    }
}
