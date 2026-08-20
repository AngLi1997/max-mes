package com.bmos.platform.service.equipment.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import com.bmos.platform.facade.equipment.dto.EquipmentApplyHeartDTO;
import com.bmos.platform.facade.equipment.dto.EquipmentQueryDTO;
import com.bmos.platform.facade.equipment.vo.EquipmentVO;
import com.bmos.platform.service.equipment.controller.vo.*;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import com.bmos.platform.service.equipment.model.EquipmentInfo;
import com.bmos.platform.service.equipment.service.dto.*;

import java.util.Collection;
import java.util.List;

public interface EquipmentInfoService {

    /**
     * 新建设备
     *
     * @param dto
     */
    Long saveEquipment(EquipmentSaveDTO dto);

    /**
     * 编辑设备
     *
     * @param dto
     */
    void updateEquipment(EquipmentUpdateDTO dto);

    /**
     * 删除设备
     *
     * @param id
     */
    void deleteEquipment(Long id);

    /**
     * 获取设备详情
     *
     * @param id
     * @return
     */
    EquipmentInfoVO getEquipmentInfo(Long id);

    /**
     * 获取设备列表
     *
     * @param dto
     * @return
     */
    CommonPage<EquipmentInfoVO> getEquipmentPage(EquipmentPageDTO dto);

    /**
     * 启停设备
     *
     * @param dto
     * @return
     */
    void enableEquipment(EquipmentEnableDTO dto);

    List<EquipmentInfo> queryInfoListByCategoryIdAndEnable(Long id, Boolean enable);

    /**
     * 打印设备标签信息
     *
     * @param dto
     * @return
     */
    EquipmentPrintInfoVO printEquipmentTagInfo(EquipmentPrintTagDTO dto);

    CommonPage<EquipmentAppPageVO> getEquipmentAppList(EquipmentAppPageDTO dto);

    EquipmentAppInfoVO equipmentAppInfo(Long id);

    EquipmentInfoVO getConfigByEquipmentId(Long equipmentId);

    List<EquipmentInfoVO> getConfigByStationId(Long stationId);

    List<EquipmentInfoVO> getEquipmentByTagCode(String tagCode);

    EquipmentInfoVO getEquipmentByEquipmentCode(String equipmentCode);

    List<AppEquipmentInfoVO> listEquipmentInfo();

    /**
     * 根据站点id获取设备列表
     *
     * @param stationPageDTO
     * @return
     */
    CommonPage<EquipmentAppPageVO> getEquipmentInfoByStationId(EquipmentStationPageDTO stationPageDTO);

    List<EquipmentPropertyAppVO> listEquipmentProperty(String stationId);

    /**
     * 设备占用心跳
     *
     * @param equipmentApplyHeartDTO
     */
    void applyEquipmentHeart(EquipmentApplyHeartDTO equipmentApplyHeartDTO);

    /**
     * 设备占用
     *
     * @param equipmentId 设备id
     */
    void applyEquipment(Long equipmentId);

    Long queryEquipmentIdByEquipmentCode(String equipmentCode);

    List<EquipmentInfoVO> getConfigByStationIdList(List<Long> stationIdList);

    List<EquipmentInfoVO> getConfigByProductionLineId(Long productionLineId);

    /**
     * 根据参数查询参数下的设备信息
     *
     * @param queryDTO
     * @return
     */
    List<EquipmentInfoVO> getEquipmentByParam(EquipmentQueryDTO queryDTO);

    /**
     * 获取打印设备
     *
     * @return
     */
    List<EquipmentPrintVO> getPrintEquipment();

    /**
     * 计算设备状态 并进行保存 并记录操作日志
     *
     * @param equipmentId
     * @param changeType
     */
    void analyseEquipmentStatus(Long equipmentId, EquipmentStatusLogChangeType changeType);

    /**
     * 根据设备id集合查询设备信息
     *
     * @param equipmentIdList
     * @return
     */
    List<EquipmentInfoVO> selectEquipmentByIdList(Collection<Long> equipmentIdList);

    /**
     * 绑定采集点
     *  @param equipmentId              设备id
     * @param acquisitionPlatform       数采平台
     * @param equipmentPropertyDTOList 点位数据
     */
    void bindDataPropertyAcquisitionPoint(Long equipmentId,  AcquisitionPlatformEnum acquisitionPlatform, List<EquipmentPropertyDTO> equipmentPropertyDTOList);

    /**
     * 获取设备的使用日志模板
     *
     * @param equipmentId 设备id
     * @return 获取结果
     */
    List<EquipmentTagUseTemplateDTO> getUseLogTemplate(Long equipmentId);

    /**
     * 根据产线id和工位id查询设备列表
     *
     * @param productionLineId 产线id
     * @param stationIdList    工位id
     * @return 查询结果
     */
    List<EquipmentInfoVO> getConfigByByProductionIdStationIdList(Long productionLineId, List<Long> stationIdList);

    List<EquipmentVO> getDeleteEquipment(List<Long> equipmentIdList);

    /**
     * 根据产线id查询设备列表
     * 不带权限
     * @param productionLineId
     * @return
     */
    List<EquipmentInfoVO> getConfigByProductionLineIdWithNoPermission(Long productionLineId);

    /**
     * 根据产线id查询设备分页
     * @param dto
     * @return
     */
    CommonPage<EquipmentInfoVO> getEquipmentPageByLineId(EquipmentPageByLineDTO dto);
}
