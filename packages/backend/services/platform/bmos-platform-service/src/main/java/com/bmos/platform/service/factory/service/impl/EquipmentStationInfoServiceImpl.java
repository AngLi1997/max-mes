package com.bmos.platform.service.factory.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import com.bmos.platform.service.factory.convert.FactoryStationConverter;
import com.bmos.platform.service.factory.mapper.EquipmentStationInfoMapper;
import com.bmos.platform.service.factory.mapper.EquipmentStationMapper;
import com.bmos.platform.service.factory.model.EquipmentStation;
import com.bmos.platform.service.factory.model.EquipmentStationInfo;
import com.bmos.platform.service.factory.model.FactoryLineStation;
import com.bmos.platform.service.factory.service.EquipmentStationInfoService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class EquipmentStationInfoServiceImpl implements EquipmentStationInfoService {

    @Autowired
    private EquipmentStationInfoMapper infoMapper;
    @Autowired
    private EquipmentStationInfoMapper equipmentStationInfoMapper;

    @Resource
    private EquipmentStationMapper equipmentStationMapper;

    @Override
    public List<String> getStationInfoNameListByStationId(Long id) {
        return infoMapper.getStationInfoNameListByStationId(id);
    }

    @Override
    public Boolean bindEquipment(List<EquipmentStationInfo> stationUserList) {
        return infoMapper.bindEquipment(stationUserList);
    }

    @Override
    public List<EquipmentStationInfo> queryStationInfoByEquipmentId(Long id) {
        return infoMapper.queryStationInfoByEquipmentId(id);
    }

    @Override
    public List<Long> queryInfoIdListByUserId(String userId) {
        return infoMapper.queryInfoIdListByUserId(userId);
    }

    @Override
    public List<EquipmentStationInfo> queryEquipmentByStationId(Long stationId) {
        return infoMapper.queryEquipmentByStationId(stationId);
    }

    @Override
    public Boolean deletcByIdList(List<EquipmentStationInfo> stationInfos) {
        return infoMapper.deletcByIdList(CollectionUtils.convertList(stationInfos, EquipmentStationInfo::getId));
    }

    @Override
    public Map<Long, List<Long>> queryStationInfoByStationIdList(List<Long> stationIdList) {
        List<EquipmentStationInfo> stationInfos = infoMapper.queryStationInfoByStationIdList(stationIdList);
        if (CollectionUtil.isEmpty(stationInfos)) {
            return Collections.emptyMap();
        }
        return CollectionUtils.convertMultiMap(stationInfos, EquipmentStationInfo::getStationId, EquipmentStationInfo::getEquipmentId);
    }

    @Override
    public List<EquipmentStation> queryStationNameByEquipmentId(Long id) {
        return infoMapper.queryStationNameByEquipmentId(id);
    }

    @Override
    public List<EquipmentStationInfo> selectByEquipmentId(Long equipmentId) {
        return infoMapper.selectByEquipmentId(equipmentId);
    }

    @Override
    public boolean existByStationId(Long stationId) {
        return equipmentStationInfoMapper.existByStationId(stationId);
    }

    @Nullable
    @Override
    public FactoryStationFeignVO queryStationById(Long stationId) {
        if (stationId == null){
            return null;
        }
        EquipmentStation equipmentStation = equipmentStationMapper.selectById(stationId);
        return FactoryStationConverter.INSTANCE.convertToFeignVO(equipmentStation);
    }

    @Override
    public void deleteRelationByStationId(Long stationId) {
        infoMapper.deleteRelationByStationId(stationId);
    }
}
