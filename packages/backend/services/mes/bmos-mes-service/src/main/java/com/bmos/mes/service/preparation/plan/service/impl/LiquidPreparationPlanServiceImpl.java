package com.bmos.mes.service.preparation.plan.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.preparation.measure.vo.UnmeasuredPreparationPlanVO;
import com.bmos.mes.service.preparation.plan.convert.LiquidPreparationPlanConverter;
import com.bmos.mes.service.preparation.plan.dto.*;
import com.bmos.mes.service.preparation.plan.mapper.LiquidPreparationMaterialBatchMapper;
import com.bmos.mes.service.preparation.plan.mapper.LiquidPreparationPlanMapper;
import com.bmos.mes.service.preparation.plan.model.*;
import com.bmos.mes.service.preparation.plan.service.LiquidPreparationPlanService;
import com.bmos.mes.service.preparation.plan.vo.*;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.strategy.PreparationPlanComponentStrategy;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.storage.manage.service.MaterialBatchFieldService;
import com.bmos.mes.service.storage.manage.vo.MaterialBatchFieldVO;
import com.bmos.mes.service.storage.manage.vo.ReservedBatchInfo;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LiquidPreparationPlanServiceImpl implements LiquidPreparationPlanService {

    @Autowired
    private LiquidPreparationPlanMapper liquidPreparationPlanMapper;

    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    private PlanService planService;

    @Autowired
    private ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private LiquidPreparationMaterialBatchMapper liquidPreparationMaterialBatchMapper;

    @Autowired
    private IStorageMaterialBatchService storageMaterialBatchService;

    @Autowired
    private PreparationPlanComponentStrategy preparationPlanComponentStrategy;

    @Autowired
    private ExecuteFormDataService executeFormDataService;

    @Autowired
    private BatchRecordComponentService componentService;

    @Autowired
    private MaterialBatchFieldService materialBatchFieldService;

    @Autowired
    private MaterialBatchFieldService batchFieldService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LiquidPreparationPlanInstanceVO getPreparationPlanInstance(LiquidPreparationPlanInstanceQueryDTO dto) {
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_NOT_EXIST);
        }
        // 查询组件实例
        UniqueComponentQueryDTO build = UniqueComponentQueryDTO.builder()
                .componentId(dto.getComponentId())
                .copyVersion(dto.getCopyVersion())
                .productPlanId(dto.getProductPlanId())
                .reuse(procedureStepModel.getReusable())
                .recordItemId(procedureStepModel.getRecordItemId())
                .recordVersionId(procedureStepModel.getRecordVersionId())
                .procedureStepModelId(dto.getProcedureStepModelId()).build();
        LiquidPreparationPlan liquidPreparationPlan = liquidPreparationPlanMapper.selectUnique(build);
        LiquidPreparationPlanInstanceVO result = new LiquidPreparationPlanInstanceVO();
        result.setProductPlanId(dto.getProductPlanId());
        if (ObjectUtil.isNull(liquidPreparationPlan)) {
            // 初始化配液计划
            liquidPreparationPlan = initAndSavePreparationPlan(dto, procedureStepModel);
            if (StrUtil.isEmpty(liquidPreparationPlan.getConfigJson())) {
                result.setNoConfig(true);
                return result;
            }
        }
        result.setId(liquidPreparationPlan.getId());
        result.setName(liquidPreparationPlan.getName());
        result.setCompleted(liquidPreparationPlan.getCompleted());
        result.setTargetVolume(liquidPreparationPlan.getActualTargetVolume() + unitCache.getGlobalUnitName(liquidPreparationPlan.getUnitId()));
        // 解析返回VO
        String configJson = liquidPreparationPlan.getConfigJson();
        parseConfigJson(configJson, result);
        return result;
    }

    /**
     * 解析配液计划物料信息
     *
     * @param configJson
     * @param result
     */
    private void parseConfigJson(String configJson, LiquidPreparationPlanInstanceVO result) {
        LiquidPreparationPlanConfig config = JsonUtils.parseObject(configJson, LiquidPreparationPlanConfig.class);
        // 目标浓度
        Long outPutFormulaMaterialId = config.getFormulaMaterialId();
        List<Long> ids = new ArrayList<>();
        ids.add(outPutFormulaMaterialId);
        List<LiquidPreparationPlanMaterial> formulaMaterialList = config.getMaterialList();
        ids.addAll(CollectionUtils.convertList(formulaMaterialList,
                LiquidPreparationPlanMaterial::getFormulaMaterialId));
        List<ProductFormulaMaterial> formulaMaterials = productFormulaConfigureService.getFormulaMaterialListByIds(ids);
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = CollectionUtils.convertMap(formulaMaterials,
                ProductFormulaMaterial::getId);
        ProductFormulaMaterial outputMaterial = formulaMaterialMap.get(outPutFormulaMaterialId);
        result.setMaterialName(outputMaterial.getMaterialName());
        result.setMaterialMergeCode(outputMaterial.getMaterialMergeCode());
        // 配液物料信息
        result.setMaterialList(config.getMaterialList().stream().map(e -> {
            Long id = e.getFormulaMaterialId();
            ProductFormulaMaterial formulaMaterial = formulaMaterialMap.get(id);
            LiquidPreparationPlanMaterialVO vo = new LiquidPreparationPlanMaterialVO();
            vo.setId(id);
            vo.setMaterialName(formulaMaterial.getMaterialName());
            vo.setMaterialMergeCode(formulaMaterial.getMaterialMergeCode());
            vo.setUnitName(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
            vo.setTargetConcentration(e.getTargetConcentration());
            vo.setConsistenceParamCode(e.getField());
            vo.setConsistenceParamName(e.getFieldName());
            return vo;
        }).collect(Collectors.toList()));
    }

    /**
     * 初始化并保存配液计划
     *
     * @param dto
     * @param procedureStepModel
     * @return
     */
    private LiquidPreparationPlan initAndSavePreparationPlan(LiquidPreparationPlanInstanceQueryDTO dto,
                                                             ProcedureStepModel procedureStepModel) {
        LiquidPreparationPlan insert = new LiquidPreparationPlan();
        Plan plan = planService.getById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        String componentConfigJson = procedureStepConfigService.getComponentConfigJson(procedureStepModel.getId(),
                dto.getComponentId(),
                procedureStepModel.getReusable(), plan.getProcessId(), plan.getProcessVersion());
        LiquidPreparationPlanConfig config = JsonUtils.parseObject(componentConfigJson,
                LiquidPreparationPlanConfig.class);
        if (config == null) {
            return insert;
        }
        ProductFormulaInfo formulaInfo = productFormulaConfigureService.getProductFormulaInfoByPlanId(plan.getId());
        Long outPutFormulaMaterialId = config.getFormulaMaterialId();
        ProductFormulaMaterial formulaMaterial = formulaInfo.getMaterialMap().get(outPutFormulaMaterialId);
        formulaMaterial.setQuantity(new BigDecimal(config.getTargetVolume()));
        BigDecimal actualVolume = MaterialQuantityCalculateUtil.calculateQuantity(plan.getBatchQuantity(),
                formulaInfo.getBatchQuantity(), formulaMaterial);
        // 校验工艺配置处配液组件配置是否完整
        config.validateConfig();
        insert.setUnitId(formulaMaterial.getUnitId());
        insert.setReuse(procedureStepModel.getReusable());
        insert.setProductPlanId(dto.getProductPlanId());
        insert.setRecordItemId(procedureStepModel.getRecordItemId());
        insert.setProcedureStepModelId(procedureStepModel.getId());
        insert.setBatchNo(plan.getBatchNo());
        insert.setComponentId(dto.getComponentId());
        insert.setCopyVersion(dto.getCopyVersion());
        insert.setRecordVersionId(procedureStepModel.getRecordVersionId());
        insert.setConfigJson(componentConfigJson);
        insert.setCompleted(false);
        insert.setActualTargetVolume(actualVolume);
        handleNameAndSave(insert);
        return insert;
    }

    private void handleNameAndSave(LiquidPreparationPlan insert) {
        Integer nextSerialNo = liquidPreparationPlanMapper.selectNextSerialNo(insert.getProductPlanId());
        insert.setSerialNo(nextSerialNo);
        if (nextSerialNo < 10) {
            insert.setName(insert.getBatchNo() + StrUtil.DASHED + 0 + nextSerialNo);
        } else {
            insert.setName(insert.getBatchNo() + StrUtil.DASHED + nextSerialNo);
        }
        liquidPreparationPlanMapper.insert(insert);
    }

    @Override
    public List<LiquidPreparationBoundMaterialBatchVO> getBoundMaterialBatch(LiquidPreparationBoundBatchQueryDTO dto) {
        List<LiquidPreparationMaterialBatchDetailInfo> list =
                liquidPreparationMaterialBatchMapper.selectDetailListByPreparationPlanId(dto);
        List<LiquidPreparationBoundMaterialBatchVO> result =
                LiquidPreparationPlanConverter.INSTANCE.convertToBatchVO(list);
        result.forEach(e -> {
            e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId()));
        });
        return result;
    }

    @Override
    public List<LiquidPreparationAvailableBoundMaterialBatchVO> getBoundAndAvailableMaterialBatch(LiquidPreparationAvailableBoundBatchQueryDTO dto) {
        LiquidPreparationPlan liquidPreparationPlan =
                liquidPreparationPlanMapper.selectById(dto.getPreparationPlanId());
        if (liquidPreparationPlan == null) {
            throw new BmosException(MesResponseCode.PREPARATION_PLAN_NOT_EXISTS);
        }
        ProductFormulaMaterial formulaMaterial =
                productFormulaConfigureService.getFormulaMaterialById(dto.getFormulaMaterialId());
        if (formulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        // 查询已预订的批次信息
        List<ReservedBatchInfo> reservedBatchInfos =
                storageMaterialBatchService.queryReservedBatch(liquidPreparationPlan.getProductPlanId(),
                        formulaMaterial.getMaterialId());
        List<LiquidPreparationAvailableBoundMaterialBatchVO> result =
                LiquidPreparationPlanConverter.INSTANCE.convertToAvailableBatchVO(reservedBatchInfos);
        // 查询当前配液计划物料的配液信息
        List<LiquidPreparationMaterialBatchDetailInfo> list =
                liquidPreparationMaterialBatchMapper.selectDetailListByPreparationPlanId(LiquidPreparationBoundBatchQueryDTO
                        .builder()
                        .preparationPlanId(liquidPreparationPlan.getId())
                        .formulaMaterialId(dto.getFormulaMaterialId())
                        .build());
        Map<Long, LiquidPreparationMaterialBatchDetailInfo> infoMap =
                CollectionUtils.convertMap(list, LiquidPreparationMaterialBatchDetailInfo::getMaterialBatchId);
        for (LiquidPreparationAvailableBoundMaterialBatchVO vo : result) {
            LiquidPreparationMaterialBatchDetailInfo info = infoMap.get(vo.getMaterialBatchId());
            if (info != null) {
                vo.setBound(true);
                vo.setPreparationQuantity(info.getPreparationQuantity());
            }
            // 物料量单位转换与修约
            vo.setMaterialQuantity(MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(vo.getMaterialQuantity(),
                    formulaMaterial.getUnitId()), formulaMaterial));
            vo.setUnitName(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completePreparationPlan(LiquidPreparationPlanCompleteDTO dto) {
        Long preparationPlanId = dto.getPreparationPlanId();
        LiquidPreparationPlan liquidPreparationPlan = liquidPreparationPlanMapper.selectById(preparationPlanId);
        if (liquidPreparationPlan == null) {
            throw new BmosException(MesResponseCode.PREPARATION_PLAN_NOT_EXISTS);
        }
        if (BooleanUtil.isTrue(liquidPreparationPlan.getCompleted())) {
            throw new BmosException(MesResponseCode.PREPARATION_PLAN_ALREADY_COMPLETED);
        }
        // 校验是否有配液信息
        List<LiquidPreparationMaterialBatchDetailInfo> list =
                liquidPreparationMaterialBatchMapper.selectDetailListByPreparationPlanId(LiquidPreparationBoundBatchQueryDTO
                        .builder()
                        .preparationPlanId(preparationPlanId)
                        .build());
        if (CollUtil.isEmpty(list)) {
            throw new BmosException(MesResponseCode.NO_ANY_PREPARATION_INFO);
        }
        // 更新配液计划状态
        liquidPreparationPlan.setCompleted(true);
        liquidPreparationPlanMapper.updateById(liquidPreparationPlan);
        // 处理组件回填数据
        handlePreparationPlanComponentData(liquidPreparationPlan, list);
    }

    /**
     * 处理配液计划组件数据
     *
     * @param instance 配液单实例信息
     * @param list     配液批次信息
     */
    private void handlePreparationPlanComponentData(LiquidPreparationPlan instance,
                                                    List<LiquidPreparationMaterialBatchDetailInfo> list) {
        List<ExecuteFormData> results = new ArrayList<>();
        // 配液计划组件执行上下文
        ProductionDetailInfo info = new ProductionDetailInfo();
        info.setLiquidPreparationBatchList(list);
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfoByPlanId(instance.getProductPlanId());
        info.setFormulaInfo(formulaInfo);
        ProcedureStepModel procedureStepModel =
                procedureStepModelService.getById(instance.getProcedureStepModelId());
        // 组件配置信息
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        ComponentListVO ingredientComponent =
                componentService.selectUsedComponentDetail(procedureStepModel.getRecordVersionId(),
                        procedureStepModel.getRecordItemId(), instance.getComponentId());
        // 填充值
        BusinessDataHandleBaseDTO dto =
                LiquidPreparationPlanConverter.INSTANCE.convertToBusinessDataBaseDTO(instance,
                        procedureStepModel);
        info.setDto(LiquidPreparationPlanConverter.INSTANCE.convertToBusinessComponentBatchSaveDTO(dto));
        info.setCustomFieldList(batchFieldService.queryMaterialAndBatchField(CollectionUtils.convertList(list,
                LiquidPreparationMaterialBatchDetailInfo::getMaterialBatchId)));
        preparationPlanComponentStrategy.handleBusinessComponent(results, ingredientComponent, info, configMap, null);
        executeFormDataService.saveResultsAndHandleRelationComponentData(results, instance.getProductPlanId(),
                instance.getProcedureStepModelId(), instance.getCopyVersion());
    }

    @Override
    public LiquidPreparationQuantityCalculateVO calculatePreparationQuantity(LiquidPreparationQuantityCalculateDTO dto) {
        LiquidPreparationPlan liquidPreparationPlan =
                liquidPreparationPlanMapper.selectById(dto.getPreparationPlanId());
        if (liquidPreparationPlan == null) {
            throw new BmosException(MesResponseCode.PREPARATION_PLAN_NOT_EXISTS);
        }
        BigDecimal actualTargetVolume = liquidPreparationPlan.getActualTargetVolume();
        String configJson = liquidPreparationPlan.getConfigJson();
        LiquidPreparationPlanConfig config = JsonUtils.parseObject(configJson, LiquidPreparationPlanConfig.class);
        List<LiquidPreparationPlanMaterial> formulaMaterialList = config.getMaterialList();
        Map<Long, LiquidPreparationPlanMaterial> materialMap = CollectionUtils.convertMap(formulaMaterialList,
                LiquidPreparationPlanMaterial::getFormulaMaterialId);
        LiquidPreparationPlanMaterial liquidPreparationPlanMaterial = materialMap.get(dto.getFormulaMaterialId());
        if (liquidPreparationPlanMaterial == null) {
            throw new BmosException(MesResponseCode.PREPARATION_MATERIAL_NOT_EXISTS);
        }
        // 浓度参数
        String consistenceParam = liquidPreparationPlanMaterial.getField();
        // 目标浓度
        BigDecimal targetConsistence = new BigDecimal(liquidPreparationPlanMaterial.getTargetConcentration());
        // 总配置点
        BigDecimal configurePoint = actualTargetVolume.multiply(targetConsistence);
        ProductFormulaMaterial formulaMaterial =
                productFormulaConfigureService.getFormulaMaterialById(dto.getFormulaMaterialId());
        if (formulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        List<ReservedBatchInfo> reservedBatchInfos =
                storageMaterialBatchService.queryReservedBatch(liquidPreparationPlan.getProductPlanId(),
                        dto.getMaterialBatchIdList());
        Map<Long, ReservedBatchInfo> batchMap = CollectionUtils.convertMap(reservedBatchInfos, ReservedBatchInfo::getMaterialBatchId);
        List<LiquidPreparationQuantityVO> vos = new ArrayList<>();
        for (Long batchId : dto.getMaterialBatchIdList()) {
            ReservedBatchInfo batch = batchMap.get(batchId);
            if (batch == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
            }
            batch.setMaterialQuantity(MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(batch.getMaterialQuantity(),
                    formulaMaterial.getUnitId()), formulaMaterial));
            vos.add(calculateBatchPreparationQuantity(vos, batch, configurePoint, formulaMaterial, consistenceParam));
        }
        LiquidPreparationQuantityCalculateVO result = new LiquidPreparationQuantityCalculateVO();
        BigDecimal calculateConfigurePoint =
                vos.stream().map(LiquidPreparationQuantityVO::getConfigurePoint).reduce(BigDecimal.ZERO,
                        BigDecimal::add);
        result.setPreparationQuantityList(vos);
        result.setSatisfied(calculateConfigurePoint.compareTo(configurePoint) >= 0);
        return result;
    }

    /**
     * 计算当前批次配液量
     *
     * @param vos                  已计算的配液量
     * @param batch                当前批次信息
     * @param configurePoint       总需配置点 (目标体积*目标浓度)
     * @param consistenceParamCode 浓度参数编码
     * @return
     */
    private LiquidPreparationQuantityVO calculateBatchPreparationQuantity(List<LiquidPreparationQuantityVO> vos,
                                                                          ReservedBatchInfo batch,
                                                                          BigDecimal configurePoint,
                                                                          ProductFormulaMaterial formulaMaterial,
                                                                          String consistenceParamCode) {
        LiquidPreparationQuantityVO vo = new LiquidPreparationQuantityVO();
        BigDecimal calculatedConfigurePoint =
                vos.stream().map(LiquidPreparationQuantityVO::getConfigurePoint).reduce(BigDecimal.ZERO,
                        BigDecimal::add);
        BigDecimal leaveConfigurePoint = configurePoint.subtract(calculatedConfigurePoint);
        vo.setMaterialBatchId(batch.getMaterialBatchId());
        if (BigDecimal.ZERO.compareTo(leaveConfigurePoint) == 0) {
            vo.setPreparationQuantity(BigDecimal.ZERO);
            vo.setConfigurePoint(BigDecimal.ZERO);
            return vo;
        }
        List<MaterialBatchFieldVO> fieldVOList = materialBatchFieldService.queryMaterialField(batch
        .getMaterialBatchId());
        Map<String, MaterialBatchFieldVO> fieldMap = CollectionUtils.convertMap(fieldVOList,
        MaterialBatchFieldVO::getField);
        MaterialBatchFieldVO materialBatchFieldVO = fieldMap.get(consistenceParamCode);
        if (materialBatchFieldVO == null || StrUtil.isEmpty(materialBatchFieldVO.getFieldValue())) {
            throw new BmosException(MesResponseCode.BATCH_PARAMS_NOT_EXISTS);
        }
        // 当前批次浓度参数获取的值
        BigDecimal param = new BigDecimal(materialBatchFieldVO.getFieldValue());
        // 当前预定批次的总配置点
        BigDecimal currentPoint = batch.getMaterialQuantity().multiply(param);
        // 若当前批次总配置点小于等于所需剩余 则全部配液
        if (currentPoint.compareTo(leaveConfigurePoint) <= 0) {
            vo.setPreparationQuantity(MaterialQuantityCalculateUtil.roundingOff(batch.getMaterialQuantity(),
                    formulaMaterial));
            vo.setConfigurePoint(MaterialQuantityCalculateUtil.roundingOff(vo.getPreparationQuantity().multiply(param), formulaMaterial));
            return vo;
        }
        // 当前批次配置点大于所需剩余配液
        RoundingMode roundingMode = RoundingEnum.getEnumByCode(formulaMaterial.getRounding()).getMapping();
        BigDecimal preparationQuantity = leaveConfigurePoint.divide(param, 20, roundingMode);
        vo.setPreparationQuantity(MaterialQuantityCalculateUtil.roundingOff(preparationQuantity, formulaMaterial));
        vo.setConfigurePoint(leaveConfigurePoint);
        return vo;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void BindMaterialBatch(LiquidPreparationBindMaterialBatchDTO dto) {
        if (CollUtil.isEmpty(dto.getMaterialBatchList())) {
            deleteBoundBatch(dto.getPreparationPlanId(), dto);
            return;
        }
        Long preparationPlanId = dto.getPreparationPlanId();
        LiquidPreparationPlan liquidPreparationPlan = liquidPreparationPlanMapper.selectById(preparationPlanId);
        boolean zero =
                dto.getMaterialBatchList().stream().anyMatch(e -> BigDecimal.ZERO.compareTo(e.getPreparationQuantity()) == 0);
        if (zero) {
            throw new BmosException(MesResponseCode.PREPARATION_QUANTITY_ZERO);
        }
        if (liquidPreparationPlan == null) {
            throw new BmosException(MesResponseCode.PREPARATION_PLAN_NOT_EXISTS);
        }
        ProductFormulaMaterial formulaMaterial =
                productFormulaConfigureService.getFormulaMaterialById(dto.getFormulaMaterialId());
        if (formulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        // 清除原绑定信息
        deleteBoundBatch(preparationPlanId, dto);
        LiquidPreparationPlanConfig config = JsonUtils.parseObject(liquidPreparationPlan.getConfigJson(),
                LiquidPreparationPlanConfig.class);
        List<LiquidPreparationPlanMaterial> formulaMaterialList = config.getMaterialList();
        int order = 0;
        for (int i = 0; i < formulaMaterialList.size(); i++) {
            LiquidPreparationPlanMaterial liquidPreparationPlanMaterial = formulaMaterialList.get(i);
            if (Objects.equals(liquidPreparationPlanMaterial.getFormulaMaterialId(), dto.getFormulaMaterialId())) {
                order = i;
                break;
            }
        }
        int finalOrder = order;
        List<LiquidPreparationMaterialBatch> collect = dto.getMaterialBatchList().stream().map(e -> {
            LiquidPreparationMaterialBatch bound = new LiquidPreparationMaterialBatch();
            bound.setLiquidPreparationPlanId(preparationPlanId);
            bound.setMaterialBatchId(e.getMaterialBatchId());
            bound.setMaterialBatchNo(e.getMaterialBatchNo());
            bound.setFormulaMaterialId(dto.getFormulaMaterialId());
            bound.setUnitId(formulaMaterial.getUnitId());
            bound.setPreparationQuantity(e.getPreparationQuantity());
            bound.setMaterialOrder(finalOrder);
            return bound;
        }).collect(Collectors.toList());
        liquidPreparationMaterialBatchMapper.insertBatch(collect);
    }

    private void deleteBoundBatch(Long dto, LiquidPreparationBindMaterialBatchDTO dto1) {
        liquidPreparationMaterialBatchMapper.deleteBoundBatch(LiquidPreparationBoundBatchQueryDTO.builder()
                .preparationPlanId(dto)
                .formulaMaterialId(dto1.getFormulaMaterialId())
                .build());
    }

    @Override
    public List<UnmeasuredPreparationPlanVO> getUnmeasuredPreparationPlanList(Long productPlanId) {
        List<UnmeasuredPreparationPlanVO> list =
                liquidPreparationPlanMapper.selectUnmeasuredPreparationPlanList(productPlanId);
        return list.stream().filter(e -> !Objects.equals(e.getBatchCount(), e.getMeasureCount())).collect(Collectors.toList());
    }
}
