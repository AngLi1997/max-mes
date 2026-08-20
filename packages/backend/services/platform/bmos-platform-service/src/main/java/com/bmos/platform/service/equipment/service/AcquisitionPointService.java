package com.bmos.platform.service.equipment.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import com.bmos.platform.service.equipment.model.AcquisitionPoint;
import com.bmos.platform.service.equipment.service.dto.AcquisitionPointDTO;
import com.bmos.platform.service.equipment.service.dto.AcquisitionPointPageQueryDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 采集点数据表(AcquisitionPoint)表服务接口
 *
 * @author makejava
 * @since 2024-04-19 15:17:52
 */
public interface AcquisitionPointService extends IService<AcquisitionPoint> {

    /**
     * 通过ID查询单条数据
     *
     * @return 实例对象
     */
    AcquisitionPoint queryById(Long id);

    /**
     * 新增数据
     *
     * @param acquisitionPointDTO 实例对象
     */
    void add(AcquisitionPointDTO acquisitionPointDTO);

    /**
     * 修改数据
     *
     * @param acquisitionPoint 实例对象
     */
    void update(AcquisitionPointDTO acquisitionPoint);

    /**
     * 通过主键删除数据
     *
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 通过id批量删除
     *
     * @param ids id集合
     */
    void deleteByIds(List<Long> ids);

    /**
     * 分页查询
     *
     * @param acquisitionPointPageQueryDTO 查询条件
     * @return 查询结果
     */
    CommonPage<AcquisitionPointDTO> page(AcquisitionPointPageQueryDTO acquisitionPointPageQueryDTO);

    /**
     * 启用
     *
     * @param ids
     */
    void enable(List<Long> ids);

    /**
     * 停用
     *
     * @param ids
     */
    void disable(List<Long> ids);

    /**
     * 查询采集点列表
     *
     * @param acquisitionIds 采集点id
     * @return 查询结果
     */
    List<AcquisitionPointDTO> getList(List<Long> acquisitionIds);

    List<AcquisitionPointDTO> listAcquisition();

    /**
     * 采集点关联设备数据
     *
     * @param acquisitionPointList 采集点id列表
     * @param equipmentTagDataCode 设备数据code
     */
    void bindEquipmentData(List<Long> acquisitionPointList, String equipmentTagDataCode);

    /**
     * 通过特定设备数据code集合查询可用的采集点
     *
     * @param codeSet 设备数据code集合
     * @param acquisitionPlatform 数采平台
     * @return 查询结果
     */
    List<AcquisitionPointDTO> listEnableByEquipmentDataProperty(Set<String> codeSet, AcquisitionPlatformEnum acquisitionPlatform);

    /**
     * 绑定设备数据
     * @param dataPointEquipmentDataMap 采集点id、设备数据code
     */
    void bindEquipmentData(Map<Long, String> dataPointEquipmentDataMap);
}
