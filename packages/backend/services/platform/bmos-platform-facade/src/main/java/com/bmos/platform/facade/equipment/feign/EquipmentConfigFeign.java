package com.bmos.platform.facade.equipment.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.equipment.dto.EquipmentApplyHeartDTO;
import com.bmos.platform.facade.equipment.dto.EquipmentQueryDTO;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentModuleTreeNodeFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * 平台设备相关接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 09:39
 */
@FeignClient(name = "bmos-platform-service", contextId = "platform-equipment")
public interface EquipmentConfigFeign {

    /**
     * 根据设备id获取设备配置信息
     *
     * @param equipmentId
     * @return
     */
    @GetMapping("/api/app/platform/equipment/feign/getConfigByEquipmentId")
    ResponseInfo<EquipmentInfoFeignVO> getConfigByEquipmentId(@RequestParam(value = "equipmentId") @NotNull Long equipmentId);

    /**
     * 根据工位id获取工位下所有设备信息
     *
     * @param stationId
     * @return
     */
    @GetMapping("/api/app/platform/equipment/feign/getConfigByStationId")
    ResponseInfo<List<EquipmentInfoFeignVO>> getConfigByStationId(@RequestParam(value = "stationId") @NotNull Long stationId);

    /**
     * 根据产线id获取其下所有设备信息
     * 带权限
     * @param productionLineId
     * @return
     */
    @GetMapping("/api/app/platform/equipment/feign/getConfigByProductionLineId")
    ResponseInfo<List<EquipmentInfoFeignVO>> getConfigByProductionLineId(@RequestParam(value = "productionLineId") @NotNull Long productionLineId);

    /**
     * @param productionLineId
     * 根据产线id获取其下所有设备信息
     * 不带权限
     * @return
     */
    @GetMapping("/api/app/platform/equipment/feign/getConfigByProductionLineIdWithNoPermission")
    ResponseInfo<List<EquipmentInfoFeignVO>> getConfigByProductionLineIdWithNoPermission(@RequestParam(value = "productionLineId") @NotNull Long productionLineId);

    /**
     * 根据工位id列表获取工位下所有设备信息
     * 当前接口不再进行维护，未来废弃掉，统一根据新接口/api/app/platform/equipment/feign/getEquipmentByParam进行统一查询
     *
     * @param stationIdList
     * @return
     */
    @GetMapping("/api/app/platform/equipment/feign/getConfigByStationIdList")
    @Deprecated
    ResponseInfo<List<EquipmentInfoFeignVO>> getConfigByStationIdList(@RequestParam(value = "stationIdList") List<Long> stationIdList);

    /**
     * 根据设备标签查询设备列表信息
     * 当前接口不再进行维护，未来废弃掉，统一根据新接口/api/app/platform/equipment/feign/getEquipmentByParam进行统一查询
     *
     * @param tagCode
     * @return
     */
    @GetMapping("/api/app/platform/equipment/feign/getEquipmentByTagCode")
    @Deprecated
    ResponseInfo<List<EquipmentInfoFeignVO>> getEquipmentByTagCode(@RequestParam(value = "tagCode") @NotBlank String tagCode);

    /**
     * 根据设备编码获取设备配置信息
     *
     * @param equipmentCode
     * @return
     */
    @GetMapping("/api/app/platform/equipment/feign/getConfigByEquipmentCode")
    ResponseInfo<EquipmentInfoFeignVO> getEquipmentByEquipmentCode(@RequestParam(value = "equipmentCode") String equipmentCode);

    /**
     * 根据设备编码获取设备配置信息
     *
     * @param equipmentCode
     * @return
     */
    @GetMapping("/api/app/platform/equipment/feign/getConfigByEquipmentCodeWithoutPermission")
    ResponseInfo<EquipmentInfoFeignVO> getEquipmentByEquipmentCodeWithoutPermission(@RequestParam(value = "equipmentCode") String equipmentCode);

    /**
     * 设备占用心跳保持
     *
     * @param equipmentApplyHeartDTO
     * @return
     */
    @PostMapping("/api/app/platform/equipment/feign/applyEquipmentHeart")
    ResponseInfo<Void> applyEquipmentHeart(@RequestBody EquipmentApplyHeartDTO equipmentApplyHeartDTO);

    /**
     * 根据参数查询该参数下的所有设备
     *
     * @param queryDTO
     * @return
     */
    @PostMapping("/api/app/platform/equipment/feign/getEquipmentByParam")
    ResponseInfo<List<EquipmentInfoFeignVO>> getConfigByStationIdList(@RequestBody EquipmentQueryDTO queryDTO);

    /**
     * 根据参数查询该参数下的所有设备
     *
     * @param tagCode
     * @return
     */
    @PostMapping("/api/app/platform/equipment/feign/getEquipmentConfigByTagCode")
    ResponseInfo<List<EquipmentInfoFeignVO>> getEquipmentConfigByTagCode(@RequestParam("tagCode") String tagCode);

    /**
     * 获取设备模型书 包含设备
     *
     * @return
     */
    @GetMapping("/api/app/platform/equipment/feign/tree")
    ResponseInfo<List<EquipmentModuleTreeNodeFeignVO>> getEquipmentFeignTree();


    /**
     * 根据设备id集合查询设备信息
     *
     * @param equipmentIdList
     * @return
     */
    @GetMapping("/api/app/platform/equipment/feign/list")
    ResponseInfo<List<EquipmentInfoFeignVO>> selectEquipmentByIdList(@RequestParam("idList") Collection<Long> equipmentIdList);

    /**
     * 设备占用
     *
     * @param equipmentId 设备id
     * @return
     */
    @PostMapping("/api/app/platform/equipment/feign/applyEquipment")
    ResponseInfo<Void> applyEquipment(@RequestParam("equipmentId") Long equipmentId);

    /**
     * 根据设备id查询删除或者停用的设备信息
     * @param equipmentIdList
     * @return
     */
    @GetMapping("/api/app/platform/equipment/feign/get/delete/equipment")
    ResponseInfo<List<EquipmentVO>> getDeleteEquipment(@RequestParam("equipmentIdList") List<Long> equipmentIdList);
}
