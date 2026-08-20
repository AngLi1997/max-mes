package com.bmos.mes.service.equipment.service;

import com.bmos.common.response.ResponseItem;

public interface EquipmentCommonService {


    /**
     * 根据设备编码判断是否是容器并返回容器id
     * @param code 设备编码
     * @param noDataException 设备code不存在时抛出的异常
     * @return
     */
    Long checkIsContainerByCode(String code, ResponseItem noDataException);


    /**
     * 根据设备编码判断是否是容器并返回容器id
     * 内部异常都抛出PLEASE_CHECK_INPUT_NO
     * @param code
     * @return
     */
    Long checkIsContainerThrCheckNo(String code);
}
