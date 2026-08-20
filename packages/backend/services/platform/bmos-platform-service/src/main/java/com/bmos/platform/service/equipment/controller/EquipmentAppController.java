package com.bmos.platform.service.equipment.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.beust.jcommander.internal.Lists;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import com.bmos.platform.facade.equipment.constant.EquipmentInfoPropertyConstant;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import com.bmos.platform.service.equipment.controller.vo.*;
import com.bmos.platform.service.equipment.model.AcquisitionPoint;
import com.bmos.platform.service.equipment.service.AcquisitionPointService;
import com.bmos.platform.service.equipment.service.EquipmentInfoService;
import com.bmos.platform.service.equipment.service.EquipmentTagService;
import com.bmos.platform.service.equipment.service.dto.*;
import com.bmos.platform.service.factory.service.FactoryStationService;
import com.bmos.unit.service.UnitCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipment/app")
@Validated
@Api(tags = "设备(APP)接口")
public class EquipmentAppController {

    @Autowired
    EquipmentTagService equipmentTagService;

    @Autowired
    FactoryStationService factoryStationService;

    @Autowired
    private EquipmentInfoService infoService;

    @Autowired
    private AcquisitionPointService acquisitionPointService;

    @Resource
    private UnitCache unitCache;

    @GetMapping("/all/station")
    @ApiOperation("获取当前设备绑定的所有工位基础信息")
    public ResponseInfo<List<EquipmentAppStationVO>> getAllStationByEquipmentId(EquipmentAppStationDTO stationDTO) {
        return ResponseInfo.success(factoryStationService.getAllStationByEquipmentId(stationDTO));
    }

    @GetMapping("/page")
    @ApiOperation("获取当前人员绑定的工位下的所有设备")
    public ResponseInfo<CommonPage<EquipmentAppPageVO>> getEquipmentAppList(EquipmentAppPageDTO dto) {
        return ResponseInfo.success(infoService.getEquipmentAppList(dto));
    }

    @GetMapping("/byLinePage")
    @ApiOperation("根据产线id获取设备分页")
    public ResponseInfo<CommonPage<EquipmentInfoVO>> getEquipmentPageByLineId(@Validated EquipmentPageByLineDTO dto) {
        return ResponseInfo.success(infoService.getEquipmentPageByLineId(dto));
    }

    @GetMapping("/station/page")
    @ApiOperation("获取当前工位下的设备分页信息")
    public ResponseInfo<CommonPage<EquipmentAppPageVO>> getEquipmentInfoByStationId(@Validated EquipmentStationPageDTO stationPageDTO) {
        return ResponseInfo.success(infoService.getEquipmentInfoByStationId(stationPageDTO));
    }

    @GetMapping("/info/{id}")
    @ApiOperation("根据设备id获取设备状态信息")
    public ResponseInfo<EquipmentAppInfoVO> equipmentAppInfo(@PathVariable("id") @NotNull @ApiParam("设备id") Long id) {
        EquipmentAppInfoVO equipmentAppInfoVO = infoService.equipmentAppInfo(id);
        if (equipmentAppInfoVO != null) {
            wrapperAcquisitionPointInfo(Lists.newArrayList(equipmentAppInfoVO));
            handleInfoPropertyList(equipmentAppInfoVO.getInfoPropertyList());
        }
        return ResponseInfo.success(equipmentAppInfoVO);
    }

    private void handleInfoPropertyList(List<EquipmentPropertyVO> infoPropertyList) {
        if (CollUtil.isEmpty(infoPropertyList)) {
            return;
        }
        infoPropertyList.forEach(info -> {
            if (EquipmentInfoPropertyConstant.UNIT_CODE_LIST.contains(info.getCode()) && NumberUtil.isLong(info.getValue())) {
                info.setShowValue(unitCache.getGlobalUnitName(Long.valueOf(info.getValue())));
            }
        });
    }

    private void wrapperAcquisitionPointInfo(List<EquipmentAppInfoVO> equipmentInfos) {
        List<EquipmentPropertyAcquisitionPointVO> propertyAcquisitionVOS =
                equipmentInfos.stream().map(EquipmentAppInfoVO::getDataPropertyList)
                        .filter(dataPropertyList -> !CollectionUtils.isEmpty(dataPropertyList)).flatMap(Collection::stream).collect(Collectors.toList());
        Set<Long> acquisitionPointId =
                propertyAcquisitionVOS.stream().filter(equipmentPropertyAcquisitionVO -> StringUtils.isNotEmpty(equipmentPropertyAcquisitionVO.getValue()))
                        .map(equipmentPropertyAcquisitionVO -> Long.valueOf(equipmentPropertyAcquisitionVO.getValue())).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(acquisitionPointId)) {
            return;
        }
        List<AcquisitionPoint> acquisitionPoints = acquisitionPointService.listByIds(acquisitionPointId);
        for (EquipmentAppInfoVO equipmentInfo : equipmentInfos) {
            List<EquipmentPropertyAcquisitionPointVO> dataPropertyList = equipmentInfo.getDataPropertyList();
            if (CollectionUtils.isEmpty(dataPropertyList)) {
                continue;
            }
            for (EquipmentPropertyAcquisitionPointVO dataProperty : dataPropertyList) {
                if (StringUtils.isEmpty(dataProperty.getValue())) {
                    continue;
                }
                Long acquisitionPointId1 = Long.valueOf(dataProperty.getValue());
                AcquisitionPoint acquisitionPoint =
                        acquisitionPoints.stream().filter(acquisitionPoint1 -> acquisitionPoint1.getId().equals(acquisitionPointId1)).findFirst().orElse(null);
                if (acquisitionPoint != null) {
                    dataProperty.setDataPointName(acquisitionPoint.getDataPointName());
                    dataProperty.setAcquisitionPointName(acquisitionPoint.getName());
                    dataProperty.setAcquisitionPointCode(acquisitionPoint.getCode());
                    dataProperty.setAcquisitionPointDataType(acquisitionPoint.getDataType());
                }
            }
        }
    }


    @PutMapping("/operate/property")
    @ApiOperation("设备属性状态变更")
    @OperationLog
    public ResponseInfo<Void> operateEquipmentProperty(@RequestBody EquipmentPropertyOperateDTO dto) {
        equipmentTagService.operateEquipmentProperty(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/fault")
    @ApiOperation("设备故障操作")
    @OperationLog
    public ResponseInfo<Void> faultEquipment(@RequestBody EquipmentOperateDTO dto) {
        equipmentTagService.faultEquipment(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/apply")
    @ApiOperation("设备占用操作")
    @OperationLog
    public ResponseInfo<Void> applyEquipment(@RequestBody EquipmentApplyOperateDTO dto) {
        equipmentTagService.applyEquipment(dto, EquipmentStatusLogChangeType.MANUAL);
        return ResponseInfo.success();
    }

    @PutMapping("/release")
    @ApiOperation("设备释放操作")
    @OperationLog
    public ResponseInfo<Void> releaseEquipment(@RequestBody EquipmentOperateDTO dto) {
        equipmentTagService.releaseEquipment(dto, EquipmentStatusLogChangeType.MANUAL);
        return ResponseInfo.success();
    }

    @PutMapping("/recover")
    @ApiOperation("设备恢复操作")
    @OperationLog
    public ResponseInfo<Void> recoverEquipment(@RequestBody EquipmentOperateDTO dto) {
        equipmentTagService.recoverEquipment(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/list/equipment/info")
    @ApiOperation("根据比标签code查询打印机设备")
    public ResponseInfo<List<AppEquipmentInfoVO>> listEquipmentInfo() {
        return ResponseInfo.success(infoService.listEquipmentInfo());
    }

    @GetMapping("/list/equipment/property")
    @ApiOperation("根据工位id获取设备属性")
    public ResponseInfo<List<EquipmentPropertyAppVO>> listEquipmentProperty(@Validated @NotBlank String stationId) {
        return ResponseInfo.success(infoService.listEquipmentProperty(stationId));
    }

    @GetMapping("/query/equipment/id")
    @ApiOperation("根据设备code获取设备id")
    public ResponseInfo<Long> queryEquipmentIdByEquipmentCode(@Validated @NotBlank String equipmentCode) {
        return ResponseInfo.success(infoService.queryEquipmentIdByEquipmentCode(equipmentCode));
    }


    @ApiOperation("根据产线id获取设备配置")
    @GetMapping("/getConfigByProductionLineId")
    public ResponseInfo<List<EquipmentInfoVO>> getConfigByProductionLineId(Long productionLineId) {
        return ResponseInfo.success(infoService.getConfigByProductionLineId(productionLineId));
    }

    @PostMapping("/station/list")
    @ApiOperation("获取当前工位下的设备列表")
    public ResponseInfo<List<EquipmentInfoVO>> getConfigByStationId(@RequestBody List<Long> stationList) {
        return ResponseInfo.success(infoService.getConfigByStationIdList(stationList));
    }

    @ApiOperation("根据产线id和工位id获取设备配置")
    @GetMapping("/{productionLineId}/getConfigByStationIdList")
    public ResponseInfo<List<EquipmentInfoVO>> getConfigByProductionIdStationIdList(@PathVariable("productionLineId") Long productionLineId, @RequestParam(value = "stationIdList") List<Long> stationIdList) {
        return ResponseInfo.success(infoService.getConfigByByProductionIdStationIdList(productionLineId,stationIdList));
    }
}
