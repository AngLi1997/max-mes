package com.bmos.mes.service.execute.service;

import com.bmos.mes.service.execute.dto.ExecuteEquipmentCodeQueryDTO;
import com.bmos.mes.service.execute.dto.ExecuteEquipmentQueryDTO;
import com.bmos.mes.service.execute.vo.ExecuteEquipmentVO;

import java.util.List;

public interface ExecuteCommonService {

    /**
     * 获取组件执行时设备列表
     * 若组件配置工位 过滤出配置中生产计划产线的工位下所有设备
     * 若组件未配置工位 过滤出生产计划产线下的所有设备
     * @param dto
     * @return
     */
    List<ExecuteEquipmentVO> getExecuteComponentEquipmentList(ExecuteEquipmentQueryDTO dto);

    /**
     * 根据设备编码获取设备
     * 传入组件信息 会根据下方条件过滤
     *  若组件配置工位 过滤出配置中生产计划产线的工位下所有设备
     *  若组件未配置工位 过滤出生产计划产线下的所有设备
     * 未传入组件信息 直接查询
     * @return
     */
    ExecuteEquipmentVO getEquipmentByCode(ExecuteEquipmentCodeQueryDTO dto);

}
