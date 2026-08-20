package com.bmos.mes.service.unit.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.unit.PlatformUnitFeignClient;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.unit.service.UnitService;
import com.bmos.mes.service.unit.vo.ExtendUnitPullDownBoxVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UnitServiceImpl implements UnitService {

    @Autowired
    private PlatformUnitFeignClient unitFeignClient;

    @Autowired
    private ProductMaterialMapper productMaterialMapper;

    @Override
    public List<ExtendUnitPullDownBoxVO> listByMaterialId(Long materialId) {
        ProductMaterial productMaterial = productMaterialMapper.selectById(materialId);
        if (ObjectUtil.isNull(productMaterial)) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        Long platformMaterialId = productMaterial.getPlatformMaterialId();
        ResponseInfo<List<ExtendUnitPullDownBoxVO>> listResponseInfo = FeignUtils
                .handleRequest(data -> unitFeignClient.getExtendUnitByMaterialId(data), platformMaterialId);
        return listResponseInfo.getData();
    }
}
