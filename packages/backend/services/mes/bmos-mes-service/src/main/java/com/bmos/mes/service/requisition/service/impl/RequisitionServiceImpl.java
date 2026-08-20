package com.bmos.mes.service.requisition.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.execute.RequisitionTypeEnum;
import com.bmos.mes.common.enums.requisition.SendStatusEnum;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.bmos.mes.service.execute.dto.RecordItemLatestDataQueryDTO;
import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.convert.ProductFormulaConverter;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.print.dto.PrintBatchDTO;
import com.bmos.mes.service.platform.print.feign.PlatformTagClient;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.ProcessVersionService;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.model.StorageMaterialDetailInfo;
import com.bmos.mes.service.record.business.strategy.MaterialReserveComponentStrategy;
import com.bmos.mes.service.record.convert.RecordComponentConvert;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.requisition.convert.RequisitionPlanConverter;
import com.bmos.mes.service.requisition.dto.*;
import com.bmos.mes.service.requisition.feign.WmsFeignClient;
import com.bmos.mes.service.requisition.mapper.*;
import com.bmos.mes.service.requisition.model.*;
import com.bmos.mes.service.requisition.service.RequisitionService;
import com.bmos.mes.service.requisition.vo.*;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.service.ICargoPositionService;
import com.bmos.mes.service.storage.manage.dto.BatchReservedMaterialQueryDTO;
import com.bmos.mes.service.storage.manage.dto.InventoryMaterialInboundDTO;
import com.bmos.mes.service.storage.manage.dto.ReserveComponentReserveMaterialDTO;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialCancelReserveDTO;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.vo.BatchReservedMaterialVO;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.platform.facade.equipment.enums.TagEquipmentPropertyCodeEnum;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentPropertyFeignVO;
import com.bmos.unit.service.UnitCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RequisitionServiceImpl implements RequisitionService {

    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    private PlanService planService;

    @Autowired
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Autowired
    private ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private RequisitionMapper requisitionMapper;

    @Autowired
    private RequisitionMaterialBatchMapper requisitionMaterialBatchMapper;

    @Autowired
    private IStorageMaterialService storageMaterialService;

    @Autowired
    private WmsFeignClient wmsFeignClient;

    @Autowired
    private ProductMaterialService productMaterialService;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private BatchRecordComponentService componentService;

    @Autowired
    private Map<String, BusinessComponentStrategy> strategyMap;

    @Autowired
    private ExecuteFormDataService executeFormDataService;

    @Autowired
    private RequisitionReceivedBatchMapper requisitionReceivedBatchMapper;

    @Autowired
    private RequisitionReceivedMaterialMapper requisitionReceivedMaterialMapper;

    @Autowired
    private ICargoPositionService cargoPositionService;

    @Autowired
    private RequisitionReceivedMapper requisitionReceivedMapper;

    @Autowired
    private EquipmentConfigFeign equipmentConfigFeign;

    @Autowired
    private PlatformTagClient platformTagClient;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ReserveComponentInstanceMapper reserveComponentInstanceMapper;

    @Autowired
    private ReserveComponentMaterialMapper reserveComponentMaterialMapper;

    @Autowired
    private MaterialReserveComponentStrategy materialReserveComponentStrategy;

    @Resource
    private IStorageMaterialMapper storageMaterialMapper;

    private final String PRINT_BODY_KEY_NO = "no";

    private final Long RAW_RECEIVE_SCENE = 121001004L;

    private final Long INTERMEDIATE_RECEIVE_SCENE = 121002004L;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RequisitionPlanVO getMaterialRequisitionPlanVO(RequisitionQueryDTO dto) {
        Long procedureStepModelId = dto.getProcedureStepModelId();
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(procedureStepModelId);
        Plan plan = planService.getById(dto.getProductPlanId());
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfoByPlanId(dto.getProductPlanId());
        Requisition requisition = requisitionMapper.selectByModelAndPlanAndComponent(procedureStepModel,
                dto.getProductPlanId(),
                dto.getComponentId(), dto.getCopyVersion());
        RequisitionPlanVO result = new RequisitionPlanVO();
        result.setProductPlanId(dto.getProductPlanId());
        if (ObjectUtil.isNull(requisition)) {
            Requisition newRequisition = getExecuteRequisitionPlan(dto.getComponentId(), dto, plan, procedureStepModel);
            handleNameAndSave(newRequisition);
            result.setId(newRequisition.getId());
            result.setName(newRequisition.getName());
            result.setCompletedPlan(false);
        } else {
            result.setId(requisition.getId());
            result.setName(requisition.getName());
            result.setCompletedPlan(requisition.getCompletedPlan());
        }
        List<BusinessComponentConfigDetailVO> componentConfig =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(componentConfig,
                BusinessComponentConfigDetailVO::getComponentId);
        BusinessComponentConfigDetailVO config = configMap.get(dto.getComponentId());
        List<ProductFormulaMaterial> planMaterialList = formulaInfo.getMaterials();
        if (ObjectUtil.isNotNull(config)) {
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(),
                    ProcedureStepConfigInfo.class);
            List<Long> formulaMaterialIdList = configInfo.getFormulaMaterialIds();
            if (CollUtil.isNotEmpty(formulaMaterialIdList)) {
                planMaterialList = planMaterialList.stream()
                        .filter(e-> formulaMaterialIdList.contains(e.getId())).collect(Collectors.toList());
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

    private Requisition getExecuteRequisitionPlan(Long componentId, RequisitionQueryDTO dto, Plan plan,
                                                  ProcedureStepModel procedureStepModel) {
        Requisition requisition = new Requisition();
        requisition.setProductPlanId(plan.getId());
        requisition.setRecordItemId(procedureStepModel.getRecordItemId());
        requisition.setRecordVersionId(procedureStepModel.getRecordVersionId());
        requisition.setCopyVersion(dto.getCopyVersion());
        requisition.setBatchNo(plan.getBatchNo());
        requisition.setReuse(procedureStepModel.getReusable());
        requisition.setProcedureStepModelId(BooleanUtil.isTrue(procedureStepModel.getReusable()) ? 0 :
                procedureStepModel.getId());
        requisition.setRequisitionType(dto.getComponentType());
        requisition.setComponentId(componentId);
        return requisition;
    }

    @Override
    public void receiveRepositoryByBatch(ReceiveRepositoryByBatchDTO dto) {
        List<PrintBatchDTO> printBatchDTOS = transactionTemplate.execute(status -> {
            try {
                List<PrintBatchDTO> result = handleStorageAndData(dto);
                status.flush();
                return result;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
        if (CollUtil.isEmpty(printBatchDTOS) || dto.getDeviceId() == null) {
            return;
        }
        EquipmentInfoFeignVO printer =
                FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByEquipmentId(data),
                        dto.getDeviceId()).getData();
        if (printer == null) {
            log.error("设备id:{}不存在", dto.getDeviceId());
            return;
        }
        String ip = "";
        String port = "";
        List<EquipmentPropertyFeignVO> propertyFeignVOList = printer.getInfoPropertyList();
        for (EquipmentPropertyFeignVO equipmentPropertyFeignVO : propertyFeignVOList) {
            if (Objects.equals(equipmentPropertyFeignVO.getCode(),
                    TagEquipmentPropertyCodeEnum.IP_ADDRESS.getCode())) {
                ip = equipmentPropertyFeignVO.getValue();
            }
            if (Objects.equals(equipmentPropertyFeignVO.getCode(), TagEquipmentPropertyCodeEnum.PORT.getCode())) {
                port = equipmentPropertyFeignVO.getValue();
            }
        }
        for (int i = 0; i < printBatchDTOS.size(); i++) {
            PrintBatchDTO printBatchDTO = printBatchDTOS.get(i);
            printBatchDTO.setPrinterIp(ip);
            printBatchDTO.setPrinterPort(Integer.valueOf(port));
            try {
                platformTagClient.printBatchTags(printBatchDTO);
            } catch (Exception e) {
                log.error("领料接收-批次接收:打印失败:{}", e.getCause() + e.getMessage());
            }
        }
    }

    public List<PrintBatchDTO> handleStorageAndData(ReceiveRepositoryByBatchDTO dto) {
        Requisition requisition = requisitionMapper.selectById(dto.getRequisitionId());
        RequisitionReceived byRequisition = requisitionReceivedMapper.selectByRequisitionId(dto.getRequisitionId());
        RequisitionReceived unique = requisitionReceivedMapper.queryUniqueComponent(UniqueComponentQueryDTO
                .builder()
                .componentId(dto.getComponentId())
                .productPlanId(dto.getProductPlanId())
                .procedureStepModelId(dto.getProcedureStepModelId())
                .reuse(dto.getReuse())
                .copyVersion(dto.getCopyVersion())
                .recordVersionId(dto.getRecordVersionId())
                .recordItemId(dto.getRecordItemId())
                .build());
        if (ObjectUtil.isNotNull(byRequisition) &&
                (unique == null || ObjectUtil.notEqual(byRequisition.getId(), unique.getId()))) {
            throw new BmosException(MesResponseCode.REQUISITION_BIND_OTHER_COMPONENT);
        }
        Long productPlanId = dto.getProductPlanId();
        Plan plan = planService.getById(productPlanId);
        ProductFormulaInfo productFormulaInfo = productFormulaConfigureService.getProductFormulaInfoByPlanId(productPlanId);
        List<ProductFormulaMaterial> formulaMaterials = productFormulaInfo.getMaterials();
        Map<String, ProductFormulaMaterial> codeMap = CollectionUtils.convertMap(formulaMaterials,
                ProductFormulaMaterial::getMaterialMergeCode);
        Map<Long, ProductFormulaMaterial> idMap = CollectionUtils.convertMap(formulaMaterials,
                ProductFormulaMaterial::getMaterialId);
        List<Long> materialIds = CollectionUtils.convertList(formulaMaterials, ProductFormulaMaterial::getMaterialId);
        List<ProductMaterial> productMaterials = productMaterialService.getSubMaterialByIdList(materialIds);
        Map<String, Long> codePrincipalMaterialMap = CollectionUtils.convertMap(productMaterials,
                ProductMaterial::getMergeCode, ProductMaterial::getPrincipalMaterialId);
        List<RequisitionReceivedBatch> batchList = requisitionReceivedBatchMapper.selectBatchIds(dto.getIdList());
        List<RequisitionReceivedMaterial> materialList = requisitionReceivedMaterialMapper
                .selectByReceivedBatchIds(CollectionUtils
                        .convertList(batchList, RequisitionReceivedBatch::getId), false);
        Map<Long, List<RequisitionReceivedMaterial>> materialPartMap = CollectionUtils.convertMultiMap(materialList,
                RequisitionReceivedMaterial::getReceivedBatchId);
        List<RequisitionReceivedBatch> update =
                batchList.stream().filter(e -> e.getFormulaMaterialId() == null).collect(Collectors.toList());
        List<PrintBatchDTO> result = new ArrayList<>();
        for (RequisitionReceivedBatch batch : batchList) {
            ProductFormulaMaterial formulaMaterial = codeMap.get(batch.getCargoMergeCode());
            List<RequisitionReceivedMaterial> receivedMaterials = materialPartMap.get(batch.getId());
            if (CollUtil.isEmpty(receivedMaterials)) {
                continue;
            }
            if (ObjectUtil.isNull(formulaMaterial)) {
                Long principalMaterialId = codePrincipalMaterialMap.get(batch.getCargoMergeCode());
                formulaMaterial = idMap.get(principalMaterialId);
            }
            Long materialId = formulaMaterial.getMaterialId();
            String inventoryBatchNo = batch.getInventoryBatchNo();
            InventoryMaterialInboundDTO inventoryMaterialInboundDTO = new InventoryMaterialInboundDTO();
            inventoryMaterialInboundDTO.setMaterialId(materialId);
            inventoryMaterialInboundDTO.setReceiverId(dto.getReceiverId());
            inventoryMaterialInboundDTO.setSenderId(dto.getSenderId());
            inventoryMaterialInboundDTO.setMaterialBatchNo(inventoryBatchNo);
            inventoryMaterialInboundDTO.setMaterialPositionId(dto.getCargoPositionId());
            inventoryMaterialInboundDTO.setOriginalBatchNo(batch.getFactoryBatchNo());
            inventoryMaterialInboundDTO.setExpiredDate(batch.getExpiredDate());
            inventoryMaterialInboundDTO.setLicenceNo(batch.getLicenceNo());
            inventoryMaterialInboundDTO.setReportNo(batch.getReportNo());
            inventoryMaterialInboundDTO.setUnitId(batch.getUnitId());
            inventoryMaterialInboundDTO.setProductName(plan.getProductName());
            inventoryMaterialInboundDTO.setProductCode(plan.getProductMergeCode());
            inventoryMaterialInboundDTO.setProductBatchNo(plan.getBatchNo());
            List<InventoryMaterialInboundDTO.MaterialInboundDTO> collect = receivedMaterials.stream().map(e -> {
                InventoryMaterialInboundDTO.MaterialInboundDTO materialInboundDTO =
                        new InventoryMaterialInboundDTO.MaterialInboundDTO();
                materialInboundDTO.setQuantity(e.getQuantity());
                materialInboundDTO.setUnitId(e.getUnitId());
                materialInboundDTO.setInventoryNo(e.getInventoryNo());
                return materialInboundDTO;
            }).collect(Collectors.toList());
            inventoryMaterialInboundDTO.setInboundList(collect);
            batch.setFormulaMaterialId(formulaMaterial.getId());
            List<StorageMaterial> storageMaterials =
                    storageMaterialService.inventoryMaterialInbound(inventoryMaterialInboundDTO, plan);
            // 根据物料类型打印物料件标签
            CategoryInfoTypeEnum materialType = formulaMaterial.getMaterialType();
            PrintBatchDTO printBatchDTO = new PrintBatchDTO();
            printBatchDTO.setSceneId(materialType.equals(CategoryInfoTypeEnum.RAW_MATERIAL) ? RAW_RECEIVE_SCENE :
                    INTERMEDIATE_RECEIVE_SCENE);
            printBatchDTO.setBodyList(storageMaterials.stream().map(e -> {
                HashMap<String, Object> map = new HashMap<>();
                map.put(PRINT_BODY_KEY_NO, e.getNo());
                return map;
            }).collect(Collectors.toList()));
            if (CollUtil.isNotEmpty(printBatchDTO.getBodyList())) {
                result.add(printBatchDTO);
            }
        }
        // 更新领料单绑定领料接收组件
        if (!requisitionReceivedMapper.existsBoundRequisition(requisition.getId())) {
            RequisitionReceived received = RequisitionPlanConverter.INSTANCE.convertToRequisitionReceived(dto);
            requisitionReceivedMapper.insert(received);
            requisition.setReceivedId(received.getId());
            requisitionMapper.updateById(requisition);
        }
        if (CollUtil.isNotEmpty(materialList)) {
            materialList.forEach(e -> e.setCargoPositionId(dto.getCargoPositionId()));
            requisitionReceivedMaterialMapper.updateBatch(materialList);
        }
        // 更新批次绑定配方物料
        if (CollUtil.isNotEmpty(update)) {
            requisitionReceivedBatchMapper.updateBatch(update);
        }
        // 处理业务组件数据
        if (CollUtil.isNotEmpty(result)) {
            ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
            dto.setReuse(procedureStepModel.getReusable());
            handleReceiveBusinessData(dto, procedureStepModel, dto.getRequisitionId());
        }
        return result;
    }

    /**
     * 处理按批次/按物料件 领料接受组件数据并保存
     *
     * @param businessSaveDTO
     * @param procedureStepModel
     */
    private void handleReceiveBusinessData(BusinessDataHandleBaseDTO businessSaveDTO,
                                           ProcedureStepModel procedureStepModel, Long requisitionId) {
        List<RequisitionReceivedBatchInfo> list = requisitionReceivedBatchMapper.selectReceivedBatchInfo(requisitionId);
        List<ExecuteFormData> results = new ArrayList<>();
        ProductionDetailInfo info = new ProductionDetailInfo();
        info.setDto(RecordComponentConvert.INSTANCE.convertToBusinessComponentBatchSaveDTO(businessSaveDTO));
        info.setUnitCache(unitCache);
        info.setRequisitionReceivedBatchList(list);
        // 查询已存在数据
        ComponentListVO receivedComponent =
                componentService.selectUsedComponentDetail(businessSaveDTO.getRecordVersionId(),
                        businessSaveDTO.getRecordItemId(), businessSaveDTO.getComponentId());
        ProductFormulaInfo formulaInfo = productFormulaConfigureService.getProductFormulaInfoByPlanId(businessSaveDTO.getProductPlanId());

        RecordItemLatestDataQueryDTO queryDTO = getRecordItemLatestDataQueryDTO(businessSaveDTO, receivedComponent);
        List<FormDataItemVO> recordItemLatestData = executeFormDataService.getRecordItemLatestData(queryDTO);
        info.setFormulaInfo(formulaInfo);
        info.setFormDataCollection(recordItemLatestData.stream().filter(e-> e.getValue() != null).collect(Collectors.toList()));
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        strategyMap.get(receivedComponent.getComponentType()).handleBusinessComponent(results, receivedComponent,
                info, configMap, null);
        executeFormDataService.saveResultsAndHandleRelationComponentData(results, businessSaveDTO);
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

    @Override
    public List<RequisitionListVO> getRequisitionList(Long batchId) {
        List<Requisition> list = requisitionMapper.selectByPlanId(batchId, SendStatusEnum.COMPLETED_SEND);
        List<RequisitionListVO> result = list.stream().map(r -> {
            RequisitionListVO vo = new RequisitionListVO();
            vo.setId(r.getId());
            vo.setName(r.getName());
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public void receiveRepositoryByMaterial(ReceiveRepositoryByMaterialDTO dto) {
        PrintBatchDTO printBatchDTO = transactionTemplate.execute(status -> {
            try {
                PrintBatchDTO result = handleReceiveMaterialStorageAndData(dto);
                status.flush();
                return result;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
        if (printBatchDTO == null || CollUtil.isEmpty(printBatchDTO.getBodyList()) || dto.getDeviceId() == null) {
            return;
        }
        try {
            EquipmentInfoFeignVO printer =
                    FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByEquipmentId(data),
                            dto.getDeviceId()).getData();
            if (printer == null) {
                log.error("设备id:{}不存在", dto.getDeviceId());
                return;
            }
            String ip = "";
            String port = "";
            List<EquipmentPropertyFeignVO> propertyFeignVOList = printer.getInfoPropertyList();
            for (EquipmentPropertyFeignVO equipmentPropertyFeignVO : propertyFeignVOList) {
                if (Objects.equals(equipmentPropertyFeignVO.getCode(),
                        TagEquipmentPropertyCodeEnum.IP_ADDRESS.getCode())) {
                    ip = equipmentPropertyFeignVO.getValue();
                }
                if (Objects.equals(equipmentPropertyFeignVO.getCode(), TagEquipmentPropertyCodeEnum.PORT.getCode())) {
                    port = equipmentPropertyFeignVO.getValue();
                }
            }
            printBatchDTO.setPrinterIp(ip);
            printBatchDTO.setPrinterPort(Integer.valueOf(port));
            platformTagClient.printBatchTags(printBatchDTO);
        } catch (Exception e) {
            log.error("领料接收-物料件接收:打印失败:{}", e.getCause() + e.getMessage());
        }
    }

    public PrintBatchDTO handleReceiveMaterialStorageAndData(ReceiveRepositoryByMaterialDTO dto) {
        Requisition requisition = requisitionMapper.selectById(dto.getRequisitionId());
        RequisitionReceived byRequisition = requisitionReceivedMapper.selectByRequisitionId(dto.getRequisitionId());
        RequisitionReceived unique = requisitionReceivedMapper.queryUniqueComponent(UniqueComponentQueryDTO
                .builder()
                .componentId(dto.getComponentId())
                .productPlanId(dto.getProductPlanId())
                .procedureStepModelId(dto.getProcedureStepModelId())
                .reuse(dto.getReuse())
                .copyVersion(dto.getCopyVersion())
                .recordVersionId(dto.getRecordVersionId())
                .recordItemId(dto.getRecordItemId())
                .build());
        if (ObjectUtil.isNotNull(byRequisition) &&
                (unique == null || ObjectUtil.notEqual(byRequisition.getId(), unique.getId()))) {
            throw new BmosException(MesResponseCode.REQUISITION_BIND_OTHER_COMPONENT);
        }
        Long materialBatchId = dto.getMaterialBatchId();
        RequisitionReceivedBatch batch = requisitionReceivedBatchMapper.selectById(materialBatchId);
        if (ObjectUtil.isNull(batch)) {
            throw new BmosException(MesResponseCode.RECEIVED_INVENTORY_BATCH_NOT_EXISTS);
        }
        Long productPlanId = dto.getProductPlanId();
        Plan plan = planService.getById(productPlanId);
        ProductFormulaInfo formulaInfo = productFormulaConfigureService.getProductFormulaInfoByPlanId(productPlanId);
        List<ProductFormulaMaterial> formulaMaterials = formulaInfo.getMaterials();
        List<RequisitionReceivedMaterial> receivedMaterials =
                requisitionReceivedMaterialMapper.selectBatchIds(dto.getIdList());
        Map<String, ProductFormulaMaterial> codeMap = CollectionUtils.convertMap(formulaMaterials,
                ProductFormulaMaterial::getMaterialMergeCode);
        InventoryMaterialInboundDTO inboundDTO = new InventoryMaterialInboundDTO();
        ProductFormulaMaterial formulaMaterial = codeMap.get(batch.getCargoMergeCode());
        if (ObjectUtil.isNull(formulaMaterial)) {
            List<ProductMaterial> subMaterialByIdList = productMaterialService.getSubMaterialByIdList(CollectionUtils
                    .convertList(formulaMaterials, ProductFormulaMaterial::getMaterialId));
            Map<String, Long> principalMap = CollectionUtils.convertMap(subMaterialByIdList, ProductMaterial::getMergeCode, ProductMaterial::getPrincipalMaterialId);
            Long materialId = principalMap.get(batch.getCargoMergeCode());
            formulaMaterial = formulaInfo.getMaterialIdMap().get(materialId);
            if (formulaMaterial == null){
                throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
            }
        }
        inboundDTO.setMaterialId(formulaMaterial.getMaterialId());
        inboundDTO.setReceiverId(dto.getReceiverId());
        inboundDTO.setSenderId(dto.getSenderId());
        inboundDTO.setMaterialPositionId(dto.getCargoPositionId());
        inboundDTO.setUnitId(batch.getUnitId());
        inboundDTO.setMaterialBatchNo(batch.getInventoryBatchNo());
        inboundDTO.setExpiredDate(batch.getExpiredDate());
        inboundDTO.setProductName(plan.getProductName());
        inboundDTO.setProductCode(plan.getProductMergeCode());
        inboundDTO.setProductBatchNo(plan.getBatchNo());
        List<RequisitionReceivedMaterial> needHandle =
                receivedMaterials.stream().filter(e -> e.getCargoPositionId() == null).collect(Collectors.toList());
        needHandle.forEach(e -> e.setCargoPositionId(dto.getCargoPositionId()));
        List<InventoryMaterialInboundDTO.MaterialInboundDTO> collect = needHandle.stream().map(e -> {
            InventoryMaterialInboundDTO.MaterialInboundDTO materialInboundDTO =
                    new InventoryMaterialInboundDTO.MaterialInboundDTO();
            materialInboundDTO.setQuantity(e.getQuantity());
            materialInboundDTO.setUnitId(e.getUnitId());
            materialInboundDTO.setInventoryNo(e.getInventoryNo());
            return materialInboundDTO;
        }).collect(Collectors.toList());
        inboundDTO.setInboundList(collect);
        List<StorageMaterial> storageMaterials = storageMaterialService.inventoryMaterialInbound(inboundDTO, plan);
        if(CollUtil.isEmpty(storageMaterials)){
            return null;
        }
        // 更新领料单绑定领料接收组件
        if (!requisitionReceivedMapper.existsBoundRequisition(requisition.getId())) {
            RequisitionReceived received = RequisitionPlanConverter.INSTANCE.convertToRequisitionReceived(dto);
            requisitionReceivedMapper.insert(received);
            requisition.setReceivedId(received.getId());
            requisitionMapper.updateById(requisition);
        }
        requisitionReceivedMaterialMapper.updateBatch(receivedMaterials);
        if (batch.getFormulaMaterialId() == null) {
            batch.setFormulaMaterialId(formulaMaterial.getId());
            requisitionReceivedBatchMapper.updateById(batch);
        }
        // 处理业务组件数据
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        dto.setReuse(procedureStepModel.getReusable());
        handleReceiveBusinessData(dto, procedureStepModel, dto.getRequisitionId());
        // 返回打印标签参数
        CategoryInfoTypeEnum materialType = formulaMaterial.getMaterialType();
        PrintBatchDTO printBatchDTO = new PrintBatchDTO();
        printBatchDTO.setSceneId(materialType.equals(CategoryInfoTypeEnum.RAW_MATERIAL) ? RAW_RECEIVE_SCENE :
                INTERMEDIATE_RECEIVE_SCENE);
        printBatchDTO.setBodyList(storageMaterials.stream().map(e -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put(PRINT_BODY_KEY_NO, e.getNo());
            return map;
        }).collect(Collectors.toList()));
        return printBatchDTO;
    }

    @Override
    public List<RepositoryBatchMaterialListVO> getRepositoryBatchMaterialList(RepositoryBatchMaterialQueryDTO dto) {
        Requisition requisition = requisitionMapper.selectById(dto.getRequisitionId());
        if (requisition == null) {
            throw new BmosException(MesResponseCode.REQUISITION_PLAN_NOT_EXISTS);
        }
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfoByPlanId(requisition.getProductPlanId());
        List<ProductFormulaMaterial> formulaMaterials = formulaInfo.getMaterials();
        Map<String, ProductFormulaMaterial> formulaMaterialCodeMap = CollectionUtils.convertMap(formulaMaterials,
                ProductFormulaMaterial::getMaterialMergeCode);
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = CollectionUtils.convertMap(formulaMaterials,
                ProductFormulaMaterial::getMaterialId);
        List<ProductMaterial> subMaterialList =
                productMaterialService.getSubMaterialByIdList(CollectionUtils.convertList(formulaMaterials,
                        ProductFormulaMaterial::getMaterialId));
        Map<String, Long> principalMaterialMap = CollectionUtils.convertMap(subMaterialList,
                ProductMaterial::getMergeCode, ProductMaterial::getPrincipalMaterialId);

        RequisitionReceivedBatch batch = requisitionReceivedBatchMapper.selectById(dto.getReceivedBatchId());

        ProductFormulaMaterial formulaMaterial = formulaMaterialCodeMap.get(batch.getCargoMergeCode());
        if (formulaMaterial == null) {
            Long materialId = principalMaterialMap.get(batch.getCargoMergeCode());
            formulaMaterial = formulaMaterialMap.get(materialId);
        }
        dto.setMaterialId(formulaMaterial.getMaterialId());
        List<RepositoryBatchMaterialListVO> result =
                requisitionReceivedMaterialMapper.selectRepositoryBatchMaterialList(dto);
        List<CargoPosition> cargoPositions =
                cargoPositionService.getByIdList(CollectionUtils.convertList(result,
                        RepositoryBatchMaterialListVO::getCargoPositionId));
        Map<Long, CargoPosition> cargoMap = CollectionUtils.convertMap(cargoPositions, CargoPosition::getId);
        ProductFormulaMaterial finalFormulaMaterial = formulaMaterial;
        result.forEach(e -> {
            BigDecimal convert = unitCache.convert(e.getQuantity(), e.getUnitId(), finalFormulaMaterial.getUnitId());
            e.setQuantity(MaterialQuantityCalculateUtil.roundingOff(convert, finalFormulaMaterial));
            e.setUnitId(finalFormulaMaterial.getUnitId());
            e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId()));
            e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId()));
            if (e.getCargoPositionId() != null) {
                CargoPosition cargoPosition = cargoMap.get(e.getCargoPositionId());
                e.setCargoPositionName(cargoPosition.getCode() + StrUtil.DASHED + cargoPosition.getPosition());
            }
        });
        return result;
    }

    @Override
    public List<RepositoryMaterialBatchListVO> getRepositoryMaterialBatch(RepositoryBatchQueryDTO dto) {
        Long formulaMaterialId = dto.getFormulaMaterialId();
        ProductFormulaMaterial formulaMaterial =
                productFormulaConfigureService.getFormulaMaterialById(formulaMaterialId);
        Long materialId = formulaMaterial.getMaterialId();
        ProductMaterial productMaterial = productMaterialService.selectById(materialId);
        InventoryBatchQueryDTO queryDTO = new InventoryBatchQueryDTO();
        List<Long> ids = new ArrayList<>();
        List<String> codes = new ArrayList<>();
        ids.add(productMaterial.getPlatformMaterialId());
        codes.add(productMaterial.getMergeCode());
        queryDTO.setMaterialPlatformIdList(ids);
        queryDTO.setMaterialMergeCodeList(codes);
        if (BooleanUtil.isFalse(productMaterial.getSubMaterial())) {
            List<ProductMaterial> subMaterialList = productMaterialService.getSubMaterial(materialId);
            ids.addAll(CollectionUtils.convertList(subMaterialList, ProductMaterial::getPlatformMaterialId));
            codes.addAll(CollectionUtils.convertList(subMaterialList, ProductMaterial::getMergeCode));
        }
        List<InventoryBatchListVO> list = FeignUtils.handleRequest(data -> wmsFeignClient.queryBatchByMaterial(data),
                queryDTO).getData();
        list.forEach(e -> {
            e.setUnitName(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
            BigDecimal convert = unitCache.toExt(e.getAvailableQuantity(),
                    formulaMaterial.getUnitId());
            e.setAvailableQuantity(MaterialQuantityCalculateUtil.roundingOff(convert, formulaMaterial));
            e.setUnitId(formulaMaterial.getUnitId());
        });
        List<RepositoryMaterialBatchListVO> result =
                RequisitionPlanConverter.INSTANCE.convertToRepositoryMaterialBatchList(list);
        // 处理已预订
        if (ObjectUtil.isNotNull(dto.getRequisitionPlanId())) {
            List<RequisitionMaterialReserved> reserveds =
                    requisitionMaterialBatchMapper.getByRequisitionAndMaterial(dto.getRequisitionPlanId(),
                            dto.getFormulaMaterialId());
            Map<Long, RequisitionMaterialReserved> reservedMap = CollectionUtils.convertMap(reserveds,
                    RequisitionMaterialReserved::getMaterialBatchId);
            Set<Long> batchIds = CollectionUtils.convertSet(reserveds, RequisitionMaterialReserved::getMaterialBatchId);
            List<RepositoryMaterialBatchListVO> inDb =
                    result.stream().filter(e -> batchIds.contains(e.getId())).collect(Collectors.toList());
            inDb.forEach(vo -> {
                vo.setReserved(true);
                vo.setPlannedQuantity(MaterialQuantityCalculateUtil.roundingOff(reservedMap.get(vo.getId()).getPlannedQuantity(), formulaMaterial));
                vo.setTheoreticalQuantity(MaterialQuantityCalculateUtil.roundingOff(reservedMap.get(vo.getId()).getTheoreticalQuantity(), formulaMaterial));
            });
        }
        return result;
    }

    @Override
    public RepositoryReservedBatchVO getRepositoryReservedBatch(RepositoryReservedBatchQueryDto dto) {
        Requisition requisition = requisitionMapper.selectById(dto.getRequisitionPlanId());
        if (requisition == null) {
            throw new BmosException(MesResponseCode.REQUISITION_PLAN_NOT_EXISTS);
        }
        BatchReservedMaterialQueryDTO batchReservedMaterialQueryDTO = new BatchReservedMaterialQueryDTO();
        ProductFormulaMaterial formulaMaterial =
                productFormulaConfigureService.getFormulaMaterialById(dto.getFormulaMaterialId());
        if (formulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        batchReservedMaterialQueryDTO.setFormulaMaterialId(dto.getFormulaMaterialId());
        batchReservedMaterialQueryDTO.setProductPlanId(requisition.getProductPlanId());
        batchReservedMaterialQueryDTO.setMaterialId(formulaMaterial.getMaterialId());
        List<BatchReservedMaterialVO> batchReservedMaterial =
                storageMaterialService.getBatchReservedMaterial(batchReservedMaterialQueryDTO);
        BigDecimal storageTheoreticalQuantity =
                batchReservedMaterial.stream().map(BatchReservedMaterialVO::getTheoreticalQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
        RepositoryReservedBatchVO result = new RepositoryReservedBatchVO();
        Long requisitionPlanId = dto.getRequisitionPlanId();
        List<RequisitionMaterialReserved> list =
                requisitionMaterialBatchMapper.getByRequisitionAndMaterial(requisitionPlanId,
                        dto.getFormulaMaterialId());
        List<RepositoryMaterialReservedBatchListVO> batchList =
                RequisitionPlanConverter.INSTANCE.convertToRepositoryMaterialReservedBatch(list);
        batchList.forEach(e -> {
            e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId()));
            e.setPlannedQuantity(MaterialQuantityCalculateUtil.roundingOff(e.getPlannedQuantity(), formulaMaterial));
            e.setTheoreticalQuantity(MaterialQuantityCalculateUtil.roundingOff(e.getTheoreticalQuantity(),
                    formulaMaterial));
        });
        result.setStorageTheoreticalQuantity(storageTheoreticalQuantity);
        result.setTotalPlannedQuantity(batchList.stream().map(RepositoryMaterialReservedBatchListVO::getPlannedQuantity).reduce(BigDecimal.ZERO, BigDecimal::add));
        result.setTotalTheoreticalQuantity(batchList.stream().map(RepositoryMaterialReservedBatchListVO::getTheoreticalQuantity).reduce(BigDecimal.ZERO, BigDecimal::add));
        result.setBatchList(batchList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeRequisitionPlan(RequisitionCompleteDTO dto) {
        Requisition requisition = requisitionMapper.selectById(dto.getRequisitionPlanId());
        if (BooleanUtil.isTrue(requisition.getCompletedPlan())) {
            throw new BmosException(MesResponseCode.REQUISITION_PLAN_COMPLETED);
        }
        List<RequisitionMaterialReserved> requisitionMaterialList =
                requisitionMaterialBatchMapper.getByRequisitionId(requisition.getId());
        if (CollUtil.isEmpty(requisitionMaterialList)) {
            throw new BmosException(MesResponseCode.NO_REQUISITION_PLAN_INFO);
        }
        requisition.setCompletedPlan(true);
        requisitionMapper.updateById(requisition);
        // 处理业务组件数据
        ComponentListVO requisitionComponent = componentService.selectUsedComponentDetail(dto.getRecordVersionId(),
                dto.getRecordItemId(), dto.getComponentId());
        // 处理业务数据
        List<ExecuteFormData> results = new ArrayList<>();
        ProductionDetailInfo info = new ProductionDetailInfo();
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        dto.setReuse(procedureStepModel.getReusable());
        info.setDto(RecordComponentConvert.INSTANCE.convertToBusinessComponentBatchSaveDTO(dto));
        info.setUnitCache(unitCache);
        info.setRepositoryReservedList(requisitionMaterialList);
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfoByPlanId(dto.getProductPlanId());
        info.setFormulaInfo(formulaInfo);
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        strategyMap.get(requisitionComponent.getComponentType()).handleBusinessComponent(results,
                requisitionComponent, info, configMap, null);
        results.forEach(e->e.setOperationUser(dto.getOperatorId()));
        executeFormDataService.saveResultsAndHandleRelationComponentData(results, dto);
        // 向wms发送领料信息
        SendSubmitDTO sendSubmitDTO = getSendSubmitDTO(dto, requisitionMaterialList, requisition);
        FeignUtils.handleRequest(data -> wmsFeignClient.submitSendOutOrderByBatch(data), sendSubmitDTO);
    }

    private SendSubmitDTO getSendSubmitDTO(RequisitionCompleteDTO dto,
                                           List<RequisitionMaterialReserved> requisitionMaterialList,
                                           Requisition requisition) {
        SendSubmitDTO sendSubmitDTO = new SendSubmitDTO();
        sendSubmitDTO.setBatchNo(dto.getBatchNo());
        sendSubmitDTO.setSubmitterId(Long.valueOf(SysUserHolder.getUser().getUserId()));
        sendSubmitDTO.setProcessId(dto.getProcessId());
        sendSubmitDTO.setRequisitionPlanId(dto.getRequisitionPlanId());
        sendSubmitDTO.setPullOrderNo(requisition.getName());
        Plan plan = planService.getById(dto.getProductPlanId());
        sendSubmitDTO.setProductName(plan.getProductName());
        sendSubmitDTO.setProcessName(plan.getProcessName());
        sendSubmitDTO.setPendingSendList(requisitionMaterialList.stream().map(m -> {
            SendSubmitQuantityDTO quantityDTO = new SendSubmitQuantityDTO();
            Long businessId = ObjectUtil.equal(requisition.getRequisitionType(),
                    RequisitionTypeEnum.BATCH_QUANTITY_PICK.getValue())
                    ? m.getMaterialBatchId() : m.getWmsMaterialId();
            quantityDTO.setBusinessId(businessId);
            quantityDTO.setTargetQuantity(m.getPlannedQuantity());
            quantityDTO.setUnitId(m.getUnitId());
            return quantityDTO;
        }).collect(Collectors.toList()));
        sendSubmitDTO.setProductCode(plan.getProductMergeCode());
        sendSubmitDTO.setProductId(plan.getProductId());
        sendSubmitDTO.setProductSpecification(plan.getProductSpecification());
        sendSubmitDTO.setSendOrderType(RequisitionTypeEnum.getByValue(requisition.getRequisitionType()).getMappingValue());
        return sendSubmitDTO;
    }

    @Override
    public List<InventoryAvailableQuantityListVO> getRepositoryMaterialQuantityList(RepositoryQuantityQueryDTO dto) {
        ProductFormulaMaterial formulaMaterial =
                productFormulaConfigureService.getFormulaMaterialById(dto.getFormulaMaterialId());
        if (ObjectUtil.isNull(formulaMaterial)) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        Long materialId = formulaMaterial.getMaterialId();
        ProductMaterial material = productMaterialService.selectById(materialId);
        List<Long> ids = new ArrayList<>();
        List<String> codes = new ArrayList<>();
        ids.add(material.getPlatformMaterialId());
        codes.add(material.getMergeCode());
        InventoryAvailableQuantityQueryDTO queryDto = new InventoryAvailableQuantityQueryDTO();
        queryDto.setMaterialMergeCodeList(codes);
        queryDto.setMaterialPlatformIdList(ids);
        if (BooleanUtil.isFalse(material.getSubMaterial())) {
            List<ProductMaterial> subMaterialList = productMaterialService.getSubMaterial(materialId);
            ids.addAll(CollectionUtils.convertList(subMaterialList, ProductMaterial::getPlatformMaterialId));
            codes.addAll(CollectionUtils.convertList(subMaterialList, ProductMaterial::getMergeCode));
        }
        List<InventoryAvailableQuantityListVO> res =
                FeignUtils.handleRequest(data -> wmsFeignClient.queryAvailableQuantityList(data), queryDto).getData();
        Long unitId = formulaMaterial.getUnitId();
        String unitName = unitCache.getGlobalUnitName(unitId);
        res.forEach(e -> {
            BigDecimal convert = unitCache.convert(e.getInventoryQuantity(),
                    unitCache.getBaseUnitId(formulaMaterial.getUnitId()), formulaMaterial.getUnitId());
            e.setInventoryQuantity(MaterialQuantityCalculateUtil.roundingOff(convert, formulaMaterial));
            e.setUnitId(unitId);
            e.setUnitName(unitName);
        });
        List<RequisitionMaterialReserved> reservedList =
                requisitionMaterialBatchMapper.getByRequisitionId(dto.getRequisitionPlanId());
        if (CollUtil.isNotEmpty(reservedList)) {
            Map<Long, RequisitionMaterialReserved> reservedMap = CollectionUtils.convertMap(reservedList,
                    RequisitionMaterialReserved::getWmsMaterialId);
            res.forEach(e -> {
                if (ObjectUtil.isNotNull(reservedMap.get(e.getId()))) {
                    e.setReservedQuantity(MaterialQuantityCalculateUtil.roundingOff(reservedMap.get(e.getId()).getPlannedQuantity(), formulaMaterial));
                    e.setReserved(reservedMap.get(e.getId()) != null);
                }
            });
        }
        return res;
    }

    @Override
    public void cancelReservedSingle(StorageMaterialCancelReservedSingleDto dto) {
        StorageMaterialCancelReserveDTO cancelDTO = new StorageMaterialCancelReserveDTO();
        Long batchId = dto.getBatchId();
        Plan plan = planService.getById(batchId);
        Long storageMaterialId = dto.getStorageMaterialId();
        Long processId = dto.getProcessId();
        cancelDTO.setOperatorId(SysUserHolder.getUser().getUserId());
        cancelDTO.setReCheckerId(SysUserHolder.getUser().getUserId());
        cancelDTO.setStorageMaterialIdList(Collections.singletonList(storageMaterialId));
        cancelDTO.setProductId(plan.getProductId());
        cancelDTO.setProcessId(processId);
        cancelDTO.setBatchId(batchId);
        storageMaterialService.cancelReserve(cancelDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reserveRepositoryMaterial(ReserveRepositoryMaterialDTO dto) {
        Requisition requisition = requisitionMapper.selectById(dto.getRequisitionPlanId());
        if (BooleanUtil.isTrue(requisition.getCompletedPlan())) {
            throw new BmosException(MesResponseCode.REQUISITION_PLAN_COMPLETED);
        }
        boolean planZero = dto.getMaterialReservedList().stream().anyMatch(e -> BigDecimal.ZERO.compareTo(e.getPlannedQuantity()) == 0);
        if (planZero) {
            throw new BmosException(MesResponseCode.PLAN_QUANTITY_CANT_BE_ZERO);
        }
        requisitionMaterialBatchMapper.cancelReservedMaterial(dto.getRequisitionPlanId(), dto.getFormulaMaterialId());
        ProductFormulaMaterial formulaMaterial =
                productFormulaConfigureService.getFormulaMaterialById(dto.getFormulaMaterialId());
        String requisitionType = requisition.getRequisitionType();
        List<RequisitionMaterialReserved> insertList = new ArrayList<>();
        List<RequisitionCompleteReservedDTO> materialBatchList = dto.getMaterialReservedList();
        if (ObjectUtil.equal(requisitionType, RequisitionTypeEnum.BATCH_QUANTITY_PICK.getValue())) {
            for (RequisitionCompleteReservedDTO batch : materialBatchList) {
                RequisitionMaterialReserved insert = new RequisitionMaterialReserved();
                insert.setRequisitionPlanId(dto.getRequisitionPlanId());
                insert.setPlannedQuantity(batch.getPlannedQuantity());
                insert.setFormulaMaterialId(dto.getFormulaMaterialId());
                insert.setUserId(SysUserHolder.getUser().getUserId());
                insert.setUnitId(formulaMaterial.getUnitId());
                insert.setExpiredDate(batch.getExpiredDate());
                insert.setMaterialBatchId(batch.getId());
                insert.setMaterialBatchNo(batch.getMaterialBatchNo());
                insert.setTheoreticalQuantity(batch.getTheoreticalQuantity());
                insert.setExpiredDate(batch.getExpiredDate());
                insert.setMergeCode(batch.getMergeCode());
                insert.setSupplier(batch.getSupplier());
                insert.setOriginBatchNo(batch.getOriginBatchNo());
                insert.setProducer(batch.getProducer());
                insert.setMaterialName(batch.getMaterialName());
                insert.setMergeCode(batch.getMergeCode());
                insert.setSpecification(batch.getSpecification());
                insert.setHydration(batch.getHydration());
                insert.setNoHydrationContent(batch.getNoHydrationContent());
                insertList.add(insert);
            }
        } else {
            for (RequisitionCompleteReservedDTO reserved : materialBatchList) {
                RequisitionMaterialReserved insert = new RequisitionMaterialReserved();
                insert.setPlannedQuantity(reserved.getPlannedQuantity());
                insert.setSpecification(reserved.getSpecification());
                insert.setWmsMaterialId(reserved.getId());
                insert.setUserId(SysUserHolder.getUser().getUserId());
                insert.setFormulaMaterialId(dto.getFormulaMaterialId());
                insert.setRequisitionPlanId(dto.getRequisitionPlanId());
                insert.setTheoreticalQuantity(reserved.getTheoreticalQuantity());
                insert.setUnitId(reserved.getUnitId());
                insert.setMergeCode(reserved.getMergeCode());
                insert.setMaterialName(reserved.getMaterialName());
                insert.setProducer(reserved.getSupplier());
                insert.setSupplier(reserved.getSupplier());
                insertList.add(insert);
            }
        }
        requisitionMaterialBatchMapper.insertBatch(insertList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelRepositoryMaterial(CancelRepositoryMaterialDTO dto) {
        requisitionMaterialBatchMapper.deleteById(dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelSendOut(Long requisitionPlanId) {
        Requisition requisition = requisitionMapper.selectById(requisitionPlanId);
        if (ObjectUtil.isNull(requisition)) {
            throw new BmosException(MesResponseCode.REQUISITION_PLAN_NOT_EXISTS);
        }
        requisition.setSendStatus(SendStatusEnum.CANCEL_SEND.getValue());
        requisitionMapper.updateById(requisition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(expression = "#dto.requisitionPlanId")
    public void sendOut(SendOutFeignDTO dto) {
        Requisition requisition = requisitionMapper.selectById(dto.getRequisitionPlanId());
        if (ObjectUtil.isNull(requisition)) {
            throw new BmosException(MesResponseCode.REQUISITION_PLAN_NOT_EXISTS);
        }
        if (Objects.equals(requisition.getSendStatus(), SendStatusEnum.COMPLETED_SEND.getValue())) {
            throw new BmosException(MesResponseCode.REQUISITION_PLAN_COMPLETED_SEND);
        }
        // 处理批次信息
        List<SendOutFeignDTO.SendOutBatch> sendOutBatchList = dto.getSendOutBatchList();
        List<RequisitionReceivedBatch> batchList = new ArrayList<>();
        List<RequisitionReceivedMaterial> materialList = new ArrayList<>();
        for (SendOutFeignDTO.SendOutBatch sendOutBatch : sendOutBatchList) {
            RequisitionReceivedBatch batch =
                    RequisitionPlanConverter.INSTANCE.convertToRequisitionReceivedBatch(sendOutBatch);
            batch.setRequisitionPlanId(dto.getRequisitionPlanId());
            batchList.add(batch);
            batch.setUnitId(unitCache.getBaseUnitId(batch.getUnitId()));
            List<SendOutFeignDTO.SendOutInventory> inventories = sendOutBatch.getInventories();
            for (SendOutFeignDTO.SendOutInventory inventory : inventories) {
                RequisitionReceivedMaterial material =
                        RequisitionPlanConverter.INSTANCE.convertToRequisitionReceivedMaterial(inventory);
                material.setRequisitionPlanId(dto.getRequisitionPlanId());
                material.setInventoryBatchId(batch.getInventoryBatchId());
                // 因为发料的量为标准量但是单位与量非对应关系 所以需要转换
                material.setUnitId(unitCache.getBaseUnitId(material.getUnitId()));
                materialList.add(material);
            }
        }
        requisitionReceivedBatchMapper.insertBatch(batchList);
        Map<Long, RequisitionReceivedBatch> idMap = CollectionUtils.convertMap(batchList,
                RequisitionReceivedBatch::getInventoryBatchId);
        for (RequisitionReceivedMaterial material : materialList) {
            material.setReceivedBatchId(idMap.get(material.getInventoryBatchId()).getId());
        }
        requisitionReceivedMaterialMapper.insertBatch(materialList);
        requisition.setSendStatus(SendStatusEnum.COMPLETED_SEND.getValue());
        requisitionMapper.updateById(requisition);
    }

    @Override
    public List<RequisitionMaterialBatchVO> getRequisitionMaterialBatchList(Long requisitionId) {
        Requisition requisition = requisitionMapper.selectById(requisitionId);
        if (requisition == null) {
            throw new BmosException(MesResponseCode.REQUISITION_PLAN_NOT_EXISTS);
        }
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfoByPlanId(requisition.getProductPlanId());
        List<ProductFormulaMaterial> formulaMaterials = formulaInfo.getMaterials();
        Map<String, ProductFormulaMaterial> formulaMaterialCodeMap = CollectionUtils.convertMap(formulaMaterials,
                ProductFormulaMaterial::getMaterialMergeCode);
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = CollectionUtils.convertMap(formulaMaterials,
                ProductFormulaMaterial::getMaterialId);
        List<ProductMaterial> subMaterialList =
                productMaterialService.getSubMaterialByIdList(CollectionUtils.convertList(formulaMaterials,
                        ProductFormulaMaterial::getMaterialId));
        Map<String, Long> codePrincipalMaterialMap = CollectionUtils.convertMap(subMaterialList,
                ProductMaterial::getMergeCode, ProductMaterial::getPrincipalMaterialId);
        List<RequisitionMaterialBatchVO> result = requisitionReceivedBatchMapper.selectByRequisitionId(requisitionId);
        List<CargoPosition> cargoPositions = cargoPositionService.getByIdList(CollectionUtils.convertList(result,
                RequisitionMaterialBatchVO::getCargoPositionId));
        Map<Long, CargoPosition> cargoMap = CollectionUtils.convertMap(cargoPositions, CargoPosition::getId);
        result.forEach(e -> {
            ProductFormulaMaterial formulaMaterial = formulaMaterialCodeMap.get(e.getMaterialMergeCode());
            if (formulaMaterial == null) {
                Long materialId = codePrincipalMaterialMap.get(e.getMaterialMergeCode());
                formulaMaterial = formulaMaterialMap.get(materialId);
            }
            BigDecimal convert = unitCache.convert(e.getOutboundQuantity(), e.getUnitId(), formulaMaterial.getUnitId());
            e.setOutboundQuantity(MaterialQuantityCalculateUtil.roundingOff(convert, formulaMaterial));
            e.setUnitId(formulaMaterial.getUnitId());
            e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId()));
            if (e.getCargoPositionId() != null) {
                CargoPosition cargoPosition = cargoMap.get(e.getCargoPositionId());
                e.setCargoPositionName(cargoPosition.getCode() + StrUtil.DASHED + cargoPosition.getPosition());
            }
        });
        return result;
    }

    @Override
    public ComponentBoundRequisitionVO getComponentBoundRequisition(ComponentBoundRequisitionQueryDTO dto) {
        Plan plan = planService.getById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_NOT_EXIST);
        }
        RequisitionReceived received = requisitionReceivedMapper.queryUniqueComponent(UniqueComponentQueryDTO
                .builder()
                .componentId(dto.getComponentId())
                .productPlanId(dto.getProductPlanId())
                .procedureStepModelId(dto.getProcedureStepModelId())
                .reuse(procedureStepModel.getReusable())
                .copyVersion(dto.getCopyVersion())
                .recordVersionId(procedureStepModel.getRecordVersionId())
                .recordItemId(procedureStepModel.getRecordItemId())
                .build());
        if (received != null && received.getRequisitionId() != null) {
            Long requisitionId = received.getRequisitionId();
            Requisition requisition = requisitionMapper.selectById(requisitionId);
            ComponentBoundRequisitionVO result = new ComponentBoundRequisitionVO();
            result.setId(requisition.getId());
            result.setName(requisition.getName());
            result.setCompletedReceive(requisition.getCompletedReceive());
            return result;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeReceive(ReceiveRequisitionCompleteDTO dto) {
        Long requisitionPlanId = dto.getRequisitionPlanId();
        Requisition requisition = requisitionMapper.selectById(requisitionPlanId);
        if (ObjectUtil.isNull(requisition)) {
            throw new BmosException(MesResponseCode.REQUISITION_PLAN_NOT_EXISTS);
        }
        if (BooleanUtil.isTrue(requisition.getCompletedReceive())) {
            throw new BmosException(MesResponseCode.REQUISITION_PLAN_COMPLETED_RECEIVED);
        }
        if (requisitionReceivedMaterialMapper.existsNotReceivedMaterial(dto.getRequisitionPlanId())) {
            throw new BmosException(MesResponseCode.REQUISITION_NOT_RECEIVE_ALL_MATERIAL);
        }
        requisition.setCompletedReceive(true);
        requisitionMapper.updateById(requisition);
    }

    @Override
    public BigDecimal calculateQuantity(QuantityCalculateDTO dto) {
        ProductFormulaMaterial formulaMaterial =
                productFormulaConfigureService.getFormulaMaterialById(dto.getFormulaMaterialId());
        if (formulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        return MaterialQuantityCalculateUtil.calculateTheoreticalQuantity(dto.getQuantity(), dto.getHydration(),
                dto.getNoHydrationContent(), formulaMaterial);
    }

    @Override
    public BatchReservedMaterialInfoVO getBatchReservedMaterialInfo(BatchReservedMaterialQueryDTO dto) {
        List<BatchReservedMaterialVO> batchReservedMaterial = storageMaterialService.getBatchReservedMaterial(dto);
        BatchReservedMaterialInfoVO info = new BatchReservedMaterialInfoVO();
        info.setReservedList(batchReservedMaterial);
        Long formulaMaterialId = dto.getFormulaMaterialId();
        ProductFormulaMaterial formulaMaterialById =
                productFormulaConfigureService.getFormulaMaterialById(formulaMaterialId);
        if (formulaMaterialById == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        if (dto.getRequisitionPlanId() != null) {
            List<RequisitionMaterialReserved> batchList =
                    requisitionMaterialBatchMapper.getByRequisitionAndMaterial(dto.getRequisitionPlanId(),
                            formulaMaterialId);
            BigDecimal plannedQuantity =
                    batchList.stream().map(RequisitionMaterialReserved::getPlannedQuantity).reduce(BigDecimal.ZERO,
                            BigDecimal::add);
            info.setTotalPlannedQuantity(MaterialQuantityCalculateUtil.roundingOff(plannedQuantity,
                    formulaMaterialById));
            BigDecimal theoretical =
                    batchList.stream().map(RequisitionMaterialReserved::getTheoreticalQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            info.setTotalTheoreticalQuantity(MaterialQuantityCalculateUtil.roundingOff(theoretical,
                    formulaMaterialById));
        }
        return info;
    }

    @Override
    public ReserveComponentInstanceVO getReserveComponentInstanceInfo(ReserveComponentInstanceQueryDTO dto) {
        ReserveComponentInstanceVO result = new ReserveComponentInstanceVO();
        Long procedureStepModelId = dto.getProcedureStepModelId();
        Plan plan = planService.getById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(procedureStepModelId);
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfoByPlanId(dto.getProductPlanId());
        // 获取组件唯一实例
        ReserveComponentInstance instance =
                reserveComponentInstanceMapper.selectUnique(UniqueComponentQueryDTO
                        .builder()
                        .productPlanId(dto.getProductPlanId())
                        .recordItemId(procedureStepModel.getRecordItemId())
                        .recordVersionId(procedureStepModel.getRecordVersionId())
                        .copyVersion(dto.getCopyVersion())
                        .componentId(dto.getComponentId())
                        .reuse(procedureStepModel.getReusable())
                        .procedureStepModelId(procedureStepModelId)
                        .build());
        if (instance == null) {
            instance = new ReserveComponentInstance();
            instance.setProductPlanId(dto.getProductPlanId());
            instance.setRecordItemId(procedureStepModel.getRecordItemId());
            instance.setRecordVersionId(procedureStepModel.getRecordVersionId());
            instance.setCopyVersion(dto.getCopyVersion());
            instance.setReuse(procedureStepModel.getReusable());
            instance.setComponentId(dto.getComponentId());
            instance.setProcedureStepModelId(dto.getProcedureStepModelId());
            instance.setBatchNo(plan.getBatchNo());
            reserveComponentInstanceMapper.insert(instance);
        }
        result.setComponentInstanceId(instance.getId());
        // 如果组件配置了配方物料则只能预定已配置的配方物料
        String configJson = procedureStepConfigService.getComponentConfigJson(procedureStepModelId,
                dto.getComponentId(),
                procedureStepModel.getReusable(), procedureStepModel.getProcessId(),
                procedureStepModel.getProcessVersion());
        List<ProductFormulaMaterial> planMaterialList = formulaInfo.getMaterials();
        if (StrUtil.isNotEmpty(configJson)) {
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(configJson,
                    ProcedureStepConfigInfo.class);
            List<Long> formulaMaterialIdList = configInfo.getFormulaMaterialIds();
            if (CollUtil.isNotEmpty(configInfo.getFormulaMaterialIds())) {
                planMaterialList = productFormulaConfigureService.getFormulaMaterialListByIds(formulaMaterialIdList);
            }
        }
        List<RequisitionPlanMaterialVO> planMaterialVOList = planMaterialList.stream().map(material -> {
            RequisitionPlanMaterialVO vo =
                    ProductFormulaConverter.INSTANCE.convertToRequisitionPlanMaterialVO(material);
            vo.setTheoreticalQuantity(BusinessComponentStrategy.calculateQuantity(plan.getBatchQuantity(), formulaInfo.getBatchQuantity(), material));
            vo.setUnitName(unitCache.getGlobalUnitName(material.getUnitId()));
            return vo;
        }).collect(Collectors.toList());
        result.setMaterialList(planMaterialVOList);
        return result;
    }

    /**
     * 物料预定组件 批量预定物料件
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reserveStorageMaterial(ReserveComponentReserveDTO dto) {
        ReserveComponentInstance instance = reserveComponentInstanceMapper.selectById(dto.getComponentInstanceId());
        if (instance == null) {
            throw new BmosException(MesResponseCode.RESERVE_COMPONENT_INSTANCE_NOT_EXIST);
        }
        List<Long> idList = dto.getStorageMaterialIdList();
        Long productPlanId = instance.getProductPlanId();
        Plan plan = planService.getById(productPlanId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        ReserveComponentReserveMaterialDTO reserveBatchDTO = new ReserveComponentReserveMaterialDTO();
        reserveBatchDTO.setProductPlanId(productPlanId);
        reserveBatchDTO.setStorageMaterialIdList(idList);
        List<StorageMaterial> storageMaterials = storageMaterialService.reserveComponentReserve(reserveBatchDTO);
        Map<Long, BigDecimal> quantityMap = CollectionUtils.convertMap(storageMaterials, StorageMaterial::getId,
                StorageMaterial::getQuantity);
        List<ReserveComponentMaterial> collect = idList.stream().map(id -> {
            ReserveComponentMaterial reserveComponentMaterial = new ReserveComponentMaterial();
            reserveComponentMaterial.setInstanceId(instance.getId());
            reserveComponentMaterial.setStorageMaterialId(id);
            reserveComponentMaterial.setCancelReserve(false);
            reserveComponentMaterial.setQuantity(quantityMap.get(id));
            return reserveComponentMaterial;
        }).collect(Collectors.toList());
        reserveComponentMaterialMapper.insertBatch(collect);
        handleReserveComponentBusinessData(instance);
    }


    /**
     * 处理并保存物料预定组件业务数据
     * @param instance 物料预定组件实例
     */
    private void handleReserveComponentBusinessData(ReserveComponentInstance instance) {
        // 查询该实例预定的物料件及批次信息
        List<ReserveComponentMaterial> instanceMaterials =
                reserveComponentMaterialMapper.selectByInstanceId(instance.getId());
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(instance.getProcedureStepModelId());
        ProductionDetailInfo info = new ProductionDetailInfo();
        BusinessComponentBatchSaveDTO businessComponentBatchSaveDTO = convertToBatchSaveDTO(instance,
                procedureStepModel);
        info.setDto(businessComponentBatchSaveDTO);
        info.setFormulaInfo(productFormulaConfigureService.getProductFormulaInfoByPlanId(instance.getProductPlanId()));
        // 查询组件预定信息以及批次预定信息
        List<StorageMaterialDetailInfo> storageMaterialDetailInfoList = getStorageMaterialDetailInfos(instance);
        info.setStorageMaterialDetailInfoList(storageMaterialDetailInfoList);
        List<ExecuteFormData> results = new ArrayList<>();
        ComponentListVO receivedComponent =
                componentService.selectUsedComponentDetail(instance.getRecordVersionId(),
                        instance.getRecordItemId(), instance.getComponentId());
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        // 获取已存在的字段值
        RecordItemLatestDataQueryDTO queryDTO = getRecordItemLatestDataQueryDTO(instance, procedureStepModel, receivedComponent);
        List<FormDataItemVO> recordItemLatestData = executeFormDataService.getRecordItemLatestData(queryDTO);
        info.setFormDataCollection(recordItemLatestData.stream().filter(e-> e.getValue() != null).collect(Collectors.toList()));
        materialReserveComponentStrategy.handleBusinessComponent(results, receivedComponent, info, configMap, null);
        executeFormDataService.saveResultsAndHandleRelationComponentData(results,
                businessComponentBatchSaveDTO.transToBaseDTO());
    }

    private List<StorageMaterialDetailInfo> getStorageMaterialDetailInfos(ReserveComponentInstance instance) {
        List<StorageMaterialDetailInfo> infos = reserveComponentMaterialMapper.selectBatchListByInstanceId(instance.getId(), instance.getProductPlanId());
        infos.sort(Comparator.comparing(StorageMaterialDetailInfo::getMinId, Comparator.nullsLast(Long::compareTo)));
        return infos;
    }

    private RecordItemLatestDataQueryDTO getRecordItemLatestDataQueryDTO(ReserveComponentInstance instance, ProcedureStepModel procedureStepModel, ComponentListVO receivedComponent) {
        List<Long> fieldIds = new ArrayList<>();
        recGetComponentFieldList(receivedComponent, fieldIds);
        RecordItemLatestDataQueryDTO queryDTO = new RecordItemLatestDataQueryDTO();
        queryDTO.setReuse(instance.getReuse());
        queryDTO.setDiscard(false);
        queryDTO.setCopyVersion(instance.getCopyVersion());
        queryDTO.setProductPlanId(instance.getProductPlanId());
        queryDTO.setProcedureStepId(procedureStepModel.getProcedureStepId());
        queryDTO.setFieldIdList(fieldIds);
        queryDTO.setRecordItemId(instance.getRecordItemId());
        return queryDTO;
    }

    private BusinessComponentBatchSaveDTO convertToBatchSaveDTO(ReserveComponentInstance instance, ProcedureStepModel procedureStepModel) {
        BusinessComponentBatchSaveDTO businessComponentBatchSaveDTO = new BusinessComponentBatchSaveDTO();
        businessComponentBatchSaveDTO.setComponentId(instance.getComponentId());
        businessComponentBatchSaveDTO.setCopyVersion(instance.getCopyVersion());
        businessComponentBatchSaveDTO.setProcedureStepModelId(instance.getProcedureStepModelId());
        businessComponentBatchSaveDTO.setReuse(instance.getReuse());
        businessComponentBatchSaveDTO.setProductPlanId(instance.getProductPlanId());
        businessComponentBatchSaveDTO.setRecordVersionId(instance.getRecordVersionId());
        businessComponentBatchSaveDTO.setRecordItemId(instance.getRecordItemId());
        businessComponentBatchSaveDTO.setBatchNo(instance.getBatchNo());
        businessComponentBatchSaveDTO.setProcessId(procedureStepModel.getProcessId());
        businessComponentBatchSaveDTO.setProcessVersion(procedureStepModel.getProcessVersion());
        businessComponentBatchSaveDTO.setProcedureStepId(procedureStepModel.getProcedureStepId());
        return businessComponentBatchSaveDTO;
    }

    /**
     * 物料预定组件 取消预定
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reserveComponentCancelReserve(ReserveComponentCancelReserveDTO dto) {
        ReserveComponentInstance instance = reserveComponentInstanceMapper.selectById(dto.getComponentInstanceId());
        if (instance == null) {
            throw new BmosException(MesResponseCode.RESERVE_COMPONENT_INSTANCE_NOT_EXIST);
        }
        // 暂存间取消预定
        Long productPlanId = instance.getProductPlanId();
        StorageMaterialCancelReserveDTO cancelDTO = new StorageMaterialCancelReserveDTO();
        Plan plan = planService.getById(productPlanId);
        Long storageMaterialId = dto.getStorageMaterialId();
        cancelDTO.setOperatorId(SysUserHolder.getUser().getUserId());
        cancelDTO.setStorageMaterialIdList(Collections.singletonList(storageMaterialId));
        cancelDTO.setProductId(plan.getProductId());
        cancelDTO.setProcessId(plan.getProcessId());
        cancelDTO.setBatchId(productPlanId);
        cancelDTO.setOperateType(StorageOperateTypeEnum.MATERIAL_CANCEL_RESERVE);
        storageMaterialService.cancelReserve(cancelDTO);
        // 添加一条cancel_reserve为true的记录 quantity为此时物料件的量
        insertCancelReserveRecord(dto, instance);
        handleReserveComponentBusinessData(instance);
    }

    private void insertCancelReserveRecord(ReserveComponentCancelReserveDTO dto, ReserveComponentInstance instance) {
        ReserveComponentMaterial reserveComponentMaterial = new ReserveComponentMaterial();
        reserveComponentMaterial.setInstanceId(instance.getId());
        reserveComponentMaterial.setStorageMaterialId(dto.getStorageMaterialId());
        reserveComponentMaterial.setCancelReserve(true);
        StorageMaterial storageMaterial = storageMaterialMapper.selectById(dto.getStorageMaterialId());
        reserveComponentMaterial.setQuantity(storageMaterial.getAvailableQuantity());
        reserveComponentMaterialMapper.insert(reserveComponentMaterial);
    }


    @Override
    public void handleNameAndSave(Requisition requisition) {
        Integer nextSerialNo = requisitionMapper.selectNextSerialNo(requisition.getProductPlanId());
        requisition.setSerialNo(nextSerialNo);
        if (nextSerialNo < 10) {
            requisition.setName(requisition.getBatchNo() + StrUtil.DASHED + 0 + nextSerialNo);
        } else {
            requisition.setName(requisition.getBatchNo() + StrUtil.DASHED + nextSerialNo);
        }
        requisitionMapper.insert(requisition);
    }
}
