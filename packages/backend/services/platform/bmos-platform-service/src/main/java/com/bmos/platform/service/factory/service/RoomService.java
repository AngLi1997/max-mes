package com.bmos.platform.service.factory.service;

import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.factory.dto.*;
import com.bmos.platform.facade.factory.vo.*;
import com.bmos.platform.service.factory.controller.vo.*;
import com.bmos.platform.service.factory.service.dto.*;
import org.apache.kafka.common.protocol.types.Field;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

/**
 * 房间相关接口
 */
public interface RoomService {

    /**
     * 新建房间
     *
     * @param dto
     */
    void saveRoom(RoomSaveDTO dto);

    /**
     * 编辑房间
     *
     * @param dto
     */
    void updateRoom(RoomUpdateDTO dto);

    /**
     * 删除房间
     *
     * @param id
     */
    void deleteRoom(Long id);

    /**
     * 启停房间
     *
     * @param dto
     */
    void enableRoom(RoomEnableDTO dto);

    /**
     * 获取房间列表
     *
     * @param dto
     * @return
     */
    CommonPage<RoomPageVO> getRoomPage(RoomPageDTO dto);

    /**
     * 获取房间详情
     *
     * @param id
     * @return
     */
    RoomInfoVO getRoomInfo(Long id);

    /**
     * (移动端)获取房间列表
     *
     * @param dto
     * @return
     */
    CommonPage<RoomMobilePageFeignVO> getRoomMobilePage(RoomMobilePageFeignDTO dto);

    /**
     * 房间状态变更
     *
     * @param dto
     */
    void operateRoomStatus(MobileChangeRoomStatusFeignDTO dto);

    /**
     * 获取设备详情
     *
     * @param id
     * @return
     */
    RoomInfoMobileFeignVO getMobileRoomInfo(Long id);

    /**
     * Feign接口根据roomId获取房间详情
     *
     * @param roomId
     * @return
     */
    RoomCleanInfoFeignVO getRoomCleanInfoByRoomId(Long roomId);

    /**
     * feign接口变更房间状态
     *
     * @param dto
     */
    void changeRoomStatus(ChangeRoomStatusFeignDTO dto);

    /**
     * 房间绑定工位
     *
     * @param dto
     */
    void bindStation(RoomBindStationDTO dto);

    /**
     * 获取房间树 包含房间信息
     *
     * @return
     */
    List<RoomTreeNodeVO> getRoomTree();

    /**
     * 根据产线id查询产线下的所有房间信息
     *
     * @param lineId
     * @param findStation: 是否查询房间下的工位信息
     * @return
     */
    List<RoomInfoFeignVO> getRoomInfoByLineId(Long lineId, boolean findStation);

    /**
     * 根据房间id集合查询房间信息 包含房间下的工位信息
     *
     * @param roomIdList
     * @return
     */
    List<RoomInfoFeignVO> selectByRoomIds(List<Long> roomIdList);

    /**
     * 打印房间信息
     *
     * @param roomId
     * @return
     */
    RoomPrintVO printRoom(Long roomId);

    /**
     * 获取房间树
     *
     * @return
     */
    List<RoomModuleTreeNodeFeignVO> getRoomFeignTree();

    /**
     * 根据房间code获取房间详情
     *
     * @param code
     * @return
     */
    RoomInfoMobileFeignVO getMobileRoomInfoByCode(String code);

    List<FactoryRoomFeignVO> queryRoomListByRoomIds(List<Long> roomIds);

    /**
     * 添加房间环境配置
     *
     * @param roomEnvPropertyDTO 黄静配置dto
     */
    void addRoomEnvProperty(List<RoomEnvPropertyDTO> roomEnvPropertyDTO);


    /**
     * 查询房间列表
     *
     * @param roomListQueryDTO 查询条件
     * @param page             分页条件
     * @return 查询结果
     */
    CommonPage<FactoryRoomDTO> page(RoomListQueryDTO roomListQueryDTO, BasePage page);

    /**
     * 保存3d模型id
     *
     * @param roomId
     * @param modelId 模型id
     */
    void save3DModel(Long roomId, String modelId);

    /**
     * 根据3D模型id获取房间
     *
     * @param modelId
     * @return
     */
    RoomInfoVO getBy3DModel(String modelId);

    /**
     * 查询房间列表
     *
     * @param queryDTO 查询条件
     * @return 结果
     */
    List<FactoryRoomDTO> list(RoomListQueryDTO queryDTO);




    /**
     * 获取楼层的设备统计信息
     *
     * @param id 楼层id
     * @return 统计信息结果
     */
    TenementFloorEquipmentStatisticsDTO getFloorEquipmentStatistics(Long id);

    /**
     * 根据产线id查询房间分页
     * @param dto
     * @return
     */
    CommonPage<RoomAppPageVO> getRoomPageByLineId(RoomAppPageDTO dto);
}
