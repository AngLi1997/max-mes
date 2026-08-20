package com.bmos.platform.service.equipment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import com.bmos.platform.service.equipment.controller.vo.TagPropertyVO;
import com.bmos.platform.service.equipment.controller.vo.TagVO;
import com.bmos.platform.service.equipment.model.EquipmentInfo;
import com.bmos.platform.service.equipment.model.EquipmentTag;
import com.bmos.platform.service.equipment.service.data.EquipmentTagData;
import com.bmos.platform.service.equipment.service.dto.*;
import com.bmos.platform.service.tag.dto.TagInstanceDTO;

import java.util.List;
import java.util.Map;

public interface EquipmentTagService extends IService<EquipmentTag> {

    /**
     * 根据equipmentId查询设备id下所有的标签以及标签属性以及设备状态
     *
     * @param equipmentId: 设备id
     * @return
     */
    EquipmentTagData getEquipmentProperty(Long equipmentId);

    /**
     * 根据设备id查询设备标签以及标签属性
     *
     * @param equipmentIdList
     * @return map key-> 设备id value-> 设备标签以及标签属性
     */
    Map<Long, EquipmentTagData> getEquipmentTagDataByEquipmentIdList(List<Long> equipmentIdList);

    /**
     * 根据标签code查询当前标签下绑定的设备id
     *
     * @param tagCode
     * @return
     */
    List<Long> getEquipmentIdByTagCode(String tagCode);

    /**
     * 设备占用操作
     *
     * @param dto
     * @param changeType
     */
    void applyEquipment(EquipmentApplyOperateDTO dto, EquipmentStatusLogChangeType changeType);

    /**
     * 设备释放操作
     *
     * @param dto
     * @param changeType
     */
    void releaseEquipment(EquipmentOperateDTO dto, EquipmentStatusLogChangeType changeType);

    /**
     * 设备故障操作
     *
     * @param dto
     */
    void faultEquipment(EquipmentOperateDTO dto);

    /**
     * 设备属性状态变更
     *
     * @param dto
     */
    void operateEquipmentProperty(EquipmentPropertyOperateDTO dto);

    /**
     * 设备恢复操作
     *
     * @param dto
     */
    void recoverEquipment(EquipmentOperateDTO dto);

    /**
     * 删除设备与标签之间的绑定关系
     *
     * @param equipmentId
     */
    void deleteByEquipmentId(Long equipmentId);

    /**
     * 计算设备状态
     *
     * @param equipmentInfo
     */
    void analyseEquipmentStatus(EquipmentInfo equipmentInfo);


    /**
     * 添加设备类型
     *
     * @param equipmentTagDTO 设备类型
     */
    void add(EquipmentTagDTO equipmentTagDTO);


    /**
     * 编辑设备类型
     *
     * @param equipmentTagDTO 设备类型DTO
     */
    void modify(EquipmentTagDTO equipmentTagDTO);

    /**
     * 删除设备类型
     *
     * @param id id
     */
    void delete(Long id);

    /**
     * 查询设备类型所有数据
     *
     * @return 查询结果
     */
    List<EquipmentTagDTO> listWithPropertyAndUseTemplate();

    /**
     * 查询子级节点
     *
     * @param tagId 当前节点id
     * @return 查询结果
     */
    List<EquipmentTagDTO> selectChildren(Long tagId);
}
