package com.bmos.mes.service.tag.convert;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.ingredient.weigh.vo.WeighBalanceEquipment;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.facade.equipment.enums.TagEquipmentPropertyCodeEnum;
import com.bmos.platform.facade.equipment.enums.TagEquipmentStatusCodeEnum;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentPropertyFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentStatusFeignVO;
import com.bmos.unit.service.UnitCache;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Mapper
public interface ScanDeviceConvert {

    ScanDeviceConvert INSTANCE = Mappers.getMapper(ScanDeviceConvert.class);

    default ScanDeviceVO convertToDeviceVo(EquipmentInfoFeignVO equipmentInfo) {
        ScanDeviceVO vo = new ScanDeviceVO();
        if (ObjectUtil.isEmpty(equipmentInfo)) {
            return vo;
        }
        vo.setDeviceId(equipmentInfo.getId());
        vo.setDeviceCode(equipmentInfo.getCode());
        vo.setDeviceName(equipmentInfo.getName());
        return vo;
    }

    default List<WeighBalanceEquipment> convertToEquipmentList(List<EquipmentInfoFeignVO> list) {
        List<WeighBalanceEquipment> equipmentList = list.stream().map(item -> {
            Map<String, EquipmentPropertyFeignVO> map = CollectionUtils.convertMap(item.getInfoPropertyList(),
                    EquipmentPropertyFeignVO::getCode);
            Map<String, EquipmentStatusFeignVO> statusMap = CollectionUtils.convertMap(item.getStatusPropertyList(),
                    EquipmentStatusFeignVO::getCode);
            WeighBalanceEquipment equipment = new WeighBalanceEquipment();
            equipment.setBalanceId(item.getId());
            equipment.setBalanceCode(item.getCode());
            equipment.setBalanceName(item.getName());
//            equipment.setStationId(item.getStationId());
            EquipmentPropertyFeignVO equipmentRange = map.get(TagEquipmentPropertyCodeEnum.WEIGHING_RANGE.getCode());
            String rangeStr = Optional.ofNullable(equipmentRange)
                    .map(EquipmentPropertyFeignVO::getValue)
                    .orElse(null);
            if (StringUtils.isNotBlank(rangeStr)) {
                // 校验格式： 小数,小数
                String regex = "^\\d+(\\.\\d*)?,\\d+(\\.\\d*)?$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(rangeStr);
                if (matcher.matches()) {
                    String[] split = rangeStr.split(",");
                    equipment.setMinRange(Arrays.stream(split).min(Comparator.comparing(BigDecimal::new))
                            .map(BigDecimal::new)
                            .orElse(null));
                    equipment.setMaxRange(Arrays.stream(split).max(Comparator.comparing(BigDecimal::new))
                            .map(BigDecimal::new)
                            .orElse(null));
                }
            }
            EquipmentPropertyFeignVO equipmentPrecision = map.get(TagEquipmentPropertyCodeEnum.WEIGHING_ACCURACY.getCode());
            equipment.setPrecision(ObjectUtil.isEmpty(equipmentPrecision) ? null
                    : Optional.ofNullable(equipmentPrecision.getValue())
                    .map(v -> {
                        try {
                            return new BigDecimal(v);
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .orElse(null)
            );
            EquipmentPropertyFeignVO equipmentUnit = map.get(TagEquipmentPropertyCodeEnum.WEIGHING_UNIT.getCode());
            if (StringUtils.isNotBlank(equipmentUnit.getValue())){
                try {
                    long unitId = Long.parseLong(equipmentUnit.getValue());
                    UnitCache unitCache = SpringUtil.getBean(UnitCache.class);
                    String unitName = unitCache.getGlobalUnitName(unitId);
                    equipment.setUnitId(unitId);
                    equipment.setUnit(unitName);
                } catch (NumberFormatException ignore) {

                }
            }
            EquipmentStatusFeignVO statusVo = statusMap.get(TagEquipmentStatusCodeEnum.CALIBRATION.getCode());
            equipment.setIsCalibrated(ObjectUtil.isEmpty(statusVo) ? null : statusVo.getFinishStatus());
            equipment.setCalibrateExpiredDate(ObjectUtil.isEmpty(statusVo) ? null : statusVo.getExpireDateTime() == null ? null : statusVo.getExpireDateTime().toLocalDate());
            EquipmentPropertyFeignVO equipmentIp = map.get(TagEquipmentPropertyCodeEnum.IP_ADDRESS.getCode());
            equipment.setWebsocketAddress(ObjectUtil.isEmpty(equipmentIp) ? null : equipmentIp.getValue());
            equipment.setProtocolType(Optional.ofNullable(map.get(TagEquipmentPropertyCodeEnum.WEIGHING_PROTOCOL_TYPE.getCode()))
                    .map(EquipmentPropertyFeignVO::getValue)
                    .orElse(null));
            equipment.setIsIdle(Objects.equals(EquipmentStatusCodeEnum.AVAILABLE.getCode(), item.getStatus()));
            return equipment;
        }).collect(Collectors.toList());
        return equipmentList;
    }

    default WeighBalanceEquipment convertToEquipment(EquipmentInfoFeignVO item) {
        Map<String, EquipmentPropertyFeignVO> map = CollectionUtils.convertMap(item.getInfoPropertyList(),
                EquipmentPropertyFeignVO::getCode);
        Map<String, EquipmentStatusFeignVO> statusMap = CollectionUtils.convertMap(item.getStatusPropertyList(),
                EquipmentStatusFeignVO::getCode);
        WeighBalanceEquipment equipment = new WeighBalanceEquipment();
        equipment.setBalanceId(item.getId());
        equipment.setBalanceCode(item.getCode());
        equipment.setBalanceName(item.getName());
        equipment.setStationIdList(item.getStationIdList());
        EquipmentPropertyFeignVO equipmentRange = map.get(TagEquipmentPropertyCodeEnum.WEIGHING_RANGE.getCode());
        String rangeStr = Optional.ofNullable(equipmentRange)
                .map(EquipmentPropertyFeignVO::getValue)
                .orElse(null);
        if (StringUtils.isNotBlank(rangeStr)) {
            // 校验格式： 小数,小数
            String regex = "^\\d+(\\.\\d*)?,\\d+(\\.\\d*)?$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(rangeStr);
            if (matcher.matches()) {
                String[] split = rangeStr.split(",");
                equipment.setMinRange(Arrays.stream(split).min(Comparator.comparing(BigDecimal::new))
                        .map(BigDecimal::new)
                        .orElse(null));
                equipment.setMaxRange(Arrays.stream(split).max(Comparator.comparing(BigDecimal::new))
                        .map(BigDecimal::new)
                        .orElse(null));
            }
        }
        EquipmentPropertyFeignVO equipmentPrecision = map.get(TagEquipmentPropertyCodeEnum.WEIGHING_ACCURACY.getCode());
        equipment.setPrecision(ObjectUtil.isEmpty(equipmentPrecision) ? null : Optional.ofNullable(equipmentPrecision.getValue()).map(BigDecimal::new).orElse(null));
        EquipmentPropertyFeignVO equipmentUnit = map.get(TagEquipmentPropertyCodeEnum.WEIGHING_UNIT.getCode());
        if (StringUtils.isNotBlank(equipmentUnit.getValue())){
            try {
                long unitId = Long.parseLong(equipmentUnit.getValue());
                UnitCache unitCache = SpringUtil.getBean(UnitCache.class);
                String unitName = unitCache.getGlobalUnitName(unitId);
                equipment.setUnitId(unitId);
                equipment.setUnit(unitName);
            } catch (NumberFormatException ignore) {

            }
        }
        EquipmentStatusFeignVO statusVo = statusMap.get(TagEquipmentStatusCodeEnum.CALIBRATION.getCode());
        equipment.setIsCalibrated(ObjectUtil.isEmpty(statusVo) ? null : statusVo.getFinishStatus());
        equipment.setCalibrateExpiredDate(ObjectUtil.isEmpty(statusVo) ? null : statusVo.getExpireDateTime() == null ? null : statusVo.getExpireDateTime().toLocalDate());
        EquipmentPropertyFeignVO equipmentIp = map.get(TagEquipmentPropertyCodeEnum.IP_ADDRESS.getCode());
        equipment.setWebsocketAddress(ObjectUtil.isEmpty(equipmentIp) ? null : equipmentIp.getValue());
        equipment.setIsIdle(Objects.equals(EquipmentStatusCodeEnum.AVAILABLE.getCode(), item.getStatus()));
        return equipment;
    }
}
