package com.bmos.mes.service.weigh.centre.input.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.ingredient.WeighInputStatus;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.components.BusinessComponentManager;
import com.bmos.mes.service.components.comps.MaterialInputComponentsFromDataOPT;
import com.bmos.mes.service.components.dto.FormDataOPT;
import com.bmos.mes.service.components.mapper.BusinessComponentInstanceMapper;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialReserveMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.model.StorageMaterialReserve;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialVO;
import com.bmos.mes.service.tag.dto.ScanWeighMaterialCodeWithMaterialWeighComponentId;
import com.bmos.mes.service.trace.material.dto.MaterialTraceHistoryDTO;
import com.bmos.mes.service.trace.material.enums.MaterialTraceOperateType;
import com.bmos.mes.service.trace.material.service.IMaterialTraceHistoryService;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mes.service.weigh.centre.input.dto.WeighInputDTO;
import com.bmos.mes.service.weigh.centre.input.mapper.IWeighInputRecordMapper;
import com.bmos.mes.service.weigh.centre.input.model.WeighInputRecord;
import com.bmos.mes.service.weigh.centre.input.service.IWeighInputService;
import com.bmos.mes.service.weigh.centre.input.vo.WeighInputRecordResultVO;
import com.bmos.mes.service.weigh.centre.input.vo.WeighInputRecordVO;
import com.bmos.mes.service.weigh.centre.requirement.dto.MaterialInputComponentConfig;
import com.bmos.mes.service.weigh.centre.requirement.mapper.IWeighInputProcessMapper;
import com.bmos.mes.service.weigh.centre.requirement.mapper.IWeighRequirementMapper;
import com.bmos.mes.service.weigh.centre.requirement.model.WeighInputProcess;
import com.bmos.mes.service.weigh.centre.requirement.model.WeighRequirement;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.facade.equipment.dto.EquipmentQueryDTO;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bmos.mes.common.enums.storage.StorageOperateTypeEnum.MATERIAL_INPUT;

/**
 * 物料投入接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/18 16:01
 */
@Service
public class WeighInputServiceImpl implements IWeighInputService {

    private static final String LOG_PREFIX = "[物料投入]";

    @Resource
    private BusinessComponentManager componentManager;

    @Resource
    private IWeighInputRecordMapper weighInputRecordMapper;

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private FactoryFeign factoryFeign;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private BusinessComponentInstanceMapper businessComponentInstanceMapper;

    @Resource
    private IStorageMaterialReserveMapper iStorageMaterialReserveMapper;

    @Resource
    private IStorageMaterialPositionLogService storageMaterialPositionLogService;

    @Resource
    private IWeighRequirementMapper weighRequirementMapper;

    @Resource
    private IWeighInputProcessMapper weighInputProcessMapper;

    @Resource
    private IStorageMaterialBatchService storageMaterialBatchService;

    @Resource
    private IMaterialTraceHistoryService materialTraceHistoryService;
    @Autowired
    private UnitCache unitCache;

    @Override
    public WeighInputRecordResultVO getInputList(Long componentInstanceId) {
        BusinessComponentInstance componentInstance = componentManager.getComponentInstanceById(componentInstanceId);
        if (componentInstance == null) {
            return null;
        }
        WeighInputRecordResultVO result = new WeighInputRecordResultVO();
        WeighInputProcess weighInputProcess = weighInputProcessMapper.getWeighProcessByComponentInstanceId(componentInstanceId);
        List<Long> requirementIds = weighRequirementMapper.selectRequirementIdListByProcedureStepConfigId(componentInstance.getProcedureStepConfigId());
        List<WeighInputRecordVO> inputList = weighInputRecordMapper.getInputList(componentInstance.getProductPlanId(), requirementIds);
        if (CollectionUtil.isEmpty(inputList)) {
            result.setCanFinished(false);
            result.setFinished(true);
            result.setList(new ArrayList<>());
            return result;
        }

        Map<String, StorageMaterial> storageMaterialMap = storageMaterialService.queryListByNos(inputList.stream().map(WeighInputRecordVO::getStorageMaterialNo).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(item -> item.getNo(), Function.identity(), (v1, v2) -> v1));

        inputList.forEach(item -> {
            StorageMaterial storageMaterial = storageMaterialMap.get(item.getStorageMaterialNo());
            WeighSignStatus weighSignStatus = getWeighSignStatus(item, storageMaterial);
            if (Objects.equals(weighSignStatus, WeighSignStatus.UN_SIGNED)) {
                item.setWeighInputStatus(WeighInputStatus.UN_SIGNED);
            } else if (Objects.equals(weighSignStatus, WeighSignStatus.SCRAPED)) {
                item.setWeighInputStatus(WeighInputStatus.SCRAPED);
            } else {
                if (item.getInputComponentInstanceId() == null) {
                    item.setWeighInputStatus(WeighInputStatus.PENDING);
                    item.setQuantity(PrecisionHelper.precision(unitCache.toExt(item.getAvailableQuantity().add(item.getReserveQuantity()), item.getUnitId()), item.getUnitId()));
                } else if (Objects.equals(item.getInputComponentInstanceId(), componentInstance.getId())) {
                    item.setWeighInputStatus(WeighInputStatus.FINISHED);
                } else {
                    item.setWeighInputStatus(WeighInputStatus.SCRAPED);
                }
            }
        });

        List<WeighRequirement> requirements = weighRequirementMapper.selectListByComponentInstanceId(componentInstance);

        List<WeighRequirement> processsingRequirementList = requirements.stream().filter(item ->
                        Objects.equals(item.getRequirementStatus(), RequirementStatusEnum.UN_PLANNED)
                        || Objects.equals(item.getRequirementStatus(), RequirementStatusEnum.UN_WEIGHED)
                        || Objects.equals(item.getRequirementStatus(), RequirementStatusEnum.WEIGHING))
                .collect(Collectors.toList());
        result.setList(weighInputProcess == null ? new ArrayList<>() : inputList);
        List<WeighInputRecordVO> processingInputList = inputList.stream()
                .filter(item -> Objects.equals(item.getWeighInputStatus(), WeighInputStatus.PENDING)
                        || Objects.equals(item.getWeighInputStatus(), WeighInputStatus.UN_SIGNED)
                        || Objects.equals(item.getWeighInputStatus(), WeighInputStatus.PROCESSING))
                .collect(Collectors.toList());

        result.setCanFinished(CollectionUtil.isEmpty(processsingRequirementList) && CollectionUtil.isEmpty(processingInputList));
        result.setFinished(weighInputProcess == null || weighInputProcess.getFinished());
        return result;
    }

    @Nullable
    private static WeighSignStatus getWeighSignStatus(WeighInputRecordVO item, StorageMaterial storageMaterial) {
        WeighSignStatus weighSignStatus;
        if (storageMaterial == null){
            weighSignStatus = WeighSignStatus.UN_SIGNED;
        }else if (Objects.equals(storageMaterial.getSignStatus(), WeighSignStatus.UN_SIGNED)){
            weighSignStatus = WeighSignStatus.UN_SIGNED;
        }else if (storageMaterial.getAvailableQuantity().equals(BigDecimal.ZERO)
                && storageMaterial.getReserveQuantity().equals(BigDecimal.ZERO)
                && item.getInputTime() == null){
            weighSignStatus = WeighSignStatus.SCRAPED;
        }else {
            weighSignStatus = storageMaterial.getSignStatus();
        }
        return weighSignStatus;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void input(WeighInputDTO dto) {

        BusinessComponentInstance componentInstance = componentManager.getComponentInstanceById(dto.getComponentInstanceId());
        if (componentInstance == null) {
            throw new RuntimeException(LOG_PREFIX + "组件实例不存在");
        }

        Plan plan = planMapper.selectById(componentInstance.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }

        // 组件配置
        MaterialInputComponentConfig config = JSONUtil.toBean(componentInstance.getComponentConfigJson(), MaterialInputComponentConfig.class);

        // 工位
        List<String> station = config.getStation();

        // 校验设备权限
        EquipmentInfoFeignVO device = validateEquipment(dto.getDeviceId(), station, componentInstance.getProductPlanId());

        List<Long> requirementIds = weighRequirementMapper.selectRequirementIdListByProcedureStepConfigId(componentInstance.getProcedureStepConfigId());

        // 投料列表
        List<WeighInputRecordVO> inputList = weighInputRecordMapper.getInputList(componentInstance.getProductPlanId(), requirementIds);
        if (!CollectionUtil.containsAll(inputList.stream().map(WeighInputRecordVO::getStorageMaterialNo).collect(Collectors.toList()), dto.getStorateMaterialNoList())) {
            // 参数中存在投料列表外的物料件不在本批需求中
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_STORAGE_MATERIAL_NOT_IN_REQUIREMENT);
        }
        // 待投料列表
        List<WeighInputRecordVO> pending = inputList.stream()
                .filter(item -> item.getInputComponentInstanceId() == null)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(pending)) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_MATERIAL_INPUT_FINISHED);
        }
        List<String> pendingNoList = pending.stream().map(WeighInputRecordVO::getStorageMaterialNo).collect(Collectors.toList());
        if (!CollectionUtil.containsAll(pendingNoList, dto.getStorateMaterialNoList())) {
            // 参数中存在不在未投料列表中的物料件 已投料或已失效
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_STORAGE_MATERIAL_INPUTED);
        }

        // 投入的物料件
        List<StorageMaterial> storageMaterialList = storageMaterialService.queryListByNos(dto.getStorateMaterialNoList());
        if (storageMaterialList.size() != dto.getStorateMaterialNoList().size()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }

        storageMaterialList.stream().filter(item -> !item.isAvailable()).findAny().ifPresent(item -> {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_AVAILABLE);
        });

        Map<Long, StorageMaterial> materialMap = storageMaterialList.stream()
                .collect(Collectors.toMap(BaseDO::getId, Function.identity(), (v1, v2) -> v1));

        List<WeighInputRecordVO> pendingRecordVOS = pending.stream().filter(item -> dto.getStorateMaterialNoList().contains(item.getStorageMaterialNo())).collect(Collectors.toList());

        // 保存投料记录
        List<WeighInputRecord> weighInputRecords = weighInputRecordMapper.selectBatchIds(pendingRecordVOS.stream().map(WeighInputRecordVO::getId).collect(Collectors.toList()));
        List<StorageMaterialPositionLogDTO> logDTOS = new ArrayList<>();
        for (WeighInputRecord weighInputRecord : weighInputRecords) {
            StorageMaterial storageMaterial = materialMap.get(weighInputRecord.getStorageMaterialId());
            weighInputRecord.setInputTime(LocalDateTime.now());
            weighInputRecord.setInputUserId(dto.getInputUserId());
            weighInputRecord.setInputUserName(UserUtils.getUsername(dto.getInputUserId()));
            weighInputRecord.setInputComponentInstanceId(componentInstance.getId());
            weighInputRecord.setDeviceId(device.getId());
            weighInputRecord.setDeviceName(device.getName());
            weighInputRecord.setDeviceCode(device.getCode());
            weighInputRecord.setQuantity(PrecisionHelper.precision(unitCache.toExt(storageMaterial.getAvailableQuantity().add(storageMaterial.getReserveQuantity()), weighInputRecord.getUnitId()), weighInputRecord.getUnitId()));
            StorageMaterialPositionLogDTO logDTO = StorageMaterialPositionLogDTO.builder()
                    .storageMaterialId(weighInputRecord.getStorageMaterialId())
                    .operateType(MATERIAL_INPUT)
                    .quantity(weighInputRecord.getQuantity())
                    .unitId(weighInputRecord.getUnitId())
                    .senderId(weighInputRecord.getInputUserId())
                    .receiverId(weighInputRecord.getInputUserId())
                    .productId(plan.getProductId())
                    .productBatchNo(plan.getBatchNo())
                    .productCode(plan.getProductMergeCode())
                    .productName(plan.getProductName())
                    .materialPositionId(Optional.ofNullable(weighInputRecord.getStorageMaterialId())
                            .map(materialMap::get)
                            .map(StorageMaterial::getMaterialPositionId)
                            .orElse(null))
                    .build();
            logDTOS.add(logDTO);
        }
        if (CollectionUtil.isNotEmpty(weighInputRecords)) {
            weighInputRecordMapper.updateBatch(weighInputRecords);
        }

        if (CollectionUtil.isNotEmpty(logDTOS)) {
            // 保存物料投入日志
            storageMaterialPositionLogService.saveLogs(logDTOS);
        }

        // 消耗
        for (StorageMaterial storageMaterial : storageMaterialList) {
            storageMaterial.consumeAllQuantity();
        }
        storageMaterialService.updateBatch(storageMaterialList);

        // 解绑容器
        storageMaterialService.unbindContainersByIds(storageMaterialList.stream()
                .map(StorageMaterial::getId)
                .collect(Collectors.toList())
        );

        // 保存物料投入的物料追溯记录
        materialTraceHistoryService.saveTraceHistory(weighInputRecords.stream()
                .map(recordVO -> MaterialTraceHistoryDTO.builder()
                        .storageMaterialId(recordVO.getStorageMaterialId())
                        .productPlanId(componentInstance.getProductPlanId())
                        .procedureStepModelId(componentInstance.getProcedureStepModelId())
                        .operateType(MaterialTraceOperateType.MATERIAL_INPUT)
                        .quantity(recordVO.getQuantity())
                        .unitId(recordVO.getUnitId())
                        .build()).collect(Collectors.toList()));

        // 更新批记录
        List<FormDataOPT> list = componentManager.getFormDataOPTList(componentInstance.getId());
        List<MaterialInputComponentsFromDataOPT> views = weighInputRecordMapper.getComponentsViewList(componentInstance.getProductPlanId(), requirementIds);
        componentManager.fillFormDataOPT(views, list);
        componentManager.saveFormDataOPT(list, componentInstance);
    }

    @Override
    public StorageMaterialVO scanWeighMaterialCodeWithMaterialWeighComponentId(ScanWeighMaterialCodeWithMaterialWeighComponentId scanQuery) {

        // 按照物料件号搜索
        StorageMaterial storageMaterial = storageMaterialService.queryByMaterialNo(scanQuery.getNo(), null);
        if (storageMaterial == null) {
            // 按照容器编号搜索
            storageMaterial = storageMaterialService.queryByContainerNo(scanQuery.getNo());
        }
        if (storageMaterial == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        // 校验物料件是否生效
        if (!storageMaterial.isAvailable()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_AVAILABLE);
        }
        StorageMaterialBatch batch = storageMaterialBatchService.getById(storageMaterial.getStorageMaterialBatchId());
        if (batch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        batch.availableValidate();
        BusinessComponentInstance componentInstance = businessComponentInstanceMapper.selectById(scanQuery.getComponentInstanceId());
        if (componentInstance == null) {
            throw new BmosException(MesResponseCode.COMPONENT_NOT_EXIST);
        }
        WeighInputRecordResultVO result = getInputList(componentInstance.getId());
        if (result == null) {
            return null;
        }
        List<WeighInputRecordVO> inputList = result.getList();
        Map<String, WeighInputRecordVO> map = CollectionUtils.convertMap(inputList,
                WeighInputRecordVO::getStorageMaterialNo);
        WeighInputRecordVO inputRecord = map.get(storageMaterial.getNo());
        // 校验是否为配料投入中待投入的物料件
        if (inputRecord == null) {
            throw new BmosException(MesResponseCode.PLEASE_SCAN_PENDING_MATERIAL);
        }
        // 校验添加的物料件已预定到当前生产批次
        StorageMaterialReserve reserve = iStorageMaterialReserveMapper.queryByStorageMaterialId(storageMaterial
                .getId());
        if (reserve == null || !Objects.equals(reserve.getBatchId(), componentInstance.getProductPlanId())) {
            throw new BmosException(MesResponseCode.SCAN_RESERVE_TAG_ERROR);
        }
        // 校验是否出暂存间
        if (storageMaterial.getMaterialPositionId() != null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_OUTBOUND);
        }
        return storageMaterialService.queryInfoById(storageMaterial.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishInput(Long componentInstanceId) {
        BusinessComponentInstance componentInstance = businessComponentInstanceMapper.selectById(componentInstanceId);
        if (componentInstance == null) {
            throw new BmosException(MesResponseCode.COMPONENT_NOT_EXIST);
        }
        WeighInputProcess weighInputProcess = weighInputProcessMapper.getWeighProcessByComponentInstanceId(componentInstanceId);
        if (weighInputProcess == null) {
            return;
        }
        if (weighInputProcess.getFinished()) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_KEY_FINISHED);
        }
        weighInputProcess.setFinished(true);
        weighInputProcessMapper.updateById(weighInputProcess);
    }

    /**
     * 校验设备权限
     *
     * @param equipmentId   设备id
     * @param station       工位id
     * @param productPlanId 产品计划id
     * @return 设备信息
     */
    private EquipmentInfoFeignVO validateEquipment(Long equipmentId, List<String> station, Long productPlanId) {

        // 投入的设备
        EquipmentInfoFeignVO device = FeignUtils.handleRequest(id -> equipmentConfigFeign.getConfigByEquipmentId(id), equipmentId).getData();
        if (device == null) {
            throw new BmosException(MesResponseCode.EQUIPMENT_INFO_COMPONENT_EQUIPMENT_NOT_EXITS_ERROR);
        }

        if (CollectionUtil.isEmpty(station)) {
            // 组件上没配置工位 根据产线查询
            Plan plan = planMapper.selectById(productPlanId);
            if (plan == null) {
                throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
            }
            // 查询产线下所有的工位
            List<FactoryStationFeignVO> data = FeignUtils.handleRequest(lineId -> factoryFeign.getStationInfoByLineId(lineId), plan.getProductionLineId()).getData();
            if (CollectionUtil.isNotEmpty(data)) {
                station = data.stream()
                        .map(FactoryStationFeignVO::getId)
                        .map(Object::toString)
                        .collect(Collectors.toList());
            }
            if (CollectionUtil.isEmpty(station)) {
                throw new BmosException(MesResponseCode.CANT_CHARGE_IN_THIS_DEVICE);
            }
            EquipmentQueryDTO equipmentQuery = new EquipmentQueryDTO();
            equipmentQuery.setStationIdList(station.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList()));
            // 所有有权限的设备
            List<EquipmentInfoFeignVO> equipments = FeignUtils.handleRequest(q -> equipmentConfigFeign.getConfigByStationIdList(q), equipmentQuery).getData();
            // 没有有权限的设备
            if (CollectionUtil.isEmpty(equipments) || !equipments.stream()
                    .map(EquipmentInfoFeignVO::getId)
                    .collect(Collectors.toList())
                    .contains(equipmentId)) {
                throw new BmosException(MesResponseCode.CANT_CHARGE_IN_THIS_DEVICE);
            }
        }
        return device;
    }
}
