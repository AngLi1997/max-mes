package com.bmos.mes.service.preparation.produce.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.audit.engine.core.utils.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.ingredient.WeighMode;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.preparation.PrepareSignStatusEnum;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.component.BasicComponentConfig;
import com.bmos.mes.common.model.component.CustomFieldDetailInfo;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataHandleService;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationPlan;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationPlanConfig;
import com.bmos.mes.service.preparation.plan.repository.PreparationPlanRepository;
import com.bmos.mes.service.preparation.produce.controller.vo.*;
import com.bmos.mes.service.preparation.produce.convert.PreparationProduceConverter;
import com.bmos.mes.service.preparation.produce.mapper.PreparationProduceProgressMapper;
import com.bmos.mes.service.preparation.produce.mapper.PreparationProduceRecordMapper;
import com.bmos.mes.service.preparation.produce.model.PreparationProduceProgress;
import com.bmos.mes.service.preparation.produce.model.PreparationProduceRecord;
import com.bmos.mes.service.preparation.produce.service.PreparationProduceService;
import com.bmos.mes.service.preparation.produce.service.dto.*;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.record.business.model.preparation.PreparationProduceDetailInfo;
import com.bmos.mes.service.record.business.strategy.PreparationProduceComponentStrategy;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.service.ICargoPositionService;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialReserveDTO;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.service.MaterialBatchFieldService;
import com.bmos.mes.service.tag.vo.ScanCargoPositionVO;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;
import com.bmos.mes.service.trace.material.dto.MaterialTraceHistoryDTO;
import com.bmos.mes.service.trace.material.enums.MaterialTraceOperateType;
import com.bmos.mes.service.trace.material.service.IMaterialTraceHistoryService;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.facade.equipment.enums.EquipmentTagCodeEnum;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.TagFeignVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import com.bmos.platform.facade.system.role.feign.RoleFeign;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.unit.service.UnitCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PreparationProduceServiceImpl implements PreparationProduceService {

    @Autowired
    PreparationProduceProgressMapper preparationProduceProgressMapper;

    @Autowired
    PreparationProduceRecordMapper preparationProduceRecordMapper;

    @Autowired
    PreparationPlanRepository preparationPlanRepository;

    @Autowired
    IStorageMaterialBatchService storageMaterialBatchService;

    @Autowired
    ProductFormulaConfigureService formulaConfigureService;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private PlanService planService;

    @Autowired
    private ProductMaterialService productMaterialService;

    @Autowired
    ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private RoleFeign roleFeign;

    @Autowired
    private FactoryFeign factoryFeign;

    @Autowired
    private ICargoPositionService cargoPositionService;

    @Autowired
    private EquipmentConfigFeign equipmentConfigFeign;

    @Autowired
    private IStorageMaterialService storageMaterialService;

    @Autowired
    private IStorageMaterialPositionLogService storageMaterialPositionLogService;

    @Autowired
    private BatchRecordComponentService recordComponentService;

    @Autowired
    PreparationProduceComponentStrategy componentStrategy;

    @Autowired
    ExecuteFormDataService executeFormDataService;

    @Autowired
    MaterialBatchFieldService materialBatchFieldService;
    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Resource
    private IMaterialTraceHistoryService materialTraceHistoryService;

    @Resource
    private ExecuteFormDataHandleService executeFormDataHandleService;

    @Override
    public PreparationProduceProgressVO getPreparationProduceProgress(PreparationProduceProgressDTO dto) {
        PreparationProduceProgress preparationProduceProgress = preparationProduceProgressMapper.selectByComponentInfo(dto.getProductPlanId(), dto.getComponentId(), dto.getProcedureStepModelId(),
                dto.getCopyVersion(), dto.getReuse());
        if (Objects.isNull(preparationProduceProgress)){
            return null;
        }
        // 根据配液单id查询配液单信息
        LiquidPreparationPlan preparationPlan = preparationPlanRepository.selectById(preparationProduceProgress.getPreparationPlanId());
        if (Objects.isNull(preparationPlan)){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_COMPONENT_PLAN_ALREADY_DELETE);
        }
        ProductFormulaMaterial formulaMaterial = null;
        if (Objects.nonNull(preparationProduceProgress.getFormulaMaterialId())){
            formulaMaterial = formulaConfigureService.getFormulaMaterialById(preparationProduceProgress.getFormulaMaterialId());
        }
        // 查询物料批次信息以及物料信息
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchService.queryMaterialBatchByNoAndMaterialId(formulaMaterial.getMaterialId(), preparationProduceProgress.getMaterialBatchNo());
        // 封装返回值
        return assemblyPreparationProduceProgressVO(preparationProduceProgress, preparationPlan, storageMaterialBatch, formulaMaterial);
    }

    @Override
    public List<PreparationProducePlanVO> getProducePlanList(Long productPlanId) {
        // 根据生产批次id查询配液单信息
        List<LiquidPreparationPlan> preparationPlanList = preparationPlanRepository.selectInputPlanList(productPlanId);
        if (CollUtil.isEmpty(preparationPlanList)){
            return Collections.emptyList();
        }
        return PreparationProduceConverter.INSTANCE.convert2ProducePlanVOList(preparationPlanList);
    }

    @Override
    public PreparationProduceMaterialVO  queryMaterial(Long preparationPlanId) {
        LiquidPreparationPlan preparationPlan = preparationPlanRepository.selectById(preparationPlanId);
        if (Objects.isNull(preparationPlan)){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_COMPONENT_PLAN_ALREADY_DELETE);
        }
        LiquidPreparationPlanConfig planConfig = JSON.parseObject(preparationPlan.getConfigJson(), LiquidPreparationPlanConfig.class);
        Long formulaMaterialId = planConfig.getFormulaMaterialId();
        if (Objects.isNull(formulaMaterialId)){
            return null;
        }
        return assemblyProduceMaterialVO(preparationPlan, formulaMaterialId);

    }

    @Override
    public PreparationProduceMaterialBatchVO queryMaterialBatch(PreparationMaterialBatchDTO dto) {
        // 根据物料批次编号查询物料批次信息
        ProductFormulaMaterial formulaMaterial = formulaConfigureService.getFormulaMaterialById(dto.getFormulaMaterialId());
        if (Objects.isNull(formulaMaterial)){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_COMPONENT_FORMULA_MATERIAL_DELETE);
        }
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchService.queryMaterialBatchByNoAndMaterialId(formulaMaterial.getMaterialId(),
                dto.getMaterialBatchNo());
        LocalDate expireDate = null;
        PreparationProduceMaterialBatchVO produceMaterialBatchVO = null;
        if (Objects.isNull(storageMaterialBatch)){
            // 查询中间品信息
            ProductMaterial productMaterial = productMaterialService.selectById(formulaMaterial.getMaterialId());
            if (Objects.nonNull(productMaterial.getExpandInfo()) && Objects.nonNull(productMaterial.getExpandInfo().getDefaultExpiration())){
                expireDate = LocalDate.now().plusDays(productMaterial.getExpandInfo().getDefaultExpiration());
            }
            produceMaterialBatchVO = PreparationProduceConverter.INSTANCE.convert2PreparationProduceMaterialBatchVO(formulaMaterial,
                    unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
        } else{
            expireDate = storageMaterialBatch.getExpiredDate();
        }
        if (Objects.isNull(produceMaterialBatchVO)){
            produceMaterialBatchVO = PreparationProduceConverter.INSTANCE.convert2PreparationProduceMaterialBatchVO(storageMaterialBatch, formulaMaterial,
                    unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
        }
        produceMaterialBatchVO.setExpireDate(expireDate);
        return produceMaterialBatchVO;
    }

    @Override
    public List<PreparationProduceUserVO> queryCheckUserList(PreparationProduceCheckUserDTO dto) {
        // 获取具备所传权限码对应权限的角色下的人
        ResponseInfo<List<FeignUserVO>> responseInfo = FeignUtils.handleRequest(data -> roleFeign.authUserList(data), dto.getAuthCode());
        if (CollUtil.isEmpty(responseInfo.getData())){
            return Collections.emptyList();
        }
        List<FeignUserVO> authFeignUserVOS = responseInfo.getData();
        // 获取当前生产计划绑定的产线id
        Plan plan = planService.getById(dto.getProductPlanId());
        Long productionLineId = plan.getProductionLineId();
        // 符合条件的工位id集合
        List<Long> stationIdList = new ArrayList<>();
        // 获取当前组件配置的工位id
        String componentConfigJson = procedureStepConfigService.getComponentConfigJson(dto.getProcedureStepModelId(), dto.getComponentId(),
                dto.getReuse(), dto.getProcessId(), dto.getProcessVersion());
        BasicComponentConfig componentConfig = StrUtil.isNotEmpty(componentConfigJson) ? null : JSON.parseObject(componentConfigJson, BasicComponentConfig.class);
        if (Objects.isNull(componentConfig) || CollUtil.isEmpty(componentConfig.getStation())){
            // 代表没有配置工位 则获取当前工艺绑定的所有工位id
            ResponseInfo<List<FactoryStationFeignVO>> lineStationResponse = FeignUtils.handleRequest(data -> factoryFeign.getStationInfoByLineId(data), productionLineId);
            if (CollUtil.isNotEmpty(lineStationResponse.getData())){
                List<FactoryStationFeignVO> lineStations = lineStationResponse.getData();
                stationIdList = lineStations.stream().map(FactoryStationFeignVO::getId).collect(Collectors.toList());
            }
        } else {
            stationIdList = weedOutNotLineStation(componentConfig, productionLineId);
        }
        if (CollUtil.isEmpty(stationIdList)){
            // 代表没有工位 直接返回空数组
            return new ArrayList<>();
        }
        ResponseInfo<List<String>> stationResponseInfo = FeignUtils.handleRequest(data -> factoryFeign.getStationUserByStationIdList(data), stationIdList);
        List<String> stationUserIdList = stationResponseInfo.getData();
        if (CollUtil.isEmpty(stationUserIdList)){
            // 代表工位没有绑定用户，直接返回空
            return new ArrayList<>();
        }
        ResponseInfo<Map<String, FeignUserVO>> staionUserResponseInfo = FeignUtils.handleRequest(data -> userFeign.getByUserIds(data), stationUserIdList);
        Map<String, FeignUserVO> stationUserMap = staionUserResponseInfo.getData();
        if (CollUtil.isEmpty(stationUserMap)){
            // 代表userId查不到对应的用户 直接返回空
            return new ArrayList<>();
        }
        // 取authFeignUserVOS与工位下的用户的交集
        return  PreparationProduceConverter.INSTANCE.convertReCheckUserVOList(authFeignUserVOS.stream()
                .filter(user -> stationUserMap.containsKey(user.getUserId())).collect(Collectors.toList()));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long produceConfirm(ProduceConfirmUserDTO dto) {
        // 1. 生成对应的产出组件执行流程 判断是切换配液单还是继续绑定配液单
        PreparationProduceProgress preparationProduceProgress = preparationProduceProgressMapper.selectByComponentInfo(dto.getProductPlanId(), dto.getComponentId(), dto.getProcedureStepModelId(),
                dto.getCopyVersion(), dto.getReuse());
        if (Objects.isNull(preparationProduceProgress)){
           // 进行绑定
            preparationProduceProgress = PreparationProduceConverter.INSTANCE.convert2Progress(dto);
            preparationProduceProgressMapper.insert(preparationProduceProgress);
        } else if (!Objects.equals(preparationProduceProgress.getPreparationPlanId(), dto.getPreparationPlanId())) {
            // 切换配液单
            ProductFormulaMaterial formulaMaterial = formulaConfigureService.getFormulaMaterialById(dto.getFormulaMaterialId());
            if (Objects.isNull(formulaMaterial)){
                throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_COMPONENT_FORMULA_MATERIAL_DELETE);
            }
            // 查询配液单是否绑定物料件
            List<PreparationProduceRecord> preparationProduceRecordList = preparationProduceRecordMapper.selectByProgressId(preparationProduceProgress.getId());

            // 若没有生成物料批次则可以变更配液单
            if (CollUtil.isNotEmpty(preparationProduceRecordList)){
                throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_CREATE_MATERIAL);
            }
            PreparationProduceProgress newPreparationProduceProgress = PreparationProduceConverter.INSTANCE.convert2Progress(dto);
            newPreparationProduceProgress.setId(preparationProduceProgress.getId());
            preparationProduceProgressMapper.updateById(newPreparationProduceProgress);
        } else {
            // 更换批号
            PreparationProduceProgress newPreparationProduceProgress = PreparationProduceConverter.INSTANCE.convert2Progress(dto);
            newPreparationProduceProgress.setId(preparationProduceProgress.getId());
            preparationProduceProgressMapper.updateById(newPreparationProduceProgress);
        }
        return preparationProduceProgress.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String produceHandle(PreparationProduceDTO dto) {
        //   校验操作人员（移动端登录的账号）是否符合组件确认的产出人，若不符合，提示登录账号与产出人不符；
        PreparationProduceProgress progress = validProduce(dto.getProgressId(), dto.getProducerId());
        // 校验净重（配液产出结果）必须大于0
        validProduceHandleDTO(dto);
        // 校验容器
        ScanDeviceVO scanDeviceVO = null;
        if (StrUtil.isNotEmpty(dto.getDeviceCode())){
            scanDeviceVO = scanPreparationProduceContainer(dto.getDeviceCode());
        }
        // 校验暂存货位
        ScanCargoPositionVO scanCargoPositionVO = null;
        if (StrUtil.isNotEmpty(dto.getCargoPositionCode())){
            scanCargoPositionVO = scanPreparationCargoCode(dto.getCargoPositionCode());
        }
        // 进行配液产出
        return doProduceHandle(dto, scanDeviceVO, scanCargoPositionVO, progress);
    }

    @Override
    public ProduceVO queryProduce(Long progressId) {
        PreparationProduceProgress preparationProduceProgress = preparationProduceProgressMapper.selectById(progressId);
        if (Objects.isNull(preparationProduceProgress)){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_NOT_BIND_PREPARATION_PLAN);
        }
        // 根据progressId查询记录
        List<PreparationProduceRecord> preparationProduceRecordList = preparationProduceRecordMapper.selectByProgressId(progressId);
        Set<String> userIdSet = Sets.newHashSet(preparationProduceProgress.getProducerId(), preparationProduceProgress.getReCheckerId());
        if (CollUtil.isNotEmpty(preparationProduceRecordList)){
            for (PreparationProduceRecord produceRecord : preparationProduceRecordList) {
                userIdSet.add(produceRecord.getProducerId());
                userIdSet.add(produceRecord.getReCheckerId());
            }
        }
        ResponseInfo<Map<String, FeignUserVO>> responseInfo = FeignUtils.handleRequest(data -> userFeign.getByUserIds(data), userIdSet);
        Map<String, FeignUserVO> userVOMap = responseInfo.getData();
        // 查询用户信息
        ProduceVO produceVO = PreparationProduceConverter.INSTANCE.convert2ProduceVO(preparationProduceProgress, userVOMap);
        ProductFormulaMaterial formulaMaterial = formulaConfigureService.getFormulaMaterialById(preparationProduceProgress.getFormulaMaterialId());
        produceVO.setMaterialName(formulaMaterial.getMaterialName());
        produceVO.setUnitId(formulaMaterial.getUnitId());
        produceVO.setUnit(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
        produceVO.setMaterialSpecification(formulaMaterial.getMaterialSpecification());
        produceVO.setMaterialMergeCode(formulaMaterial.getMaterialMergeCode());
        if (CollUtil.isEmpty(preparationProduceRecordList)){
            return produceVO;
        }
        List<Long> positionIdList = preparationProduceRecordList.stream()
                .map(PreparationProduceRecord::getMaterialPositionId).filter(Objects::nonNull).collect(Collectors.toList());
        List<CargoPosition> cargoPositions = cargoPositionService.getByIdList(positionIdList);
        Map<Long, CargoPosition> cargoPositionMap = cargoPositions.stream().collect(Collectors.toMap(CargoPosition::getId, Function.identity()));
        List<ProduceRecordVO> produceRecordVOList = convert2ProduceRecordVOList(preparationProduceRecordList, cargoPositionMap, formulaMaterial, userVOMap);
        produceVO.setProduceRecordList(produceRecordVOList);
        return produceVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sign(ProducerSignDTO dto) {
        List<PreparationProduceRecord> produceRecordList = preparationProduceRecordMapper.selectBySignStatus(dto.getProgressId(),
                PrepareSignStatusEnum.UN_SIGNED.getValue());
        if (CollUtil.isEmpty(produceRecordList)){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_NO_UNSIGNED_RECORD);
        }
        // 签名产出人校验
        validProduce(dto.getProgressId(), dto.getProducerId());
        // 进行签名
        doSign(dto, produceRecordList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long changeProducer(ProduceChangeUserDTO dto) {
        PreparationProduceProgress preparationProduceProgress = preparationProduceProgressMapper.selectById(dto.getProgressId());
        if (Objects.isNull(preparationProduceProgress)){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_NOT_BIND_PREPARATION_PLAN);
        }
        // 产出人变更
        List<PreparationProduceRecord> produceRecordList = preparationProduceRecordMapper.selectByProgressId(dto.getProgressId());
        if (CollUtil.isEmpty(produceRecordList)){
            return dto.getProgressId();
        }
        List<PreparationProduceRecord> noSignedRecordList = produceRecordList.stream()
                .filter(produceRecord -> PrepareSignStatusEnum.UN_SIGNED.getValue().equals(produceRecord.getSignStatus())).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(noSignedRecordList)){
            // 已产出物料件需签名后才能更换
            String noSignMaterial = noSignedRecordList.stream().map(PreparationProduceRecord::getStorageMaterialNo).collect(Collectors.joining(StrUtil.COMMA));
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_SIGNED_RECORD_NOT_CHANGE, noSignMaterial);
        }
        // 变更签名
        preparationProduceProgress.setProducerId(dto.getProducerId());
        preparationProduceProgress.setReCheckerId(dto.getReCheckerId());
        preparationProduceProgressMapper.updateById(preparationProduceProgress);
        return preparationProduceProgress.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scrap(ProduceScrapDTO dto) {
        PreparationProduceProgress preparationProduceProgress = preparationProduceProgressMapper.selectById(dto.getProgressId());
        if (Objects.isNull(preparationProduceProgress)){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_NOT_BIND_PREPARATION_PLAN);
        }
        if (CollUtil.isEmpty(dto.getScrapStorageMaterialIdList())){
            return ;
        }
        List<PreparationProduceRecord> produceRecordList = preparationProduceRecordMapper.selectBatchIds(dto.getScrapStorageMaterialIdList());
        if (CollUtil.isEmpty(produceRecordList)){
            return ;
        }
        for (PreparationProduceRecord produceRecord : produceRecordList) {
            produceRecord.setSignStatus(PrepareSignStatusEnum.SCRAPED.getValue());
        }
        preparationProduceRecordMapper.updateBatch(produceRecordList);
        // 进行产出作废回填批记录
        generateBatchRecord(preparationProduceProgress);
        // 解绑物料件与容器的绑定关系
        storageMaterialService.unbindContainersByIds(produceRecordList.stream().map(PreparationProduceRecord::getStorageMaterialId).collect(Collectors.toList()));
        // 记录物料作废日志
        if (CollUtil.isNotEmpty(dto.getScrapStorageMaterialIdList())){
            Plan plan = planService.getById(preparationProduceProgress.getProductPlanId());
            List<StorageMaterial> storageMaterialList = storageMaterialService.queryListByIds(produceRecordList.stream().map(PreparationProduceRecord::getStorageMaterialId).collect(Collectors.toList()));
            createStorageMaterialAndCargoLog(storageMaterialList, plan, dto.getProducerId(), dto.getReCheckerId(), StorageOperateTypeEnum.PREPARATION_SCRAP);
        }
    }

    @Override
    public ScanDeviceVO scanPreparationProduceContainer(String code) {
        // 当前容器是否绑定其他物料件
        ResponseInfo<EquipmentInfoFeignVO> responseInfo = FeignUtils.handleRequest(data -> equipmentConfigFeign.getEquipmentByEquipmentCodeWithoutPermission(data), code);
        EquipmentInfoFeignVO equipmentInfoFeignVO = responseInfo.getData();
        // 校验是否为容器以及容器是否存在
        this.validateContainer(equipmentInfoFeignVO);
        // 设备是否可用
        if (!EquipmentStatusCodeEnum.AVAILABLE.getCode().equals(equipmentInfoFeignVO.getStatus())){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_CONTAINER_NOT_AVAILABLE, code);
        }
        // 校验容器是否在其他物料件下
        StorageMaterial storageMaterial = storageMaterialService.getByContainerId(equipmentInfoFeignVO.getId());
        if (Objects.nonNull(storageMaterial)){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_CONTAINER_EXIST, code, storageMaterial.getNo());
        }
        return PreparationProduceConverter.INSTANCE.convertScanDeviceVO(equipmentInfoFeignVO);
    }

    private void validateContainer(EquipmentInfoFeignVO equipment) {
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
    public ScanCargoPositionVO scanPreparationCargoCode(String code) {
        CargoPosition cargoPosition = cargoPositionService.getByCodeWithPermission(code);
        if (Objects.isNull(cargoPosition)){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_CARGO_NOT_EXIST, code);
        }
        return PreparationProduceConverter.INSTANCE.convertCargoPositionVO(cargoPosition);
    }

    /**
     * 剔除不属于目标产线下的工位
     * @param componentConfig
     * @param productionLineId
     * @return
     */
    private List<Long> weedOutNotLineStation(BasicComponentConfig componentConfig, Long productionLineId) {
        List<Long> stationIdList = new ArrayList<>();
        for (String stationShow : componentConfig.getStationShow()) {
            String lineId = StrUtil.split(stationShow, StrUtil.DASHED).get(0);
            if (StrUtil.equals(lineId, String.valueOf(productionLineId))){
                stationIdList.add(Long.valueOf(StrUtil.split(stationShow, StrUtil.DASHED).get(1)));
            }
        }
        return stationIdList;
    }


    /**
     * 封装返回值
     * @param preparationProduceProgress 流程信息
     * @param preparationPlan 配液单信息
     * @param storageMaterialBatch 物料批次信息
     * @param formulaMaterial 配方物料信息
     * @return
     */
    private PreparationProduceProgressVO assemblyPreparationProduceProgressVO(PreparationProduceProgress preparationProduceProgress, LiquidPreparationPlan preparationPlan,
                                                                              StorageMaterialBatch storageMaterialBatch, ProductFormulaMaterial formulaMaterial) {
        // 查询产出人以及复核人信息
        ResponseInfo<Map<String, FeignUserVO>> responseInfo = FeignUtils.handleRequest(data -> userFeign.getByUserIds(data),
                Lists.newArrayList(preparationProduceProgress.getProducerId(), preparationProduceProgress.getReCheckerId()));
        if (CollUtil.isEmpty(responseInfo.getData())){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_USER_NOT_EXIST);
        }
        Map<String, FeignUserVO> userMap = responseInfo.getData();
        PreparationProduceProgressVO preparationProduceProgressVO = PreparationProduceConverter.INSTANCE.convert2PreparationProduceProgressVO(preparationProduceProgress, userMap);
        PreparationProducePlanVO planVO = PreparationProduceConverter.INSTANCE.convert2ProducePlanVO(preparationPlan);
        String unitName = unitCache.getGlobalUnitName(formulaMaterial.getUnitId());
        PreparationProduceMaterialBatchVO batchVO = PreparationProduceConverter.INSTANCE.convert2PreparationProduceMaterialBatchVO(storageMaterialBatch, formulaMaterial, unitName);
        batchVO.setMaterialBatchNo(preparationProduceProgress.getMaterialBatchNo());
        batchVO.setExpireDate(batchVO.getExpireDate() != null ? batchVO.getExpireDate() : preparationProduceProgress.getExpiredDate());
        preparationProduceProgressVO.setPlanVO(planVO);
        preparationProduceProgressVO.setBatchVO(batchVO);
        // 校验是否产出物料件
        List<PreparationProduceRecord> preparationProduceRecordList = preparationProduceRecordMapper.selectByProgressId(preparationProduceProgress.getId());
        preparationProduceProgressVO.setProduceStorageMaterialFlg(CollUtil.isNotEmpty(preparationProduceRecordList));
        return preparationProduceProgressVO;
    }

    /**
     * 填充物料批次信息
     * @param preparationPlan
     * @param formulaMaterialId
     * @return
     */
    private PreparationProduceMaterialVO assemblyProduceMaterialVO(LiquidPreparationPlan preparationPlan, Long formulaMaterialId) {
        ProductFormulaMaterial formulaMaterial = formulaConfigureService.getFormulaMaterialById(formulaMaterialId);
        PreparationProduceMaterialVO preparationProduceMaterialVO = PreparationProduceConverter.INSTANCE.convertProduceMaterialVO(formulaMaterial);
        // 查询生产计划id
        Plan plan = planService.getById(preparationPlan.getProductPlanId());
        if (Objects.isNull(plan)){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_COMPONENT_PLAN_ALREADY_DELETE);
        }
        preparationProduceMaterialVO.setMaterialBatchNo(plan.getPlanNo());
        preparationProduceMaterialVO.setUnit(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
        PreparationMaterialBatchDTO preparationMaterialBatchDTO = new PreparationMaterialBatchDTO(preparationProduceMaterialVO.getMaterialBatchNo(), formulaMaterial.getId());
        PreparationProduceMaterialBatchVO produceMaterialBatchVO = queryMaterialBatch(preparationMaterialBatchDTO);
        preparationProduceMaterialVO.setExpireDate(produceMaterialBatchVO.getExpireDate());
        return preparationProduceMaterialVO;
    }

    /**
     * 配液产出
     * 配液产出成功时，若产出批次不存在，则创建该中间品批次和物料件，物料件预定到当前生产批次，物料件默认为未签名状态（需要签名后物料件生效才能用于其他功能使用）；
     * 若产出批次已存在（同一中间品物料批次号不重复），则在该中间品批次创建物料件；
     * 物料件默认初始量和预定量一致（都为产出结果），可用量和消耗量为0；
     * 物料批次量在物料件签名生效时改变；
     * 配液产出物料件与添加的容器绑定
     * 产生的物料件会直接入库暂存货位、
     * 记录日志
     *  物料日志：配液产出记录2条物料日志，操作类型为“产出”，具体操作分别为“配液产出-产出”和“配液产出-复核”，操作人员分别为当前配液产出组件的产出人和复核人；
     *  货位日志：（添加暂存货位就需要记录）：配液产出记录2条货位日志，操作类型为“入库”，具体操作分别为“物料入库-产出”和“物料入库-复核”；操作人员分别为当前配液产出组件的产出人和复核人；日志中的物料量对应产出结果值
     *  操作日志：配液产出记录1条操作日志，操作类型：新增；业务操作：配液产出；
     * @param dto
     * @param scanDeviceVO
     * @param scanCargoPositionVO
     * @param progress
     */
    private String doProduceHandle(PreparationProduceDTO dto, ScanDeviceVO scanDeviceVO,
                                 ScanCargoPositionVO scanCargoPositionVO, PreparationProduceProgress progress) {
        // 1. 查询当前物料id下的物料批号是否存在物料批次
        ProductFormulaMaterial formulaMaterial = formulaConfigureService.getFormulaMaterialById(progress.getFormulaMaterialId());
        if (Objects.isNull(formulaMaterial)) {
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_COMPONENT_FORMULA_MATERIAL_DELETE);
        }
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchService.queryMaterialBatchByNoAndMaterialId(formulaMaterial.getMaterialId(),
                progress.getMaterialBatchNo());
        if (Objects.isNull(storageMaterialBatch)){
            // 不存在则生成一个物料批次
            storageMaterialBatch = createNewMaterialBatch(progress, formulaMaterial);
        }
        // 创建物料件
        StorageMaterial storageMaterial = createStorageMaterial(dto, scanDeviceVO, scanCargoPositionVO, progress,
                storageMaterialBatch, formulaMaterial);
        // 将当前物料件预定到当前生产批次
        Plan plan = planService.getById(progress.getProductPlanId());
        if (Objects.isNull(plan)){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_PRODUCT_PLAN_DELETE);
        }
        predetermineMaterial(progress, storageMaterial, plan);
        List<PreparationProduceRecord> preparationProduceRecordList = preparationProduceRecordMapper.selectByProgressId(progress.getId());
        // 生成产出记录
        PreparationProduceRecord produceRecord = new PreparationProduceRecord();
        produceRecord.setProcedureProduceProgressId(progress.getId());
        produceRecord.setProducerId(dto.getProducerId());
        produceRecord.setStorageMaterialId(storageMaterial.getId());
        produceRecord.setStorageMaterialNo(storageMaterial.getNo());
        produceRecord.setStorageMaterialBatchId(storageMaterial.getStorageMaterialBatchId());
        produceRecord.setStorageMaterialBatchNo(storageMaterialBatch.getMaterialBatchNo());
        produceRecord.setNetWeight(String.valueOf(dto.getQuantity()));
        produceRecord.setUnitId(formulaMaterial.getUnitId());
        if (Objects.nonNull(scanDeviceVO)){
            produceRecord.setContainerId(scanDeviceVO.getDeviceId());
            produceRecord.setContainerCode(scanDeviceVO.getDeviceCode());
            produceRecord.setContainerName(scanDeviceVO.getDeviceName());
        }
        produceRecord.setWeighMode(WeighMode.MANUAL.getValue());
        produceRecord.setSignStatus(PrepareSignStatusEnum.UN_SIGNED.getValue());
        produceRecord.setProducerId(dto.getProducerId());
        produceRecord.setReCheckerId(progress.getReCheckerId());
        produceRecord.setProduceTime(LocalDateTime.now());
        produceRecord.setSort(preparationProduceRecordList.size() + 1);
        preparationProduceRecordList.add(produceRecord);
        if (Objects.nonNull(scanCargoPositionVO)){
            produceRecord.setMaterialPositionId(scanCargoPositionVO.getId());
        }
        preparationProduceRecordMapper.insert(produceRecord);
        // 记录物料日志
        createStorageMaterialAndCargoLog(Lists.newArrayList(storageMaterial), plan, progress.getProducerId(), progress.getReCheckerId(), StorageOperateTypeEnum.PREPARATION_PRODUCE);

        // 保存配液产出的物料追溯记录
        materialTraceHistoryService.saveTraceHistory(MaterialTraceHistoryDTO.builder()
                .storageMaterialId(storageMaterial.getId())
                .productPlanId(progress.getProductPlanId())
                .procedureStepModelId(progress.getProcedureStepModelId())
                .operateType(MaterialTraceOperateType.PREPARATION_OUTPUT)
                .quantity(dto.getQuantity())
                .unitId(formulaMaterial.getUnitId())
                .build());

        // 回填批记录
        generateBatchRecord(progress);
        if (CollUtil.isNotEmpty(preparationProduceRecordList)){
            preparationProduceRecordMapper.updateBatch(preparationProduceRecordList);
        }
        return storageMaterial.getNo();
    }

    private List<ProduceRecordVO> convert2ProduceRecordVOList(List<PreparationProduceRecord> preparationProduceRecordList,
                                                              Map<Long, CargoPosition> cargoPositionMap,
                                                              ProductFormulaMaterial formulaMaterial,
                                                              Map<String, FeignUserVO> userVOMap) {
        List<ProduceRecordVO> produceRecordVOList = new ArrayList<>();
        for (PreparationProduceRecord preparationProduceRecord : preparationProduceRecordList) {
            ProduceRecordVO produceRecordVO = new ProduceRecordVO();
            produceRecordVO.setId(preparationProduceRecord.getId());
            produceRecordVO.setStorageMaterialNo(preparationProduceRecord.getStorageMaterialNo());
            produceRecordVO.setStorageMaterialBatchNo(preparationProduceRecord.getStorageMaterialBatchNo());
            produceRecordVO.setStorageMaterialBatchId(preparationProduceRecord.getStorageMaterialBatchId());
            produceRecordVO.setUnitId(preparationProduceRecord.getUnitId());
            produceRecordVO.setUnit(unitCache.getGlobalUnitName(preparationProduceRecord.getUnitId()));
            produceRecordVO.setMaterialCode(formulaMaterial.getMaterialMergeCode());
            produceRecordVO.setMaterialName(formulaMaterial.getMaterialName());
            produceRecordVO.setQuantity(new BigDecimal(preparationProduceRecord.getNetWeight()));
            produceRecordVO.setSignStatus(PrepareSignStatusEnum.getEnumByValue(preparationProduceRecord.getSignStatus()));
            produceRecordVO.setContainerId(preparationProduceRecord.getContainerId());
            produceRecordVO.setContainerName(preparationProduceRecord.getContainerName());
            produceRecordVO.setProducerId(preparationProduceRecord.getProducerId());
            produceRecordVO.setSort(preparationProduceRecord.getSort());
            produceRecordVO.setSpecification(formulaMaterial.getMaterialSpecification());
            FeignUserVO feignUserVO = userVOMap.get(preparationProduceRecord.getProducerId());
            produceRecordVO.setProducerId(preparationProduceRecord.getProducerId());
            if (Objects.nonNull(feignUserVO)){
                produceRecordVO.setProducerName(feignUserVO.getUserName());
                produceRecordVO.setProducerFullName(feignUserVO.getLoginName() + StrUtil.DASHED + feignUserVO.getUserName());
            }
            FeignUserVO reCheckUserVO = userVOMap.get(preparationProduceRecord.getReCheckerId());
            if (Objects.nonNull(reCheckUserVO)){
                produceRecordVO.setReCheckerName(reCheckUserVO.getUserName());
                produceRecordVO.setReCheckerFullName(reCheckUserVO.getLoginName() + StrUtil.DASHED + reCheckUserVO.getUserName());
            }
            produceRecordVO.setProducerId(preparationProduceRecord.getReCheckerId());
            produceRecordVO.setProduceTime(preparationProduceRecord.getProduceTime());
            CargoPosition cargoPosition = cargoPositionMap.get(preparationProduceRecord.getMaterialPositionId());
            if (Objects.nonNull(cargoPosition)){
                produceRecordVO.setMaterialPositionName(cargoPosition.getCode() + StrUtil.DASHED + cargoPosition.getPosition());
            }
            produceRecordVOList.add(produceRecordVO);
        }
        return produceRecordVOList;
    }

    /**
     * 产出人必须为当前配液产出组件确认的产出人
     * @param progressId
     * @param producerId
     */
    private PreparationProduceProgress validProduce(Long progressId, String producerId) {
        PreparationProduceProgress preparationProduceProgress = preparationProduceProgressMapper.selectById(progressId);
        if (!StrUtil.equals(preparationProduceProgress.getProducerId(), producerId)){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_PRODUCER_NOT_MATCH);
        }
        return preparationProduceProgress;
    }

    /**
     * 签名
     * @param dto
     * @param produceRecordList
     */
    private void doSign(ProducerSignDTO dto, List<PreparationProduceRecord> produceRecordList) {
        LocalDateTime now = LocalDateTime.now();
        for (PreparationProduceRecord produceRecord : produceRecordList) {
            produceRecord.setSignStatus(PrepareSignStatusEnum.SIGNED.getValue());
            produceRecord.setReCheckerId(dto.getReCheckerId());
            produceRecord.setSignTime(now);
        }
        preparationProduceRecordMapper.updateBatch(produceRecordList);
        // 对应生成的物料件签名
        List<Long> ids = CollectionUtils.convertList(produceRecordList, PreparationProduceRecord::getStorageMaterialId);
        storageMaterialService.signBatchByIdList(ids);
    }

    /**
     * 配液量取组件进行批记录回填
     * @param progress
     */
    private void generateBatchRecord(PreparationProduceProgress progress) {
        // 寻找组件
        BatchRecordComponent recordComponent = recordComponentService.getById(progress.getComponentId());
        if (ObjectUtil.isNull(recordComponent)){
            throw new BmosException(MesResponseCode.COMPONENT_NOT_EXIST);
        }
        // 查询当前componentId
        ComponentListVO componentListVO =
                recordComponentService.selectUsedComponentDetail(progress.getRecordVersionId(),
                        progress.getRecordItemId(),
                        recordComponent.getId());
        if (ObjectUtil.isNull(componentListVO)){
            throw new BmosException(MesResponseCode.COMPONENT_NOT_EXIST);
        }
        // 产出结果信息
        ProduceVO produceVO = this.queryProduce(progress.getId());
        if (Objects.isNull(produceVO)){
            return ;
        }
        List<ProduceRecordVO> produceRecordList = produceVO.getProduceRecordList();
        if (CollUtil.isEmpty(produceRecordList)){
            return ;
        }
        // 根据sort字段进行排序
        produceRecordList.sort(Comparator.comparing(ProduceRecordVO::getSort));

        PreparationProduceDetailInfo detailInfo = new PreparationProduceDetailInfo();
        LiquidPreparationPlan liquidPreparationPlan = preparationPlanRepository.selectById(progress.getPreparationPlanId());
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(progress.getProcedureStepModelId());
        BusinessDataHandleBaseDTO businessDataHandleBaseDTO = PreparationProduceConverter.INSTANCE.convertToBusinessDataBaseDTO(liquidPreparationPlan, procedureStepModel);
        detailInfo.setDto(PreparationProduceConverter.INSTANCE.convert2BaseDTO(businessDataHandleBaseDTO));
        detailInfo.setPreparationProduceMaterialInfos(PreparationProduceConverter.INSTANCE.convert2DetailInfo(produceRecordList));
        List<ExecuteFormData> executeFormDataList = new ArrayList<>();
        Set<Long> storageMaterialBatchIdSet = produceRecordList.stream().map(ProduceRecordVO::getStorageMaterialBatchId).collect(Collectors.toSet());
        List<CustomFieldDetailInfo> customFieldDetailInfos = materialBatchFieldService.queryMaterialAndBatchField(storageMaterialBatchIdSet);
        detailInfo.setCustomFieldList(customFieldDetailInfos);
        ProcedureStepModel stepModel = procedureStepModelService.getById(progress.getProcedureStepModelId());
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(stepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        componentStrategy.handleBusinessComponent(executeFormDataList, componentListVO,
                detailInfo, configMap, null);
        if (CollectionUtil.isEmpty(executeFormDataList)){
            return ;
        }
        BusinessDataHandleBaseDTO dto = PreparationProduceConverter.INSTANCE.convert2HandleBaseDTO(progress);
        executeFormDataService.saveResultsAndHandleRelationComponentData(executeFormDataList, dto);
    }

    /**
     * 记录物料日志以及货位日志
     * @param storageMaterialList
     * @param plan
     * @param producerId
     * @param reCheckerId
     * @param operateTypeEnum
     */
    private void createStorageMaterialAndCargoLog(List<StorageMaterial> storageMaterialList, Plan plan, String producerId, String reCheckerId,
                                                   StorageOperateTypeEnum operateTypeEnum) {
        List<StorageMaterialPositionLogDTO> storageMaterialPositionLogDTOS = new ArrayList<>();
        if (CollUtil.isEmpty(storageMaterialList)){
            return ;
        }
        for (StorageMaterial storageMaterial : storageMaterialList) {
            StorageMaterialPositionLogDTO storageMaterialPositionLogDTO = new StorageMaterialPositionLogDTO();
            storageMaterialPositionLogDTO.setStorageMaterialId(storageMaterial.getId());
            storageMaterialPositionLogDTO.setOperateType(operateTypeEnum);
            storageMaterialPositionLogDTO.setQuantity(unitCache.toExt(storageMaterial.getQuantity(), storageMaterial.getFinalUnitId()));
            storageMaterialPositionLogDTO.setUnitId(storageMaterial.getFinalUnitId());
            storageMaterialPositionLogDTO.setSenderId(SysUserHolder.getUser().getUserId());
            storageMaterialPositionLogDTO.setReceiverId(SysUserHolder.getUser().getUserId());
            storageMaterialPositionLogDTO.setProductId(plan.getProductId());
            storageMaterialPositionLogDTO.setMaterialPositionId(storageMaterial.getMaterialPositionId());
            storageMaterialPositionLogDTO.setProductName(plan.getProductName());
            storageMaterialPositionLogDTO.setProductCode(plan.getProductMergeCode());
            storageMaterialPositionLogDTO.setProductBatchNo(plan.getBatchNo());
            storageMaterialPositionLogDTO.setSenderId(producerId);
            storageMaterialPositionLogDTO.setReceiverId(reCheckerId);
            storageMaterialPositionLogDTOS.add(storageMaterialPositionLogDTO);
        }
        storageMaterialPositionLogService.saveLogs(storageMaterialPositionLogDTOS);
    }


    /**
     * 将产出的物料件预定到当前生产批次
     * @param progress
     * @param storageMaterial
     */
    private void predetermineMaterial(PreparationProduceProgress progress, StorageMaterial storageMaterial, Plan plan) {

        storageMaterialService.reserve(StorageMaterialReserveDTO.builder()
                .storageMaterialId(storageMaterial.getId())
                .processId(plan.getProcessId())
                .batchId(plan.getId())
                .productId(plan.getProductId())
                .reCheckerId(progress.getReCheckerId())
                .operatorId(progress.getProducerId())
                .remark(progress.getRemark())
                .build());
    }

    /**
     * 创建新的物料件
     * 绑定设备+暂存货位
     * @param dto
     * @param scanDeviceVO
     * @param scanCargoPositionVO
     * @param progress
     * @param storageMaterialBatch
     * @return
     */
    private StorageMaterial createStorageMaterial(PreparationProduceDTO dto,
                                                  ScanDeviceVO scanDeviceVO,
                                                  ScanCargoPositionVO scanCargoPositionVO,
                                                  PreparationProduceProgress progress,
                                                  StorageMaterialBatch storageMaterialBatch,
                                                  ProductFormulaMaterial formulaMaterial) {
        StorageMaterial storageMaterial = new StorageMaterial();
        storageMaterial.setMaterialId(storageMaterialBatch.getMaterialId());
        storageMaterial.setStorageMaterialBatchId(storageMaterialBatch.getId());
        storageMaterial.setNo(storageMaterialService.getSerial());
        storageMaterial.setInitQuantity(unitCache.toBasic(dto.getQuantity(),formulaMaterial.getUnitId()));
        storageMaterial.setAvailableQuantity(unitCache.toBasic(dto.getQuantity(),
                formulaMaterial.getUnitId()));
        storageMaterial.setConsumeQuantity(BigDecimal.ZERO);
        storageMaterial.setUnitId(storageMaterialBatch.getUnitId());
        storageMaterial.setUnitExtendId(storageMaterialBatch.getUnitExtendId());
        storageMaterial.setReserveQuantity(BigDecimal.ZERO);
        storageMaterial.setSignStatus(WeighSignStatus.UN_SIGNED);
        storageMaterial.setProductPlanId(progress.getProductPlanId());
        if (Objects.nonNull(scanDeviceVO)){
            storageMaterial.setContainerId(scanDeviceVO.getDeviceId());
            storageMaterial.setContainer(scanDeviceVO.getDeviceCode() + StrUtil.DASHED + scanDeviceVO.getDeviceName());
        }
        if (Objects.nonNull(scanCargoPositionVO)){
            storageMaterial.setMaterialPositionId(scanCargoPositionVO.getId());
        }
        storageMaterialService.save(storageMaterial);
        // 确认编码
        storageMaterialService.confirmSerial(storageMaterial.getNo());
        return storageMaterial;
    }

    /**
     * 配液产出生成新的物料批次
     * @param progress
     * @param formulaMaterial
     */
    private StorageMaterialBatch createNewMaterialBatch(PreparationProduceProgress progress, ProductFormulaMaterial formulaMaterial) {
        StorageMaterialBatch storageMaterialBatch = new StorageMaterialBatch();
        storageMaterialBatch.setMaterialId(formulaMaterial.getMaterialId());
        storageMaterialBatch.setMaterialBatchNo(progress.getMaterialBatchNo());
        storageMaterialBatch.setExpiredDate(progress.getExpiredDate());
        storageMaterialBatch.setAvailable(LocalDate.now().isBefore(progress.getExpiredDate()));
        // 递交人为产出人
        storageMaterialBatch.setSenderId(progress.getProducerId());
        storageMaterialBatch.setUnitId(formulaMaterial.getUnitId());
        storageMaterialBatch.setProduceDate(LocalDate.now());
        storageMaterialBatchService.createMaterialBatch(storageMaterialBatch);
        return storageMaterialBatch;
    }

    /**
     * 校验配液产出的前端传参
     * 校验净重（配液产出结果）必须大于0
     * @param dto
     */
    private void validProduceHandleDTO(PreparationProduceDTO dto) {
        if (dto.getQuantity().compareTo(BigDecimal.ZERO) <= 0){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_QUANTITY_MUST_GE_ZERO);
        }
    }
}
