package com.bmos.mes.service.facotry.service;

import com.bmos.mes.service.facotry.controller.vo.*;
import com.bmos.mes.service.facotry.service.data.PlanComponentRoomDTO;
import com.bmos.mes.service.facotry.service.dto.*;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface FactoryService {

    /**
     * 根据房间id获取房间信息
     * @param roomId
     * @return
     */
    FactoryRoomInfoVO getRoomInfo(Long roomId);

    /**
     * 根据房间id进行房间清场操作
     * 若传递的组件id中的组件类型为清场检测组件，则对清场组件表单中的值进行修改
     * 若传递的组件id中的组件类型为清场执行组件、则对清场执组件中的值进行填充
     * @param dto
     */
    void cleanRoom(FactoryRoomCleanDTO dto);

    /**
     * 房间清场检测组件值填充
     * @param dto
     */
    void saveRoomCleanCheckComponent(RoomCleanCheckSaveDTO dto);

    /**
     * 房间清场信息组件值填充
     * @param dto
     */
    void saveRoomCleanInfoComponent(RoomCleanInfoSaveDTO dto);

    /**
     * 获取产线列表
     *
     * @return
     */
    List<FactoryLineModuleTreeVO> getFactoryLine();

    /**
     * 获取产线下的房间列表
     * @param lineId
     * @return
     */
    List<FactoryRoomVO> getLineRoom(List<Long> lineId);

    /**
     * 获取房间列表
     *
     * @param dto
     * @return
     */
    CommonPage<RoomMobilePageVO> getRoomMobilePage(RoomMobilePageDTO dto);

    /**
     * 改变房间状态
     * @param dto
     */
    void operateRoomStatus(ChangeRoomStatusDTO dto);

    /**
     * 根据房间id获取房间信息
     *
     * @param id
     * @return
     */
    RoomInfoMobileVO getMobileRoomInfo(Long id);

    List<FactoryLineInfoVO> getFactoryLineByProcessVersionId(Long processVersionId);

    List<FactoryLineInfoVO> getFactoryLineByProcessVersion(Long processId, String version);

    /**
     * 查询生产计划所使用的工艺中工序内组件上所绑定的房间信息(剔除不属于生产计划中所配置的产线id)
     * 若工序绑定房间 过滤当前生产计划产线下的工序绑定的房间
     * @param dto
     * @return
     */
    List<RoomInfoMobileVO> planStepComponentRoomList(PlanComponentRoomDTO dto);

    /**
     * 清场相关组件扫描二维码时获取房间详情
     * @param dto
     * @return
     */
    RoomInfoMobileVO getMobileComponentRoomInfo(CleanExecuteRoomInfoDTO dto);

    /**
     * 【清场人、QA人员】数据选项需根据房间的数据权限以及权限码进行过滤
     * @param dto
     * @return
     */
    List<FactoryRoomAuthUserVO> getRoomAuthUser(FactoryRoomAuthUserDTO dto);

    /**
     * 根据房间code获取房间信息
     * @param code
     * @return
     */
    RoomInfoMobileVO getMobileRoomInfoByCode(String code);

    List<FactoryLineModuleTreeVO> getFactoryProcessLine(Long processVersionId);

    List<FactoryRoomVO> getProcessLineRoom(List<Long> lineIds, Long procedureModelId);
}
