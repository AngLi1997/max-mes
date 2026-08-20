package com.bmos.mes.service.weigh.centre2.requirement.service.impl;

import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.formula.mapper.ProductFormulaMaterialMapper;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.mes.service.weigh.centre.config.mapper.IWeighCentreMapper;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentre;
import com.bmos.mes.service.weigh.centre2.requirement.dto.RequirementQueryDTO;
import com.bmos.mes.service.weigh.centre2.requirement.dto.TicketRequirementQueryDTO;
import com.bmos.mes.service.weigh.centre2.requirement.mapper.ITicketRequirementMapper;
import com.bmos.mes.service.weigh.centre2.requirement.service.ITicketRequirementService;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementOccupancyQuantityResult;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementVO;
import com.bmos.mes.service.weigh.centre2.requirement.vo.WeighRequirementListVO;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 配料信息物料查询Service实现类
 *
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:16
 */
@Service
public class TicketRequirementServiceImpl implements ITicketRequirementService {

    @Resource
    private ITicketRequirementMapper weighRequirementMapper;

    @Resource
    private ProductFormulaMaterialMapper formulaMaterialMapper;

    @Resource
    private UnitCache unitCache;

    @Resource
    private IWeighCentreMapper weighCentreMapper;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    /**
     * 根据物料ID查询配料信息物料列表
     *
     * @param queryDTO 查询参数
     * @return 配料信息物料列表
     */
    @Override
    public List<TicketRequirementVO> queryMaterialList(TicketRequirementQueryDTO queryDTO) {

        ProductFormulaMaterial productFormulaMaterial = formulaMaterialMapper.selectById(queryDTO.getFormulaMaterialId());
        if (productFormulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXIST);
        }

        Long materialId = productFormulaMaterial.getMaterialId();

        Long unitId = productFormulaMaterial.getUnitId();
        String unitName = unitCache.getGlobalUnitName(unitId);
        List<TicketRequirementVO> list = weighRequirementMapper.selectMaterialList(materialId);


        // 查询物料批次id列表
        List<Long> storageMaterialBatchIds = list.stream().map(TicketRequirementVO::getStorageMaterialBatchId).collect(Collectors.toList());
        // 根据物料批次查询每个批次占用量
        List<TicketRequirementOccupancyQuantityResult> occupancy = weighRequirementMapper.selectOccupancyQuantity(storageMaterialBatchIds, null);
        // 统一单位
        Map<Long, BigDecimal> occupancyMap = new HashMap<>();
        // 批次占有量求和
        for (TicketRequirementOccupancyQuantityResult result : occupancy) {
            occupancyMap.putIfAbsent(result.getStorageMaterialBatchId(), BigDecimal.ZERO);
            BigDecimal addValue;
            if (Objects.equals(result.getUnitId(), unitId)) {
                addValue = result.getOccupancyQuantity();
            } else {
                addValue = unitCache.toExt(unitCache.toBasic(result.getOccupancyQuantity(), result.getUnitId()), unitId);
            }
            occupancyMap.put(result.getStorageMaterialBatchId(), occupancyMap.get(result.getStorageMaterialBatchId()).add(addValue));
        }

        list.forEach(item -> {
            item.setUnitId(unitId);
            item.setUnitName(unitName);
            // 库存量
            item.setQuantity(MaterialQuantityCalculateUtil.roundingOff(unitCache.toExt(item.getQuantity(), unitId), productFormulaMaterial));
            // 占用量
            item.setOccupancyQuantity(MaterialQuantityCalculateUtil.roundingOff(occupancyMap.getOrDefault(item.getStorageMaterialBatchId(), BigDecimal.ZERO), productFormulaMaterial));
            // 理论量
            item.setTheoreticalQuantity(MaterialQuantityCalculateUtil.calculateTheoreticalQuantity(checkZero(item.getQuantity().subtract(item.getOccupancyQuantity())),
                    item.getHydration(),
                    item.getNoHydrationContent(),
                    productFormulaMaterial));
        });
        return list;
    }

    @Override
    public List<WeighRequirementListVO> list(RequirementQueryDTO queryDTO) {

        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollectionUtils.isAnyEmpty(deptIds)){
            return new ArrayList<>();
        }
        List<WeighCentre> weighCentres = weighCentreMapper.listAllByDeptIds(deptIds);
        if (CollectionUtils.isAnyEmpty(weighCentres)){
            return new ArrayList<>();
        }
        return weighRequirementMapper.queryRequirementList(queryDTO, CollectionUtils.convertList(weighCentres, WeighCentre::getId));
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