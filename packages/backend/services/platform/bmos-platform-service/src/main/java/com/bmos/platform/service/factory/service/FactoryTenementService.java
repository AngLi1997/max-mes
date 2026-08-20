package com.bmos.platform.service.factory.service;

import com.bmos.platform.service.equipment.service.dto.EquipmentTagDTO;
import com.bmos.platform.service.factory.service.dto.FactoryTenementDTO;

import java.util.List;

/**
 * 楼宇(BpFactoryTenement)表服务接口
 *
 * @author makejava
 * @since 2024-12-30 11:54:58
 */
public interface FactoryTenementService {


    /**
     * 添加楼宇信息
     *
     * @param factoryTenementDTO
     */
    void add(FactoryTenementDTO factoryTenementDTO);

    /**
     * 修改楼宇信息
     * @param factoryTenementDTO
     */
    void update(FactoryTenementDTO factoryTenementDTO);

    /**
     * 删除楼栋
     * @param id
     */
    void delete(Long id);

    /**
     * 查询楼栋列表
     * @return
     */
    List<FactoryTenementDTO> listAll();

}
