package com.bmos.platform.facade.factory.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.equipment.dto.UpdateStationDTO;
import com.bmos.platform.facade.equipment.vo.EquipmentFeignStationVO;
import com.bmos.platform.facade.factory.dto.BatchRoomCleanPageDTO;
import com.bmos.platform.facade.factory.dto.ChangeRoomStatusFeignDTO;
import com.bmos.platform.facade.factory.dto.LineUseDTO;
import com.bmos.platform.facade.factory.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * 房间feign接口
 */
@FeignClient(name = "bmos-platform-service", contextId = "platform-factory")
public interface FactoryFeign {

    /**
     * 根据工位id查询工位信息
     * @param stationId 工位id
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/station/queryStationById")
    ResponseInfo<FactoryStationFeignVO> queryStationById(@RequestParam("stationId") Long stationId);

    /**
     * 根据产线id集合查询工位信息
     * @param productLineIds 产线id集合
     * @return 工位信息列表
     */
    @GetMapping("/api/app/platform/feign/factory/station/queryStationListByProductLineIds")
    ResponseInfo<List<FactoryStationFeignVO>> queryStationListByProductLineIds(@RequestParam(value = "productLineIds") List<Long> productLineIds);

    /**
     * 根据条件查询产线信息
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/line/getLineByCondition")
    ResponseInfo<List<FactoryLineFeignVO>> getLineByCondition(@RequestParam(value = "name") String name);

    /**
     * 获取产线模型树VO
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/line/tree")
    ResponseInfo<List<LineModuleTreeNodeFeignVO>> getLineModuleTreeVO();

    /**
     * 查询产线的详情信息 包含产线下的房间以及工位
     *
     * @param lineIds: 产线id集合
     * @param stationFlag:是否查询工位
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/line/detail/list")
    ResponseInfo<List<FactoryLineDetailFeignVO>> getLineDetailByLineIds(@RequestParam(value = "lineIds") Collection<Long> lineIds, @RequestParam("stationFlag") boolean stationFlag);

    /**
     * 产线/房间/工位被某个业务配置绑定或解绑
     * @param dto
     * @return
     */
    @PutMapping("/api/app/platform/feign/factory/bind/config")
    ResponseInfo<Void> bindUseCount(@RequestBody LineUseDTO dto);

    /**
     * 获取房间树
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/room/tree")
    ResponseInfo<List<RoomModuleTreeNodeFeignVO>> getRoomFeignTree();

    /**
     * 根据房间id查询房间的清场信息以及房间基础信息
     *
     * @param roomId
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/room/getRoomCleanInfoByRoomId")
    ResponseInfo<RoomCleanInfoFeignVO> getRoomCleanInfoByRoomId(@RequestParam(value = "roomId") @NotNull Long roomId);

    /**
     * 查询产线下的所有房间信息
     * @param roomId
     * @param findStation 是否查询房间下的工位信息
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/room/getRoomInfoByLineId")
    ResponseInfo<List<RoomInfoFeignVO>> getRoomInfoByLineId(@RequestParam(value = "lineId") @NotNull Long roomId, @RequestParam(value = "findStation") @NotNull boolean findStation);

    /**
     * 获取产线下的所有工位信息 已去重
     * @param lineId
     * @return
     */
    @GetMapping("api/app/platform/feign/factory/station/getStationInfoByLineId")
    ResponseInfo<List<FactoryStationFeignVO>> getStationInfoByLineId(@RequestParam(value = "lineId") Long lineId);

    /**
     * 变更房间状态
     * @param dto
     * @return
     */
    @PutMapping("/api/app/platform/feign/factory/room/changeRoomStatus")
    ResponseInfo<Void> changeRoomStatus(@RequestBody ChangeRoomStatusFeignDTO dto);

    /**
     * 根据房间id集合查询工位信息
     *
     * @param roomIdList
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/room/selectByRoomIds")
    ResponseInfo<List<RoomInfoFeignVO>> selectByRoomIds(@RequestParam(value = "roomIdList") List<Long> roomIdList);

    /**
     * 分页查询某个生产批次的清场信息分页
     *
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/feign/factory/room/getRoomCleanInfoPage")
    ResponseInfo<CommonPage<BatchRoomCleanInfoVO>> getRoomCleanInfoPage(@RequestBody BatchRoomCleanPageDTO dto);

    /**
     * 根据工位id集合修改工位使用情况
     * @param dto true:绑定，false:解绑
     */
    @PostMapping("/api/app/platform/feign/factory/station/updateStationUseCount")
    ResponseInfo<Boolean> updateStationUseCount(@RequestBody UpdateStationDTO dto);

    /**
     * 根据工位id获取工位下所有用户信息
     * @param stationId
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/station/getStationUserByStationId")
    ResponseInfo<List<String>> getStationUserByStationId(@RequestParam(value = "stationId") Long stationId);

    /**
     * 根据工位id列表获取工位下所有用户信息
     * @param stationIdList
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/station/getStationUserByStationIdList")
    ResponseInfo<List<String>> getStationUserByStationIdList(@RequestParam(value = "stationIdList") List<Long> stationIdList);

    /**
     * 根据工位id列表获取工位下所有用户信息
     *
     * @param stationIdList
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/station/checkStationPermission")
    ResponseInfo<List<StationPermissionVO>> checkStationPermission(@RequestParam(value = "stationIdList") Collection<Long> stationIdList, @RequestParam(value = "userId") String userId);

    /**
     * 根据房间id查询房间下的工位信息
     * @param roomId
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/station/getStationByRoomId")
    ResponseInfo<List<EquipmentFeignStationVO>> getStationByRoomId(@RequestParam(value = "roomId") Long roomId);

    /**
     * 根据用户id查询工位id列表
     * @param userId 用户id
     * @return 工位id列表
     */
    @GetMapping("/api/app/platform/feign/factory/station/getStationIdsByUserId")
    ResponseInfo<List<String>> getStationIdsByUserId(@RequestParam("userId") String userId);

    /**
     * 根据产线id集合查询产线详情数据
     * @param lineIdList 产线id集合
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/station/queryLineDetailListByLineIds")
    ResponseInfo<List<FactoryLineDetailFeignVO>> queryLineDetailListByLineIds(@RequestParam("lineIdList") List<Long> lineIdList);

    /**
     * 根据产线id获取已删除的产线数据
     */
    @GetMapping("/api/app/platform/feign/factory/query/line/list")
    ResponseInfo<List<FactoryLineFeignVO>> queryLineListByLineIds(@RequestParam("lineIdList") List<Long> lineIdList);

    @GetMapping("/api/app/platform/feign/factory/query/room/list")
    ResponseInfo<List<FactoryRoomFeignVO>> queryRoomListByRoomIds(@RequestParam("roomIds") List<Long> roomIds);

}
