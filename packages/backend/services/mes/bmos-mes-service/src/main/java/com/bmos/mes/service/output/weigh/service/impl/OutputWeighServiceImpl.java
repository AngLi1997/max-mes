package com.bmos.mes.service.output.weigh.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.ingredient.WeighMode;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.ingredient.WeighType;
import com.bmos.mes.common.enums.storage.ChargeRecycleTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.dto.RecordItemLatestDataQueryDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.convert.ProductFormulaConverter;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.ingredient.weigh.dto.WeighLogSaveDTO;
import com.bmos.mes.service.ingredient.weigh.service.WeighLogService;
import com.bmos.mes.service.ingredient.weigh.vo.WeighBalanceEquipment;
import com.bmos.mes.service.ingredient.weigh.vo.WeighResult;
import com.bmos.mes.service.output.weigh.convert.OutputWeighProcessConvert;
import com.bmos.mes.service.output.weigh.dto.*;
import com.bmos.mes.service.output.weigh.mapper.IOutputWeighProcessMapper;
import com.bmos.mes.service.output.weigh.mapper.IOutputWeighRecordMapper;
import com.bmos.mes.service.output.weigh.model.OutputWeighProcess;
import com.bmos.mes.service.output.weigh.model.OutputWeighRecord;
import com.bmos.mes.service.output.weigh.service.IOutputWeighService;
import com.bmos.mes.service.output.weigh.vo.OutputMaterialItem;
import com.bmos.mes.service.output.weigh.vo.OutputWeighProcessVO;
import com.bmos.mes.service.output.weigh.vo.OutputWeighRecordComponentView;
import com.bmos.mes.service.output.weigh.vo.OutputWeighStorageMaterialVO;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.mapper.ProcedureStepConfigMapper;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcessVersionService;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.condition.event.OutputWeighSignType;
import com.bmos.mes.service.product.convert.ProductMaterialConverter;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.MaterialExpandInfo;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.strategy.OutputWeighComponentStrategy;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.station.service.IStationService;
import com.bmos.mes.service.storage.config.mapper.ICargoPositionMapper;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.service.ICargoPositionService;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialManageBatchCreateDTO;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialReserveDTO;
import com.bmos.mes.service.storage.manage.mapper.IChargeRecycleComponentMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialChargeRecycleMapper;
import com.bmos.mes.service.storage.manage.model.ChargeRecycleComponent;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.model.StorageMaterialChargeRecycle;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialManageService;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialSimpleBatchVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialVO;
import com.bmos.mes.service.tag.convert.ScanDeviceConvert;
import com.bmos.mes.service.trace.material.dto.MaterialTraceHistoryDTO;
import com.bmos.mes.service.trace.material.enums.MaterialTraceOperateType;
import com.bmos.mes.service.trace.material.service.IMaterialTraceHistoryService;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.facade.equipment.dto.EquipmentQueryDTO;
import com.bmos.platform.facade.equipment.enums.EquipmentTagCodeEnum;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bmos.mes.common.enums.storage.StorageOperateTypeEnum.MANUAL_OUTPUT;
import static com.bmos.mes.common.enums.storage.StorageOperateTypeEnum.OUTPUT_WEIGHT;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/28 15:26
 */
@Service
@Slf4j
public class OutputWeighServiceImpl implements IOutputWeighService {

    private static final String LOG_PREFIX = "[产出称量]";

    @Resource
    private EquipmentConfigFeign configFeign;

    @Resource
    private IOutputWeighProcessMapper outputWeighProcessMapper;

    @Resource
    private IOutputWeighRecordMapper outputWeighRecordMapper;

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Resource
    private IStorageMaterialManageService storageMaterialManageService;

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private UnitCache unitCache;

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private WeighLogService weighLogService;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Resource
    private ProcedureStepConfigMapper procedureStepConfigMapper;

    @Resource
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Resource
    private IChargeRecycleComponentMapper chargeRecycleComponentMapper;

    @Resource
    private IStorageMaterialChargeRecycleMapper storageMaterialChargeRecycleMapper;

    @Resource
    private ExecuteFormDataService executeFormDataService;

    @Resource
    private BatchRecordComponentService batchRecordComponentService;

    @Resource
    private ProcedureStepConfigService procedureStepConfigService;

    @Resource
    private OutputWeighComponentStrategy outputWeighComponentStrategy;

    @Resource
    private ProcessVersionService processVersionService;

    @Resource
    private ICargoPositionMapper cargoPositionMapper;

    @Resource
    private ICargoPositionService cargoPositionService;

    @Resource
    private IStorageMaterialPositionLogService storageMaterialPositionLogService;

    @Resource
    private IStationService stationService;

    @Resource
    private ITaskConditionCalculator conditionChangeHandler;

    @Resource
    private IMaterialTraceHistoryService materialTraceHistoryService;

    @Override
    public List<WeighBalanceEquipment> getBalanceListByStationIds(List<Long> stationIds) {
        EquipmentQueryDTO query = new EquipmentQueryDTO();
        query.setStationIdList(stationIds);
        // 称具
        query.setTagCode(EquipmentTagCodeEnum.WEIGHING_DEVICE_12020.getCode());
        ResponseInfo<List<EquipmentInfoFeignVO>> configByStationId = configFeign.getConfigByStationIdList(query);
        if (CollectionUtil.isEmpty(configByStationId.getData())) {
            return Collections.emptyList();
        }
        return ScanDeviceConvert.INSTANCE.convertToEquipmentList(configByStationId.getData());
    }

    @Override
    @Nullable
    public OutputWeighProcessVO getOutputWeighProcess(OutputWeighProcessQuery query) {
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(query.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        OutputWeighProcess process = outputWeighProcessMapper.getOutputWeighProcess(query.getProductPlanId(),
                query.getProcedureStepModelId(), query.getCopyVersion(), query.getComponentId(), procedureStepModel.getReusable());
        if (process == null) {
            return null;
        }
        StorageMaterialBatch storageMaterialBatch =
                storageMaterialBatchMapper.selectByMaterialIdAndNo(process.getMaterialId(),
                        process.getStorageMaterialBatchNo());
        StorageMaterialBatch relevanceStorageMaterialBatch =
                Optional.ofNullable(process.getRelevanceStorageMaterialBatchId())
                        .map(storageMaterialBatchMapper::selectById)
                        .orElse(null);
        OutputWeighProcessVO result = OutputWeighProcessConvert.INSTANCE.convertToVO(process);
        BaseUserDO weigher = UserUtils.getUser(result.getWeigherId());
        if (weigher != null) {
            result.setWeigherName(weigher.getUserName());
            result.setWeigherLoginName(weigher.getLoginName());
        }
        BaseUserDO reChecker = UserUtils.getUser(result.getReCheckerId());
        if (reChecker != null) {
            result.setReCheckerName(reChecker.getUserName());
            result.setReCheckerLoginName(reChecker.getLoginName());
        }
        ProductMaterial material = productMaterialMapper.selectById(process.getMaterialId());
        if (material == null) {
            return result;
        }
        result.setMaterialName(material.getName());
        result.setMaterialSpecification(material.getSpecification());
        result.setMaterialMergeCode(material.getMergeCode());
        result.setBasicUnitId(material.getUnitId());
        result.setBasicUnit(unitCache.getGlobalUnitName(material.getUnitId()));
        result.setStorageMaterialBatchNo(process.getStorageMaterialBatchNo());
        ProductFormulaInfo formulaInfo = productFormulaConfigureService.getProductFormulaInfoByPlanId(process.getProductPlanId());
        ProductFormulaMaterial productFormulaMaterial = Optional.ofNullable(formulaInfo)
                .map(ProductFormulaInfo::getMaterialIdMap)
                .map(map -> map.get(process.getMaterialId()))
                .orElse(null);
        if (productFormulaMaterial != null) {
            result.setUnitId(productFormulaMaterial.getUnitId());
            result.setUnit(unitCache.getGlobalUnitName(productFormulaMaterial.getUnitId()));
            result.setScale(productFormulaMaterial.getScale());
        }
        StorageMaterialSimpleBatchVO history = queryBatchInfo(process.getMaterialId(), process.getStorageMaterialBatchNo());
        if (history != null) {
            result.setStorageMaterialBatchId(history.getStorageMaterialBatchId());
        }
        if (relevanceStorageMaterialBatch != null) {
            result.setRelevanceStorageMaterialBatchNo(relevanceStorageMaterialBatch.getMaterialBatchNo());
        }
        result.setStationIds(stationService.getStationIdsByProcedureStepModelIdAndComponentId(process.getProcedureStepModelId(), process.getComponentId(), process.getProductPlanId()));
        if (storageMaterialBatch != null) {
            Map<Long, CargoPosition> positionMap = new HashMap<>();
            List<OutputWeighRecord> outputWeighRecords = outputWeighRecordMapper.queryRecordListByProcessId(process.getId());
            if (CollectionUtil.isNotEmpty(outputWeighRecords)) {
                List<Long> positionIds = outputWeighRecords.stream()
                        .map(OutputWeighRecord::getMaterialPositionId)
                        .collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(positionIds)) {
                    positionMap = cargoPositionMapper.selectBatchIds(positionIds).stream()
                            .collect(Collectors.toMap(CargoPosition::getId, Function.identity(), (k1, k2) -> k1));
                }
            }

            Map<Long, StorageMaterialVO> materialMap = new HashMap<>();
            List<Long> storageMaterialIds = outputWeighRecords.stream()
                    .map(OutputWeighRecord::getStorageMaterialId)
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(storageMaterialIds)) {
                materialMap = storageMaterialService.queryInfoByIds(storageMaterialIds)
                        .stream().collect(Collectors.toMap(StorageMaterialVO::getId, Function.identity(), (k1, k2) -> k1));
            }
            Map<Long, CargoPosition> finalPositionMap = positionMap;
            Map<Long, StorageMaterialVO> finalMaterialMap = materialMap;
            result.setWeightRecordList(outputWeighRecords
                    .stream()
                    .map(record -> {
                        OutputWeighStorageMaterialVO item = new OutputWeighStorageMaterialVO();
                        item.setId(record.getStorageMaterialId());
                        item.setStorageMaterialNo(record.getStorageMaterialNo());
                        item.setUnitId(record.getUnitId());
                        item.setUnit(unitCache.getGlobalUnitName(record.getUnitId()));
                        StorageMaterialVO productMaterial = finalMaterialMap.get(record.getStorageMaterialId());
                        if (productMaterial != null) {
                            item.setMaterialCode(productMaterial.getMergeCode());
                            item.setMaterialName(productMaterial.getMaterialName());
                            item.setStorageMaterialBatchNo(productMaterial.getMaterialBatchNo());
                            item.setStorageMaterialBatchId(record.getStorageMaterialBatchId());
                            item.setExpiredDate(productMaterial.getExpiredDate());
                        }
                        item.setQuantity(unitCache.toExt(record.getQuantity(), record.getUnitId()).stripTrailingZeros());
                        if (!record.getByPiece()) {
                            item.setTareWeight(unitCache.toExt(record.getTareWeight(), record.getUnitId()).stripTrailingZeros());
                            item.setGrossWeight(unitCache.toExt(record.getGrossWeight(), record.getUnitId()).stripTrailingZeros());
                            item.setNetWeight(unitCache.toExt(record.getNetWeight(), record.getUnitId()).stripTrailingZeros());
                        }
                        item.setSignStatus(record.getSignStatus());
                        // 容器信息
                        item.setContainerId(record.getContainerId());
                        item.setContainerName(record.getContainerName());
                        // 查询称量人信息
                        if (record.getWeigherId() != null) {
                            BaseUserDO user = UserUtils.getUser(record.getWeigherId());
                            if (user != null) {
                                item.setWeigherId(user.getUserId());
                                item.setWeigherFullName(user.getUserName() + "-" + user.getLoginName());
                            }
                        }
                        if (record.getReCheckerId() != null) {
                            BaseUserDO user = UserUtils.getUser(record.getReCheckerId());
                            if (user != null) {
                                item.setReCheckerId(user.getUserId());
                                item.setReCheckerFullName(user.getUserName() + "-" + user.getLoginName());
                            }
                        }
                        item.setWeighTime(record.getWeighTime());
                        item.setMaterialPositionName(Optional.ofNullable(record.getMaterialPositionId())
                                .map(finalPositionMap::get)
                                .map(position -> position.getCode() + "-" + position.getPosition())
                                .orElse(null)
                        );
                        return item;
                    }).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long makeSureWeigher(OutputMakeSureWeigherDTO dto) {
        log.info("{}确认称量人:{}", LOG_PREFIX, dto);
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(dto.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        OutputWeighProcess existProcess = outputWeighProcessMapper.getOutputWeighProcess(dto.getProductPlanId(),
                dto.getProcedureStepModelId(), dto.getCopyVersion(), dto.getComponentId(), procedureStepModel.getReusable());
        if (existProcess != null) {
            throw new BmosException(MesResponseCode.OUTPUT_WEIGH_PROCESS_WEIGHER_EXIST);
        }
        OutputWeighProcess process = new OutputWeighProcess();
        process.setProductPlanId(dto.getProductPlanId());
        process.setProcedureStepModelId(dto.getProcedureStepModelId());
        process.setCopyVersion(dto.getCopyVersion());
        process.setComponentId(dto.getComponentId());
        process.setReuse(procedureStepModel.getReusable());
        process.setWeigherId(dto.getWeigherId());
        process.setReCheckerId(dto.getReCheckerId());
        process.setRemark(dto.getRemark());
        outputWeighProcessMapper.insert(process);
        return process.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeSureBatch(OutputMakeSureBatchDTO dto) {
        log.info("{}确认称量批次:{}", LOG_PREFIX, dto);
        OutputWeighProcess process = outputWeighProcessMapper.selectById(dto.getOutputWeighProcessId());
        if (process == null) {
            throw new BmosException(MesResponseCode.OUTPUT_WEIGH_PROCESS_NOT_EXIST);
        }
        if (dto.getRelevanceMaterialId() != null && StrUtil.isNotBlank(dto.getRelevanceMaterialBatchNo())) {
            StorageMaterialBatch relevanceStorageMaterialBatch =
                    storageMaterialBatchMapper.selectByMaterialIdAndNo(dto.getRelevanceMaterialId(), dto.getRelevanceMaterialBatchNo());
            if (relevanceStorageMaterialBatch == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
            }
            process.setRelevanceStorageMaterialBatchId(relevanceStorageMaterialBatch.getId());
        }
        ProductMaterial material = productMaterialMapper.selectById(dto.getMaterialId());
        if (material == null) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        StorageMaterialBatch batch = storageMaterialBatchMapper.selectByMaterialIdAndNo(dto.getMaterialId(),
                dto.getStorageMaterialBatchNo());
        LocalDate expiredDate;
        if (batch == null) {
            if (dto.getExpiredDate() == null) {
                throw new ValidationException("物料批次不存在，请输入有效期");
            }
            expiredDate = dto.getExpiredDate();
        } else {
            expiredDate = batch.getExpiredDate();
        }
        process.setMaterialId(dto.getMaterialId());
        process.setRelevanceMaterialId(dto.getRelevanceMaterialId());
        process.setStorageMaterialBatchNo(dto.getStorageMaterialBatchNo());
        process.setExpiredDate(expiredDate);
        outputWeighProcessMapper.updateById(process);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WeighResult.WeighResultItem> weighAndPrint(OutputWeighAndPrintDTO dto) {
        log.info("{}称量打码:{}", LOG_PREFIX, dto);
        OutputWeighProcess process = outputWeighProcessMapper.selectById(dto.getOutputWeighProcessId());
        if (process == null) {
            throw new BmosException(MesResponseCode.OUTPUT_WEIGH_PROCESS_NOT_EXIST);
        }
        Plan plan = planMapper.selectById(process.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }

        String weigherId = process.getWeigherId();
        if (weigherId == null) {
            throw new BmosException(MesResponseCode.OUTPUT_WEIGH_PROCESS_WEIGHER_NOT_EXIST);
        }
        String loginUserId = SysUserHolder.getUser().getUserId();
        if (!Objects.equals(weigherId, loginUserId)) {
            // 登录账号与称量人不符
            throw new BmosException(MesResponseCode.OUTPUT_LOGIN_USER_WEIGHER_NOT_MATCH);
        }
        // 净重校验
        dto.validateNetWeight();
        ProductMaterial material = productMaterialMapper.selectAllInfoById(process.getMaterialId());
        if (material == null) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        StorageMaterialBatch storageMaterialBatch =
                storageMaterialBatchMapper.selectByMaterialIdAndNo(process.getMaterialId(),
                        process.getStorageMaterialBatchNo());
        // 判断是否存在批次 不存在则创建
        if (storageMaterialBatch == null) {
            log.info("{}物料批次不存在，新建物料批次:{}", LOG_PREFIX, process.getStorageMaterialBatchNo());
            StorageMaterialManageBatchCreateDTO batchCreateDTO = new StorageMaterialManageBatchCreateDTO();
            if (process.getRelevanceMaterialId() != null) {
                StorageMaterialBatch relevanceStorageMaterialBatch =
                        storageMaterialBatchMapper.selectById(process.getRelevanceStorageMaterialBatchId());
                if (relevanceStorageMaterialBatch == null) {
                    throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
                }
                log.info("{}本次成品称量存在关联批次，继承关联批次信息:{}", LOG_PREFIX, relevanceStorageMaterialBatch.getMaterialBatchNo());
                batchCreateDTO.setMaterialId(process.getMaterialId());
                batchCreateDTO.setMaterialBatchNo(process.getStorageMaterialBatchNo());
                batchCreateDTO.setFactoryBatchNo(relevanceStorageMaterialBatch.getFactoryBatchNo());
                batchCreateDTO.setExpiredDate(process.getExpiredDate());
                batchCreateDTO.setProduceDate(relevanceStorageMaterialBatch.getProduceDate());
                batchCreateDTO.setHydration(relevanceStorageMaterialBatch.getHydration());
                batchCreateDTO.setNoHydrationContent(relevanceStorageMaterialBatch.getNoHydrationContent());
                batchCreateDTO.setReportNo(relevanceStorageMaterialBatch.getReportNo());
                batchCreateDTO.setLicenceNo(relevanceStorageMaterialBatch.getLicenceNo());
                batchCreateDTO.setSupplier(relevanceStorageMaterialBatch.getSupplier());
                batchCreateDTO.setProducer(relevanceStorageMaterialBatch.getProducer());
                batchCreateDTO.setOriginalBatchNo(relevanceStorageMaterialBatch.getOriginalBatchNo());
            } else {
                log.info("{}本次成品称量不存在关联批次", LOG_PREFIX);
                batchCreateDTO.setMaterialId(process.getMaterialId());
                batchCreateDTO.setMaterialBatchNo(process.getStorageMaterialBatchNo());
                batchCreateDTO.setExpiredDate(process.getExpiredDate());
            }
            batchCreateDTO.setOperatorId(loginUserId);
            storageMaterialBatch = storageMaterialManageService.addBatch(batchCreateDTO);
        } else {
            log.info("{}物料批次存在，直接产出物料批次中间品:{}", LOG_PREFIX, process.getStorageMaterialBatchNo());
        }
        EquipmentInfoFeignVO container = null;
        if (dto.getContainerId() != null) {
            container = FeignUtils.handleRequest(ctId -> equipmentConfigFeign.getConfigByEquipmentId(ctId), dto.getContainerId()).getData();
        }
        Long containerId = null;
        String containerName = null;
        if (container != null) {
            containerId = container.getId();
            containerName = container.getCode() + "-" + container.getName();
        }

        if (storageMaterialBatch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }

        CargoPosition cargoPosition;
        // 货位
        if (dto.getSize() == 1 && dto.getMaterialPositionId() != null) {
            cargoPosition = cargoPositionService.getByIdWithPermission(dto.getMaterialPositionId());
            if (cargoPosition == null) {
                throw new BmosException(MesResponseCode.CARGO_POSITION_NOT_EXIST);
            }
        } else {
            cargoPosition = null;
        }

        List<String> serials = storageMaterialService.batchGetSerial(dto.getSize());


        // 使用传入的单位
        Long unitId = dto.getUnitId();
        CacheUnit globalUnit = unitCache.getGlobalUnit(unitId);


        Boolean byPiece = dto.getByPiece();

        List<StorageMaterial> storageMaterialList = new ArrayList<>();

        // 物料重量
        // 按件则直接使用物料量
        // 皮净毛则使用净重
        BigDecimal weight = unitCache.toBasic(byPiece ? dto.getQuantity() : dto.getNetWeight(), unitId);

        // 生成物料件
        for (int i = 0; i < dto.getSize(); i++) {
            // 生产物料件记录
            StorageMaterial storageMaterial = new StorageMaterial();
            if (cargoPosition != null) {
                storageMaterial.setMaterialPositionId(cargoPosition.getId());
            }
            storageMaterial.setMaterialId(storageMaterialBatch.getMaterialId());
            storageMaterial.setStorageMaterialBatchId(storageMaterialBatch.getId());
            storageMaterial.setNo(serials.get(i));
            if (globalUnit != null) {
                if (globalUnit.getExtend()) {
                    storageMaterial.setUnitId(globalUnit.getParentUnitId());
                    storageMaterial.setUnitExtendId(globalUnit.getUnitId());
                } else {
                    storageMaterial.setUnitId(unitId);
                }
            }
            storageMaterial.setInitQuantity(weight);
            storageMaterial.setAvailableQuantity(weight);
            storageMaterial.setConsumeQuantity(BigDecimal.ZERO);
            storageMaterial.setReserveQuantity(BigDecimal.ZERO);
            storageMaterial.setSignStatus(WeighSignStatus.UN_SIGNED);
            storageMaterial.setProductPlanId(plan.getId());
            storageMaterial.setContainerId(containerId);
            storageMaterial.setContainer(containerName);
            storageMaterialList.add(storageMaterial);
        }

        storageMaterialService.saveBatch(storageMaterialList);

        // 保存中间品产出的物料追溯记录
        materialTraceHistoryService.saveTraceHistory(storageMaterialList.stream()
                .map(storageMaterial -> MaterialTraceHistoryDTO.builder()
                        .storageMaterialId(storageMaterial.getId())
                        .productPlanId(process.getProductPlanId())
                        .procedureStepModelId(process.getProcedureStepModelId())
                        .operateType(MaterialTraceOperateType.MIDDLE_OUTPUT)
                        .quantity(unitCache.toExt(storageMaterial.getAvailableQuantity(), storageMaterial.getFinalUnitId()))
                        .unitId(storageMaterial.getFinalUnitId())
                        .build()).collect(Collectors.toList()));

        List<StorageMaterialPositionLogDTO> logs = storageMaterialList.stream()
                .map(storageMaterial -> StorageMaterialPositionLogDTO.builder()
                        .materialPositionId(dto.getMaterialPositionId())
                        .storageMaterialId(storageMaterial.getId())
                        .operateType(byPiece ? MANUAL_OUTPUT : OUTPUT_WEIGHT)
                        .quantity(unitCache.toExt(storageMaterial.getAvailableQuantity(), storageMaterial.getFinalUnitId()))
                        .unitId(storageMaterial.getFinalUnitId())
                        .senderId(process.getWeigherId())
                        .receiverId(process.getReCheckerId())
                        .productName(plan.getProductName())
                        .productCode(plan.getProductMergeCode())
                        .productBatchNo(plan.getBatchNo())
                        .tareWeight(dto.getTareWeight())
                        .grossWeight(dto.getGrossWeight())
                        .build())
                .collect(Collectors.toList());

        // 预定物料件
        storageMaterialService.reserveBatch(storageMaterialList.stream()
                .map(storageMaterial -> StorageMaterialReserveDTO.builder()
                        .storageMaterialId(storageMaterial.getId())
                        .processId(plan.getProcessId())
                        .batchId(plan.getId())
                        .productId(plan.getProductId())
                        .reCheckerId(process.getReCheckerId())
                        .operatorId(process.getWeigherId())
                        .grossWeight(dto.getGrossWeight())
                        .tareWeight(dto.getTareWeight())
                        .remark(process.getRemark())
                        .build()).collect(Collectors.toList()));

        List<OutputWeighRecord> recordList = storageMaterialList.stream()
                .map(storageMaterial -> {
                    // 保存称量记录
                    OutputWeighRecord record = new OutputWeighRecord();
                    record.setOutputWeighProcessId(dto.getOutputWeighProcessId());
                    record.setStorageMaterialId(storageMaterial.getId());
                    record.setStorageMaterialNo(storageMaterial.getNo());
                    record.setStorageMaterialBatchId(storageMaterial.getStorageMaterialBatchId());
                    record.setByPiece(byPiece);
                    if (!byPiece) {
                        record.setGrossWeight(unitCache.toBasic(dto.getGrossWeight(), unitId));
                        record.setNetWeight(unitCache.toBasic(dto.getNetWeight(), unitId));
                        record.setTareWeight(checkZero(record.getGrossWeight().subtract(record.getNetWeight())));
                        record.setQuantity(unitCache.toBasic(dto.getNetWeight(), unitId));
                    } else {
                        record.setQuantity(unitCache.toBasic(dto.getQuantity(), unitId));
                    }
                    record.setUnitId(unitId);
                    record.setWeigherId(process.getWeigherId());
                    record.setReCheckerId(process.getReCheckerId());
                    record.setMaterialPositionId(dto.getMaterialPositionId());
                    record.setWeighMode(WeighMode.getByValue(dto.getWeighMode()));
                    record.setSignStatus(WeighSignStatus.UN_SIGNED);
                    record.setWeighTime(LocalDateTime.now());
                    record.setContainerId(storageMaterial.getContainerId());
                    record.setContainerName(storageMaterial.getContainer());
                    return record;
                })
                .collect(Collectors.toList());
        outputWeighRecordMapper.insertBatch(recordList);

        List<WeighLogSaveDTO> weighLogSaveDTOS = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            OutputWeighRecord record = recordList.get(i);
            StorageMaterial storageMaterial = storageMaterialList.get(i);
            WeighLogSaveDTO build = WeighLogSaveDTO.builder()
                    .unitId(record.getUnitId())
                    .weigherId(record.getWeigherId())
                    .reCheckerId(record.getReCheckerId())
                    .weighType(WeighType.PRODUCT)
                    .netWeight(record.getNetWeight())
                    .grossWeight(record.getGrossWeight())
                    .weighTime(LocalDateTime.now())
                    .materialId(storageMaterial.getMaterialId())
                    .materialName(material.getName())
                    .materialMergeCode(material.getMergeCode())
                    .materialType(material.getCategoryType())
                    .storageMaterialId(storageMaterial.getId())
                    .materialNo(storageMaterial.getNo())
                    .materialBatchNo(storageMaterialBatch.getMaterialBatchNo())
                    .productPlanId(process.getProductPlanId())
                    .equipmentId(dto.getContainerId())
                    .productId(plan.getProductId())
                    .materialBatchId(storageMaterialBatch.getId())
                    .tareWeight(record.getTareWeight()).build();
            if (!Objects.equals(WeighMode.MANUAL.getValue(), dto.getWeighMode()) && dto.getDeviceId() != null) {
                EquipmentInfoFeignVO device = FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByEquipmentId(data), dto.getDeviceId()).getData();
                if (device != null) {
                    WeighBalanceEquipment weighBalanceEquipment = ScanDeviceConvert.INSTANCE.convertToEquipment(device);
                    build.setEquipmentCode(device.getCode());
                    build.setEquipmentName(device.getName());
                    build.setEquipmentExpireDate(weighBalanceEquipment.getCalibrateExpiredDate());
                    build.setEquipmentStatus(weighBalanceEquipment.getIsCalibrated());
                }
            }
            weighLogSaveDTOS.add(build);
        }
        // 手动产出不记录称量日志
        if (!dto.getByPiece()) {
            weighLogService.saveLogs(weighLogSaveDTOS);
        }
        this.publishOutputWeighEvent(plan.getId(), false, process.getProcedureStepModelId());

        List<WeighResult.WeighResultItem> resultList = storageMaterialList.stream()
                .map(storageMaterial -> {

                    // 回显组件数据
                    List<OutputWeighRecord> outputWeighRecords = outputWeighRecordMapper.queryRecordListByProcessId(process.getId());
                    ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(process.getProcedureStepModelId());
                    List<OutputWeighRecordComponentView> outputWeighRecordComponentViews = trantoComponentViewList(outputWeighRecords, null);
                    BusinessComponentBatchSaveDTO business = new BusinessComponentBatchSaveDTO();
                    List<ExecuteFormData> results = generateExecuteFormData(plan.getId(), procedureStepModel, process, outputWeighRecordComponentViews, business);
                    executeFormDataService.saveResultsAndHandleRelationComponentData(results, business.transToBaseDTO(), false);

                    WeighResult.WeighResultItem result = new WeighResult.WeighResultItem();
                    result.setStorageMaterialId(storageMaterial.getId());
                    result.setStorageMaterialNo(storageMaterial.getNo());
                    result.setByPiece(byPiece);
                    if (!byPiece) {
                        result.setTareWeight(dto.getTareWeight());
                        result.setGrossWeight(dto.getGrossWeight());
                        result.setNetWeight(dto.getNetWeight());
                        result.setQuantity(dto.getNetWeight());
                    } else {
                        result.setQuantity(dto.getQuantity());
                    }
                    result.setUnit(unitCache.getGlobalUnitName(unitId));
                    result.setContainerName(storageMaterial.getContainer());
                    result.setMaterialPositionName(cargoPosition == null ? null : cargoPosition.getCode() + "-" + cargoPosition.getPosition());
                    return result;
                })
                .collect(Collectors.toList());
        storageMaterialService.batchConfirmSerial(serials);

        // 记录产出称量日志
        storageMaterialPositionLogService.saveLogs(logs);

        return resultList;
    }

    private RecordItemLatestDataQueryDTO getRecordItemLatestDataQueryDTO(Long planId, ProcedureStepModel stepModel, OutputWeighProcess process, ComponentListVO component) {
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

    private void publishOutputWeighEvent(Long planId, boolean sign, Long stepModelId) {
        List<Long> stepModelIdList = selectStepModelIdListByReusable(stepModelId);
        if (sign) {
            List<OutputWeighValidateSignDTO> validateSignDTOS = stepModelIdList.stream().map(item -> {
                OutputWeighValidateSignDTO signDTO = new OutputWeighValidateSignDTO();
                signDTO.setProductPlanId(planId);
                signDTO.setProcedureStepModelId(item);
                return signDTO;
            }).collect(Collectors.toList());
            //校验当前步骤以及复用步骤是否签名完成
            if (!validateComponentSign(validateSignDTOS)) {
                return;
            }
        }
        OutputWeighSignType outputWeighSignType = new OutputWeighSignType(planId, sign, stepModelIdList);
        conditionChangeHandler.refreshConditionResult(outputWeighSignType);
    }

    private List<Long> selectStepModelIdListByReusable(Long stepModelId) {
        ProcedureStepModel model = procedureStepModelMapper.selectById(stepModelId);
        //如果可复用找到当前工艺版本下复用记录项的所有步骤
        if (model.getReusable()) {
            List<ProcedureStepModel> stepModelList = procedureStepModelMapper.queryListByRecordItemIdAndProcessIdAndVersion(model.getRecordItemId(),
                    model.getProcessId(), model.getProcessVersion(), true);
            return CollectionUtils.convertList(stepModelList, ProcedureStepModel::getId);
        }
        //如果不复用只影响当前工步的
        return Collections.singletonList(stepModelId);
    }

    private List<OutputWeighRecordComponentView> trantoComponentViewList(List<OutputWeighRecord> outputWeighRecords, Long materialId) {
        if (CollectionUtil.isEmpty(outputWeighRecords)) {
            return new ArrayList<>();
        }
        Map<Long, StorageMaterialVO> map = storageMaterialService.queryInfoByIds(outputWeighRecords.stream().map(OutputWeighRecord::getStorageMaterialId)
                .collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(StorageMaterialVO::getId, Function.identity(), (k1, k2) -> k1));
        List<OutputWeighRecordComponentView> result = new ArrayList<>();
        for (OutputWeighRecord item : outputWeighRecords) {
            OutputWeighRecordComponentView view = new OutputWeighRecordComponentView();
            StorageMaterialVO storageMaterial = map.get(item.getStorageMaterialId());
            if (storageMaterial != null) {
                if (materialId == null || Objects.equals(materialId, storageMaterial.getMaterialId())) {
                    view.setMaterialId(storageMaterial.getMaterialId());
                    view.setMaterialName(storageMaterial.getMaterialName());
                    view.setMergeCode(storageMaterial.getMergeCode());
                    view.setSpecification(storageMaterial.getMaterialSpecification());
                    view.setMaterialBatchNo(storageMaterial.getMaterialBatchNo());
                    view.setMaterialNo(storageMaterial.getMaterialNo());
                    view.setTareWeight(item.getTareWeight());
                    view.setQuantity(item.getQuantity());
                    view.setGrossWeight(item.getGrossWeight());
                    view.setNetWeight(item.getNetWeight());
                    view.setUnit(unitCache.getGlobalUnitName(item.getUnitId()));
                    view.setUnitId(item.getUnitId());
                    view.setWeigherName(UserUtils.getUsername(item.getWeigherId()));
                    view.setReCheckerName(UserUtils.getUsername(item.getReCheckerId()));
                    view.setWeighTime(item.getWeighTime());
                    view.setWeighSignStatus(item.getSignStatus());
                    result.add(view);
                }
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sign(OutputWeighSignDTO dto) {
        log.info("{}称量签名:{}", LOG_PREFIX, dto);
        OutputWeighProcess process = outputWeighProcessMapper.selectById(dto.getOutputWeighProcessId());
        if (process == null) {
            throw new BmosException(MesResponseCode.OUTPUT_WEIGH_PROCESS_NOT_EXIST);
        }
        // 未签名的记录
        List<OutputWeighRecord> records = outputWeighRecordMapper.queryRecordListByProcessId(process.getId())
                .stream()
                .filter(item -> Objects.equals(item.getSignStatus(), WeighSignStatus.UN_SIGNED))
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(records)) {
            throw new BmosException(MesResponseCode.OUTPUT_WEIGH_RECORD_NO_UNSINGED_RECORD);
        }
//        if (!Objects.equals(SysUserHolder.getUser().getUserId(), process.getWeigherId())) {
//            throw new BmosException(MesResponseCode.WEIGHER_NOT_MATCH);
//        }
//        if (!Objects.equals(dto.getReCheckerId(), process.getReCheckerId())) {
//            throw new BmosException(MesResponseCode.RECHECKER_NOT_MATCH);
//        }
        records.forEach(record -> {
            record.setSignStatus(WeighSignStatus.SIGNED);
            record.setWeigherId(process.getWeigherId());
            record.setReCheckerId(process.getReCheckerId());
        });
        outputWeighRecordMapper.updateBatch(records);
        List<Long> storageMaterialIds =
                records.stream().map(OutputWeighRecord::getStorageMaterialId).collect(Collectors.toList());
        storageMaterialService.signBatchByIdList(storageMaterialIds);
        this.publishOutputWeighEvent(process.getProductPlanId(), true, process.getProcedureStepModelId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeWeigher(OutputChangeWeigherDTO dto) {
        log.info("{}切换称量人:{}", LOG_PREFIX, dto);
        OutputWeighProcess process = outputWeighProcessMapper.selectById(dto.getOutputWeighProcessId());
        if (process == null) {
            throw new BmosException(MesResponseCode.OUTPUT_WEIGH_PROCESS_NOT_EXIST);
        }
        // 未签名的记录
        List<OutputWeighRecord> records = outputWeighRecordMapper.queryRecordListByProcessId(process.getId())
                .stream()
                .filter(item -> Objects.equals(item.getSignStatus(), WeighSignStatus.UN_SIGNED))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(records)) {
            // 已称量物料件需签名后才能更换
            throw new BmosException(MesResponseCode.WEIGH_RECORD_EXIST_UNSINGED_RECORD);
        }
        process.setWeigherId(dto.getWeigherId());
        process.setReCheckerId(dto.getReCheckerId());
        outputWeighProcessMapper.updateById(process);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scrap(OutputScrapDTO dto) {
        log.info("{}称量报废:{}", LOG_PREFIX, dto);
        OutputWeighProcess process = outputWeighProcessMapper.selectById(dto.getOutputWeighProcessId());
        if (process == null) {
            throw new BmosException(MesResponseCode.OUTPUT_WEIGH_PROCESS_NOT_EXIST);
        }
        List<OutputWeighRecord> unsignedList = outputWeighRecordMapper.queryRecordListByProcessId(process.getId())
                .stream()
                .filter(item -> Objects.equals(item.getSignStatus(), WeighSignStatus.UN_SIGNED))
                .collect(Collectors.toList());
        List<Long> unSignedStorageMaterialIds = unsignedList
                .stream()
                .map(OutputWeighRecord::getStorageMaterialId)
                .collect(Collectors.toList());
        if (!CollectionUtil.containsAll(unSignedStorageMaterialIds, dto.getScrapStorageMaterialIdList())) {
            throw new BmosException(MesResponseCode.OUTPUT_CANT_SCRAP_NOT_UNSIGNED_RECORD);
        }
        List<OutputWeighRecord> result = outputWeighRecordMapper.scrapBatch(dto.getScrapStorageMaterialIdList());
        storageMaterialService.scrapBatch(result, process.getWeigherId(), process.getReCheckerId(), dto.getRemark(), process.getProductPlanId());
        storageMaterialService.unbindContainersByIds(dto.getScrapStorageMaterialIdList());

        // 回显组件数据
        List<OutputWeighRecord> outputWeighRecords = outputWeighRecordMapper.queryRecordListByProcessId(process.getId());
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(process.getProcedureStepModelId());
        List<OutputWeighRecordComponentView> outputWeighRecordComponentViews = trantoComponentViewList(outputWeighRecords, null);
        BusinessComponentBatchSaveDTO business = new BusinessComponentBatchSaveDTO();
        List<ExecuteFormData> results = generateExecuteFormData(process.getProductPlanId(), procedureStepModel, process, outputWeighRecordComponentViews, business);
        executeFormDataService.saveResultsAndHandleRelationComponentData(results, business.transToBaseDTO());
    }

    @Override
    public List<OutputMaterialItem> getMiddleMaterialList(Long outputWeighProcessId) {
        OutputWeighProcess process = Optional.ofNullable(outputWeighProcessId)
                .map(processId -> outputWeighProcessMapper.selectById(processId))
                .orElse(null);
        if (process == null) {
            return queryConfigMaterialList(new ArrayList<>(), null);
        }
        List<Long> componentMaterialIds = Optional.of(process)
                .map(OutputWeighProcess::getProcedureStepModelId)
                .map(procedureStepModelMapper::selectById)
                .map(procedureStepModel -> procedureStepConfigMapper.selectComponentConfig(
                        procedureStepModel.getId(),
                        process.getComponentId(),
                        procedureStepModel.getReusable(),
                        procedureStepModel.getProcessId(),
                        procedureStepModel.getProcessVersion()))
                .map(ProcedureStepConfig::getConfigInfo)
                .map(configStr -> JSONUtil.toBean(configStr, ProcedureStepConfigInfo.class))
                .map(ProcedureStepConfigInfo::getFormulaMaterialIds)
                .orElse(null);
        return queryConfigMaterialList(componentMaterialIds, process.getProductPlanId());
    }

    @Override
    public List<OutputMaterialItem> getUnionOriginMaterialList(Long outputWeighProcessId) {

        OutputWeighProcess process = Optional.ofNullable(outputWeighProcessId)
                .map(processId -> outputWeighProcessMapper.selectById(processId))
                .orElse(null);
        if (process == null) {
            return new ArrayList<>();
        }
        List<ChargeRecycleComponent> components = Optional.of(process)
                .map(OutputWeighProcess::getProcedureStepModelId)
                .map(procedureStepModelMapper::selectById)
                .map(procedureStepModel -> chargeRecycleComponentMapper.selectListByProductPlanId(process.getProductPlanId()))
                .orElse(new ArrayList<>());
        if (CollectionUtil.isEmpty(components)) {
            return new ArrayList<>();
        }
        List<StorageMaterialChargeRecycle> storageMaterialChargeRecycles = storageMaterialChargeRecycleMapper.selectByChargeRecycleIds(components.stream()
                .map(ChargeRecycleComponent::getId).collect(Collectors.toList()), ChargeRecycleTypeEnum.CHARGE);
        if (CollectionUtil.isEmpty(storageMaterialChargeRecycles)) {
            return new ArrayList<>();
        }
        Set<Long> materialIds = storageMaterialChargeRecycles
                .stream()
                .map(StorageMaterialChargeRecycle::getMaterialId)
                .collect(Collectors.toSet());

        Map<Long, Set<String>> batchMap = new HashMap<>();
        for (StorageMaterialChargeRecycle storageMaterialChargeRecycle : storageMaterialChargeRecycles) {
            Set<String> batchNos = batchMap.computeIfAbsent(storageMaterialChargeRecycle.getMaterialId(), k -> new HashSet<>());
            batchNos.add(storageMaterialChargeRecycle.getMaterialBatchNo());
        }
        // 分组统计批次编号
        return productMaterialMapper.selectListByBatchIds(materialIds)
                .stream()
                .filter(item -> Objects.equals(item.getCategoryType(), CategoryInfoTypeEnum.RAW_MATERIAL.getValue()))
                .map(item -> {
                    OutputMaterialItem result = ProductMaterialConverter.INSTANCE.convertToOutputItemVO(item);
                    result.setBatchNoList(batchMap.get(item.getId()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public StorageMaterialSimpleBatchVO queryBatchInfo(Long materialId, String batchNo) {
        ProductMaterial productMaterial = productMaterialMapper.selectAllInfoById(materialId);
        if (productMaterial == null) {
            return null;
        }
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchMapper.queryByMaterialIdAndBatchNo(materialId, batchNo);
        if (storageMaterialBatch == null) {
            return StorageMaterialSimpleBatchVO.builder()
                    .expiredDate(Optional.of(productMaterial)
                            .map(ProductMaterial::getExpandInfo)
                            .map(MaterialExpandInfo::getDefaultExpiration)
                            .map(defaultExpiration -> LocalDate.now().plusDays((long) defaultExpiration))
                            .orElse(null))
                    .build();
        }
        return StorageMaterialSimpleBatchVO.builder()
                .storageMaterialBatchId(storageMaterialBatch.getId())
                .storageMaterialBatchNo(storageMaterialBatch.getMaterialBatchNo())
                .expiredDate(storageMaterialBatch.getExpiredDate())
                .build();
    }

    @Override
    public Boolean validateComponentSign(List<OutputWeighValidateSignDTO> validateSignList) {
        if (CollectionUtil.isEmpty(validateSignList)) {
            return true;
        }
        List<Long> productPlanIds = new ArrayList<>();
        List<Long> procedureStepModelIds = new ArrayList<>();
        for (OutputWeighValidateSignDTO dto : validateSignList) {
            if (dto.getProductPlanId() != null) {
                productPlanIds.add(dto.getProductPlanId());
            }
            if (dto.getProcedureStepModelId() != null) {
                procedureStepModelIds.add(dto.getProcedureStepModelId());
            }
        }

        List<OutputWeighProcess> processes = outputWeighProcessMapper.queryList(productPlanIds, procedureStepModelIds);
        if (CollectionUtil.isEmpty(processes)) {
            return true;
        }
        List<Long> processIds = processes.stream()
                .map(OutputWeighProcess::getId)
                .collect(Collectors.toList());
        List<OutputWeighRecord> recordList = outputWeighRecordMapper.queryRecordListByProcessIds(processIds);
        if (CollectionUtil.isEmpty(recordList)) {
            return true;
        }
        OutputWeighRecord unSigned = recordList.stream()
                .filter(item -> Objects.equals(item.getSignStatus(), WeighSignStatus.UN_SIGNED))
                .findAny()
                .orElse(null);
        return unSigned == null;
    }

    private List<OutputMaterialItem> queryConfigMaterialList(List<Long> componentMaterialIds, Long productPlanId) {
        if (CollectionUtil.isNotEmpty(componentMaterialIds)) {
            // 直接根据id列表查询
            return ProductFormulaConverter.INSTANCE.convertToOutputMaterialItem(productFormulaConfigureService.selectByIds(componentMaterialIds));
        }
        return Optional.ofNullable(productPlanId)
                .map(productFormulaConfigureService::getProductFormulaInfoByPlanId)
                .map(ProductFormulaInfo::getMaterials)
                .map(list -> list.stream().filter(item -> Objects.equals(item.getMaterialType(), CategoryInfoTypeEnum.INTERMEDIATE)).collect(Collectors.toList()))
                .map(ProductFormulaConverter.INSTANCE::convertToOutputMaterialItem)
                .orElse(new ArrayList<>());
    }

    private List<ExecuteFormData> generateExecuteFormData(Long productPlanId, ProcedureStepModel procedureStepModel, OutputWeighProcess process, List<OutputWeighRecordComponentView> outputWeighRecords, BusinessComponentBatchSaveDTO business) {
        buildBusinessDTO(procedureStepModel, process, business);
        ComponentListVO component = batchRecordComponentService.selectUsedComponentDetail(procedureStepModel.getRecordVersionId(),
                procedureStepModel.getRecordItemId(), process.getComponentId());
        ProductionDetailInfo info = new ProductionDetailInfo();
        List<ExecuteFormData> results = new ArrayList<>();
        info.setDto(business);
        info.setOutputWeighRecords(outputWeighRecords);
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        ProductFormulaInfo formulaInfo = productFormulaConfigureService.getProductFormulaInfoByPlanId(process.getProductPlanId());
        info.setFormulaInfo(formulaInfo);

        RecordItemLatestDataQueryDTO queryDTO = getRecordItemLatestDataQueryDTO(productPlanId, procedureStepModel, process, component);
        List<FormDataItemVO> recordItemLatestData = executeFormDataService.getRecordItemLatestData(queryDTO);
        info.setFormDataCollection(recordItemLatestData);

        outputWeighComponentStrategy.handleBusinessComponent(results, component, info, configMap, null);
        return sameFieldValueCheck(productPlanId, procedureStepModel, results, process, component);
    }

    /**
     * 检查是否存在相同的值
     *
     * @param productPlanId      生产计划id
     * @param procedureStepModel 工序步骤模型
     * @param results            称量结果所有formData
     * @return
     */
    private List<ExecuteFormData> sameFieldValueCheck(Long productPlanId, ProcedureStepModel procedureStepModel, List<ExecuteFormData> results, OutputWeighProcess process, ComponentListVO component) {
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
                    String value = existFields.get(re.getFieldId());
                    if (StrUtil.isBlank(value) && StrUtil.isBlank(re.getValue())){
                        return false;
                    }
                    return !StrUtil.equals(value, re.getValue());
                })
                .collect(Collectors.toList());
    }

    private void buildBusinessDTO(ProcedureStepModel procedureStepModel, OutputWeighProcess process, BusinessComponentBatchSaveDTO dto) {
        dto.setProductPlanId(process.getProductPlanId());
        Plan plan = planMapper.selectById(process.getProductPlanId());
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

    private BigDecimal checkZero(BigDecimal value) {
        if (value == null) {
            return null;
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return value;
    }
}
