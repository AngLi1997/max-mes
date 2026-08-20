package com.bmos.mes.service.ingredient.input.service.imp;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.constant.ProcessConstant;
import com.bmos.mes.common.enums.ingredient.IngredientInputStatus;
import com.bmos.mes.common.enums.ingredient.IngredientWeighStatus;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.dto.RecordItemLatestDataQueryDTO;
import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.ingredient.input.dto.IngredientInputDTO;
import com.bmos.mes.service.ingredient.input.dto.InputComponentInstanceQueryDTO;
import com.bmos.mes.service.ingredient.input.dto.PendingInputPlanListQueryListDTO;
import com.bmos.mes.service.ingredient.input.mapper.IIngredientInputComponentInstanceMapper;
import com.bmos.mes.service.ingredient.input.mapper.IIngredientInputRecordMapper;
import com.bmos.mes.service.ingredient.input.model.IngredientInputComponentInstance;
import com.bmos.mes.service.ingredient.input.model.IngredientInputRecord;
import com.bmos.mes.service.ingredient.input.model.IngredientInputRecordDetail;
import com.bmos.mes.service.ingredient.input.service.IIngredientInputService;
import com.bmos.mes.service.ingredient.input.vo.IngredientInputPlanVO;
import com.bmos.mes.service.ingredient.input.vo.IngredientInputRecordVO;
import com.bmos.mes.service.ingredient.input.vo.InputComponentInstanceVO;
import com.bmos.mes.service.ingredient.plan.mapper.IngredientMaterialBatchMapper;
import com.bmos.mes.service.ingredient.plan.mapper.IngredientPlanMapper;
import com.bmos.mes.service.ingredient.plan.model.IngredientMaterialBatchDetailInfo;
import com.bmos.mes.service.ingredient.plan.model.IngredientPlan;
import com.bmos.mes.service.ingredient.weigh.mapper.IIngredientWeighBatchProcessMapper;
import com.bmos.mes.service.ingredient.weigh.model.IngredientWeighBatchProcess;
import com.bmos.mes.service.ingredient.weigh.vo.IngredientPlanItemVO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.mapper.ProcedureStepConfigMapper;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.ProcessVersionService;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.strategy.IngredientInputComponentStrategy;
import com.bmos.mes.service.record.convert.RecordComponentConvert;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialQueryValidateDTO;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialReserveMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.model.StorageMaterialReserve;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialDetailVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialVO;
import com.bmos.mes.service.tag.dto.ScanWeighMaterialCodeWithIngredientPlanId;
import com.bmos.mes.service.trace.material.dto.MaterialTraceHistoryDTO;
import com.bmos.mes.service.trace.material.enums.MaterialTraceOperateType;
import com.bmos.mes.service.trace.material.service.IMaterialTraceHistoryService;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.google.common.base.Functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.bmos.mes.common.enums.record.BusinessComponentTypeEnum.getEnumByValue;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/26 10:16
 */
@Service
public class IIngredientInputServiceImpl implements IIngredientInputService {

    @Resource
    private IIngredientInputRecordMapper ingredientInputRecordMapper;

    @Resource
    private IngredientPlanMapper ingredientPlanMapper;

    @Resource
    private IIngredientWeighBatchProcessMapper ingredientWeighBatchProcessMapper;

    @Resource
    private IStorageMaterialMapper storageMaterialMapper;

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private UnitCache unitCache;

    @Resource
    private BatchRecordComponentService batchRecordComponentService;

    @Resource
    private ProcedureStepModelService procedureStepModelService;

    @Resource
    private ProcedureStepConfigService procedureStepConfigService;

    @Resource
    private ExecuteFormDataService executeFormDataService;

    @Resource
    private IngredientInputComponentStrategy ingredientInputComponentStrategy;

    @Resource
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Resource
    private IIngredientInputComponentInstanceMapper inputComponentInstanceMapper;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Resource
    private ProcedureStepConfigMapper procedureStepConfigMapper;

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Resource
    private IngredientMaterialBatchMapper ingredientMaterialBatchMapper;

    @Resource
    private IMaterialTraceHistoryService materialTraceHistoryService;

    private static final BusinessComponentTypeEnum[] alwaysUpdateComponentType = new BusinessComponentTypeEnum[]{
//            INGREDIENTS_INPUT_SUMMARY_MATERIAL_NAME,
//            INGREDIENTS_INPUT_SUMMARY_MATERIAL_CODE,
//            INGREDIENTS_INPUT_SUMMARY_MATERIAL_SPECIFICATION,
//            INGREDIENTS_INPUT_SUMMARY_TOTAL_QUANTITY,
//            INGREDIENTS_INPUT_SUMMARY_UNIT,
//            INGREDIENTS_INPUT_SUMMARY_TOTAL_NUMBER,
    };


    @Resource
    private IStorageMaterialPositionLogService storageMaterialPositionLogService;

    @Resource
    private PlanService planService;

    @Autowired
    private ProcessVersionService processVersionService;

    @Autowired
    private IStorageMaterialReserveMapper iStorageMaterialReserveMapper;
    @Override
    @Nullable
    public IngredientInputPlanVO queryInputListByPlanId(Long ingredientPlanId, Long componentInstanceId) {
        IngredientPlan ingredientPlan = ingredientPlanMapper.selectById(ingredientPlanId);
        if (ingredientPlan == null) {
            return null;
        }

        IngredientInputComponentInstance componentInstance = inputComponentInstanceMapper.selectById(componentInstanceId);
        if (componentInstance == null) {
            return null;
        }

        // 查询组件上的配方物料
        List<Long> formulaMaterialIds = Optional.of(componentInstance)
                .map(IngredientInputComponentInstance::getProcedureStepModelId)
                .map(procedureStepModelMapper::selectById)
                .map(procedureStepModel -> procedureStepConfigMapper.selectComponentConfig(
                        componentInstance.getProcedureStepModelId(),
                        componentInstance.getComponentId(),
                        componentInstance.getReuse(),
                        procedureStepModel.getProcessId(),
                        procedureStepModel.getProcessVersion()))
                .map(ProcedureStepConfig::getConfigInfo)
                .map(configStr -> JSONUtil.toBean(configStr, ProcedureStepConfigInfo.class))
                .map(ProcedureStepConfigInfo::getFormulaMaterialIds)
                .orElse(new ArrayList<>());
        List<Long> materialIds = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(formulaMaterialIds)){
            materialIds = productFormulaConfigureService.getFormulaMaterialListByIds(formulaMaterialIds)
                    .stream()
                    .map(ProductFormulaMaterial::getMaterialId)
                    .collect(Collectors.toList());
        }

        IngredientInputPlanVO result = new IngredientInputPlanVO();
        result.setProductPlanId(ingredientPlan.getProductPlanId());
        result.setIngredientPlanId(ingredientPlan.getId());
        result.setIngredientPlanName(ingredientPlan.getName());
        // 称量记录
        List<IngredientInputRecordVO> inputList = ingredientInputRecordMapper.queryInputListByPlanId(ingredientPlanId, materialIds);
        List<IngredientWeighBatchProcess> batchWeighProcess =
                ingredientWeighBatchProcessMapper.queryByIngredientPlanId(ingredientPlanId);
        List<IngredientMaterialBatchDetailInfo> ingredientBatchList = ingredientMaterialBatchMapper.getByIngredientId(ingredientPlanId);
        if (CollectionUtil.isNotEmpty(batchWeighProcess)) {
            if (!Objects.equals(batchWeighProcess.size(), ingredientBatchList.size()) || batchWeighProcess.stream()
                    .anyMatch(item -> !Objects.equals(item.getWeighStatus(), IngredientWeighStatus.FINISHED))) {
                result.setWeighStatus(IngredientWeighStatus.PROCESSING);
            } else {
                result.setWeighStatus(IngredientWeighStatus.FINISHED);
            }
        } else {
            result.setWeighStatus(IngredientWeighStatus.PENDING);
        }
        List<Long> storageMaterialIds =
                inputList.stream().map(IngredientInputRecordVO::getStorageMaterialId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(inputList)) {
            Map<Long, StorageMaterial> storageMaterialMap = storageMaterialMapper.selectBatchIds(storageMaterialIds)
                    .stream()
                    .collect(Collectors.toMap(StorageMaterial::getId, Functions.identity(), (k1, k2) -> k1));
            inputList.forEach(item -> {
                item.setUnit(unitCache.getGlobalUnitName(item.getUnitId()));
                if (item.getImporterId() != null) {
                    if (Objects.equals(item.getComponentInstanceId(), componentInstanceId)){
                        // 相同的组件 则显示已投料
                        item.setInputStatus(IngredientInputStatus.FINISHED);
                    }else{
                        // 不同的组件投料 显示已失效
                        item.setInputStatus(IngredientInputStatus.SCRAPED);
                    }
                } else {
                    // 待投料/作废
                    Boolean available = Optional.ofNullable(item.getStorageMaterialId())
                            .map(storageMaterialMap::get)
                            .map(StorageMaterial::isAvailable)
                            .orElse(false);
                    if (available){
                        item.setQuantity(storageMaterialMap.get(item.getStorageMaterialId()).getQuantity());
                    }
                    item.setInputStatus(available ? IngredientInputStatus.PENDING : IngredientInputStatus.SCRAPED);
                }
            });
        }
        PrecisionHelper.convertUnitRenderList(inputList);
        result.setInputList(inputList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void input(IngredientInputDTO dto) {
        IngredientPlan ingredientPlan = ingredientPlanMapper.selectById(dto.getIngredientPlanId());
        if (ingredientPlan == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_NOT_EXIST);
        }
        IngredientInputComponentInstance componentInstance =
                inputComponentInstanceMapper.selectUnique(UniqueComponentQueryDTO.builder()
                        .productPlanId(dto.getProductPlanId())
                        .componentId(dto.getComponentId())
                        .copyVersion(dto.getCopyVersion())
                        .recordItemId(dto.getRecordItemId())
                        .recordVersionId(dto.getRecordVersionId())
                        .reuse(dto.getReuse())
                        .procedureStepModelId(dto.getProcedureStepModelId())
                        .build());
        if (componentInstance == null) {
            throw new BmosException(MesResponseCode.INPUT_COMPONENT_INSTANCE_NOT_EXISTS);
        }

        // 查询组件上的配方物料
        List<Long> formulaMaterialIds = Optional.of(dto)
                .map(IngredientInputDTO::getProcedureStepModelId)
                .map(procedureStepModelMapper::selectById)
                .map(procedureStepModel -> procedureStepConfigMapper.selectComponentConfig(
                        procedureStepModel.getId(),
                        dto.getComponentId(),
                        procedureStepModel.getReusable(),
                        procedureStepModel.getProcessId(),
                        procedureStepModel.getProcessVersion()))
                .map(ProcedureStepConfig::getConfigInfo)
                .map(configStr -> JSONUtil.toBean(configStr, ProcedureStepConfigInfo.class))
                .map(ProcedureStepConfigInfo::getFormulaMaterialIds)
                .orElse(new ArrayList<>());
        List<Long> materialIds = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(formulaMaterialIds)){
            materialIds = productFormulaConfigureService.getFormulaMaterialListByIds(formulaMaterialIds)
                    .stream()
                    .map(ProductFormulaMaterial::getMaterialId)
                    .collect(Collectors.toList());
        }

        // 校验设备权限
        this.checkStationEquipmentRight(dto);
        List<String> storateMaterialNoList = dto.getStorateMaterialNoList();
        // 待投料列表
        List<String> waitingPendingList =
                ingredientInputRecordMapper.queryInputListByPlanId(ingredientPlan.getId(), materialIds).stream()
                        .filter(vo -> Objects.isNull(vo.getImporterId()))
                        .map(IngredientInputRecordVO::getStorageMaterialNo)
                        .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(waitingPendingList)) {
            throw new BmosException(MesResponseCode.INGREDIENT_INPUT_FINISHED);
        }
        List<IngredientInputRecord> inputList =
                ingredientInputRecordMapper.queryInputedListByPlanId(ingredientPlan.getId());
        if (!CollectionUtil.isEmpty(inputList)) {
            // 未称量的物料件号
            List<String> existStorateMaterialNoList = inputList.stream()
                    .map(IngredientInputRecord::getStorageMaterialNo)
                    .collect(Collectors.toList());
            if (CollectionUtil.containsAny(existStorateMaterialNoList, storateMaterialNoList)) {
                throw new BmosException(MesResponseCode.INGREDIENT_STORAGE_MATERIAL_INPUTED);
            }
        }
        List<StorageMaterial> storageMaterialList = storageMaterialMapper.queryListByNos(storateMaterialNoList);
        if (storageMaterialList.size() != storateMaterialNoList.size()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        Set<Long> storageBatchIdList = storageMaterialList.stream()
                .map(StorageMaterial::getStorageMaterialBatchId)
                .collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(storageBatchIdList)){
            List<StorageMaterialBatch> storageMaterialBatches = storageMaterialBatchMapper.selectBatchIds(storageBatchIdList);
            storageMaterialBatches.forEach(StorageMaterialBatch::availableValidate);
        }
        EquipmentInfoFeignVO device = FeignUtils.handleRequest(id -> equipmentConfigFeign.getConfigByEquipmentId(id),
                dto.getDeviceId()).getData();
        List<IngredientInputRecord> list = new ArrayList<>();
        for (StorageMaterial storageMaterial : storageMaterialList) {
            if (!storageMaterial.isAvailable()) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_AVAILABLE);
            }
            IngredientInputRecord record = new IngredientInputRecord();
            record.setIngredientPlanId(dto.getIngredientPlanId());
            record.setStorageMaterialBatchId(storageMaterial.getStorageMaterialBatchId());
            record.setStorageMaterialId(storageMaterial.getId());
            record.setStorageMaterialNo(storageMaterial.getNo());
            record.setQuantity(storageMaterial.getReserveQuantity());
            record.setUnitId(storageMaterial.getFinalUnitId());
            record.setDeviceId(device.getId());
            record.setDeviceName(device.getName());
            record.setDeviceCode(device.getCode());
            record.setImporterId(dto.getInputUserId());
            record.setRemark(dto.getRemark());
            record.setInputTime(LocalDateTime.now());
            record.setComponentInstanceId(componentInstance.getId());
            list.add(record);
        }
        if (CollectionUtil.isNotEmpty(list)) {
            ingredientInputRecordMapper.insertBatch(list);
        }
        // 保存物料日志
        if (CollectionUtil.isNotEmpty(list)) {
            this.saveMaterialLogs(dto,storageMaterialList);
        }
        // 更新物料键的数量
        if (CollectionUtil.isNotEmpty(list)) {
            this.inPutConsumeMaterial(storageMaterialList);
        }

        if (CollectionUtil.isNotEmpty(storageMaterialList)){
            // 保存配料投入的物料追溯记录
            materialTraceHistoryService.saveTraceHistory(storageMaterialList.stream()
                    .map(storageMaterial -> MaterialTraceHistoryDTO.builder()
                            .storageMaterialId(storageMaterial.getId())
                            .productPlanId(componentInstance.getProductPlanId())
                            .procedureStepModelId(componentInstance.getProcedureStepModelId())
                            .operateType(MaterialTraceOperateType.INGREDIENT_INPUT)
                            .quantity(unitCache.toExt(storageMaterial.getConsumeQuantity(), storageMaterial.getFinalUnitId()))
                            .unitId(storageMaterial.getFinalUnitId())
                            .build()).collect(Collectors.toList()));
        }

        // 解绑容器
        storageMaterialMapper.unbindContainersByIds(storageMaterialList.stream().map(StorageMaterial::getId).collect(Collectors.toList()));

        // 处理业务数据
        inputList.addAll(list);
        List<IngredientInputRecordDetail> recordList =
                ingredientInputRecordMapper.queryInputedDetailListByComponentInstanceId(componentInstance.getId());
        List<ExecuteFormData> results = generateExecuteFormData(dto, recordList, componentInstance);
        executeFormDataService.saveResultsAndHandleRelationComponentData(results, dto);
        // 更新组件实例
        if (componentInstance.getIngredientPlanId() == null) {
            componentInstance.setIngredientPlanId(ingredientPlan.getId());
            inputComponentInstanceMapper.updateById(componentInstance);
        }
    }

    private void checkStationEquipmentRight(IngredientInputDTO dto) {
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Plan plan = planService.getById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        if (CollectionUtil.isEmpty(configs)) {
            this.checkProductLineEquipmentRight(dto, plan.getProductionLineId());
            return;
        }
        Optional<BusinessComponentConfigDetailVO> configDetailVoOptional =
                configs.stream().filter(item -> item.getComponentId().equals(dto.getComponentId())).findFirst();
        if (!configDetailVoOptional.isPresent()) {
            this.checkProductLineEquipmentRight(dto, plan.getProductionLineId());
            return;
        }
        BusinessComponentConfigDetailVO configDetailVO = configDetailVoOptional.get();
        String configInfo = configDetailVO.getConfigInfo();
        JSONObject jsonObject = new JSONObject(configInfo);
        JSONArray stationPathIds = jsonObject.getJSONArray(ProcessConstant.stationPathField);
        if (CollUtil.isEmpty(stationPathIds)) {
            return;
        }
        List<String> pathList = stationPathIds.toList(String.class);
        Long productionLineId = plan.getProductionLineId();
        if (CollectionUtil.isNotEmpty(pathList)) {
            // todo 工位接口变化
            // 过滤出属于当前生产计划产线的工位配置
            List<Long> stationIds =
                    pathList.stream().filter(e -> e.startsWith(String.valueOf(productionLineId))).map(e -> {
                        return Long.valueOf(CollUtil.getLast(StrUtil.split(e, StrUtil.DASHED)));
                    }).collect(Collectors.toList());
            if (CollUtil.isEmpty(stationIds)) {
                return;
            }
            ResponseInfo<List<EquipmentInfoFeignVO>> byStationIdList =
                    equipmentConfigFeign.getConfigByStationIdList(stationIds);
            List<EquipmentInfoFeignVO> data = byStationIdList.getData();
            if (CollectionUtil.isEmpty(data)) {
                throw new BmosException(MesResponseCode.INPUT_EQUIPMENT_NOT_FOUND_ERROR);
            }
            if (data.stream().noneMatch(item -> item.getId().equals(dto.getDeviceId()))) {
                throw new BmosException(MesResponseCode.INPUT_EQUIPMENT_SCAN_ERROR);
            }
            // TODO 查看人员是否有工位权限
        }
        this.checkProductLineEquipmentRight(dto, plan.getProductionLineId());
    }

    private void checkProductLineEquipmentRight(IngredientInputDTO dto, Long productionLineId) {
        // 如果没有配置工位，查询产线下的设备
        ResponseInfo<List<EquipmentInfoFeignVO>> productionLine =
                equipmentConfigFeign.getConfigByProductionLineId(productionLineId);
        List<EquipmentInfoFeignVO> data = productionLine.getData();
        if (CollectionUtil.isEmpty(data)) {
            throw new BmosException(MesResponseCode.INPUT_EQUIPMENT_SCAN_PRODUCT_LINE_NOT_HAVE_EQUIPMENT_ERROR);
        }
        if (data.stream().noneMatch(item -> item.getId().equals(dto.getDeviceId()))) {
            throw new BmosException(MesResponseCode.INPUT_EQUIPMENT_SCAN_PRODUCT_LINE_NOT_BIND_EQUIPMENT_ERROR);
        }
    }

    /**
     * 投入消耗物料
     *
     * @param storageMaterialList 物料件集合
     */
    private void inPutConsumeMaterial(List<StorageMaterial> storageMaterialList) {
        storageMaterialList.forEach(StorageMaterial::consumeAllQuantity);
        storageMaterialMapper.updateBatch(storageMaterialList);
    }

    private void saveMaterialLogs(IngredientInputDTO dto, List<StorageMaterial> storageMaterialList) {
        Plan plan = planService.getById(dto.getProductPlanId());
        List<StorageMaterialPositionLogDTO> storageMaterialPositionLogDTOS = storageMaterialList.stream().map(item -> {
            StorageMaterialPositionLogDTO storageMaterialPositionLogDTO = new StorageMaterialPositionLogDTO();
            storageMaterialPositionLogDTO.setStorageMaterialId(item.getId());
            storageMaterialPositionLogDTO.setMaterialPositionId(item.getMaterialPositionId());
            storageMaterialPositionLogDTO.setOperateType(StorageOperateTypeEnum.BATCHING_INPUT);
            storageMaterialPositionLogDTO.setQuantity(unitCache.toExt(item.getInitQuantity(), item.getFinalUnitId()));
            storageMaterialPositionLogDTO.setUnitId(item.getFinalUnitId());
            storageMaterialPositionLogDTO.setSenderId(SysUserHolder.getUser().getUserId());
            storageMaterialPositionLogDTO.setProductName(plan.getProductName());
            storageMaterialPositionLogDTO.setProductCode(plan.getProductMergeCode());
            storageMaterialPositionLogDTO.setProductBatchNo(plan.getBatchNo());
            return storageMaterialPositionLogDTO;
        }).collect(Collectors.toList());
        storageMaterialPositionLogService.saveLogs(storageMaterialPositionLogDTOS);
    }

    private List<ExecuteFormData> generateExecuteFormData(IngredientInputDTO dto,
                                                          List<IngredientInputRecordDetail> inputList, IngredientInputComponentInstance instance) {
        ComponentListVO component = batchRecordComponentService.selectUsedComponentDetail(dto.getRecordVersionId(),
                dto.getRecordItemId(), dto.getComponentId());
        ProductionDetailInfo info = new ProductionDetailInfo();
        List<ExecuteFormData> results = new ArrayList<>();
        info.setDto(RecordComponentConvert.INSTANCE.convertToBusinessComponentBatchSaveDTO(dto));
        info.setIngredientInputRecordDetailList(inputList);
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfoByPlanId(dto.getProductPlanId());
        info.setFormulaInfo(formulaInfo);
        ingredientInputComponentStrategy.handleBusinessComponent(results, component, info, configMap, null);
        return sameFieldValueCheck(dto.getProductPlanId(), procedureStepModel, results,
                alwaysUpdateComponentType,
                instance.getReuse(),
                instance.getCopyVersion(),
                component);
    }

    @Override
    public StorageMaterialVO scanWeighMaterialCodeWithIngredientPlanId(ScanWeighMaterialCodeWithIngredientPlanId scanQuery) {
        IngredientPlan ingredientPlan = ingredientPlanMapper.selectById(scanQuery.getIngredientPlanId());
        if (ingredientPlan == null) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_NOT_EXIST);
        }
        // 按照物料件号搜索
        StorageMaterialDetailVO detail = storageMaterialService.queryByCodeAndValidate(StorageMaterialQueryValidateDTO.builder()
                .no(scanQuery.getNo())
                .productPlanId(ingredientPlan.getProductPlanId())
                .build()
                .validateAll());
        StorageMaterial storageMaterial = detail.getStorageMaterial();
        // 校验是否是待投入物料件
        validatePendingInput(scanQuery, storageMaterial);
        return storageMaterialService.queryInfoById(storageMaterial.getId());
    }

    private void validatePendingInput(ScanWeighMaterialCodeWithIngredientPlanId scanQuery, StorageMaterial storageMaterial) {
        IngredientInputComponentInstance componentInstance = inputComponentInstanceMapper.selectById(scanQuery.getComponentInstanceId());
        if (componentInstance == null) {
            throw new BmosException(MesResponseCode.INPUT_COMPONENT_INSTANCE_NOT_EXISTS);
        }
        // 查询组件上的配方物料
        List<Long> formulaMaterialIds = Optional.of(componentInstance)
                .map(IngredientInputComponentInstance::getProcedureStepModelId)
                .map(procedureStepModelMapper::selectById)
                .map(procedureStepModel -> procedureStepConfigMapper.selectComponentConfig(
                        procedureStepModel.getId(),
                        componentInstance.getComponentId(),
                        procedureStepModel.getReusable(),
                        procedureStepModel.getProcessId(),
                        procedureStepModel.getProcessVersion()))
                .map(ProcedureStepConfig::getConfigInfo)
                .map(configStr -> JSONUtil.toBean(configStr, ProcedureStepConfigInfo.class))
                .map(ProcedureStepConfigInfo::getFormulaMaterialIds)
                .orElse(new ArrayList<>());
        List<Long> materialIds = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(formulaMaterialIds)){
            materialIds = productFormulaConfigureService.getFormulaMaterialListByIds(formulaMaterialIds)
                    .stream()
                    .map(ProductFormulaMaterial::getMaterialId)
                    .collect(Collectors.toList());
        }

        List<IngredientInputRecordVO> inputList =
                ingredientInputRecordMapper.queryInputListByPlanId(scanQuery.getIngredientPlanId(), materialIds);
        Map<Long, IngredientInputRecordVO> map = CollectionUtils.convertMap(inputList,
                IngredientInputRecordVO::getStorageMaterialId);
        IngredientInputRecordVO inputRecord = map.get(storageMaterial.getId());
        // 校验是否为配料投入中待投入的物料件
        if (inputRecord == null) {
            throw new BmosException(MesResponseCode.PLEASE_SCAN_PENDING_MATERIAL);
        }
    }

    @Override
    public List<IngredientPlanItemVO> queryPendingInputPlanList(PendingInputPlanListQueryListDTO dto) {
        return ingredientPlanMapper.queryPendingInputPlanList(dto);
    }

    @Override
    public InputComponentInstanceVO getInputComponentInstance(InputComponentInstanceQueryDTO dto) {
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        UniqueComponentQueryDTO build = UniqueComponentQueryDTO.builder()
                .productPlanId(dto.getProductPlanId())
                .componentId(dto.getComponentId())
                .copyVersion(dto.getCopyVersion())
                .recordItemId(procedureStepModel.getRecordItemId())
                .recordVersionId(procedureStepModel.getRecordVersionId())
                .reuse(procedureStepModel.getReusable())
                .procedureStepModelId(procedureStepModel.getId())
                .build();
        IngredientInputComponentInstance instance = inputComponentInstanceMapper.selectUnique(build);
        InputComponentInstanceVO result = new InputComponentInstanceVO();
        if (instance == null) {
            IngredientInputComponentInstance componentInstance = new IngredientInputComponentInstance();
            componentInstance.setComponentId(dto.getComponentId());
            componentInstance.setProductPlanId(dto.getProductPlanId());
            componentInstance.setCopyVersion(dto.getCopyVersion());
            componentInstance.setProcedureStepModelId(dto.getProcedureStepModelId());
            componentInstance.setRecordItemId(procedureStepModel.getRecordItemId());
            componentInstance.setRecordVersionId(procedureStepModel.getRecordVersionId());
            componentInstance.setReuse(procedureStepModel.getReusable());
            inputComponentInstanceMapper.insert(componentInstance);
            result.setComponentInstanceId(componentInstance.getId());
            return result;
        }
        result.setComponentInstanceId(instance.getId());
        if (instance.getIngredientPlanId() != null) {
            result.setIngredientPlanId(instance.getIngredientPlanId());
            IngredientPlan ingredientPlan = ingredientPlanMapper.selectById(instance.getIngredientPlanId());
            if (ingredientPlan == null) {
                throw new BmosException(MesResponseCode.INGREDIENT_PLAN_NOT_EXIST);
            }
            result.setIngredientPlanName(ingredientPlan.getName());
        }
        return result;
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
    private List<ExecuteFormData> sameFieldValueCheck(Long productPlanId,
                                                      ProcedureStepModel procedureStepModel,
                                                      List<ExecuteFormData> results,
                                                      BusinessComponentTypeEnum[] alwaysUpdateComponentType,
                                                      Boolean reuse,
                                                      Long copyVersion,
                                                      ComponentListVO component) {
        RecordItemLatestDataQueryDTO queryDTO = getRecordItemLatestDataQueryDTO(productPlanId, procedureStepModel, reuse, copyVersion, component);
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

    private RecordItemLatestDataQueryDTO getRecordItemLatestDataQueryDTO(Long planId, ProcedureStepModel stepModel, Boolean reuse, Long copyVersion, ComponentListVO component) {
        List<Long> fieldIds = new ArrayList<>();
        recGetComponentFieldList(component, fieldIds);
        RecordItemLatestDataQueryDTO queryDTO = new RecordItemLatestDataQueryDTO();
        queryDTO.setReuse(reuse);
        queryDTO.setDiscard(false);
        queryDTO.setCopyVersion(copyVersion);
        queryDTO.setProductPlanId(planId);
        queryDTO.setProcedureStepId(stepModel.getProcedureStepId());
        queryDTO.setFieldIdList(fieldIds);
        queryDTO.setRecordItemId(stepModel.getRecordItemId());
        return queryDTO;
    }

    private void recGetComponentFieldList(ComponentListVO vo, List<Long> result){
        result.add(vo.getFieldId());
        if(CollUtil.isNotEmpty(vo.getChildren())){
            vo.getChildren().forEach(e->{
                recGetComponentFieldList(e, result);
            });
        }
    }
}
