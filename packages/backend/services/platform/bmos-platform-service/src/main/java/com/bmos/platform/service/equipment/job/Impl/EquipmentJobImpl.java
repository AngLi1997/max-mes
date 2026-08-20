package com.bmos.platform.service.equipment.job.Impl;

import cn.hutool.core.util.StrUtil;
import com.bmos.cache.redis.RedisService;
import com.bmos.expire.properties.ExpireMessageProperty;
import com.bmos.platform.common.enums.PlatformRedisKeyDefine;
import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import com.bmos.platform.service.equipment.expire.EquipmentExpireInitialization;
import com.bmos.platform.service.equipment.expire.EquipmentExpireListener;
import com.bmos.platform.service.equipment.job.EquipmentJob;
import com.bmos.platform.service.equipment.mapper.EquipmentInfoMapper;
import com.bmos.platform.service.equipment.model.EquipmentInfo;
import com.bmos.platform.service.equipment.model.EquipmentOperateLog;
import com.bmos.platform.service.equipment.service.EquipmentLogService;
import com.bmos.platform.service.equipment.service.EquipmentTagService;
import com.bmos.platform.service.equipment.service.dto.EquipmentOperateDTO;
import com.bmos.scheduler.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EquipmentJobImpl implements EquipmentJob {

    @Autowired
    EquipmentInfoMapper equipmentInfoMapper;

    @Autowired
    EquipmentTagService equipmentTagService;

    @Autowired
    EquipmentLogService equipmentLogService;

    @Autowired
    RedisService redisService;

    @Resource
    EquipmentExpireListener expireListener;

    @Resource
    private EquipmentExpireInitialization equipmentExpireInitialization;


    @Override
    @XxlJob("equipmentHeart")
    public void equipmentHeart() {
        // 查询所有被业务自动占用的设备
        List<EquipmentInfo> equipmentInfos = equipmentInfoMapper.selectBusinessOccupyEquipment();
        for (EquipmentInfo equipmentInfo : equipmentInfos) {
            if (Objects.isNull(equipmentInfo.getOperateLogId())) {
                continue;
            }
            EquipmentOperateLog operateLog = equipmentLogService.selectOperateLogById(equipmentInfo.getOperateLogId());
            if (Objects.isNull(operateLog) || !StrUtil.equals(operateLog.getChangeType(), EquipmentStatusLogChangeType.BUSINESS.getValue())) {
                continue;
            }
            Object o = redisService.get(String.valueOf(equipmentInfo.getId()), PlatformRedisKeyDefine.EQUIPMENT_APPLY_HEART);
            if (Objects.nonNull(o)) {
                // 代表心跳存在
                continue;
            }
            // 释放设备
            equipmentTagService.releaseEquipment(new EquipmentOperateDTO(equipmentInfo.getId()), EquipmentStatusLogChangeType.BUSINESS);
        }
    }

    @Override
    @XxlJob("equipmentPropertiesStatus")
    public void equipmentPropertiesStatus() {
        List<ExpireMessageProperty> init = equipmentExpireInitialization.init();
        init = init.stream()
                .filter(e-> e.getExpireMessage().getExpireTime() < convert2TimeStamp(LocalDateTime.now()))
                .collect(Collectors.toList());
        for (ExpireMessageProperty expireMessageProperty : init) {
            expireListener.onExpire(expireMessageProperty);
        }
    }

    Long convert2TimeStamp(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        return instant.getEpochSecond();
    }
}
