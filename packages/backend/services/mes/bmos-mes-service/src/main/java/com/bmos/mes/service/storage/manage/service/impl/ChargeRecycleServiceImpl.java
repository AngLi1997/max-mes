package com.bmos.mes.service.storage.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.constant.ProcessConstant;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.storage.ChargeRecycleTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.equipment.service.EquipmentCommonService;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.bmos.mes.service.execute.dto.RecordItemLatestDataQueryDTO;
import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.print.dto.PrintCommonDTO;
import com.bmos.mes.service.platform.print.feign.PlatformTagClient;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.ProcessVersionService;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.strategy.ChargeRecycleComponentStrategy;
import com.bmos.mes.service.record.convert.RecordComponentConvert;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.storage.manage.convert.StorageMaterialConverter;
import com.bmos.mes.service.storage.manage.dto.ChargeStorageMaterialDTO;
import com.bmos.mes.service.storage.manage.dto.ComponentChargeRecycleListQueryDTO;
import com.bmos.mes.service.storage.manage.dto.RecycleStorageMaterialDTO;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialQueryValidateDTO;
import com.bmos.mes.service.storage.manage.mapper.IChargeRecycleComponentMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialChargeRecycleMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialReserveMapper;
import com.bmos.mes.service.storage.manage.model.*;
import com.bmos.mes.service.storage.manage.service.ChargeRecycleService;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.vo.ChargeRecycleListVO;
import com.bmos.mes.service.storage.manage.vo.ComponentChargeListVO;
import com.bmos.mes.service.storage.manage.vo.ComponentChargeRecycleVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialDetailVO;
import com.bmos.mes.service.tag.convert.ScanDeviceConvert;
import com.bmos.mes.service.tag.dto.ScanChargeRecycleDeviceCodeDTO;
import com.bmos.mes.service.tag.dto.ScanMaterialOrDeviceDTO;
import com.bmos.mes.service.tag.vo.ChargeRecycleMaterialVO;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;
import com.bmos.mes.service.tag.vo.ScanMaterialOrDeviceVO;
import com.bmos.mes.service.trace.material.dto.MaterialTraceHistoryDTO;
import com.bmos.mes.service.trace.material.enums.MaterialTraceOperateType;
import com.bmos.mes.service.trace.material.service.IMaterialTraceHistoryService;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.facade.equipment.enums.EquipmentTagCodeEnum;
import com.bmos.platform.facade.equipment.enums.TagEquipmentPropertyCodeEnum;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentPropertyFeignVO;
import com.bmos.platform.facade.equipment.vo.TagFeignVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.unit.service.UnitCache;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChargeRecycleServiceImpl implements ChargeRecycleService {

    @Resource
    private ProcedureStepConfigService procedureStepConfigService;

    @Resource
    private IChargeRecycleComponentMapper chargeRecycleComponentMapper;

    @Resource
    private IStorageMaterialChargeRecycleMapper storageMaterialChargeRecycleMapper;

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Resource
    private IStorageMaterialBatchService storageMaterialBatchService;

    @Resource
    private ProductMaterialService productMaterialService;

    @Resource
    private UnitCache unitCache;

    @Resource
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Resource
    private PlanService planService;

    @Resource
    private ProcessVersionService processVersionService;

    @Resource
    private BatchRecordComponentService batchRecordComponentService;

    @Resource
    private ChargeRecycleComponentStrategy chargeRecycleComponentStrategy;

    @Resource
    private ProcedureStepModelService procedureStepModelService;

    @Resource
    private ExecuteFormDataService executeFormDataService;

    @Resource
    private IStorageMaterialReserveMapper materialReserveMapper;

    @Resource
    private PlatformTagClient platformTagClient;

    @Resource
    private IMaterialTraceHistoryService materialTraceHistoryService;

    @Resource
    private EquipmentCommonService equipmentCommonService;

    private final String PRINT_BODY_KEY_NO = "no";

    private final Long RAW_RECYCLE_SCENE = 121001005L;

    private final Long INTERMEDIATE_RECYCLE_SCENE = 121002005L;

    @Override
    public ComponentChargeRecycleVO getComponentChargeRecycleList(ComponentChargeRecycleListQueryDTO dto) {
        ComponentChargeRecycleVO result = new ComponentChargeRecycleVO();
        UniqueComponentQueryDTO build = UniqueComponentQueryDTO.builder()
                .componentId(dto.getComponentId())
                .copyVersion(dto.getCopyVersion())
                .productPlanId(dto.getProductPlanId())
                .reuse(dto.getReuse())
                .recordItemId(dto.getRecordItemId())
                .recordVersionId(dto.getRecordVersionId())
                .procedureStepModelId(dto.getProcedureStepModelId()).build();
        ChargeRecycleComponent chargeRecycleComponent =
                chargeRecycleComponentMapper.selectUnique(build);
        if (chargeRecycleComponent == null) {
            ChargeRecycleComponent insert = new ChargeRecycleComponent();
            insert.setProductPlanId(dto.getProductPlanId());
            insert.setComponentId(dto.getComponentId());
            insert.setReuse(dto.getReuse());
            insert.setCopyVersion(dto.getCopyVersion());
            insert.setProcedureStepModelId(dto.getProcedureStepModelId());
            insert.setRecordItemId(dto.getRecordItemId());
            insert.setRecordVersionId(dto.getRecordVersionId());
            chargeRecycleComponentMapper.insert(insert);
            result.setChargeRecycleComponentId(insert.getId());
            result.setList(new ArrayList<>());
            return result;
        }
        Long id = chargeRecycleComponent.getId();
        List<StorageMaterialChargeRecycle> list = storageMaterialChargeRecycleMapper.selectByChargeRecycleId(id, null);
        result.setChargeRecycleComponentId(id);
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfoByPlanId(chargeRecycleComponent.getProductPlanId());
        List<Long> recycleStorageMaterialIdList =
                list.stream().filter(e -> ChargeRecycleTypeEnum.RECYCLE.equals(e.getOperationType()))
                        .map(StorageMaterialChargeRecycle::getStorageMaterialId).collect(Collectors.toList());
        List<StorageMaterial> storageMaterials = storageMaterialService.queryListByIds(recycleStorageMaterialIdList);
        Map<Long, StorageMaterial> storageMaterialMap = CollectionUtils.convertMap(storageMaterials,
                StorageMaterial::getId);
        List<ChargeRecycleListVO> chargeRecycleListVOS =
                StorageMaterialConverter.INSTANCE.convertToChargeRecycleListVO(list);
        Map<Long, ProductFormulaMaterial> materialIdMap = formulaInfo.getMaterialIdMap();
        chargeRecycleListVOS.forEach(e -> {
            ProductFormulaMaterial formulaMaterial = materialIdMap.get(e.getMaterialId());
            if (formulaMaterial == null) {
                throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
            }
            e.setCategoryInfoType(formulaMaterial.getMaterialType());
            e.setUnitName(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
            e.setQuantity(MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(e.getQuantity(),
                    formulaMaterial.getUnitId()), formulaMaterial));
            e.setUnitId(formulaMaterial.getUnitId());
            StorageMaterial storageMaterial = storageMaterialMap.get(e.getStorageMaterialId());
            if (e.getOperationType().equals(ChargeRecycleTypeEnum.RECYCLE)) {
                e.setUseUp(storageMaterial.getQuantity().compareTo(BigDecimal.ZERO) == 0);
            }
        });
        result.setList(chargeRecycleListVOS);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void chargeStorageMaterial(ChargeStorageMaterialDTO dto) {
        Long chargeRecycleComponentId = dto.getChargeRecycleComponentId();
        ChargeRecycleComponent chargeRecycleComponent =
                chargeRecycleComponentMapper.selectById(chargeRecycleComponentId);
        if (chargeRecycleComponent == null) {
            throw new BmosException(MesResponseCode.NO_CHARGE_RECYCLE_INIT_DATA);
        }
        EquipmentInfoFeignVO equipmentInfo = getEquipmentInfo(dto.getDeviceId());
        List<StorageMaterial> storageMaterials =
                storageMaterialService.queryListByIds(CollectionUtils.convertList(dto.getChargeMaterialList(),
                        ChargeStorageMaterialDTO.ChargeMaterial::getStorageMaterialId));
        Long productPlanId = chargeRecycleComponent.getProductPlanId();
        // 配方信息
        ProductFormulaInfo formulaInfo = productFormulaConfigureService.getProductFormulaInfoByPlanId(productPlanId);
        Map<Long, ProductFormulaMaterial> materialIdMap = formulaInfo.getMaterialIdMap();
        List<StorageMaterialBatch> batchList =
                storageMaterialBatchService.queryListByIds(CollectionUtils.convertList(storageMaterials,
                        StorageMaterial::getStorageMaterialBatchId));
        batchList.forEach(StorageMaterialBatch::availableValidate);
        // 物料批次map
        Map<Long, StorageMaterialBatch> batchMap = CollectionUtils.convertMap(batchList, StorageMaterialBatch::getId);
        List<ChargeStorageMaterialDTO.ChargeMaterial> chargeMaterialList = dto.getChargeMaterialList();
        Map<Long, ChargeStorageMaterialDTO.ChargeMaterial> chargeMap = CollectionUtils.convertMap(chargeMaterialList,
                ChargeStorageMaterialDTO.ChargeMaterial::getStorageMaterialId);
        List<MaterialTraceHistoryDTO> traceList = new ArrayList<>();
        List<StorageMaterialChargeRecycle> collect = storageMaterials.stream().map(e -> {
            // 将物料件单位更新为配方物料单位
            ProductFormulaMaterial formulaMaterial = materialIdMap.get(e.getMaterialId());
            e.setUnitId(unitCache.getBaseUnitId(formulaMaterial.getUnitId()));
            e.setUnitExtendId(formulaMaterial.getUnitId());
            StorageMaterialBatch batch = batchMap.get(e.getStorageMaterialBatchId());
            if (batch == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
            }
            StorageMaterialChargeRecycle storage = new StorageMaterialChargeRecycle();
            storage.setMaterialId(e.getMaterialId());
            storage.setMaterialName(formulaMaterial.getMaterialName());
            storage.setSpecification(formulaMaterial.getMaterialSpecification());
            storage.setMaterialMergeCode(formulaMaterial.getMaterialMergeCode());
            storage.setMaterialBatchNo(batch.getMaterialBatchNo());
            storage.setMaterialBatchId(batch.getId());
            storage.setStorageMaterialNo(e.getNo());
            storage.setStorageMaterialId(e.getId());
            // 投入时修约为配方单位精度进行计算
            ChargeStorageMaterialDTO.ChargeMaterial chargeMaterial = chargeMap.get(e.getId());
            BigDecimal roundingChargeQuantity =
                    MaterialQuantityCalculateUtil.roundingOff(chargeMaterial.getChargeQuantity(), formulaMaterial);
            BigDecimal chargeBasicQuantity = unitCache.toBasic(roundingChargeQuantity, chargeMaterial.getUnitId());
            // 在新校验规则下不预定也可以直接投料 所以使用可用量和预定量之和进行比较计算
            BigDecimal storageBasicQuantity = e.getQuantity();
            // 预定量转为配方单位后修约后的量
            BigDecimal reserveRoundingQuantity =
                    MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(e.getQuantity(),
                            formulaMaterial.getUnitId()), formulaMaterial);

            BigDecimal storageRoundingQuantity =
                    MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(storageBasicQuantity,
                            formulaMaterial.getUnitId()), formulaMaterial);
            // 投入量大于预定量
            if (roundingChargeQuantity.compareTo(storageRoundingQuantity) > 0) {
                throw new BmosException(MesResponseCode.CHARGE_QUANTITY_MORE_THAN_RESERVED);
            }
            // 更新物料件消耗量等信息
            // 投入量减预定量 绝对值小于精度 说明被完全消耗
            if (roundingChargeQuantity.subtract(reserveRoundingQuantity).abs().compareTo(formulaMaterial.getScale()) < 0) {
                e.consumeAllQuantity();
            } else {
                e.consumeQuantity(chargeBasicQuantity);
            }
            storage.setQuantity(chargeBasicQuantity);
            storage.setUnitId(unitCache.getBaseUnitId(e.getUnitId()));
            storage.setOperationType(ChargeRecycleTypeEnum.CHARGE);
            storage.setChargeRecycleComponentId(dto.getChargeRecycleComponentId());
            storage.setOperatorId(dto.getOperatorId());
            storage.setEquipmentId(equipmentInfo.getId());
            storage.setEquipmentName(equipmentInfo.getName());
            storage.setEquipmentCode(equipmentInfo.getCode());

            traceList.add(MaterialTraceHistoryDTO.builder()
                    .storageMaterialId(e.getId())
                    .productPlanId(productPlanId)
                    .procedureStepModelId(chargeRecycleComponent.getProcedureStepModelId())
                    .operateType(MaterialTraceOperateType.PRODUCT_INPUT)
                    .quantity(roundingChargeQuantity)
                    .unitId(chargeMaterial.getUnitId())
                    .build());

            return storage;
        }).collect(Collectors.toList());
        storageMaterialChargeRecycleMapper.insertBatch(collect);
        // 消耗物料
        storageMaterialService.chargeConsume(storageMaterials, dto.getOperatorId(), productPlanId);
        // 保存生产投料的物料追溯记录
        materialTraceHistoryService.saveTraceHistory(traceList);
        // 数据处理
        List<ExecuteFormData> results = generateExecuteFormData(dto, dto.getChargeRecycleComponentId());
        executeFormDataService.saveResultsAndHandleRelationComponentData(results, dto);
    }

    @NotNull
    private EquipmentInfoFeignVO getEquipmentInfo(Long deviceId) {
        if (deviceId == null) {
            return new EquipmentInfoFeignVO();
        }
        ResponseInfo<EquipmentInfoFeignVO> equipmentRes =
                FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByEquipmentId(data),
                        deviceId);
        EquipmentInfoFeignVO equipmentInfo = equipmentRes.getData();
        if (equipmentInfo == null) {
            throw new BmosException(MesResponseCode.EQUIPMENT_INFO_COMPONENT_EQUIPMENT_NOT_EXITS_ERROR);
        }
        return equipmentInfo;
    }

    @Override
    public List<ComponentChargeListVO> getComponentChargeList(Long chargeRecycleComponentId) {
        ChargeRecycleComponent chargeRecycleComponent =
                chargeRecycleComponentMapper.selectById(chargeRecycleComponentId);
        if (chargeRecycleComponent == null) {
            throw new BmosException(MesResponseCode.NO_CHARGE_RECYCLE_INIT_DATA);
        }
        List<StorageMaterialChargeRecycle> chargeRecycleList =
                storageMaterialChargeRecycleMapper.selectByChargeRecycleId(chargeRecycleComponentId,
                        null);
        // 投料map
        Map<Long, List<StorageMaterialChargeRecycle>> materialMap =
                CollectionUtils.convertMultiMap(chargeRecycleList.stream()
                                .filter(e -> ChargeRecycleTypeEnum.CHARGE.equals(e.getOperationType())).collect(Collectors.toList()),
                        StorageMaterialChargeRecycle::getMaterialId);
        // 回收列表
        List<StorageMaterialChargeRecycle> recycleList =
                chargeRecycleList.stream().filter(e -> ChargeRecycleTypeEnum.RECYCLE.equals(e.getOperationType())).collect(Collectors.toList());
        Map<Long, List<StorageMaterialChargeRecycle>> recycleBatchMap = CollectionUtils.convertMultiMap(recycleList,
                StorageMaterialChargeRecycle::getMaterialBatchId);
        // 配方信息
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfoByPlanId(chargeRecycleComponent.getProductPlanId());
        Map<Long, ProductFormulaMaterial> materialIdMap = formulaInfo.getMaterialIdMap();
        ArrayList<ComponentChargeListVO> result = new ArrayList<>();
        for (Map.Entry<Long, List<StorageMaterialChargeRecycle>> entry : materialMap.entrySet()) {
            Long key = entry.getKey();
            List<StorageMaterialChargeRecycle> value = entry.getValue();
            ComponentChargeListVO vo = new ComponentChargeListVO();
            StorageMaterialChargeRecycle first = CollUtil.getFirst(value);
            vo.setMaterialId(key);
            vo.setMaterialName(first.getMaterialName());
            vo.setMaterialMergeCode(first.getMaterialMergeCode());
            vo.setSpecification(first.getSpecification());
            ProductFormulaMaterial formulaMaterial = materialIdMap.get(first.getMaterialId());
            Map<Long, List<StorageMaterialChargeRecycle>> batchMap = CollectionUtils.convertMultiMap(value,
                    StorageMaterialChargeRecycle::getMaterialBatchId);
            vo.setChargeBatchInfoList(batchMap.entrySet().stream().map(e -> {
                StorageMaterialChargeRecycle first1 = CollUtil.getFirst(e.getValue());
                ComponentChargeListVO.ChargeBatchInfo batch = new ComponentChargeListVO.ChargeBatchInfo();
                batch.setUnitId(formulaMaterial.getUnitId());
                batch.setUnitName(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
                BigDecimal chargeQuantity =
                        e.getValue().stream().map(charge -> {
                            BigDecimal quantity = charge.getQuantity();
                            BigDecimal ext = unitCache.toExt(quantity, formulaMaterial.getUnitId());
                            return MaterialQuantityCalculateUtil.roundingOff(ext, formulaMaterial);
                        }).reduce(BigDecimal.ZERO,
                                BigDecimal::add);
                List<StorageMaterialChargeRecycle> recycles = recycleBatchMap.get(first1.getMaterialBatchId());

                // 计算结果
                BigDecimal resultQuantity = CollUtil.isNotEmpty(recycles) ? chargeQuantity.subtract(recycles.stream()
                        .map(recycle->{
                            BigDecimal quantity = recycle.getQuantity();
                            BigDecimal ext = unitCache.toExt(quantity, formulaMaterial.getUnitId());
                            return MaterialQuantityCalculateUtil.roundingOff(ext, formulaMaterial);
                        }).reduce(BigDecimal.ZERO, BigDecimal::add)) :
                        chargeQuantity;
                // 修约
                batch.setQuantity(MaterialQuantityCalculateUtil.roundingOff(resultQuantity, formulaMaterial));
                batch.setMaterialBatchId(first1.getMaterialBatchId());
                batch.setMaterialBatchNo(first1.getMaterialBatchNo());
                return batch;
            }).collect(Collectors.toList()));
            result.add(vo);
        }
        return result;
    }

    @Override
    public void recycleStorageMaterial(RecycleStorageMaterialDTO dto) {
        ChargeRecycleComponent chargeRecycleComponent =
                chargeRecycleComponentMapper.selectById(dto.getChargeRecycleComponentId());
        if (chargeRecycleComponent == null) {
            throw new BmosException(MesResponseCode.NO_CHARGE_RECYCLE_INIT_DATA);
        }
        Long productPlanId = chargeRecycleComponent.getProductPlanId();
        Plan plan = planService.getById(productPlanId);
        dto.setProductId(plan.getProductId());
        // 校验回收量
        BigDecimal chargeQuantity =
                storageMaterialChargeRecycleMapper.selectChargeQuantity(dto.getChargeRecycleComponentId(), dto.getMaterialBatchId());
        BigDecimal recycleQuantity =
                storageMaterialChargeRecycleMapper.selectRecycleQuantity(dto.getChargeRecycleComponentId(), dto.getMaterialBatchId());
        BigDecimal convert = unitCache.convert(dto.getQuantity(), dto.getUnitId(), unitCache.getBaseUnitId(dto.getUnitId()));
        if (convert.compareTo(chargeQuantity.subtract(recycleQuantity)) > 0) {
            throw new BmosException(MesResponseCode.RECYCLE_QUANTITY_MORE_THAN_CHARGE);
        }
        StorageMaterial storageMaterial = storageMaterialService.recycleStorageMaterial(dto);
        StorageMaterialChargeRecycle insert = getStorageMaterialChargeRecycle(dto, storageMaterial);
        insert.setEquipmentName(dto.getDeviceName());
        insert.setEquipmentCode(dto.getDeviceCode());
        storageMaterialChargeRecycleMapper.insert(insert);
        // 数据处理
        List<ExecuteFormData> results = generateExecuteFormData(dto, dto.getChargeRecycleComponentId());
        executeFormDataService.saveResultsAndHandleRelationComponentData(results, dto);
        // 打印
        Long printerId = dto.getPrinterId();
        if (printerId == null) {
            log.error("投料回收:打印设备未配置");
            return;
        }
        EquipmentInfoFeignVO printer = FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByEquipmentId(data), printerId).getData();
        if(printer == null){
            log.error("设备:{}不存在", printerId);
            return;
        }
        PrintCommonDTO printCommonDTO = getPrintCommonDTO(storageMaterial, printer);
        try {
            platformTagClient.printTag(printCommonDTO);
        } catch (Exception e) {
            log.error("回收:打印失败:{}", e.getCause() + e.getMessage());
        }

    }

    private PrintCommonDTO getPrintCommonDTO(StorageMaterial storageMaterial, EquipmentInfoFeignVO printer) {
        PrintCommonDTO printCommonDTO = new PrintCommonDTO();
        HashMap<String, Object> body = new HashMap<>();
        body.put(PRINT_BODY_KEY_NO, storageMaterial.getNo());
        ProductMaterial productMaterial = productMaterialService.selectById(storageMaterial.getMaterialId());
        printCommonDTO.setBody(body);
        Integer materialType = productMaterial.getCategoryType();
        printCommonDTO.setSceneId(CategoryInfoTypeEnum.RAW_MATERIAL.getValue().equals(materialType) ? RAW_RECYCLE_SCENE : INTERMEDIATE_RECYCLE_SCENE);
        String ip = "";
        String port = "";
        List<EquipmentPropertyFeignVO> propertyFeignVOList = printer.getInfoPropertyList();
        for (EquipmentPropertyFeignVO equipmentPropertyFeignVO : propertyFeignVOList) {
            if (Objects.equals(equipmentPropertyFeignVO.getCode(), TagEquipmentPropertyCodeEnum.IP_ADDRESS.getCode())){
                ip = equipmentPropertyFeignVO.getValue();
            }
            if (Objects.equals(equipmentPropertyFeignVO.getCode(), TagEquipmentPropertyCodeEnum.PORT.getCode())){
                port = equipmentPropertyFeignVO.getValue();
            }
        }
        printCommonDTO.setPrinterIp(ip);
        printCommonDTO.setPrinterPort(Integer.parseInt(port));
        return printCommonDTO;
    }

    @Override
    public ScanMaterialOrDeviceVO scanMaterialOrDevice(ScanMaterialOrDeviceDTO dto) {
        ScanMaterialOrDeviceVO result = new ScanMaterialOrDeviceVO();
        ChargeRecycleComponent component = chargeRecycleComponentMapper.selectById(dto.getChargeRecycleId());
        Long productPlanId = component.getProductPlanId();
        StorageMaterialDetailVO detail = storageMaterialService.queryByCodeAndValidate(StorageMaterialQueryValidateDTO
                .builder()
                .no(dto.getCode())
                .productPlanId(productPlanId)
                .build());
        Plan plan = planService.getById(productPlanId);
        String componentConfigJson =
                procedureStepConfigService.getComponentConfigJson(component.getProcedureStepModelId(),
                        component.getComponentId(), component.getReuse(), plan.getProcessId(),
                        plan.getProcessVersion());
        // 校验 若配置了配方物料只能配置所选的物料
        ProcedureStepConfigInfo configInfo = StrUtil.isNotBlank(componentConfigJson) ?
                JsonUtils.parseObject(componentConfigJson,
                ProcedureStepConfigInfo.class) : new ProcedureStepConfigInfo();
        List<Long> formulaMaterialIds = configInfo.getFormulaMaterialIds();
        ProductFormulaInfo formulaInfo = productFormulaConfigureService.getProductFormulaInfoByPlanId(plan.getId());
        Map<Long, ProductFormulaMaterial> materialIdMap = formulaInfo.getMaterialIdMap();
        StorageMaterial storageMaterial = detail.getStorageMaterial();
        if (CollUtil.isNotEmpty(formulaMaterialIds)) {
            List<ProductFormulaMaterial> formulaMaterialListByIds =
                    productFormulaConfigureService.getFormulaMaterialListByIds(formulaMaterialIds);
            Map<Long, ProductFormulaMaterial> formulaMaterialMap =
                    CollectionUtils.convertMap(formulaMaterialListByIds, ProductFormulaMaterial::getMaterialId);
            ProductFormulaMaterial formulaMaterial = formulaMaterialMap.get(storageMaterial.getMaterialId());
            if (formulaMaterial == null) {
                throw new BmosException(MesResponseCode.CANT_CHARGE_THIS_MATERIAL);
            }
        } else {
            ProductFormulaMaterial formulaMaterial = materialIdMap.get(storageMaterial.getMaterialId());
            if (formulaMaterial == null) {
                throw new BmosException(MesResponseCode.CANT_CHARGE_THIS_MATERIAL);
            }
        }
        detail.validateAll();
        ProductFormulaMaterial formulaMaterial = materialIdMap.get(storageMaterial.getMaterialId());
        if(formulaMaterial == null){
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        ChargeRecycleMaterialVO vo = new ChargeRecycleMaterialVO();
        vo.setMaterialId(storageMaterial.getMaterialId());
        vo.setStorageMaterialNo(storageMaterial.getNo());
        vo.setQuantity(MaterialQuantityCalculateUtil.roundingOff(unitCache.convert(storageMaterial.getQuantity(),
                unitCache.getBaseUnitId(formulaMaterial.getUnitId()),
                formulaMaterial.getUnitId()), formulaMaterial));
        vo.setUnitId(formulaMaterial.getUnitId());
        vo.setUnitName(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
        vo.setStorageMaterialBatchId(storageMaterial.getStorageMaterialBatchId());
        vo.setMaterialBatchNo(detail.getStorageMaterialBatch().getMaterialBatchNo());
        vo.setMaterialMergeCode(formulaMaterial.getMaterialMergeCode());
        vo.setMaterialName(formulaMaterial.getMaterialName());
        vo.setStorageMaterialId(storageMaterial.getId());
        result.setMaterialInfo(vo);
        return result;
    }

    @Override
    public ScanDeviceVO scanChargeRecycleDeviceCode(ScanChargeRecycleDeviceCodeDTO dto) {
        ResponseInfo<EquipmentInfoFeignVO> equipment =
                equipmentConfigFeign.getEquipmentByEquipmentCode(dto.getDeviceCode());
        ScanDeviceVO deviceVO = ScanDeviceConvert.INSTANCE.convertToDeviceVo(equipment.getData());
        ChargeRecycleComponent component = chargeRecycleComponentMapper.selectById(dto.getChargeRecycleId());
        Long productPlanId = component.getProductPlanId();
        Plan plan = planService.getById(productPlanId);
        String configJson =
                procedureStepConfigService.getComponentConfigJson(component.getProcedureStepModelId(),
                        component.getComponentId(), component.getReuse(), plan.getProcessId(),
                        plan.getProcessVersion());
        if (StringUtil.isNotBlank(configJson)) {
            JSONArray jsonArray = JSONUtil.parseObj(configJson).getJSONArray(ProcessConstant.stationPathField);
            if (CollUtil.isEmpty(jsonArray)) {
                return deviceVO;
            }
            // 校验绑定工位设备
            List<String> pathList =
                    jsonArray.toList(String.class);
            List<Long> list = pathList.stream()
                    .filter(e -> e.startsWith(String.valueOf(plan.getProductionLineId())))
                    .map(e -> {
                        List<String> split = StrUtil.split(e, StrUtil.DASHED);
                        return Long.valueOf(CollUtil.getLast(split));
                    }).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(list)) {
                ResponseInfo<List<EquipmentInfoFeignVO>> res =
                        FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByStationIdList(data), list);
                List<EquipmentInfoFeignVO> data = res.getData();
                Set<Long> deviceSet = CollectionUtils.convertSet(data, EquipmentInfoFeignVO::getId);
                if (!deviceSet.contains(deviceVO.getDeviceId())) {
                    throw new BmosException(MesResponseCode.CANT_CHARGE_IN_THIS_DEVICE);
                }
            }
        } else {
            // 校验产线设备
            Long productionLineId = plan.getProductionLineId();
            ResponseInfo<List<EquipmentInfoFeignVO>> res =
                    FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByProductionLineId(data),
                            productionLineId);
            List<EquipmentInfoFeignVO> data = res.getData();
            Set<Long> deviceSet = CollectionUtils.convertSet(data, EquipmentInfoFeignVO::getId);
            if (!deviceSet.contains(deviceVO.getDeviceId())) {
                throw new BmosException(MesResponseCode.CANT_CHARGE_IN_THIS_DEVICE);
            }
        }
        return deviceVO;
    }

    @Override
    public ScanDeviceVO scanChargeRecycleContainer(ScanChargeRecycleDeviceCodeDTO dto) {

        ResponseInfo<EquipmentInfoFeignVO> equipmentRes =
                FeignUtils.handleRequest(data -> equipmentConfigFeign.getEquipmentByEquipmentCode(data),
                        dto.getDeviceCode());
        EquipmentInfoFeignVO equipment = equipmentRes.getData();
        if (equipment == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_NOT_EXIST);
        }
        ChargeRecycleComponent component = chargeRecycleComponentMapper.selectById(dto.getChargeRecycleId());
        Long productPlanId = component.getProductPlanId();
        Plan plan = planService.getById(productPlanId);
        ResponseInfo<List<EquipmentInfoFeignVO>> listResponseInfo = FeignUtils.handleRequest(data ->
                equipmentConfigFeign.getConfigByProductionLineId(data), plan.getProductionLineId());
        List<EquipmentInfoFeignVO> equipmentList = listResponseInfo.getData();
        if(CollUtil.isEmpty(equipmentList)){
            throw new BmosException(MesResponseCode.PLEASE_SCAN_PRODUCTION_LINE_CONTAINER);
        }
        Set<Long> equipmentSet = equipmentList.stream().map(EquipmentInfoFeignVO::getId).collect(Collectors.toSet());
        if (!equipmentSet.contains(equipment.getId())) {
            throw new BmosException(MesResponseCode.PLEASE_SCAN_PRODUCTION_LINE_CONTAINER);
        }
        // 判断是否是容器
        if (CollectionUtil.isNotEmpty(equipment.getEquipmentTagDataList())) {
            Optional<TagFeignVO> any = equipment.getEquipmentTagDataList().stream()
                    .filter(item -> Objects.equals(item.getCode(), EquipmentTagCodeEnum.CONTAINER_12021.getCode()))
                    .findAny();
            if (!any.isPresent()) {
                throw new BmosException(MesResponseCode.EQUIPMENT_NOT_CONTAINER);
            }
        }
        StorageMaterial storageMaterial = storageMaterialService.selectStorageMaterialByContainerId(equipment.getId());
        if (storageMaterial != null) {
            throw new BmosException(MesResponseCode.CONTAINER_ALREADY_HAS_MATERIAL);
        }
        if (!ObjectUtil.equal(EquipmentStatusCodeEnum.AVAILABLE.getCode(), equipment.getStatus())) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_NOT_AVAILABLE);
        }
        return ScanDeviceConvert.INSTANCE.convertToDeviceVo(equipment);
    }

    private List<ExecuteFormData> generateExecuteFormData(BusinessDataHandleBaseDTO dto,
                                                          Long chargeRecycleComponentId) {
        ComponentListVO component = batchRecordComponentService.selectUsedComponentDetail(dto.getRecordVersionId(),
                dto.getRecordItemId(), dto.getComponentId());
        ProductionDetailInfo info = new ProductionDetailInfo();
        List<ExecuteFormData> results = new ArrayList<>();
        info.setDto(RecordComponentConvert.INSTANCE.convertToBusinessComponentBatchSaveDTO(dto));
        RecordItemLatestDataQueryDTO queryDTO = getRecordItemLatestDataQueryDTO(dto, component);
        List<FormDataItemVO> recordItemLatestData = executeFormDataService.getRecordItemLatestData(queryDTO);
        info.setFormDataCollection(recordItemLatestData);
        List<StorageMaterialChargeRecycle> chargeRecycleList =
                storageMaterialChargeRecycleMapper.selectByChargeRecycleId(chargeRecycleComponentId, null);
        info.setChargeRecycleList(chargeRecycleList);
        ProcessVersion processVersion = processVersionService.getByProcessIdAndVersion(dto.getProcessId(),
                dto.getProcessVersion());
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfo(processVersion.getProductFormulaVersionId());
        info.setFormulaInfo(formulaInfo);
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        chargeRecycleComponentStrategy.handleBusinessComponent(results, component, info, configMap, null);
        return results;
    }

    private RecordItemLatestDataQueryDTO getRecordItemLatestDataQueryDTO(BusinessDataHandleBaseDTO dto, ComponentListVO component) {
        List<Long> fieldIds = new ArrayList<>();
        recGetComponentFieldList(component, fieldIds);
        RecordItemLatestDataQueryDTO queryDTO = new RecordItemLatestDataQueryDTO();
        queryDTO.setReuse(dto.getReuse());
        queryDTO.setDiscard(false);
        queryDTO.setCopyVersion(dto.getCopyVersion());
        queryDTO.setProductPlanId(dto.getProductPlanId());
        queryDTO.setProcedureStepId(dto.getProcedureStepId());
        queryDTO.setFieldIdList(fieldIds);
        queryDTO.setRecordItemId(dto.getRecordItemId());
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

    private StorageMaterialChargeRecycle getStorageMaterialChargeRecycle(RecycleStorageMaterialDTO dto,
                                                                         StorageMaterial storageMaterial) {
        StorageMaterialChargeRecycle insert = new StorageMaterialChargeRecycle();
        ProductMaterial productMaterial = productMaterialService.selectById(storageMaterial.getMaterialId());
        insert.setMaterialId(storageMaterial.getMaterialId());
        insert.setMaterialName(productMaterial.getName());
        insert.setSpecification(productMaterial.getSpecification());
        insert.setMaterialMergeCode(productMaterial.getMergeCode());
        StorageMaterialBatch batch = storageMaterialBatchService.getById(storageMaterial.getStorageMaterialBatchId());
        insert.setMaterialBatchNo(batch.getMaterialBatchNo());
        insert.setMaterialBatchId(storageMaterial.getStorageMaterialBatchId());
        insert.setStorageMaterialNo(storageMaterial.getNo());
        insert.setStorageMaterialId(storageMaterial.getId());
        // 回收使用基本单位量
        insert.setQuantity(unitCache.convert(dto.getQuantity(), dto.getUnitId(),
                unitCache.getBaseUnitId(dto.getUnitId())));
        insert.setUnitId(unitCache.getBaseUnitId(dto.getUnitId()));
        insert.setOperationType(ChargeRecycleTypeEnum.RECYCLE);
        insert.setChargeRecycleComponentId(dto.getChargeRecycleComponentId());
        insert.setOperatorId(dto.getOperatorId());
        insert.setEquipmentId(storageMaterial.getContainerId());
        return insert;
    }
}
