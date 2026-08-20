package com.bmos.mes.service.storage.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.component.ComponentDetail;
import com.bmos.mes.service.components.BusinessComponentManager;
import com.bmos.mes.service.components.comps.StorageMaterialComponentFromDataOPT;
import com.bmos.mes.service.components.dto.FormDataOPT;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.PlatformCodeConstants;
import com.bmos.mes.service.platform.code.dto.BatchConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.code.dto.BatchNextUseCodeDTO;
import com.bmos.mes.service.platform.code.feign.PlatformCodeFeign;
import com.bmos.mes.service.platform.code.vo.BatchNextCodeVO;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.storage.config.service.ICargoPositionService;
import com.bmos.mes.service.storage.config.vo.CargoPositionVO;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.manage.dto.*;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialManageService;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.service.MaterialBatchFieldService;
import com.bmos.mes.service.storage.manage.vo.MaterialBatchFieldVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialManageBatchVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialManageVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialMobileVO;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.bmos.mes.common.enums.record.BusinessComponentTypeEnum.CUSTOM_FIELD;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/27 11:09
 */
@Service
@Slf4j
public class StorageMaterialManageServiceImpl implements IStorageMaterialManageService {

    private static final String LOG_PREFIX = "[物料管理]";

    @Resource
    private IStorageMaterialMapper storageMaterialMapper;

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private ICargoPositionService cargoPositionService;


    @Resource
    private PlatformCodeFeign platformCodeFeign;

    @Resource
    private UnitCache unitCache;

    @Resource
    private IStorageMaterialPositionLogService storageMaterialPositionLogService;

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private MaterialBatchFieldService materialBatchFieldService;

    @Resource
    private BusinessComponentManager businessComponentManager;

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Override
    public CommonPage<StorageMaterialManageBatchVO> queryBatchPage(StorageMaterialBatchManagePageQuery pageQuery) {

        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<StorageMaterialManageBatchVO> page = storageMaterialBatchMapper.queryBatchPage(pageQuery);
        page.forEach(item -> {
            // 单位查询
            CacheUnit unit = unitCache.getGlobalUnit(item.getUnitId());
            if (unit != null) {
                item.setUnit(unit.getUnitName());
                if (!unit.getExtend()) {
                    item.setBasicUnitId(unit.getUnitId());
                    item.setBasicUnit(unit.getUnitName());
                } else {
                    CacheUnit basic = unitCache.getGlobalUnit(unit.getParentUnitId());
                    if (basic != null) {
                        item.setBasicUnitId(basic.getUnitId());
                        item.setBasicUnit(basic.getUnitName());
                    }
                }
            }
            item.setDyingFlag(false);
            item.setExpireFlag(false);
            // 查询临期
            if (ObjectUtils.isNotNull(item.getExpiredDate())) {
                item.setExpireFlag(LocalDate.now().isAfter(item.getExpiredDate()));
                if (ObjectUtils.isNotNull(item.getDyingPeriod())) {
                    LocalDate dyingDate = item.getExpiredDate().minusDays(item.getDyingPeriod());
                    item.setDyingFlag(LocalDate.now().isAfter(dyingDate) || LocalDate.now().isEqual(dyingDate));
                }
            }
        });
        PrecisionHelper.convertUnitRenderList(page);
        return CommonPage.convertPage(page);
    }

    @Override
    public CommonPage<StorageMaterialManageVO> queryPage(StorageMaterialManagePageQuery pageQuery) {

        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<StorageMaterialManageVO> page = storageMaterialMapper.queryPage(pageQuery);
        page.forEach(item -> {
            item.setUnit(unitCache.getGlobalUnitName(item.getUnitId()));
            item.setReserveUserName(UserUtils.getUsername(item.getReserveUserId()));
        });
        PrecisionHelper.convertUnitRenderList(page);
        return CommonPage.convertPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StorageMaterialBatch addBatch(StorageMaterialManageBatchCreateDTO dto) {

        log.info("{}新增批次:{}", LOG_PREFIX, dto);

        ProductMaterial material = productMaterialMapper.selectById(dto.getMaterialId());
        if (material == null) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        StorageMaterialBatch existBatch = storageMaterialBatchMapper.queryByMaterialIdAndBatchNo(dto.getMaterialId(), dto.getMaterialBatchNo());
        if (existBatch != null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_EXIST);
        }
        StorageMaterialBatch batch = new StorageMaterialBatch();
        batch.setMaterialId(material.getId());
        batch.setMaterialBatchNo(dto.getMaterialBatchNo());
        batch.setExpiredDate(dto.getExpiredDate());
        if (dto.getExpiredDate() != null) {
            batch.setAvailable(!LocalDate.now().isAfter(dto.getExpiredDate()));
        }
        batch.setUnitId(material.getUnitId());
        batch.setUnitExtendId(material.getUnitExtendId());
        batch.setFactoryBatchNo(dto.getFactoryBatchNo());
        batch.setHydration(dto.getHydration());
        batch.setNoHydrationContent(dto.getNoHydrationContent());
        batch.setSupplier(dto.getSupplier());
        batch.setProducer(dto.getProducer());
        batch.setProduceDate(dto.getProduceDate());
        batch.setLicenceNo(dto.getLicenceNo());
        batch.setReportNo(dto.getReportNo());
        batch.setOriginalBatchNo(dto.getOriginalBatchNo());
        batch.setQualityStatus(dto.getQualityStatus());
        storageMaterialBatchMapper.insert(batch);
        // 绑定物料自定义字段
        materialBatchFieldService.save(batch.getId(), dto.getMaterialBatchFieldDTOList());
        return batch;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editBatch(StorageMaterialManageEditBatchDTO dto) {

        log.info("{}编辑批次:{}", LOG_PREFIX, dto);

        Long batchId = dto.getStorageMaterialBatchId();
        StorageMaterialBatch inventoryBatch = storageMaterialBatchMapper.selectById(batchId);
        if (inventoryBatch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        storageMaterialBatchMapper.update(null, Wrappers.lambdaUpdate(StorageMaterialBatch.class)
                .eq(StorageMaterialBatch::getId, batchId)
                .set(StorageMaterialBatch::getFactoryBatchNo, dto.getFactoryBatchNo())
                .set(StorageMaterialBatch::getProduceDate, dto.getProduceDate())
                .set(StorageMaterialBatch::getExpiredDate, dto.getExpiredDate())
                .set(StorageMaterialBatch::getAvailable, !LocalDate.now().isAfter(dto.getExpiredDate()))
                .set(StorageMaterialBatch::getHydration, dto.getHydration())
                .set(StorageMaterialBatch::getNoHydrationContent, dto.getNoHydrationContent())
                .set(StorageMaterialBatch::getReportNo, dto.getReportNo())
                .set(StorageMaterialBatch::getLicenceNo, dto.getLicenceNo())
                .set(StorageMaterialBatch::getSupplier, dto.getSupplier())
                .set(StorageMaterialBatch::getProducer, dto.getProducer())
                .set(StorageMaterialBatch::getOriginalBatchNo, dto.getOriginalBatchNo())
                .set(StorageMaterialBatch::getQualityStatus, dto.getQualityStatus())
                // 如果批次有效期至发生改变，需要更新批次的临期提醒标志
                .set(ObjectUtil.notEqual(dto.getExpiredDate(), inventoryBatch.getExpiredDate()),StorageMaterialBatch::getExpireWarningFlag, false)
        );
        // 删除原有的绑定关系
        materialBatchFieldService.delete(batchId);
        // 重新进行绑定
        materialBatchFieldService.save(batchId, dto.getMaterialBatchFieldDTOList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<StorageMaterial> add(StorageMaterialManageCreateDTO dto) {
        Long batchId = dto.getStorageMaterialBatchId();
        StorageMaterialBatch batch = storageMaterialBatchMapper.selectById(batchId);
        if (batch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        if (dto.getPositionId() != null) {
            CargoPositionVO cargoPosition = cargoPositionService.queryInfoById(dto.getPositionId());
            if (cargoPosition == null) {
                throw new BmosException(MesResponseCode.CARGO_POSITION_NOT_EXIST);
            }
        }

        EquipmentInfoFeignVO container = null;
        if (dto.getContainerId() != null) {
            container = FeignUtils.handleRequest(containerId -> equipmentConfigFeign.getConfigByEquipmentId(containerId), dto.getContainerId()).getData();
            if (container == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_NOT_EXIST);
            }
        }

        BatchNextCodeVO batchCode = FeignUtils.handleRequest(data -> platformCodeFeign.getBatchNextUseNo(data), BatchNextUseCodeDTO.builder()
                .code(PlatformCodeConstants.MES_STORAGE_MATERIAL_SERIAL)
                .fields(new HashMap<>())
                .num(dto.getSize())
                .build()).getData();
        List<String> nos = batchCode.getNos()
                .stream().map(BatchNextCodeVO.NextCodeVO::getNo).collect(Collectors.toList());
        List<StorageMaterial> list = new ArrayList<>();
        for (int i = 0; i < dto.getSize(); i++) {
            StorageMaterial storageMaterial = new StorageMaterial();
            storageMaterial.setMaterialId(batch.getMaterialId());
            storageMaterial.setStorageMaterialBatchId(batch.getId());
            storageMaterial.setMaterialPositionId(dto.getPositionId());
            storageMaterial.setNo(nos.get(i));
            BigDecimal initQuantity = unitCache.toBasic(dto.getSingleQuantity(), dto.getSingleUnitId());
            storageMaterial.setInitQuantity(initQuantity);
            storageMaterial.setAvailableQuantity(initQuantity);
            storageMaterial.setConsumeQuantity(BigDecimal.ZERO);
            CacheUnit unit = unitCache.getGlobalUnit(dto.getSingleUnitId());
            if (unit != null) {
                if (unit.getExtend()) {
                    storageMaterial.setUnitId(unitCache.getBaseUnitId(unit.getUnitId()));
                    storageMaterial.setUnitExtendId(unit.getUnitId());
                } else {
                    storageMaterial.setUnitId(unit.getUnitId());
                }
            }
            storageMaterial.setReserveQuantity(BigDecimal.ZERO);
            storageMaterial.setSignStatus(dto.getWeighSignStatus());
            if (container != null) {
                storageMaterial.setContainerId(container.getId());
                storageMaterial.setContainer(container.getCode() + "-" + container.getName());
            }
            list.add(storageMaterial);
        }
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        storageMaterialMapper.insertBatch(list);
        // 确认编码
        FeignUtils.handleRequest(data -> platformCodeFeign.batchConfirmNo(data), BatchConfirmNextUseCodeDTO.builder()
                .code(PlatformCodeConstants.MES_STORAGE_MATERIAL_SERIAL)
                .fields(new HashMap<>())
                .fullNos(nos)
                .build());
        if (dto.getSaveLog()) {
            // 仅生成物料日志
            storageMaterialPositionLogService.saveLogs(list.stream()
                    .map(item -> {
                        StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                        logDTO.setStorageMaterialId(item.getId());
                        logDTO.setMaterialPositionId(item.getMaterialPositionId());
                        logDTO.setOperateType(StorageOperateTypeEnum.ADD);
                        logDTO.setQuantity(unitCache.toExt(item.getInitQuantity(), item.getFinalUnitId()));
                        logDTO.setUnitId(item.getFinalUnitId());
                        logDTO.setSenderId(dto.getOperatorId());
                        logDTO.setReceiverId(dto.getOperatorId());
                        return logDTO;
                    }).collect(Collectors.toList()));
        }
        return list;
    }

    @Override
    public StorageMaterialManageBatchVO queryBatchDetail(Long id) {
        if (id == null) {
            return null;
        }
        StorageMaterialManageBatchVO result = storageMaterialBatchMapper.queryBatchById(id);
        if (result == null) {
            return null;
        }

        // 单位查询
        CacheUnit unit = unitCache.getGlobalUnit(result.getUnitId());
        if (unit != null) {
            result.setUnit(unit.getUnitName());
            if (!unit.getExtend()) {
                result.setBasicUnitId(unit.getUnitId());
                result.setBasicUnit(unit.getUnitName());
            } else {
                CacheUnit basic = unitCache.getGlobalUnit(unit.getParentUnitId());
                if (basic != null) {
                    result.setBasicUnitId(basic.getUnitId());
                    result.setBasicUnit(basic.getUnitName());
                }
            }
        }
        result.setDyingFlag(false);
        result.setExpireFlag(false);
        // 查询临期
        if (ObjectUtils.isNotNull(result.getExpiredDate())) {
            result.setExpireFlag(LocalDate.now().isAfter(result.getExpiredDate()));
            if (ObjectUtils.isNotNull(result.getDyingPeriod())) {
                LocalDate dyingDate = result.getExpiredDate().minusDays(result.getDyingPeriod());
                result.setDyingFlag(LocalDate.now().isAfter(dyingDate) || LocalDate.now().isEqual(dyingDate));
            }
        }
        result.setAvailableQuantity(unitCache.toExt(result.getAvailableQuantity(), result.getUnitId()));
        result.setConsumeQuantity(unitCache.toExt(result.getConsumeQuantity(), result.getUnitId()));
        result.setReserveQuantity(unitCache.toExt(result.getReserveQuantity(), result.getUnitId()));
        result.setInitQuantity(unitCache.toExt(result.getInitQuantity(), result.getUnitId()));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMaterialComponentValue(StorageMaterialComponentDTO dto) {
        BusinessComponentInstance componentInstance = businessComponentManager.getComponentInstanceById(dto.getComponentInstanceId());
        if (componentInstance == null) {
            throw new BmosException(MesResponseCode.COMPONENT_NOT_EXIST);
        }
        log.info("{} 保存物料件组件值:{}, {}", LOG_PREFIX, dto.getComponentInstanceId(), dto.getNo());

        StorageMaterialMobileVO storageMaterial = storageMaterialService.queryInfoByMaterialNo(dto.getNo(), false, null);
        if (storageMaterial == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        StorageMaterialComponentFromDataOPT opt = new StorageMaterialComponentFromDataOPT();
        opt.setMaterialName(storageMaterial.getMaterialName());
        opt.setMaterialCode(storageMaterial.getMaterialCode());
        opt.setMaterialBatchNo(storageMaterial.getMaterialBatchNo());
        opt.setNo(storageMaterial.getMaterialNo());
        opt.setQuantity(storageMaterial.getQuantity());
        if (storageMaterial.getNetWeight() != null) {
            opt.setNetWeight(storageMaterial.getNetWeight().toPlainString());
        }
        if (storageMaterial.getTareWeight() != null) {
            opt.setTareWeight(storageMaterial.getTareWeight().toPlainString());
        }
        if (storageMaterial.getGrossWeight() != null) {
            opt.setGrossWeight(storageMaterial.getGrossWeight().toPlainString());
        }
        opt.setUnit(storageMaterial.getUnit());
        List<FormDataOPT> list = businessComponentManager.getFormDataOPTList(dto.getComponentInstanceId());
        businessComponentManager.fillFormDataOPT(opt, list);
        // 查询自定义字段
        List<MaterialBatchFieldVO> materialField = materialBatchFieldService.queryMaterialField(storageMaterial.getMaterialBatchId());
        if (CollectionUtil.isNotEmpty(materialField)) {
            // 处理自定义字段
            for (FormDataOPT formDataOPT : list) {
                if (StringUtils.equals(formDataOPT.getComponentType(), CUSTOM_FIELD.getValue())) {
                    String componentDetailStr = formDataOPT.getComponentDetail();
                    if (StringUtils.isNotBlank(componentDetailStr)) {
                        ComponentDetail componentDetail = JSON.parseObject(componentDetailStr, ComponentDetail.class);
                        String newValue = materialField.stream()
                                // 过滤自定义字段
                                .filter(item -> StringUtils.equals(item.getFieldType(), componentDetail.getDataSources())
                                        && StringUtils.equals(item.getField(), componentDetail.getFieldData()))
                                .findFirst()
                                .map(MaterialBatchFieldVO::getFieldValue)
                                .orElse(null);
                        formDataOPT.setValue(newValue);
                    }
                }
            }
        }
        businessComponentManager.saveFormDataOPT(list, componentInstance);
    }


    @Override
    public List<StorageMaterialManageBatchVO> queryExpireWarningList() {
        return storageMaterialBatchMapper.selectExpireWarningList();
    }

    @Override
    public void updateBatchExpireFlag(List<Long> batchIds, boolean flag) {
        LambdaUpdateWrapper<StorageMaterialBatch> ul = new UpdateWrapper<StorageMaterialBatch>().lambda();
        ul.set(StorageMaterialBatch::getExpireWarningFlag, flag);
        ul.in(StorageMaterialBatch::getId, batchIds);
        ul.eq(StorageMaterialBatch::getDeleted, false);
        storageMaterialBatchMapper.update(null, ul);
    }
}
