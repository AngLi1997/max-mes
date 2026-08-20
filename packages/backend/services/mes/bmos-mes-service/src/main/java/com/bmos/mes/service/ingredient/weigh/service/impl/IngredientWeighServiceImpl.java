package com.bmos.mes.service.ingredient.weigh.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.mes.common.constant.ProcessConstant;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.ingredient.*;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.equipment.service.EquipmentCommonService;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.dto.RecordItemLatestDataQueryDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.mapper.ProductFormulaMaterialMapper;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.ingredient.DiffUtil;
import com.bmos.mes.service.ingredient.plan.mapper.IngredientMaterialBatchMapper;
import com.bmos.mes.service.ingredient.plan.mapper.IngredientPlanMapper;
import com.bmos.mes.service.ingredient.plan.model.IngredientMaterialBatch;
import com.bmos.mes.service.ingredient.plan.model.IngredientMaterialBatchDetailInfo;
import com.bmos.mes.service.ingredient.plan.model.IngredientPlan;
import com.bmos.mes.service.ingredient.weigh.convert.IngredientWeighProcessConvert;
import com.bmos.mes.service.ingredient.weigh.dto.*;
import com.bmos.mes.service.ingredient.weigh.mapper.IIngredientWeighBatchProcessMapper;
import com.bmos.mes.service.ingredient.weigh.mapper.IIngredientWeighProcessMapper;
import com.bmos.mes.service.ingredient.weigh.mapper.IIngredientWeighRecordMapper;
import com.bmos.mes.service.ingredient.weigh.model.IngredientWeighBatchProcess;
import com.bmos.mes.service.ingredient.weigh.model.IngredientWeighProcess;
import com.bmos.mes.service.ingredient.weigh.model.IngredientWeighRecord;
import com.bmos.mes.service.ingredient.weigh.service.IIngredientWeighService;
import com.bmos.mes.service.ingredient.weigh.service.WeighLogService;
import com.bmos.mes.service.ingredient.weigh.vo.*;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.mapper.ProcedureStepConfigMapper;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.condition.event.WeighingIngredientSignType;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.strategy.IngredientWeighComponentStrategy;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.storage.config.mapper.ICargoPositionMapper;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.service.ICargoPositionService;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialQueryValidateDTO;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialReserveDTO;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialReserveMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.model.StorageMaterialReserve;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialDetailVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialVO;
import com.bmos.mes.service.tag.convert.ScanDeviceConvert;
import com.bmos.mes.service.tag.dto.ScanDeviceCodeDTO;
import com.bmos.mes.service.tag.dto.ScanWeighMaterialCodeDTO;
import com.bmos.mes.service.tag.vo.ScanCargoPositionVO;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.facade.equipment.dto.EquipmentQueryDTO;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.facade.equipment.enums.EquipmentTagCodeEnum;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.TagFeignVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bmos.mes.common.enums.record.BusinessComponentTypeEnum.getEnumByValue;
import static com.bmos.mes.common.enums.storage.StorageOperateTypeEnum.INGREDIENT_WEIGHT;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/19 09:21
 */
@Service
@Slf4j
public class IngredientWeighServiceImpl implements IIngredientWeighService {

    private static final String LOG_PREFIX = "[配料称量]";

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Resource
    private ProductMaterialMapper materialMapper;

    @Resource
    private IStorageMaterialReserveMapper storageMaterialReserveMapper;

    @Resource
    private IngredientPlanMapper ingredientPlanMapper;

    @Resource
    private IngredientMaterialBatchMapper ingredientMaterialBatchMapper;

    @Resource
    private IIngredientWeighProcessMapper ingredientWeighProcessMapper;

    @Resource
    private IIngredientWeighBatchProcessMapper ingredientWeighBatchProcessMapper;

    @Resource
    private IIngredientWeighRecordMapper iIngredientWeighRecordMapper;

    @Resource
    private IStorageMaterialPositionLogService storageMaterialPositionLogService;

    @Resource
    private UnitCache unitCache;

    @Resource
    private ICargoPositionService cargoPositionService;

    @Resource
    private ICargoPositionMapper cargoPositionMapper;

    @Resource
    private PlanService planService;

    @Resource
    private ProductFormulaMaterialMapper formulaMaterialMapper;

    @Autowired
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private WeighLogService weighLogService;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Resource
    private ProcedureStepConfigMapper procedureStepConfigMapper;

    @Resource
    private ExecuteFormDataService executeFormDataService;

    @Resource
    private BatchRecordComponentService batchRecordComponentService;

    @Resource
    private ProcedureStepConfigService procedureStepConfigService;

    @Resource
    private IngredientWeighComponentStrategy ingredientWeighComponentStrategy;

    @Resource
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private EquipmentCommonService equipmentCommonService;

    @Resource
    private FactoryFeign factoryFeign;

    @Resource
    private ITaskConditionCalculator conditionChangeHandler;

    private static final BusinessComponentTypeEnum[] alwaysUpdateComponentType = new BusinessComponentTypeEnum[]{
//            WEIGHING_INGREDIENTS_SUMMARY_NAME,
//            WEIGHING_INGREDIENTS_SUMMARY_CODE,
//            WEIGHING_INGREDIENTS_SUMMARY_SPECIFICATION,
//            WEIGHING_INGREDIENTS_SUMMARY_NET_WEIGHT,
//            WEIGHING_INGREDIENTS_SUMMARY_TARE_WEIGHT,
//            WEIGHING_INGREDIENTS_SUMMARY_GROSS_WEIGHT,
//            WEIGHING_INGREDIENTS_SUMMARY_UNIT,
//            WEIGHING_INGREDIENTS_SUMMARY_TOTAL_NUMBER
    };


    @Override
    @NotNull
    public IngredientWeighStorageMaterialVO queryWeighStorageMaterial(ScanWeighMaterialCodeDTO scanQuery) {
        IngredientPlan ingredientPlan = ingredientPlanMapper.selectById(scanQuery.getIngredientPlanId());
        if (ingredientPlan == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_NOT_EXIST);
        }
        // 根据code查询物料件
        StorageMaterialQueryValidateDTO build = StorageMaterialQueryValidateDTO.builder()
                .no(scanQuery.getNo())
                .productPlanId(ingredientPlan.getProductPlanId())
                .build();
        StorageMaterialDetailVO detail = storageMaterialService.queryByCodeAndValidate(build);
        StorageMaterial storageMaterial = detail.getStorageMaterial();
        // 校验物料件符合所选物料批次
        if (!Objects.equals(storageMaterial.getStorageMaterialBatchId(), scanQuery.getMaterialBatchId())) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_MATCH);
        }
        // 通用校验
        detail.validateAll();
        StorageMaterialBatch batch = detail.getStorageMaterialBatch();
        StorageMaterialReserve reserve = detail.getStorageMaterialReserve();
        IngredientMaterialBatch ingredientMaterialBatch = ingredientMaterialBatchMapper.getByIngredientPlanIdAndMaterialBatchId(ingredientPlan.getId(), scanQuery.getMaterialBatchId());
        if (ingredientMaterialBatch == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_BATCH_NOT_EXIST);
        }

        // 物料件相关
        IngredientWeighStorageMaterialVO ingredientWeighStorageMaterialVO = new IngredientWeighStorageMaterialVO();
        ingredientWeighStorageMaterialVO.setId(storageMaterial.getId());
        ingredientWeighStorageMaterialVO.setNo(storageMaterial.getNo());

        long unitId = scanQuery.getUnitId() == null ? ingredientMaterialBatch.getUnitId() : scanQuery.getUnitId();
        // 物料件的预订量 使用配方的单位和精度换算修约
        ingredientWeighStorageMaterialVO.setUnitId(unitId);
        ingredientWeighStorageMaterialVO.setUnit(unitCache.getGlobalUnitName(unitId));
        // 若未被预定 取物料件可用量
        ingredientWeighStorageMaterialVO.setQuantity(PrecisionHelper.precision(unitCache.toExt(reserve == null ?
                storageMaterial.getAvailableQuantity() : reserve.getReserveQuantity(), unitId), unitId));

        // 物料批次相关
        ingredientWeighStorageMaterialVO.setMaterialBatchNo(batch.getMaterialBatchNo());
        ingredientWeighStorageMaterialVO.setMaterialBatchId(batch.getMaterialId());
        ingredientWeighStorageMaterialVO.setSupplier(batch.getSupplier());
        ingredientWeighStorageMaterialVO.setHydration(batch.getHydration());
        ingredientWeighStorageMaterialVO.setNoHydrationContent(batch.getNoHydrationContent());
        ingredientWeighStorageMaterialVO.setExpiredDate(batch.getExpiredDate());
        ingredientWeighStorageMaterialVO.setFactoryBatchNo(batch.getFactoryBatchNo());
        return ingredientWeighStorageMaterialVO;
    }

    @Override
    public List<IngredientPlanItemVO> queryPendingIngredientPlanList(Long productPlanId, String batchNo) {
        // 所有已完成的配料单
        return ingredientPlanMapper.queryPendingIngredientPlanList(productPlanId, batchNo)
                .stream().filter(item -> !Objects.equals(item.getPlanBatchCount(), item.getWeighBatchCount()))
                .collect(Collectors.toList());
    }

    @Override
    @NotNull
    public IngredientPlanDetailVO queryIngredientPlanById(Long id, Long componentId, Long procedureStepModelId) {
        IngredientPlan ingredientPlan = ingredientPlanMapper.selectById(id);
        if (ingredientPlan == null) {
            return null;
        }
        IngredientPlanDetailVO result = new IngredientPlanDetailVO();
        result.setId(ingredientPlan.getId());
        result.setName(ingredientPlan.getName());
        List<IngredientMaterialBatchDetailInfo> batchDetailInfos = ingredientMaterialBatchMapper.getByIngredientId(ingredientPlan.getId());
        if (CollectionUtil.isEmpty(batchDetailInfos)) {
            return result;
        }
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = new HashMap<>();
        formulaMaterialMap = formulaMaterialMapper.selectBatchIds(batchDetailInfos.stream().map(IngredientMaterialBatch::getFormulaMaterialId).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(ProductFormulaMaterial::getMaterialId, Function.identity(), (k1, k2) -> k1));
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(procedureStepModelId);
        // 当工艺配置配料称量业务组件绑定了物料，添加批次时应只回显对应的物料
        List<Long> formulaMaterialIds = Optional.of(procedureStepModel)
                .map(psm -> procedureStepConfigMapper.selectComponentConfig(
                        psm.getId(),
                        componentId,
                        psm.getReusable(),
                        psm.getProcessId(),
                        psm.getProcessVersion()))
                .map(ProcedureStepConfig::getConfigInfo)
                .map(configStr -> JSONUtil.toBean(configStr, ProcedureStepConfigInfo.class))
                .map(ProcedureStepConfigInfo::getFormulaMaterialIds)
                .orElse(new ArrayList<>());
        if (CollectionUtil.isNotEmpty(formulaMaterialIds)) {
            batchDetailInfos = batchDetailInfos.stream()
                    .filter(item -> CollectionUtil.contains(formulaMaterialIds, item.getFormulaMaterialId()))
                    .collect(Collectors.toList());
        }
        List<Long> storageBatchIds = batchDetailInfos.stream()
                .map(IngredientMaterialBatchDetailInfo::getMaterialBatchId)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(storageBatchIds)) {
            result.setBatchList(new ArrayList<>());
            return result;
        }
        List<StorageMaterialBatch> storageMaterialBatches = storageMaterialBatchMapper.selectBatchIds(storageBatchIds);

        List<Long> materialIds = storageMaterialBatches.stream().map(StorageMaterialBatch::getMaterialId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(materialIds)) {
            result.setBatchList(new ArrayList<>());
            return result;
        }
        Map<Long, ProductMaterial> materialMap = materialMapper.selectListByBatchIds(materialIds)
                .stream()
                .collect(Collectors.toMap(ProductMaterial::getId, Function.identity(), (k1, k2) -> k1));
        Map<Long, StorageMaterialBatch> materialBatchMap = storageMaterialBatches.stream().collect(Collectors.toMap(StorageMaterialBatch::getId, Function.identity(), (k1, k2) -> k1));
        List<IngredientPlanDetailVO.IngredientPlanDetailBatchVO> list = new ArrayList<>();

        Map<Long, IngredientRecordCalc> ingredientBatchWeighStatusMap = getIngredientBatchWeighStatusMap(ingredientPlan.getId());

        for (IngredientMaterialBatchDetailInfo batchDetailInfo : batchDetailInfos) {
            IngredientPlanDetailVO.IngredientPlanDetailBatchVO item = new IngredientPlanDetailVO.IngredientPlanDetailBatchVO();
            StorageMaterialBatch storageMaterialBatch = materialBatchMap.get(batchDetailInfo.getMaterialBatchId());
            if (storageMaterialBatch == null) {
                continue;
            }
            ProductMaterial material = materialMap.get(storageMaterialBatch.getMaterialId());
            item.setMaterialId(material.getId());
            item.setMaterialName(material.getName());
            item.setMaterialCode(material.getCode());
            item.setMergeCode(material.getMergeCode());
            item.setStorageMaterialBatchId(storageMaterialBatch.getId());
            item.setStorageMaterialBatchNo(storageMaterialBatch.getMaterialBatchNo());
            item.setUnitId(batchDetailInfo.getUnitId());
            item.setUnit(unitCache.getGlobalUnitName(batchDetailInfo.getUnitId()));
            item.setTargetQuantity(MaterialQuantityCalculateUtil.roundingOff(batchDetailInfo.getIngredientQuantity(), formulaMaterialMap.get(item.getMaterialId())));
            item.setFinishedQuantity(Optional.ofNullable(ingredientBatchWeighStatusMap.get(storageMaterialBatch.getId()))
                    .map(IngredientRecordCalc::getWeighedQuantity)
                    .orElse(BigDecimal.ZERO).stripTrailingZeros()
            );
            item.setUnFinishedQuantity(item.getFinishedQuantity().compareTo(item.getTargetQuantity()) > 0
                    ? BigDecimal.ZERO
                    : item.getTargetQuantity().subtract(item.getFinishedQuantity()).stripTrailingZeros());

            item.setWeighStatus(Optional.ofNullable(ingredientBatchWeighStatusMap.get(storageMaterialBatch.getId()))
                    .map(IngredientRecordCalc::getIngredientWeighStatus)
                    .orElse(IngredientWeighStatus.PENDING)
            );
            list.add(item);
        }
        result.setBatchList(list);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeSureWeigh(IngredientMakeSureWeighDTO dto) {

        Plan plan = planMapper.selectById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }

        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(dto.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }

        IngredientWeighProcess existProcess = ingredientWeighProcessMapper.getIngredientWeighProcess(dto.getProductPlanId(), dto.getComponentId(), dto.getProcedureStepModelId(), dto.getCopyVersion(), procedureStepModel.getReusable());
        if (existProcess == null) {
            if (dto.getWeigherId() == null || dto.getReCheckerId() == null) {
                throw new ValidationException("首次称量时称量人为必填");
            }
            // 首次确认 初始化所有待称量批次
            IngredientWeighProcess process = new IngredientWeighProcess();
            process.setProductPlanId(dto.getProductPlanId());
            process.setIngredientPlanId(dto.getIngredientPlanId());
            process.setPreWeigherId(dto.getWeigherId());
            process.setPreReCheckerId(dto.getReCheckerId());
            process.setRemark(dto.getRemark());
            process.setComponentId(dto.getComponentId());
            process.setReuse(procedureStepModel.getReusable());
            process.setProcedureStepModelId(dto.getProcedureStepModelId());
            process.setCopyVersion(dto.getCopyVersion());
            ingredientWeighProcessMapper.insert(process);
            IngredientWeighBatchProcess batchProcess = createBatchProcess(dto, process, plan);
            ingredientWeighBatchProcessMapper.insert(batchProcess);
        } else {
            IngredientWeighBatchProcess batchProcess = ingredientWeighBatchProcessMapper.queryByIngredientPlanIdAndStorageMaterialBatchId(dto.getIngredientPlanId(), dto.getStorageMaterialBatchId());
            if (batchProcess == null) {
                batchProcess = createBatchProcess(dto, existProcess, plan);
                ingredientWeighBatchProcessMapper.insert(batchProcess);
            }
            if (CollectionUtil.isNotEmpty(dto.getConsumeStorateMaterialIdList())) {
                batchProcess.addPuts(weighConsume(dto.getConsumeStorateMaterialIdList(), dto.getRemark(), plan));
            }
            batchProcess.setWeigherId(existProcess.getPreWeigherId());
            batchProcess.setReCheckerId(existProcess.getPreReCheckerId());
            batchProcess.setRemark(existProcess.getRemark());
            batchProcess.setWeighStatus(IngredientWeighStatus.PROCESSING);
            batchProcess.setProcedureStepModelId(dto.getProcedureStepModelId());
            batchProcess.setComponentId(dto.getComponentId());
            batchProcess.setCopyVersion(dto.getCopyVersion());
            batchProcess.setReuse(procedureStepModel.getReusable());
            ingredientWeighBatchProcessMapper.updateById(batchProcess);
        }
    }

    private IngredientWeighBatchProcess createBatchProcess(IngredientMakeSureWeighDTO dto, IngredientWeighProcess process, Plan plan) {
        IngredientWeighBatchProcess batchProcess = new IngredientWeighBatchProcess();
        batchProcess.setIngredientWeighProcessId(process.getId());
        batchProcess.setIngredientPlanId(dto.getIngredientPlanId());
        batchProcess.setStorageMaterialBatchId(dto.getStorageMaterialBatchId());
        batchProcess.setWeigherId(process.getPreWeigherId());
        batchProcess.setReCheckerId(process.getPreReCheckerId());
        batchProcess.setWeighProcess(WeighProcess.INGREDIENT);
        batchProcess.setWeighStatus(IngredientWeighStatus.PROCESSING);
        batchProcess.setProcedureStepModelId(dto.getProcedureStepModelId());
        batchProcess.setComponentId(dto.getComponentId());
        batchProcess.setReuse(process.getReuse());
        batchProcess.setCopyVersion(dto.getCopyVersion());
        if (CollectionUtil.isNotEmpty(dto.getConsumeStorateMaterialIdList())) {
            batchProcess.addPuts(weighConsume(dto.getConsumeStorateMaterialIdList(), dto.getRemark(), plan));
        }
        return batchProcess;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addConsumeStorageMaterial(IngredientWeighConsumeStorageMaterialDTO dto) {
        IngredientWeighBatchProcess batchProcess = ingredientWeighBatchProcessMapper.queryByIngredientPlanIdAndStorageMaterialBatchId(dto.getIngredientPlanId(), dto.getStorageMaterialBatchId());
        if (batchProcess == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_BATCH_NOT_EXIST);
        }
        IngredientWeighProcess ingredientWeighProcess = ingredientWeighProcessMapper.selectById(batchProcess.getIngredientWeighProcessId());
        if (ingredientWeighProcess == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_WEIGHT_PROCESS_NOT_EXIST);
        }
        Plan plan = planMapper.selectById(ingredientWeighProcess.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        if (CollectionUtil.isNotEmpty(dto.getConsumeStorateMaterialIdList())) {
            batchProcess.addPuts(weighConsume(dto.getConsumeStorateMaterialIdList(), null, plan));
            ingredientWeighBatchProcessMapper.updateById(batchProcess);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WeighResult weighAndPrint(IngredientWeighAndPrintDTO dto) {

        // 批次详情
        IngredientWeighBatchProcess batchProcess = ingredientWeighBatchProcessMapper.queryByIngredientPlanIdAndStorageMaterialBatchId(dto.getIngredientPlanId(), dto.getStorageMaterialBatchId());
        if (batchProcess == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_BATCH_NOT_EXIST);
        }
        // 称量人校验
        if (!Objects.equals(batchProcess.getWeigherId(), SysUserHolder.getUser().getUserId())) {
            throw new BmosException(MesResponseCode.WEIGHER_NOT_MATCH);
        }
        // 净重校验
        dto.validateWeight();

        // 计划批次
        IngredientMaterialBatch planBatch = ingredientMaterialBatchMapper.getByIngredientPlanIdAndMaterialBatchId(batchProcess.getIngredientPlanId(), batchProcess.getStorageMaterialBatchId());
        if (planBatch == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_BATCH_NOT_EXIST);
        }

        // 配方物料
        ProductFormulaMaterial productFormulaMaterial = formulaMaterialMapper.selectById(planBatch.getFormulaMaterialId());
        if (productFormulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXIST);
        }

        // 当前称量类型 配料/余料
        WeighType weighType;
        if (Objects.equals(batchProcess.getWeighProcess(), WeighProcess.INGREDIENT)) {
            weighType = WeighType.INGREDIENT;
        } else if (Objects.equals(batchProcess.getWeighProcess(), WeighProcess.ODD)) {
            weighType = WeighType.ODD;
        } else {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_BATCH_ENOUGH);
        }

        // 已称量记录
        List<IngredientWeighRecord> existRecordList = iIngredientWeighRecordMapper.queryByWeighBatchProcessId(batchProcess.getId(), weighType);

        // 已称量求和
        BigDecimal existSum = existRecordList.stream().map(IngredientWeighRecord::getNetWeight).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 本次称量值（基本单位量）
        BigDecimal result = existSum.add(unitCache.toBasic(dto.getNetWeight(), productFormulaMaterial.getUnitId()));

        // 称量信息
        IngredientWeighProcess ingredientWeighProcess = ingredientWeighProcessMapper.selectById(batchProcess.getIngredientWeighProcessId());
        if (ingredientWeighProcess == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_WEIGHT_PROCESS_NOT_EXIST);
        }

        // 生产计划
        Plan plan = planMapper.selectById(ingredientWeighProcess.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        // 生成称量记录
        IngredientWeighRecord record = new IngredientWeighRecord();
        // 生产物料件记录
        StorageMaterial storageMaterial = new StorageMaterial();

        record.setIngredientWeighBatchProcessId(batchProcess.getId());
        record.setIngredientPlanId(dto.getIngredientPlanId());
        record.setStorageMaterialBatchId(dto.getStorageMaterialBatchId());
        record.setGrossWeight(unitCache.toBasic(dto.getGrossWeight(), productFormulaMaterial.getUnitId()));
        record.setNetWeight(unitCache.toBasic(dto.getNetWeight(), productFormulaMaterial.getUnitId()));
        record.setTareWeight(checkZero(record.getGrossWeight().subtract(record.getNetWeight())));
        record.setUnitId(productFormulaMaterial.getUnitId());

        // 容器信息
        EquipmentInfoFeignVO container;
        if (dto.getContainerId() != null) {
            container = FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByEquipmentId(data), dto.getContainerId()).getData();
            if (container == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_NOT_EXIST);
            }
            StorageMaterial existContainer = storageMaterialService.selectStorageMaterialByContainerId(container.getId());
            if (existContainer != null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_OCCUPY);
            }
            String containerName = getContainerName(container);
            record.setContainerId(container.getId());
            record.setContainerName(containerName);
            storageMaterial.setContainerId(container.getId());
            storageMaterial.setContainer(containerName);
        }

        record.setMaterialPositionId(dto.getMaterialPositionId());
        record.setWeighType(weighType);
        record.setWeighMode(WeighMode.getByValue(dto.getWeighMode()));
        record.setWeighTime(LocalDateTime.now());
        record.setWeigherId(batchProcess.getWeigherId());
        record.setReCheckerId(batchProcess.getReCheckerId());
        // 生成物料件
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchMapper.selectById(dto.getStorageMaterialBatchId());
        if (storageMaterialBatch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        // 货位
        if (dto.getMaterialPositionId() != null) {
            CargoPosition cargoPosition = cargoPositionService.getByIdWithPermission(dto.getMaterialPositionId());
            if (cargoPosition == null) {
                throw new BmosException(MesResponseCode.CARGO_POSITION_NOT_EXIST);
            }
            storageMaterial.setMaterialPositionId(cargoPosition.getId());
        }
        storageMaterial.setMaterialId(storageMaterialBatch.getMaterialId());
        storageMaterial.setStorageMaterialBatchId(storageMaterialBatch.getId());
        storageMaterial.setNo(storageMaterialService.getSerial());
        BigDecimal weight = record.getNetWeight();
        storageMaterial.setInitQuantity(weight);
        storageMaterial.setAvailableQuantity(weight);
        storageMaterial.setConsumeQuantity(BigDecimal.ZERO);
        // 使用配方单位
        Long unitId = productFormulaMaterial.getUnitId();
        CacheUnit globalUnit = unitCache.getGlobalUnit(unitId);
        if (globalUnit != null) {
            if (globalUnit.getExtend()) {
                storageMaterial.setUnitId(globalUnit.getParentUnitId());
                storageMaterial.setUnitExtendId(globalUnit.getUnitId());
            } else {
                storageMaterial.setUnitId(unitId);
            }
        }
        storageMaterial.setReserveQuantity(BigDecimal.ZERO);
        storageMaterial.setSignStatus(WeighSignStatus.UN_SIGNED);
        storageMaterial.setProductPlanId(ingredientWeighProcess.getProductPlanId());
        storageMaterialService.save(storageMaterial);

        StorageMaterialPositionLogDTO logs = StorageMaterialPositionLogDTO.builder()
                .materialPositionId(dto.getMaterialPositionId())
                .storageMaterialId(storageMaterial.getId())
                .operateType(INGREDIENT_WEIGHT)
                .quantity(unitCache.toExt(weight, storageMaterial.getFinalUnitId()))
                .unitId(storageMaterial.getFinalUnitId())
                .senderId(batchProcess.getWeigherId())
                .receiverId(batchProcess.getReCheckerId())
                .productName(plan.getProductName())
                .productCode(plan.getProductMergeCode())
                .productBatchNo(plan.getBatchNo())
                .tareWeight(dto.getTareWeight())
                .grossWeight(dto.getGrossWeight())
                .build();

        if (Objects.equals(batchProcess.getWeighProcess(), WeighProcess.INGREDIENT)) {
            // 配料称量 需要预定
            // 预定物料件
            storageMaterialService.reserve(StorageMaterialReserveDTO.builder()
                    .storageMaterialId(storageMaterial.getId())
                    .processId(plan.getProcessId())
                    .batchId(plan.getId())
                    .productId(plan.getProductId())
                    .reCheckerId(batchProcess.getReCheckerId())
                    .operatorId(batchProcess.getWeigherId())
                    .remark(batchProcess.getRemark())
                    .grossWeight(dto.getGrossWeight())
                    .tareWeight(dto.getTareWeight())
                    .build());
        }

        // 称量出来的物料件默认未签名
        record.setSignStatus(WeighSignStatus.UN_SIGNED);
        record.setStorageMaterialId(storageMaterial.getId());
        iIngredientWeighRecordMapper.insert(record);
        // 发布配料称量变化的事件
        this.publishWeightEvent(plan.getId(), false, batchProcess.getProcedureStepModelId());

        // 允差
        BigDecimal[] diff;
        // 转换为目标单位进行比较
        result = unitCache.toExt(result, planBatch.getUnitId());
        boolean updateProcess = false;
        if (Objects.equals(batchProcess.getWeighProcess(), WeighProcess.INGREDIENT)) {
            // 配料称量
            diff = DiffUtil.diff(planBatch.getIngredientQuantity(), productFormulaMaterial.getChargeMixtureToleranceUpper(), productFormulaMaterial.getChargeMixtureToleranceLower(), productFormulaMaterial.getChargeMixtureToleranceType(),
                    productFormulaMaterial.getScale(),
                    productFormulaMaterial.getScaleLength());
            boolean withDiff = withDiff(diff);

            // 处理单边允差
            BigDecimal putIn;

            if (withDiff) {
                if (diff[0] == null) {
                    diff[0] = diff[1];
                }
                if (diff[2] == null) {
                    diff[2] = diff[1];
                }
                // 物料总量 + 上限允差值
                putIn = unitCache.toExt(batchProcess.getPutInQuantity(), planBatch.getUnitId()).add(diff[2].subtract(diff[1]));

            }else {
                // 物料总量(20250512确认 没配置允差 按0处理)
                putIn = unitCache.toExt(batchProcess.getPutInQuantity(), planBatch.getUnitId());
                diff[0] = diff[1];
                diff[2] = diff[1];
            }

            if (result.compareTo(putIn) > 0) {
                // 称量结果超过了 物料总量+允差上限的总和，证明称具上一定放了带称量物料件以外的东西，不允许称量
                throw new BmosException(MesResponseCode.INGREDIENT_WEIGH_INPUT_NOT_ENOUGH);
            }

            if (withDiff && result.compareTo(diff[0]) >= 0 && result.compareTo(diff[2]) <= 0) {
                log.info("{}配料称量：称量结果{}在允差范围{}内，结束称量，进入余料称量", LOG_PREFIX, result, Arrays.toString(diff));
                // 完成配料称量(允差范围内)
                batchProcess.setWeighProcess(WeighProcess.ODD);
                updateProcess = true;
            } else if (withDiff && result.compareTo(diff[2]) > 0) {
                log.info("{}配料称量：称量结果{}超出允差范围{}", LOG_PREFIX, result, Arrays.toString(diff));
                // 超出批次目标量范围
                throw new BmosException(MesResponseCode.INGREDIENT_WEIGH_OVER_TARGET);
            } else if (!withDiff && result.compareTo(diff[1]) == 0) {
                log.info("{}配料称量：无允差，称量结果{}等于目标结果{}，结束称量，进入余料称量", LOG_PREFIX, result, Arrays.toString(diff));
                // 完成配料称量(精确等于)
                batchProcess.setWeighProcess(WeighProcess.ODD);
                updateProcess = true;
            } else if (!withDiff && result.compareTo(diff[1]) > 0) {
                log.info("{}配料称量：称量结果{}超出允差范围{}", LOG_PREFIX, result, Arrays.toString(diff));
                throw new BmosException(MesResponseCode.INGREDIENT_WEIGH_OVER_TARGET);
            } else {
                log.info("{}配料称量：称量结果{}不满足允差{}, 继续配料称量", LOG_PREFIX, result, Arrays.toString(diff));
            }
        } else if (Objects.equals(batchProcess.getWeighProcess(), WeighProcess.ODD)) {
            // 余料称量
            List<IngredientWeighRecord> ing = iIngredientWeighRecordMapper.queryByWeighBatchProcessId(batchProcess.getId(), WeighType.INGREDIENT);
            BigDecimal ingWeigh = ing.stream().map(IngredientWeighRecord::getNetWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
            diff = DiffUtil.diff(unitCache.toExt(batchProcess.getPutInQuantity().subtract(ingWeigh), planBatch.getUnitId()),
                    productFormulaMaterial.getOddmentToleranceUpper(),
                    productFormulaMaterial.getOddmentToleranceLower(),
                    productFormulaMaterial.getOddmentToleranceType(),
                    productFormulaMaterial.getScale(),
                    productFormulaMaterial.getScaleLength()
            );
            boolean withDiff = withDiff(diff);
            // 处理单边允差
            if (withDiff) {
                if (diff[0] == null) {
                    diff[0] = diff[1];
                }
                if (diff[2] == null) {
                    diff[2] = diff[1];
                }
            }
            if (withDiff && result.compareTo(diff[0]) >= 0 && result.compareTo(diff[2]) <= 0) {
                // 完成余料称量(允差范围内 或者大于下限即可（因为有超出的情况）)
                log.info("{}余料称量：称量结果{}在允差范围{}内，结束称量，完成余料称量", LOG_PREFIX, result, Arrays.toString(diff));
                batchProcess.setWeighProcess(WeighProcess.FINISHED);
                batchProcess.setWeighStatus(IngredientWeighStatus.FINISHED);
                updateProcess = true;
            } else if (withDiff && result.compareTo(diff[2]) > 0) {
                log.info("{}余料称量：称量结果{}超出允差范围{}，结束称量，完成余料称量", LOG_PREFIX, result, Arrays.toString(diff));
                batchProcess.setWeighProcess(WeighProcess.FINISHED);
                batchProcess.setWeighStatus(IngredientWeighStatus.FINISHED);
                updateProcess = true;
            } else if (!withDiff && result.compareTo(diff[1]) == 0) {
                log.info("{}余料称量：无允差，称量结果{}等于目标结果{}，完成余料称量", LOG_PREFIX, result, Arrays.toString(diff));
                batchProcess.setWeighProcess(WeighProcess.FINISHED);
                batchProcess.setWeighStatus(IngredientWeighStatus.FINISHED);
                updateProcess = true;
            } else if (!withDiff && result.compareTo(diff[1]) > 0) {
                log.info("{}余料称量：无允差，称量结果{}大于目标结果{}，完成余料称量", LOG_PREFIX, result, Arrays.toString(diff));
                batchProcess.setWeighProcess(WeighProcess.FINISHED);
                batchProcess.setWeighStatus(IngredientWeighStatus.FINISHED);
                updateProcess = true;
            } else {
                log.info("{}余料称量：称量结果{}不满足允差{}, 继续余料称量", LOG_PREFIX, result, Arrays.toString(diff));
            }
        }
        if (updateProcess) {
            ingredientWeighBatchProcessMapper.updateById(batchProcess);
        }
        // 记录日志
        ProductMaterial material = materialMapper.selectAllInfoById(storageMaterialBatch.getMaterialId());

        // 保存称量日志
        saveWeighLog(dto, record, batchProcess, weighType, material, storageMaterial, plan, storageMaterialBatch, ingredientWeighProcess);
        List<IngredientWeighBatchProcess> ingredientWeighBatchProcesses = ingredientWeighBatchProcessMapper.queryBatchProcessByComponent(batchProcess.getIngredientPlanId(), batchProcess.getComponentId(), batchProcess.getCopyVersion(), batchProcess.getProcedureStepModelId(), batchProcess.getReuse());
        List<IngredientWeighRecord> ingredientWeighRecords = iIngredientWeighRecordMapper.queryByBatchProcessIds(ingredientWeighBatchProcesses.stream().map(IngredientWeighBatchProcess::getId).collect(Collectors.toList()));
        // 回显组件数据
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(batchProcess.getProcedureStepModelId());
        List<IngredientWeighRecordComponentView> ingredientWeighRecordComponentViews = trantoComponentViewList(ingredientWeighRecords);
        BusinessComponentBatchSaveDTO business = new BusinessComponentBatchSaveDTO();
        buildBusinessDTO(plan.getId(), procedureStepModel, batchProcess, business);
        List<ExecuteFormData> results = generateExecuteFormData(ingredientWeighProcess.getProductPlanId(), procedureStepModel, batchProcess, ingredientWeighRecordComponentViews, business, storageMaterialBatch.getMaterialId());
        executeFormDataService.saveResultsAndHandleRelationComponentData(results, business.transToBaseDTO());
        // 查询单次称量结果
        List<IngredientWeighRecord> ingredientResult = iIngredientWeighRecordMapper.queryByWeighBatchProcessId(batchProcess.getId(), WeighType.INGREDIENT);
        BigDecimal ingredientResultSum = ingredientResult.stream()
                .map(IngredientWeighRecord::getNetWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<IngredientWeighRecord> weighResult = Objects.equals(weighType, WeighType.INGREDIENT) ? ingredientResult : iIngredientWeighRecordMapper.queryByWeighBatchProcessId(batchProcess.getId(), weighType);
        BigDecimal sum = weighResult.stream()
                .map(IngredientWeighRecord::getNetWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<Long> materialPositionIds = weighResult.stream().map(IngredientWeighRecord::getMaterialPositionId).collect(Collectors.toList());
        Map<Long, CargoPosition> postionMap = CollectionUtil.isEmpty(materialPositionIds) ? new HashMap<>()
                : cargoPositionMapper.selectBatchIds(materialPositionIds)
                .stream()
                .collect(Collectors.toMap(BaseDO::getId, Function.identity(), (k1, k2) -> k2));
        WeighResult resultView = new WeighResult();
        resultView.setNo(storageMaterial.getNo());
        resultView.setQuantity(unitCache.toExt(batchProcess.getPutInQuantity(), planBatch.getUnitId()).stripTrailingZeros());
        if (Objects.equals(weighType, WeighType.INGREDIENT)) {
            resultView.setTargetQuantity(planBatch.getIngredientQuantity().stripTrailingZeros());
            resultView.setWeighedQuantity(unitCache.toExt(sum, planBatch.getUnitId()).stripTrailingZeros());
            resultView.setUnWeighedQuantity(checkZero(resultView.getTargetQuantity().subtract(resultView.getWeighedQuantity())).stripTrailingZeros());
        } else {
            resultView.setTargetQuantity(checkZero(unitCache.toExt(batchProcess.getPutInQuantity(), planBatch.getUnitId()).subtract(unitCache.toExt(ingredientResultSum, planBatch.getUnitId()))).stripTrailingZeros());
            resultView.setWeighedQuantity(unitCache.toExt(sum, planBatch.getUnitId()).stripTrailingZeros());
            resultView.setUnWeighedQuantity(checkZero(resultView.getTargetQuantity().subtract(resultView.getWeighedQuantity())).stripTrailingZeros());
        }
        resultView.setUnit(unitCache.getGlobalUnitName(planBatch.getUnitId()));
        resultView.setNextProcess(batchProcess.getWeighProcess());
        resultView.setResultItemList(weighResult.stream()
                .map(item -> {
                    WeighResult.WeighResultItem res = new WeighResult.WeighResultItem();
                    res.setStorageMaterialId(item.getStorageMaterialId());
                    res.setGrossWeight(unitCache.toExt(item.getGrossWeight(), item.getUnitId()).stripTrailingZeros());
                    res.setNetWeight(unitCache.toExt(item.getNetWeight(), item.getUnitId()).stripTrailingZeros());
                    res.setTareWeight(checkZero(res.getGrossWeight().subtract(res.getNetWeight())).stripTrailingZeros());
                    res.setUnit(unitCache.getGlobalUnitName(item.getUnitId()));
                    res.setContainerName(item.getContainerName());
                    res.setMaterialPositionName(Optional.ofNullable(item.getMaterialPositionId())
                            .map(postionMap::get)
                            .map(cargoPosition -> cargoPosition.getCode() + "-" + cargoPosition.getPosition())
                            .orElse(null));
                    return res;
                })
                .collect(Collectors.toList()));
        // 确认编码
        storageMaterialService.confirmSerial(storageMaterial.getNo());

        // 记录配料称量日志
        storageMaterialPositionLogService.saveLog(logs);

        return resultView;
    }

    private List<Long> selectStepModelIdListByReusable(Long stepModelId) {
        ProcedureStepModel model = procedureStepModelMapper.selectById(stepModelId);
        //如果可复用找到当前工艺版本下复用记录项的所有步骤
        if (model.getReusable()) {
            List<ProcedureStepModel> stepModelList = procedureStepModelMapper.queryListByRecordItemIdAndProcessIdAndVersion(model.getRecordItemId(),
                    model.getProcessId(), model.getProcessVersion(), model.getReusable());
            return CollectionUtils.convertList(stepModelList, ProcedureStepModel::getId);
        }
        //如果不复用只影响当前工步的
        return Collections.singletonList(stepModelId);
    }

    private void publishWeightEvent(Long planId, boolean sign, Long stepModelId) {
        List<Long> stepModelIdList = selectStepModelIdListByReusable(stepModelId);
        if (sign) {
            List<IngredientWeighValidateSignDTO> validateSignDTOS = stepModelIdList.stream().map(item -> {
                IngredientWeighValidateSignDTO signDTO = new IngredientWeighValidateSignDTO();
                signDTO.setProductPlanId(planId);
                signDTO.setProcedureStepModelId(item);
                return signDTO;
            }).collect(Collectors.toList());
            //校验当前步骤以及复用步骤是否签名完成
            if (!validateComponentSign(validateSignDTOS)) {
                return;
            }
        }
        conditionChangeHandler.refreshConditionResult(new WeighingIngredientSignType(planId, sign, stepModelIdList));
    }

    private void saveWeighLog(IngredientWeighAndPrintDTO dto, IngredientWeighRecord record, IngredientWeighBatchProcess batchProcess, WeighType weighType, ProductMaterial material, StorageMaterial storageMaterial, Plan plan, StorageMaterialBatch storageMaterialBatch, IngredientWeighProcess ingredientWeighProcess) {
        WeighLogSaveDTO saveLogDTO = WeighLogSaveDTO
                .builder()
                .unitId(record.getUnitId())
                .weigherId(batchProcess.getWeigherId())
                .reCheckerId(batchProcess.getReCheckerId())
                .weighType(weighType)
                .netWeight(record.getNetWeight())
                .grossWeight(record.getGrossWeight())
                .tareWeight(record.getTareWeight())
                .weighTime(LocalDateTime.now())
                .materialName(material.getName())
                .materialMergeCode(material.getMergeCode())
                .materialNo(storageMaterial.getNo())
                .materialId(storageMaterial.getId())
                .productId(plan.getProductId())
                .materialBatchId(batchProcess.getStorageMaterialBatchId())
                .materialId(storageMaterial.getMaterialId())
                .materialType(material.getCategoryType())
                .materialBatchNo(storageMaterialBatch.getMaterialBatchNo())
                .productPlanId(ingredientWeighProcess.getProductPlanId())
                .equipmentId(dto.getContainerId())
                .build();
        if (!Objects.equals(WeighMode.MANUAL.getValue(), dto.getWeighMode()) && dto.getDeviceId() != null) {
            EquipmentInfoFeignVO device = FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByEquipmentId(data), dto.getDeviceId()).getData();
            if (device != null) {
                WeighBalanceEquipment weighBalanceEquipment = ScanDeviceConvert.INSTANCE.convertToEquipment(device);
                saveLogDTO.setEquipmentCode(device.getCode());
                saveLogDTO.setEquipmentName(device.getName());
                saveLogDTO.setEquipmentExpireDate(weighBalanceEquipment.getCalibrateExpiredDate());
                saveLogDTO.setEquipmentStatus(weighBalanceEquipment.getIsCalibrated());
            }
        }
        weighLogService.saveLog(saveLogDTO);
    }

    private List<IngredientWeighRecordComponentView> trantoComponentViewList(List<IngredientWeighRecord> ingredientWeighRecords) {
        if (CollectionUtil.isEmpty(ingredientWeighRecords)) {
            return new ArrayList<>();
        }
        Map<Long, StorageMaterialVO> map = storageMaterialService.queryInfoByIds(ingredientWeighRecords.stream().map(IngredientWeighRecord::getStorageMaterialId)
                .collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(StorageMaterialVO::getId, Function.identity(), (k1, k2) -> k1));
        List<IngredientWeighRecordComponentView> result = new ArrayList<>();
        for (IngredientWeighRecord item : ingredientWeighRecords) {
            if (!Objects.equals(item.getWeighType(), WeighType.INGREDIENT)) {
                continue;
            }
            IngredientWeighRecordComponentView view = new IngredientWeighRecordComponentView();
            StorageMaterialVO storageMaterial = map.get(item.getStorageMaterialId());
            if (storageMaterial != null) {
                view.setMaterialId(storageMaterial.getMaterialId());
                view.setMaterialName(storageMaterial.getMaterialName());
                view.setMergeCode(storageMaterial.getMergeCode());
                view.setSpecification(storageMaterial.getMaterialSpecification());
                view.setMaterialBatchNo(storageMaterial.getMaterialBatchNo());
                view.setMaterialNo(storageMaterial.getMaterialNo());
                view.setTareWeight(item.getTareWeight());
                view.setGrossWeight(item.getGrossWeight());
                view.setNetWeight(item.getNetWeight());
                view.setUnit(unitCache.getGlobalUnitName(item.getUnitId()));
                view.setWeigherName(UserUtils.getUsername(item.getWeigherId()));
                view.setReCheckerName(UserUtils.getUsername(item.getReCheckerId()));
                view.setWeighTime(item.getWeighTime());
                view.setWeighType(item.getWeighType());
                result.add(view);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sign(IngredientWeighSignDTO dto) {
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(dto.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        IngredientWeighProcess ingredientWeighProcess = ingredientWeighProcessMapper.getIngredientWeighProcessByIngredientPlanId(dto.getPlanId(), dto.getComponentId(), dto.getProcedureStepModelId(), dto.getCopyVersion(), procedureStepModel.getReusable());
        if (ingredientWeighProcess == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_WEIGHT_PROCESS_NOT_EXIST);
        }
        List<IngredientWeighBatchProcess> ingredientWeighBatchProcesses = ingredientWeighBatchProcessMapper.queryBatchProcessByComponent(
                dto.getPlanId(),
                dto.getComponentId(),
                dto.getCopyVersion(),
                dto.getProcedureStepModelId(),
                procedureStepModel.getReusable());
        if (CollectionUtil.isEmpty(ingredientWeighBatchProcesses)) {
            return;
        }
        List<IngredientWeighRecord> weighRecords = iIngredientWeighRecordMapper.queryByBatchProcessIds(ingredientWeighBatchProcesses.stream()
                        .map(IngredientWeighBatchProcess::getId)
                        .collect(Collectors.toList()))
                .stream()
                .filter(item -> Objects.equals(item.getSignStatus(), WeighSignStatus.UN_SIGNED))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(weighRecords)) {
            weighRecords.forEach(weighRecord -> {
                weighRecord.setRemark(dto.getRemark());
                weighRecord.setSignStatus(WeighSignStatus.SIGNED);
            });
            iIngredientWeighRecordMapper.updateBatch(weighRecords);
            IngredientPlan ingredientPlan = ingredientPlanMapper.selectById(dto.getPlanId());
            this.publishWeightEvent(ingredientPlan.getProductPlanId(), true, dto.getProcedureStepModelId());
            List<Long> storageMaterialIds = weighRecords.stream().map(IngredientWeighRecord::getStorageMaterialId).collect(Collectors.toList());
            storageMaterialService.signBatchByIdList(storageMaterialIds);
        }
    }

    @Nullable
    @Override
    public WeighStorageMaterialBatchVO queryWeighDetailByPlanIdAndBatchId(Long ingredientPlanId, Long storageMaterialBatchId) {

        IngredientMaterialBatch planBatch = ingredientMaterialBatchMapper.getByIngredientPlanIdAndMaterialBatchId(ingredientPlanId, storageMaterialBatchId);
        if (planBatch == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_BATCH_NOT_EXIST);
        }
        StorageMaterialBatch batch = storageMaterialBatchMapper.selectById(planBatch.getMaterialBatchId());
        if (batch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        ProductMaterial material = materialMapper.selectAllInfoById(batch.getMaterialId());
        if (material == null) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        ProductFormulaMaterial productFormulaMaterial = formulaMaterialMapper.selectById(planBatch.getFormulaMaterialId());
        if (productFormulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXIST);
        }
        WeighStorageMaterialBatchVO result = new WeighStorageMaterialBatchVO();
        result.setIngredientPlanId(planBatch.getIngredientPlanId());
        result.setStorageMaterialBatchId(planBatch.getMaterialBatchId());
        result.setStorageMaterialName(material.getName());
        result.setStorageMaterialCode(material.getMergeCode());
        result.setStorageMaterialBatchNo(batch.getMaterialBatchNo());
        result.setUnitId(planBatch.getUnitId());
        result.setUnit(unitCache.getGlobalUnitName(planBatch.getUnitId()));
        // 配方物料精度
        result.setScale(productFormulaMaterial.getScale());
        // 物料总量(该配料批次的配料消耗量，来源为配料消耗记录)
        result.setToleranceTypeEnum(productFormulaMaterial.getChargeMixtureToleranceType());
        result.setMaxTolerance(productFormulaMaterial.getChargeMixtureToleranceUpper() == null ? null : productFormulaMaterial.getChargeMixtureToleranceUpper().stripTrailingZeros());
        result.setMinTolerance(productFormulaMaterial.getChargeMixtureToleranceLower() == null ? null : productFormulaMaterial.getChargeMixtureToleranceLower().stripTrailingZeros());
        result.setOddToleranceTypeEnum(productFormulaMaterial.getOddmentToleranceType());
        result.setOddMinTolerance(productFormulaMaterial.getOddmentToleranceLower() == null ? null : productFormulaMaterial.getOddmentToleranceLower().stripTrailingZeros());
        result.setOddMaxTolerance(productFormulaMaterial.getOddmentToleranceUpper() == null ? null : productFormulaMaterial.getOddmentToleranceUpper().stripTrailingZeros());
        IngredientWeighBatchProcess batchProcess = ingredientWeighBatchProcessMapper.queryByIngredientPlanIdAndStorageMaterialBatchId(ingredientPlanId, storageMaterialBatchId);
        result.setConsumeTotalQuantity(batchProcess == null ? BigDecimal.ZERO : unitCache.toExt(batchProcess.getPutInQuantity(), planBatch.getUnitId()).stripTrailingZeros());
        if (batchProcess == null) {
            return result;
        }
        IngredientWeighProcess process = ingredientWeighProcessMapper.selectById(batchProcess.getIngredientWeighProcessId());
        if (process == null) {
            return result;
        }
        result.setWeighProcess(batchProcess.getWeighProcess());
        result.setWeigherId(process.getPreWeigherId());
        result.setReCheckerId(process.getPreReCheckerId());
        result.setWeigherName(UserUtils.getUsername(process.getPreWeigherId()));
        result.setReCheckerName(UserUtils.getUsername(process.getPreReCheckerId()));
        result.setCategoryType(CategoryInfoTypeEnum.getEnumByValue(material.getCategoryType()));
        // 已称重的批次信息
        List<IngredientWeighRecord> weighRecords = iIngredientWeighRecordMapper.queryByWeighBatchProcessId(batchProcess.getId(), null);
        List<IngredientWeighRecord> ingredientWeighRecords = weighRecords.stream()
                .filter(item -> Objects.equals(item.getWeighType(), WeighType.INGREDIENT))
                .collect(Collectors.toList());
        if (Objects.equals(batchProcess.getWeighProcess(), WeighProcess.INGREDIENT)) {
            // 配料称量中
            result.setTargetTotalQuantity(planBatch.getIngredientQuantity().stripTrailingZeros());
            BigDecimal weighedQuantity = CollectionUtil.isEmpty(ingredientWeighRecords) ? BigDecimal.ZERO : ingredientWeighRecords.stream()
                    .map(IngredientWeighRecord::getNetWeight)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 该配料批次的配料称量已称量的重量，来源为配料称量产生的物料件汇总
            result.setWeighedQuantity(unitCache.toExt(weighedQuantity, planBatch.getUnitId()).stripTrailingZeros());
            result.setUnWeighedQuantity(checkZero(result.getTargetTotalQuantity().subtract(result.getWeighedQuantity())).stripTrailingZeros());
            // 目标量(该配料批次的配料称量总目标量（配料量），来源为配料计划)
        } else if (Objects.equals(batchProcess.getWeighProcess(), WeighProcess.ODD)) {
            if (CollectionUtil.isNotEmpty(weighRecords)) {
                // 余料称量中
                // 目标量(该配料批次的余料称量总目标量，由"物料总量"减去"配料称量的已称量”；)
                List<IngredientWeighRecord> oddWeighRecords = weighRecords.stream()
                        .filter(item -> Objects.equals(item.getWeighType(), WeighType.ODD))
                        .collect(Collectors.toList());
                BigDecimal weighedQuantity = CollectionUtil.isEmpty(ingredientWeighRecords) ? BigDecimal.ZERO : ingredientWeighRecords.stream()
                        .map(IngredientWeighRecord::getNetWeight)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal oddWeighedQuantity = CollectionUtil.isEmpty(oddWeighRecords) ? BigDecimal.ZERO : oddWeighRecords.stream()
                        .map(IngredientWeighRecord::getNetWeight)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                result.setTargetTotalQuantity(checkZero(result.getConsumeTotalQuantity().subtract(unitCache.toExt(weighedQuantity, planBatch.getUnitId()))).stripTrailingZeros());
                // 余料已称量
                result.setWeighedQuantity(unitCache.toExt(oddWeighedQuantity, planBatch.getUnitId()).stripTrailingZeros());
                // 余料未称量 = 配料计划量 - 已称量 - 已称余料
                result.setUnWeighedQuantity(checkZero(result.getTargetTotalQuantity().subtract(result.getWeighedQuantity())).stripTrailingZeros());
            } else {
                // 配料未称量
                result.setWeighedQuantity(unitCache.toExt(BigDecimal.ZERO, planBatch.getUnitId()).stripTrailingZeros());
                result.setTargetTotalQuantity(result.getConsumeTotalQuantity().stripTrailingZeros());
                result.setUnWeighedQuantity(checkZero(result.getTargetTotalQuantity().subtract(result.getWeighedQuantity())).stripTrailingZeros());
            }
        }

        BigDecimal ingredientWeigh = ingredientWeighRecords.stream()
                .map(IngredientWeighRecord::getNetWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 配料允差范围
        BigDecimal[] ingDiff = DiffUtil.diff(checkZero(result.getTargetTotalQuantity()),
                result.getMaxTolerance(),
                result.getMinTolerance(),
                result.getToleranceTypeEnum(),
                productFormulaMaterial.getScale(), productFormulaMaterial.getScaleLength());
        result.setToleranceDiff(new BigDecimal[]{
                checkZero(ingDiff[0] == null ? null : ingDiff[0].subtract(result.getWeighedQuantity()).stripTrailingZeros()),
                checkZero(ingDiff[1] == null ? null : ingDiff[1].subtract(result.getWeighedQuantity()).stripTrailingZeros()),
                checkZero(ingDiff[2] == null ? null : ingDiff[2].subtract(result.getWeighedQuantity()).stripTrailingZeros()),
        });

        // 余料允差范围
        BigDecimal[] oddDiff = DiffUtil.diff(checkZero(result.getConsumeTotalQuantity().subtract(unitCache.toExt(ingredientWeigh, result.getUnitId()))),
                result.getOddMaxTolerance(),
                result.getOddMinTolerance(),
                result.getOddToleranceTypeEnum(), productFormulaMaterial.getScale(), productFormulaMaterial.getScaleLength());
        result.setOddToleranceDiff(new BigDecimal[]{
                checkZero(oddDiff[0] == null ? null : (oddDiff[0].subtract(result.getWeighedQuantity())).stripTrailingZeros()),
                checkZero(oddDiff[1] == null ? null : (oddDiff[1].subtract(result.getWeighedQuantity())).stripTrailingZeros()),
                checkZero(oddDiff[2] == null ? null : (oddDiff[2].subtract(result.getWeighedQuantity())).stripTrailingZeros()),
        });
        List<IngredientWeighStorageMaterialVO> ingredientList = queryWeighedStorageMaterialList(ingredientPlanId,
                batchProcess.getComponentId(),
                batchProcess.getProcedureStepModelId(),
                batchProcess.getCopyVersion(),
                WeighType.INGREDIENT);
        if (CollectionUtil.isNotEmpty(ingredientList)) {
            result.setIngredientList(ingredientList);
        }
        List<IngredientWeighStorageMaterialVO> oddList = queryWeighedStorageMaterialList(ingredientPlanId,
                batchProcess.getComponentId(),
                batchProcess.getProcedureStepModelId(),
                batchProcess.getCopyVersion(), WeighType.ODD);
        if (CollectionUtil.isNotEmpty(oddList)) {
            result.setOddList(oddList);
        }
        return result;
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
    public IngredientWeighProcessVO getIngredientWeighProcess(InputWeighProcessQuery inputWeighProcessQuery) {
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(inputWeighProcessQuery.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }

        IngredientWeighProcess process = ingredientWeighProcessMapper.getIngredientWeighProcess(
                inputWeighProcessQuery.getProductPlanId(),
                inputWeighProcessQuery.getComponentId(),
                inputWeighProcessQuery.getProcedureStepModelId(),
                inputWeighProcessQuery.getCopyVersion(),
                procedureStepModel.getReusable()
        );
        IngredientWeighProcessVO result = IngredientWeighProcessConvert.INSTANCE.convertToVO(process);
        if (process == null || process.getIngredientPlanId() == null) {
            return result;
        }
        IngredientPlan ingredientPlan = ingredientPlanMapper.selectById(process.getIngredientPlanId());
        if (ingredientPlan != null) {
            result.setIngredientPlanName(ingredientPlan.getName());
        }
        // 查询称量中的批次id
        IngredientWeighBatchProcess batchProcess = ingredientWeighBatchProcessMapper.queryBatchProcessByComponent(
                process.getIngredientPlanId(),
                inputWeighProcessQuery.getComponentId(),
                inputWeighProcessQuery.getCopyVersion(),
                inputWeighProcessQuery.getProcedureStepModelId(),
                procedureStepModel.getReusable(),
                IngredientWeighStatus.PROCESSING
        );
        if (batchProcess != null && Objects.equals(batchProcess.getWeighStatus(), IngredientWeighStatus.PROCESSING)) {
            Optional.of(batchProcess)
                    .map(bp -> storageMaterialBatchMapper.selectById(bp.getStorageMaterialBatchId()))
                    .ifPresent(pending -> {
                        result.setPendingStorageMaterialBatchId(pending.getId());
                        result.setPendingStorageMaterialBatchNo(pending.getMaterialBatchNo());
                        ProductMaterial productMaterial = materialMapper.selectById(pending.getMaterialId());
                        if (productMaterial != null) {
                            result.setPendingStorageMaterialFullName(productMaterial.getMergeCode() + "-" + productMaterial.getName());
                        }
                        result.setPendingStorageMaterialBatchUnitId(pending.getFinalUnitId());
                        result.setPendingStorageMaterialBatchUnit(unitCache.getGlobalUnitName(pending.getFinalUnitId()));
                    });
        }

        // 查询工位 优先查询组件中配置的
        List<String> stationPathIds = Optional.of(procedureStepModel)
                .map(stm -> procedureStepConfigMapper.selectComponentConfig(
                        stm.getId(),
                        inputWeighProcessQuery.getComponentId(),
                        stm.getReusable(),
                        stm.getProcessId(),
                        stm.getProcessVersion()))
                .map(ProcedureStepConfig::getConfigInfo)
                .map(configStr -> JSONUtil.parseObj(configStr).getJSONArray(ProcessConstant.stationPathField))
                .map(s -> s.toList(String.class))
                .orElse(new ArrayList<>());
        Plan plan = planService.getById(process.getProductPlanId());
        // 组件未配置的 查询产线下的所有工位
        if (CollectionUtil.isEmpty(stationPathIds)) {
            result.setStation(Optional.ofNullable(FeignUtils.handleRequest(plId ->
                                    factoryFeign.queryStationListByProductLineIds(Collections.singletonList(plId)),
                            plan.getProductionLineId()).getData())
                    .map(stationList -> stationList.stream().map(FactoryStationFeignVO::getId).collect(Collectors.toList()))
                    .orElse(new ArrayList<>()));
            return result;
        }
        result.setStation(stationPathIds.stream()
                .filter(e -> e.startsWith(String.valueOf(plan.getProductionLineId())))
                .map(e -> {
                    List<String> split = StrUtil.split(e, StrUtil.DASHED);
                    return Long.valueOf(CollUtil.getLast(split));
                }).collect(Collectors.toList()));
        return result;
    }

    @Override
    public ScanDeviceVO scanDeviceCode(ScanDeviceCodeDTO scanQuery) {
        ResponseInfo<EquipmentInfoFeignVO> equipment = FeignUtils.handleRequest(data -> equipmentConfigFeign.getEquipmentByEquipmentCode(data), scanQuery.getDeviceCode());
        return ScanDeviceConvert.INSTANCE.convertToDeviceVo(equipment.getData());
    }

    @Override
    public List<WeighBalanceEquipment> getBalanceListByStationIds(List<Long> stationIds) {

        if (CollectionUtils.isAnyEmpty(stationIds)){
            return new ArrayList<>();
        }

        EquipmentQueryDTO query = new EquipmentQueryDTO();
        query.setStationIdList(stationIds);
        // 称具
        query.setTagCode(EquipmentTagCodeEnum.WEIGHING_DEVICE_12020.getCode());
        ResponseInfo<List<EquipmentInfoFeignVO>> configByStationId = equipmentConfigFeign.getConfigByStationIdList(query);
        if (CollUtil.isEmpty(configByStationId.getData())) {
            return Collections.emptyList();
        }
        return ScanDeviceConvert.INSTANCE.convertToEquipmentList(configByStationId.getData());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finish(IngredientWeighFinishDTO weighFinishDTO) {
        IngredientWeighBatchProcess batchProcess = ingredientWeighBatchProcessMapper.queryByIngredientPlanIdAndStorageMaterialBatchId(weighFinishDTO.getIngredientPlanId(), weighFinishDTO.getStorageMaterialBatchId());
        if (batchProcess == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_BATCH_NOT_EXIST);
        }

        IngredientWeighProcess ingredientWeighProcess = ingredientWeighProcessMapper.selectById(batchProcess.getIngredientWeighProcessId());
        if (ingredientWeighProcess == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_NOT_EXIST);
        }
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchMapper.selectById(weighFinishDTO.getStorageMaterialBatchId());
        if (storageMaterialBatch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        WeighProcess weighProcess = batchProcess.getWeighProcess();
        if (Objects.equals(WeighProcess.INGREDIENT, weighProcess)) {
            // 完成配料称量
            batchProcess.setWeighProcess(WeighProcess.ODD);
        } else if (Objects.equals(WeighProcess.ODD, weighProcess)) {
            // 完成余料称量
            batchProcess.setWeighProcess(WeighProcess.FINISHED);
            batchProcess.setWeighStatus(IngredientWeighStatus.FINISHED);
        }
        ingredientWeighBatchProcessMapper.updateById(batchProcess);

        // 回显组件数据
        List<IngredientWeighBatchProcess> ingredientWeighBatchProcesses = ingredientWeighBatchProcessMapper.queryBatchProcessByComponent(batchProcess.getIngredientPlanId(), batchProcess.getComponentId(), batchProcess.getCopyVersion(), batchProcess.getProcedureStepModelId(), batchProcess.getReuse());
        List<IngredientWeighRecord> ingredientWeighRecords = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(ingredientWeighBatchProcesses)) {
            ingredientWeighRecords = iIngredientWeighRecordMapper.queryByBatchProcessIds(ingredientWeighBatchProcesses.stream().map(IngredientWeighBatchProcess::getId).collect(Collectors.toList()));
        }
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(batchProcess.getProcedureStepModelId());
        List<IngredientWeighRecordComponentView> ingredientWeighRecordComponentViews = trantoComponentViewList(ingredientWeighRecords);
        BusinessComponentBatchSaveDTO business = new BusinessComponentBatchSaveDTO();
        List<ExecuteFormData> results = generateExecuteFormData(ingredientWeighProcess.getProductPlanId(), procedureStepModel, batchProcess, ingredientWeighRecordComponentViews, business, storageMaterialBatch.getMaterialId());
        executeFormDataService.saveResultsAndHandleRelationComponentData(results, business.transToBaseDTO());
    }

    @Override
    public IngredientWeighStorageMaterialListVO queryResult(IngredientWeighResultQuery query) {

        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(query.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }

        IngredientWeighStorageMaterialListVO list = new IngredientWeighStorageMaterialListVO();
        // 该配料批次的配料称量未称量的重量，为目标量减去已称量的值
        List<IngredientWeighStorageMaterialVO> ingredientList = queryWeighedStorageMaterialList(
                query.getPlanId(),
                query.getComponentId(),
                query.getProcedureStepModelId(),
                query.getCopyVersion(),
                WeighType.INGREDIENT);
        if (CollectionUtil.isNotEmpty(ingredientList)) {
            list.setIngredientList(ingredientList);
        }
        List<IngredientWeighStorageMaterialVO> oddList = queryWeighedStorageMaterialList(query.getPlanId(),
                query.getComponentId(),
                query.getProcedureStepModelId(),
                query.getCopyVersion(), WeighType.ODD);
        if (CollectionUtil.isNotEmpty(oddList)) {
            list.setOddList(oddList);
        }

        IngredientWeighProcess process = ingredientWeighProcessMapper.getIngredientWeighProcessByIngredientPlanId(query.getPlanId(), query.getComponentId(), query.getProcedureStepModelId(), query.getCopyVersion(), procedureStepModel.getReusable());
        if (process == null) {
            return list;
        }

        BaseUserDO weigher = UserUtils.getUser(process.getPreWeigherId());
        BaseUserDO reChecker = UserUtils.getUser(process.getPreReCheckerId());
        if (weigher != null) {
            list.setWeigherId(weigher.getUserId());
            list.setWeigherName(weigher.getUserName());
            list.setWeigherLoginName(weigher.getLoginName());
        }
        if (reChecker != null) {
            list.setReCheckerId(reChecker.getUserId());
            list.setReCheckerName(reChecker.getUserName());
            list.setReCheckerLoginName(reChecker.getLoginName());
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeWeigher(IngredientChangeWeigherDTO dto) {
        IngredientWeighProcess ingredientWeighProcess = ingredientWeighProcessMapper.selectById(dto.getIngredientWeighProcessId());
        if (ingredientWeighProcess == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_WEIGHT_PROCESS_NOT_EXIST);
        }
        // 未签名的记录
        List<IngredientWeighRecord> recordList = iIngredientWeighRecordMapper.queryByIngredientPlanId(ingredientWeighProcess.getIngredientPlanId())
                .stream()
                .filter(item -> Objects.equals(item.getSignStatus(), WeighSignStatus.UN_SIGNED))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(recordList)) {
            // 已称量物料件需签名后才能更换
            throw new BmosException(MesResponseCode.WEIGH_RECORD_EXIST_UNSINGED_RECORD);
        }
        ingredientWeighProcess.setPreWeigherId(dto.getWeigherId());
        ingredientWeighProcess.setPreReCheckerId(dto.getReCheckerId());
        ingredientWeighProcessMapper.updateById(ingredientWeighProcess);

        List<IngredientWeighBatchProcess> ingredientWeighBatchProcesses = ingredientWeighBatchProcessMapper.queryProcessingBatchByPlanId(ingredientWeighProcess.getIngredientPlanId());
        if (CollectionUtil.isNotEmpty(ingredientWeighBatchProcesses)) {
            ingredientWeighBatchProcesses.forEach(item -> {
                item.setWeigherId(dto.getWeigherId());
                item.setReCheckerId(dto.getReCheckerId());
            });
            ingredientWeighBatchProcessMapper.updateBatch(ingredientWeighBatchProcesses);
        }
    }

    @Override
    public ScanDeviceVO scanWeighContainerCode(String code) {
        EquipmentInfoFeignVO container = FeignUtils.handleRequest(c -> equipmentConfigFeign.getEquipmentByEquipmentCodeWithoutPermission(c), code).getData();
        if (container == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_NOT_EXIST);
        }
        validateContainer(container);
        if (!Objects.equals(container.getStatus(), EquipmentStatusCodeEnum.AVAILABLE.getCode())) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_NOT_AVAILABLE);
        }
        StorageMaterial exist = storageMaterialService.getByContainerId(container.getId());
        if (exist != null) {
            throw new BmosException(MesResponseCode.CONTAINER_ALREADY_HAS_MATERIAL);
        }
        ScanDeviceVO result = new ScanDeviceVO();
        result.setDeviceId(container.getId());
        result.setDeviceCode(container.getCode());
        result.setDeviceName(container.getName());
        return result;
    }

    private static void validateContainer(EquipmentInfoFeignVO equipment) {
        // 校验是否是容器
        if (equipment == null || CollUtil.isEmpty(equipment.getEquipmentTagDataList())) {
            throw new BmosException(MesResponseCode.PLEASE_SCAN_CONTAINER_TAG);
        }
        Optional<TagFeignVO> any = equipment.getEquipmentTagDataList().stream()
                .filter(item -> Objects.equals(item.getCode(), EquipmentTagCodeEnum.CONTAINER_12021.getCode()))
                .findAny();
        if (!any.isPresent()) {
            throw new BmosException(MesResponseCode.PLEASE_SCAN_CONTAINER_TAG);
        }

    }

    @Override
    public ScanCargoPositionVO scanWeighPositionCode(String code) {
        CargoPosition cargoPosition = cargoPositionService.getByCodeWithPermission(code);
        if (cargoPosition == null) {
            throw new BmosException(MesResponseCode.CARGO_POSITION_NOT_EXIST);
        }
        ScanCargoPositionVO result = new ScanCargoPositionVO();
        result.setId(cargoPosition.getId());
        result.setCode(cargoPosition.getCode());
        result.setName(cargoPosition.getPosition());
        result.setFullName(cargoPosition.getCode() + "-" + cargoPosition.getPosition());
        return result;
    }

    @Override
    public Boolean validateComponentSign(List<IngredientWeighValidateSignDTO> validateSignList) {
        if (CollectionUtil.isEmpty(validateSignList)) {
            return true;
        }
        List<Long> productPlanIds = new ArrayList<>();
        List<Long> procedureStepModelIds = new ArrayList<>();
        for (IngredientWeighValidateSignDTO dto : validateSignList) {
            if (dto.getProductPlanId() != null) {
                productPlanIds.add(dto.getProductPlanId());
            }
            if (dto.getProcedureStepModelId() != null) {
                procedureStepModelIds.add(dto.getProcedureStepModelId());
            }
        }
        List<IngredientWeighProcess> ingredientWeighProcess = ingredientWeighProcessMapper.getIngredientWeighProcess(productPlanIds);
        if (CollectionUtil.isEmpty(ingredientWeighProcess)) {
            return true;
        }
        List<Long> ingredientWeighProcessIdList = ingredientWeighProcess.stream()
                .map(IngredientWeighProcess::getId)
                .collect(Collectors.toList());
        List<IngredientWeighBatchProcess> batchProcesses = ingredientWeighBatchProcessMapper.queryList(ingredientWeighProcessIdList, procedureStepModelIds);
        if (CollectionUtil.isEmpty(batchProcesses)) {
            return true;
        }
        List<Long> batchProcessIds = batchProcesses.stream().map(IngredientWeighBatchProcess::getId).collect(Collectors.toList());
        List<IngredientWeighRecord> recordList = iIngredientWeighRecordMapper.queryByBatchProcessIds(batchProcessIds);
        if (CollectionUtil.isEmpty(recordList)) {
            return true;
        }
        IngredientWeighRecord unSigned = recordList.stream()
                .filter(item -> Objects.equals(item.getSignStatus(), WeighSignStatus.UN_SIGNED))
                .findAny()
                .orElse(null);
        return unSigned == null;
    }

    /**
     * 称量消耗
     *
     * @param consumeStorageMaterialIdList 消耗物料件id列表
     * @param remark                       备注
     * @return 消耗总量（标准量）
     */
    private BigDecimal weighConsume(List<Long> consumeStorageMaterialIdList, String remark, Plan plan) {
        if (CollectionUtil.isEmpty(consumeStorageMaterialIdList)) {
            return BigDecimal.ZERO;
        }
        List<StorageMaterial> list = storageMaterialService.queryListByIds(consumeStorageMaterialIdList);
        if (CollectionUtil.isEmpty(list)) {
            return BigDecimal.ZERO;
        }

        List<StorageMaterialBatch> storageMaterialBatches = storageMaterialBatchMapper.selectBatchIds(list.stream()
                .map(StorageMaterial::getStorageMaterialBatchId)
                .collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(storageMaterialBatches)) {
            return BigDecimal.ZERO;
        }
        storageMaterialBatches.forEach(StorageMaterialBatch::availableValidate);
        return storageMaterialService.weighConsume(list, remark, plan, StorageOperateTypeEnum.WEIGH_CONSUME);
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

    private List<IngredientWeighStorageMaterialVO> queryWeighedStorageMaterialList(Long ingredientPlanId,
                                                                                   Long componentId,
                                                                                   Long procedureStepModelId,
                                                                                   Long copyVersion,
                                                                                   WeighType weighType) {
        if (ingredientPlanId == null || weighType == null || componentId == null || procedureStepModelId == null || copyVersion == null) {
            return new ArrayList<>();
        }

        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(procedureStepModelId);
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }

        List<IngredientWeighStorageMaterialVO> list = iIngredientWeighRecordMapper.queryWeighedStorageMaterialList(ingredientPlanId, weighType, componentId, procedureStepModelId, procedureStepModel.getReusable(), copyVersion);
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        list.forEach(item -> {
            BaseUserDO weigher = UserUtils.getUser(item.getWeigherId());
            BaseUserDO reChecker = UserUtils.getUser(item.getReCheckerId());
            if (weigher != null) {
                item.setWeigherName(weigher.getUserName());
                item.setWeigherLoginName(weigher.getLoginName());
            }
            if (reChecker != null) {
                item.setReCheckerName(reChecker.getUserName());
                item.setReCheckerLoginName(reChecker.getLoginName());
            }
            item.setUnit(unitCache.getGlobalUnitName(item.getUnitId()));
            item.setQuantity(unitCache.toExt(item.getQuantity(), item.getUnitId()).stripTrailingZeros());
            item.setGrossWeight(unitCache.toExt(item.getGrossWeight(), item.getUnitId()).stripTrailingZeros());
            item.setTareWeight(unitCache.toExt(item.getTareWeight(), item.getUnitId()).stripTrailingZeros());
            item.setNetWeight(unitCache.toExt(item.getNetWeight(), item.getUnitId()).stripTrailingZeros());
        });
        return list;
    }

    private Map<Long, IngredientRecordCalc> getIngredientBatchWeighStatusMap(Long ingredientPlanId) {
        Map<Long, IngredientRecordCalc> calcMap = new HashMap<>();
        for (IngredientWeighRecord record : iIngredientWeighRecordMapper.queryByIngredientPlanId(ingredientPlanId)) {
            if (!Objects.equals(record.getWeighType(), WeighType.INGREDIENT)) {
                continue;
            }
            IngredientRecordCalc calc = calcMap.get(record.getStorageMaterialBatchId());
            if (calc == null) {
                calc = new IngredientRecordCalc();
                calcMap.put(record.getStorageMaterialBatchId(), calc);
            }
            calc.setWeighedQuantity(calc.getWeighedQuantity().add(unitCache.toExt(record.getNetWeight(), record.getUnitId())));
        }
        for (IngredientWeighBatchProcess ingredientWeighBatchProcess : ingredientWeighBatchProcessMapper.queryByIngredientPlanId(ingredientPlanId)) {
            IngredientRecordCalc calc = calcMap.get(ingredientWeighBatchProcess.getStorageMaterialBatchId());
            if (calc == null) {
                calc = new IngredientRecordCalc();
                calcMap.put(ingredientWeighBatchProcess.getStorageMaterialBatchId(), calc);
            }
            calc.setIngredientWeighStatus(ingredientWeighBatchProcess.getWeighStatus());
        }
        return calcMap;
    }

    private boolean withDiff(BigDecimal[] diff) {
        return diff[0] != null && diff[2] != null;
    }

    private List<ExecuteFormData> generateExecuteFormData(Long productPlanId, ProcedureStepModel procedureStepModel, IngredientWeighBatchProcess process, List<IngredientWeighRecordComponentView> ingredientWeighRecordComponentViews, BusinessComponentBatchSaveDTO business, Long materialId) {

        List<IngredientPlanDetailVO.IngredientPlanDetailBatchVO> finishedBatchList = Optional.ofNullable(this.queryIngredientPlanById(process.getIngredientPlanId(), business.getComponentId(), procedureStepModel.getId()))
                .map(IngredientPlanDetailVO::getBatchList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(item -> !item.getWeighStatus().equals(IngredientWeighStatus.PENDING))
                .collect(Collectors.toList());

        List<Long> storageBatchIds = ingredientWeighBatchProcessMapper.queryBatchProcessByComponent(
                process.getIngredientPlanId(),
                        process.getComponentId(),
                        process.getCopyVersion(),
                        process.getProcedureStepModelId(),
                        process.getReuse())
                .stream()
                .map(IngredientWeighBatchProcess::getStorageMaterialBatchId)
                .collect(Collectors.toList());

        buildBusinessDTO(productPlanId, procedureStepModel, process, business);
        ComponentListVO component = batchRecordComponentService.selectUsedComponentDetail(procedureStepModel.getRecordVersionId(),
                procedureStepModel.getRecordItemId(), process.getComponentId());
        ProductionDetailInfo info = new ProductionDetailInfo();
        List<ExecuteFormData> results = new ArrayList<>();
        info.setDto(business);
        info.setIngredientWeighRecords(ingredientWeighRecordComponentViews);
        info.setIngredientWeighMaterialId(materialId);
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        ProductFormulaInfo formulaInfo = productFormulaConfigureService.getProductFormulaInfoByPlanId(productPlanId);
        info.setFormulaInfo(formulaInfo);

        RecordItemLatestDataQueryDTO queryDTO = getRecordItemLatestDataQueryDTO(productPlanId, procedureStepModel, process, component);
        List<FormDataItemVO> recordItemLatestData = executeFormDataService.getRecordItemLatestData(queryDTO);
        info.setFormDataCollection(recordItemLatestData);
        info.setIngredientStorageBatchId(process.getStorageMaterialBatchId());
        if (CollectionUtil.isEmpty(storageBatchIds)){
            info.setFinishedStorageBatchIdSummaryList(new ArrayList<>());
            info.setFinishedMaterialIdSummaryList(new ArrayList<>());
        }else {
            List<StorageMaterialBatch> storageMaterialBatches = storageMaterialBatchMapper.selectBatchIds(storageBatchIds);
            info.setFinishedStorageBatchIdSummaryList(storageBatchIds);
            info.setFinishedMaterialIdSummaryList(storageMaterialBatches.stream().map(StorageMaterialBatch::getMaterialId).collect(Collectors.toList()));
        }
        ingredientWeighComponentStrategy.handleBusinessComponent(results, component, info, configMap, null);
        return sameFieldValueCheck(productPlanId, procedureStepModel, results, alwaysUpdateComponentType, process, component);
    }

    private RecordItemLatestDataQueryDTO getRecordItemLatestDataQueryDTO(Long planId, ProcedureStepModel stepModel, IngredientWeighBatchProcess process, ComponentListVO component) {
        List<Long> fieldIds = new ArrayList<>();
        recGetComponentFieldList(component, fieldIds);
        RecordItemLatestDataQueryDTO queryDTO = new RecordItemLatestDataQueryDTO();
        queryDTO.setReuse(process.getReuse());
        queryDTO.setDiscard(false);
        queryDTO.setCopyVersion(process.getCopyVersion());
        queryDTO.setProductPlanId(planId);
        queryDTO.setProcedureStepId(stepModel.getProcedureStepId());
        queryDTO.setFieldIdList(fieldIds);
        queryDTO.setRecordItemId(stepModel.getRecordItemId());
        return queryDTO;
    }

    private void recGetComponentFieldList(ComponentListVO vo, List<Long> result) {
        result.add(vo.getFieldId());
        if (CollUtil.isNotEmpty(vo.getChildren())) {
            vo.getChildren().forEach(e -> {
                recGetComponentFieldList(e, result);
            });
        }
    }

    /**
     * 检查是否存在相同的值
     *
     * @param productPlanId             生产计划id
     * @param procedureStepModel        工序步骤模型
     * @param results                   称量结果所有formData
     * @param alwaysUpdateComponentType 始终更新的组件值类型
     * @return
     */
    private List<ExecuteFormData> sameFieldValueCheck(Long productPlanId, ProcedureStepModel procedureStepModel, List<ExecuteFormData> results, BusinessComponentTypeEnum[] alwaysUpdateComponentType, IngredientWeighBatchProcess process, ComponentListVO component) {
        RecordItemLatestDataQueryDTO queryDTO = getRecordItemLatestDataQueryDTO(productPlanId, procedureStepModel, process, component);
        List<FormDataItemVO> existRecord = executeFormDataService.getRecordItemLatestData(queryDTO);
        if (CollectionUtil.isEmpty(existRecord)) {
            return results;
        }
        Map<Long, String> existFields = new HashMap<>();
        for (FormDataItemVO formDataItemVO : existRecord) {
            if (formDataItemVO.getValue() != null) {
                existFields.put(formDataItemVO.getFieldId(), formDataItemVO.getValue());
            }
        }
        return results.stream()
                .filter(re -> {
                    BusinessComponentTypeEnum enumByValue = getEnumByValue(re.getComponentType());
                    if (ArrayUtil.contains(alwaysUpdateComponentType, enumByValue)) {
                        return true;
                    }
                    String value = existFields.get(re.getFieldId());
                    return value == null || !StrUtil.equals(value, re.getValue());
                })
                .collect(Collectors.toList());
    }

    private void buildBusinessDTO(Long productPlanId, ProcedureStepModel procedureStepModel, IngredientWeighBatchProcess process, BusinessComponentBatchSaveDTO dto) {
        dto.setProductPlanId(productPlanId);
        Plan plan = planMapper.selectById(productPlanId);
        dto.setBatchNo(plan.getBatchNo());
        dto.setProcessId(procedureStepModel.getProcessId());
        dto.setProcessVersion(procedureStepModel.getProcessVersion());
        dto.setRecordItemId(procedureStepModel.getRecordItemId());
        dto.setRecordVersionId(procedureStepModel.getRecordVersionId());
        dto.setProcedureStepId(procedureStepModel.getProcedureStepId());
        dto.setProcedureStepModelId(procedureStepModel.getId());
        dto.setReuse(procedureStepModel.getReusable());
        dto.setCopyVersion(process.getCopyVersion());
        dto.setComponentId(process.getComponentId());
    }

    private BigDecimal precision(BigDecimal value, ProductFormulaMaterial productFormulaMaterial) {
        return MaterialQuantityCalculateUtil.roundingOff(value,
                productFormulaMaterial.getScale(),
                productFormulaMaterial.getScaleLength(),
                RoundingEnum.getEnumByCode(productFormulaMaterial.getRounding()).getMapping());
    }
}
