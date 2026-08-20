package com.bmos.mes.service.equipment.service;

/**
 * 设备心跳接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 09:20
 */
public interface IEquipmentHeartbeatService {

    /**
     * 刷新设备心跳
     *
     * @param equipmentId 设备id
     * @param batchNo     生产批号
     * @param productName 产品名称
     */
    void flushHeartbeat(Long equipmentId, String batchNo, String productName);
}
