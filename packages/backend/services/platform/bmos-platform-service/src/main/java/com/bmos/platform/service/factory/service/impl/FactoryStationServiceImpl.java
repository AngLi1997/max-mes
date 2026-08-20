package com.bmos.platform.service.factory.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.enums.factory.FactoryModuleEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.equipment.dto.UpdateStationDTO;
import com.bmos.platform.facade.equipment.vo.EquipmentFeignStationVO;
import com.bmos.platform.facade.equipment.vo.EquipmentModuleTreeNodeFeignVO;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import com.bmos.platform.facade.factory.vo.StationPermissionVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentAppStationVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentInfoStationVO;
import com.bmos.platform.service.equipment.controller.vo.StationEquipmentInfoTreeNodeVO;
import com.bmos.platform.service.equipment.convert.EquipmentConvert;
import com.bmos.platform.service.equipment.mapper.EquipmentCategoryMapper;
import com.bmos.platform.service.equipment.mapper.EquipmentInfoMapper;
import com.bmos.platform.service.equipment.model.EquipmentCategory;
import com.bmos.platform.service.equipment.model.EquipmentInfo;
import com.bmos.platform.service.equipment.service.dto.EquipmentAppStationDTO;
import com.bmos.platform.service.factory.controller.vo.StationPageVO;
import com.bmos.platform.service.factory.controller.vo.StationTreeNodeVO;
import com.bmos.platform.service.factory.controller.vo.StationVO;
import com.bmos.platform.service.factory.convert.FactoryModuleConverter;
import com.bmos.platform.service.factory.convert.FactoryStationConverter;
import com.bmos.platform.service.factory.mapper.EquipmentStationMapper;
import com.bmos.platform.service.factory.model.*;
import com.bmos.platform.service.factory.repository.FactoryModuleRepository;
import com.bmos.platform.service.factory.repository.LineRepository;
import com.bmos.platform.service.factory.repository.RoomRepository;
import com.bmos.platform.service.factory.service.EquipmentStationInfoService;
import com.bmos.platform.service.factory.service.EquipmentStationUserService;
import com.bmos.platform.service.factory.service.FactoryStationService;
import com.bmos.platform.service.factory.service.dto.*;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FactoryStationServiceImpl implements FactoryStationService {

    @Autowired
    private EquipmentStationMapper mapper;

    @Autowired
    private FactoryModuleRepository factoryModuleRepository;

    @Autowired
    private EquipmentStationUserService stationUserService;

    @Autowired
    private EquipmentStationInfoService infoService;

    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;

    @Autowired
    private EquipmentCategoryMapper categoryMapper;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    LineRepository lineRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStation(StationSaveDTO dto) {
        List<EquipmentStation> equipmentStations = mapper.queryStationListByCode(dto.getCode());
        if (CollUtil.isNotEmpty(equipmentStations)) {
            throw new BmosException(PlatformResponseCode.FACTORY_SAVE_ERROR);
        }
        FactoryModule module = factoryModuleRepository.selectById(dto.getModuleId());
        if (Objects.isNull(module)) {
            throw new BmosException(PlatformResponseCode.FACTORY_MODULE_NOT_EXIST);
        }
        EquipmentStation equipmentStation = FactoryStationConverter.INSTANCE.convertToStation(dto);
        mapper.insert(equipmentStation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStation(StationUpdateDTO dto) {
        EquipmentStation equipmentStation = mapper.selectById(dto.getId());
        if (Objects.isNull(equipmentStation)) {
            throw new BmosException(PlatformResponseCode.FACTORY_STATION_NOT_EXIST);
        }
        equipmentStation.setName(dto.getName());
        equipmentStation.setDescription(dto.getDescription());
        mapper.updateById(equipmentStation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStation(Long id) {
        EquipmentStation equipmentStation = mapper.selectById(id);
        if (Boolean.TRUE.equals(equipmentStation.getEnable())) {
            throw new BmosException(PlatformResponseCode.STATION_DELETE_ERROR);
        }
        this.validDisabledStation(equipmentStation);
        // 删除与用户之间的绑定关系
        stationUserService.deleteStationUserByStationId(id);
        // 删除与房间之间的绑定关系
        roomRepository.deleteRelationByStationId(id);
        // 删除与产线之间的绑定关系
        lineRepository.deleteRelationByStationId(id);
        // 删除与设备之间的绑定关系
        infoService.deleteRelationByStationId(id);
        // 删除工位
        mapper.deleteStation(id);
    }

    @Override
    public void enableStation(StationEnableDTO dto) {
        EquipmentStation equipmentStation = mapper.selectById(dto.getId());
        if (Objects.isNull(equipmentStation)) {
            throw new BmosException(PlatformResponseCode.FACTORY_STATION_NOT_EXIST);
        }
        if (Boolean.FALSE.equals(dto.getEnable())) {
            this.validDisabledStation(equipmentStation);
        }
        equipmentStation.setEnable(dto.getEnable());
        mapper.saveOrUpdateStation(equipmentStation);
    }

    @Override
    public CommonPage<StationPageVO> getStationPage(StationPageDTO dto) {
        if (ObjectUtil.isNotNull(dto.getModuleId())) {
            List<Long> moduleIdList = factoryModuleRepository.getAllChildModuleId(dto.getModuleId(), FactoryModuleEnum.STATION.getType());
            dto.setModuleIdList(moduleIdList);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<StationPageVO> voPage = mapper.getStationPage(dto);
        if (CollUtil.isEmpty(voPage)) {
            BasePage page = new BasePage();
            page.setPageSize(dto.getPageSize());
            page.setPageNum(dto.getPageNum());
            return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
        }
        Map<Long, List<String>> userMap = stationUserService.queryStationUserByStationIdList(CollectionUtils.convertList(voPage, StationPageVO::getId));
        Map<Long, List<Long>> infoMap = infoService.queryStationInfoByStationIdList(CollectionUtils.convertList(voPage, StationPageVO::getId));
        voPage.forEach(item -> {
            item.setUserIdList(userMap.get(item.getId()));
            item.setEquipmentIdList(infoMap.get(item.getId()));
        });
        return CommonPage.convertPage(voPage);
    }

    @Override
    public StationVO getStationInfo(Long id) {
        StationVO stationInfo = mapper.getStationInfo(id);
        if (ObjectUtil.isEmpty(stationInfo)) {
            return null;
        }
        List<String> infoNameList = infoService.getStationInfoNameListByStationId(id);
        stationInfo.setEquipmentDetail(infoNameList);
        List<String> userNameList = stationUserService.getStationUserNameByStationId(id);
        stationInfo.setUserDetail(userNameList);
        // 查询当前工位所属的产线列表
        String lineName = lineRepository.selectLineNameByStationId(stationInfo.getId());
        if (Objects.nonNull(lineName)) {
            stationInfo.setLineName(Lists.newArrayList(lineName));
            return stationInfo;
        }
        FactoryRoom room = roomRepository.selectRoomByStationId(stationInfo.getId());
        if (Objects.isNull(room)) {
            return stationInfo;
        }
        stationInfo.setRoomName(room.getName());
        // 查询当前房间属于那个工位
        List<FactoryLine> factoryLines = lineRepository.selectLineByRoomId(room.getId());
        if (CollUtil.isNotEmpty(factoryLines)) {
            stationInfo.setLineName(factoryLines.stream().map(FactoryLine::getName).collect(Collectors.toList()));
        }
        return stationInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindEquipment(StationBindEquipmentDTO dto) {
        List<EquipmentStationInfo> stationInfos = infoService.queryEquipmentByStationId(dto.getStationId());
        if (CollUtil.isNotEmpty(stationInfos)) {
            infoService.deletcByIdList(stationInfos);
        }
        List<EquipmentStationInfo> stationUserList = dto.getEquipmentId().stream().map(item -> {
            EquipmentStationInfo info = new EquipmentStationInfo();
            info.setEquipmentId(item);
            info.setStationId(dto.getStationId());
            return info;
        }).collect(Collectors.toList());
        infoService.bindEquipment(stationUserList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindUser(StationBindUserDTO dto) {
        List<EquipmentStationUser> stationUser = stationUserService.getStationUserByStationId(dto.getStationId());
        if (CollUtil.isNotEmpty(stationUser)) {
            stationUserService.deleteById(stationUser);
        }
        List<EquipmentStationUser> stationUserList = dto.getUserIdList().stream().map(item -> {
            EquipmentStationUser user = new EquipmentStationUser();
            user.setUserId(item);
            user.setStationId(dto.getStationId());
            return user;
        }).collect(Collectors.toList());
        stationUserService.saveUserList(stationUserList);
    }


    @Override
    @DistributedLock(key = "updateStationUseCount")
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStationUseCount(UpdateStationDTO dto) {
        List<EquipmentStation> stationList = mapper.selectStationByIdList(dto.getStationIdList());
        if (CollUtil.isEmpty(stationList)) {
            return Boolean.TRUE;
        }
        stationList.forEach(item -> {
            item.setUseCount(dto.getType() ? item.getUseCount() + 1 : item.getUseCount() - 1);
        });
        return mapper.updateStationUseCount(stationList);
    }

    @Override
    public List<String> getStationUserByStationId(Long stationId) {
        return stationUserService.getUserIdListByStationId(stationId);
    }

    @Override
    public List<StationEquipmentInfoTreeNodeVO> stationEquipment() {
        List<EquipmentCategory> categoryList = categoryMapper.selectCategoryList();
        if (CollUtil.isEmpty(categoryList)) {
            return Collections.emptyList();
        }
        List<StationEquipmentInfoTreeNodeVO> treeNodeList = BeanUtil.copyToList(categoryList, StationEquipmentInfoTreeNodeVO.class);
        List<EquipmentInfo> infos = equipmentInfoMapper.selectInfoListByEnable(Boolean.TRUE);
        if (CollUtil.isEmpty(infos)) {
            return TreeUtil.buildTree(treeNodeList, false);
        }
        List<EquipmentInfoStationVO> vos = BeanUtil.copyToList(infos, EquipmentInfoStationVO.class);
        Map<Long, List<EquipmentInfoStationVO>> infoMap = CollectionUtils.convertMultiMap(vos, EquipmentInfoStationVO::getCategoryId);
        treeNodeList.forEach(item -> {
            item.setInfoList(infoMap.get(item.getId()));
        });
        List<StationEquipmentInfoTreeNodeVO> stationEquipmentInfoTreeNodeVOS = TreeUtil.buildTree(treeNodeList, false);
        return weedOutNoEquipment(stationEquipmentInfoTreeNodeVOS);
    }

    @Override
    public List<String> getStationUserByStationIdList(List<Long> stationIdList) {
        return stationUserService.getUserIdListByStationIdList(stationIdList);
    }

    @Override
    public List<EquipmentAppStationVO> getAllStationByEquipmentId(EquipmentAppStationDTO stationDTO) {
        List<EquipmentStationInfo> equipmentStationInfos = infoService.selectByEquipmentId(stationDTO.getEquipmentId());
        if (CollUtil.isEmpty(equipmentStationInfos)) {
            return Collections.emptyList();
        }
        List<EquipmentStation> equipmentStations = mapper.selectBatchIds(equipmentStationInfos.stream().map(EquipmentStationInfo::getStationId).collect(Collectors.toList()));
        return EquipmentConvert.INSTANCE.convert2AppStationVO(equipmentStations);
    }

    @Override
    public List<StationTreeNodeVO> stationTree() {
        // 查询所有工位模型
        List<FactoryModule> factoryStationList = factoryModuleRepository.selectListByType(FactoryModuleEnum.STATION.getType());
        if (CollectionUtil.isEmpty(factoryStationList)) {
            return new ArrayList<>();
        }
        // 查询房间模型下所有房间
        List<Long> moduleIdList = factoryStationList.stream().map(FactoryModule::getId).collect(Collectors.toList());
        List<EquipmentStation> stationList = mapper.selectByModuleIdList(moduleIdList);
        Map<Long, List<EquipmentStation>> stationMap = stationList.stream().collect(Collectors.groupingBy(EquipmentStation::getModuleId));
        List<StationTreeNodeVO> stationTreeNodeVOList = FactoryModuleConverter.INSTANCE.convert2StationTree(factoryStationList, stationMap);
        List<StationTreeNodeVO> stationTreeNodeVOS = TreeUtil.buildTree(stationTreeNodeVOList, false);
        StationTreeNodeVO root = new StationTreeNodeVO();
        root.setChildren(stationTreeNodeVOS);
        weedOutStation(root);
        return root.getChildren();
    }

    @Override
    public List<FactoryStationFeignVO> queryStationListByLineIds(List<Long> productLineIds) {
        // 查询产线与工位的绑定关系
        List<FactoryLineStation> factoryLineStationList = lineRepository.selectByLineIdList(productLineIds);
        if (CollectionUtil.isEmpty(factoryLineStationList)) {
            return new ArrayList<>();
        }
        List<Long> stationIdList = factoryLineStationList.stream().map(FactoryLineStation::getStationId).collect(Collectors.toList());
        List<EquipmentStation> stationList = mapper.selectBatchIds(stationIdList);
        return FactoryStationConverter.INSTANCE.convert2FeignVO(stationList);
    }

    @Override
    public List<StationPermissionVO> checkStationPermission(Collection<Long> stationIdList, String userId) {
        // 根据userId查询当前人员绑定的的工位
        List<EquipmentStationUser> stationUserList = stationUserService.getStationByUserId(userId);
        Set<Long> stationIdSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(stationUserList)) {
            stationIdSet = stationUserList.stream().map(EquipmentStationUser::getStationId).collect(Collectors.toSet());
        }
        List<StationPermissionVO> stationPermissionVOList = new ArrayList<>();
        for (Long stationId : stationIdList) {
            StationPermissionVO stationPermissionVO = new StationPermissionVO();
            stationPermissionVO.setStationId(stationId);
            stationPermissionVO.setPermission(stationIdSet.contains(stationId));
            stationPermissionVOList.add(stationPermissionVO);
        }
        return stationPermissionVOList;
    }

    @Override
    public List<FactoryStationFeignVO> getStationInfoByLineId(Long lineId) {
        // 查询产线与工位的绑定关系
        List<FactoryLineStation> factoryLineStationList = lineRepository.selectStationByLineId(lineId);
        // 查询产线与房间的绑定关系
        List<FactoryLineRoom> factoryLineRoomList = lineRepository.selectByLineId(lineId);
        if (CollectionUtil.isEmpty(factoryLineStationList) && CollectionUtil.isEmpty(factoryLineRoomList)) {
            return new ArrayList<>();
        }
        Set<Long> stationIdSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(factoryLineStationList)) {
            stationIdSet = factoryLineStationList.stream().map(FactoryLineStation::getStationId).collect(Collectors.toSet());
        }
        if (CollectionUtil.isNotEmpty(factoryLineRoomList)) {
            List<Long> roomIdList = factoryLineRoomList.stream().map(FactoryLineRoom::getRoomId).collect(Collectors.toList());
            List<FactoryRoomStation> factoryRoomStationList = roomRepository.selectBindStationByRoomIdList(roomIdList);
            stationIdSet.addAll(CollectionUtil.isEmpty(factoryRoomStationList) ? new ArrayList<>() : factoryRoomStationList.stream().map(FactoryRoomStation::getStationId).collect(Collectors.toSet()));
        }
        if (CollectionUtil.isEmpty(stationIdSet)) {
            return new ArrayList<>();
        }
        return FactoryStationConverter.INSTANCE.convert2FeignVO(mapper.selectBatchIds(stationIdSet));
    }

    @Override
    public List<EquipmentFeignStationVO> getStationByRoomId(Long roomId) {
        List<FactoryRoomStation> factoryRoomStationList = roomRepository.selectBindStationByRoomId(roomId);
        if (CollectionUtil.isEmpty(factoryRoomStationList)) {
            return new ArrayList<>();
        }
        List<Long> stationIdList = factoryRoomStationList.stream().map(FactoryRoomStation::getStationId).collect(Collectors.toList());
        List<EquipmentStation> stationList = mapper.selectBatchIds(stationIdList);
        return FactoryStationConverter.INSTANCE.convert2FeignStationVO(stationList);
    }

    @Override
    public List<EquipmentModuleTreeNodeFeignVO> getEquipmentFeignTree() {
        List<StationEquipmentInfoTreeNodeVO> stationEquipmentInfoTreeNodeVOS = this.stationEquipment();
        return FactoryStationConverter.INSTANCE.convert2EquipmentTree(stationEquipmentInfoTreeNodeVOS);
    }

    @Override
    public List<String> getStationIdsByUserId(String userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        return stationUserService.getStationByUserId(userId)
                .stream()
                .map(EquipmentStationUser::getStationId)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public FactoryStationFeignVO queryStationById(Long stationId) {
        if (stationId == null) {
            return null;
        }
        return infoService.queryStationById(stationId);
    }

    @Override
    public List<EquipmentStation> selectByIds(List<Long> stationIdList) {
        if (CollUtil.isEmpty(stationIdList)) {
            return new ArrayList<>();
        }
        return mapper.selectBatchIds(stationIdList);
    }

    @Override
    public void userBindStations(UserBindStationsDTO dto) {
        stationUserService.userBindStations(dto);
    }

    @Override
    public List<Long> userStationList(String userId) {
        return stationUserService.userStationList(userId);
    }

    /**
     * 剔除没有工位数据的分类
     *
     * @param root
     */
    private void weedOutStation(StationTreeNodeVO root) {
        List<StationTreeNodeVO> children = new ArrayList<>();
        for (StationTreeNodeVO child : root.getChildren()) {
            if (CollUtil.isNotEmpty(child.getChildren())) {
                weedOutStation(child);
            }
            if (CollUtil.isNotEmpty(child.getChildren()) || CollUtil.isNotEmpty(child.getInfoList())) {
                children.add(child);
            }
        }
        root.setChildren(children);
    }

    private void validDisabledStation(EquipmentStation equipmentStation) {
        // 是否业务配置绑定
        if (equipmentStation.getUseCount() > 0) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ENABLE_ERROR);
        }
        // 判断工位是否绑定房间
        if (roomRepository.existBindRoom(equipmentStation.getId())) {
            throw new BmosException(PlatformResponseCode.FACTORY_STATION_ALREADY_BIND_ROOM);
        }
        // 判断工位是否绑定产线
        if (lineRepository.existStationBindLine(equipmentStation.getId())) {
            throw new BmosException(PlatformResponseCode.FACTORY_STATION_ALREADY_BIND_LINE);
        }
        // 是否绑定设备或人
        if (infoService.existByStationId(equipmentStation.getId())) {
            throw new BmosException(PlatformResponseCode.FACTORY_STATION_ALREADY_BIND_EQUIPMENT);
        }
        if (stationUserService.existByStationId(equipmentStation.getId())) {
            throw new BmosException(PlatformResponseCode.FACTORY_STATION_ALREADY_BIND_PERSON);
        }
    }

    /**
     * 剔除没有设备的设备分类
     *
     * @param stationEquipmentInfoTreeNodeVOS
     */
    private List<StationEquipmentInfoTreeNodeVO> weedOutNoEquipment(List<StationEquipmentInfoTreeNodeVO> stationEquipmentInfoTreeNodeVOS) {
        List<StationEquipmentInfoTreeNodeVO> res = new ArrayList<>();
        for (StationEquipmentInfoTreeNodeVO stationEquipmentInfoTreeNodeVO : stationEquipmentInfoTreeNodeVOS) {
            if (CollUtil.isEmpty(stationEquipmentInfoTreeNodeVO.getInfoList()) && CollUtil.isEmpty(stationEquipmentInfoTreeNodeVO.getChildren())) {
                continue;
            }
            if (CollUtil.isNotEmpty(stationEquipmentInfoTreeNodeVO.getInfoList())) {
                res.add(stationEquipmentInfoTreeNodeVO);
            }
            if (CollUtil.isNotEmpty(stationEquipmentInfoTreeNodeVO.getChildren())) {
                stationEquipmentInfoTreeNodeVO.setChildren(weedOutNoEquipment(stationEquipmentInfoTreeNodeVO.getChildren()));
                if (CollUtil.isNotEmpty(stationEquipmentInfoTreeNodeVO.getChildren()) && CollUtil.isEmpty(stationEquipmentInfoTreeNodeVO.getInfoList())) {
                    res.add(stationEquipmentInfoTreeNodeVO);
                }
            }
        }
        return res;
    }
}
