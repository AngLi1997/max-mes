package com.bmos.platform.facade.factory.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.factory.dto.MobileChangeRoomStatusFeignDTO;
import com.bmos.platform.facade.factory.dto.RoomMobilePageFeignDTO;
import com.bmos.platform.facade.factory.vo.RoomInfoMobileFeignVO;
import com.bmos.platform.facade.factory.vo.RoomMobilePageFeignVO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * app端feign
 */
@FeignClient(name = "bmos-platform-service", contextId = "platform-factory-app")
public interface FactoryAppFeign {

    /**
     * 移动端获取房间列表
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/feign/factory/room/page")
    @ApiOperation("(移动端)获取房间列表")
    ResponseInfo<CommonPage<RoomMobilePageFeignVO>> getRoomMobilePage(@RequestBody RoomMobilePageFeignDTO dto);

    /**
     * 移动端房间状态变更
     * @param dto
     * @return
     */
    @PutMapping("/api/app/platform/feign/factory/room/status")
    @ApiOperation("房间状态变更")
    ResponseInfo<Void> operateRoomStatus(@RequestBody MobileChangeRoomStatusFeignDTO dto);

    /**
     * 移动端 获取房间详情
     * @param id
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/room/info")
    @ApiOperation("获取房间详情")
    ResponseInfo<RoomInfoMobileFeignVO> getMobileRoomInfo(@RequestParam("id") @NotNull Long id);

    /**
     * 移动端 获取房间详情
     * @param code
     * @return
     */
    @GetMapping("/api/app/platform/feign/factory/room/infoByCode")
    @ApiOperation("获取房间详情")
    ResponseInfo<RoomInfoMobileFeignVO> getMobileRoomInfoByCode(@RequestParam("code") @NotNull String code);
}
