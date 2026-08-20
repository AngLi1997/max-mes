package com.bmos.platform.service.factory.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.equipment.dto.UpdateStationDTO;
import com.bmos.platform.facade.equipment.vo.EquipmentFeignStationVO;
import com.bmos.platform.facade.equipment.vo.EquipmentModuleTreeNodeFeignVO;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import com.bmos.platform.facade.factory.vo.StationPermissionVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentAppStationVO;
import com.bmos.platform.service.equipment.controller.vo.StationEquipmentInfoTreeNodeVO;
import com.bmos.platform.service.factory.controller.vo.StationPageVO;
import com.bmos.platform.service.factory.controller.vo.StationTreeNodeVO;
import com.bmos.platform.service.factory.controller.vo.StationVO;
import com.bmos.platform.service.equipment.service.dto.EquipmentAppStationDTO;
import com.bmos.platform.service.factory.model.EquipmentStation;
import com.bmos.platform.service.factory.model.EquipmentStationInfo;
import com.bmos.platform.service.factory.service.dto.*;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public interface FactoryStationService {
    /**
     * 新建设备工位
     *
     * @param dto
     */
    void saveStation(StationSaveDTO dto);

    /**
     * 编辑设备工位
     *
     * @param dto
     */
    void updateStation(StationUpdateDTO dto);

    /**
     * 删除设备工位
     *
     * @param id
     */
    void deleteStation(Long id);

    /**
     * 启停设备工位
     *
     * @param dto
     */
    void enableStation(StationEnableDTO dto);

    /**
     * 获取设备工位列表
     *
     * @param dto
     * @return
     */
    CommonPage<StationPageVO> getStationPage(StationPageDTO dto);

    /**
     * 获取设备工位详情(因暂不知道具体展示那些字段需要待定)
     *
     * @param id
     * @return
     */
    StationVO getStationInfo(Long id);

    /**
     * 工位绑定设备
     *
     * @param dto
     */
    void bindEquipment(StationBindEquipmentDTO dto);

    /**
     * 工位绑定用户
     *
     * @param dto
     */
    void bindUser(StationBindUserDTO dto);

    Boolean updateStationUseCount(UpdateStationDTO dto);

    /**
     * 根据工位id获取工位下绑定的用户信息
     *
     * @param stationId
     * @return
     */
    List<String> getStationUserByStationId(Long stationId);

    List<StationEquipmentInfoTreeNodeVO> stationEquipment();

    List<String> getStationUserByStationIdList(List<Long> stationIdList);


    /**
     * 获取当前设备下绑定的所有设备工位信息
     * @param stationDTO
     * @return
     */
    List<EquipmentAppStationVO> getAllStationByEquipmentId(EquipmentAppStationDTO stationDTO);

    /**
     * 获取工位树信息 包含工位
     * @return
     */
    List<StationTreeNodeVO> stationTree();

    /**
     * 根据产线id集合查询工位信息
     * @param productLineIds
     * @return
     */
    List<FactoryStationFeignVO> queryStationListByLineIds(List<Long> productLineIds);

    /**
     * 检测当前人员是否有此工位权限
     *
     * @param stationIdList
     * @param userId
     * @return
     */
    List<StationPermissionVO> checkStationPermission(Collection<Long> stationIdList, String userId);

    /**
     * 获取产线下所有工位信息
     * @param lineId
     * @return
     */
    List<FactoryStationFeignVO> getStationInfoByLineId(Long lineId);

    /**
     * 根据房间获取工位信息
     * @param roomId
     * @return
     */
    List<EquipmentFeignStationVO> getStationByRoomId(Long roomId);

    /**
     * 获取设备模型书 包含设备信息
     *
     * @return
     */
    List<EquipmentModuleTreeNodeFeignVO> getEquipmentFeignTree();

    /**
     * 根据人员id获取工位id集合
     * @param userId 用户id
     * @return
     */
    List<String> getStationIdsByUserId(String userId);

    /**
     * 根据工位id获取工位信息
     * @param stationId
     * @return
     */
    @Nullable
    FactoryStationFeignVO queryStationById(Long stationId);

    /**
     * 根据工位id查询工位信息
     *
     * @param stationIdList
     * @return
     */
    List<EquipmentStation> selectByIds(List<Long> stationIdList);

    /**
     * 工位绑定用户集合
     * @param dto
     */
    void userBindStations(UserBindStationsDTO dto);

    /**
     * 获取当前用户所拥有的工位id集合
     * @param userId
     * @return
     */
    List<Long> userStationList(String userId);
}
