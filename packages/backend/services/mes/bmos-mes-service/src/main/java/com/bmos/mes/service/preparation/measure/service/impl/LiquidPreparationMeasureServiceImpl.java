package com.bmos.mes.service.preparation.measure.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.formula.ToleranceTypeEnum;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.preparation.MeasureModeEnum;
import com.bmos.mes.common.enums.preparation.MeasureStageEnum;
import com.bmos.mes.common.enums.preparation.MeasureStatusEnum;
import com.bmos.mes.common.enums.preparation.MeasureTypeEnum;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.equipment.service.EquipmentCommonService;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.model.ProductFormulaToleranceInfo;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.ingredient.DiffUtil;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.preparation.measure.dto.*;
import com.bmos.mes.service.preparation.measure.mapper.LiquidPreparationMeasureBatchMapper;
import com.bmos.mes.service.preparation.measure.mapper.LiquidPreparationMeasureInstanceMapper;
import com.bmos.mes.service.preparation.measure.mapper.LiquidPreparationMeasureRecordMapper;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureBatch;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureInstance;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureRecord;
import com.bmos.mes.service.preparation.measure.service.LiquidPreparationMeasureLogService;
import com.bmos.mes.service.preparation.measure.service.LiquidPreparationMeasureService;
import com.bmos.mes.service.preparation.measure.service.vo.MeasuredBatchDetailVO;
import com.bmos.mes.service.preparation.measure.vo.*;
import com.bmos.mes.service.preparation.plan.mapper.LiquidPreparationMaterialBatchMapper;
import com.bmos.mes.service.preparation.plan.mapper.LiquidPreparationPlanMapper;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationMaterialBatch;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationPlan;
import com.bmos.mes.service.process.mapper.ProcedureStepConfigMapper;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.strategy.LiquidMeasureComponentStrategy;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.service.ICargoPositionService;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialQueryValidateDTO;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialReserveDTO;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialReserveMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.service.MaterialBatchFieldService;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialDetailVO;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.unit.service.UnitCache;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LiquidPreparationMeasureServiceImpl implements LiquidPreparationMeasureService {

    @Autowired
    private LiquidPreparationMeasureInstanceMapper measureInstanceMapper;

    @Autowired
    private LiquidPreparationPlanMapper liquidPreparationPlanMapper;

    @Autowired
    private LiquidPreparationMaterialBatchMapper liquidPreparationMaterialBatchMapper;

    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    private ProcedureStepConfigMapper procedureStepConfigMapper;

    @Autowired
    private LiquidPreparationMeasureBatchMapper measureBatchMapper;

    @Autowired
    private IStorageMaterialService storageMaterialService;

    @Autowired
    private EquipmentConfigFeign equipmentConfigFeign;

    @Autowired
    private IStorageMaterialBatchService storageMaterialBatchService;

    @Autowired
    private IStorageMaterialReserveMapper reserveMapper;

    @Autowired
    private EquipmentCommonService equipmentCommonService;

    @Autowired
    private ProductFormulaConfigureService formulaConfigureService;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private LiquidPreparationMeasureRecordMapper measureRecordMapper;

    @Autowired
    private ICargoPositionService cargoPositionService;

    @Autowired
    private PlanService planService;

    @Autowired
    private LiquidMeasureComponentStrategy liquidMeasureComponentStrategy;

    @Autowired
    private ExecuteFormDataService executeFormDataService;

    @Autowired
    private BatchRecordComponentService batchRecordComponentService;

    @Autowired
    private ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private LiquidPreparationMeasureLogService measureLogService;

    @Autowired
    private MaterialBatchFieldService materialBatchFieldService;

    @Resource
    private IStorageMaterialPositionLogService storageMaterialPositionLogService;


    @Override
    public LiquidMeasureInstanceVO getMeasureInstance(LiquidMeasureInstanceQueryDTO dto) {
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        LiquidPreparationMeasureInstance instance = measureInstanceMapper.selectUnique(UniqueComponentQueryDTO.builder()
                .productPlanId(dto.getProductPlanId())
                .procedureStepModelId(dto.getProcedureStepModelId())
                .reuse(procedureStepModel.getReusable())
                .copyVersion(dto.getCopyVersion())
                .componentId(dto.getComponentId())
                .build());
        // 初次进入 初始化组件实例
        if (instance == null) {
            LiquidPreparationMeasureInstance insert = getInitMeasureInstance(dto, procedureStepModel);
            measureInstanceMapper.insert(insert);
            instance = insert;
        }
        LiquidMeasureInstanceVO result = new LiquidMeasureInstanceVO();
        result.setId(instance.getId());
        result.setLiquidPreparationPlanId(instance.getLiquidPreparationPlanId());
        if (instance.getLiquidPreparationPlanId() != null) {
            LiquidPreparationPlan liquidPreparationPlan =
                    liquidPreparationPlanMapper.selectById(instance.getLiquidPreparationPlanId());
            result.setLiquidPreparationPlanName(liquidPreparationPlan.getName());
        }
        // 正在量取的信息
        LiquidPreparationMeasureBatch measureBatch = measureBatchMapper.selectMeasuringBatch(instance.getId());
        if (measureBatch != null) {
            LiquidPreparationMaterialBatch planBatch =
                    liquidPreparationMaterialBatchMapper.selectById(measureBatch.getLiquidPreparationPlanBatchId());
            ProductFormulaMaterial formulaMaterial =
                    formulaConfigureService.getFormulaMaterialById(planBatch.getFormulaMaterialId());
            result.setMeasuringBatchId(measureBatch.getId());
            result.setMeasuringBatchNo(planBatch.getMaterialBatchNo());
            result.setMeasuringBatchUnitId(planBatch.getUnitId());
            result.setMaterialBatchId(measureBatch.getMaterialBatchId());
            result.setPlanBatchId(planBatch.getId());
            result.setMeasuringBatchUnitName(unitCache.getGlobalUnitName(planBatch.getUnitId()));
            result.setMaterialFullName(formulaMaterial.getMaterialMergeCode() + StrUtil.DASHED + formulaMaterial.getMaterialName());
        }
        return result;
    }

    @Override
    public LiquidPreparationDetailVO queryLiquidPreparationPlanDetail(LiquidPreparationPlanDetailQueryDTO dto) {
        LiquidPreparationPlan liquidPreparationPlan =
                liquidPreparationPlanMapper.selectById(dto.getLiquidPreparationId());
        if (liquidPreparationPlan == null) {
            throw new BmosException(MesResponseCode.PREPARATION_PLAN_NOT_EXISTS);
        }
        LiquidPreparationDetailVO result = new LiquidPreparationDetailVO();
        result.setId(liquidPreparationPlan.getId());
        result.setName(liquidPreparationPlan.getName());
        LiquidPreparationMeasureInstance instance = measureInstanceMapper.selectById(dto.getId());
        if (instance == null) {
            throw new BmosException(MesResponseCode.LIQUID_MEASURE_INSTANCE_NOT_EXISTS);
        }
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(instance.getProcedureStepModelId());
        // 当工艺配置配液量取业务组件绑定了物料，添加批次时应只回显对应的物料
        List<Long> formulaMaterialIds = Optional.of(procedureStepModel)
                .map(psm -> procedureStepConfigMapper.selectComponentConfig(
                        psm.getId(),
                        instance.getComponentId(),
                        psm.getReusable(),
                        psm.getProcessId(),
                        psm.getProcessVersion()))
                .map(ProcedureStepConfig::getConfigInfo)
                .map(configStr -> JSONUtil.toBean(configStr, ProcedureStepConfigInfo.class))
                .map(ProcedureStepConfigInfo::getFormulaMaterialIds)
                .orElse(new ArrayList<>());
        // 查询配料单批次信息
        List<LiquidPreparationDetailBatchVO> voList =
                liquidPreparationMaterialBatchMapper.selectMeasureInfoByPlanId(liquidPreparationPlan.getId());
        if (CollUtil.isNotEmpty(formulaMaterialIds)) {
            voList =
                    voList.stream().filter(e -> formulaMaterialIds.contains(e.getFormulaMaterialId())).collect(Collectors.toList());
        }
        result.setBatchList(voList);
        List<LiquidPreparationMeasureRecord> records = measureRecordMapper.selectByMeasureInstanceId(instance.getId())
                .stream()
                .filter(e-> Objects.equals(e.getMeasureType(), MeasureTypeEnum.LIQUID_MEASURE))
                .collect(Collectors.toList());
        List<LiquidPreparationMeasureBatch> measureBatches =
                measureBatchMapper.selectByPreparationPlanId(liquidPreparationPlan.getId());
        Map<Long, LiquidPreparationMeasureBatch> measureBatchMap = CollectionUtils.convertMap(measureBatches,
                LiquidPreparationMeasureBatch::getLiquidPreparationPlanBatchId);
        Map<Long, List<LiquidPreparationMeasureRecord>> batchMap = CollectionUtils.convertMultiMap(records,
                LiquidPreparationMeasureRecord::getStorageMaterialBatchId);
        // 处理已量取和未量取的值
        for (LiquidPreparationDetailBatchVO item : voList) {
            List<LiquidPreparationMeasureRecord> list = batchMap.get(item.getMaterialBatchId());
            item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
            item.setMeasuredQuantity(CollUtil.isEmpty(list) ? BigDecimal.ZERO :
                    list.stream()
                            .map(LiquidPreparationMeasureRecord::getQuantity)
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
            BigDecimal unmeasured = item.getPreparationQuantity().subtract(item.getMeasuredQuantity());
            item.setUnmeasuredQuantity(unmeasured.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : unmeasured);
            LiquidPreparationMeasureBatch measureBatch = measureBatchMap.get(item.getId());
            item.setMeasureStatus(measureBatch == null ? MeasureStatusEnum.UNMEASURED :
                    measureBatch.getMeasureStatus());
        }
        return result;
    }

    @Override
    public LiquidMeasureMaterialPieceVO scanLiquidMeasureMaterialPiece(LiquidMeasureMaterialPieceQueryDTO dto) {
        LiquidPreparationMeasureInstance instance = measureInstanceMapper.selectById(dto.getId());
        if (instance == null) {
            throw new BmosException(MesResponseCode.LIQUID_MEASURE_INSTANCE_NOT_EXISTS);
        }
        StorageMaterialDetailVO detail = storageMaterialService.queryByCodeAndValidate(StorageMaterialQueryValidateDTO
                .builder()
                .no(dto.getCode())
                .productPlanId(instance.getProductPlanId())
                .build());
        StorageMaterial storageMaterial = detail.getStorageMaterial();
        // 是否符合配液批次
        if (!Objects.equals(dto.getMaterialBatchId(), storageMaterial.getStorageMaterialBatchId())) {
            throw new BmosException(MesResponseCode.MATERIAL_PIECE_NOT_MATCH_MEASURE_BATCH);
        }
        detail.validateAll();
        return generateMeasureMaterialVO(storageMaterial, detail.getStorageMaterialBatch(), instance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long confirmMeasure(LiquidPreparationConfirmMeasureDTO dto) {
        Long id = dto.getId();
        LiquidPreparationMeasureInstance instance = measureInstanceMapper.selectById(id);
        if (instance == null) {
            throw new BmosException(MesResponseCode.LIQUID_MEASURE_INSTANCE_NOT_EXISTS);
        }
        boolean firstIn = instance.getPreMeasurerId() == null;
        // 必须要有量取人
        if (firstIn && (dto.getMeasurerId() == null || dto.getReCheckerId() == null)) {
            throw new BmosException(MesResponseCode.FIRST_TIME_MEASURE_MUST_HAVE_MEASURER);
        }
        // 配液批次
        LiquidPreparationMaterialBatch preparationBatch =
                liquidPreparationMaterialBatchMapper.selectById(dto.getPlanBatchId());
        if (preparationBatch == null) {
            throw new BmosException(MesResponseCode.LIQUID_PREPARATION_BATCH_NOT_EXISTS);
        }
        // 量取批次记录
        LiquidPreparationMeasureBatch measureBatch =
                measureBatchMapper.selectByLiquidPreparationPlanBatchId(dto.getPlanBatchId());
        if (measureBatch == null) {
            measureBatch = new LiquidPreparationMeasureBatch();
            measureBatch.setLiquidPreparationPlanBatchId(preparationBatch.getId());
            measureBatch.setMeasureInstanceId(instance.getId());
            measureBatch.setMaterialBatchId(preparationBatch.getMaterialBatchId());
            measureBatch.setMeasureStatus(MeasureStatusEnum.MEASURING);
            measureBatch.setMeasureStage(MeasureStageEnum.LIQUID_MEASURE);
            measureBatch.setLiquidPreparationPlanId(preparationBatch.getLiquidPreparationPlanId());
            measureBatchMapper.insert(measureBatch);
        }
        // 添加投入物料量
        if (CollUtil.isNotEmpty(dto.getConsumeStorateMaterialIdList())) {
            BigDecimal basicQuantity = measureConsume(dto.getConsumeStorateMaterialIdList(),
                    planService.getById(instance.getProductPlanId()), dto.getRemark());
            ProductFormulaMaterial formulaMaterial =
                    formulaConfigureService.getFormulaMaterialById(preparationBatch.getFormulaMaterialId());
            BigDecimal roundingQuantity = MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(basicQuantity,
                    formulaMaterial.getUnitId()), formulaMaterial);
            measureBatch.putQuantity(roundingQuantity);
        }
        // 更新量取批次信息
        measureBatch.setMeasurerId(dto.getMeasurerId());
        measureBatch.setReCheckerId(dto.getReCheckerId());
        measureBatch.setRemark(dto.getRemark());
        measureBatchMapper.updateById(measureBatch);
        // 首次进入需要更新量取组件绑定的配液计划
        if (firstIn) {
            instance.setLiquidPreparationPlanId(preparationBatch.getLiquidPreparationPlanId());
            instance.setPreMeasurerId(dto.getMeasurerId());
            instance.setPreReCheckerId(dto.getReCheckerId());
            measureInstanceMapper.updateById(instance);
        }
        return measureBatch.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addConsumeStorageMaterial(LiquidPreparationAddMaterialDTO dto) {
        LiquidPreparationMeasureBatch measureBatch = measureBatchMapper.selectById(dto.getMeasureBatchId());
        if (measureBatch == null) {
            throw new BmosException(MesResponseCode.LIQUID_PREPARATION_MEASURE_BATCH_NOT_EXISTS);
        }
        LiquidPreparationMeasureInstance instance =
                measureInstanceMapper.selectById(measureBatch.getMeasureInstanceId());
        BigDecimal basicQuantity = measureConsume(dto.getConsumeStorateMaterialIdList(),
                planService.getById(instance.getProductPlanId()), null);
        LiquidPreparationMaterialBatch planBatch =
                liquidPreparationMaterialBatchMapper.selectById(measureBatch.getLiquidPreparationPlanBatchId());
        ProductFormulaMaterial formulaMaterial =
                formulaConfigureService.getFormulaMaterialById(planBatch.getFormulaMaterialId());
        BigDecimal roundingQuantity = MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(basicQuantity,
                formulaMaterial.getUnitId()), formulaMaterial);
        measureBatch.putQuantity(roundingQuantity);
        measureBatchMapper.updateById(measureBatch);
    }

    @Override
    public LiquidPreparationMeasureBatchDetailVO queryMeasureBatchDetailInfo(Long measureBatchId) {
        // 量取批次
        LiquidPreparationMeasureBatch measureBatch = measureBatchMapper.selectById(measureBatchId);
        if (measureBatch == null) {
            throw new BmosException(MesResponseCode.LIQUID_PREPARATION_MEASURE_BATCH_NOT_EXISTS);
        }
        // 配液批次
        LiquidPreparationMaterialBatch planBatch =
                liquidPreparationMaterialBatchMapper.selectById(measureBatch.getLiquidPreparationPlanBatchId());
        ProductFormulaMaterial formulaMaterial =
                formulaConfigureService.getFormulaMaterialById(planBatch.getFormulaMaterialId());
        LiquidPreparationMeasureBatchDetailVO result = getMeasureBatchDetailBasicInfoVO(measureBatch, formulaMaterial
                , planBatch);
        // 量取组件实例
        LiquidPreparationMeasureInstance instance =
                measureInstanceMapper.selectById(measureBatch.getMeasureInstanceId());
        if (instance == null) {
            return result;
        }
        result.setMeasurerId(instance.getPreMeasurerId());
        result.setMeasurerName(UserUtils.getUsername(instance.getPreMeasurerId()));
        result.setReCheckerId(instance.getPreReCheckerId());
        result.setReCheckerName(UserUtils.getUsername(instance.getPreReCheckerId()));
        // 处理已量取信息
        List<LiquidPreparationMeasureRecord> measureRecords =
                measureRecordMapper.selectByMeasureBatchId(measureBatchId, null);
        BigDecimal measuredQuantity = measureRecords.stream()
                .filter(e -> Objects.equals(e.getMeasureType(), MeasureTypeEnum.LIQUID_MEASURE))
                .map(LiquidPreparationMeasureRecord::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
        switch (measureBatch.getMeasureStage()) {
            case LIQUID_MEASURE:
                // 配液目标量为配液计划批次量
                result.setTargetQuantity(planBatch.getPreparationQuantity());
                result.setMeasuredQuantity(measuredQuantity);
                result.setUnmeasuredQuantity(result.getTargetQuantity().subtract(result.getMeasuredQuantity()));
                break;
            case LIQUID_ODD:
                BigDecimal oddMeasuredQuantity = measureRecords.stream()
                        .filter(e -> Objects.equals(e.getMeasureType(), MeasureTypeEnum.ODD_LIQUID_MEASURE))
                        .map(LiquidPreparationMeasureRecord::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
                // 余液目标量为投入总量减去配液已量取量
                result.setTargetQuantity(result.getTotalQuantity().subtract(measuredQuantity));
                // 已量取量
                result.setMeasuredQuantity(oddMeasuredQuantity);
                result.setUnmeasuredQuantity(result.getTargetQuantity().subtract(result.getMeasuredQuantity()));
                break;
        }

        // 处理允差范围
        handleToleranceDiff(formulaMaterial, result);
        return result;
    }

    private void handleToleranceDiff(ProductFormulaMaterial formulaMaterial,
                                     LiquidPreparationMeasureBatchDetailVO result) {
        // 配液允差范围
        ProductFormulaToleranceInfo toleranceInfo = formulaMaterial.getToleranceInfo();
        MeasureStageEnum measureStage = result.getMeasureStage();
        switch (measureStage) {
            case LIQUID_MEASURE:
                result.setToleranceUpper(toleranceInfo.getLiquidMeasureToleranceUpper());
                result.setToleranceLower(toleranceInfo.getLiquidMeasureToleranceLower());
                result.setToleranceType(ToleranceTypeEnum.getEnumByValue(toleranceInfo.getLiquidMeasureToleranceType()));
                BigDecimal[] ingDiff = DiffUtil.diff(checkZero(result.getTargetQuantity()),
                        result.getToleranceUpper(),
                        result.getToleranceLower(),
                        result.getToleranceType(),
                        formulaMaterial.getScale(), formulaMaterial.getScaleLength());
                result.setToleranceDiff(new BigDecimal[]{
                        checkZero(ingDiff[0] == null ? null :
                                ingDiff[0].subtract(result.getMeasuredQuantity()).stripTrailingZeros()),
                        checkZero(ingDiff[1] == null ? null :
                                ingDiff[1].subtract(result.getMeasuredQuantity()).stripTrailingZeros()),
                        checkZero(ingDiff[2] == null ? null :
                                ingDiff[2].subtract(result.getMeasuredQuantity()).stripTrailingZeros()),
                });
                break;
            case LIQUID_ODD:
                result.setToleranceUpper(toleranceInfo.getOddLiquidMeasureToleranceUpper());
                result.setToleranceLower(toleranceInfo.getOddLiquidMeasureToleranceLower());
                result.setToleranceType(ToleranceTypeEnum.getEnumByValue(toleranceInfo.getOddLiquidMeasureToleranceType()));
                // 余液允差范围
                BigDecimal[] oddDiff =
                        DiffUtil.diff(checkZero(result.getTargetQuantity()),
                                result.getToleranceUpper(),
                                result.getToleranceLower(),
                                result.getToleranceType()
                                , formulaMaterial.getScale(), formulaMaterial.getScaleLength());
                result.setToleranceDiff(new BigDecimal[]{
                        checkZero(oddDiff[0] == null ? null :
                                (oddDiff[0].subtract(result.getMeasuredQuantity())).stripTrailingZeros()),
                        checkZero(oddDiff[1] == null ? null :
                                (oddDiff[1].subtract(result.getMeasuredQuantity())).stripTrailingZeros()),
                        checkZero(oddDiff[2] == null ? null :
                                (oddDiff[2].subtract(result.getMeasuredQuantity())).stripTrailingZeros()),
                });
                break;
        }
    }

    private BigDecimal checkZero(BigDecimal value) {
        if (value == null) {
            return null;
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return value;
    }

    @Override
    public LiquidMeasureResultVO queryMeasureResult(LiquidMeasureResultQueryDTO dto) {
        LiquidPreparationMeasureInstance instance = measureInstanceMapper.selectById(dto.getId());
        if (instance == null) {
            throw new BmosException(MesResponseCode.LIQUID_MEASURE_INSTANCE_NOT_EXISTS);
        }
        LiquidMeasureResultVO result = new LiquidMeasureResultVO();
        BaseUserDO measurer =
                Optional.ofNullable(UserUtils.getUser(instance.getPreMeasurerId())).orElse(new BaseUserDO());
        BaseUserDO reChecker =
                Optional.ofNullable(UserUtils.getUser(instance.getPreReCheckerId())).orElse(new BaseUserDO());
        result.setMeasurerId(instance.getPreMeasurerId());
        result.setReCheckerId(instance.getPreReCheckerId());
        result.setMeasurerName(measurer.getUserName());
        result.setMeasurerLoginName(measurer.getLoginName());
        result.setReCheckerName(reChecker.getUserName());
        result.setReCheckerLoginName(reChecker.getLoginName());
        // 处理配液余液量取结果列表
        List<MeasureResultRecordVO> recordVOS = measureRecordMapper.selectResultVOByMeasureInstanceId(dto.getId());
        recordVOS.forEach(e->e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId())));
        Map<MeasureTypeEnum, List<MeasureResultRecordVO>> measureMap = CollectionUtils.convertMultiMap(recordVOS,
                MeasureResultRecordVO::getMeasureType);
        result.setMeasureList(measureMap.getOrDefault(MeasureTypeEnum.LIQUID_MEASURE, new ArrayList<>()));
        result.setOddMeasureList(measureMap.getOrDefault(MeasureTypeEnum.ODD_LIQUID_MEASURE, new ArrayList<>()));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MeasurePrintResultVO measureAndPrint(LiquidMeasureAndPrintDTO dto) {
        // 量取批次
        LiquidPreparationMeasureBatch measureBatch = measureBatchMapper.selectById(dto.getMeasureBatchId());
        // 配液批次
        LiquidPreparationMaterialBatch planBatch =
                liquidPreparationMaterialBatchMapper.selectById(measureBatch.getLiquidPreparationPlanBatchId());
        if (Objects.equals(measureBatch.getMeasureStage(), MeasureStageEnum.COMPLETED)) {
            throw new BmosException(MesResponseCode.LIQUID_MEASURE_BATCH_COMPLETED);
        }
        // 量取组件实例
        LiquidPreparationMeasureInstance instance =
                measureInstanceMapper.selectById(measureBatch.getMeasureInstanceId());
        if (!Objects.equals(instance.getPreMeasurerId(), SysUserHolder.getUser().getUserId())) {
            throw new BmosException(MesResponseCode.LOGIN_USER_NOT_MATCH_MEASURER);
        }
        dto.validateQuantity();
        // 配方物料
        ProductFormulaMaterial productFormulaMaterial =
                formulaConfigureService.getFormulaMaterialById(planBatch.getFormulaMaterialId());

        MeasureStageEnum measureStage = measureBatch.getMeasureStage();
        // 当前量取类型 配液量取/余液量取
        MeasureTypeEnum measureType = Objects.equals(measureStage, MeasureStageEnum.LIQUID_MEASURE) ?
                MeasureTypeEnum.LIQUID_MEASURE : MeasureTypeEnum.ODD_LIQUID_MEASURE;
        MeasurePrintResultVO result = new MeasurePrintResultVO();
        List<LiquidPreparationMeasureRecord> list = measureRecordMapper.selectByMeasureBatchId(measureBatch.getId(),
                measureType);
        // 已量取量
        BigDecimal existSum = list.stream().map(LiquidPreparationMeasureRecord::getQuantity).reduce(BigDecimal.ZERO,
                BigDecimal::add);
        // 加上当前量取的总量
        BigDecimal allQuantity = existSum.add(dto.getMeasureQuantity());

        // 量取记录
        LiquidPreparationMeasureRecord record = initMeasureRecord(dto, measureBatch, planBatch, productFormulaMaterial);
        // 允差校验
        boolean updateBatch = false;
        switch (measureType) {
            case LIQUID_MEASURE:
                updateBatch = liquidMeasure(planBatch, productFormulaMaterial, measureBatch, allQuantity);
                break;
            case ODD_LIQUID_MEASURE:
                updateBatch = oddLiquidMeasure(measureBatch, allQuantity, productFormulaMaterial);
                break;
        }
        if (updateBatch) {
            measureBatchMapper.updateById(measureBatch);
        }
        // 生成物料件
        StorageMaterial storageMaterial = new StorageMaterial();
        StorageMaterialBatch materialBatch = storageMaterialBatchService.getById(measureBatch.getMaterialBatchId());

        EquipmentInfoFeignVO container;
        if (dto.getContainerId() != null) {
            container = FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByEquipmentId(data),
                    dto.getContainerId()).getData();
            if (container == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_NOT_EXIST);
            }
            StorageMaterial existContainer =
                    storageMaterialService.selectStorageMaterialByContainerId(container.getId());
            if (existContainer != null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_OCCUPY);
            }
            String containerName = getContainerName(container);
            record.setContainerId(container.getId());
            record.setContainerName(containerName);
            storageMaterial.setContainerId(container.getId());
            storageMaterial.setContainer(containerName);
        }
        // 货位
        if (dto.getMaterialPositionId() != null) {
            CargoPosition cargoPosition = cargoPositionService.getByIdWithPermission(dto.getMaterialPositionId());
            if (cargoPosition == null) {
                throw new BmosException(MesResponseCode.CARGO_POSITION_NOT_EXIST);
            }
            record.setMaterialPosition(cargoPosition.getCode() + StrUtil.DASHED + cargoPosition.getPosition());
            storageMaterial.setMaterialPositionId(cargoPosition.getId());
        }
        BigDecimal basicQuantity = unitCache.toBasic(dto.getMeasureQuantity(),
                productFormulaMaterial.getUnitId());
        storageMaterial.setMaterialId(materialBatch.getMaterialId());
        storageMaterial.setStorageMaterialBatchId(materialBatch.getId());
        storageMaterial.setNo(storageMaterialService.getSerial());
        storageMaterial.setInitQuantity(basicQuantity);
        storageMaterial.setAvailableQuantity(basicQuantity);
        storageMaterial.setConsumeQuantity(BigDecimal.ZERO);
        storageMaterial.setUnitId(materialBatch.getUnitId());
        storageMaterial.setUnitExtendId(materialBatch.getUnitExtendId());
        storageMaterial.setReserveQuantity(BigDecimal.ZERO);
        storageMaterial.setSignStatus(WeighSignStatus.UN_SIGNED);
        storageMaterial.setProductPlanId(instance.getProductPlanId());
        storageMaterialService.save(storageMaterial);
        // 确认编码
        storageMaterialService.confirmSerial(storageMaterial.getNo());
        Plan plan = planService.getById(instance.getProductPlanId());
        // 配液量取需要预定到当前生产批次
        if (Objects.equals(measureType, MeasureTypeEnum.LIQUID_MEASURE)) {
            // 预定物料件
            storageMaterialService.reserve(StorageMaterialReserveDTO.builder()
                    .storageMaterialId(storageMaterial.getId())
                    .processId(plan.getProcessId())
                    .batchId(plan.getId())
                    .productId(plan.getProductId())
                    .reCheckerId(measureBatch.getReCheckerId())
                    .operatorId(measureBatch.getMeasurerId())
                    .remark(measureBatch.getRemark())
                    .build());
        }

        record.setStorageMaterialId(storageMaterial.getId());
        record.setStorageMaterialNo(storageMaterial.getNo());
        measureRecordMapper.insert(record);
        // 量取日志
        saveMeasureLog(record, measureType, instance, planBatch, productFormulaMaterial);
        // 货位物料日志
        StorageMaterialPositionLogDTO log = StorageMaterialPositionLogDTO.builder()
                .materialPositionId(dto.getMaterialPositionId())
                .storageMaterialId(storageMaterial.getId())
                .operateType(Objects.equals(measureType, MeasureTypeEnum.LIQUID_MEASURE) ?
                        StorageOperateTypeEnum.MEASURE_WEIGH : StorageOperateTypeEnum.MEASURE_ODD)
                .quantity(unitCache.toExt(basicQuantity, storageMaterial.getFinalUnitId()))
                .unitId(storageMaterial.getFinalUnitId())
                .senderId(measureBatch.getMeasurerId())
                .receiverId(measureBatch.getReCheckerId())
                .productName(plan.getProductName())
                .productCode(plan.getProductMergeCode())
                .productBatchNo(plan.getBatchNo())
                .build();
        storageMaterialPositionLogService.saveLog(log);
        // 组件数据处理
        generateResultsAndSave(instance, productFormulaMaterial, plan);
        // 展示结果
        switch (measureType) {
            case LIQUID_MEASURE:
                result.setTargetQuantity(planBatch.getPreparationQuantity());
                result.setMeasuredQuantity(allQuantity);
                result.setUnmeasuredQuantity(result.getTargetQuantity().subtract(result.getMeasuredQuantity()));
                break;
            case ODD_LIQUID_MEASURE:
                result.setTargetQuantity(measureBatch.getPutQuantity().subtract(measureRecordMapper.selectByMeasureBatchId(measureBatch.getId(),
                        MeasureTypeEnum.LIQUID_MEASURE).stream().map(LiquidPreparationMeasureRecord::getQuantity).reduce(BigDecimal.ZERO,BigDecimal::add)));
                result.setMeasuredQuantity(allQuantity);
                result.setUnmeasuredQuantity(result.getTargetQuantity().subtract(result.getMeasuredQuantity()));
                break;
        }
        result.setNo(storageMaterial.getNo());
        result.setQuantity(measureBatch.getPutQuantity());
        result.setUnitName(unitCache.getGlobalUnitName(productFormulaMaterial.getUnitId()));
        result.setNextMeasureStage(measureBatch.getMeasureStage());
        list.add(record);
        List<Long> materialPositionIds =
                list.stream().map(LiquidPreparationMeasureRecord::getMaterialPositionId).collect(Collectors.toList());
        Map<Long, CargoPosition> postionMap = CollectionUtil.isEmpty(materialPositionIds) ? new HashMap<>()
                : cargoPositionService.getByIdList(materialPositionIds)
                .stream()
                .collect(Collectors.toMap(BaseDO::getId, Function.identity(), (k1, k2) -> k2));
        result.setResultList(list.stream().map(e -> {
            MeasurePrintResultVO.MeasureResultItem res = new MeasurePrintResultVO.MeasureResultItem();
            res.setMeasureQuantity(e.getQuantity());
            res.setStorageMaterialNo(e.getStorageMaterialNo());
            res.setStorageMateriaId(e.getStorageMaterialId());
            res.setUnitName(unitCache.getGlobalUnitName(e.getUnitId()));
            res.setMaterialPositionName(e.getMaterialPositionId() == null ? null :
                    Optional.of(e.getMaterialPositionId())
                            .map(postionMap::get)
                            .map(cargoPosition -> cargoPosition.getCode() + "-" + cargoPosition.getPosition())
                            .orElse(null));
            res.setContainerName(e.getContainerName());
            return res;
        }).collect(Collectors.toList()));
        return result;
    }

    @NotNull
    private static LiquidPreparationMeasureRecord initMeasureRecord(LiquidMeasureAndPrintDTO dto,
                                                                    LiquidPreparationMeasureBatch measureBatch,
                                                                    LiquidPreparationMaterialBatch planBatch,
                                                                    ProductFormulaMaterial productFormulaMaterial) {
        LiquidPreparationMeasureRecord record = new LiquidPreparationMeasureRecord();
        MeasureTypeEnum measureType = Objects.equals(measureBatch.getMeasureStage(), MeasureStageEnum.LIQUID_MEASURE) ?
                MeasureTypeEnum.LIQUID_MEASURE : MeasureTypeEnum.ODD_LIQUID_MEASURE;
        record.setMeasureInstanceId(measureBatch.getMeasureInstanceId());
        record.setLiquidPreparationPlanId(planBatch.getLiquidPreparationPlanId());
        record.setMeasureBatchId(measureBatch.getId());
        record.setQuantity(dto.getMeasureQuantity());
        record.setUnitId(planBatch.getUnitId());
        record.setMeasurerId(measureBatch.getMeasurerId());
        record.setReCheckerId(measureBatch.getReCheckerId());
        record.setMeasureTime(LocalDateTime.now());
        record.setMeasureType(measureType);
        record.setMeasureMode(MeasureModeEnum.getEnumByValue(dto.getMeasureMode()));
        record.setStorageMaterialBatchId(measureBatch.getMaterialBatchId());
        record.setStorageMaterialBatchNo(planBatch.getMaterialBatchNo());
        record.setMaterialId(productFormulaMaterial.getMaterialId());
        record.setMaterialName(productFormulaMaterial.getMaterialName());
        record.setMaterialMergeCode(productFormulaMaterial.getMaterialMergeCode());
        record.setMaterialPositionId(dto.getMaterialPositionId());
        record.setSignStatus(WeighSignStatus.UN_SIGNED);
        record.setFormulaMaterialId(productFormulaMaterial.getId());
        return record;
    }

    private void saveMeasureLog(LiquidPreparationMeasureRecord record, MeasureTypeEnum measureType,
                                LiquidPreparationMeasureInstance instance, LiquidPreparationMaterialBatch planBatch,
                                ProductFormulaMaterial productFormulaMaterial) {
        measureLogService.saveLog(LiquidPreparationMeasureLogSaveDTO.builder()
                .storageMaterialId(record.getStorageMaterialId())
                .materialNo(record.getStorageMaterialNo())
                .unitId(record.getUnitId())
                .measureType(measureType)
                .measurerId(record.getMeasurerId())
                .reCheckerId(record.getReCheckerId())
                .productPlanId(instance.getProductPlanId())
                .materialBatchNo(planBatch.getMaterialBatchNo())
                .materialBatchId(planBatch.getMaterialBatchId())
                .materialId(productFormulaMaterial.getMaterialId())
                .materialName(productFormulaMaterial.getMaterialName())
                .materialMergeCode(productFormulaMaterial.getMaterialMergeCode())
                .materialType(productFormulaMaterial.getMaterialType().getValue())
                .measureQuantity(record.getQuantity())
                .build());
    }

    /**
     * 处理并保存批记录数据
     *
     * @param instance
     * @param productFormulaMaterial
     * @param plan
     */
    private void generateResultsAndSave(LiquidPreparationMeasureInstance instance,
                                        ProductFormulaMaterial productFormulaMaterial, Plan plan) {
        List<MeasureResultRecordVO> records = measureRecordMapper.selectResultVOByMeasureInstanceId(instance.getId());
        List<MeasuredBatchDetailVO> measuredBatch = measureBatchMapper.selectMeasureBatchListDetailByInstanceId(instance.getId());
        ProductionDetailInfo info = new ProductionDetailInfo();
        info.setMeasuredBatchDetailVOS(measuredBatch);
        info.setCurrentMeasureFormulaMaterialId(productFormulaMaterial.getId());
        info.setMeasureResultRecordList(records);
        info.setFormulaInfo(formulaConfigureService.getProductFormulaInfoByPlanId(instance.getProductPlanId()));
        List<ExecuteFormData> dataList = new ArrayList<>();
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(instance.getProcedureStepModelId());
        BusinessComponentBatchSaveDTO baseDTO = getBusinessComponentBatchSaveDTO(instance, plan, procedureStepModel);
        info.setDto(baseDTO);
        ComponentListVO componentListVO =
                batchRecordComponentService.selectUsedComponentDetail(procedureStepModel.getRecordVersionId(),
                        procedureStepModel.getRecordItemId(), instance.getComponentId());
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        info.setCustomFieldList(materialBatchFieldService.queryMaterialAndBatchField(CollectionUtils.convertList(records,
                MeasureResultRecordVO::getStorageMaterialBatchId)));
        liquidMeasureComponentStrategy.handleBusinessComponent(dataList, componentListVO, info, configMap, null);
        executeFormDataService.saveResultsAndHandleRelationComponentData(dataList, instance.getProductPlanId(),
                instance.getProcedureStepModelId(), instance.getCopyVersion());
    }

    private static BusinessComponentBatchSaveDTO getBusinessComponentBatchSaveDTO(LiquidPreparationMeasureInstance instance,
                                                                                  Plan plan,
                                                                                  ProcedureStepModel procedureStepModel) {
        BusinessComponentBatchSaveDTO baseDTO = new BusinessComponentBatchSaveDTO();
        baseDTO.setComponentId(instance.getComponentId());
        baseDTO.setProductPlanId(instance.getProductPlanId());
        baseDTO.setBatchNo(plan.getBatchNo());
        baseDTO.setProcessId(procedureStepModel.getProcessId());
        baseDTO.setProcessVersion(procedureStepModel.getProcessVersion());
        baseDTO.setRecordItemId(procedureStepModel.getRecordItemId());
        baseDTO.setRecordVersionId(procedureStepModel.getRecordVersionId());
        baseDTO.setProcedureStepId(procedureStepModel.getProcedureStepId());
        baseDTO.setProcedureStepModelId(procedureStepModel.getId());
        baseDTO.setReuse(instance.getReuse());
        baseDTO.setCopyVersion(instance.getCopyVersion());
        return baseDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeMeasure(LiquidMeasureCompleteDTO dto) {
        LiquidPreparationMeasureBatch measureBatch = measureBatchMapper.selectById(dto.getMeasureBatchId());
        if (measureBatch == null) {
            throw new BmosException(MesResponseCode.LIQUID_PREPARATION_MEASURE_BATCH_NOT_EXISTS);
        }
        MeasureStageEnum measureStage = measureBatch.getMeasureStage();
        if (Objects.equals(measureStage, MeasureStageEnum.LIQUID_MEASURE)) {
            measureBatch.setMeasureStage(MeasureStageEnum.LIQUID_ODD);
            measureBatch.setMeasurerId(dto.getCompleteUserId());
        } else if (Objects.equals(measureStage, MeasureStageEnum.LIQUID_ODD)) {
            measureBatch.setMeasureStage(MeasureStageEnum.COMPLETED);
            measureBatch.setMeasureStatus(MeasureStatusEnum.COMPLETED);
        }
        measureBatchMapper.updateById(measureBatch);
        // 处理组件数据
        LiquidPreparationMeasureInstance instance =
                measureInstanceMapper.selectById(measureBatch.getMeasureInstanceId());
        Plan plan = planService.getById(instance.getProductPlanId());
        LiquidPreparationMaterialBatch planBatch =
                liquidPreparationMaterialBatchMapper.selectById(measureBatch.getLiquidPreparationPlanBatchId());
        ProductFormulaMaterial formulaMaterial =
                formulaConfigureService.getFormulaMaterialById(planBatch.getFormulaMaterialId());
        generateResultsAndSave(instance, formulaMaterial, plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeMeasurer(LiquidMeasureChangeMeasurerDTO dto) {
        LiquidPreparationMeasureInstance instance = measureInstanceMapper.selectById(dto.getMeasureInstanceId());
        if (instance == null) {
            throw new BmosException(MesResponseCode.LIQUID_MEASURE_INSTANCE_NOT_EXISTS);
        }
        List<LiquidPreparationMeasureRecord> list =
                measureRecordMapper.selectByMeasureInstanceId(dto.getMeasureInstanceId());
        // 未签名的量取记录
        List<LiquidPreparationMeasureRecord> filter =
                list.stream().filter(item -> Objects.equals(item.getSignStatus(), WeighSignStatus.UN_SIGNED)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(filter)) {
            throw new BmosException(MesResponseCode.MEASURE_RECORD_EXIST_UNSINGED_RECORD);
        }
        instance.setPreMeasurerId(dto.getMeasurerId());
        instance.setPreReCheckerId(dto.getReCheckerId());
        measureInstanceMapper.updateById(instance);
        List<LiquidPreparationMeasureBatch> measureBatches =
                measureBatchMapper.selectByMeasureInstanceId(dto.getMeasureInstanceId());
        if (CollUtil.isNotEmpty(measureBatches)) {
            measureBatches.forEach(e -> {
                e.setMeasurerId(dto.getMeasurerId());
                e.setReCheckerId(dto.getReCheckerId());
            });
            measureBatchMapper.updateBatch(measureBatches);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sign(LiquidMeasureSignDTO dto) {
        List<LiquidPreparationMeasureRecord> records =
                measureRecordMapper.selectByMeasureInstanceId(dto.getMeasureInstanceId())
                        .stream()
                        .filter(e-> Objects.equals(e.getSignStatus(), WeighSignStatus.UN_SIGNED))
                        .collect(Collectors.toList());
        records.forEach(record -> {
            record.setRemark(dto.getRemark());
            record.setSignStatus(WeighSignStatus.SIGNED);
        });
        if (CollUtil.isNotEmpty(records)) {
            measureRecordMapper.updateBatch(records);
            List<Long> ids = CollectionUtils.convertList(records, LiquidPreparationMeasureRecord::getStorageMaterialId);
            storageMaterialService.signBatchByIdList(ids);
        }
    }

    /**
     * 根据容器id获取容器编码和容器名称
     *
     * @param container 容器
     * @return 容器编码-容器名称
     */
    private String getContainerName(EquipmentInfoFeignVO container) {
        if (container == null) {
            return null;
        }
        return container.getCode() + "-" + container.getName();
    }

    private boolean oddLiquidMeasure(LiquidPreparationMeasureBatch measureBatch, BigDecimal allQuantity,
                                     ProductFormulaMaterial productFormulaMaterial) {
        BigDecimal[] diff;
        // 余液量取允差
        ProductFormulaToleranceInfo toleranceInfo = productFormulaMaterial.getToleranceInfo();
        diff = DiffUtil.diff(measureBatch.getPutQuantity()
                        .subtract(measureRecordMapper.selectByMeasureBatchId(measureBatch.getId(), MeasureTypeEnum.LIQUID_MEASURE)
                                .stream()
                                .map(LiquidPreparationMeasureRecord::getQuantity)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)),
                toleranceInfo.getOddLiquidMeasureToleranceUpper(),
                toleranceInfo.getOddLiquidMeasureToleranceLower(),
                ToleranceTypeEnum.getEnumByValue(toleranceInfo.getOddLiquidMeasureToleranceType()),
                productFormulaMaterial.getScale(),
                productFormulaMaterial.getScaleLength()
        );
        boolean oddWithDiff = withDiff(diff);
        // 处理单边允差
        if (oddWithDiff) {
            if (diff[0] == null) {
                diff[0] = diff[1];
            }
            if (diff[2] == null) {
                diff[2] = diff[1];
            }
        }
        if (oddWithDiff && allQuantity.compareTo(diff[0]) >= 0 && allQuantity.compareTo(diff[2]) <= 0) {
            // 完成余液量取(允差范围内 或者大于下限即可（因为有超出的情况）)
            log.info("余液量取：称量结果在允差范围{}内，结束称量，完成余液量取", Arrays.toString(diff));
            measureBatch.setMeasureStage(MeasureStageEnum.COMPLETED);
            measureBatch.setMeasureStatus(MeasureStatusEnum.COMPLETED);
            return true;
        } else if (oddWithDiff && allQuantity.compareTo(diff[2]) > 0) {
            log.info("余液量取：称量结果超出允差范围{}，结束称量，完成余液量取", Arrays.toString(diff));
            measureBatch.setMeasureStage(MeasureStageEnum.COMPLETED);
            measureBatch.setMeasureStatus(MeasureStatusEnum.COMPLETED);
            return true;
        } else if (!oddWithDiff && allQuantity.compareTo(diff[1]) == 0) {
            log.info("余液量取：无允差，称量结果等于目标结果{}，完成余液量取", Arrays.toString(diff));
            measureBatch.setMeasureStage(MeasureStageEnum.COMPLETED);
            measureBatch.setMeasureStatus(MeasureStatusEnum.COMPLETED);
            return true;
        } else if (!oddWithDiff && allQuantity.compareTo(diff[1]) > 0) {
            log.info("余液量取：无允差，称量结果大于目标结果{}，完成余液量取", Arrays.toString(diff));
            measureBatch.setMeasureStage(MeasureStageEnum.COMPLETED);
            measureBatch.setMeasureStatus(MeasureStatusEnum.COMPLETED);
            return true;
        } else {
            log.info("余液量取：称量结果不满足允差{}, 继续余液量取", Arrays.toString(diff));
        }
        return false;
    }

    /**
     * 配液量取
     *
     * @param planBatch              配液计划批次
     * @param productFormulaMaterial 配方物料
     * @param measureBatch           量取批次
     * @param allQuantity            已量取总量
     * @return 是否更新量取批次信息
     */
    private boolean liquidMeasure(LiquidPreparationMaterialBatch planBatch,
                                  ProductFormulaMaterial productFormulaMaterial,
                                  LiquidPreparationMeasureBatch measureBatch, BigDecimal allQuantity) {
        BigDecimal[] diff;
        // 配液允差
        ProductFormulaToleranceInfo toleranceInfo = productFormulaMaterial.getToleranceInfo();

        diff = DiffUtil.diff(planBatch.getPreparationQuantity(),
                toleranceInfo.getLiquidMeasureToleranceUpper(),
                toleranceInfo.getLiquidMeasureToleranceLower(),
                ToleranceTypeEnum.getEnumByValue(toleranceInfo.getLiquidMeasureToleranceType()),
                productFormulaMaterial.getScale(),
                productFormulaMaterial.getScaleLength());
        boolean withDiff = withDiff(diff);
        BigDecimal putIn;
        // 处理单边允差
        if (withDiff) {
            if (diff[0] == null) {
                diff[0] = diff[1];
            }
            if (diff[2] == null) {
                diff[2] = diff[1];
            }
            // 物料总量 + 上限允差值
            putIn = measureBatch.getPutQuantity().add(diff[2].subtract(diff[1]));
        }else {
            // 物料总量(20250512确认 没配置允差 按0处理)
            putIn = measureBatch.getPutQuantity();
        }
        if (allQuantity.compareTo(putIn) > 0) {
            // 量取结果超过了 物料总量+允差上限的总和，不允许称量
            throw new BmosException(MesResponseCode.LIQUID_MEASURE_OVER_DIFF);
        }
        if (withDiff && allQuantity.compareTo(diff[0]) >= 0 && allQuantity.compareTo(diff[2]) <= 0) {
            // 完成配液量取(允差范围内)
            log.info("配液量取：称量结果在允差范围{}内，结束称量，进入余液量取", Arrays.toString(diff));
            measureBatch.setMeasureStage(MeasureStageEnum.LIQUID_ODD);
            return true;
        } else if (withDiff && allQuantity.compareTo(diff[2]) > 0) {
            log.info("配液量取：称量结果超出允差范围{}", Arrays.toString(diff));
            // 超出批次目标量范围
            throw new BmosException(MesResponseCode.LIQUID_MEASURE_OVER_TARGET);
        } else if (!withDiff && allQuantity.compareTo(diff[1]) == 0) {
            // 完成配液量取(精确等于)
            log.info("配液量取：无允差，称量结果等于目标结果{}，结束称量，进入余液量取", Arrays.toString(diff));
            measureBatch.setMeasureStage(MeasureStageEnum.LIQUID_ODD);
            return true;
        } else if (!withDiff && allQuantity.compareTo(diff[1]) > 0) {
            log.info("配液量取：称量结果超出允差范围{}", Arrays.toString(diff));
            throw new BmosException(MesResponseCode.LIQUID_MEASURE_OVER_TARGET);
        } else {
            // 不满足 继续量取
            log.info("配液量取：称量结果不满足允差{}, 继续配液量取", Arrays.toString(diff));
        }
        return false;
    }

    private boolean withDiff(BigDecimal[] diff) {
        return diff[0] != null && diff[2] != null;
    }

    private LiquidPreparationMeasureBatchDetailVO getMeasureBatchDetailBasicInfoVO(LiquidPreparationMeasureBatch measureBatch, ProductFormulaMaterial formulaMaterial, LiquidPreparationMaterialBatch planBatch) {
        LiquidPreparationMeasureBatchDetailVO result = new LiquidPreparationMeasureBatchDetailVO();
        result.setInstanceId(measureBatch.getMeasureInstanceId());
        result.setMaterialName(formulaMaterial.getMaterialName());
        result.setMaterialMergeCode(formulaMaterial.getMaterialMergeCode());
        result.setMaterialBatchNo(planBatch.getMaterialBatchNo());
        result.setMaterialBatchId(planBatch.getMaterialBatchId());
        result.setTotalQuantity(measureBatch.getPutQuantity());
        result.setTargetQuantity(planBatch.getPreparationQuantity());
        result.setUnitId(formulaMaterial.getUnitId());
        result.setUnitName(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
        result.setCategoryType(formulaMaterial.getMaterialType());
        result.setMeasureStage(measureBatch.getMeasureStage());
        return result;
    }

    /**
     * 量取消耗
     *
     * @param consumeStorageMaterialIdList 消耗物料件id列表
     * @param remark                       备注
     * @return 消耗总量（标准量）
     */
    private BigDecimal measureConsume(List<Long> consumeStorageMaterialIdList, Plan plan, String remark) {
        if (CollectionUtil.isEmpty(consumeStorageMaterialIdList)) {
            return BigDecimal.ZERO;
        }
        List<StorageMaterial> list = storageMaterialService.queryListByIds(consumeStorageMaterialIdList);
        if (CollectionUtil.isEmpty(list)) {
            return BigDecimal.ZERO;
        }
        // 批次可用状态校验
        List<StorageMaterialBatch> batches =
                storageMaterialBatchService.queryListByIds(CollectionUtils.convertList(list,
                        StorageMaterial::getStorageMaterialBatchId));
        batches.forEach(StorageMaterialBatch::availableValidate);
        return storageMaterialService.weighConsume(list, remark, plan, StorageOperateTypeEnum.MEASURE_CONSUME);
    }

    private LiquidMeasureMaterialPieceVO generateMeasureMaterialVO(StorageMaterial storageMaterial,
                                                                   StorageMaterialBatch batch,
                                                                   LiquidPreparationMeasureInstance instance) {
        BigDecimal reserveQuantity = storageMaterial.getQuantity();
        ProductFormulaInfo formulaInfo =
                formulaConfigureService.getProductFormulaInfoByPlanId(instance.getProductPlanId());
        ProductFormulaMaterial formulaMaterial = formulaInfo.getMaterialIdMap().get(storageMaterial.getMaterialId());
        if (formulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        // 根据配方修约
        BigDecimal roundingQuantity = MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(reserveQuantity,
                formulaMaterial.getUnitId()), formulaMaterial);
        return LiquidMeasureMaterialPieceVO.builder()
                .id(storageMaterial.getId())
                .materialMergeCode(formulaMaterial.getMaterialMergeCode())
                .materialName(formulaMaterial.getMaterialName())
                .materialBatchNo(batch.getMaterialBatchNo())
                .produceDate(batch.getProduceDate())
                .materialNo(storageMaterial.getNo())
                .unitId(formulaMaterial.getUnitId())
                .unitName(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()))
                .materialQuantity(roundingQuantity)
                .expiredDate(batch.getExpiredDate())
                .supplier(batch.getSupplier())
                .producer(batch.getProducer())
                .originalCode(batch.getOriginalBatchNo())
                .originalBatchNo(batch.getFactoryBatchNo())
                .fieldList(materialBatchFieldService.queryMaterialField(batch.getId()))
                .build();
    }

    @NotNull
    private static LiquidPreparationMeasureInstance getInitMeasureInstance(LiquidMeasureInstanceQueryDTO dto,
                                                                           ProcedureStepModel procedureStepModel) {
        LiquidPreparationMeasureInstance insert = new LiquidPreparationMeasureInstance();
        insert.setProductPlanId(dto.getProductPlanId());
        insert.setComponentId(dto.getComponentId());
        insert.setProcedureStepModelId(dto.getProcedureStepModelId());
        insert.setCopyVersion(dto.getCopyVersion());
        insert.setReuse(procedureStepModel.getReusable());
        return insert;
    }
}
