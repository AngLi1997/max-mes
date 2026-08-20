package com.bmos.mes.service.storage.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.MaterialExpandInfo;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.storage.config.mapper.IStorageMapper;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.service.ICargoPositionService;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialBatchPageQuery;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.storage.manage.service.MaterialBatchFieldService;
import com.bmos.mes.service.storage.manage.vo.*;
import com.bmos.mes.service.utils.BigDecimalFormatUtil;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/20 10:28
 */
@Service
public class StorageMaterialBatchServiceImpl implements IStorageMaterialBatchService {

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Resource
    private IStorageMaterialMapper storageMaterialMapper;

    @Resource
    private UnitCache unitCache;

    @Resource
    private ICargoPositionService cargoPositionService;

    @Resource
    private IStorageMapper storageMapper;

    @Resource
    private MaterialBatchFieldService materialBatchFieldService;

    @Resource
    private ProductMaterialMapper materialMapper;

    @Override
    public CommonPage<StorageMaterialBatchVO> queryPage(StorageMaterialBatchPageQuery pageQuery) {

        List<StorageMaterial> list;
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
            list = storageMaterialMapper.queryListByPositionIds(collect);
        } else {
            positionIdList.add(positionId);
            // 传的是货位id
            list = storageMaterialMapper.queryListByPositionId(positionId);
        }
        Set<Long> storageMaterialBatchIdList = list.stream()
                .map(StorageMaterial::getStorageMaterialBatchId)
                .collect(Collectors.toSet());
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<StorageMaterialBatchVO> result = storageMaterialBatchMapper.queryList(pageQuery, storageMaterialBatchIdList);
        CommonPage<StorageMaterialBatchVO> page = CommonPage.convertPage(result);
        List<Long> batchIds = page.getList().stream()
                .map(StorageMaterialBatchVO::getId)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(batchIds)) {
            return page;
        }
        Map<Long, List<StorageMaterial>> group = storageMaterialMapper.queryListByBatchIdsAndPositionId(batchIds, positionIdList)
                .stream()
                .collect(Collectors.groupingBy(StorageMaterial::getStorageMaterialBatchId));

        for (StorageMaterialBatchVO vo : page.getList()) {
            vo.setUnit(unitCache.getGlobalUnitName(vo.getFinalUnitId()));
            List<StorageMaterial> storageMaterials = group.get(vo.getId()) == null ? new ArrayList<>() : group.get(vo.getId());
            // 件数
            vo.setSize(storageMaterials.size());
            // 可用量
            BigDecimal available = storageMaterials.stream()
                    .map(StorageMaterial::getAvailableQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal reserve = storageMaterials.stream()
                    .map(StorageMaterial::getReserveQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            vo.setRate(Optional.ofNullable(vo.getFinalUnitId())
                    .map(unitId -> unitCache.getGlobalUnit(unitId))
                    .map(CacheUnit::getRate)
                    .map(BigDecimal::toPlainString)
                    .orElse("1"));
            vo.setAvailableQuantity(BigDecimalFormatUtil.formatBigDecimal(available));
            vo.setReserveQuantity(BigDecimalFormatUtil.formatBigDecimal(reserve));
            vo.setQuantity(BigDecimalFormatUtil.formatBigDecimal(available.add(reserve)));
            // 扩展信息
            fillExtendsData(vo);
        }
        // 精度修约
        PrecisionHelper.convertUnitRenderList(page.getList());
        return page;
    }

    @Override
    public List<MaterialBatchListVO> queryMaterialBatchListByMaterialId(Long materialId, String batchNo) {
        List<StorageMaterialBatch> list = storageMaterialBatchMapper.queryListByMaterialIdAndLikeBatchNo(materialId, batchNo);
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(item -> {
            MaterialBatchListVO vo = new MaterialBatchListVO();
            vo.setId(item.getId());
            vo.setMaterialBatchNo(item.getMaterialBatchNo());
            vo.setOriginalBatchNo(item.getOriginalBatchNo());
            vo.setExpiredDate(item.getExpiredDate());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean checkExistedBatchByMaterialId(Long materialId) {
        return storageMaterialBatchMapper.checkExistedBatchByMaterialId(materialId);
    }

    @Override
    public List<ReservedBatchInfo> queryReservedBatch(Long batchId, Long materialId) {
        return storageMaterialBatchMapper.selectReservedBatch(batchId, materialId);
    }

    @Override
    public List<StorageMaterialBatch> queryListByIds(List<Long> longs) {
        return storageMaterialBatchMapper.selectBatchIds(longs);
    }

    @Override
    public StorageMaterialBatch getById(Long storageMaterialBatchId) {
        return storageMaterialBatchMapper.selectById(storageMaterialBatchId);
    }

    @Override
    public List<ReservedBatchInfo> queryReservedBatch(Long productPlanId, List<Long> materialBatchIdList) {
        if (CollUtil.isEmpty(materialBatchIdList)) {
            return new ArrayList<>();
        }
        return storageMaterialBatchMapper.selectReservedBatchByBatchId(productPlanId, materialBatchIdList);
    }

    @Override
    public StorageMaterialBatchDetailVO queryMaterialBatchDetail(Long materialBatchId) {
        StorageMaterialBatch batch = storageMaterialBatchMapper.selectById(materialBatchId);
        if (batch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        ProductMaterial productMaterial = materialMapper.selectById(batch.getMaterialId());
        if (productMaterial == null) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        StorageMaterialBatchDetailVO result = new StorageMaterialBatchDetailVO();
        result.setMaterialBatchNo(batch.getMaterialBatchNo());
        result.setProduceDate(batch.getProduceDate());
        result.setHydration(batch.getHydration());
        result.setNoHydrationContent(batch.getNoHydrationContent());
        result.setFactoryBatchNo(batch.getFactoryBatchNo());
        result.setReportNo(batch.getReportNo());
        result.setLicenceNo(batch.getLicenceNo());
        result.setQualityStatus(MaterialQualityStatusEnum.getEnumByValue(batch.getQualityStatus()));
        result.setExpiredDate(batch.getExpiredDate());
        result.setMaterialName(productMaterial.getName());
        result.setMaterialMergeCode(productMaterial.getMergeCode());
        List<MaterialBatchFieldVO> materialBatchFieldVOS = materialBatchFieldService.queryMaterialField(materialBatchId);
        result.setFieldList(materialBatchFieldVOS);
        return result;
    }

    @Override
    public StorageMaterialBatch queryMaterialBatchByNoAndMaterialId(Long materialId, String materialBatchNo) {
        return storageMaterialBatchMapper.queryMaterialBatchByNoAndMaterialId(materialId, materialBatchNo);
    }

    @Override
    public void createMaterialBatch(StorageMaterialBatch storageMaterialBatch) {
        storageMaterialBatchMapper.insert(storageMaterialBatch);
    }

    @Override
    public void updateById(StorageMaterialBatch storageMaterialBatch) {
        storageMaterialBatchMapper.updateById(storageMaterialBatch);
    }

    private void fillExtendsData(StorageMaterialBatchVO batchVO) {
        if (batchVO == null) {
            return;
        }
        String json = batchVO.getExpandInfoJson();
        if (StrUtil.isBlank(json)) {
            return;
        }
        MaterialExpandInfo ex = JsonUtils.parseObject(json, MaterialExpandInfo.class);
        if (ex == null) {
            return;
        }
        batchVO.setExpandInfo(ex);
    }
}
