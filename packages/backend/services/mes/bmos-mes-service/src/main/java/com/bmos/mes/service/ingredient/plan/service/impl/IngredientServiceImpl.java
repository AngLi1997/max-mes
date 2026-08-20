package com.bmos.mes.service.ingredient.plan.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.mes.common.enums.formula.DryAndPureTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.convert.ProductFormulaConverter;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.ingredient.plan.convert.IngredientPlanConverter;
import com.bmos.mes.service.ingredient.plan.dto.*;
import com.bmos.mes.service.ingredient.plan.mapper.IngredientMaterialBatchMapper;
import com.bmos.mes.service.ingredient.plan.mapper.IngredientPlanMapper;
import com.bmos.mes.service.ingredient.plan.model.IngredientMaterialBatch;
import com.bmos.mes.service.ingredient.plan.model.IngredientMaterialBatchDetailInfo;
import com.bmos.mes.service.ingredient.plan.model.IngredientPlan;
import com.bmos.mes.service.ingredient.plan.service.IngredientService;
import com.bmos.mes.service.ingredient.plan.vo.*;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.ProcessVersionService;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.convert.RecordComponentConvert;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.requisition.vo.RequisitionPlanMaterialVO;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientPlanMapper ingredientPlanMapper;

    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    private PlanService planService;

    @Autowired
    private ProcessVersionService processVersionService;

    @Autowired
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Autowired
    private ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private IngredientMaterialBatchMapper ingredientMaterialBatchMapper;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private IStorageMaterialBatchService storageMaterialBatchService;

    @Autowired
    private BatchRecordComponentService componentService;

    @Autowired
    private Map<String, BusinessComponentStrategy> strategyMap;

    @Autowired
    private ExecuteFormDataService executeFormDataService;

    @Override
    public IngredientPlanVO getMaterialIngredientPlanVO(IngredientQueryDTO dto) {
        Long procedureStepModelId = dto.getProcedureStepModelId();
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(procedureStepModelId);
        Plan plan = planService.getById(dto.getProductPlanId());
        ProcessVersion process = processVersionService.getByProcessIdAndVersion(plan.getProcessId(),
                plan.getProcessVersion());
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfo(process.getProductFormulaVersionId());
        UniqueComponentQueryDTO build = UniqueComponentQueryDTO.builder()
                .componentId(dto.getComponentId())
                .copyVersion(dto.getCopyVersion())
                .productPlanId(dto.getProductPlanId())
                .reuse(procedureStepModel.getReusable())
                .recordItemId(procedureStepModel.getRecordItemId())
                .recordVersionId(procedureStepModel.getRecordVersionId())
                .procedureStepModelId(dto.getProcedureStepModelId()).build();
        IngredientPlan ingredientPlan = ingredientPlanMapper.selectUnique(build);
        IngredientPlanVO result = new IngredientPlanVO();
        result.setProductPlanId(dto.getProductPlanId());
        if (ObjectUtil.isNull(ingredientPlan)) {
            IngredientPlan insert = new IngredientPlan();
            insert.setReuse(procedureStepModel.getReusable());
            insert.setProductPlanId(plan.getId());
            insert.setRecordItemId(procedureStepModel.getRecordItemId());
            insert.setProcedureStepModelId(procedureStepModel.getId());
            insert.setBatchNo(plan.getBatchNo());
            insert.setComponentId(dto.getComponentId());
            insert.setCopyVersion(dto.getCopyVersion());
            insert.setRecordVersionId(procedureStepModel.getRecordVersionId());
            handleNameAndSave(insert);
            result.setId(insert.getId());
            result.setName(insert.getName());
            result.setCompleted(false);
        } else {
            result.setId(ingredientPlan.getId());
            result.setName(ingredientPlan.getName());
            result.setCompleted(ingredientPlan.getCompleted());
        }
        List<BusinessComponentConfigDetailVO> componentConfig =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(componentConfig,
                BusinessComponentConfigDetailVO::getComponentId);
        BusinessComponentConfigDetailVO configDetail = configMap.get(dto.getComponentId());
        List<ProductFormulaMaterial> planMaterialList =
                productFormulaConfigureService.getProductFormulaMaterialList(process.getProductFormulaVersionId());
        if (ObjectUtil.isNotNull(configDetail)) {
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(configDetail.getConfigInfo(),
                    ProcedureStepConfigInfo.class);
            List<Long> formulaMaterialIdList = configInfo.getFormulaMaterialIds();
            if (CollUtil.isNotEmpty(formulaMaterialIdList)) {
                planMaterialList = planMaterialList.stream().filter(e-> formulaMaterialIdList.contains(e.getId())).collect(Collectors.toList());
            }
        }
        List<RequisitionPlanMaterialVO> planMaterialVOList = planMaterialList.stream().map(material -> {
            RequisitionPlanMaterialVO vo =
                    ProductFormulaConverter.INSTANCE.convertToRequisitionPlanMaterialVO(material);
            vo.setTheoreticalQuantity(BusinessComponentStrategy.calculateQuantity(plan.getBatchQuantity(), formulaInfo.getBatchQuantity(), material));
            return vo;
        }).collect(Collectors.toList());
        result.setMaterialList(planMaterialVOList);
        result.getMaterialList().forEach(m -> {
            m.setUnitName(unitCache.getGlobalUnitName(m.getUnitId()));
        });
        return result;
    }

    @Override
    public List<AvailableAndBoundMaterialBatchVO> getAvailableAndAddedMaterialBatch(IngredientAvailableAndBoundBatchQueryDTO dto) {
        List<IngredientMaterialBatchDetailInfo> list =
                ingredientMaterialBatchMapper.getByIngredientIdAndFormulaMaterialId(dto.getIngredientPlanId(),
                        dto.getFormulaMaterialId());
        ProductFormulaMaterial formulaMaterial =
                productFormulaConfigureService.getFormulaMaterialById(dto.getFormulaMaterialId());
        List<AvailableAndBoundMaterialBatchVO> reserved =
                IngredientPlanConverter.INSTANCE.convertToBoundMaterialBatchVOList(storageMaterialBatchService.queryReservedBatch(dto.getBatchId(), formulaMaterial.getMaterialId()));
        Set<Long> boundIds = CollectionUtils.convertSet(list, IngredientMaterialBatch::getMaterialBatchId);
        Map<Long, IngredientMaterialBatchDetailInfo> boundMap = CollectionUtils.convertMap(list,
                IngredientMaterialBatch::getMaterialBatchId);
        reserved.forEach(e -> {
            e.setMaterialQuantity(MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(e.getMaterialQuantity(),
                    formulaMaterial.getUnitId()), formulaMaterial));
            e.setUnitId(formulaMaterial.getUnitId());
            e.setUnitName(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
            if (boundIds.contains(e.getMaterialBatchId())) {
                IngredientMaterialBatch batch = boundMap.get(e.getMaterialBatchId());
                e.setBound(true);
                e.setIngredientQuantity(MaterialQuantityCalculateUtil.roundingOff(batch.getIngredientQuantity(),
                        formulaMaterial));
                e.setTheoreticalQuantity(MaterialQuantityCalculateUtil.roundingOff(batch.getTheoreticalQuantity(),
                        formulaMaterial));
            }
        });
        return reserved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ingredientBindMaterialBatch(IngredientBindMaterialBatchDTO dto) {
        ingredientMaterialBatchMapper.deleteByIngredientPlanIdAndFormulaMaterialId(dto.getIngredientPlanId(),
                dto.getFormulaMaterialId());
        List<IngredientMaterialBatch> collect = dto.getMaterialBatchList().stream().map(e -> {
            IngredientMaterialBatch batch = IngredientPlanConverter.INSTANCE.convertToIngredientMaterialBatch(e);
            batch.setIngredientPlanId(dto.getIngredientPlanId());
            batch.setFormulaMaterialId(dto.getFormulaMaterialId());
            return batch;
        }).collect(Collectors.toList());
        ingredientMaterialBatchMapper.insertBatch(collect);

    }

    @Override
    public List<IngredientBoundMaterialBatchVO> getBoundMaterialBatch(IngredientBoundMaterialBatchQueryDTO dto) {
        List<IngredientMaterialBatchDetailInfo> list =
                ingredientMaterialBatchMapper.getByIngredientIdAndFormulaMaterialId(dto.getIngredientPlanId(),
                        dto.getFormulaMaterialId());
        ProductFormulaMaterial formulaMaterial = productFormulaConfigureService.getFormulaMaterialById(dto.getFormulaMaterialId());
        if(formulaMaterial == null){
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        List<IngredientBoundMaterialBatchVO> result =
                IngredientPlanConverter.INSTANCE.convertDetailToBoundMaterialBatchVO(list);
        result.forEach(e -> {
            e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId()));
            e.setIngredientQuantity(MaterialQuantityCalculateUtil.roundingOff(e.getIngredientQuantity(), formulaMaterial));
        });
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeIngredientPlan(IngredientPlanCompleteDTO dto) {
        IngredientPlan ingredientPlan = ingredientPlanMapper.selectById(dto.getIngredientPlanId());
        if (ObjectUtil.isNull(ingredientPlan)) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_NOT_EXIST);
        }
        if (BooleanUtil.isTrue(ingredientPlan.getCompleted())) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_COMPLETED);
        }
        ComponentListVO ingredientComponent = componentService.selectUsedComponentDetail(dto.getRecordVersionId(),
                dto.getRecordItemId(), dto.getComponentId());
        List<IngredientMaterialBatchDetailInfo> ingredientMaterialBatchList =
                ingredientMaterialBatchMapper.getByIngredientId(ingredientPlan.getId());
        if (CollUtil.isEmpty(ingredientMaterialBatchList)){
            throw new BmosException(MesResponseCode.NO_ANY_INGREDIENT_INFO);
        }
        // 处理业务数据
        List<ExecuteFormData> results = new ArrayList<>();
        ProductionDetailInfo info = new ProductionDetailInfo();
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        dto.setReuse(procedureStepModel.getReusable());
        info.setDto(RecordComponentConvert.INSTANCE.convertToBusinessComponentBatchSaveDTO(dto));
        info.setUnitCache(unitCache);
        ProcessVersion processVersion = processVersionService.getByProcessIdAndVersion(dto.getProcessId(),
                dto.getProcessVersion());
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfo(processVersion.getProductFormulaVersionId());
        info.setFormulaInfo(formulaInfo);
        info.setIngredientMaterialBatchList(ingredientMaterialBatchList);
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        strategyMap.get(ingredientComponent.getComponentType()).handleBusinessComponent(results, ingredientComponent,
                info, configMap, null);
        executeFormDataService.saveResultsAndHandleRelationComponentData(results, dto);
        ingredientPlan.setCompleted(true);
        ingredientPlanMapper.updateById(ingredientPlan);
    }

    @Override
    public IngredientQuantityCalculateVO calculateTheoreticalQuantity(TheoreticalQuantityCalculateDTO dto) {
        IngredientPlan ingredientPlan = ingredientPlanMapper.selectById(dto.getIngredientPlanId());
        if (ingredientPlan == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_NOT_EXIST);
        }
        List<IngredientMaterialBatch> batchList =
                ingredientMaterialBatchMapper.selectByIngredientIdAndFormulaMaterialId(dto.getIngredientPlanId(),
                        dto.getFormulaMaterialId());

        Long formulaMaterialId = dto.getFormulaMaterialId();
        ProductFormulaMaterial formulaMaterial =
                productFormulaConfigureService.getFormulaMaterialById(formulaMaterialId);
        if (ObjectUtil.isNull(formulaMaterial)) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        IngredientQuantityCalculateVO result = new IngredientQuantityCalculateVO();
        // 理论总量
        BigDecimal theoreticalQuantity = dto.getTheoreticalQuantity();
        // 已选理论量
        BigDecimal chosenTheoreticalQuantity = null;
        if (CollUtil.isNotEmpty(batchList)) {
            chosenTheoreticalQuantity =
                    batchList.stream().map(IngredientMaterialBatch::getTheoreticalQuantity).reduce(BigDecimal.ZERO,
                            BigDecimal::add);
        } else {
            chosenTheoreticalQuantity = BigDecimal.ZERO;
        }
        // 剩余所需理论量
        BigDecimal needTheoretical = theoreticalQuantity.subtract(chosenTheoreticalQuantity);
        BigDecimal quantity = dto.getQuantity();
        DryAndPureTypeEnum dryPureType = formulaMaterial.getDryPureType();
        // 物料批次理论量
        BigDecimal batchTheoreticalQuantity = BigDecimal.ZERO;
        BigDecimal tempQuantity = BigDecimal.ZERO;
        BigDecimal hundred = new BigDecimal(100);
        int divideScale = 20;
        // 纯度
        BigDecimal content;
        if (hundred.compareTo(dto.getNoHydrationContent()) < 0) {
            content = BigDecimal.ONE;
        } else {
            content = dto.getNoHydrationContent().divide(hundred, divideScale, RoundingMode.DOWN);
        }
        // 水分
        BigDecimal hydration = dto.getHydration().divide(hundred, divideScale, RoundingMode.DOWN);
        switch (dryPureType) {
            case NO_TYPE:
                batchTheoreticalQuantity = quantity;
                tempQuantity = needTheoretical;
                break;
            case PURE:
                batchTheoreticalQuantity = quantity.multiply(content);
                tempQuantity = needTheoretical.divide(content, divideScale, RoundingMode.DOWN);
                break;
            case DRY_PURE:
                batchTheoreticalQuantity = quantity.multiply(BigDecimal.ONE.subtract(hydration)).multiply(content);
                tempQuantity = needTheoretical.divide(BigDecimal.ONE.subtract(hydration).multiply(content),
                        divideScale, RoundingMode.DOWN);
                break;
            case DRY_PURE_WITH_PARAM:
                BigDecimal dryPureParam = formulaMaterial.getDryPureParam();
                BigDecimal dryPure = BigDecimal.ONE.subtract(hydration).multiply(content);
                batchTheoreticalQuantity = quantity.multiply(dryPure.divide(dryPureParam, divideScale,
                        RoundingMode.DOWN));
                tempQuantity = needTheoretical.divide(dryPure.divide(dryPureParam, divideScale, RoundingMode.DOWN),
                        divideScale, RoundingMode.DOWN);
                break;
        }
        // 批次理论量少于所需理论量
        if (batchTheoreticalQuantity.compareTo(needTheoretical) <= 0) {
            result.setIngredientQuantity(BusinessComponentStrategy.roundingOff(quantity, formulaMaterial.getScale(),
                    formulaMaterial.getScaleLength(),
                    RoundingEnum.getEnumByCode(formulaMaterial.getRounding()).getMapping()));
            result.setTheoreticalQuantity(batchTheoreticalQuantity);
        } else {
            result.setIngredientQuantity(BusinessComponentStrategy.roundingOff(tempQuantity, formulaMaterial.getScale(),
                    formulaMaterial.getScaleLength(),
                    RoundingEnum.getEnumByCode(formulaMaterial.getRounding()).getMapping()));
            result.setTheoreticalQuantity(needTheoretical);
        }
        return result;
    }

    @Override
    public IngredientQuantityListCalculateVO calculateIngredientQuantity(IngredientQuantityCalculateDTO dto) {
        IngredientPlan ingredientPlan = ingredientPlanMapper.selectById(dto.getIngredientPlanId());
        if (ingredientPlan == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_NOT_EXIST);
        }
        Long productPlanId = ingredientPlan.getProductPlanId();
        Plan plan = planService.getById(productPlanId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        ProductFormulaInfo formulaInfo = productFormulaConfigureService.getProductFormulaInfoByPlanId(plan.getId());
        ProductFormulaMaterial formulaMaterial = formulaInfo.getMaterialMap().get(dto.getFormulaMaterialId());
        // 所需理论量
        BigDecimal needTotalTheoreticalQuantity = MaterialQuantityCalculateUtil.calculateQuantity(plan.getBatchQuantity(),
                formulaInfo.getBatchQuantity(),
                formulaMaterial);
        if (needTotalTheoreticalQuantity == null) {
            throw new BmosException(MesResponseCode.NEED_QUANTITY_NULL_PLEASE_CHECK_CONFIG);
        }
        // 已计算理论量
        List<AvailableAndBoundMaterialBatchVO> reserved =
                IngredientPlanConverter.INSTANCE.convertToBoundMaterialBatchVOList(storageMaterialBatchService.queryReservedBatch(plan.getId(), formulaMaterial.getMaterialId()));
        reserved.forEach(e -> {
            e.setMaterialQuantity(MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(e.getMaterialQuantity(),
                    formulaMaterial.getUnitId()), formulaMaterial));
            e.setUnitId(formulaMaterial.getUnitId());
            e.setUnitName(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
        });
        Map<Long, AvailableAndBoundMaterialBatchVO> batchMap = CollectionUtils.convertMap(reserved,
                AvailableAndBoundMaterialBatchVO::getMaterialBatchId);
        List<IngredientQuantityListVO> vos = new ArrayList<>();
        for (Long batchId : dto.getMaterialBatchIdList()) {
            AvailableAndBoundMaterialBatchVO batch = batchMap.get(batchId);
            IngredientQuantityListVO vo = getIngredientQuantityListVO(vos, batch, needTotalTheoreticalQuantity,
                    formulaMaterial);
            vos.add(vo);
        }
        IngredientQuantityListCalculateVO result = new IngredientQuantityListCalculateVO();
        result.setIngredientQuantityList(vos);
        result.setIngredientTotalQuantity(MaterialQuantityCalculateUtil.roundingOff(vos.stream()
                .map(IngredientQuantityListVO::getIngredientQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add), formulaMaterial));
        result.setChosenTheoreticalQuantity(MaterialQuantityCalculateUtil.roundingOff(vos.stream()
                .map(IngredientQuantityListVO::getTheoreticalQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add), formulaMaterial));
        return result;
    }

    private IngredientQuantityListVO getIngredientQuantityListVO(List<IngredientQuantityListVO> vos,
                                                                 AvailableAndBoundMaterialBatchVO batch,
                                                                 BigDecimal needTotalTheoreticalQuantity,
                                                                 ProductFormulaMaterial formulaMaterial) {
        IngredientQuantityListVO vo = new IngredientQuantityListVO();
        BigDecimal calculateTheoreticalQuantity =
                vos.stream().map(IngredientQuantityListVO::getTheoreticalQuantity).reduce(BigDecimal.ZERO,
                        BigDecimal::add);
        BigDecimal leaveNeedTheoreticalQuantity = needTotalTheoreticalQuantity.subtract(calculateTheoreticalQuantity);
        if (BigDecimal.ZERO.compareTo(leaveNeedTheoreticalQuantity) == 0) {
            vo.setTheoreticalQuantity(BigDecimal.ZERO);
            vo.setIngredientQuantity(BigDecimal.ZERO);
            vo.setMaterialBatchId(batch.getMaterialBatchId());
        }
        // 临时量 用于记录当前批次理论量大于所需理论量时反向计算的配料量
        BigDecimal tempQuantity = BigDecimal.ZERO;

        vo.setMaterialBatchId(batch.getMaterialBatchId());
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal quantity = batch.getMaterialQuantity();
        int divideScale = 20;
        // 纯度
        BigDecimal content;
        if (batch.getNoHydrationContent() == null || hundred.compareTo(batch.getNoHydrationContent()) < 0) {
            content = BigDecimal.ONE;
        } else {
            content = batch.getNoHydrationContent().divide(hundred, divideScale, RoundingMode.DOWN);
        }
        // 水分
        BigDecimal hydration = batch.getHydration() == null ? BigDecimal.ONE : batch.getHydration().divide(hundred,
                divideScale, RoundingMode.DOWN);
        // 干度
        BigDecimal dryness =  batch.getHydration() == null ? BigDecimal.ONE : BigDecimal.ONE.subtract(batch.getHydration().divide(hundred,
                divideScale, RoundingMode.DOWN));
        DryAndPureTypeEnum dryPureType = formulaMaterial.getDryPureType();
        BigDecimal currentTheoreticalQuantity = BigDecimal.ZERO;
        switch (dryPureType) {
            case NO_TYPE:
                currentTheoreticalQuantity = quantity;
                tempQuantity = leaveNeedTheoreticalQuantity;
                break;
            case PURE:
                currentTheoreticalQuantity = quantity.multiply(content);
                tempQuantity = leaveNeedTheoreticalQuantity.divide(content, divideScale, RoundingMode.DOWN);
                break;
            case DRY_PURE:
                currentTheoreticalQuantity = quantity.multiply(dryness).multiply(content);
                tempQuantity = leaveNeedTheoreticalQuantity.divide(dryness.multiply(content),
                        divideScale, RoundingMode.DOWN);
                break;
            case DRY_PURE_WITH_PARAM:
                BigDecimal dryPureParam = formulaMaterial.getDryPureParam();
                BigDecimal dryPure = dryness.multiply(content);
                currentTheoreticalQuantity = quantity.multiply(dryPure.divide(dryPureParam, divideScale,
                        RoundingMode.DOWN));
                tempQuantity = leaveNeedTheoreticalQuantity.divide(dryPure.divide(dryPureParam, divideScale,
                                RoundingMode.DOWN),
                        divideScale, RoundingMode.DOWN);
                break;
        }
        // 已经计算理论量和当前理论量之和小于等于所需理论量
        if (calculateTheoreticalQuantity.add(currentTheoreticalQuantity).compareTo(needTotalTheoreticalQuantity) <= 0) {
            vo.setIngredientQuantity(MaterialQuantityCalculateUtil.roundingOff(quantity, formulaMaterial.getScale(),
                    formulaMaterial.getScaleLength(),
                    RoundingEnum.getEnumByCode(formulaMaterial.getRounding()).getMapping()));
            vo.setTheoreticalQuantity(MaterialQuantityCalculateUtil.roundingOff(currentTheoreticalQuantity, formulaMaterial.getScale(),
                    formulaMaterial.getScaleLength(),
                    RoundingEnum.getEnumByCode(formulaMaterial.getRounding()).getMapping()));
        } else {
            vo.setTheoreticalQuantity(leaveNeedTheoreticalQuantity);
            vo.setIngredientQuantity(MaterialQuantityCalculateUtil.roundingOff(tempQuantity, formulaMaterial.getScale(),
                    formulaMaterial.getScaleLength(),
                    RoundingEnum.getEnumByCode(formulaMaterial.getRounding()).getMapping()));
        }
        return vo;
    }

    private void handleNameAndSave(IngredientPlan ingredient) {
        Integer nextSerialNo = ingredientPlanMapper.selectNextSerialNo(ingredient.getProductPlanId());
        ingredient.setSerialNo(nextSerialNo);
        if (nextSerialNo < 10) {
            ingredient.setName(ingredient.getBatchNo() + StrUtil.DASHED + 0 + nextSerialNo);
        } else {
            ingredient.setName(ingredient.getBatchNo() + StrUtil.DASHED + nextSerialNo);
        }
        ingredientPlanMapper.insert(ingredient);
    }
}
