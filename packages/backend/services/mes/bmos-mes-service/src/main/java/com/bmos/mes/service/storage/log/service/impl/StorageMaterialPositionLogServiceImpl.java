package com.bmos.mes.service.storage.log.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.material.MaterialOperationTypeEnum;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.service.inspect.mapper.InspectMapper;
import com.bmos.mes.service.inspect.model.Inspect;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.MaterialLog;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.MaterialLogService;
import com.bmos.mes.service.storage.config.mapper.ICargoPositionMapper;
import com.bmos.mes.service.storage.config.mapper.IStorageMapper;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.model.Storage;
import com.bmos.mes.service.storage.config.service.ICargoPositionService;
import com.bmos.mes.service.storage.log.convert.StorageMaterialPositionLogConvert;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogPageQuery;
import com.bmos.mes.service.storage.log.mapper.IStorageMaterialPositionLogMapper;
import com.bmos.mes.service.storage.log.model.StorageMaterialPositionLog;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.log.vo.StorageMaterialPositionLogVO;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.unit.dto.RemoteQueryDTO;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/21 09:31
 */
@Service
@Slf4j
public class StorageMaterialPositionLogServiceImpl implements IStorageMaterialPositionLogService {

    private static final String LOG_PREFIX = "[货位日志]";

    @Resource
    private IStorageMaterialMapper storageMaterialMapper;

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Resource
    private ICargoPositionMapper cargoPositionMapper;

    @Resource
    private IStorageMapper storageMapper;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private UnitCache unitCache;

    @Resource
    private MaterialLogService materialLogService;

    @Resource
    private ICargoPositionService cargoPositionService;

    @Resource
    IStorageMaterialPositionLogMapper storageMaterialPositionLogMapper;

    @Resource
    private InspectMapper inspectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(StorageMaterialPositionLogDTO dto) {
        saveLogs(Collections.singletonList(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLogs(List<StorageMaterialPositionLogDTO> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        List<StorageMaterial> storageMaterials = storageMaterialMapper.selectBatchIds(list.stream()
                .map(StorageMaterialPositionLogDTO::getStorageMaterialId)
                .collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(storageMaterials)) {
            return;
        }

        // 等待事务提交成功之后再保存日志
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == STATUS_COMMITTED){
                    StorageMaterialResultSet rs = queryMaterialDetailResultSet(storageMaterials);
                    // 总日志条目
                    List<StorageMaterialPositionLog> result = new ArrayList<>();
                    List<MaterialLog> materialLogs = new ArrayList<>();
                    for (StorageMaterialPositionLogDTO dto : list) {
                        // 物料件
                        StorageMaterial storageMaterial = rs.storageMaterialMap.get(dto.getStorageMaterialId());
                        if (storageMaterial == null) {
                            log.error(LOG_PREFIX + "保存货位日志错误，物料件不存在，id:{}", dto.getStorageMaterialId());
                            return;
                        }
                        // 物料批次
                        StorageMaterialBatch batch = rs.batchMap.get(storageMaterial.getStorageMaterialBatchId());
                        // 货位
                        CargoPosition cargoPosition = rs.cargoPositionMap.get(storageMaterial.getMaterialPositionId());
                        // 物料
                        ProductMaterial material = rs.materialMap.get(storageMaterial.getMaterialId());
                        // 操作类型
                        StorageOperateTypeEnum operateType = dto.getOperateType();
                        assembleMaterialLog(dto, storageMaterial, rs, materialLogs);
                        if (dto.getMaterialPositionId() == null){
                            // 没有货位不添加货位日志
                            continue;
                        }
                        if (operateType.getName() == null){
                            // 只添加物料日志，不添加货位日志
                            continue;
                        }
                        for (int i = 0; i < operateType.getSubTypes().length; i++) {
                            String subType = operateType.getSubTypes()[i];
                            StorageMaterialPositionLog log = new StorageMaterialPositionLog();
                            log.setMaterialId(storageMaterial.getMaterialId());
                            log.setMaterialNo(storageMaterial.getNo());
                            if (cargoPosition == null) {
                                cargoPosition = cargoPositionMapper.selectById(dto.getMaterialPositionId());
                            }
                            if (cargoPosition != null){
                                log.setMaterialPositionId(cargoPosition.getId());
                                log.setStorageId(cargoPosition.getStorageId());
                                log.setMaterialPositionName(cargoPosition.getPosition());
                                log.setMaterialPositionCode(cargoPosition.getCode());
                                log.setMaterialPositionPath(queryMaterialPositionPath(cargoPosition.getIdPath()));
                            }
                            if (batch != null) {
                                log.setMaterialBatchNo(batch.getMaterialBatchNo());
                            }
                            if (material != null) {
                                log.setMaterialName(material.getName());
                                log.setMaterialCode(material.getMergeCode());
                            }
                            log.setOperateTime(LocalDateTime.now());
                            if (Objects.equals(operateType, StorageOperateTypeEnum.MOVE) && i == 0) {
                                log.setOperationType(StorageOperateTypeEnum.OUTBOUND);
                            } else if (Objects.equals(operateType, StorageOperateTypeEnum.MOVE) && i == 1) {
                                log.setOperationType(StorageOperateTypeEnum.INBOUND);
                            } else {
                                log.setOperationType(operateType);
                            }
                            if (StrUtil.isNotBlank(subType)){
                                log.setOperateDetail(operateType.getPrefix() + "-" + subType);
                            }else{
                                log.setOperateDetail(operateType.getPrefix());
                            }
                            log.setOperatorName(Optional.ofNullable(i == 0 ? dto.getSenderId() : dto.getReceiverId())
                                    .map(UserUtils::getUsername)
                                    .orElse(null));
                            log.setQuantity(dto.getQuantity());
                            log.setUnit(Optional.ofNullable(unitCache.getGlobalUnit(storageMaterial.getFinalUnitId()))
                                    .map(CacheUnit::getUnitName)
                                    .orElse(null));
                            if (dto.getQuantity() != null && storageMaterial.getFinalUnitId() != null){
                                log.setQuantity(PrecisionHelper.precision(log.getQuantity(), storageMaterial.getFinalUnitId()));
                            }
                            log.setRemark(dto.getRemark());
                            log.setProductName(dto.getProductName());
                            log.setProductCode(dto.getProductCode());
                            log.setProductBatchNo(dto.getProductBatchNo());
                            result.add(log);
                        }
                    }
                    if (CollectionUtil.isNotEmpty(materialLogs)) {
                        handleMaterialLogUnit(materialLogs);
                        materialLogService.saveMaterialLogs(materialLogs);
                    }
                    SysUser user = SysUserHolder.getUser();
                    if (CollectionUtil.isNotEmpty(result)) {
                        for (StorageMaterialPositionLog storageMaterialPositionLog : result) {
                            storageMaterialPositionLog.setId(CustomIdGenerator.nextId());
                            storageMaterialPositionLog.setCreateTime(LocalDateTime.now());
                            storageMaterialPositionLog.setUpdateTime(LocalDateTime.now());
                            storageMaterialPositionLog.setCreateBy(user.getUserId());
                            storageMaterialPositionLog.setUpdateBy(user.getUserId());
                        }
                        storageMaterialPositionLogMapper.saveOrUpdateBatch(result);
                    }
                    log.info("物料预定日志记录完成");
                }else {
                    log.info("事务回滚，物料预定日志记录失败");
                }
            }
            @Override
            public int getOrder() {
                return TransactionSynchronization.super.getOrder();
            }
        });
    }

    private void handleMaterialLogUnit(List<MaterialLog> materialLogs) {
        List<Long> unitIds = materialLogs.stream().map(MaterialLog::getUnitId).collect(Collectors.toList());
        RemoteQueryDTO remoteQueryDTO = new RemoteQueryDTO();
        remoteQueryDTO.setUnitExtendIds(unitIds);
        remoteQueryDTO.setUnitIds(unitIds);
        materialLogs.forEach(materialLog -> {
            materialLog.setUnitName(unitCache.getGlobalUnitName(materialLog.getUnitId()));
        });
    }

    private void assembleMaterialLog(StorageMaterialPositionLogDTO dto, StorageMaterial storageMaterial,
                                     StorageMaterialResultSet rs, List<MaterialLog> materialLogs) {
        // 物料批次
        StorageMaterialBatch batch = rs.batchMap.get(storageMaterial.getStorageMaterialBatchId());
        // 货位
        CargoPosition cargoPosition = rs.cargoPositionMap.get(storageMaterial.getMaterialPositionId());
        // 物料
        ProductMaterial material = rs.materialMap.get(storageMaterial.getMaterialId());
        // 检验
        Inspect inspect = rs.inspectMap.get(batch == null ? storageMaterial.getMaterialId() : batch.getMaterialBatchNo() + "-" + batch.getMaterialId());
        StorageOperateTypeEnum operateType = dto.getOperateType();
        for (int i = 0; i < operateType.getSubTypes().length; i++) {
            MaterialLog log = new MaterialLog();
            log.setMaterialPositionId(storageMaterial.getMaterialPositionId());
            log.setMaterialId(storageMaterial.getMaterialId());
            log.setMaterialNo(storageMaterial.getNo());
            log.setRemark(dto.getRemark());
            if (cargoPosition != null) {
                log.setStorageId(cargoPosition.getStorageId());
                log.setMaterialPositionName(cargoPosition.getPosition());
                log.setMaterialPositionCode(cargoPosition.getCode());
                log.setMaterialPositionPath(queryMaterialPositionPath(cargoPosition.getIdPath()));
            }
            if (batch != null) {
                log.setMaterialBatchNo(batch.getMaterialBatchNo());
                log.setMaterialBatchId(batch.getId());
                log.setOriginalCode(batch.getOriginalBatchNo());
                log.setOriginalNo(batch.getFactoryBatchNo());
                log.setExpirationTime(batch.getExpiredDate().toString());
                log.setEnable(batch.getAvailable());
                log.setSupplier(batch.getSupplier());
                log.setProducer(batch.getProducer());
                log.setQualityStatus(batch.getQualityStatus());
            }
            if (material != null) {
                log.setMaterialName(material.getName());
                log.setMaterialCode(material.getMergeCode());
                log.setExpandInfo(material.getExpandInfo());
                log.setCategoryType(material.getCategoryType());
            }
            if (inspect != null) {
                log.setInspectId(inspect.getId());
                log.setRequestVerifyNo(inspect.getInspectNo());
            }
            log.setOperationTime(LocalDateTime.now());
            String userId = i == 0 ? dto.getSenderId() : dto.getReceiverId();
            BaseUserDO baseUserDO = Optional.ofNullable(userId).map(UserUtils::getUser).orElse(new BaseUserDO());
            log.setUserId(baseUserDO.getUserId());
            log.setUserName(baseUserDO.getUserName());
            log.setLoginName(baseUserDO.getLoginName());
            log.setScheduled(unitCache.toExt(storageMaterial.getReserveQuantity(), storageMaterial.getFinalUnitId()).stripTrailingZeros().toPlainString());
            log.setAvailable(unitCache.toExt(storageMaterial.getAvailableQuantity(), storageMaterial.getFinalUnitId()).stripTrailingZeros().toPlainString());
            log.setUnitId(storageMaterial.getFinalUnitId());
            log.setStorageMaterialId(storageMaterial.getId());
            log.setProductId(dto.getProductId());
            log.setProductName(dto.getProductName());
            log.setProductMergeCode(dto.getProductCode());
            log.setBatchNo(dto.getProductBatchNo());
            if (dto.getGrossWeight() != null){
                log.setGrossWeight(dto.getGrossWeight().toPlainString());
            }
            if (dto.getTareWeight() != null){
                log.setTareWeight(dto.getTareWeight().toPlainString());
            }
            switch (operateType) {
                case INBOUND:
                    log.setOperationType(MaterialOperationTypeEnum.IN_STORAGE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.IN_STORAGE.getSpecificTypes()[i].getValue());
                    break;
                case OUTBOUND:
                    log.setOperationType(MaterialOperationTypeEnum.OUT_STORAGE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.OUT_STORAGE.getSpecificTypes()[i].getValue());
                    break;
                case SEND_BACK:
                    // 入库记录入库
                    log.setOperationType(MaterialOperationTypeEnum.IN_STORAGE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.IN_STORAGE.getSpecificTypes()[i].getValue());
                    break;
                case MOVE:
                    // 移库记录出入库
                    log.setOperationType(i == 0 ? MaterialOperationTypeEnum.OUT_STORAGE.getValue() : MaterialOperationTypeEnum.IN_STORAGE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.MOVE_STORAGE.getSpecificTypes()[i].getValue());
                    break;
                case MINUS:
                case PLUS:
                    log.setOperationType(MaterialOperationTypeEnum.CHECK_STORAGE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.CHECK_STORAGE.getSpecificTypes()[i].getValue());
                    break;
                case RESERVE:
                    log.setOperationType(MaterialOperationTypeEnum.RESERVE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.RESERVE.getSpecificTypes()[i].getValue());
                    break;
                case CANCEL_RESERVE:
                    log.setOperationType(MaterialOperationTypeEnum.CANCEL_RESERVE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.CANCEL_RESERVE.getSpecificTypes()[i].getValue());
                    break;
                case SPLIT_PACKAGE:
                    log.setOperationType(MaterialOperationTypeEnum.OUT_STORAGE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.SPLIT_PACKAGE.getSpecificTypes()[i].getValue());
                    break;
                case SPLIT_PACKAGE_NEW:
                    log.setOperationType(MaterialOperationTypeEnum.SPLIT_PACKAGE_NEW.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.SPLIT_PACKAGE_NEW.getSpecificTypes()[i].getValue());
                    break;
                case WEIGH_CONSUME:
                    log.setOperationType(MaterialOperationTypeEnum.WEIGH_CONSUME.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.WEIGH_CONSUME.getSpecificTypes()[i].getValue());
                    break;
                case MATERIAL_WEIGH_CONSUME:
                    log.setOperationType(MaterialOperationTypeEnum.MATERIAL_WEIGH_CONSUME.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.MATERIAL_WEIGH_CONSUME.getSpecificTypes()[i].getValue());
                    break;
                case MATERIAL_ODD_WEIGH_CONSUME:
                    log.setOperationType(MaterialOperationTypeEnum.MATERIAL_ODD_WEIGH_CONSUME.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.MATERIAL_ODD_WEIGH_CONSUME.getSpecificTypes()[i].getValue());
                    break;
                case MEASURE_CONSUME:
                    log.setOperationType(MaterialOperationTypeEnum.MEASURE_CONSUME.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.MEASURE_CONSUME.getSpecificTypes()[i].getValue());
                    break;
                case ADD:
                    log.setOperationType(MaterialOperationTypeEnum.ADD.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.ADD.getSpecificTypes()[i].getValue());
                    break;
                case WEIGH_SCRAP:
                    log.setOperationType(MaterialOperationTypeEnum.WEIGH_SCRAP.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.WEIGH_SCRAP.getSpecificTypes()[i].getValue());
                    break;
                case CHARGE:
                    log.setOperationType(MaterialOperationTypeEnum.CHARGE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.CHARGE.getSpecificTypes()[i].getValue());
                    break;
                case RECYCLE:
                    log.setOperationType(MaterialOperationTypeEnum.RECYCLE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.RECYCLE.getSpecificTypes()[i].getValue());
                    break;
                case REQUISITION_RECEIVE:
                    log.setOperationType(MaterialOperationTypeEnum.REQUISITION_RECEIVE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.REQUISITION_RECEIVE.getSpecificTypes()[i].getValue());
                    break;
                case INGREDIENT_WEIGHT:
                    log.setOperationType(MaterialOperationTypeEnum.INGREDIENT_WEIGHT.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.INGREDIENT_WEIGHT.getSpecificTypes()[i].getValue());
                    break;
                case MATERIAL_WEIGHT:
                    log.setOperationType(MaterialOperationTypeEnum.MATERIAL_WEIGHT.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.MATERIAL_WEIGHT.getSpecificTypes()[i].getValue());
                    break;
                case MATERIAL_ODD_WEIGHT:
                    log.setOperationType(MaterialOperationTypeEnum.MATERIAL_ODD_WEIGHT.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.MATERIAL_ODD_WEIGHT.getSpecificTypes()[i].getValue());
                    break;
                case MATERIAL_RESERVE:
                    log.setOperationType(MaterialOperationTypeEnum.REQUISITION_RESERVE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.REQUISITION_RESERVE.getSpecificTypes()[i].getValue());
                    break;
                case BATCHING_INPUT:
                    log.setOperationType(MaterialOperationTypeEnum.BATCHING_INPUT.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.BATCHING_INPUT.getSpecificTypes()[i].getValue());
                    break;
                case OUTPUT_WEIGHT:
                    log.setOperationType(MaterialOperationTypeEnum.OUTPUT_WEIGHT.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.OUTPUT_WEIGHT.getSpecificTypes()[i].getValue());
                    break;
                case MATERIAL_CANCEL_RESERVE:
                    log.setOperationType(MaterialOperationTypeEnum.MATERIAL_CANCEL_RESERVE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.MATERIAL_CANCEL_RESERVE.getSpecificTypes()[i].getValue());
                    break;
                case PREPARATION_INPUT:
                    log.setOperationType(MaterialOperationTypeEnum.PREPARATION_INPUT.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.PREPARATION_INPUT.getSpecificTypes()[i].getValue());
                    break;
                case MATERIAL_INPUT:
                    log.setOperationType(MaterialOperationTypeEnum.MATERIAL_INPUT.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.MATERIAL_INPUT.getSpecificTypes()[i].getValue());
                    break;
                case PREPARATION_PRODUCE:
                    log.setOperationType(MaterialOperationTypeEnum.PREPARATION_PRODUCE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.PREPARATION_PRODUCE.getSpecificTypes()[i].getValue());
                    break;
                case PREPARATION_SCRAP:
                    log.setOperationType(MaterialOperationTypeEnum.PREPARATION_PRODUCE_SCRAP.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.PREPARATION_PRODUCE_SCRAP.getSpecificTypes()[i].getValue());
                    break;
                case SEND_BACK_AND_CONSUME:
                    log.setOperationType(MaterialOperationTypeEnum.SEND_BACK_AND_CONSUME.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.SEND_BACK_AND_CONSUME.getSpecificTypes()[i].getValue());
                    break;
                case DESTROY_AND_CONSUME:
                    log.setOperationType(MaterialOperationTypeEnum.DESTROY_AND_CONSUME.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.DESTROY_AND_CONSUME.getSpecificTypes()[i].getValue());
                    break;
                case USE_AND_CONSUME:
                    log.setOperationType(MaterialOperationTypeEnum.USE_AND_CONSUME.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.USE_AND_CONSUME.getSpecificTypes()[i].getValue());
                    break;
                case RECEIVE:
                    log.setOperationType(MaterialOperationTypeEnum.RECEIVE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.RECEIVE.getSpecificTypes()[i].getValue());
                    break;
                case MANUAL_OUTPUT:
                    log.setOperationType(MaterialOperationTypeEnum.MANUAL_OUTPUT.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.MANUAL_OUTPUT.getSpecificTypes()[i].getValue());
                    break;
                case MEASURE_WEIGH:
                    log.setOperationType(MaterialOperationTypeEnum.PREPARATION_MEASURE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.PREPARATION_MEASURE.getSpecificTypes()[i].getValue());
                    break;
                case MEASURE_ODD:
                    log.setOperationType(MaterialOperationTypeEnum.PREPARATION_MEASURE_ODD.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.PREPARATION_MEASURE_ODD.getSpecificTypes()[i].getValue());
                    break;
                case WEIGH_TICKET_POSITION_EXECUTE:
                    log.setOperationType(MaterialOperationTypeEnum.WEIGH_TICKET_MATERIAL_EXECUTE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.WEIGH_TICKET_MATERIAL_EXECUTE.getSpecificTypes()[i].getValue());
                    break;
                case WEIGH_TICKET_POSITION_ODD_EXECUTE:
                    log.setOperationType(MaterialOperationTypeEnum.WEIGH_TICKET_MATERIAL_ODD_EXECUTE.getValue());
                    log.setSpecificOperationType(MaterialOperationTypeEnum.WEIGH_TICKET_MATERIAL_ODD_EXECUTE.getSpecificTypes()[i].getValue());
                    break;
                default:
                    break;
            }
            materialLogs.add(log);
        }

    }

    @Nullable
    private String queryMaterialPositionPath(String idPath) {
        if (StrUtil.isBlank(idPath)) {
            return null;
        }
        String materialPositionPath = null;
        String[] split = idPath.split(",");
        List<Long> storageIds = Optional.of(split)
                .map(p -> Arrays.stream(p).map(Long::parseLong).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        if (CollectionUtil.isNotEmpty(storageIds)) {
            List<Storage> storages = storageMapper.queryListByIds(storageIds);
            Map<String, String> map = storages.stream().collect(Collectors.toMap(item -> item.getId().toString(), Storage::getName, (k1, k2) -> k1));
            List<String> names = new ArrayList<>();
            for (String id : split) {
                names.add(map.get(id));
            }
            materialPositionPath = StrUtil.join("/", names);
        }
        return materialPositionPath;
    }

    @Override
    public CommonPage<StorageMaterialPositionLogVO> queryPage(StorageMaterialPositionLogPageQuery pageQuery) {
        List<Long> positionIdList = new ArrayList<>();
        Long positionId = pageQuery.getMaterialPositionId();
        if (positionId == null || storageMapper.selectById(positionId) != null) {
            List<CargoPosition> cargoPositions = cargoPositionService.queryAllEnabledChildrenByStorageId(positionId);
            if (CollectionUtil.isEmpty(cargoPositions)) {
                return CommonPage.CommonPage(Collections.emptyList(), 0L, pageQuery);
            }
            List<Long> collect = cargoPositions.stream().map(CargoPosition::getId).collect(Collectors.toList());
            collect.add(positionId);
            positionIdList.addAll(collect);
        } else {
            positionIdList.add(positionId);
            // 传的是货位id
        }
        if (CollectionUtil.isEmpty(positionIdList)){
            return CommonPage.CommonPage(Collections.emptyList(), 0L, pageQuery);
        }
        if (pageQuery.getProductId() != null){
            ProductMaterial productMaterial = productMaterialMapper.selectById(pageQuery.getProductId());
            Optional.ofNullable(productMaterial).ifPresent(item -> {
                pageQuery.setProductName(item.getName());
                pageQuery.setProductCode(item.getCode());
            });
        }
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<StorageMaterialPositionLog> list = storageMaterialPositionLogMapper.queryPage(pageQuery, positionIdList);
        CommonPage<StorageMaterialPositionLog> page = CommonPage.convertPage(list);
        return StorageMaterialPositionLogConvert.INSTANCE.convertPage(page);

    }

    /**
     * 查询物料件相关结果集
     *
     * @param storageMaterials 物料件列表
     * @return
     */
    private StorageMaterialResultSet queryMaterialDetailResultSet(List<StorageMaterial> storageMaterials) {
        // 物料件信息
        Map<Long, StorageMaterial> storageMaterialMap = storageMaterials.stream()
                .collect(Collectors.toMap(StorageMaterial::getId, Function.identity(), (k1, k2) -> k1));
        // 物料批次
        List<Long> batchIdList = storageMaterials.stream()
                .map(StorageMaterial::getStorageMaterialBatchId)
                .collect(Collectors.toList());
        List<StorageMaterialBatch> batchList = storageMaterialBatchMapper.selectBatchIds(batchIdList);
        Map<Long, StorageMaterialBatch> batchMap = batchList
                .stream()
                .collect(Collectors.toMap(StorageMaterialBatch::getId, Function.identity(), (k1, k2) -> k1));
        // 货位信息
        Map<Long, CargoPosition> cargoPositionMap = cargoPositionMapper.selectBatchIds(storageMaterials.stream()
                        .map(StorageMaterial::getMaterialPositionId)
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(CargoPosition::getId, Function.identity(), (k1, k2) -> k1));
        // 物料信息
        Map<Long, ProductMaterial> materialMap = productMaterialMapper.selectListByBatchIds(storageMaterials.stream()
                        .map(StorageMaterial::getMaterialId)
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(ProductMaterial::getId, Function.identity(), (k1, k2) -> k1));
        // 请验信息
        Map<String, Inspect> inspectMap = inspectMapper.selectByBatchNoAndMaterialIdList(CollectionUtils.convertSet(batchList, StorageMaterialBatch::getMaterialBatchNo)
                , CollectionUtils.convertSet(batchList, StorageMaterialBatch::getMaterialId))
                .stream()
                .collect(Collectors.toMap(
                        // Key组合：materialBatchNo-materialId
                        inspect -> inspect.getMaterialBatchNo() + "-" + inspect.getMaterialId(),
                        Function.identity(),
                        (existing, replacement) ->
                                replacement.getCreateTime().isAfter(existing.getCreateTime()) ? replacement : existing
                ));

        return new StorageMaterialResultSet(storageMaterialMap, batchMap, cargoPositionMap, materialMap, inspectMap);
    }

    /**
     * 物料件结果集
     */
    private static class StorageMaterialResultSet {

        /**
         * 物料件结果集
         */
        public final Map<Long, StorageMaterial> storageMaterialMap;

        /**
         * 物料批次结果集
         */
        public final Map<Long, StorageMaterialBatch> batchMap;

        /**
         * 货位结果集
         */
        public final Map<Long, CargoPosition> cargoPositionMap;

        /**
         * 物料信息结果集
         */
        public final Map<Long, ProductMaterial> materialMap;

        /**
         * 请验信息结果集 batchNo-MaterialId:inspect
         */
        private final Map<String, Inspect> inspectMap;

        public StorageMaterialResultSet(Map<Long, StorageMaterial> storageMaterialMap, Map<Long, StorageMaterialBatch> batchMap, Map<Long, CargoPosition> cargoPositionMap, Map<Long, ProductMaterial> materialMap, Map<String, Inspect> inspectMap) {
            this.storageMaterialMap = storageMaterialMap;
            this.batchMap = batchMap;
            this.cargoPositionMap = cargoPositionMap;
            this.materialMap = materialMap;
            this.inspectMap = inspectMap;
        }
    }

    public Long generateRandomTimeStamp(){
        String startDateStr = "2023-08-10 00:00:00";
        String endDateStr = "2024-09-20 23:59:59";

        // 创建SimpleDateFormat对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            // 将字符串转换为Date对象
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            // 将Date对象转换为毫秒时间戳
            long startTime = startDate.getTime();
            long endTime = endDate.getTime();

            // 生成随机时间戳
            long randomTimestamp = generateRandomTimestamp(startTime, endTime);

            return randomTimestamp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 10000000L;
    }

    /**
     * 生成指定范围内的随机时间戳
     * @param startTime 起始时间戳
     * @param endTime 结束时间戳
     * @return 随机时间戳
     */
    public static long generateRandomTimestamp(long startTime, long endTime) {
        Random random = new Random();
        return startTime + (long) (random.nextDouble() * (endTime - startTime));
    }
}
