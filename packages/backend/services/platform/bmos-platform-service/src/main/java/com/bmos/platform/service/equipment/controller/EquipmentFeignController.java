package com.bmos.platform.service.equipment.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.AdminUtil;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.equipment.dto.EquipmentApplyHeartDTO;
import com.bmos.platform.facade.equipment.dto.EquipmentQueryDTO;
import com.bmos.platform.facade.equipment.enums.AcquisitionPointDataTypeEnum;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.*;
import com.bmos.platform.facade.factory.vo.StationPermissionVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentAppStationVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentInfoVO;
import com.bmos.platform.service.equipment.model.AcquisitionPoint;
import com.bmos.platform.service.equipment.service.AcquisitionPointService;
import com.bmos.platform.service.equipment.service.EquipmentInfoService;
import com.bmos.platform.service.equipment.service.dto.EquipmentAppStationDTO;
import com.bmos.platform.service.factory.model.EquipmentStationInfo;
import com.bmos.platform.service.factory.service.EquipmentStationInfoService;
import com.bmos.platform.service.factory.service.FactoryStationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备对内接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 09:20
 */
@RestController
@RequestMapping("/equipment/feign")
@Validated
public class EquipmentFeignController implements EquipmentConfigFeign {

    @Autowired
    private EquipmentInfoService infoService;

    @Autowired
    FactoryStationService factoryStationService;

    @Autowired
    private AcquisitionPointService acquisitionPointService;

    @Autowired
    private EquipmentStationInfoService equipmentStationInfoService;

    /**
     * 根据设备id获取设备配置信息
     *
     * @param equipmentId
     * @return
     */
    @Override
    @GetMapping("/getConfigByEquipmentId")
    public ResponseInfo<EquipmentInfoFeignVO> getConfigByEquipmentId(@RequestParam("equipmentId") @NotNull Long equipmentId) {
        EquipmentInfoVO configByEquipmentId = infoService.getConfigByEquipmentId(equipmentId);
        if (configByEquipmentId == null) {
            return ResponseInfo.success();
        }
        EquipmentInfoFeignVO equipmentInfoFeignVO = BeanUtil.copyProperties(configByEquipmentId,
                EquipmentInfoFeignVO.class);
        equipmentInfoFeignVO.setEquipmentTagDataList(BeanUtil.copyToList(configByEquipmentId.getTagIdList(),
                TagFeignVO.class));
        this.wrapperAcquisitionPointInfo(Lists.newArrayList(equipmentInfoFeignVO));
        return ResponseInfo.success(equipmentInfoFeignVO);
    }


    private void wrapperAcquisitionPointInfo(List<EquipmentInfoFeignVO> equipmentInfos) {
        List<EquipmentPropertyAcquisitionPointFeignVO> propertyAcquisitionVOS =
                equipmentInfos.stream().map(EquipmentInfoFeignVO::getDataPropertyList)
                        .filter(dataPropertyList -> !CollectionUtils.isEmpty(dataPropertyList)).flatMap(Collection::stream).collect(Collectors.toList());
        Set<Long> acquisitionPointId =
                propertyAcquisitionVOS.stream().filter(equipmentPropertyAcquisitionVO -> StringUtils.isNotEmpty(equipmentPropertyAcquisitionVO.getValue()))
                        .map(equipmentPropertyAcquisitionVO -> Long.valueOf(equipmentPropertyAcquisitionVO.getValue())).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(acquisitionPointId)) {
            return;
        }
        List<AcquisitionPoint> acquisitionPoints = acquisitionPointService.listByIds(acquisitionPointId);
        for (EquipmentInfoFeignVO equipmentInfo : equipmentInfos) {
            List<EquipmentPropertyAcquisitionPointFeignVO> dataPropertyList = equipmentInfo.getDataPropertyList();
            if (CollectionUtils.isEmpty(dataPropertyList)) {
                continue;
            }
            for (EquipmentPropertyAcquisitionPointFeignVO dataProperty : dataPropertyList) {
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
                    dataProperty.setAcquisitionPointDataType(Objects.requireNonNull(AcquisitionPointDataTypeEnum.findByName(acquisitionPoint.getDataType().getName())));
                }
            }
        }
    }

    /**
     * 根据工位id获取设备信息
     *
     * @param stationId
     * @return
     */
    @GetMapping("/getConfigByStationId")
    @Override
    public ResponseInfo<List<EquipmentInfoFeignVO>> getConfigByStationId(@RequestParam(value = "stationId") @NotNull Long stationId) {
        return ResponseInfo.success(this.wrapper(infoService.getConfigByStationId(stationId)));
    }

    @Override
    @GetMapping("/getConfigByProductionLineId")
    public ResponseInfo<List<EquipmentInfoFeignVO>> getConfigByProductionLineId(Long productionLineId) {
        return ResponseInfo.success(this.wrapper(infoService.getConfigByProductionLineId(productionLineId)));
    }

    @Override
    @GetMapping("/getConfigByProductionLineIdWithNoPermission")
    public ResponseInfo<List<EquipmentInfoFeignVO>> getConfigByProductionLineIdWithNoPermission(Long productionLineId) {
        List<EquipmentInfoFeignVO> result = this.wrapper(infoService.getConfigByProductionLineIdWithNoPermission(productionLineId));
        this.wrapperAcquisitionPointInfo(result);
        return ResponseInfo.success(result);
    }

    @Override
    @GetMapping("/getConfigByStationIdList")
    public ResponseInfo<List<EquipmentInfoFeignVO>> getConfigByStationIdList(@RequestParam(value = "stationIdList") List<Long> stationIdList) {
        return ResponseInfo.success(this.wrapper(infoService.getConfigByStationIdList(stationIdList)));
    }

    @Override
    @GetMapping("/getEquipmentByTagCode")
    public ResponseInfo<List<EquipmentInfoFeignVO>> getEquipmentByTagCode(@NotBlank String tagCode) {
        return ResponseInfo.success(this.wrapper(infoService.getEquipmentByTagCode(tagCode)));
    }

    @Override
    @GetMapping("/getConfigByEquipmentCode")
    public ResponseInfo<EquipmentInfoFeignVO> getEquipmentByEquipmentCode(String equipmentCode) {
        EquipmentInfoVO equipmentByEquipmentCode = infoService.getEquipmentByEquipmentCode(equipmentCode);
        if (equipmentByEquipmentCode == null) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_EXIST);
        }
        if (!equipmentByEquipmentCode.getEnable()) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_ENABLE);
        }
        // 非admin用户查询工位权限，如果没有工位权限，则抛出异常
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            EquipmentAppStationDTO stationDTO = new EquipmentAppStationDTO();
            stationDTO.setEquipmentId(equipmentByEquipmentCode.getId());
            List<EquipmentAppStationVO> allStationByEquipmentId = factoryStationService.getAllStationByEquipmentId(stationDTO);
            if (CollectionUtil.isEmpty(allStationByEquipmentId)) {
                throw new BmosException(PlatformResponseCode.NOT_HAVE_EQUIPMENT_PERMISSION);
            }
            List<StationPermissionVO> stationPermissionVOS = factoryStationService.checkStationPermission(allStationByEquipmentId.stream().map(EquipmentAppStationVO::getStationId).collect(Collectors.toList()), SysUserHolder.getUser().getUserId());
            if (CollectionUtil.isEmpty(stationPermissionVOS) || stationPermissionVOS.stream().noneMatch(StationPermissionVO::isPermission)) {
                throw new BmosException(PlatformResponseCode.NOT_HAVE_EQUIPMENT_PERMISSION);

            }
        }
        return ResponseInfo.success(getEquipmentInfoFeignVOResponseInfo(equipmentByEquipmentCode));
    }

    @Override
    @GetMapping("/getConfigByEquipmentCodeWithoutPermission")
    public ResponseInfo<EquipmentInfoFeignVO> getEquipmentByEquipmentCodeWithoutPermission(String equipmentCode) {
        EquipmentInfoVO equipmentByEquipmentCode = infoService.getEquipmentByEquipmentCode(equipmentCode);
        if (equipmentByEquipmentCode == null) {
            return ResponseInfo.success();
        }
        EquipmentInfoFeignVO result = getEquipmentInfoFeignVOResponseInfo(equipmentByEquipmentCode);
        this.wrapperAcquisitionPointInfo(Collections.singletonList(result));
        return ResponseInfo.success(result);
    }

    private EquipmentInfoFeignVO getEquipmentInfoFeignVOResponseInfo(EquipmentInfoVO equipmentByEquipmentCode) {
        EquipmentInfoFeignVO equipmentInfoFeignVO = BeanUtil.copyProperties(equipmentByEquipmentCode,
                EquipmentInfoFeignVO.class);
        equipmentInfoFeignVO.setEquipmentTagDataList(BeanUtil.copyToList(equipmentByEquipmentCode.getTagIdList(),
                TagFeignVO.class));
        // 查询当前设备属于哪些工位
        List<EquipmentStationInfo> stationInfos = equipmentStationInfoService.queryStationInfoByEquipmentId(equipmentInfoFeignVO.getId());
        if (CollUtil.isNotEmpty(stationInfos)){
            equipmentInfoFeignVO.setStationIdList(stationInfos.stream().map(EquipmentStationInfo::getStationId).collect(Collectors.toList()));
        }
        return equipmentInfoFeignVO;
    }

    @Override
    @PostMapping("/getEquipmentByParam")
    public ResponseInfo<List<EquipmentInfoFeignVO>> getConfigByStationIdList(@RequestBody EquipmentQueryDTO queryDTO) {
        List<EquipmentInfoFeignVO> result = this.wrapper(infoService.getEquipmentByParam(queryDTO));
        this.wrapperAcquisitionPointInfo(result);
        return ResponseInfo.success(result);
    }

    @Override
    @PostMapping("/getEquipmentConfigByTagCode")
    public ResponseInfo<List<EquipmentInfoFeignVO>> getEquipmentConfigByTagCode(String tagCode) {
        List<EquipmentInfoVO> equipmentByTagCode = infoService.getEquipmentByTagCode(tagCode);
        return ResponseInfo.success(this.wrapper(equipmentByTagCode));
    }

    private List<EquipmentInfoFeignVO> wrapper(List<EquipmentInfoVO> equipmentInfoVOS) {
        ArrayList<EquipmentInfoFeignVO> res = new ArrayList<>();
        if (CollectionUtil.isEmpty(equipmentInfoVOS)) {
            return res;
        }
        for (EquipmentInfoVO equipmentInfoVO : equipmentInfoVOS) {
            EquipmentInfoFeignVO equipmentInfoFeignVO = BeanUtil.copyProperties(equipmentInfoVO,
                    EquipmentInfoFeignVO.class);
            equipmentInfoFeignVO.setAcquisitionPlatform(equipmentInfoVO.getAcquisitionPlatform() == null ?
                    null : equipmentInfoVO.getAcquisitionPlatform().getValue());
            equipmentInfoFeignVO.setEquipmentTagDataList(BeanUtil.copyToList(equipmentInfoVO.getTagIdList(),
                    TagFeignVO.class));
            res.add(equipmentInfoFeignVO);
        }
        return res;
    }

    @Override
    @GetMapping("/tree")
    public ResponseInfo<List<EquipmentModuleTreeNodeFeignVO>> getEquipmentFeignTree() {
        return ResponseInfo.success(factoryStationService.getEquipmentFeignTree());
    }

    @Override
    @GetMapping("/list")
    public ResponseInfo<List<EquipmentInfoFeignVO>> selectEquipmentByIdList(@RequestParam("idList") Collection<Long> equipmentIdList) {
        return ResponseInfo.success(BeanUtil.copyToList(infoService.selectEquipmentByIdList(equipmentIdList), EquipmentInfoFeignVO.class));
    }

    @Override
    @PostMapping("/applyEquipment")
    public ResponseInfo<Void> applyEquipment(@RequestParam("equipmentId") Long equipmentId) {
        infoService.applyEquipment(equipmentId);
        return ResponseInfo.success();
    }

    @Override
    @PostMapping("/applyEquipmentHeart")
    public ResponseInfo<Void> applyEquipmentHeart(@RequestBody EquipmentApplyHeartDTO equipmentApplyHeartDTO) {
        infoService.applyEquipmentHeart(equipmentApplyHeartDTO);
        return ResponseInfo.success();
    }

    @Override
    @GetMapping("/get/delete/equipment")
    public ResponseInfo<List<EquipmentVO>> getDeleteEquipment(@RequestParam("equipmentIdList") List<Long> equipmentIdList){
        return ResponseInfo.success(infoService.getDeleteEquipment(equipmentIdList));
    }
}
