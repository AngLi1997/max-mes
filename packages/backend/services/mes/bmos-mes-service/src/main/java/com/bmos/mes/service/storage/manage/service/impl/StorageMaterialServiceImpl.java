package com.bmos.mes.service.storage.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.component.BasicComponentConfig;
import com.bmos.mes.service.formula.mapper.ProductFormulaMaterialMapper;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.ingredient.weigh.mapper.IIngredientWeighRecordMapper;
import com.bmos.mes.service.ingredient.weigh.model.IngredientWeighRecord;
import com.bmos.mes.service.output.weigh.mapper.IOutputWeighRecordMapper;
import com.bmos.mes.service.output.weigh.model.OutputWeighRecord;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.model.ProductPlanRelation;
import com.bmos.mes.service.plan.info.service.ProductPlanRelationService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.PlatformCodeConstants;
import com.bmos.mes.service.platform.code.dto.BatchConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.code.dto.BatchNextUseCodeDTO;
import com.bmos.mes.service.platform.code.dto.ConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.code.dto.NextUseCodeDTO;
import com.bmos.mes.service.platform.code.feign.PlatformCodeFeign;
import com.bmos.mes.service.platform.code.vo.BatchNextCodeVO;
import com.bmos.mes.service.process.mapper.ProcessMapper;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.condition.event.MaterialReserveType;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.MaterialExpandInfo;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.requisition.dto.AvailableStorageMaterialQueryDTO;
import com.bmos.mes.service.requisition.vo.BatchAvailableMaterialVO;
import com.bmos.mes.service.storage.config.mapper.ICargoPositionMapper;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.service.ICargoPositionService;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.manage.convert.StorageMaterialConverter;
import com.bmos.mes.service.storage.manage.dto.*;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialReserveMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.model.StorageMaterialReserve;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.service.MaterialBatchFieldService;
import com.bmos.mes.service.storage.manage.vo.*;
import com.bmos.mes.service.tag.dto.ScanDeviceCodeValidateStationDTO;
import com.bmos.mes.service.tag.dto.ScanMaterialDeviceCodeDTO;
import com.bmos.mes.service.tag.enums.CodeType;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;
import com.bmos.mes.service.tag.vo.ScanMaterialVO;
import com.bmos.mes.service.utils.BigDecimalFormatUtil;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mes.service.weigh.centre.execute.mapper.IWeighExecuteWeighRecordMapper;
import com.bmos.mes.service.weigh.centre.execute.model.WeighExecuteWeighRecord;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.facade.equipment.enums.EquipmentTagCodeEnum;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.TagFeignVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

import static com.bmos.mes.common.constant.StorageConstant.MAX_BOUND_SIZE;
import static com.bmos.mes.common.enums.record.CustomFieldDatasourceEnum.*;
import static com.bmos.mes.common.enums.storage.StorageOperateTypeEnum.*;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/19 15:44
 */
@Service
@Slf4j
public class StorageMaterialServiceImpl implements IStorageMaterialService {

    private static final String LOG_PREFIX = "[暂存物料件]";

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;
    @Resource
    private IStorageMaterialMapper storageMaterialMapper;

    @Resource
    private IStorageMaterialReserveMapper storageMaterialReserveMapper;

    @Resource
    private IStorageMaterialPositionLogService storageMaterialPositionLogService;

    @Resource
    private ICargoPositionMapper cargoPositionMapper;

    @Resource
    private ProductMaterialMapper materialMapper;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private ICargoPositionService cargoPositionService;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private ProcedureStepConfigService procedureStepConfigService;

    @Resource
    private ProductFormulaMaterialMapper formulaMaterialMapper;

    @Resource
    private UnitCache unitCache;
    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private PlatformCodeFeign platformCodeFeign;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Resource
    private ITaskConditionCalculator conditionChangeHandler;

    @Resource
    private MaterialBatchFieldService materialBatchFieldService;

    @Resource
    private IIngredientWeighRecordMapper ingredientWeighRecordMapper;

    @Resource
    private IOutputWeighRecordMapper outputWeighRecordMapper;

    @Resource
    private IWeighExecuteWeighRecordMapper weighExecuteWeighRecordMapper;

    @Resource
    private FactoryFeign factoryFeign;

    @Resource
    private ProductPlanRelationService productPlanRelationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inbound(StorageMaterialInboundDTO dto) {
        log.info("{}物料入库:{}", LOG_PREFIX, dto);
        int size = dto.getInboundList().size();
        ProductMaterial material = materialMapper.selectById(dto.getMaterialId());
        if (material == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        if (size > MAX_BOUND_SIZE) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_INBOUND_SIZE_EXCEED);
        }
        StorageMaterialBatch existBatch =
                storageMaterialBatchMapper.queryByMaterialIdAndBatchNo(dto.getMaterialId(),
                        dto.getMaterialBatchNo());
        Long batchId;
        if (existBatch != null) {
            // 比对有效期、原始批号、是否匹配
            if (!(Objects.equals(existBatch.getOriginalBatchNo(), dto.getOriginalBatchNo())
                    && Objects.equals(existBatch.getExpiredDate(), dto.getExpiredDate()))) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_EXIST);
            }
            // 沿用批次
            batchId = existBatch.getId();
        } else {
            // 新增批次
            StorageMaterialBatch batch = new StorageMaterialBatch();
            batch.setMaterialId(dto.getMaterialId());
            batch.setUnitId(material.getUnitId());
            batch.setUnitExtendId(material.getUnitExtendId());
            batch.setMaterialBatchNo(dto.getMaterialBatchNo());
            batch.setOriginalBatchNo(dto.getOriginalBatchNo());
            batch.setExpiredDate(dto.getExpiredDate());
            batch.setAvailable(!LocalDate.now().isAfter(dto.getExpiredDate()));
            batch.setLinkExplain(dto.getLinkExplain());
            batch.setSenderId(dto.getSenderId());
            batch.setReceiverId(dto.getReceiverId());
            storageMaterialBatchMapper.insert(batch);
            batchId = batch.getId();
        }
        List<StorageMaterial> list = new ArrayList<>();
        // 保存物料件信息
        for (StorageMaterialInboundDTO.InboundDTO inboundDTO : dto.getInboundList()) {
            for (int i = 0; i < inboundDTO.getSize(); i++) {
                StorageMaterial storageMaterial = new StorageMaterial();
                storageMaterial.setMaterialId(dto.getMaterialId());
                storageMaterial.setMaterialPositionId(dto.getMaterialPositionId());
                storageMaterial.setStorageMaterialBatchId(batchId);
                if (inboundDTO.getUnitExtendId() != null) {
                    storageMaterial.setUnitExtendId(inboundDTO.getUnitExtendId());
                    storageMaterial.setUnitId(Optional.ofNullable(inboundDTO.getUnitExtendId())
                            .map(unitCache::getGlobalUnit)
                            .map(CacheUnit::getParentUnitId)
                            .orElse(null)
                    );
                } else {
                    storageMaterial.setUnitId(inboundDTO.getUnitId());
                }
                storageMaterial.setInitQuantity(unitCache.toBasic(inboundDTO.getSingleQuantity(),
                        storageMaterial.getFinalUnitId()));
                storageMaterial.setAvailableQuantity(storageMaterial.getInitQuantity());
                storageMaterial.setConsumeQuantity(BigDecimal.ZERO);
                storageMaterial.setReserveQuantity(BigDecimal.ZERO);
                list.add(storageMaterial);
            }
        }
        List<String> nos = batchGetSerial(list.size());
        Iterator<String> iterator = nos.iterator();
        list.forEach(item -> item.setNo(iterator.next()));
        ;
        storageMaterialMapper.insertBatch(list);


        // 保存入库日志
        storageMaterialPositionLogService.saveLogs(list.stream()
                .map(item -> {
                    StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                    logDTO.setStorageMaterialId(item.getId());
                    logDTO.setOperateType(StorageOperateTypeEnum.INBOUND);
                    logDTO.setQuantity(unitCache.toExt(item.getAvailableQuantity(), item.getFinalUnitId()));
                    logDTO.setUnitId(item.getFinalUnitId());
                    logDTO.setSenderId(dto.getSenderId());
                    logDTO.setReceiverId(dto.getReceiverId());
                    logDTO.setRemark(dto.getLinkExplain());
                    logDTO.setMaterialPositionId(item.getMaterialPositionId());
                    return logDTO;
                }).collect(Collectors.toList()));
        // 确认编号
        batchConfirmSerial(nos);
    }

    @Override
    public void sendBack(StorageMaterialSendBackDTO dto) {
        log.info("{}物料入库:{}", LOG_PREFIX, dto);
        int size = dto.getSendBackList().size();
        if (size > MAX_BOUND_SIZE) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_INBOUND_SIZE_EXCEED);
        }
        StorageMaterialBatch batch = storageMaterialBatchMapper.queryByMaterialIdAndBatchNo(dto.getMaterialId(),
                dto.getMaterialBatchNo());
        if (batch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        List<StorageMaterial> list = new ArrayList<>();
        // 保存物料件信息
        List<String> nos = batchGetSerial(dto.getSendBackList().size());
        Iterator<String> iterator = nos.iterator();
        for (StorageMaterialSendBackDTO.SendBackDTO sendBackDTO : dto.getSendBackList()) {
            StorageMaterial storageMaterial = new StorageMaterial();
            storageMaterial.setMaterialId(dto.getMaterialId());
            storageMaterial.setMaterialPositionId(dto.getMaterialPositionId());
            storageMaterial.setNo(iterator.next());
            storageMaterial.setStorageMaterialBatchId(batch.getId());
            storageMaterial.setUnitId(sendBackDTO.getUnitId());
            storageMaterial.setUnitExtendId(sendBackDTO.getUnitExtendId());
            storageMaterial.setInitQuantity(unitCache.toBasic(sendBackDTO.getSingleQuantity(),
                    sendBackDTO.getFinalUnitId()));
            storageMaterial.setAvailableQuantity(storageMaterial.getInitQuantity());
            storageMaterial.setConsumeQuantity(BigDecimal.ZERO);
            storageMaterial.setReserveQuantity(BigDecimal.ZERO);
            list.add(storageMaterial);
        }
        storageMaterialMapper.insertBatch(list);

        // 保存入库日志
        storageMaterialPositionLogService.saveLogs(list.stream()
                .map(item -> {
                    StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                    logDTO.setStorageMaterialId(item.getId());
                    logDTO.setOperateType(StorageOperateTypeEnum.SEND_BACK);
                    logDTO.setQuantity(unitCache.toExt(item.getInitQuantity(),
                            item.getFinalUnitId()));
                    logDTO.setUnitId(item.getFinalUnitId());
                    logDTO.setSenderId(dto.getSenderId());
                    logDTO.setReceiverId(dto.getReceiverId());
                    logDTO.setRemark(dto.getLinkExplain());
                    logDTO.setMaterialPositionId(item.getMaterialPositionId());
                    return logDTO;
                }).collect(Collectors.toList()));
        batchConfirmSerial(nos);
    }

    @Override
    public CommonPage<StorageMaterialVO> queryPage(StorageMaterialPageQuery pageQuery) {
        List<CargoPosition> cargoPositions =
                cargoPositionService.queryAllEnabledChildrenByStorageId(pageQuery.getMaterialPositionId());
        if (CollectionUtil.isEmpty(cargoPositions)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, pageQuery);
        }
        List<Long> positionIds = cargoPositions.stream().map(CargoPosition::getId).collect(Collectors.toList());
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<StorageMaterialVO> list = storageMaterialMapper.queryPageWithPosition(pageQuery, positionIds);
        CommonPage<StorageMaterialVO> page = CommonPage.convertPage(list);
        for (StorageMaterialVO vo : page.getList()) {
            vo.setUnit(unitCache.getGlobalUnitName(vo.getFinalUnitId()));
            vo.setRate(Optional.ofNullable(vo.getFinalUnitId())
                    .map(unitId -> unitCache.getGlobalUnit(unitId))
                    .map(CacheUnit::getRate)
                    .map(BigDecimal::toPlainString)
                    .orElse("1"));
            BigDecimal available = new BigDecimal(vo.getAvailableQuantity());
            BigDecimal reserve = new BigDecimal(vo.getReserveQuantity());
            vo.setAvailableQuantity(BigDecimalFormatUtil.formatBigDecimal(available));
            vo.setReserveQuantity(BigDecimalFormatUtil.formatBigDecimal(reserve));
            vo.setQuantity(BigDecimalFormatUtil.formatBigDecimal(available.add(reserve)));
            vo.setConsumeQuantity(BigDecimalFormatUtil.formatBigDecimal(new BigDecimal(vo.getConsumeQuantity())));
        }
        // 精度修约
        PrecisionHelper.convertUnitRenderList(page.getList());
        return page;
    }

    @Nullable
    @Override
    public StorageMaterialVO queryInfoById(Long id) {
        StorageMaterialVO storageMaterialVO = storageMaterialMapper.queryInfoById(id);
        if (storageMaterialVO == null) {
            return null;
        }
        storageMaterialVO.setUnit(unitCache.getGlobalUnitName(storageMaterialVO.getFinalUnitId()));
        storageMaterialVO.setAvailableQuantity(BigDecimalFormatUtil.formatBigDecimal(unitCache.toExt(new BigDecimal(storageMaterialVO.getAvailableQuantity()), storageMaterialVO.getFinalUnitId())));
        storageMaterialVO.setInitQuantity(BigDecimalFormatUtil.formatBigDecimal(unitCache.toExt(new BigDecimal(storageMaterialVO.getInitQuantity()), storageMaterialVO.getFinalUnitId())));
        storageMaterialVO.setConsumeQuantity(BigDecimalFormatUtil.formatBigDecimal(unitCache.toExt(new BigDecimal(storageMaterialVO.getConsumeQuantity()), storageMaterialVO.getFinalUnitId())));
        return storageMaterialVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void outbound(StorageMaterialOutboundDTO dto) {
        log.info("{}物料出库:{}", LOG_PREFIX, dto);
        StorageMaterialOutboundMobileDTO param = new StorageMaterialOutboundMobileDTO();
        param.setOutboundList(dto.getOutboundList().stream()
                .map(item -> new StorageMaterialOutboundMobileDTO.OutBoundDTO(item.getId()))
                .collect(Collectors.toList()));
        param.setLinkExplain(dto.getLinkExplain());
        param.setSenderId(dto.getSenderId());
        param.setReceiverId(dto.getReceiverId());
        // 修改为同移动端出库一致
        this.outboundMobile(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void move(StorageMaterialMoveDTO dto) {
        log.info("{}物料移库:{}", LOG_PREFIX, dto);
        StorageMaterialBatch batch = storageMaterialBatchMapper.selectById(dto.getStorageMaterialBatchId());
        if (batch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        List<Long> storageMaterialIds = dto.getStorageMaterialIdList();
        List<StorageMaterial> storageMaterials = storageMaterialMapper.selectBatchIds(storageMaterialIds);
        if (storageMaterials.size() != storageMaterialIds.size()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        for (StorageMaterial storageMaterial : storageMaterials) {
            storageMaterial.setMaterialPositionId(dto.getTargetMaterialPositionId());
        }
        storageMaterialMapper.updateBatch(storageMaterials);

        // 保存移库日志
        storageMaterialPositionLogService.saveLogs(storageMaterials.stream()
                .map(item -> {
                    StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                    logDTO.setStorageMaterialId(item.getId());
                    logDTO.setOperateType(StorageOperateTypeEnum.MOVE);
                    logDTO.setQuantity(unitCache.toExt(item.getAvailableQuantity(),
                            item.getFinalUnitId()));
                    logDTO.setUnitId(item.getFinalUnitId());
                    logDTO.setSenderId(dto.getMoverId());
                    logDTO.setReceiverId(dto.getMoverId());
                    logDTO.setRemark(dto.getLinkExplain());
                    logDTO.setMaterialPositionId(item.getMaterialPositionId());
                    return logDTO;
                }).collect(Collectors.toList()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void check(StorageMaterialCheckDTO dto) {
        log.info("{}物料盘库:{}", LOG_PREFIX, dto);
        StorageMaterial storageMaterial = storageMaterialMapper.selectById(dto.getStorageMaterialId());
        if (storageMaterial == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }

        StorageMaterialReserve reserve = storageMaterialReserveMapper.queryByStorageMaterialId(storageMaterial.getId());
        // 被预定的物料已被预定 不允许盘库
        if (reserve != null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_RESERVE_EXIST);
        }

        // 盘库前
        BigDecimal before = storageMaterial.getAvailableQuantity();
        // 本次消耗
        BigDecimal toConsume = unitCache.toBasic(dto.getConsumeQuantity(), storageMaterial.getFinalUnitId());
        storageMaterial.setInitQuantity(unitCache.toBasic(dto.getInitQuantity(), storageMaterial.getFinalUnitId()));
        if (dto.getUseUp()) {
            storageMaterial.setAvailableQuantity(BigDecimal.ZERO);
            storageMaterial.setConsumeQuantity(storageMaterial.getInitQuantity());
        } else {
            storageMaterial.setAvailableQuantity(storageMaterial.getInitQuantity().subtract(toConsume));
            storageMaterial.setConsumeQuantity(toConsume);
        }
        // 盘库后
        BigDecimal after = storageMaterial.getAvailableQuantity();

        if (storageMaterial.getInitQuantity().compareTo(storageMaterial.getAvailableQuantity().add(storageMaterial.getConsumeQuantity())) != 0) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CHECK_QUANTITY_ERROR);
        }

        storageMaterialMapper.updateById(storageMaterial);
        // 增量
        BigDecimal change;
        if (after.compareTo(before) > 0) {
            change = after.subtract(before).abs();
        } else {
            change = before.subtract(after).abs();
        }
        // 保存盘点日志
        storageMaterialPositionLogService.saveLog(StorageMaterialPositionLogDTO.builder()
                .materialPositionId(storageMaterial.getMaterialPositionId())
                .storageMaterialId(storageMaterial.getId())
                // 新增或不变都算盘增 减少算盘减
                .operateType(after.compareTo(before) >= 0 ? StorageOperateTypeEnum.PLUS : StorageOperateTypeEnum.MINUS)
                .quantity(unitCache.toExt(change, storageMaterial.getFinalUnitId()))
                .unitId(storageMaterial.getFinalUnitId())
                .senderId(dto.getCheckerId())
                .receiverId(dto.getReCheckerId())
                .remark(dto.getRemark())
                .build());
    }

    @Override
    public List<MaterialPartListVO> queryListByBatchId(Long batchNoId, String no) {
        List<StorageMaterial> storageMaterials = storageMaterialMapper.selectByBatchIdAndMaterialNo(batchNoId, no);
        return storageMaterials.stream().map(storageMaterial -> {
            MaterialPartListVO materialPartListVO = new MaterialPartListVO();
            materialPartListVO.setMaterialNo(storageMaterial.getNo());
            materialPartListVO.setId(storageMaterial.getId());
            return materialPartListVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<StorageMaterialVO> queryInfoList(StorageMaterialListQuery query) {

        List<CargoPosition> cargoPositions =
                cargoPositionService.queryAllEnabledChildrenByStorageId(query.getMaterialPositionId());
        if (CollectionUtil.isEmpty(cargoPositions)) {
            return new ArrayList<>();
        }
        List<Long> positionIds = cargoPositions.stream().map(CargoPosition::getId).collect(Collectors.toList());
        List<StorageMaterialVO> list = storageMaterialMapper.queryList(query, positionIds);
        for (StorageMaterialVO vo : list) {
            vo.setUnit(unitCache.getGlobalUnitName(vo.getFinalUnitId()));
            vo.setAvailableQuantity(BigDecimalFormatUtil.formatBigDecimal(unitCache.toExt(new BigDecimal(vo.getAvailableQuantity()), vo.getFinalUnitId())));
            vo.setConsumeQuantity(BigDecimalFormatUtil.formatBigDecimal(unitCache.toExt(new BigDecimal(vo.getConsumeQuantity()), vo.getFinalUnitId())));
            vo.setReserveQuantity(BigDecimalFormatUtil.formatBigDecimal(unitCache.toExt(new BigDecimal(vo.getReserveQuantity()), vo.getFinalUnitId())));
            vo.setRate(Optional.ofNullable(vo.getFinalUnitId())
                    .map(unitId -> unitCache.getGlobalUnit(unitId))
                    .map(CacheUnit::getRate)
                    .map(BigDecimal::toPlainString)
                    .orElse("1"));
        }
        return list;
    }

    @Nullable
    @Override
    public StorageMaterialMobileVO queryInfoByMaterialNo(String materialNo, boolean validateAvailable, CodeType codeType) {
        StorageMaterialMobileVO result = new StorageMaterialMobileVO();
        if (StrUtil.isBlank(materialNo)) {
            return null;
        }
        Long id = storageMaterialMapper.queryIdByMaterialNo(materialNo);
        if (codeType == null) {
            if (id == null) {
                StorageMaterialMobileVO containerMaterial = this.queryMaterialIdByContainerNo(materialNo);
                if (containerMaterial == null || containerMaterial.getId() == null) {
                    return containerMaterial;
                }else {
                    id = containerMaterial.getId();
                }
            }
        } else if (codeType == CodeType.CONTAINER) {
            // 限制死 只查容器
            result = this.queryMaterialIdByContainerNo(materialNo);
            if (result == null){
                return null;
            }
            if (result.getId() == null){
                return result;
            }
            id = result.getId();
        }

        StorageMaterialVO storageMaterial = storageMaterialMapper.queryInfoById(id);
        if (storageMaterial == null) {
            return null;
        }

        if (validateAvailable) {
            if ((new BigDecimal(storageMaterial.getAvailableQuantity()).add(new BigDecimal(storageMaterial.getReserveQuantity())).compareTo(BigDecimal.ZERO) <= 0)) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_AVAILABLE);
            }
            if (storageMaterial.getSignStatus() != null && !Objects.equals(storageMaterial.getSignStatus(),
                    WeighSignStatus.SIGNED)) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_AVAILABLE);
            }
        }
        // 查询物料信息
        ProductMaterial material = materialMapper.selectAllInfoById(storageMaterial.getMaterialId());
        if (material == null) {
            return null;
        }

        result.setId(storageMaterial.getId());
        result.setMaterialId(storageMaterial.getMaterialId());
        result.setMaterialName(storageMaterial.getMaterialName());
        result.setMaterialCode(storageMaterial.getMaterialCode());
        result.setMergeCode(storageMaterial.getMergeCode());
        result.setMaterialBatchNo(storageMaterial.getMaterialBatchNo());
        result.setMaterialBatchId(storageMaterial.getMaterialBatchId());
        result.setMaterialNo(storageMaterial.getMaterialNo());
        result.setUnitId(storageMaterial.getUnitId());
        result.setUnitExtendId(storageMaterial.getUnitExtendId());
        result.setOriginalCode(storageMaterial.getOriginalBatchNo());
        result.setFactoryBatchNo(storageMaterial.getFactoryBatchNo());
        result.setIsAvailable(storageMaterial.isAvailable());
        // 物料分类
        result.setCategoryType(CategoryInfoTypeEnum.getEnumByValue(material.getCategoryType()));
        // 查询单位
        CacheUnit cacheUnit = Optional.ofNullable(storageMaterial.getFinalUnitId())
                .map(unitId -> unitCache.getGlobalUnit(unitId))
                .orElse(null);
        if (cacheUnit != null) {
            result.setUnit(cacheUnit.getUnitName());
            result.setRate(cacheUnit.getRate().toPlainString());
        }
        result.setExpiredDate(storageMaterial.getExpiredDate());
        if (storageMaterial.getMaterialPositionId() != null) {
            CargoPosition cargoPosition = cargoPositionMapper.selectById(storageMaterial.getMaterialPositionId());
            if (cargoPosition != null) {
                result.setMaterialPositionId(cargoPosition.getId());
                result.setMaterialPositionName(cargoPosition.getPosition());
                result.setMaterialPositionCode(cargoPosition.getCode());
            }
        }

        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchMapper.selectById(storageMaterial.getMaterialBatchId());
        if (storageMaterialBatch != null) {
            result.setProducer(storageMaterialBatch.getProducer());
            result.setSupplier(storageMaterialBatch.getSupplier());
            result.setQualityStatus(MaterialQualityStatusEnum.getEnumByValue(storageMaterialBatch.getQualityStatus()));
        }
        if (material.getExpandInfo() != null) {
            MaterialExpandInfo expandInfo = material.getExpandInfo();
            if (StrUtil.isBlank(result.getSupplier())) {
                result.setSupplier(expandInfo.getSupplier());
            }
            if (StrUtil.isBlank(result.getProducer())) {
                result.setProducer(expandInfo.getProducer());
            }
        }
        result.setQuantity(PrecisionHelper.formatBigDecimal(unitCache.toExt(new BigDecimal(storageMaterial.getQuantity()), storageMaterial.getFinalUnitId())));
        result.setAvailableQuantity(PrecisionHelper.formatBigDecimal(unitCache.toExt(new BigDecimal(storageMaterial.getAvailableQuantity()), storageMaterial.getFinalUnitId())));
        result.setInitQuantity(PrecisionHelper.formatBigDecimal(unitCache.toExt(new BigDecimal(storageMaterial.getInitQuantity()), storageMaterial.getFinalUnitId())));
        result.setConsumeQuantity(PrecisionHelper.formatBigDecimal(unitCache.toExt(new BigDecimal(storageMaterial.getConsumeQuantity()), storageMaterial.getFinalUnitId())));
        result.setReserveQuantity(PrecisionHelper.formatBigDecimal(unitCache.toExt(new BigDecimal(storageMaterial.getReserveQuantity()), storageMaterial.getFinalUnitId())));
        StorageMaterialMobileVO finalResult = result;
        Optional.ofNullable(storageMaterial.getContainerId())
                .map(deviceId -> FeignUtils.handleRequest(dId -> equipmentConfigFeign.getConfigByEquipmentId(dId),
                        deviceId).getData())
                .map(equipmentInfoFeignVO -> {
                    finalResult.setContainer(equipmentInfoFeignVO.getName());
                    finalResult.setContainerNo(equipmentInfoFeignVO.getCode());
                    return finalResult;
                });
        StorageMaterialReserve reserve = storageMaterialReserveMapper.queryByStorageMaterialId(storageMaterial.getId());
        if (reserve != null) {
            result.setProductId(reserve.getProductId());
            StorageMaterialMobileVO finalResult1 = result;
            Optional.ofNullable(reserve.getProductId())
                    .map(pid -> productMaterialMapper.selectById(pid))
                    .ifPresent(productMaterial -> {
                        finalResult1.setProductName(productMaterial.getName());
                        finalResult1.setProductMergeCode(productMaterial.getMergeCode());
                    });
            result.setProcessId(reserve.getProcessId());
            result.setProcessName(Optional.ofNullable(reserve.getProcessId())
                    .map(pid -> processMapper.selectById(pid))
                    .map(Process::getName)
                    .orElse(null));
            result.setBatchId(reserve.getBatchId());
            // 从生产计划中获取预定批次号
            result.setBatchNo(Optional.ofNullable(reserve.getBatchId())
                    .map(planMapper::selectById)
                    .map(Plan::getBatchNo)
                    .orElse(null));
            BaseUserDO user = UserUtils.getUser(reserve.getReserveUserId());
            if (user != null) {
                result.setReserveUserName(user.getUserName() + "-" + user.getLoginName());
            }
            result.setReserveTime(reserve.getReserveTime());
        }

        IngredientWeighRecord weighRecord = ingredientWeighRecordMapper.queryByStorageMaterialId(storageMaterial.getId());
        OutputWeighRecord outWeighRecord;
        WeighExecuteWeighRecord weighExecuteWeighRecord;
        if (weighRecord != null) {
            // 配料称量结果
            PrecisionHelper.precision(unitCache.toExt(weighRecord.getTareWeight(), weighRecord.getUnitId()), weighRecord.getUnitId());
            result.setTareWeight(PrecisionHelper.precision(unitCache.toExt(weighRecord.getTareWeight(), weighRecord.getUnitId()), weighRecord.getUnitId()));
            result.setGrossWeight(PrecisionHelper.precision(unitCache.toExt(weighRecord.getGrossWeight(), weighRecord.getUnitId()), weighRecord.getUnitId()));
            result.setNetWeight(PrecisionHelper.precision(unitCache.toExt(weighRecord.getNetWeight(), weighRecord.getUnitId()), weighRecord.getUnitId()));
        } else if ((outWeighRecord = outputWeighRecordMapper.queryByStorageMaterialId(storageMaterial.getId())) != null) {
            // 产出称量结果
            if (outWeighRecord.getTareWeight() != null) {
                result.setTareWeight(PrecisionHelper.precision(unitCache.toExt(outWeighRecord.getTareWeight(), outWeighRecord.getUnitId()), outWeighRecord.getUnitId()));
            }
            if (outWeighRecord.getGrossWeight() != null) {
                result.setGrossWeight(PrecisionHelper.precision(unitCache.toExt(outWeighRecord.getGrossWeight(), outWeighRecord.getUnitId()), outWeighRecord.getUnitId()));
            }
            if (outWeighRecord.getNetWeight() != null) {
                result.setNetWeight(PrecisionHelper.precision(unitCache.toExt(outWeighRecord.getNetWeight(), outWeighRecord.getUnitId()), outWeighRecord.getUnitId()));
            }
        } else if ((weighExecuteWeighRecord = weighExecuteWeighRecordMapper.queryByStorageMaterialId(storageMaterial.getId())) != null) {
            // 称量中心称量结果
            result.setTareWeight(weighExecuteWeighRecord.getTareWeight());
            result.setGrossWeight(weighExecuteWeighRecord.getGrossWeight());
            result.setNetWeight(weighExecuteWeighRecord.getNetWeight());
        }

        // 查询自定义字段
        List<MaterialBatchFieldVO> materialFields = materialBatchFieldService.queryMaterialField(storageMaterial.getMaterialBatchId());
        if (CollectionUtil.isNotEmpty(materialFields)) {
            for (MaterialBatchFieldVO materialField : materialFields) {
                if (StringUtils.equals(materialField.getFieldType(), MaterialBatchCustomFields.getValue())) {
                    result.getMaterialBatchCustomFields().add(materialField);
                } else if (StringUtils.equals(materialField.getFieldType(), MaterialCustomFields.getValue())) {
                    result.getMaterialCustomFields().add(materialField);
                } else if (StringUtils.equals(materialField.getFieldType(), MaterialPieceCustomFields.getValue())) {
                    result.getMaterialPieceCustomFields().add(materialField);
                }
            }
        }

        return result;
    }


    private StorageMaterialMobileVO queryMaterialIdByContainerNo(String containerNo){
        StorageMaterialMobileVO result = new StorageMaterialMobileVO();
        EquipmentInfoFeignVO container;
        try {
            container = FeignUtils.handleRequest(code -> equipmentConfigFeign.getEquipmentByEquipmentCode(code), containerNo).getData();
        } catch (BmosException e) {
            return null;
        }
        // 判断是否是容器
        if (container != null && CollectionUtil.isNotEmpty(container.getEquipmentTagDataList())) {
            Optional<TagFeignVO> any = container.getEquipmentTagDataList().stream()
                    .filter(item -> Objects.equals(item.getCode(), EquipmentTagCodeEnum.CONTAINER_12021.getCode()))
                    .findAny();
            if (!any.isPresent()) {
                throw new BmosException(MesResponseCode.EQUIPMENT_NOT_CONTAINER);
            }
            StorageMaterial storageMaterial = queryByContainerNo(container.getCode());
            result.setContainer(container.getName());
            result.setContainerNo(container.getCode());
            if (storageMaterial != null){
                result.setId(storageMaterial.getId());
            }
            return result;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void outboundMobile(StorageMaterialOutboundMobileDTO dto) {
        // 物料件解绑货位
        log.info("{}移动端出库:{}", LOG_PREFIX, dto);
        // 参数校验
        List<Long> ids = dto.getOutboundList().stream()
                .map(StorageMaterialOutboundMobileDTO.OutBoundDTO::getId)
                .collect(Collectors.toList());
        List<StorageMaterial> storageMaterials = storageMaterialMapper.queryPositionIdNotNullListByIds(ids);
        if (storageMaterials.size() != ids.size()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_OUTBOUNDED);
        }
        Set<Long> positionIds = storageMaterials.stream()
                .map(StorageMaterial::getMaterialPositionId)
                .collect(Collectors.toSet());
        if (positionIds.size() != 1) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_IN_SAME_POSITION);
        }
        Long positionId = positionIds.iterator().next();
        if (dto.getMaterialPositionId() != null && !Objects.equals(positionId, dto.getMaterialPositionId())) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_POSITION_NOT_MATCH);
        }
        // 保存移动端出库日志
        storageMaterialPositionLogService.saveLogs(storageMaterials.stream()
                .map(item -> {
                    StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                    logDTO.setStorageMaterialId(item.getId());
                    logDTO.setOperateType(StorageOperateTypeEnum.OUTBOUND);
                    // 需求说明：移动端出库时，出库数量为物料量（可用+预定）
                    logDTO.setQuantity(unitCache.toExt(item.getAvailableQuantity().add(item.getReserveQuantity()),
                            item.getFinalUnitId()));
                    logDTO.setUnitId(item.getFinalUnitId());
                    logDTO.setSenderId(dto.getSenderId());
                    logDTO.setReceiverId(dto.getReceiverId());
                    logDTO.setRemark(dto.getLinkExplain());
                    logDTO.setMaterialPositionId(item.getMaterialPositionId());
                    return logDTO;
                }).collect(Collectors.toList()));

        // 开始出库 将货位id设置为null
        storageMaterialMapper.updatePositionIdNullByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendBackMobile(StorageMaterialSendBackMobileDTO dto) {
        // 物料件绑定货位
        log.info("{}移动端入库:{}", LOG_PREFIX, dto);
        // 参数校验
        List<Long> ids = dto.getSendBackList().stream()
                .map(StorageMaterialSendBackMobileDTO.SendBackDTO::getId)
                .collect(Collectors.toList());
        List<StorageMaterial> storageMaterials = storageMaterialMapper.queryPositionIdIsNullListByIds(ids);
        if (storageMaterials.size() != ids.size()) {
            throw new ValidationException("物料件不存在或已存在在暂存货位中");
        }
        CargoPosition cargoPosition = cargoPositionMapper.selectById(dto.getMaterialPositionId());
        if (cargoPosition == null) {
            throw new ValidationException("暂存货位不存在");
        }
        // 开始入库 将货位id填充进去
        for (StorageMaterial storageMaterial : storageMaterials) {
            storageMaterial.setMaterialPositionId(dto.getMaterialPositionId());
        }
        storageMaterialMapper.updateBatch(storageMaterials);
        // 保存移动端入库日志
        storageMaterialPositionLogService.saveLogs(storageMaterials.stream()
                .map(item -> {
                    StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                    logDTO.setStorageMaterialId(item.getId());
                    logDTO.setOperateType(StorageOperateTypeEnum.SEND_BACK);
                    // 需求说明：移动端入库时，入库数量为物料量（可用+预定）
                    logDTO.setQuantity(unitCache.toExt(item.getAvailableQuantity().add(item.getReserveQuantity()),
                            item.getFinalUnitId()));
                    logDTO.setUnitId(item.getFinalUnitId());
                    logDTO.setSenderId(dto.getSenderId());
                    logDTO.setReceiverId(dto.getReceiverId());
                    logDTO.setRemark(dto.getLinkExplain());
                    logDTO.setMaterialPositionId(item.getMaterialPositionId());
                    return logDTO;
                }).collect(Collectors.toList()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveMobile(StorageMaterialMoveMobileDTO dto) {
        log.info("{}移动端移库:{}", LOG_PREFIX, dto);
        List<Long> storageMaterialIds = dto.getStorageMaterialIdList();
        List<StorageMaterial> storageMaterials = storageMaterialMapper.selectBatchIds(storageMaterialIds);
        if (storageMaterials.size() != storageMaterialIds.size()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        for (StorageMaterial storageMaterial : storageMaterials) {
            if ((storageMaterial.getAvailableQuantity().add(storageMaterial.getReserveQuantity())).compareTo(BigDecimal.ZERO) == 0) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_QUANTITY_ZERO);
            }
            if (!Objects.equals(storageMaterial.getMaterialPositionId(), dto.getSourceMaterialPositionId())) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_POSITION_NOT_MATCH);
            }
            storageMaterial.setMaterialPositionId(dto.getTargetMaterialPositionId());
        }
        storageMaterialMapper.updateBatch(storageMaterials);
        // 保存移库日志
        storageMaterialPositionLogService.saveLogs(storageMaterials.stream()
                .map(item -> {
                    StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                    logDTO.setStorageMaterialId(item.getId());
                    logDTO.setOperateType(StorageOperateTypeEnum.MOVE);
                    // 移动端移库使用物料量(可用+预定)
                    logDTO.setQuantity(unitCache.toExt(
                            item.getAvailableQuantity().add(item.getReserveQuantity()), item.getFinalUnitId()));
                    logDTO.setUnitId(item.getFinalUnitId());
                    logDTO.setSenderId(dto.getMoverId());
                    logDTO.setReceiverId(dto.getMoverId());
                    logDTO.setRemark(dto.getLinkExplain());
                    logDTO.setMaterialPositionId(item.getMaterialPositionId());
                    return logDTO;
                }).collect(Collectors.toList()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reserve(StorageMaterialReserveDTO dto) {
        log.info("{}物料预定:{}", LOG_PREFIX, dto);
        StorageMaterial storageMaterial = storageMaterialMapper.selectById(dto.getStorageMaterialId());
        if (storageMaterial == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        // 已被预定
        if (storageMaterialReserveMapper.existReserve(storageMaterial.getId())) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_RESERVED);
        }
        // 判断可用量是否大于0
        if (storageMaterial.getAvailableQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_QUANTITY_ZERO);
        }

        // 判断批次有效性
        StorageMaterialBatch batch = storageMaterialBatchMapper.selectById(storageMaterial.getStorageMaterialBatchId());
        if (!batch.getAvailable()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_EXPIRED);
        }

        Plan plan = planMapper.selectById(dto.getBatchId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }

        ProductFormulaInfo formulaInfo = productFormulaConfigureService.getProductFormulaInfoByPlanId(dto.getBatchId());
        List<ProductFormulaMaterial> materials = formulaInfo.getMaterials();
        if (CollectionUtil.isNotEmpty(materials)) {
            List<Long> materialIds = materials.stream().map(ProductFormulaMaterial::getMaterialId)
                    .collect(Collectors.toList());
            if (!CollectionUtil.contains(materialIds, storageMaterial.getMaterialId())) {
                throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_RESERVE_NOT_MATCH);
            }
        }


        BigDecimal reserveQuantity = storageMaterial.getAvailableQuantity();
        // 保存预定信息
        StorageMaterialReserve reserve = new StorageMaterialReserve();
        reserve.setStorageMaterialId(dto.getStorageMaterialId());
        reserve.setProductId(dto.getProductId());
        reserve.setProcessId(dto.getProcessId());
        reserve.setBatchId(plan.getId());
        reserve.setBatchNo(plan.getBatchNo());
        reserve.setReserveQuantity(reserveQuantity);
        reserve.setReserveRemark(dto.getRemark());
        reserve.setReserveTime(LocalDateTime.now());
        reserve.setReserveUserId(dto.getOperatorId());
        storageMaterialReserveMapper.insert(reserve);
        // 更新物料件信息
        storageMaterial.setReserveQuantity(reserveQuantity);
        storageMaterial.setAvailableQuantity(BigDecimal.ZERO);
        storageMaterial.setProductPlanId(plan.getId());
        storageMaterialMapper.updateById(storageMaterial);
        // 记录物料预定日志
        storageMaterialPositionLogService.saveLog(StorageMaterialPositionLogDTO.builder()
                .materialPositionId(storageMaterial.getMaterialPositionId())
                .storageMaterialId(storageMaterial.getId())
                .operateType(RESERVE)
                // 不涉及到单位转换 直接保存预定数量
                .quantity(unitCache.toExt(reserveQuantity, storageMaterial.getFinalUnitId()))
                .productName(plan.getProductName())
                .productCode(plan.getProductMergeCode())
                .productBatchNo(plan.getBatchNo())
                .tareWeight(dto.getTareWeight())
                .grossWeight(dto.getGrossWeight())
                .unitId(storageMaterial.getFinalUnitId())
                .senderId(dto.getOperatorId())
                .receiverId(dto.getReCheckerId())
                .remark(dto.getRemark())
                .build());
        // 发布物料预定量变化的事件
        List<StorageMaterialReserve> reserveList = new ArrayList<>();
        reserveList.add(reserve);
        this.publishStorageMaterialReserveEvent(plan.getId(), Collections.singletonList(storageMaterial.getMaterialId()));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reserveBatch(List<StorageMaterialReserveDTO> dtos) {
        log.info("{}物料预定:{}", LOG_PREFIX, dtos);

        if (CollectionUtil.isEmpty(dtos)) {
            return;
        }
        List<Long> storageMaterialIds = dtos.stream().map(StorageMaterialReserveDTO::getStorageMaterialId).collect(Collectors.toList());
        List<StorageMaterial> storageMaterials = storageMaterialMapper.selectBatchIds(storageMaterialIds);
        if (storageMaterials.size() != dtos.size()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        Map<Long, StorageMaterial> materialMap = storageMaterials.stream()
                .collect(Collectors.toMap(StorageMaterial::getId, Function.identity(), (k1, k2) -> k1));

        List<Long> planIds = dtos.stream().map(StorageMaterialReserveDTO::getBatchId).collect(Collectors.toList());
        Map<Long, ProductFormulaInfo> productFormulaInfoMap = productFormulaConfigureService.getProductFormulaInfoByPlanIds(planIds);
        List<Plan> plans = planMapper.selectBatchIds(planIds);
        Map<Long, Plan> planMap = plans.stream()
                .collect(Collectors.toMap(Plan::getId, Function.identity(), (k1, k2) -> k1));
        for (StorageMaterialReserveDTO dto : dtos) {
            StorageMaterial storageMaterial = materialMap.get(dto.getStorageMaterialId());
            ProductFormulaInfo formulaInfo = productFormulaInfoMap.get(dto.getBatchId());
            List<ProductFormulaMaterial> materials = formulaInfo.getMaterials();
            if (CollectionUtil.isNotEmpty(materials)) {
                List<Long> materialIds = materials.stream().map(ProductFormulaMaterial::getMaterialId)
                        .collect(Collectors.toList());
                if (!CollectionUtil.contains(materialIds, storageMaterial.getMaterialId())) {
                    throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_RESERVE_NOT_MATCH);
                }
            }
        }

        // 已被预定
        if (storageMaterialReserveMapper.existReserves(storageMaterialIds)) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_RESERVED);
        }

        List<StorageMaterialReserve> reserves = new ArrayList<>();
        List<StorageMaterialPositionLogDTO> lotDTOs = new ArrayList<>();
        for (int i = 0; i < dtos.size(); i++) {
            StorageMaterialReserveDTO dto = dtos.get(i);
            Plan plan = planMap.get(dto.getBatchId());
            StorageMaterial storageMaterial = materialMap.get(dto.getStorageMaterialId());
            // 判断可用量是否大于0
            if (storageMaterial.getAvailableQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_QUANTITY_ZERO);
            }
            // 判断批次有效性
            StorageMaterialBatch batch = storageMaterialBatchMapper.selectById(storageMaterial.getStorageMaterialBatchId());
            if (!batch.getAvailable()) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_EXPIRED);
            }
            BigDecimal reserveQuantity = storageMaterial.getAvailableQuantity();
            // 保存预定信息
            StorageMaterialReserve reserve = new StorageMaterialReserve();
            reserve.setStorageMaterialId(dto.getStorageMaterialId());
            reserve.setProductId(dto.getProductId());
            reserve.setProcessId(dto.getProcessId());
            reserve.setBatchId(plan.getId());
            reserve.setBatchNo(plan.getBatchNo());
            reserve.setReserveQuantity(reserveQuantity);
            reserve.setReserveRemark(dto.getRemark());
            reserve.setReserveTime(LocalDateTime.now());
            reserve.setReserveUserId(dto.getOperatorId());
            reserves.add(reserve);
            // 更新物料件信息
            storageMaterial.setReserveQuantity(reserveQuantity);
            storageMaterial.setAvailableQuantity(BigDecimal.ZERO);
            storageMaterial.setProductPlanId(plan.getId());
            lotDTOs.add(StorageMaterialPositionLogDTO.builder()
                    .materialPositionId(storageMaterial.getMaterialPositionId())
                    .storageMaterialId(storageMaterial.getId())
                    .operateType(RESERVE)
                    // 不涉及到单位转换 直接保存预定数量
                    .quantity(unitCache.toExt(reserveQuantity, storageMaterial.getFinalUnitId()))
                    .productName(plan.getProductName())
                    .productCode(plan.getProductMergeCode())
                    .productBatchNo(plan.getBatchNo())
                    .tareWeight(dto.getTareWeight())
                    .grossWeight(dto.getGrossWeight())
                    .unitId(storageMaterial.getFinalUnitId())
                    .senderId(dto.getOperatorId())
                    .receiverId(dto.getReCheckerId())
                    .remark(dto.getRemark())
                    .build());
            // 发布物料预定量变化的事件
            this.publishStorageMaterialReserveEvent(plan.getId(), Collections.singletonList(storageMaterial.getMaterialId()));
        }
        storageMaterialReserveMapper.insertBatch(reserves);
        storageMaterialMapper.updateBatch(storageMaterials);
        // 记录物料预定日志
        storageMaterialPositionLogService.saveLogs(lotDTOs);
    }

    private void publishStorageMaterialReserveEvent(Long planId, List<Long> materialIdList) {
        List<Long> materialIdS = materialIdList.stream().distinct().collect(Collectors.toList());
        MaterialReserveType materialReserveType = new MaterialReserveType(planId, materialIdS);
        conditionChangeHandler.refreshConditionResult(materialReserveType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelReserve(StorageMaterialCancelReserveDTO dto) {
        log.info("{}取消预定:{}", LOG_PREFIX, dto);
        List<StorageMaterialReserve> reserveList =
                storageMaterialReserveMapper.queryListByReserveProductInfo(dto.getProductId(), dto.getProcessId(),
                        dto.getBatchId());
        if (reserveList.isEmpty()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_RESERVED_IN_PRODUCT_AND_BATCH);
        }
        List<Long> storageMaterialIds =
                reserveList.stream().map(StorageMaterialReserve::getStorageMaterialId).collect(Collectors.toList());
        if (!CollectionUtil.containsAll(storageMaterialIds, dto.getStorageMaterialIdList())) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_RESERVED);
        }
        List<StorageMaterial> storageMaterials = storageMaterialMapper.queryListByIds(dto.getStorageMaterialIdList());
        if (storageMaterials.size() != dto.getStorageMaterialIdList().size()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        // 更新物料预约量
        for (StorageMaterial storageMaterial : storageMaterials) {
            BigDecimal reserveQuantity = storageMaterial.getReserveQuantity();
            storageMaterial.setReserveQuantity(BigDecimal.ZERO);
            storageMaterial.setAvailableQuantity(reserveQuantity);
        }

        Plan plan = Optional.ofNullable(planMapper.selectById(dto.getBatchId())).orElse(new Plan());

        storageMaterialMapper.updateBatch(storageMaterials);

        // 记录物料取消预定日志
        storageMaterialPositionLogService.saveLogs(storageMaterials.stream()
                .map(item -> {
                    StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                    logDTO.setStorageMaterialId(item.getId());
                    logDTO.setOperateType(Optional.ofNullable(dto.getOperateType()).orElse(StorageOperateTypeEnum.CANCEL_RESERVE));
                    logDTO.setQuantity(unitCache.toExt(item.getAvailableQuantity(),
                            item.getFinalUnitId()));
                    logDTO.setUnitId(item.getFinalUnitId());
                    logDTO.setSenderId(dto.getOperatorId());
                    logDTO.setReceiverId(dto.getReCheckerId());
                    logDTO.setRemark(dto.getRemark());
                    logDTO.setMaterialPositionId(item.getMaterialPositionId());
                    return logDTO;
                }).collect(Collectors.toList()));
        // 删除预定信息
        storageMaterialReserveMapper.deleteByStorageMaterialIds(dto.getStorageMaterialIdList());
        this.publishStorageMaterialReserveEvent(plan.getId(), CollectionUtils.convertList(storageMaterials, StorageMaterial::getMaterialId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String splitPackage(StorageMaterialSplitPackageDTO dto) {
        log.info("{}拆包出库:{}", LOG_PREFIX, dto);
        StorageMaterial storageMaterial = storageMaterialMapper.selectById(dto.getStorageMaterialId());
        if (storageMaterial == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        // 出库量
        BigDecimal splitQuantity = unitCache.toBasic(dto.getQuantity(), storageMaterial.getFinalUnitId());
        if (storageMaterial.getQuantity().compareTo(splitQuantity) < 0) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_OUTBOUND_NOT_ENOUGH);
        }

        // 原始物料件存在预定量优先扣除预订量 然后扣除可用量

        // 可用量扣除
        BigDecimal reserveDif;
        // 预订量扣除
        BigDecimal availableDif;
        if (storageMaterial.getReserveQuantity().compareTo(splitQuantity) > 0) {
            // 预订量够扣除的
            reserveDif = splitQuantity;
            availableDif = BigDecimal.ZERO;
        } else {
            // 预定量不够扣除的 计算差值 从可用量中扣除
            reserveDif = storageMaterial.getReserveQuantity();
            availableDif = splitQuantity.subtract(reserveDif);
        }
        log.info("{}拆包出库：总出库量:{}, 预订量出库:{}, 可用量出库:{}", LOG_PREFIX, splitQuantity, reserveDif, availableDif);
        storageMaterial.setReserveQuantity(storageMaterial.getReserveQuantity().subtract(reserveDif));
        storageMaterial.setAvailableQuantity(storageMaterial.getAvailableQuantity().subtract(availableDif));
        storageMaterial.setConsumeQuantity(storageMaterial.getConsumeQuantity().add(availableDif).add(reserveDif));

        // 生成新的物料件
        StorageMaterial child = this.splitNewPackage(storageMaterial, reserveDif, availableDif, dto.getContainerId());
        // 新增拆包物料件
        storageMaterialMapper.insert(child);

        // 保存拆包出库拆出来的物料件的日志
        StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
        logDTO.setStorageMaterialId(child.getId());
        logDTO.setMaterialPositionId(child.getMaterialPositionId());
        logDTO.setOperateType(SPLIT_PACKAGE_NEW);
        logDTO.setQuantity(unitCache.toExt(child.getInitQuantity(), child.getFinalUnitId()));
        logDTO.setUnitId(child.getFinalUnitId());
        logDTO.setSenderId(dto.getSenderId());
        logDTO.setReceiverId(dto.getReceiverId());
        storageMaterialPositionLogService.saveLog(logDTO);

        // 更新原物料件
        storageMaterialMapper.updateById(storageMaterial);
        // 处理预约信息
        if (reserveDif.compareTo(BigDecimal.ZERO) != 0) {
            StorageMaterialReserve oldReserve =
                    storageMaterialReserveMapper.queryByStorageMaterialId(storageMaterial.getId());
            BigDecimal oldReserveQuantity = oldReserve.getReserveQuantity();
            BigDecimal subtract = oldReserveQuantity.subtract(reserveDif);
            if (subtract.compareTo(BigDecimal.ZERO) == 0) {
                // 扣除完成后预定量为0 删除预定信息
                storageMaterialReserveMapper.deleteById(oldReserve.getId());
            }
            oldReserve.setReserveQuantity(subtract);
            storageMaterialReserveMapper.updateById(oldReserve);
            List<StorageMaterialReserve> reserveList = new ArrayList<>();
            reserveList.add(oldReserve);
            if (child.getReserveQuantity().compareTo(BigDecimal.ZERO) > 0) {
                // 新的物料件有预定量 保存预定信息
                StorageMaterialReserve newReserve = new StorageMaterialReserve();
                BeanUtils.copyProperties(oldReserve, newReserve, "id", "storageMaterialId", "reserveQuantity");
                newReserve.setStorageMaterialId(child.getId());
                newReserve.setReserveQuantity(child.getReserveQuantity());
                storageMaterialReserveMapper.insert(newReserve);
                reserveList.add(newReserve);
            }
            // 发出计划物料预定量变化的事件
            this.publishStorageMaterialReserveEvent(oldReserve.getBatchId(), Collections.singletonList(storageMaterial.getMaterialId()));
        }
        // 记录拆包出库日志
        storageMaterialPositionLogService.saveLog(StorageMaterialPositionLogDTO.builder()
                .materialPositionId(storageMaterial.getMaterialPositionId())
                .storageMaterialId(storageMaterial.getId())
                .operateType(SPLIT_PACKAGE)
                // 不涉及到单位转换 直接保存预定数量
                .quantity(unitCache.toExt(splitQuantity, storageMaterial.getFinalUnitId()))
                .unitId(storageMaterial.getFinalUnitId())
                .senderId(dto.getSenderId())
                .receiverId(dto.getReceiverId())
                .remark(dto.getRemark())
                .build());
        // 确认物料件号
        confirmSerial(child.getNo());
        return child.getNo();
    }

    @Override
    public List<BatchReservedMaterialVO> getBatchReservedMaterial(BatchReservedMaterialQueryDTO dto) {
        List<BatchReservedMaterialVO> result = storageMaterialReserveMapper.queryBatchReservedMaterial(dto);
        Long formulaMaterialId = dto.getFormulaMaterialId();
        ProductFormulaMaterial formulaMaterial = formulaMaterialMapper.selectById(formulaMaterialId);
        List<BatchReservedMaterialVO> filter =
                result.stream().filter(BatchReservedMaterialVO::isAvailable).collect(Collectors.toList());
        filter.forEach(e -> {
            BigDecimal quantity = MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(e.getReserveQuantity(),
                            formulaMaterial.getUnitId()),
                    formulaMaterial);
            e.setQuantity(quantity);
            e.setReserveQuantity(quantity);
            e.setUnitId(formulaMaterial.getUnitId());
            e.setUnitName(unitCache.getGlobalUnitName(formulaMaterial.getUnitId()));
            // 理论量
            e.setTheoreticalQuantity(MaterialQuantityCalculateUtil.calculateTheoreticalQuantity(e.getReserveQuantity(),
                    e.getHydration(), e.getNoHydrationContent(), formulaMaterial));
        });
        return filter;
    }

    @Override
    public List<BatchReservedAvailableMaterialVO> getReservedAvailableStorageMaterial(BatchReservedMaterialQueryDTO dto) {
        ProductFormulaMaterial formulaMaterial = formulaMaterialMapper.selectById(dto.getFormulaMaterialId());
        List<BatchReservedMaterialVO> batchReservedMaterialVOS =
                storageMaterialReserveMapper.queryBatchReservedMaterial(dto);
        List<BatchReservedAvailableMaterialVO> reserved =
                StorageMaterialConverter.INSTANCE.convertToReservedAvailableVO(batchReservedMaterialVOS.stream()
                        .filter(BatchReservedMaterialVO::isAvailable).collect(Collectors.toList()));
        reserved.forEach(e -> e.setReserved(true));
        List<Long> deptIds = platformApiAdaptor.deptIds();
        reserved.forEach(e -> e.setQuantity(e.getReserveQuantity()));
        List<BatchReservedAvailableMaterialVO> availableList =
                storageMaterialMapper.selectAvailableMaterial(formulaMaterial.getMaterialId(), deptIds);
        reserved.addAll(availableList);
        ProductMaterial productMaterial = materialMapper.selectAllInfoById(formulaMaterial.getMaterialId());
        reserved.forEach(e -> {
            BigDecimal quantity = unitCache.toExt(e.getQuantity(), formulaMaterial.getUnitId());
            e.setQuantity(MaterialQuantityCalculateUtil.roundingOff(quantity, formulaMaterial));
            e.setReserveQuantity(quantity);
            e.setMaterialExpandInfo(productMaterial.getExpandInfo());
            e.setUnitId(formulaMaterial.getUnitId());
            e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId()));
            // 理论量 使用修约后的物料量计算
            e.setTheoreticalQuantity(MaterialQuantityCalculateUtil.calculateTheoreticalQuantity(e.getQuantity(),
                    e.getHydration(),
                    e.getNoHydrationContent(), formulaMaterial));
        });
        reserved.sort(Comparator.comparing(o -> Integer.valueOf(o.getMaterialNo())));
        return reserved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reserveBatch(StorageMaterialReserveBatchDTO dto) {
        Long planId = dto.getBatchId();
        Plan plan = planMapper.selectById(planId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        List<Long> storageMaterialIdList = dto.getStorageMaterialIdList();
        List<StorageMaterialReserve> reserved = storageMaterialReserveMapper.selectByBatchIdAndMaterialId(planId,
                dto.getMaterialId());
        Set<Long> existedIds = CollectionUtils.convertSet(reserved, StorageMaterialReserve::getStorageMaterialId);
        // 已存在的去除这次还要预定的则为需要取消预订的
        List<Long> cancel = existedIds.stream().filter(e -> !storageMaterialIdList.contains(e)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(cancel)) {
            StorageMaterialCancelReserveDTO cancelReserveDTO = new StorageMaterialCancelReserveDTO();
            cancelReserveDTO.setBatchId(planId);
            cancelReserveDTO.setProcessId(plan.getProcessId());
            cancelReserveDTO.setProductId(plan.getProductId());
            cancelReserveDTO.setStorageMaterialIdList(cancel);
            cancelReserveDTO.setOperatorId(SysUserHolder.getUser().getUserId());
            cancelReserveDTO.setReCheckerId(SysUserHolder.getUser().getUserId());
            cancelReserve(cancelReserveDTO);
        }
        // 去除已存在的则为需要新预定的
        storageMaterialIdList.removeAll(existedIds);
        if (CollUtil.isEmpty(storageMaterialIdList)) {
            return;
        }
        List<StorageMaterial> storageMaterials = storageMaterialMapper.selectBatchIds(storageMaterialIdList);
        // 校验是否已被预定
        List<StorageMaterialReserve> reservedByOther = storageMaterialReserveMapper.selectOtherBatchReserved(storageMaterialIdList, plan.getId());
        if (CollUtil.isNotEmpty(reservedByOther)) {
            StorageMaterialReserve first = CollUtil.getFirst(reservedByOther);
            Map<Long, StorageMaterial> storageMap = CollectionUtils.convertMap(storageMaterials, StorageMaterial::getId);
            StorageMaterial storageMaterial = storageMap.get(first.getStorageMaterialId());
            throw new BmosException(MesResponseCode.MATERIAL_RESERVED_BY_OTHER_BATCH, storageMaterial.getNo());
        }

        List<StorageMaterialReserve> reserves = new ArrayList<>();
        List<StorageMaterialPositionLogDTO> logs = new ArrayList<>();
        for (StorageMaterial storageMaterial : storageMaterials) {
            StorageMaterialReserve reserve = new StorageMaterialReserve();
            reserve.setStorageMaterialId(storageMaterial.getId());
            reserve.setProcessId(dto.getProcessId());
            reserve.setBatchId(plan.getId());
            reserve.setBatchNo(plan.getBatchNo());
            reserve.setProductId(plan.getProductId());
            reserve.setReserveUserId(SysUserHolder.getUser().getUserId());
            reserve.setReserveTime(LocalDateTime.now());
            reserve.setReserveQuantity(storageMaterial.getAvailableQuantity());
            storageMaterial.setAvailableQuantity(BigDecimal.ZERO);
            storageMaterial.setReserveQuantity(reserve.getReserveQuantity());
            reserves.add(reserve);
            // 物料日志
            StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
            logDTO.setStorageMaterialId(storageMaterial.getId());
            logDTO.setOperateType(MATERIAL_RESERVE);
            logDTO.setQuantity(storageMaterial.getQuantity());
            logDTO.setUnitId(storageMaterial.getFinalUnitId());
            logDTO.setSenderId(SysUserHolder.getUser().getUserId());
            logDTO.setReceiverId(SysUserHolder.getUser().getUserId());
            logDTO.setProductId(plan.getProductId());
            logDTO.setProductName(plan.getProductName());
            logDTO.setProductCode(plan.getProductMergeCode());
            logDTO.setProductBatchNo(plan.getBatchNo());
            logDTO.setMaterialPositionId(storageMaterial.getMaterialPositionId());
            logs.add(logDTO);
        }
        storageMaterialMapper.updateBatch(storageMaterials);
        storageMaterialReserveMapper.insertBatch(reserves);
        storageMaterialPositionLogService.saveLogs(logs);
        this.publishStorageMaterialReserveEvent(dto.getBatchId(), CollectionUtils.convertList(storageMaterials, StorageMaterial::getMaterialId));
    }

    @Override
    public List<StorageMaterial> inventoryMaterialInbound(InventoryMaterialInboundDTO dto, Plan plan) {
        if (CollUtil.isEmpty(dto.getInboundList())) {
            return new ArrayList<>();
        }
        StorageMaterialBatch existBatch =
                storageMaterialBatchMapper.queryByMaterialIdAndBatchNo(dto.getMaterialId(),
                        dto.getMaterialBatchNo());
        Long batchId;
        if (existBatch != null) {
            // 沿用批次
            batchId = existBatch.getId();
        } else {
            // 新增批次
            StorageMaterialBatch batch = new StorageMaterialBatch();
            batch.setMaterialId(dto.getMaterialId());
            batch.setMaterialBatchNo(dto.getMaterialBatchNo());
            batch.setOriginalBatchNo(dto.getOriginalBatchNo());
            batch.setExpiredDate(dto.getExpiredDate());
            CacheUnit unit = unitCache.getGlobalUnit(dto.getUnitId());
            if (unit != null) {
                if (unit.getExtend()) {
                    batch.setUnitId(unit.getParentUnitId());
                    batch.setUnitExtendId(unit.getUnitId());
                } else {
                    batch.setUnitId(unit.getUnitId());
                }
            }
            batch.setSupplier(dto.getSupplier());
            batch.setProducer(dto.getProducer());
            batch.setHydration(dto.getHydration());
            batch.setNoHydrationContent(dto.getNoHydrationContent());
            batch.setAvailable(!LocalDate.now().isAfter(dto.getExpiredDate()));
            batch.setLinkExplain(dto.getLinkExplain());
            batch.setSenderId(dto.getSenderId());
            batch.setReceiverId(dto.getReceiverId());
            batch.setLicenceNo(dto.getLicenceNo());
            batch.setReportNo(dto.getReportNo());
            storageMaterialBatchMapper.insert(batch);
            existBatch = batch;
            batchId = batch.getId();
        }
        List<InventoryMaterialInboundDTO.MaterialInboundDTO> inboundList = dto.getInboundList();
        // 保存入库日志
        List<String> nos = batchGetSerial(inboundList.size());
        Iterator<String> iterator = nos.iterator();
        List<StorageMaterial> list = inboundList.stream().map(e -> {
            StorageMaterial storageMaterial = new StorageMaterial();
            storageMaterial.setStorageMaterialBatchId(batchId);
            storageMaterial.setMaterialId(dto.getMaterialId());
            storageMaterial.setMaterialPositionId(dto.getMaterialPositionId());
            storageMaterial.setNo(iterator.next());
            storageMaterial.setUnitId(e.getUnitId());
            storageMaterial.setUnitExtendId(e.getUnitId());
            storageMaterial.setInitQuantity(e.getQuantity());
            storageMaterial.setAvailableQuantity(BigDecimal.ZERO);
            storageMaterial.setConsumeQuantity(BigDecimal.ZERO);
            storageMaterial.setReserveQuantity(e.getQuantity());
            storageMaterial.setSource(e.getInventoryNo());
            return storageMaterial;
        }).collect(Collectors.toList());
        storageMaterialMapper.insertBatch(list);
        // 预定到生产计划下
        if (plan != null) {
            List<StorageMaterialReserve> reserveList = list.stream().map(storageMaterial -> {
                StorageMaterialReserve storageMaterialReserve = new StorageMaterialReserve();
                storageMaterialReserve.setStorageMaterialId(storageMaterial.getId());
                storageMaterialReserve.setProductId(plan.getProductId());
                storageMaterialReserve.setProcessId(plan.getProcessId());
                storageMaterialReserve.setBatchId(plan.getId());
                storageMaterialReserve.setBatchNo(plan.getBatchNo());
                storageMaterialReserve.setReserveQuantity(storageMaterial.getReserveQuantity());
                storageMaterialReserve.setReserveTime(LocalDateTime.now());
                storageMaterialReserve.setReserveUserId(SysUserHolder.getUser().getUserId());
                return storageMaterialReserve;
            }).collect(Collectors.toList());
            storageMaterialReserveMapper.insertBatch(reserveList);
            this.publishStorageMaterialReserveEvent(plan.getId(), CollectionUtils.convertList(list, StorageMaterial::getMaterialId));
        }
        // 保存入库日志
        storageMaterialPositionLogService.saveLogs(list.stream()
                .map(item -> {
                    StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                    logDTO.setStorageMaterialId(item.getId());
                    logDTO.setOperateType(REQUISITION_RECEIVE);
                    logDTO.setQuantity(item.getQuantity());
                    logDTO.setUnitId(item.getFinalUnitId());
                    logDTO.setSenderId(dto.getSenderId());
                    logDTO.setReceiverId(dto.getReceiverId());
                    logDTO.setRemark(dto.getLinkExplain());
                    logDTO.setProductName(dto.getProductName());
                    logDTO.setProductCode(dto.getProductCode());
                    logDTO.setProductBatchNo(dto.getProductBatchNo());
                    logDTO.setMaterialPositionId(item.getMaterialPositionId());
                    return logDTO;
                }).collect(Collectors.toList()));
        batchConfirmSerial(nos);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void chargeConsume(List<StorageMaterial> list, String operatorId, Long productPlanId) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        // 更新物料件信息 各个量已在调用处计算
        storageMaterialMapper.updateBatch(list);
        Plan plan = planMapper.selectById(productPlanId);
        // 记录物料投料日志
        List<StorageMaterialPositionLogDTO> logs =
                list.stream().map(storageMaterial -> StorageMaterialPositionLogDTO.builder()
                        .storageMaterialId(storageMaterial.getId())
                        .operateType(CHARGE)
                        .quantity(storageMaterial.getReserveQuantity())
                        .unitId(storageMaterial.getUnitId())
                        .senderId(operatorId)
                        .receiverId(operatorId)
                        .productId(plan.getProductId())
                        .productBatchNo(plan.getBatchNo())
                        .productCode(plan.getProductMergeCode())
                        .productName(plan.getProductName())
                        .materialPositionId(storageMaterial.getMaterialPositionId())
                        .build()).collect(Collectors.toList());
        storageMaterialPositionLogService.saveLogs(logs);
        // 预定量剩余为0时则为完全消耗
        List<StorageMaterial> completelyConsume =
                list.stream().filter(e -> BigDecimal.ZERO.compareTo(e.getReserveQuantity()) == 0).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(completelyConsume)) {
            List<Long> idList = CollectionUtils.convertList(completelyConsume, StorageMaterial::getId);
            // 解绑容器
            storageMaterialMapper.unbindContainersByIds(idList);
            // 消耗物料件不再删除预定记录
//            List<StorageMaterialReserve> reserveList =
//                    storageMaterialReserveMapper.queryByStorageMaterialIds(idList);
//            if (CollUtil.isNotEmpty(reserveList)) {
//                storageMaterialReserveMapper.deleteBatchIds(CollectionUtils.convertList(reserveList,
//                        StorageMaterialReserve::getId));
//            }
        }
        // 发布预定量变化
        this.publishStorageMaterialReserveEvent(productPlanId, CollectionUtils.convertList(list, StorageMaterial::getMaterialId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StorageMaterial recycleStorageMaterial(RecycleStorageMaterialDTO dto) {
        StorageMaterial existContainerStorageMaterial =
                storageMaterialMapper.selectStorageMaterialByContainerId(dto.getDeviceId());
        if (existContainerStorageMaterial != null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_OCCUPY);
        }
        EquipmentInfoFeignVO container = null;
        if (dto.getDeviceId() != null) {
            container =
                    FeignUtils.handleRequest(containerId -> equipmentConfigFeign.getConfigByEquipmentId(containerId),
                            dto.getDeviceId()).getData();
            if (container == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_NOT_EXIST);
            }
        }
        StorageMaterial storageMaterial = new StorageMaterial();
        storageMaterial.setStorageMaterialBatchId(dto.getMaterialBatchId());
        StorageMaterialBatch batch = storageMaterialBatchMapper.selectById(dto.getMaterialBatchId());
        storageMaterial.setNo(getSerial());
        Long baseUnitId = unitCache.getBaseUnitId(dto.getUnitId());
        BigDecimal quantity = unitCache.convert(dto.getQuantity(), dto.getUnitId(), baseUnitId);
        storageMaterial.setMaterialId(batch.getMaterialId());
        storageMaterial.setUnitId(dto.getUnitId());
        storageMaterial.setProductPlanId(dto.getProductPlanId());
        storageMaterial.setReserveQuantity(quantity);
        storageMaterial.setAvailableQuantity(BigDecimal.ZERO);
        storageMaterial.setConsumeQuantity(BigDecimal.ZERO);
        storageMaterial.setInitQuantity(quantity);
        if (container != null) {
            storageMaterial.setContainerId(container.getId());
            storageMaterial.setContainer(container.getCode() + "-" + container.getName());
            dto.setDeviceCode(container.getCode());
            dto.setDeviceName(container.getName());
        }
        storageMaterialMapper.insert(storageMaterial);
        // 预定到当前计划下
        StorageMaterialReserve storageMaterialReserve = new StorageMaterialReserve();
        Plan plan = planMapper.selectById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        storageMaterialReserve.setStorageMaterialId(storageMaterial.getId());
        storageMaterialReserve.setProductId(plan.getProductId());
        storageMaterialReserve.setProcessId(plan.getProcessId());
        storageMaterialReserve.setBatchId(dto.getProductPlanId());
        storageMaterialReserve.setBatchNo(plan.getBatchNo());
        storageMaterialReserve.setReserveQuantity(storageMaterial.getReserveQuantity());
        storageMaterialReserve.setReserveRemark(dto.getRemark());
        storageMaterialReserve.setReserveTime(LocalDateTime.now());
        storageMaterialReserve.setReserveUserId(dto.getOperatorId());
        storageMaterialReserveMapper.insert(storageMaterialReserve);
        // 记录物料回收日志
        storageMaterialPositionLogService.saveLog(StorageMaterialPositionLogDTO.builder()
                .materialPositionId(storageMaterial.getMaterialPositionId())
                .storageMaterialId(storageMaterial.getId())
                .operateType(RECYCLE)
                .quantity(storageMaterial.getReserveQuantity())
                .unitId(storageMaterial.getUnitId())
                .senderId(dto.getOperatorId())
                .receiverId(dto.getOperatorId())
                .remark(dto.getRemark())
                .productCode(plan.getProductMergeCode())
                .productName(plan.getProductName())
                .productBatchNo(plan.getBatchNo())
                .build());
        confirmSerial(storageMaterial.getNo());
        List<StorageMaterialReserve> reserveList = new ArrayList<>();
        reserveList.add(storageMaterialReserve);
        this.publishStorageMaterialReserveEvent(dto.getProductPlanId(), Collections.singletonList(storageMaterial.getMaterialId()));
        return storageMaterial;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scrapBatch(List<OutputWeighRecord> list, String weigherId, String reCheckerId, String remark, Long productPlanId) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        Map<Long, OutputWeighRecord> recordMap = list.stream()
                .collect(Collectors.toMap(OutputWeighRecord::getStorageMaterialId, Function.identity(), (k1, k2) -> k1));
        List<StorageMaterial> storageMaterials = storageMaterialMapper.selectBatchIds(recordMap.keySet());
        if (storageMaterials.size() != recordMap.size()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        Map<Long, BigDecimal> diff = storageMaterials.stream()
                .collect(Collectors.toMap(BaseDO::getId, StorageMaterial::getReserveQuantity, (k1, k2) -> k1));
        Plan plan = planMapper.selectById(productPlanId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        // 保存称量作废日志
        storageMaterialPositionLogService.saveLogs(storageMaterials.stream()
                .map(item -> {
                    StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                    logDTO.setStorageMaterialId(item.getId());
                    logDTO.setOperateType(StorageOperateTypeEnum.WEIGH_SCRAP);
                    logDTO.setQuantity(unitCache.toExt(diff.get(item.getId()), item.getFinalUnitId()));
                    logDTO.setUnitId(item.getFinalUnitId());
                    logDTO.setSenderId(weigherId);
                    logDTO.setReceiverId(reCheckerId);
                    logDTO.setRemark(remark);
                    logDTO.setMaterialPositionId(item.getMaterialPositionId());
                    OutputWeighRecord outputWeighRecord = recordMap.get(item.getId());
                    if (outputWeighRecord != null) {
                        logDTO.setTareWeight(outputWeighRecord.getTareWeight());
                        logDTO.setGrossWeight(outputWeighRecord.getGrossWeight());
                    }
                    logDTO.setProductBatchNo(plan.getBatchNo());
                    logDTO.setProductCode(plan.getProductMergeCode());
                    logDTO.setProductName(plan.getProductName());
                    logDTO.setProductId(plan.getProductId());
                    return logDTO;
                }).collect(Collectors.toList()));
        storageMaterialMapper.scrapBatch(recordMap.keySet());
        // 删除预定信息
        storageMaterialReserveMapper.deleteByStorageMaterialIds(recordMap.keySet());
    }

    @Nullable
    @Override
    public StorageMaterial queryByMaterialNoIgnoreAvailable(String storageMaterialNo) {
        if (StrUtil.isBlank(storageMaterialNo)) {
            return null;
        }
        return storageMaterialMapper.queryByMaterialNoIgnoreAvailable(storageMaterialNo);
    }

    @Nullable
    @Override
    public StorageMaterial selectStorageMaterialByContainerId(Long id) {
        if (id == null) {
            return null;
        }
        return storageMaterialMapper.selectStorageMaterialByContainerId(id);
    }

    @Nullable
    @Override
    public StorageMaterial queryByContainerNo(String no) {
        if (StrUtil.isBlank(no)) {
            return null;
        }
        EquipmentInfoFeignVO container =
                FeignUtils.handleRequest(deviceNo -> equipmentConfigFeign.getEquipmentByEquipmentCode(deviceNo), no).getData();
        if (container == null) {
            return null;
        }
        StorageMaterial storageMaterial = storageMaterialMapper.selectStorageMaterialByContainerId(container.getId());
        if (storageMaterial == null) {
            return null;
        }
        return storageMaterial;
    }

    @Override
    public StorageMaterial getByContainerId(Long id) {
        return storageMaterialMapper.selectByContainerId(id);
    }

    @Override
    public List<StorageMaterialVO> queryInfoByIds(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<StorageMaterialVO> list = storageMaterialMapper.queryInfoByIds(ids);
        list.forEach(item -> item.setUnit(unitCache.getGlobalUnitName(item.getFinalUnitId())));
        PrecisionHelper.convertUnitRenderList(list);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelReserveByProductPlanId(Long productPlanId) {
        if (productPlanId == null) {
            return;
        }
        Plan plan = planMapper.selectById(productPlanId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        log.info("{}批生产结束，取消预定物料件", LOG_PREFIX);
        List<StorageMaterialReserve> storageMaterialReserves =
                storageMaterialReserveMapper.selectByProductPlanId(plan.getId());
        if (CollectionUtil.isEmpty(storageMaterialReserves)) {
            return;
        }
        log.info("{}取消预定物料件列表:{}", LOG_PREFIX, storageMaterialReserves);
        List<Long> storageMaterialIds = storageMaterialReserves.stream()
                .map(StorageMaterialReserve::getStorageMaterialId)
                .collect(Collectors.toList());
        StorageMaterialCancelReserveDTO reserve = new StorageMaterialCancelReserveDTO();
        reserve.setProductId(plan.getProductId());
        reserve.setProcessId(plan.getProcessId());
        reserve.setBatchId(plan.getId());
        reserve.setStorageMaterialIdList(storageMaterialIds);
        reserve.setOperatorId(SysUserHolder.getUser().getUserId());
        reserve.setReCheckerId(SysUserHolder.getUser().getUserId());
        // 取消预定
        this.cancelReserve(reserve);
    }

    /**
     * 获取物料件流水号
     *
     * @return 物料件流水号
     */
    @Override
    public String getSerial() {
        return FeignUtils.handleRequest(data -> platformCodeFeign.getNextUseNo(data), NextUseCodeDTO.builder()
                        .code(PlatformCodeConstants.MES_STORAGE_MATERIAL_SERIAL)
                        .fields(new HashMap<>())
                        .build())
                .getData().getNo();
    }

    @Override
    public List<String> batchGetSerial(int size) {
        return FeignUtils.handleRequest(data -> platformCodeFeign.getBatchNextUseNo(data), BatchNextUseCodeDTO.builder()
                        .code(PlatformCodeConstants.MES_STORAGE_MATERIAL_SERIAL)
                        .fields(new HashMap<>())
                        .num(size)
                        .build())
                .getData().getNos()
                .stream().map(BatchNextCodeVO.NextCodeVO::getNo).collect(Collectors.toList());
    }

    /**
     * 确认物料件编号
     *
     * @param serial
     */
    @Override
    public void confirmSerial(String serial) {
        FeignUtils.handleRequest(data -> platformCodeFeign.confirmNo(data), ConfirmNextUseCodeDTO.builder()
                .code(PlatformCodeConstants.MES_STORAGE_MATERIAL_SERIAL)
                .fullNo(serial)
                .fields(new HashMap<>())
                .build());
    }

    @Override
    public void batchConfirmSerial(List<String> serial) {
        FeignUtils.handleRequest(data -> platformCodeFeign.batchConfirmNo(data), BatchConfirmNextUseCodeDTO.builder()
                .code(PlatformCodeConstants.MES_STORAGE_MATERIAL_SERIAL)
                .fullNos(serial)
                .fields(new HashMap<>())
                .build());
    }

    @Nullable
    @Override
    public StorageMaterial queryByMaterialNo(String storageMaterialNo, Boolean available) {
        if (StrUtil.isBlank(storageMaterialNo)) {
            return null;
        }
        return storageMaterialMapper.queryByMaterialNo(storageMaterialNo, available);
    }

    @Override
    public void save(StorageMaterial storageMaterial) {
        storageMaterialMapper.insert(storageMaterial);
    }

    @Override
    public void saveBatch(List<StorageMaterial> storageMaterials) {
        storageMaterialMapper.insertBatch(storageMaterials);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signBatchByIdList(List<Long> storageMaterialIds) {
        if (CollectionUtil.isEmpty(storageMaterialIds)) {
            return;
        }
        List<StorageMaterial> list = storageMaterialMapper.queryUnsignedListByMaterialIds(storageMaterialIds);
        if (list.size() != storageMaterialIds.size()) {
            throw new BmosException(MesResponseCode.INGREDIENT_PLAN_RECORD_SIGNED);
        }
        list.forEach(item -> item.setSignStatus(WeighSignStatus.SIGNED));
        storageMaterialMapper.updateBatch(list);
    }

    @Override
    public List<StorageMaterial> queryListByIds(Collection<Long> consumeStorageMaterialIdList) {
        if (CollectionUtil.isEmpty(consumeStorageMaterialIdList)) {
            return new ArrayList<>();
        }
        return storageMaterialMapper.selectBatchIds(consumeStorageMaterialIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal weighConsume(List<StorageMaterial> list, String remark, Plan plan,
                                   StorageOperateTypeEnum operateType) {
        if (CollectionUtil.isEmpty(list)) {
            return BigDecimal.ZERO;
        }
        Map<Long, BigDecimal> diff = new HashMap<>();
        for (StorageMaterial storageMaterial : list) {
            BigDecimal diffValue;
            if (!BigDecimal.ZERO.equals(storageMaterial.getReserveQuantity())) {
                log.info("{}存在预定量 消耗预定量{}", storageMaterial.getNo(), storageMaterial.getReserveQuantity());
                // 预订量减少
                diffValue = storageMaterial.getReserveQuantity();
                storageMaterial.setReserveQuantity(BigDecimal.ZERO);
            } else {
                log.info("{}不存在预定量 消耗可用量{}", storageMaterial.getNo(), storageMaterial.getAvailableQuantity());
                diffValue = storageMaterial.getAvailableQuantity();
                storageMaterial.setAvailableQuantity(BigDecimal.ZERO);
            }
            diff.put(storageMaterial.getId(), diffValue);
            // 消耗量增加
            storageMaterial.setConsumeQuantity(storageMaterial.getConsumeQuantity().add(diffValue));
        }
        storageMaterialMapper.updateBatch(list);
        // 解绑容器
        storageMaterialMapper.unbindContainersByIds(list.stream()
                .map(StorageMaterial::getId)
                .collect(Collectors.toList())
        );
        // 查询预定信息
        List<Long> storageMaterialIds = list.stream().map(StorageMaterial::getId).collect(Collectors.toList());
        List<StorageMaterialReserve> reserveList =
                storageMaterialReserveMapper.queryByStorageMaterialIds(storageMaterialIds);
        Map<Long, StorageMaterialReserve> reserveMap =
                reserveList.stream().collect(Collectors.toMap(StorageMaterialReserve::getId, Function.identity(), (k1
                        , k2) -> k1));
        List<Long> productIds =
                reserveList.stream().map(StorageMaterialReserve::getProductId).collect(Collectors.toList());
        Map<Long, ProductMaterial> productMaterialMap = CollectionUtils.isAnyEmpty(productIds) ? new HashMap<>() :
                productMaterialMapper.selectListByBatchIds(productIds).stream().collect(Collectors.toMap(ProductMaterial::getId, Function.identity(), (k1, k2) -> k1));
        // 消耗物料件不再删除预定记录
        // 保存称量消耗日志
        String loginUserId = SysUserHolder.getUser().getUserId();
        storageMaterialPositionLogService.saveLogs(list.stream()
                .map(item -> {
                    StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                    logDTO.setStorageMaterialId(item.getId());
                    logDTO.setOperateType(operateType);
                    logDTO.setQuantity(diff.get(item.getId()));
                    logDTO.setUnitId(item.getFinalUnitId());
                    logDTO.setSenderId(loginUserId);
                    logDTO.setReceiverId(loginUserId);
                    logDTO.setRemark(remark);
                    Optional.ofNullable(item.getId())
                            .map(reserveMap::get)
                            .map(StorageMaterialReserve::getProductId)
                            .map(productMaterialMap::get)
                            .ifPresent(pm -> {
                                logDTO.setProductName(pm.getName());
                                logDTO.setProductCode(pm.getMergeCode());
                            });
                    if (plan != null) {
                        logDTO.setProductId(plan.getProductId());
                        logDTO.setProductName(plan.getProductName());
                        logDTO.setProductCode(plan.getProductMergeCode());
                        logDTO.setProductBatchNo(plan.getBatchNo());
                    }
                    logDTO.setMaterialPositionId(item.getMaterialPositionId());
                    // 生产批号
                    return logDTO;
                }).collect(Collectors.toList()));
        return diff.values().stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbindContainersByIds(List<Long> ids) {
        storageMaterialMapper.unbindContainersByIds(ids);
    }

    @Override
    public List<BatchAvailableMaterialVO> getAvailableStorageMaterial(AvailableStorageMaterialQueryDTO dto) {
        ProductFormulaMaterial formulaMaterial = formulaMaterialMapper.selectById(dto.getFormulaMaterialId());
        List<Long> deptIds = platformApiAdaptor.deptIds();
        List<BatchReservedAvailableMaterialVO> availableList =
                storageMaterialMapper.selectAvailableMaterial(formulaMaterial.getMaterialId(), deptIds);
        availableList.forEach(e -> {
            BigDecimal quantity = unitCache.toExt(e.getQuantity(), formulaMaterial.getUnitId());
            e.setQuantity(MaterialQuantityCalculateUtil.roundingOff(quantity, formulaMaterial));
            e.setReserveQuantity(quantity);
            e.setUnitId(formulaMaterial.getUnitId());
            e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId()));
            // 理论量 使用修约后的物料量计算
            e.setTheoreticalQuantity(MaterialQuantityCalculateUtil.calculateTheoreticalQuantity(e.getQuantity(),
                    e.getHydration(),
                    e.getNoHydrationContent(), formulaMaterial));
        });
        return StorageMaterialConverter.INSTANCE.convertToAvailableVO(availableList);
    }

    @Override
    public void updateBatch(List<StorageMaterial> storageMaterials) {
        storageMaterialMapper.updateBatch(storageMaterials);
    }

    @Override
    public List<StorageMaterial> reserveComponentReserve(ReserveComponentReserveMaterialDTO dto) {
        Plan plan = planMapper.selectById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        // 校验是否被其他生产批次预定
        List<StorageMaterialReserve> reservedByOther =
                storageMaterialReserveMapper.selectOtherBatchReserved(dto.getStorageMaterialIdList(), plan.getId());
        List<StorageMaterial> storageMaterials = storageMaterialMapper.selectBatchIds(dto.getStorageMaterialIdList());
        if (CollUtil.isNotEmpty(reservedByOther)) {
            StorageMaterialReserve first = CollUtil.getFirst(reservedByOther);
            Map<Long, StorageMaterial> storageMaterialMap = CollectionUtils.convertMap(storageMaterials,
                    StorageMaterial::getId);
            StorageMaterial storageMaterial = storageMaterialMap.get(first.getStorageMaterialId());
            throw new BmosException(MesResponseCode.MATERIAL_RESERVED_BY_OTHER_BATCH, storageMaterial.getNo());
        }
        // 校验是否存在过期批次
        List<StorageMaterialBatch> batches =
                storageMaterialBatchMapper.selectBatchIds(CollectionUtils.convertSet(storageMaterials,
                        StorageMaterial::getStorageMaterialBatchId));
        if (batches.stream().anyMatch(e -> !e.getAvailable())) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_EXPIRED);
        }
        List<StorageMaterialReserve> reserves = new ArrayList<>();
        List<StorageMaterialPositionLogDTO> logs = new ArrayList<>();
        for (StorageMaterial storageMaterial : storageMaterials) {
            StorageMaterialReserve reserve = new StorageMaterialReserve();
            reserve.setStorageMaterialId(storageMaterial.getId());
            reserve.setProcessId(plan.getProcessId());
            reserve.setBatchId(plan.getId());
            reserve.setBatchNo(plan.getBatchNo());
            reserve.setProductId(plan.getProductId());
            reserve.setReserveUserId(SysUserHolder.getUser().getUserId());
            reserve.setReserveTime(LocalDateTime.now());
            reserve.setReserveQuantity(storageMaterial.getAvailableQuantity());
            storageMaterial.setAvailableQuantity(BigDecimal.ZERO);
            storageMaterial.setReserveQuantity(reserve.getReserveQuantity());
            reserves.add(reserve);
            // 物料日志
            StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
            logDTO.setStorageMaterialId(storageMaterial.getId());
            logDTO.setOperateType(MATERIAL_RESERVE);
            logDTO.setQuantity(storageMaterial.getQuantity());
            logDTO.setUnitId(storageMaterial.getFinalUnitId());
            logDTO.setSenderId(SysUserHolder.getUser().getUserId());
            logDTO.setReceiverId(SysUserHolder.getUser().getUserId());
            logDTO.setProductId(plan.getProductId());
            logDTO.setProductName(plan.getProductName());
            logDTO.setProductCode(plan.getProductMergeCode());
            logDTO.setProductBatchNo(plan.getBatchNo());
            logDTO.setMaterialPositionId(storageMaterial.getMaterialPositionId());
            logs.add(logDTO);
        }
        storageMaterialMapper.updateBatch(storageMaterials);
        storageMaterialReserveMapper.insertBatch(reserves);
        storageMaterialPositionLogService.saveLogs(logs);
        this.publishStorageMaterialReserveEvent(plan.getId(), CollectionUtils.convertList(storageMaterials, StorageMaterial::getMaterialId));
        return storageMaterials;
    }

    @Override
    public ScanMaterialVO queryWeighStorageMaterial(ScanMaterialDeviceCodeDTO scanQuery) {
        // 按照物料件号搜索
        StorageMaterial storageMaterial = queryByMaterialNo(scanQuery.getNo(), false);
        if (storageMaterial == null) {
            // 按照容器编号搜索
            EquipmentInfoFeignVO container = FeignUtils.handleRequest(code -> equipmentConfigFeign.getEquipmentByEquipmentCode(code), scanQuery.getNo()).getData();
            // 判断是否是容器
            if (container != null && CollectionUtil.isNotEmpty(container.getEquipmentTagDataList())) {
                Optional<TagFeignVO> any = container.getEquipmentTagDataList().stream()
                        .filter(item -> Objects.equals(item.getCode(), EquipmentTagCodeEnum.CONTAINER_12021.getCode()))
                        .findAny();
                if (!any.isPresent()) {
                    throw new BmosException(MesResponseCode.EQUIPMENT_NOT_CONTAINER);
                }
                storageMaterial = queryByContainerNo(scanQuery.getNo());
                if (storageMaterial == null) {
                    throw new BmosException(MesResponseCode.EQUIPMENT_CONTAINER_NO_MATERIAL);
                }
            }

        }
        if (storageMaterial == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        StorageMaterialBatch batch = storageMaterialBatchMapper.selectById(storageMaterial.getStorageMaterialBatchId());
        if (batch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        batch.availableValidate();

        // 校验产品预定信息
        if (scanQuery.getProductPlanId() != null) {
            StorageMaterialReserve reserve = storageMaterialReserveMapper.queryByStorageMaterialId(storageMaterial.getId());
            if (reserve == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_TAG_NOT_RESERVED);
            }
            if (!Objects.equals(reserve.getBatchId(), scanQuery.getProductPlanId())) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_MATCH);
            }
        }

        // 校验出库
        if (scanQuery.getIsOutbound() != null) {
            if (scanQuery.getIsOutbound() && storageMaterial.getMaterialPositionId() != null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_OUTBOUND);
            } else if (!scanQuery.getIsOutbound() && storageMaterial.getMaterialPositionId() == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_OUTBOUNDED);
            }
        }

        // 校验配方物料id
        if (scanQuery.getFormulaMaterialId() != null) {
            ProductFormulaMaterial productFormulaMaterial = formulaMaterialMapper.selectById(scanQuery.getFormulaMaterialId());
            if (productFormulaMaterial == null || !Objects.equals(productFormulaMaterial.getMaterialId(), storageMaterial.getMaterialId())) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_MATCH);
            }
        }

        if (scanQuery.getIsAvailable() != null) {
            if (scanQuery.getIsAvailable() && !storageMaterial.isAvailable()) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_AVAILABLE);
            }
            if (!scanQuery.getIsAvailable() && storageMaterial.isAvailable()) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_AVAILABLE);
            }
        }

        // 物料件相关
        ScanMaterialVO result = new ScanMaterialVO();
        result.setId(storageMaterial.getId());
        result.setNo(storageMaterial.getNo());
        long unitId = scanQuery.getUnitId() == null ? storageMaterial.getUnitId() : scanQuery.getUnitId();
        // 物料件的预订量 使用配方的单位和精度换算修约
        result.setUnitId(unitId);
        result.setUnit(unitCache.getGlobalUnitName(unitId));
        result.setReserveQuantity(PrecisionHelper.precision(unitCache.toExt(storageMaterial.getReserveQuantity(), unitId), unitId));
        // 物料批次相关
        result.setMaterialBatchNo(batch.getMaterialBatchNo());
        result.setMaterialBatchId(batch.getId());
        result.setSupplier(batch.getSupplier());
        result.setHydration(batch.getHydration());
        result.setNoHydrationContent(batch.getNoHydrationContent());
        result.setExpiredDate(batch.getExpiredDate());
        result.setFactoryBatchNo(batch.getFactoryBatchNo());
        return result;
    }

    @Override
    public void consumeWholeMaterial(List<Long> inputMaterialIdList, String inputUserId, Plan plan,
                                     StorageOperateTypeEnum operateTypeEnum) {
        List<StorageMaterial> storageMaterialList = storageMaterialMapper.selectBatchIds(inputMaterialIdList);
        if (CollUtil.isEmpty(storageMaterialList)) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        for (StorageMaterial storageMaterial : storageMaterialList) {
            storageMaterial.consumeAllQuantity();
        }
        storageMaterialMapper.updateBatch(storageMaterialList);
        // 记录物料日志、
        List<StorageMaterialPositionLogDTO> storageMaterialPositionLogDTOS = new ArrayList<>();
        for (StorageMaterial storageMaterial : storageMaterialList) {
            StorageMaterialPositionLogDTO storageMaterialPositionLogDTO = new StorageMaterialPositionLogDTO();
            storageMaterialPositionLogDTO.setStorageMaterialId(storageMaterial.getId());
            storageMaterialPositionLogDTO.setOperateType(operateTypeEnum);
            storageMaterialPositionLogDTO.setQuantity(storageMaterial.getQuantity());
            storageMaterialPositionLogDTO.setUnitId(storageMaterial.getFinalUnitId());
            storageMaterialPositionLogDTO.setSenderId(SysUserHolder.getUser().getUserId());
            storageMaterialPositionLogDTO.setReceiverId(SysUserHolder.getUser().getUserId());
            storageMaterialPositionLogDTO.setProductId(plan.getProductId());
            storageMaterialPositionLogDTO.setProductName(plan.getProductName());
            storageMaterialPositionLogDTO.setProductCode(plan.getProductMergeCode());
            storageMaterialPositionLogDTO.setProductBatchNo(plan.getBatchNo());
            storageMaterialPositionLogDTO.setSenderId(inputUserId);
            storageMaterialPositionLogDTOS.add(storageMaterialPositionLogDTO);
        }
        storageMaterialPositionLogService.saveLogs(storageMaterialPositionLogDTOS);
    }

    @Override
    public List<StorageMaterial> queryListByNos(List<String> storateMaterialNoList) {
        if (CollectionUtil.isEmpty(storateMaterialNoList)) {
            return new ArrayList<>();
        }
        return storageMaterialMapper.queryListByNos(storateMaterialNoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendBackAndConsumeMobile(StorageMaterialConsumeDTO dto) {
        consumeMobile(dto, SEND_BACK_AND_CONSUME);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void destroyAndConsumeMobile(StorageMaterialConsumeDTO dto) {
        consumeMobile(dto, DESTROY_AND_CONSUME);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void useAndConsumeMobile(StorageMaterialConsumeDTO dto) {
        consumeMobile(dto, USE_AND_CONSUME);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> receiveMobile(StorageMaterialReceiveMobileDTO dto) {
        log.info("{}物料入库:{}", LOG_PREFIX, dto);
        int size = dto.getInboundList().size();
        ProductMaterial material = materialMapper.selectById(dto.getMaterialId());
        if (material == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        if (size > MAX_BOUND_SIZE) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_INBOUND_SIZE_EXCEED);
        }
        StorageMaterialBatch existBatch =
                storageMaterialBatchMapper.queryByMaterialIdAndBatchNo(dto.getMaterialId(),
                        dto.getMaterialBatchNo());
        Long batchId;
        if (existBatch != null) {
            // 比对有效期、原始批号、是否匹配
            if (!(Objects.equals(existBatch.getOriginalBatchNo(), dto.getOriginalCode())
                    && Objects.equals(existBatch.getExpiredDate(), dto.getExpiredDate()))) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_EXIST);
            }
            // 沿用批次
            batchId = existBatch.getId();
        } else {
            // 新增批次
            StorageMaterialBatch batch = new StorageMaterialBatch();
            batch.setMaterialId(dto.getMaterialId());
            batch.setUnitId(material.getUnitId());
            batch.setUnitExtendId(material.getUnitExtendId());
            batch.setMaterialBatchNo(dto.getMaterialBatchNo());
            batch.setOriginalBatchNo(dto.getOriginalCode());
            batch.setExpiredDate(dto.getExpiredDate());
            batch.setAvailable(!LocalDate.now().isAfter(dto.getExpiredDate()));
            batch.setLinkExplain(dto.getLinkExplain());
            batch.setSenderId(dto.getSenderId());
            batch.setReceiverId(dto.getReceiverId());
            batch.setReportNo(dto.getReportNo());
            batch.setLicenceNo(dto.getLicenceNo());
            batch.setFactoryBatchNo(dto.getOriginalBatchNo());
            batch.setProduceDate(dto.getProductDate());
            batch.setHydration(dto.getHydration());
            batch.setNoHydrationContent(dto.getNoHydrationContent());
            batch.setSupplier(dto.getSupplier());
            batch.setProducer(dto.getProducer());
            batch.setQualityStatus(dto.getQualityStatus());
            storageMaterialBatchMapper.insert(batch);
            batchId = batch.getId();

            // 保存自定义字段
            materialBatchFieldService.save(batchId, dto.getMaterialBatchFieldVOList());
        }

        List<StorageMaterial> list = new ArrayList<>();
        // 保存物料件信息
        for (StorageMaterialReceiveMobileDTO.InboundDTO inboundDTO : dto.getInboundList()) {
            for (int i = 0; i < inboundDTO.getSize(); i++) {
                StorageMaterial storageMaterial = new StorageMaterial();
                storageMaterial.setMaterialId(dto.getMaterialId());
                storageMaterial.setMaterialPositionId(dto.getMaterialPositionId());
                storageMaterial.setStorageMaterialBatchId(batchId);
                if (inboundDTO.getUnitExtendId() != null) {
                    storageMaterial.setUnitExtendId(inboundDTO.getUnitExtendId());
                    storageMaterial.setUnitId(Optional.ofNullable(inboundDTO.getUnitExtendId())
                            .map(unitCache::getGlobalUnit)
                            .map(CacheUnit::getParentUnitId)
                            .orElse(null)
                    );
                } else {
                    storageMaterial.setUnitId(inboundDTO.getUnitId());
                }
                storageMaterial.setInitQuantity(unitCache.toBasic(inboundDTO.getSingleQuantity(),
                        storageMaterial.getFinalUnitId()));
                storageMaterial.setAvailableQuantity(storageMaterial.getInitQuantity());
                storageMaterial.setConsumeQuantity(BigDecimal.ZERO);
                storageMaterial.setReserveQuantity(BigDecimal.ZERO);
                list.add(storageMaterial);
            }
        }
        List<String> nos = batchGetSerial(list.size());
        Iterator<String> iterator = nos.iterator();
        list.forEach(item -> item.setNo(iterator.next()));
        storageMaterialMapper.insertBatch(list);
        // 保存入库日志
        storageMaterialPositionLogService.saveLogs(list.stream()
                .map(item -> {
                    StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                    logDTO.setStorageMaterialId(item.getId());
                    logDTO.setOperateType(StorageOperateTypeEnum.RECEIVE);
                    logDTO.setQuantity(unitCache.toExt(item.getAvailableQuantity(), item.getFinalUnitId()));
                    logDTO.setUnitId(item.getFinalUnitId());
                    logDTO.setSenderId(dto.getSenderId());
                    logDTO.setReceiverId(dto.getReceiverId());
                    logDTO.setRemark(dto.getLinkExplain());
                    logDTO.setMaterialPositionId(item.getMaterialPositionId());
                    return logDTO;
                }).collect(Collectors.toList()));
        // 确认编号
        batchConfirmSerial(nos);
        return nos;
    }

    @Override
    public ScanDeviceVO scanDeviceCodeAndValidateStationIds(ScanDeviceCodeValidateStationDTO scanQuery) {
        // 查询设备信息
        ResponseInfo<EquipmentInfoFeignVO> responseInfo = FeignUtils.handleRequest(data-> equipmentConfigFeign.getEquipmentByEquipmentCodeWithoutPermission(data), scanQuery.getDeviceCode());
        EquipmentInfoFeignVO equipmentInfoFeignVO = responseInfo.getData();
        if (Objects.isNull(equipmentInfoFeignVO)){
            // 设备不存在
            throw new BmosException(MesResponseCode.EQUIPMENT_NOT_EXIST);
        }
        // 设备是否可用
        if (!EquipmentStatusCodeEnum.AVAILABLE.getCode().equals(equipmentInfoFeignVO.getStatus())){
            throw new BmosException(MesResponseCode.PREPARATION_PRODUCE_CONTAINER_NOT_AVAILABLE, equipmentInfoFeignVO.getCode());
        }

        ScanDeviceVO scanDeviceVO = new ScanDeviceVO();
        scanDeviceVO.setDeviceId(equipmentInfoFeignVO.getId());
        scanDeviceVO.setDeviceName(equipmentInfoFeignVO.getName());
        scanDeviceVO.setDeviceCode(equipmentInfoFeignVO.getCode());
        if (scanQuery.getComponentId() == null || scanQuery.getProcedureStepModelId() == null || scanQuery.getProductPlanId() == null){
            return scanDeviceVO;
        }

        // 获取当前生产计划绑定的产线id
        Plan plan = planMapper.selectById(scanQuery.getProductPlanId());
        if (plan == null){
            return scanDeviceVO;
        }
        Long productionLineId = plan.getProductionLineId();
        ResponseInfo<List<FactoryStationFeignVO>> lineStationResponse = FeignUtils.handleRequest(data -> factoryFeign.getStationInfoByLineId(data), productionLineId);
        Set<Long> stationIdSet = new HashSet<>();
        List<FactoryStationFeignVO> lineStations = lineStationResponse.getData();
        if (CollUtil.isNotEmpty(lineStations)){
            stationIdSet = lineStations.stream().map(FactoryStationFeignVO::getId).collect(Collectors.toSet());
        }
        // 判断当前设备所属工位是否在配置中
        String componentConfigJson = procedureStepConfigService.getStepComponentConfigJson(scanQuery.getProcedureStepModelId(), scanQuery.getComponentId());
        BasicComponentConfig basicComponentConfig = StrUtil.isNotEmpty(componentConfigJson)  ? JSON.parseObject(componentConfigJson, BasicComponentConfig.class) : null;
        if (Objects.nonNull(basicComponentConfig) && CollUtil.isNotEmpty(basicComponentConfig.getStation())){
            // 过滤当前配置的工位id不在生产批次对应的产线下
            stationIdSet = basicComponentConfig.getStation().stream().filter(stationIdSet::contains).collect(Collectors.toSet());
        }
        if (!CollectionUtil.containsAny(equipmentInfoFeignVO.getStationIdList(), stationIdSet)){
            throw new BmosException(MesResponseCode.CANT_CHARGE_IN_THIS_DEVICE);
        }
        return scanDeviceVO;
    }

    @Override
    public boolean validateReserveStatus(Long storageMaterialId, Long productPlanId) {
        StorageMaterialReserve reserve = storageMaterialReserveMapper.selectByStorageMaterialId(storageMaterialId);
        if (reserve == null || Objects.equals(reserve.getBatchId(), productPlanId)) {
            return false;
        }
        List<ProductPlanRelation> list = productPlanRelationService.getList(productPlanId);
        boolean match = list.stream().anyMatch(item -> Objects.equals(item.getRelationProductPlanId(), reserve.getBatchId()));
        if (match) {
            return false;
        }
        return true;
    }

    @Override
    public StorageMaterialDetailVO queryByCodeAndValidate(StorageMaterialQueryValidateDTO dto) {
        StorageMaterial storageMaterial = storageMaterialMapper.queryByMaterialNo(dto.getNo(), null);
        if (storageMaterial == null) {
            storageMaterial = queryByContainerCode(dto.getNo());
        }
        StorageMaterialDetailVO result = new StorageMaterialDetailVO();
        result.setStorageMaterial(storageMaterial);
        // 可用状态校验
        if (dto.isValidateAvailable()) {
            storageMaterial.availableValidate();
        }
        // 批次状态校验
        StorageMaterialBatch batch = null;
        if (dto.isValidateBatch()) {
            batch = storageMaterialBatchMapper.selectById(storageMaterial.getStorageMaterialBatchId());
            if (batch == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
            }
            batch.availableValidate();
        }
        // 预定状态校验
        result.setOrderByOthers(validateReserveStatus(storageMaterial.getId(), dto.getProductPlanId()));
        if (dto.isValidateReserve() && result.isOrderByOthers()) {
            throw new BmosException(MesResponseCode.RESERVED_BY_UNRELATED_BATCH);
        }
        // 出库状态校验
        if (dto.isValidateOutbound()) {
            storageMaterial.outboundValidate();
        }
        result.setStorageMaterialBatch(batch == null ? storageMaterialBatchMapper.selectById(storageMaterial.getStorageMaterialBatchId()) : batch);
        result.setStorageMaterialReserve(storageMaterialReserveMapper.queryByStorageMaterialId(storageMaterial.getId()));
        return result;
    }

    @Override
    public StorageMaterial selectById(Long id) {
        return storageMaterialMapper.selectById(id);
    }

    @Override
    public StorageMaterialDetailVO scanLHStorageMaterial(ScanLhStorageMaterialDTO dto) {
        StorageMaterial storageMaterial = this.queryByMaterialNo(dto.getNo(), null);
        if (Objects.isNull(storageMaterial)){
            storageMaterial = queryNoErrorByContainerCode(dto.getNo());
        }
        if (Objects.isNull(storageMaterial)){
            throw new BmosException(MesResponseCode.PLEASE_SCAN_MATERIAL_OR_CONTAINER);
        }
        // 识别物料是原辅包还是中间品
        Long materialId = storageMaterial.getMaterialId();
        ProductMaterial productMaterial = materialMapper.selectById(materialId);
        if (CategoryInfoTypeEnum.PRODUCTION.getValue().equals(productMaterial.getCategoryType())){
            throw new BmosException(MesResponseCode.WEIGH_SCAN_STORAGE_MATERIAL_TYPE_ERROR);
        }

        // 校验物料批次是否属于当前传递的物料批次
        if (!Objects.equals(storageMaterial.getStorageMaterialBatchId(), dto.getStorageMaterialBatchId())) {
            throw new BmosException(MesResponseCode.WEIGH_STORAGE_MATERIAL_NOT_BELONG_BATCH, storageMaterial.getNo());
        }
        // 物料件是否生效
        if (storageMaterial.getAvailableQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_AVAILABLE);
        }
        storageMaterial.availableValidate();

        // 批次有效期校验
        StorageMaterialBatch batch = storageMaterialBatchMapper.selectById(storageMaterial.getStorageMaterialBatchId());
        if (batch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        // 批次是否合格 校验批次是否在有效期内
        batch.availableValidate();

        StorageMaterialDetailVO result = new StorageMaterialDetailVO();
        result.setStorageMaterial(storageMaterial);
        // 预定状态校验
        StorageMaterialReserve reserve = storageMaterialReserveMapper.selectByStorageMaterialId(storageMaterial.getId());

        result.setOrderByOthers(Objects.nonNull(reserve));
        if (Objects.nonNull(dto.getProductPlanId()) && result.isOrderByOthers()) {
            throw new BmosException(MesResponseCode.WEIGH_STORAGE_MATERIAL_ALREADY_RESERVE);
        }
        // 物料件是否出库
        storageMaterial.outboundValidate();

        result.setStorageMaterialBatch(batch);
        result.setStorageMaterialReserve(storageMaterialReserveMapper.queryByStorageMaterialId(storageMaterial.getId()));
        return result;
    }

    private StorageMaterial queryByContainerCode(String no) {
        StorageMaterial storageMaterial;
        EquipmentInfoFeignVO equipment = FeignUtils.handleRequest(data -> equipmentConfigFeign.getEquipmentByEquipmentCodeWithoutPermission(data), no).getData();
        if (equipment == null) {
            throw new BmosException(MesResponseCode.PLEASE_CHECK_INPUT_NO);
        }
        // 判断是否是容器
        if (CollectionUtil.isNotEmpty(equipment.getEquipmentTagDataList())) {
            Optional<TagFeignVO> any = equipment.getEquipmentTagDataList().stream()
                    .filter(item -> Objects.equals(item.getCode(), EquipmentTagCodeEnum.CONTAINER_12021.getCode()))
                    .findAny();
            if (!any.isPresent()) {
                throw new BmosException(MesResponseCode.PLEASE_CHECK_INPUT_NO);
            }
        }
        if ((storageMaterial = storageMaterialMapper.selectByContainerId(equipment.getId())) == null) {
            throw new BmosException(MesResponseCode.PLEASE_CHECK_INPUT_NO);
        }
        return storageMaterial;
    }

    private StorageMaterial queryNoErrorByContainerCode(String no){
        StorageMaterial storageMaterial;
        EquipmentInfoFeignVO equipment = FeignUtils.handleRequest(data -> equipmentConfigFeign.getEquipmentByEquipmentCodeWithoutPermission(data), no).getData();
        if (equipment == null){
            return null;
        }
        // 判断是否是容器
        if (CollectionUtil.isNotEmpty(equipment.getEquipmentTagDataList())) {
            Optional<TagFeignVO> any = equipment.getEquipmentTagDataList().stream()
                    .filter(item -> Objects.equals(item.getCode(), EquipmentTagCodeEnum.CONTAINER_12021.getCode()))
                    .findAny();
            if (!any.isPresent()) {
                return null;
            }
        }
        if ((storageMaterial = storageMaterialMapper.selectByContainerId(equipment.getId())) == null) {
            return null;
        }
        return storageMaterial;
    }

    /**
     * 物料消耗
     *
     * @param dto
     * @param operateType 消耗类型
     */
    private void consumeMobile(StorageMaterialConsumeDTO dto, StorageOperateTypeEnum operateType) {
        List<Long> storageMaterialIdList = dto.getStorageMaterialIdList();
        if (CollectionUtil.isEmpty(storageMaterialIdList)) {
            return;
        }
        List<StorageMaterial> storageMaterials = storageMaterialMapper.selectBatchIds(storageMaterialIdList);
        if (storageMaterialIdList.size() != storageMaterials.size()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        Map<Long, BigDecimal> quantityMap = new HashMap<>();
        for (StorageMaterial storageMaterial : storageMaterials) {
            if (!storageMaterial.isAvailable()) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_AVAILABLE);
            }
            if (storageMaterial.getMaterialPositionId() == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_OUTBOUNDED);
            }

            BigDecimal quantity = storageMaterial.getReserveQuantity().add(storageMaterial.getAvailableQuantity());
            quantityMap.put(storageMaterial.getId(), quantity);
            storageMaterial.setConsumeQuantity(storageMaterial.getConsumeQuantity().add(quantity));
            storageMaterial.setAvailableQuantity(BigDecimal.ZERO);
            storageMaterial.setReserveQuantity(BigDecimal.ZERO);
        }

        Map<Long, Long> positionMap = storageMaterials.stream().collect(Collectors.toMap(StorageMaterial::getId, StorageMaterial::getMaterialPositionId, (v1, v2) -> v1));

        // 更新量
        storageMaterialMapper.updateBatch(storageMaterials);
        // 解绑货位
        storageMaterialMapper.updatePositionIdNullByIds(storageMaterialIdList);
        // 消耗不删除预定记录
//        storageMaterialReserveMapper.deleteByStorageMaterialIds(storageMaterialIdList);
        // 解绑容器
        storageMaterialMapper.unbindContainersByIds(storageMaterialIdList);
        // 保存消耗日志
        storageMaterialPositionLogService.saveLogs(storageMaterials.stream()
                .map(item -> {
                    StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
                    logDTO.setStorageMaterialId(item.getId());
                    logDTO.setOperateType(operateType);
                    logDTO.setQuantity(quantityMap.get(item.getId()));
                    logDTO.setUnitId(item.getFinalUnitId());
                    logDTO.setSenderId(dto.getOperatorId());
                    logDTO.setReceiverId(dto.getReCheckerId());
                    logDTO.setRemark(dto.getLinkExplain());
                    logDTO.setMaterialPositionId(positionMap.get(item.getId()));
                    return logDTO;
                }).collect(Collectors.toList()));
    }

    /**
     * 根据已存在的物料件拆出一个新的包
     *
     * @param storageMaterial 原物料件
     * @param reserveDif      预订量差值
     * @param availableDif    可用量差值
     * @param containerId     容器id
     * @return 新的物料件
     */
    private StorageMaterial splitNewPackage(StorageMaterial storageMaterial, BigDecimal reserveDif,
                                            BigDecimal availableDif, Long containerId) {
        StorageMaterial child = new StorageMaterial();
        child.setMaterialId(storageMaterial.getMaterialId());
        child.setStorageMaterialBatchId(storageMaterial.getStorageMaterialBatchId());
        // 拆包出库产生新的物料件出暂存货位
        child.setMaterialPositionId(null);
        child.setNo(getSerial());
        child.setInitQuantity(reserveDif.add(availableDif));
        child.setAvailableQuantity(availableDif);
        child.setConsumeQuantity(BigDecimal.ZERO);
        child.setReserveQuantity(reserveDif);
        child.setUnitId(storageMaterial.getUnitId());
        child.setUnitExtendId(storageMaterial.getUnitExtendId());
        if (containerId != null) {
            EquipmentInfoFeignVO container =
                    FeignUtils.handleRequest(ctId -> equipmentConfigFeign.getConfigByEquipmentId(ctId), containerId).getData();
            if (container != null) {
                if (!Objects.equals(EquipmentStatusCodeEnum.AVAILABLE.getCode(), container.getStatus())) {
                    throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_NOT_AVAILABLE);
                }
                StorageMaterial exist = getByContainerId(container.getId());
                if (exist != null) {
                    throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_OCCUPY);
                }
                child.setContainerId(container.getId());
                child.setContainer(container.getCode() + "-" + container.getName());
                // 设备占用
                equipmentConfigFeign.applyEquipment(containerId);
            }
        }
        return child;
    }
}
