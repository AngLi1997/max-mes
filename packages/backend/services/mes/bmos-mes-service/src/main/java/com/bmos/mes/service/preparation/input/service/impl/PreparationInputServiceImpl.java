package com.bmos.mes.service.preparation.input.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.audit.engine.core.utils.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.preparation.MeasureStatusEnum;
import com.bmos.mes.common.enums.preparation.PrepareInputStatusEnum;
import com.bmos.mes.common.enums.preparation.PrepareSignStatusEnum;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.component.BasicComponentConfig;
import com.bmos.mes.service.equipment.service.EquipmentCommonService;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationInputComponentInstanceVO;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationInputPlanVO;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationInputRecordVO;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationPlanItemVO;
import com.bmos.mes.service.preparation.input.convert.PreparationInputConvert;
import com.bmos.mes.service.preparation.input.mapper.PreparationInputComponentInstanceMapper;
import com.bmos.mes.service.preparation.input.mapper.PreparationInputRecordMapper;
import com.bmos.mes.service.preparation.input.model.PreparationInputComponentInstance;
import com.bmos.mes.service.preparation.input.model.PreparationInputRecord;
import com.bmos.mes.service.preparation.input.service.PreparationInputService;
import com.bmos.mes.service.preparation.input.service.dto.PreparationCompleteDTO;
import com.bmos.mes.service.preparation.input.service.dto.PreparationInputBindPlanDTO;
import com.bmos.mes.service.preparation.input.service.dto.PreparationInputComponentInstanceDTO;
import com.bmos.mes.service.preparation.input.service.dto.PreparationInputDTO;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureRecord;
import com.bmos.mes.service.preparation.measure.vo.LiquidPreparationDetailBatchVO;
import com.bmos.mes.service.preparation.plan.mapper.LiquidPreparationMaterialBatchMapper;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationPlan;
import com.bmos.mes.service.preparation.plan.repository.PreparationPlanRepository;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.record.business.model.preparation.PreparationInputDetailInfo;
import com.bmos.mes.service.record.business.strategy.PreparationInputComponentStrategy;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialQueryValidateDTO;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.model.StorageMaterialReserve;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialDetailVO;
import com.bmos.mes.service.tag.dto.ScanPreparationInputContainerDTO;
import com.bmos.mes.service.tag.dto.ScanPreparationInputMaterialDTO;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;
import com.bmos.mes.service.tag.vo.ScanInputMaterialVO;
import com.bmos.mes.service.trace.material.dto.MaterialTraceHistoryDTO;
import com.bmos.mes.service.trace.material.enums.MaterialTraceOperateType;
import com.bmos.mes.service.trace.material.service.IMaterialTraceHistoryService;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.unit.service.UnitCache;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PreparationInputServiceImpl implements PreparationInputService {

    @Autowired
    PreparationInputRecordMapper preparationInputRecordMapper;

    @Autowired
    PreparationInputComponentInstanceMapper preparationInputComponentInstanceMapper;

    @Autowired
    PreparationPlanRepository preparationPlanRepository;

    @Autowired
    PlanService planService;

    @Autowired
    ExecuteFormDataService executeFormDataService;

    @Autowired
    IStorageMaterialService storageMaterialService;

    @Autowired
    ProcedureStepModelService procedureStepModelService;

    @Autowired
    ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private BatchRecordComponentService recordComponentService;

    @Autowired
    private ProductMaterialService productMaterialService;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private FactoryFeign factoryFeign;

    @Autowired
    private EquipmentConfigFeign equipmentConfigFeign;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private PreparationInputComponentStrategy componentStrategy;

    @Autowired
    private IStorageMaterialBatchService storageMaterialBatchService;

    @Resource
    private IMaterialTraceHistoryService materialTraceHistoryService;

    @Resource
    private LiquidPreparationMaterialBatchMapper planBatchMapper;

    @Resource
    private EquipmentCommonService equipmentCommonService;

    @Override
    public PreparationInputComponentInstanceVO getInputComponentInstance(PreparationInputComponentInstanceDTO dto) {
        // 查询实例
        PreparationInputComponentInstance componentInstance = existInstanceThrowException(dto.getProductPlanId(), dto.getComponentId(), dto.getCopyVersion(),
                dto.getProcedureStepModelId(), dto.getReuse(), false);
        if (Objects.isNull(componentInstance)){
            return null;
        }
        // 查询配液单信息
        LiquidPreparationPlan liquidPreparationPlan = existPlanThrowException(componentInstance.getPreparationPlanId());
        PreparationInputComponentInstanceVO result = PreparationInputConvert.INSTANCE.convert2InstanceVO(componentInstance, liquidPreparationPlan);
        // 查询是否存在未完成量取的批次
        List<LiquidPreparationDetailBatchVO> batchList = planBatchMapper.selectMeasureInfoByPlanId(liquidPreparationPlan.getId());
        batchList = batchList.stream()
                .filter(e-> !Objects.equals(e.getMeasureStatus(), MeasureStatusEnum.COMPLETED))
                .collect(Collectors.toList());
        result.setHasUnmeasured(!batchList.isEmpty());
        return result;
    }

    @Override
    public List<PreparationPlanItemVO> queryPendingInputPlanList(Long productPlanId) {
        // 根据生产计划id查询当前生产计划下的配液单列表
        List<LiquidPreparationPlan> planList = preparationPlanRepository.selectInputPlanList(productPlanId);
        if (CollectionUtil.isEmpty(planList)){
            return Collections.emptyList();
        }
        // 配液单id
        List<Long> preparationPlanIdList = planList.stream().map(LiquidPreparationPlan::getId).collect(Collectors.toList());
        // 查询有投料记录的配液单
        List<PreparationInputRecord> recordList = preparationInputRecordMapper.selectByPlanIdList(preparationPlanIdList);
        if (CollectionUtil.isEmpty(recordList)){
            return PreparationInputConvert.INSTANCE.convert2PlanItemVOList(planList);
        }
        Set<Long> alreadyInputPlanIdList = recordList.stream().map(PreparationInputRecord::getPreparationPlanId).collect(Collectors.toSet());
        return PreparationInputConvert.INSTANCE.convert2PlanItemVOList(planList, alreadyInputPlanIdList);
    }

    @Override
    public Long bindPreparationPlan(PreparationInputBindPlanDTO dto) {
        PreparationInputComponentInstance componentInstance = existInstanceThrowException(dto.getProductPlanId(), dto.getComponentId(),
                dto.getCopyVersion(), dto.getProcedureStepModelId(), dto.getReuse(), false);
        if (Objects.nonNull(componentInstance)){
            // 若存在 则查询当前配液单是否有投料记录
            if (preparationInputRecordMapper.existByPlanId(componentInstance.getPreparationPlanId())){
                throw new BmosException(MesResponseCode.PREPARATION_PLAN_HAS_INPUT);
            }
        }
        // 进行配液单绑定
        return doBindPreparationPlan(dto, componentInstance);
    }

    @Override
    public PreparationInputPlanVO queryInputListByPlanId(Long componentInstanceId) {
        PreparationInputComponentInstance componentInstance = preparationInputComponentInstanceMapper.selectById(componentInstanceId);
        if (Objects.isNull(componentInstance)){
            throw new BmosException(MesResponseCode.PREPARATION_INPUT_NOT_BIND_PLAN);
        }
        LiquidPreparationPlan liquidPreparationPlan = existPlanThrowException(componentInstance.getPreparationPlanId());
        return doQueryInputListByPlanId(componentInstance, liquidPreparationPlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void input(PreparationInputDTO dto) {
        if (CollectionUtil.isEmpty(dto.getStorateMaterialNoList())){
            // 无需进行投料
            return ;
        }
        // 是否绑定配液单校验
        PreparationInputComponentInstance componentInstance = existInstanceThrowException(dto.getProductPlanId(), dto.getComponentId(),
                dto.getCopyVersion(), dto.getProcedureStepModelId(), dto.getReuse(), true);
        // 校验所投的物料是否为待投料
        LiquidPreparationPlan liquidPreparationPlan = existPlanThrowException(dto.getPreparationPlanId());
        PreparationInputPlanVO preparationInputPlanVO = this.doQueryInputListByPlanId(componentInstance, liquidPreparationPlan);
        if (CollectionUtil.isEmpty(preparationInputPlanVO.getInputList())){
            // 无需进行投料
            return ;
        }
        Set<String> needInputNoSet = new HashSet<>(dto.getStorateMaterialNoList());
        String changeStorageMaterialNo = preparationInputPlanVO.getInputList().stream()
                .filter(inputRecordVO -> needInputNoSet.contains(inputRecordVO.getStorageMaterialNo()) && !PrepareInputStatusEnum.PENDING.equals(inputRecordVO.getInputStatus()))
                .map(PreparationInputRecordVO::getStorageMaterialNo).collect(Collectors.joining(StrUtil.COMMA));
        if (StrUtil.isNotEmpty(changeStorageMaterialNo)){
            // 物料件已投料，请重新确认
            throw new BmosException(MesResponseCode.PREPARE_INPUT_MATERIAL_NO_ALREADY_INPUT, changeStorageMaterialNo);
        }
        doInput(componentInstance, preparationInputPlanVO, needInputNoSet, dto);
    }

    /**
     * 进行实际物料件投入
     * @param componentInstance
     * @param preparationInputPlanVO
     * @param needInputNoSet
     * @param dto
     */
    private void doInput(PreparationInputComponentInstance componentInstance,
                         PreparationInputPlanVO preparationInputPlanVO,
                         Set<String> needInputNoSet,
                         PreparationInputDTO dto) {
        // 1. 新增物料件投入记录
        // 筛选出需要投入的物料件信息
        List<PreparationInputRecordVO> inputRecordVOList = new ArrayList<>();
        List<PreparationInputRecordVO> alreadyInputRecordList = new ArrayList<>();
        for (PreparationInputRecordVO inputRecordVO : preparationInputPlanVO.getInputList()) {
            if (needInputNoSet.contains(inputRecordVO.getStorageMaterialNo())){
                inputRecordVOList.add(inputRecordVO);
            } else if (PrepareInputStatusEnum.FINISHED.equals(inputRecordVO.getInputStatus())) {
                alreadyInputRecordList.add(inputRecordVO);
            }
        }
        Integer sort = 0;
        if (CollUtil.isNotEmpty(alreadyInputRecordList)){
            alreadyInputRecordList.sort(Comparator.comparing(PreparationInputRecordVO::getSort));
            sort = alreadyInputRecordList.get(alreadyInputRecordList.size() - 1).getSort();
        }
        // 物料件解除绑定的设备
        List<Long> inputMaterialIdList = inputRecordVOList.stream().map(PreparationInputRecordVO::getStorageMaterialId).collect(Collectors.toList());
        storageMaterialService.unbindContainersByIds(inputMaterialIdList);
        // 获取投入的设备信息
        ResponseInfo<EquipmentInfoFeignVO> responseInfo = FeignUtils.handleRequest(
                data-> equipmentConfigFeign.getConfigByEquipmentId(data), dto.getDeviceId());
        EquipmentInfoFeignVO equipmentInfoFeignVO = responseInfo.getData();
        if (Objects.isNull(equipmentInfoFeignVO)){
            // 设备不存在
            throw new BmosException(MesResponseCode.EQUIPMENT_NOT_EXIST);
        }
        List<PreparationInputRecord> recordList = PreparationInputConvert.INSTANCE.convert2InputRecordVOList(inputRecordVOList, componentInstance,
                dto, equipmentInfoFeignVO, sort);
        if (CollUtil.isNotEmpty(recordList)){
            // 插入物料件投料记录
            preparationInputRecordMapper.insertBatch(recordList);
        }
        // 查询投料人名称
        ResponseInfo<Map<String, FeignUserVO>> userResponse = FeignUtils.handleRequest(
                data -> userFeign.getByUserIds(data), Lists.newArrayList(dto.getInputUserId()));
        Map<String, FeignUserVO> userMap = new HashMap<>();
        if (CollUtil.isNotEmpty(userResponse.getData())){
            userMap = userResponse.getData();
        }
        for (int i = 0; i < inputRecordVOList.size(); i++) {
            PreparationInputRecord preparationInputRecord = recordList.get(i);
            PreparationInputRecordVO inputRecordVO = inputRecordVOList.get(i);
            fillInputRecordVO(inputRecordVO, preparationInputRecord, userMap);
        }
        // 物料件：初始量和可用量不变，预定量减少，消耗量增加(因为是整件投料，所以直接消耗整件物料)
        Plan plan = planService.getById(componentInstance.getProductPlanId());
        storageMaterialService.consumeWholeMaterial(inputMaterialIdList, dto.getInputUserId(), plan, StorageOperateTypeEnum.PREPARATION_INPUT);

        // 保存配液投入的物料追溯记录
        materialTraceHistoryService.saveTraceHistory(inputRecordVOList.stream()
                .map(recordVO -> MaterialTraceHistoryDTO.builder()
                        .storageMaterialId(recordVO.getStorageMaterialId())
                        .productPlanId(componentInstance.getProductPlanId())
                        .procedureStepModelId(componentInstance.getProcedureStepModelId())
                        .operateType(MaterialTraceOperateType.PREPARATION_INPUT)
                        .quantity(recordVO.getQuantity())
                        .unitId(recordVO.getUnitId())
                        .build()).collect(Collectors.toList()));

        // 2 回填批记录
        generateBatchRecord(componentInstance, inputRecordVOList, alreadyInputRecordList, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void complete(PreparationCompleteDTO dto) {
        PreparationInputComponentInstance componentInstance = preparationInputComponentInstanceMapper.selectById(dto.getComponentInstanceId());
        if (Objects.isNull(componentInstance)){
            throw new BmosException(MesResponseCode.PREPARATION_INPUT_NOT_BIND_PLAN);
        }
        if (componentInstance.getComplete()){
            throw new BmosException(MesResponseCode.PREPARATION_INPUT_COMPLETE);
        }
        // 校验当前配液单下的物料件是否有除已投料/已失效的其他物料件
        LiquidPreparationPlan liquidPreparationPlan = existPlanThrowException(componentInstance.getPreparationPlanId());
        PreparationInputPlanVO preparationInputPlanVO = this.doQueryInputListByPlanId(componentInstance, liquidPreparationPlan);
        if (CollectionUtil.isEmpty(preparationInputPlanVO.getInputList())){
            return ;
        }
        String noMatchStorageMaterialNo = preparationInputPlanVO.getInputList().stream().filter(inputRecordVO -> !(PrepareInputStatusEnum.FINISHED.equals(inputRecordVO.getInputStatus()) || PrepareInputStatusEnum.SCRAPED.equals(inputRecordVO.getInputStatus())))
                .map(PreparationInputRecordVO::getStorageMaterialNo).collect(Collectors.joining(StrUtil.COMMA));
        if (preparationInputPlanVO.getInputList().stream().noneMatch(inputRecordVO -> (PrepareInputStatusEnum.FINISHED.equals(inputRecordVO.getInputStatus()) || PrepareInputStatusEnum.SCRAPED.equals(inputRecordVO.getInputStatus())))){
            // 存在未投料物料件
            throw new BmosException(MesResponseCode.PREPARATION_INPUT_NOT_FINISHED, noMatchStorageMaterialNo);
        }
        // 完成
        componentInstance.setComplete(true);
        preparationInputComponentInstanceMapper.updateById(componentInstance);
    }

    @Override
    public ScanDeviceVO scanPreparationInputContainer(ScanPreparationInputContainerDTO dto) {
        PreparationInputComponentInstance componentInstance = preparationInputComponentInstanceMapper.selectById(dto.getComponentInstanceId());
        if (Objects.isNull(componentInstance)){
            // 没有绑定配液单无法进行扫码
            throw new BmosException(MesResponseCode.PREPARATION_INPUT_NOT_BIND_PLAN);
        }
        // 查询设备信息
        ResponseInfo<EquipmentInfoFeignVO> responseInfo = FeignUtils.handleRequest(data-> equipmentConfigFeign.getEquipmentByEquipmentCodeWithoutPermission(data), dto.getCode());
        EquipmentInfoFeignVO equipmentInfoFeignVO = responseInfo.getData();
        if (Objects.isNull(equipmentInfoFeignVO)){
            // 设备不存在
            throw new BmosException(MesResponseCode.EQUIPMENT_NOT_EXIST);
        }
        // 校验当前容器是否绑定其他物料件
        this.validOtherStorage(equipmentInfoFeignVO);
        // 获取当前生产计划绑定的产线id
        Plan plan = planService.getById(componentInstance.getProductPlanId());
        Long productionLineId = plan.getProductionLineId();
        ResponseInfo<List<FactoryStationFeignVO>> lineStationResponse = FeignUtils.handleRequest(data -> factoryFeign.getStationInfoByLineId(data), productionLineId);
        Set<Long> stationIdSet = new HashSet<>();
        List<FactoryStationFeignVO> lineStations = lineStationResponse.getData();
        if (CollUtil.isNotEmpty(lineStations)){
            stationIdSet = lineStations.stream().map(FactoryStationFeignVO::getId).collect(Collectors.toSet());
        }
        // 判断当前设备所属工位是否在配置中
        String componentConfigJson = procedureStepConfigService.getComponentConfigJson(componentInstance.getProcedureStepModelId(), componentInstance.getComponentId(),
                componentInstance.getReuse(), dto.getProcessId(), dto.getProcessVersion());
        BasicComponentConfig basicComponentConfig = StrUtil.isNotEmpty(componentConfigJson)  ? JSON.parseObject(componentConfigJson, BasicComponentConfig.class) : null;
        if (Objects.nonNull(basicComponentConfig) && CollUtil.isNotEmpty(basicComponentConfig.getStation())){
            // 剔除当前配置的工位id不在生产批次对应的产线下
            stationIdSet = basicComponentConfig.getStation().stream().filter(stationIdSet::contains).collect(Collectors.toSet());
        }
        Boolean containFlg = false;
        if (CollUtil.isNotEmpty(equipmentInfoFeignVO.getStationIdList())){
            for (Long stationId :equipmentInfoFeignVO.getStationIdList()){
                if (stationIdSet.contains(stationId)){
                    containFlg = true;
                    break;
                }
            }
        }
        if (!containFlg){
            throw new BmosException(MesResponseCode.CANT_CHARGE_IN_THIS_DEVICE);
        }
        return PreparationInputConvert.INSTANCE.convert2ScanDeviceVO(equipmentInfoFeignVO);
    }

    @Override
    public ScanInputMaterialVO scanPreparationInputMaterial(ScanPreparationInputMaterialDTO dto) {
        PreparationInputComponentInstance componentInstance = preparationInputComponentInstanceMapper.selectById(dto.getComponentInstanceId());
        if (Objects.isNull(componentInstance)){
            // 没有绑定配液单无法进行扫码
            throw new BmosException(MesResponseCode.PREPARATION_INPUT_NOT_BIND_PLAN);
        }
        StorageMaterialDetailVO detail = storageMaterialService.queryByCodeAndValidate(StorageMaterialQueryValidateDTO.builder()
                .no(dto.getCode())
                .productPlanId(componentInstance.getProductPlanId())
                .build()
                .validateAll());
        StorageMaterial storageMaterial = detail.getStorageMaterial();
        // 校验物料件是否为配液列表中待投入状态的物料件
        List<LiquidPreparationMeasureRecord> list = preparationPlanRepository.selectLiquidMeasureRecordByPreparationId(componentInstance.getPreparationPlanId());
        boolean existed = list.stream().anyMatch(item -> Objects.equals(item.getStorageMaterialId(), storageMaterial.getId()));
        if (!existed) {
            throw new BmosException(MesResponseCode.PLEASE_SCAN_PENDING_MATERIAL);
        }
        return PreparationInputConvert.INSTANCE.convert2ScanInputMaterialVO(storageMaterial);
    }

    /**
     * 校验实例是否存在
     * @param planId 生产计划id
     * @param componentId 组件id
     * @param copyVersion 赋值版本
     * @param procedureStepModelId 步骤模型id
     * @param reuse 是否复用
     * @param exception 是否抛出异常
     * @return: 配液投入实例
     */
    private PreparationInputComponentInstance existInstanceThrowException(Long planId, Long componentId, Long copyVersion, Long procedureStepModelId,
                                                                          Boolean reuse, Boolean exception) {
        PreparationInputComponentInstance componentInstance = preparationInputComponentInstanceMapper.selectByComponentInfo(planId, componentId, copyVersion,
                procedureStepModelId, reuse);
        if (Objects.isNull(componentInstance)){
            if (exception){
                throw new BmosException(MesResponseCode.PREPARATION_INPUT_NOT_BIND_PLAN);
            }
        }else if (componentInstance.getComplete()){
            if (exception){
                // 代表配液投入已完成
                throw new BmosException(MesResponseCode.PREPARATION_INPUT_COMPLETE);
            }
            return componentInstance;
        }
        return componentInstance;
    }

    /**
     * 校验配液单是否存在
     * @param preparationPlanId 配液单id
     * @return 配液单
     */
    private LiquidPreparationPlan existPlanThrowException(Long preparationPlanId) {
        LiquidPreparationPlan liquidPreparationPlan = preparationPlanRepository.selectById(preparationPlanId);
        if (Objects.isNull(liquidPreparationPlan)){
            throw new BmosException(MesResponseCode.PREPARATION_PLAN_NOT_EXISTS);
        }
        return liquidPreparationPlan;
    }

    /**
     * 配液单绑定业务实现
     * @param dto
     * @param componentInstance
     * @return
     */
    private Long doBindPreparationPlan(PreparationInputBindPlanDTO dto, PreparationInputComponentInstance componentInstance) {
        if (Objects.isNull(componentInstance)){
            // 绑定配液单
            ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
            if (procedureStepModel == null) {
                // 异常情况 工序步骤不存在
                throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
            }
            componentInstance = PreparationInputConvert.INSTANCE.convert2ComponentInstance(dto, procedureStepModel);
            preparationInputComponentInstanceMapper.insert(componentInstance);
        } else {
            // 切换配液单
            componentInstance.setPreparationPlanId(dto.getPreparationPlanId());
            preparationInputComponentInstanceMapper.updateById(componentInstance);
        }
        return componentInstance.getId();
    }


    /**
     * 数据填充展示
     * 填充物料信息
     *    待投料：配液量取操作产生的物料件已签名，未投入时，默认展示为“待投料”，可以进行扫码确认；
     *    投料中：物料件在当前配液投入组件已确认（扫码），展示为“投料中”；
     *    已投料：物料件在当前配液投入组件投料成功后，展示为“已投料”；
     *    已失效：配液量取操作产生的物料件未在当前配液投入组件投入，其他功能使该物料件失效了，展示为“已失效”；
     *    未签名：配液量取操作产生的物料件未签名时，展示为“未签名”；
     * @param componentInstance
     * @param liquidPreparationPlan
     * @return
     */
    private PreparationInputPlanVO doQueryInputListByPlanId(PreparationInputComponentInstance componentInstance,
                                                            LiquidPreparationPlan liquidPreparationPlan) {
        // 根据配液单id查询配液单下的物料件
        List<LiquidPreparationMeasureRecord> liquidPreparationMeasureRecords = preparationPlanRepository.selectLiquidMeasureRecordByPreparationId(componentInstance.getPreparationPlanId());
        PreparationInputPlanVO preparationInputPlanVO = PreparationInputConvert.INSTANCE.convert2InputPlanVO(liquidPreparationPlan);
        List<PreparationInputRecordVO> recordVOList = new ArrayList<>();
        preparationInputPlanVO.setInputList(recordVOList);
        if (CollUtil.isEmpty(liquidPreparationMeasureRecords)){
            return preparationInputPlanVO;
        }
        // 根据物料件id查询投料记录
        Map<Long, LiquidPreparationMeasureRecord> preparationMeasureMap = liquidPreparationMeasureRecords.stream().collect(Collectors.toMap(LiquidPreparationMeasureRecord::getStorageMaterialId, Function.identity()));
        for (LiquidPreparationMeasureRecord measureRecord : preparationMeasureMap.values()) {
            PreparationInputRecordVO inputRecordVO =  PreparationInputConvert.INSTANCE.convert2InputRecordVO(measureRecord);
            recordVOList.add(inputRecordVO);
        }
        fillInputRecordVOList(recordVOList, componentInstance.getPreparationPlanId(), preparationMeasureMap);
        return preparationInputPlanVO;
    }

    private void fillInputRecordVOList(List<PreparationInputRecordVO> recordVOList, Long preparationPlanId, Map<Long, LiquidPreparationMeasureRecord> preparationMeasureMap) {
        List<PreparationInputRecord> preparationInputRecordList = preparationInputRecordMapper.selectByStorageMaterialIdList(preparationMeasureMap.keySet());
        Map<Long, PreparationInputRecord> preparationInputRecordMap = preparationInputRecordList.stream().collect(Collectors.toMap(PreparationInputRecord::getStorageMaterialId, Function.identity()));
        List<StorageMaterial> storageMaterialList = storageMaterialService.queryListByIds(preparationMeasureMap.keySet());
        Map<Long, StorageMaterial> storageMaterialMap = storageMaterialList.stream().collect(Collectors.toMap(StorageMaterial::getId, Function.identity()));
        Set<Long> materialIdList = preparationMeasureMap.values().stream().map(LiquidPreparationMeasureRecord::getMaterialId).collect(Collectors.toSet());
        List<ProductMaterial> materialList = productMaterialService.getByIds(materialIdList);
        Map<Long, ProductMaterial> materialMap = materialList.stream().collect(Collectors.toMap(ProductMaterial::getId, Function.identity()));
        Set<String> userIdList = preparationInputRecordList.stream().map(PreparationInputRecord::getImporterId)
                .filter(StrUtil::isNotEmpty).collect(Collectors.toSet());
        Map<String, FeignUserVO> userMap = new HashMap<>();
        ResponseInfo<Map<String, FeignUserVO>> responseInfo = FeignUtils.handleRequest(data -> userFeign.getByUserIds(userIdList), userIdList);
        if (CollUtil.isNotEmpty(responseInfo.getData())){
            userMap = responseInfo.getData();
        }
        for (int i = 0; i < recordVOList.size(); i++) {
            LiquidPreparationMeasureRecord measureRecord = preparationMeasureMap.get(recordVOList.get(i).getStorageMaterialId());
            StorageMaterial storageMaterial = storageMaterialMap.get(recordVOList.get(i).getStorageMaterialId());
            PreparationInputRecordVO inputRecordVO = recordVOList.get(i);
            PreparationInputRecord preparationInputRecord = preparationInputRecordMap.get(recordVOList.get(i).getStorageMaterialId());
            inputRecordVO.setUnit(Objects.requireNonNull(unitCache.getGlobalUnitName(measureRecord.getUnitId())));
            // 判断记录是否失效
            if (Objects.isNull(storageMaterial)){
                inputRecordVO.setInputStatus(PrepareInputStatusEnum.SCRAPED);
            }
            inputRecordVO.setStorageMaterialBatchId(storageMaterial.getStorageMaterialBatchId());
            inputRecordVO.setSpecification(materialMap.get(storageMaterial.getMaterialId()).getSpecification());
            inputRecordVO.setMaterialId(storageMaterial.getMaterialId());
            if (WeighSignStatus.UN_SIGNED.getValue().equals(measureRecord.getSignStatus().getValue())){
                // 未进行量取签名则为未签名
                inputRecordVO.setInputStatus(PrepareInputStatusEnum.NOT_SIGN);
                continue ;
            }
            // 物料批次过期
            StorageMaterialBatch storageMaterialBatch = storageMaterialBatchService.getById(storageMaterial.getStorageMaterialBatchId());

            if (Objects.nonNull(preparationInputRecord)){
                // 若有投入记录 若与组件绑定的配料单id相同则代表已投料 配料单不同则代表已失效
                if (!Objects.equals(preparationInputRecord.getPreparationPlanId(), preparationPlanId)){
                    // 代表已失效
                    inputRecordVO.setInputStatus(PrepareInputStatusEnum.SCRAPED);
                    continue ;
                }
                // 代表已投料
                inputRecordVO.setInputStatus(PrepareInputStatusEnum.FINISHED);
                fillInputRecordVO(inputRecordVO, preparationInputRecord, userMap);
            } else if (Objects.isNull(storageMaterialBatch) || !storageMaterialBatch.getAvailable() || !storageMaterial.isAvailable()){
                inputRecordVO.setInputStatus(PrepareInputStatusEnum.SCRAPED);
            }else {
                inputRecordVO.setInputStatus(PrepareInputStatusEnum.PENDING);
            }
        }
    }

    /**
     * 填充投入相关数据
     * @param inputRecordVO
     * @param preparationInputRecord
     */
    private void fillInputRecordVO(PreparationInputRecordVO inputRecordVO,
                                   PreparationInputRecord preparationInputRecord,
                                   Map<String, FeignUserVO> userFeignMap) {
        FeignUserVO feignUserVO = userFeignMap.get(preparationInputRecord.getImporterId());
        inputRecordVO.setInputTime(preparationInputRecord.getInputTime());
        inputRecordVO.setImporterId(preparationInputRecord.getImporterId());
        if (Objects.nonNull(feignUserVO)){
            inputRecordVO.setImporterName(feignUserVO.getUserName());
            inputRecordVO.setImportShowName(feignUserVO.getLoginName() + StrUtil.DASHED + feignUserVO.getUserName());
        }
        inputRecordVO.setDeviceId(preparationInputRecord.getDeviceId());
        inputRecordVO.setDeviceName(preparationInputRecord.getDeviceName());
        inputRecordVO.setDeviceCode(preparationInputRecord.getDeviceCode());
        inputRecordVO.setInputSignStatus(PrepareSignStatusEnum.getEnumByValue(preparationInputRecord.getSignStatus()));
        inputRecordVO.setSort(preparationInputRecord.getSort());
    }

    /**
     * 配液投入回填批记录
     * @param componentInstance 组件实例
     * @param inputRecordVOList 需要投料的记录
     * @param alreadyInputRecordList 曾经已经投料的记录
     */
    private void generateBatchRecord(PreparationInputComponentInstance componentInstance,
                                     List<PreparationInputRecordVO> inputRecordVOList,
                                     List<PreparationInputRecordVO> alreadyInputRecordList,
                                     PreparationInputDTO dto) {
        // 寻找组件
        BatchRecordComponent recordComponent = recordComponentService.getById(componentInstance.getComponentId());
        if (ObjectUtil.isNull(recordComponent)){
            throw new BmosException(MesResponseCode.COMPONENT_NOT_EXIST);
        }
        // 查询当前componentId
        ComponentListVO componentListVO =
                recordComponentService.selectUsedComponentDetail(componentInstance.getRecordVersionId(),
                        componentInstance.getRecordItemId(),
                        recordComponent.getId());
        if (ObjectUtil.isNull(componentListVO)){
            throw new BmosException(MesResponseCode.COMPONENT_NOT_EXIST);
        }
        PreparationInputDetailInfo detailInfo = new PreparationInputDetailInfo();
        detailInfo.setDto(PreparationInputConvert.INSTANCE.convert2BaseDTO(dto));
        detailInfo.setPreInputStorageMaterialList(PreparationInputConvert.INSTANCE.convert2StorageMaterialInfoList(alreadyInputRecordList));
        detailInfo.setCurrentInputStoratageMaterialList(PreparationInputConvert.INSTANCE.convert2StorageMaterialInfoList(inputRecordVOList));
        ProcedureStepModel stepModel = procedureStepModelService.getById(componentInstance.getProcedureStepModelId());
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(stepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        List<ExecuteFormData> executeFormDataList = new ArrayList<>();
        componentStrategy.handleBusinessComponent(executeFormDataList, componentListVO,
                detailInfo, configMap, null);
        if (CollectionUtil.isEmpty(executeFormDataList)){
            return ;
        }
        executeFormDataService.saveResultsAndHandleRelationComponentData(executeFormDataList, dto);
    }

    /**
     * 校验容器是否绑定其他物料件下/容器是否可用
     * @param equipmentInfoFeignVO
     */
    private void validOtherStorage(EquipmentInfoFeignVO equipmentInfoFeignVO) {
        // 设备是否可用
        if (!EquipmentStatusCodeEnum.AVAILABLE.getCode().equals(equipmentInfoFeignVO.getStatus())){
            throw new BmosException(MesResponseCode.INPUT_EQUIPMENT_STATUS_UNAVAILABLE);
        }
        // 校验容器是否在其他物料件下
//        StorageMaterial storageMaterial = storageMaterialService.getByContainerId(equipmentInfoFeignVO.getId());
//        if (Objects.nonNull(storageMaterial)){
//            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_CONTAINER_EXIST, equipmentInfoFeignVO.getCode(), storageMaterial.getNo());
//        }
    }
}
