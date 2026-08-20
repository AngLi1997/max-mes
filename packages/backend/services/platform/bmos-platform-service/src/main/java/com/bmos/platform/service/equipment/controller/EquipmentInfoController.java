package com.bmos.platform.service.equipment.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.AdminUtil;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.equipment.constant.EquipmentInfoPropertyConstant;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.factory.vo.StationPermissionVO;
import com.bmos.platform.service.equipment.controller.vo.*;
import com.bmos.platform.service.equipment.model.AcquisitionPoint;
import com.bmos.platform.service.equipment.service.AcquisitionPointService;
import com.bmos.platform.service.equipment.service.EquipmentInfoService;
import com.bmos.platform.service.equipment.service.dto.*;
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
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipment")
@Validated
@Api(tags = "设备管理接口")
public class EquipmentInfoController {

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @Autowired
    private AcquisitionPointService acquisitionPointService;

    @Resource
    private UnitCache unitCache;

    @PostMapping("/save")
    @ApiOperation("新建设备")
    @OperationLog
    @DistributedLock(expression = "#dto.code")
    public ResponseInfo<Long> saveEquipment(@RequestBody EquipmentSaveDTO dto) {
        return ResponseInfo.success(equipmentInfoService.saveEquipment(dto));
    }

    @PutMapping("/update")
    @ApiOperation("编辑设备")
    @OperationLog
    public ResponseInfo<Void> updateEquipment(@RequestBody EquipmentUpdateDTO dto) {
        equipmentInfoService.updateEquipment(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除设备")
    @OperationLog
    public ResponseInfo<Void> deleteEquipment(@PathVariable @NotNull @ApiParam("设备id") Long id) {
        equipmentInfoService.deleteEquipment(id);
        return ResponseInfo.success();
    }


    @GetMapping("/info/{id}")
    @ApiOperation("获取设备详情")
    public ResponseInfo<EquipmentInfoVO> getEquipmentInfo(@PathVariable @NotNull @ApiParam("设备id") Long id) {
        EquipmentInfoVO equipmentInfo = equipmentInfoService.getEquipmentInfo(id);
        this.wrapperAcquisitionPointInfo(Lists.newArrayList(equipmentInfo));
        handleInfoPropertyList(equipmentInfo.getInfoPropertyList());
        return ResponseInfo.success(equipmentInfo);
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

    private void wrapperAcquisitionPointInfo(List<EquipmentInfoVO> equipmentInfos) {
        List<EquipmentPropertyAcquisitionPointVO> propertyAcquisitionVOS =
                equipmentInfos.stream().map(EquipmentInfoVO::getDataPropertyList)
                        .filter(dataPropertyList -> !CollectionUtils.isEmpty(dataPropertyList)).flatMap(Collection::stream).collect(Collectors.toList());
        Set<Long> acquisitionPointId =
                propertyAcquisitionVOS.stream().filter(equipmentPropertyAcquisitionVO -> StringUtils.isNotEmpty(equipmentPropertyAcquisitionVO.getValue()))
                        .map(equipmentPropertyAcquisitionVO -> Long.valueOf(equipmentPropertyAcquisitionVO.getValue())).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(acquisitionPointId)) {
            return;
        }
        List<AcquisitionPoint> acquisitionPoints = acquisitionPointService.listByIds(acquisitionPointId);
        for (EquipmentInfoVO equipmentInfo : equipmentInfos) {
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

    @GetMapping("/page")
    @ApiOperation("获取设备列表")
    public ResponseInfo<CommonPage<EquipmentInfoVO>> getEquipmentPage(EquipmentPageDTO dto) {
        CommonPage<EquipmentInfoVO> equipmentPage = equipmentInfoService.getEquipmentPage(dto);
        if (!CollectionUtils.isEmpty(equipmentPage.getList())) {
            wrapperAcquisitionPointInfo(equipmentPage.getList());
        }
        return ResponseInfo.success(equipmentPage);
    }

    @PostMapping("/enable")
    @ApiOperation("启停设备")
    @OperationLog
    public ResponseInfo<Void> enableEquipment(@RequestBody EquipmentEnableDTO dto) {
        equipmentInfoService.enableEquipment(dto);
        return ResponseInfo.success();
    }


    @PostMapping("/printTag")
    @ApiOperation("打印设备标签")
    public ResponseInfo<EquipmentPrintInfoVO> printEquipmentTagInfo(@RequestBody EquipmentPrintTagDTO dto) {
        return ResponseInfo.success(equipmentInfoService.printEquipmentTagInfo(dto));
    }

    @GetMapping("/getPrintEquipment")
    @ApiOperation("获取打印机设备")
    public ResponseInfo<List<EquipmentPrintVO>> getPrintEquipment() {
        return ResponseInfo.success(equipmentInfoService.getPrintEquipment());
    }


    @PostMapping("/{equipmentId}/acquisitionPoint")
    @ApiOperation("匹配采集点")
    public ResponseInfo<Void> bindDataPropertyAcquisitionPoint(@PathVariable("equipmentId") Long equipmentId,
                                                               @RequestBody EquipmentAcquisitionBindVO equipmentAcquisitionBindVO) {
        equipmentInfoService.bindDataPropertyAcquisitionPoint(equipmentId,equipmentAcquisitionBindVO.getAcquisitionPlatform(),
                BeanUtil.copyToList(equipmentAcquisitionBindVO.getEquipmentBindAcquisitionVOS(), EquipmentPropertyDTO.class));
        return ResponseInfo.success();
    }


    @ApiOperation("根据产线id获取设备配置")
    @GetMapping("/getConfigByProductionLineId")
    public ResponseInfo<List<EquipmentInfoVO>> getConfigByProductionLineId(Long productionLineId) {
        return ResponseInfo.success(equipmentInfoService.getConfigByProductionLineId(productionLineId));
    }

    @ApiOperation("根据工位id获取设备配置")
    @GetMapping("/getConfigByStationIdList")
    public ResponseInfo<List<EquipmentInfoVO>> getConfigByStationIdList(@RequestParam(value = "stationIdList") List<Long> stationIdList) {
        return ResponseInfo.success(equipmentInfoService.getConfigByStationIdList(stationIdList));
    }


    @ApiOperation("获取设备的使用日志模板")
    @GetMapping("/{equipmentId}/useLogTemplate")
    public ResponseInfo<List<EquipmentTagUseTemplateVO>> getEquipmentUseTemplate(@PathVariable("equipmentId") Long equipmentId) {
        return ResponseInfo.success(BeanUtil.copyToList(equipmentInfoService.getUseLogTemplate(equipmentId), EquipmentTagUseTemplateVO.class));
    }

    @GetMapping("/getConfigByEquipmentCode")
    public ResponseInfo<EquipmentInfoVO> getEquipmentByEquipmentCode(String equipmentCode) {
        EquipmentInfoVO equipmentByEquipmentCode = equipmentInfoService.getEquipmentByEquipmentCode(equipmentCode);
        if (equipmentByEquipmentCode == null) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_EXIST);
        }
        if (!equipmentByEquipmentCode.getEnable()) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_ENABLE);
        }
        return ResponseInfo.success(equipmentByEquipmentCode);
    }
}
