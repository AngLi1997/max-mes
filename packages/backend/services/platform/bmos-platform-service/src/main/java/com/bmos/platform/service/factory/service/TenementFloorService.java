package com.bmos.platform.service.factory.service;

import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.service.factory.service.dto.FactoryTenementFloorDTO;
import com.bmos.platform.service.factory.service.dto.FactoryTenementListQueryDTO;
import com.bmos.platform.service.factory.service.dto.TenementFloorEquipmentStatisticsDTO;

import java.util.List;

/**
 * 楼宇楼层表(BpTenementFloor)表服务接口
 *
 * @author makejava
 * @since 2024-12-30 14:09:26
 */
public interface TenementFloorService {

    /**
     * 添加楼层
     *
     * @param factoryTenementFloorDTO 楼层信息
     */
    void add(FactoryTenementFloorDTO factoryTenementFloorDTO);

    /**
     * 分页查询
     *
     * @param tenementFloorDTO
     * @param page
     * @return
     */
    List<FactoryTenementFloorDTO> queryByPage(FactoryTenementFloorDTO tenementFloorDTO, BasePage page);

    /**
     * 修改楼层
     *
     * @param tenementFloorDTO
     */
    void update(FactoryTenementFloorDTO tenementFloorDTO);

    /**
     * 删除楼层
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 启用/停用楼层
     *
     * @param id 主键id
     */
    void enable(Long id);

    /**
     * 查询楼栋楼层列表
     *
     * @param listQueryDTO 查询条件
     * @return
     */
    List<FactoryTenementFloorDTO> list(FactoryTenementListQueryDTO listQueryDTO);
}
