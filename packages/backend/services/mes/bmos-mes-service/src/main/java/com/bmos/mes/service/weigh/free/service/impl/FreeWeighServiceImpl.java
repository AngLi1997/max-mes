package com.bmos.mes.service.weigh.free.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.enums.ingredient.WeighMode;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.ingredient.weigh.dto.WeighLogSaveDTO;
import com.bmos.mes.service.ingredient.weigh.service.WeighLogService;
import com.bmos.mes.service.ingredient.weigh.vo.WeighBalanceEquipment;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.service.ICargoPositionService;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialManageBatchCreateDTO;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialManageService;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.tag.convert.ScanDeviceConvert;
import com.bmos.mes.service.weigh.free.dto.FreeWeighDTO;
import com.bmos.mes.service.weigh.free.dto.FreeWeighHistoryPageQuery;
import com.bmos.mes.service.weigh.free.entity.FreeWeighHistoryDO;
import com.bmos.mes.service.weigh.free.mapper.IFreeWeighHistoryMapper;
import com.bmos.mes.service.weigh.free.service.IFreeWeighService;
import com.bmos.mes.service.weigh.free.vo.FreeWeighHistoryPage;
import com.bmos.mes.service.weigh.free.vo.FreeWeighResult;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.facade.equipment.enums.EquipmentTagCodeEnum;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.bmos.mes.common.enums.ingredient.WeighType.MAIN;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/2/25 10:06
 */
@Service
@Slf4j
public class FreeWeighServiceImpl implements IFreeWeighService {

    private static final String LOG_PREFIX = "[自由称量]";

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Resource
    private IStorageMaterialManageService storageMaterialManageService;

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private ICargoPositionService cargoPositionService;

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Resource
    private UnitCache unitCache;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private IStorageMaterialPositionLogService storageMaterialPositionLogService;

    @Resource
    private WeighLogService weighLogService;

    @Resource
    private IFreeWeighHistoryMapper freeWeighHistoryMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public FreeWeighResult weighAndPrint(FreeWeighDTO dto) {

        log.info("{}开始自由称量打码", LOG_PREFIX);

        ProductMaterial material = productMaterialMapper.selectById(dto.getMaterialId());
        if (material == null){
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }

        // 批次
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchMapper.selectByMaterialIdAndNo(dto.getMaterialId(), dto.getStorageMaterialBatchNo());
        // 判断是否存在批次 不存在则创建
        if (storageMaterialBatch == null) {
            log.info("{}物料批次不存在，新建物料批次:{}", LOG_PREFIX, dto.getStorageMaterialBatchNo());
            StorageMaterialManageBatchCreateDTO batchCreateDTO = new StorageMaterialManageBatchCreateDTO();
            batchCreateDTO.setMaterialId(dto.getMaterialId());
            batchCreateDTO.setMaterialBatchNo(dto.getStorageMaterialBatchNo());
            batchCreateDTO.setExpiredDate(dto.getExpiredDate());
            batchCreateDTO.setOperatorId(dto.getWeigherId());
            storageMaterialBatch = storageMaterialManageService.addBatch(batchCreateDTO);
        } else {
            log.info("{}物料批次存在，直接产出物料批次物料件:{}", LOG_PREFIX, dto.getStorageMaterialBatchNo());
        }


        // 容器
        EquipmentInfoFeignVO container = null;
        if (dto.getContainerId() != null) {
            container = FeignUtils.handleRequest(ctId -> equipmentConfigFeign.getConfigByEquipmentId(ctId), dto.getContainerId()).getData();
        }
        Long containerId = null;
        String containerName = null;
        if (container != null) {
            if (!ObjectUtil.equal(EquipmentStatusCodeEnum.AVAILABLE.getCode(), container.getStatus())) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_NOT_AVAILABLE);
            }
            StorageMaterial storageMaterial = storageMaterialService.selectStorageMaterialByContainerId(containerId);
            if (storageMaterial != null) {
                throw new BmosException(MesResponseCode.CONTAINER_ALREADY_HAS_MATERIAL);
            }

            containerId = container.getId();
            containerName = container.getCode() + "-" + container.getName();
        }

        // 货位
        CargoPosition cargoPosition = null;
        if (dto.getMaterialPositionId() != null) {
            cargoPosition = cargoPositionService.getByIdWithPermission(dto.getMaterialPositionId());
            if (cargoPosition == null) {
                throw new BmosException(MesResponseCode.CARGO_POSITION_NOT_EXIST);
            }
        }

        // 单位
        // 使用传入的单位
        Long unitId = dto.getUnitId();
        CacheUnit globalUnit = unitCache.getGlobalUnit(unitId);

        // 净重
        BigDecimal weight = unitCache.toBasic(dto.getNetWeight(), unitId);

        // 物料件号
        String serial = storageMaterialService.getSerial();
        // 创建物料件
        StorageMaterial storageMaterial = new StorageMaterial();
        if (cargoPosition != null) {
            storageMaterial.setMaterialPositionId(cargoPosition.getId());
        }
        storageMaterial.setMaterialId(storageMaterialBatch.getMaterialId());
        storageMaterial.setStorageMaterialBatchId(storageMaterialBatch.getId());
        storageMaterial.setNo(serial);

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
        storageMaterial.setContainerId(containerId);
        storageMaterial.setContainer(containerName);

        // 批次信息(!!! productId和batchNo可能会对应不上，和产品确认，属于正常情况)
        ProductMaterial product = null;
        if (dto.getProductId() != null){
            product = productMaterialMapper.selectById(dto.getProductId());
            if (product == null){
                throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
            }
            storageMaterial.setProductId(dto.getProductId());
        }
        storageMaterial.setBatchNo(dto.getBatchNo());

        storageMaterialService.save(storageMaterial);

        // 物料日志/货位日志
        storageMaterialPositionLogService.saveLog(StorageMaterialPositionLogDTO.builder()
                .materialPositionId(dto.getMaterialPositionId())
                .storageMaterialId(storageMaterial.getId())
                .operateType(StorageOperateTypeEnum.MATERIAL_WEIGHT)
                .quantity(unitCache.toExt(storageMaterial.getAvailableQuantity(), storageMaterial.getFinalUnitId()))
                .unitId(storageMaterial.getFinalUnitId())
                .senderId(dto.getWeigherId())
                .receiverId(dto.getReCheckerId())
                .tareWeight(dto.getTareWeight())
                .grossWeight(dto.getGrossWeight())
                .build());
        // 称量日志
        WeighLogSaveDTO wl = createWeighLogDTO(dto, storageMaterial, storageMaterialBatch, material, product);
        weighLogService.saveLog(wl);

        // 保存称量历史
        FreeWeighHistoryDO history = new FreeWeighHistoryDO();
        history.setStorageMaterialId(storageMaterial.getId());
        history.setTareWeight(dto.getTareWeight());
        history.setGrossWeight(dto.getGrossWeight());
        history.setNetWeight(dto.getNetWeight());
        history.setUnitId(dto.getUnitId());
        history.setWeigherId(dto.getWeigherId());
        history.setReCheckerId(dto.getReCheckerId());
        if (container != null){
            history.setContainerId(container.getId());
            history.setContainerName(container.getCode() + "-" + container.getName());
        }
        if (cargoPosition != null){
            history.setPositionId(cargoPosition.getId());
            history.setPositionName(cargoPosition.getCode() + "-" + cargoPosition.getPosition());
        }
        history.setWeighTime(LocalDateTime.now());
        history.setWeighMode(CommonEnum.getEnumByValue(WeighMode.class, dto.getWeighMode()));
        history.setDeviceId(dto.getDeviceId());
        freeWeighHistoryMapper.insert(history);


        // 返回结果
        FreeWeighResult freeWeighResult = new FreeWeighResult();
        freeWeighResult.setStorageMaterialNo(storageMaterial.getNo());
        freeWeighResult.setTareWeight(dto.getTareWeight());
        freeWeighResult.setGrossWeight(dto.getGrossWeight());
        freeWeighResult.setNetWeight(dto.getNetWeight());
        freeWeighResult.setUnitId(unitId);
        freeWeighResult.setUnit(unitCache.getGlobalUnitName(unitId));
        freeWeighResult.setContainerName(containerName);
        if (cargoPosition != null){
            freeWeighResult.setCargoPositionName(cargoPosition.getCode() + "-" + cargoPosition.getPosition());
        }
        storageMaterialService.confirmSerial(serial);
        return freeWeighResult;
    }

    @Override
    public List<WeighBalanceEquipment> getBalanceList() {
        List<EquipmentInfoFeignVO> list = FeignUtils.handleRequest(code -> equipmentConfigFeign.getEquipmentConfigByTagCode(code), EquipmentTagCodeEnum.WEIGHING_DEVICE_12020.getCode()).getData();
        return ScanDeviceConvert.INSTANCE.convertToEquipmentList(list);
    }

    @Override
    public CommonPage<FreeWeighHistoryPage> queryHistoryPage(FreeWeighHistoryPageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<FreeWeighHistoryPage> list = freeWeighHistoryMapper.queryHistoryPage(pageQuery);
        return CommonPage.convertPage(list);
    }

    @NotNull
    private WeighLogSaveDTO createWeighLogDTO(FreeWeighDTO dto, StorageMaterial storageMaterial, StorageMaterialBatch storageMaterialBatch, ProductMaterial material, ProductMaterial product) {
        WeighLogSaveDTO log = WeighLogSaveDTO.builder()
                .unitId(dto.getUnitId())
                .weigherId(dto.getWeigherId())
                .reCheckerId(dto.getReCheckerId())
                .weighType(MAIN)
                .netWeight(unitCache.toBasic(dto.getNetWeight(), dto.getUnitId()))
                .grossWeight(unitCache.toBasic(dto.getGrossWeight(), dto.getUnitId()))
                .tareWeight(unitCache.toBasic(dto.getTareWeight(), dto.getUnitId()))
                .weighTime(LocalDateTime.now())
                .materialId(storageMaterial.getMaterialId())
                .storageMaterialId(storageMaterial.getId())
                .materialNo(storageMaterial.getNo())
                .materialBatchNo(storageMaterialBatch.getMaterialBatchNo())
                .build();
        log.setMaterialBatchId(storageMaterialBatch.getId());
        log.setEquipmentId(dto.getContainerId());
        log.setMaterialName(material.getName());
        log.setMaterialMergeCode(material.getMergeCode());
        log.setMaterialType(material.getCategoryType());
        if (product != null){
            log.setProductId(product.getId());
            log.setProductName(product.getName());
            log.setProductMergeCode(product.getMergeCode());
        }
        log.setProductBatchNo(dto.getBatchNo());
        if (!Objects.equals(WeighMode.MANUAL.getValue(), dto.getWeighMode()) && dto.getDeviceId() != null) {
            EquipmentInfoFeignVO device = FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByEquipmentId(data), dto.getDeviceId()).getData();
            if (device != null) {
                WeighBalanceEquipment weighBalanceEquipment = ScanDeviceConvert.INSTANCE.convertToEquipment(device);
                log.setEquipmentCode(device.getCode());
                log.setEquipmentName(device.getName());
                log.setEquipmentExpireDate(weighBalanceEquipment.getCalibrateExpiredDate());
                log.setEquipmentStatus(weighBalanceEquipment.getIsCalibrated());
            }
        }
        return log;
    }

}
