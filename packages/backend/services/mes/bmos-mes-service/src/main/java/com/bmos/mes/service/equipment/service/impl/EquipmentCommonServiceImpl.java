package com.bmos.mes.service.equipment.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.response.ResponseItem;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.equipment.service.EquipmentCommonService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.platform.facade.equipment.enums.EquipmentTagCodeEnum;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.TagFeignVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class EquipmentCommonServiceImpl implements EquipmentCommonService {

    @Autowired
    private EquipmentConfigFeign equipmentConfigFeign;

    @Override
    public Long checkIsContainerByCode(String code, ResponseItem noDataException) {
        ResponseInfo<EquipmentInfoFeignVO> res =
                FeignUtils.handleRequest(data -> equipmentConfigFeign.getEquipmentByEquipmentCodeWithoutPermission(data), code);
        EquipmentInfoFeignVO data = res.getData();
        if (data == null) {
            throw new BmosException(noDataException == null ? MesResponseCode.EQUIPMENT_NOT_EXIST : noDataException);
        }
        // 判断是否是容器
        if (CollectionUtil.isNotEmpty(data.getEquipmentTagDataList())) {
            Optional<TagFeignVO> any = data.getEquipmentTagDataList().stream()
                    .filter(item -> Objects.equals(item.getCode(), EquipmentTagCodeEnum.CONTAINER_12021.getCode()))
                    .findAny();
            if (!any.isPresent()) {
                throw new BmosException(MesResponseCode.EQUIPMENT_NOT_CONTAINER);
            }
        }
        return data.getId();
    }

    @Override
    public Long checkIsContainerThrCheckNo(String code) {
        ResponseInfo<EquipmentInfoFeignVO> res =
                FeignUtils.handleRequest(data -> equipmentConfigFeign.getEquipmentByEquipmentCodeWithoutPermission(data), code);
        EquipmentInfoFeignVO data = res.getData();
        if (data == null) {
            throw new BmosException(MesResponseCode.PLEASE_CHECK_INPUT_NO);
        }
        // 判断是否是容器
        if (CollectionUtil.isNotEmpty(data.getEquipmentTagDataList())) {
            Optional<TagFeignVO> any = data.getEquipmentTagDataList().stream()
                    .filter(item -> Objects.equals(item.getCode(), EquipmentTagCodeEnum.CONTAINER_12021.getCode()))
                    .findAny();
            if (!any.isPresent()) {
                throw new BmosException(MesResponseCode.PLEASE_CHECK_INPUT_NO);
            }
        }
        return data.getId();
    }
}
