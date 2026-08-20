package com.bmos.mes.service.storage.manage.controller;

import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.print.dto.PrintCommonDTO;
import com.bmos.mes.service.platform.print.dto.PrintCommonDTOBatchByDeviceId;
import com.bmos.mes.service.platform.print.dto.PrintCommonDTOByDeviceId;
import com.bmos.mes.service.platform.print.feign.PlatformTagClient;
import com.bmos.mes.service.storage.manage.dto.*;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.vo.MaterialPartListVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialMobileVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialVO;
import com.bmos.mes.service.tag.enums.CodeType;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.equipment.enums.TagEquipmentPropertyCodeEnum;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentPropertyFeignVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 暂存间管理 - 物料件
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/6 11:58
 */
@RestController
@RequestMapping("/storage/material")
@Validated
@Api(tags = "暂存间管理 - 物料件")
@Slf4j
public class StorageMaterialController {

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Resource
    private PlatformTagClient platformTagClient;

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @GetMapping("/page")
    @ApiOperation("分页查询物料件")
    public ResponseInfo<CommonPage<StorageMaterialVO>> queryPage(@Validated StorageMaterialPageQuery query) {
        return ResponseInfo.success(storageMaterialService.queryPage(query));
    }

    @GetMapping("/infoList")
    @ApiOperation("查询物料件列表")
    public ResponseInfo<List<StorageMaterialVO>> queryInfoList(@Validated StorageMaterialListQuery query) {
        return ResponseInfo.success(storageMaterialService.queryInfoList(query));
    }

    @GetMapping("/info")
    @ApiOperation("根据物料件id查询物料件详情")
    @ApiImplicitParam(name = "id", value = "暂存物料件id", required = true, example = "1")
    public ResponseInfo<StorageMaterialVO> queryInfoById(@RequestParam Long id) {
        return ResponseInfo.success(storageMaterialService.queryInfoById(id));
    }

    @PostMapping("/inbound")
    @ApiOperation("物料入库(web端)")
    @OperationLog
    public ResponseInfo<Void> inbound(@Validated @RequestBody StorageMaterialInboundDTO dto) {
        storageMaterialService.inbound(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/sendBack")
    @ApiOperation("物料入库(移动端(原退库接口))")
    @OperationLog
    public ResponseInfo<Void> sendBack(@Validated @RequestBody StorageMaterialSendBackDTO dto) {
        storageMaterialService.sendBack(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/outbound")
    @ApiOperation("物料出库")
    @OperationLog
    public ResponseInfo<Void> outbound(@Validated @RequestBody StorageMaterialOutboundDTO dto) {
        storageMaterialService.outbound(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/move")
    @ApiOperation("移库")
    @OperationLog
    public ResponseInfo<Void> move(@Validated @RequestBody StorageMaterialMoveDTO dto) {
        storageMaterialService.move(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/check")
    @ApiOperation("盘库")
    @OperationLog
    public ResponseInfo<Void> check(@Validated @RequestBody StorageMaterialCheckDTO dto) {
        storageMaterialService.check(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据物料批次id查询物料件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "batchNoId", value = "物料批次id", required = true),
            @ApiImplicitParam(name = "storageMaterialNo", value = "物料件号")
    })
    public ResponseInfo<List<MaterialPartListVO>> queryListByBatchId(@NotNull Long batchNoId, String storageMaterialNo){
        return ResponseInfo.success(storageMaterialService.queryListByBatchId(batchNoId, storageMaterialNo));
    }

    @GetMapping("/infoByNo")
    @ApiOperation("根据物料件编号查询物料件详情(移动端)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "materialNo", value = "物料件/设备编号", required = true),
            @ApiImplicitParam(name = "codeType", value = "material")
    })
    public ResponseInfo<StorageMaterialMobileVO> queryInfoByMaterialNo(@RequestParam String materialNo, @RequestParam(required = false) String codeType) {
        CodeType type = null;
        if(StringUtils.isNotBlank(codeType)){
            type = CommonEnum.getEnumByValue(CodeType.class, StringUtils.lowerCase(codeType));
        }
        return ResponseInfo.success(storageMaterialService.queryInfoByMaterialNo(materialNo, false, type));
    }

    @PutMapping("/outboundMobile")
    @ApiOperation("物料出库(移动端)")
    @OperationLog
    public ResponseInfo<Void> outboundMobile(@Validated @RequestBody StorageMaterialOutboundMobileDTO dto) {
        storageMaterialService.outboundMobile(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/sendBackMobile")
    @ApiOperation("物料入库(移动端)")
    @OperationLog
    public ResponseInfo<Void> sendBackMobile(@Validated @RequestBody StorageMaterialSendBackMobileDTO dto) {
        storageMaterialService.sendBackMobile(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/moveMobile")
    @ApiOperation("移库(移动端)")
    @OperationLog
    public ResponseInfo<Void> moveMobile(@Validated @RequestBody StorageMaterialMoveMobileDTO dto) {
        storageMaterialService.moveMobile(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/reserve")
    @ApiOperation("物料预定(移动端)")
    @OperationLog
    public ResponseInfo<Void> reserve(@Validated @RequestBody StorageMaterialReserveDTO dto) {
        storageMaterialService.reserve(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/cancelReserve")
    @ApiOperation("取消预定(移动端)")
    @OperationLog
    public ResponseInfo<Void> cancelReserve(@Validated @RequestBody StorageMaterialCancelReserveDTO dto) {
        storageMaterialService.cancelReserve(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/splitPackage")
    @ApiOperation("拆包出库")
    @OperationLog
    public ResponseInfo<String> splitPackage(@Validated @RequestBody StorageMaterialSplitPackageDTO dto) {
        return ResponseInfo.success(storageMaterialService.splitPackage(dto));
    }


    @PostMapping("/printStorageMaterialTag")
    @ApiOperation("打印标签")
    @OperationLog
    public ResponseInfo<Void> printStorageMaterialTag(@Validated @RequestBody PrintCommonDTOByDeviceId printDTO) {
        EquipmentInfoFeignVO printer = FeignUtils.handleRequest(deviceId -> equipmentConfigFeign.getConfigByEquipmentId(deviceId), printDTO.getDeviceId()).getData();
        if (printer == null){
            log.error("设备id:{}不存在", printDTO.getDeviceId());
        }
        String ip = "";
        String port = "";
                List<EquipmentPropertyFeignVO> propertyFeignVOList = printer.getInfoPropertyList();
        for (EquipmentPropertyFeignVO equipmentPropertyFeignVO : propertyFeignVOList) {
            if (Objects.equals(equipmentPropertyFeignVO.getCode(), TagEquipmentPropertyCodeEnum.IP_ADDRESS.getCode())){
                ip = equipmentPropertyFeignVO.getValue();
            }
            if (Objects.equals(equipmentPropertyFeignVO.getCode(), TagEquipmentPropertyCodeEnum.PORT.getCode())){
                port = equipmentPropertyFeignVO.getValue();
            }
        }
        PrintCommonDTO printCommonDTO = new PrintCommonDTO();
        printCommonDTO.setPrinterIp(ip);
        printCommonDTO.setPrinterPort(Integer.parseInt(port));
        printCommonDTO.setSceneId(printDTO.getSceneId());
        printCommonDTO.setBody(printDTO.getBody());
        return platformTagClient.printTag(printCommonDTO);
    }

    @PostMapping("/printStorageMaterialTagBatch")
    @ApiOperation("打印标签(批量)")
    @OperationLog
    public ResponseInfo<Void> printStorageMaterialTagBatch(@Validated @RequestBody PrintCommonDTOBatchByDeviceId printDTO) {
        EquipmentInfoFeignVO printer = FeignUtils.handleRequest(deviceId -> equipmentConfigFeign.getConfigByEquipmentId(deviceId), printDTO.getDeviceId()).getData();
        if (printer == null){
            log.error("设备id:{}不存在", printDTO.getDeviceId());
        }
        String ip = "";
        String port = "";
        List<EquipmentPropertyFeignVO> propertyFeignVOList = printer.getInfoPropertyList();
        for (EquipmentPropertyFeignVO equipmentPropertyFeignVO : propertyFeignVOList) {
            if (Objects.equals(equipmentPropertyFeignVO.getCode(), TagEquipmentPropertyCodeEnum.IP_ADDRESS.getCode())){
                ip = equipmentPropertyFeignVO.getValue();
            }
            if (Objects.equals(equipmentPropertyFeignVO.getCode(), TagEquipmentPropertyCodeEnum.PORT.getCode())){
                port = equipmentPropertyFeignVO.getValue();
            }
        }

        List<Map<String, Object>> bodies = printDTO.getBody();
        for (Map<String, Object> body : bodies) {
            PrintCommonDTO printCommonDTO = new PrintCommonDTO();
            printCommonDTO.setPrinterIp(ip);
            printCommonDTO.setPrinterPort(Integer.parseInt(port));
            printCommonDTO.setSceneId(printDTO.getSceneId());
            printCommonDTO.setBody(body);
            platformTagClient.printTag(printCommonDTO);
        }
        return ResponseInfo.success();
    }

    @PostMapping("/sendBackAndConsumeMobile")
    @ApiOperation("退库并消耗")
    @OperationLog
    public ResponseInfo<Void> sendBackAndConsumeMobile(@Validated @RequestBody StorageMaterialConsumeDTO dto) {
        storageMaterialService.sendBackAndConsumeMobile(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/destroyAndConsumeMobile")
    @ApiOperation("销毁并消耗")
    @OperationLog
    public ResponseInfo<Void> destroyAndConsumeMobile(@Validated @RequestBody StorageMaterialConsumeDTO dto) {
        storageMaterialService.destroyAndConsumeMobile(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/useAndConsumeMobile")
    @ApiOperation("使用并消耗")
    @OperationLog
    public ResponseInfo<Void> useAndConsumeMobile(@Validated @RequestBody StorageMaterialConsumeDTO dto) {
        storageMaterialService.useAndConsumeMobile(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/receiveMobile")
    @ApiOperation("物料接收（移动端）")
    @OperationLog
    public ResponseInfo<List<String>> receiveMobile(@Validated @RequestBody StorageMaterialReceiveMobileDTO dto) {
        return ResponseInfo.success(storageMaterialService.receiveMobile(dto));
    }

    @PostMapping("/confirm/batchReserve")
    @ApiOperation("批量预定（生产前确认）")
    public ResponseInfo<Void> planConfirmBatchReserve(@Validated @RequestBody ReserveComponentReserveMaterialDTO dto) {
        storageMaterialService.reserveComponentReserve(dto);
        return ResponseInfo.success();
    }


}
