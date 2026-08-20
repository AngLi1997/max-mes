package com.bmos.mes.service.equipment.service.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.mes.service.equipment.service.IEquipmentHeartbeatService;
import com.bmos.platform.facade.equipment.dto.EquipmentApplyHeartDTO;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * EquipmentHeartbeatServiceImpl
 *
 * @author: liang
 * @date: 2024/5/8 15:34
 * @version: 1
 */
@Service
@Slf4j
public class EquipmentHeartbeatServiceImpl implements IEquipmentHeartbeatService {

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Override
    public void flushHeartbeat(Long equipmentId, String batchNo, String productName) {
        EquipmentApplyHeartDTO heart = new EquipmentApplyHeartDTO();
        heart.setId(equipmentId);
        heart.setBatchNo(batchNo);
        heart.setProductName(productName);
        try {
            equipmentConfigFeign.applyEquipmentHeart(heart);
        } catch (Exception e){
            if (e instanceof BmosException){
                throw e;
            }else {
                log.error("设备心跳发送失败:{}",e.getMessage());
            }
        }
    }
}
