package com.bmos.platform.service.factory.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.platform.service.factory.convert.FactoryStationConverter;
import com.bmos.platform.service.factory.mapper.EquipmentStationUserMapper;
import com.bmos.platform.service.factory.model.EquipmentStationUser;
import com.bmos.platform.service.factory.service.EquipmentStationUserService;
import com.bmos.platform.service.factory.service.dto.UserBindStationsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EquipmentStationUserServiceImpl implements EquipmentStationUserService {

    @Autowired
    private EquipmentStationUserMapper userMapper;

    @Override
    public Boolean saveUserList(List<EquipmentStationUser> stationUserList) {
        return userMapper.saveUserList(stationUserList);
    }

    @Override
    public List<String> getStationUserNameByStationId(Long id) {
        return userMapper.queryStationNameListByStationId(id);
    }

    @Override
    public List<String> getUserIdListByStationId(Long stationId) {
        List<EquipmentStationUser> equipmentStationUserList = userMapper.selectByStationId(stationId);
        return CollectionUtil.isNotEmpty(equipmentStationUserList) ? equipmentStationUserList.stream().map(EquipmentStationUser::getUserId).collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public List<EquipmentStationUser> getStationUserByStationId(Long stationId) {
        return userMapper.selectByStationId(stationId);
    }

    @Override
    public Boolean deleteById(List<EquipmentStationUser> stationUser) {
        return userMapper.deleteById(CollectionUtils.convertList(stationUser, EquipmentStationUser::getId));
    }

    @Override
    public Map<Long, List<String>> queryStationUserByStationIdList(List<Long> stationIdList) {
        List<EquipmentStationUser> stationUsers = userMapper.queryStationUserByStationIdList(stationIdList);
        if (CollectionUtil.isEmpty(stationUsers)) {
            return Collections.emptyMap();
        }
        return CollectionUtils.convertMultiMap(stationUsers, EquipmentStationUser::getStationId, EquipmentStationUser::getUserId);

    }

    @Override
    public List<String> getUserIdListByStationIdList(List<Long> stationIdList) {
        List<EquipmentStationUser> stationUsers = userMapper.queryStationUserByStationIdList(stationIdList);
        if (CollectionUtil.isEmpty(stationUsers)) {
            return new ArrayList<>();
        }
        return CollectionUtils.convertList(stationUsers, EquipmentStationUser::getUserId);
    }

    @Override
    public List<EquipmentStationUser> getStationByUserId(String userId) {
        return userMapper.getStationByUserId(userId);
    }

    @Override
    public boolean existByStationId(Long stationId) {
        return userMapper.existByStationId(stationId);
    }

    @Override
    public void userBindStations(UserBindStationsDTO dto) {
        if (CollUtil.isEmpty(dto.getAllStationIdList())){
            return ;
        }
        // 删除当前用户与前端能够显示的所有的工位的绑定关系
        userMapper.deleteByUserIdAndStationIdList(dto.getUserId(), dto.getAllStationIdList());
        List<EquipmentStationUser> stationUserList = FactoryStationConverter.INSTANCE.convertToStationUserList(dto.getUserId(), dto.getStationIdList());
        if (CollUtil.isEmpty(stationUserList)){
            return;
        }
        userMapper.saveOrUpdateBatch(stationUserList);
    }

    @Override
    public List<Long> userStationList(String userId) {
        List<EquipmentStationUser> stationUsers = userMapper.getStationByUserId(userId);
        if (CollUtil.isEmpty(stationUsers)){
            return new ArrayList<>();
        }
        return stationUsers.stream().map(EquipmentStationUser::getStationId).collect(Collectors.toList());
    }

    @Override
    public void deleteStationUserByStationId(Long stationId) {
        userMapper.deleteByStationId(stationId);
    }
}
