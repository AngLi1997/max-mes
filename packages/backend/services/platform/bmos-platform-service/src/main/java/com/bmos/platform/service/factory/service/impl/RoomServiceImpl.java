package com.bmos.platform.service.factory.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.id.IdUtils;
import com.bmos.expire.producer.ExpireMessageProducer;
import com.bmos.expire.properties.ExpireMessage;
import com.bmos.expire.properties.ExpireMessageProperty;
import com.bmos.mq.listener.Event.StateEvent;
import com.bmos.mq.listener.enums.StateEventTypeEnum;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.GlobalConstants;
import com.bmos.platform.common.enums.equipment.PropertyTypeEnum;
import com.bmos.platform.common.enums.expire.ExpireListenerConstants;
import com.bmos.platform.common.enums.factory.FactoryModuleEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.facade.factory.dto.ChangeRoomStatusFeignDTO;
import com.bmos.platform.facade.factory.dto.MobileChangeRoomStatusFeignDTO;
import com.bmos.platform.facade.factory.dto.RoomMobilePageFeignDTO;
import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import com.bmos.platform.facade.factory.enums.RoomStatusOperateTypeEnum;
import com.bmos.platform.facade.factory.vo.*;
import com.bmos.platform.service.dict.service.DictService;
import com.bmos.platform.service.dict.vo.DictVO;
import com.bmos.platform.service.equipment.mapper.EquipmentInfoMapper;
import com.bmos.platform.service.equipment.mapper.EquipmentPropertyInfoMapper;
import com.bmos.platform.service.equipment.model.EquipmentInfo;
import com.bmos.platform.service.equipment.model.EquipmentPropertyInfo;
import com.bmos.platform.service.factory.controller.vo.*;
import com.bmos.platform.service.factory.convert.FactoryModuleConverter;
import com.bmos.platform.service.factory.convert.FactoryRoomConverter;
import com.bmos.platform.service.factory.convert.FactoryStationConverter;
import com.bmos.platform.service.factory.enums.TenementFloorStatusEnums;
import com.bmos.platform.service.factory.mapper.*;
import com.bmos.platform.service.factory.mapper.param.RoomParam;
import com.bmos.platform.service.factory.model.*;
import com.bmos.platform.service.factory.repository.*;
import com.bmos.platform.service.factory.service.RoomService;
import com.bmos.platform.service.factory.service.data.RoomStatusOperateData;
import com.bmos.platform.service.factory.service.dto.*;
import com.bmos.platform.service.feign.CommonFeignClient;
import com.bmos.platform.service.feign.CommonFeignClientFactory;
import com.bmos.platform.service.permission.model.ResourcePermission;
import com.bmos.platform.service.permission.service.ResourcePermissionService;
import com.bmos.platform.service.permission.service.dto.ResourcePermissionSaveDTO;
import com.bmos.platform.service.system.user.model.User;
import com.bmos.platform.service.system.user.service.UserService;
import com.bmos.platform.service.util.PageUtils;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final String MES_SERVICE_NAME = "bmos-mes-service";

    private static final String ROOM_ENV_PROPERTY_DICT_CODE = "EnvironmentalParameters";
    private static final String EQUIPMENT_DATA_DICT_CODE = "DeviceDataFields";

    @Autowired
    FactoryRoomMapper factoryRoomMapper;

    @Autowired
    FactoryRoomStationMapper roomStationMapper;

    @Autowired
    FactoryModuleRepository factoryModuleRepository;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    PlatformApiAdaptor platformApiAdaptor;

    @Autowired
    FactoryStationRepository factoryStationRepository;

    @Autowired
    ResourcePermissionService resourcePermissionService;

    @Autowired
    RoomLogRepository roomLogRepository;

    @Autowired
    UserService userService;

    @Autowired
    ExpireMessageProducer expireMessageProducer;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CommonFeignClientFactory commonFeignClientFactory;

    @Autowired
    private EquipmentStationMapper equipmentStationMapper;

    @Autowired
    private BpFactoryRoomEnvPropertyMapper roomEnvPropertyMapper;

    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;

    @Autowired
    private EquipmentPropertyInfoMapper equipmentPropertyInfoMapper;

    @Autowired
    private FactoryTenementMapper factoryTenementMapper;

    @Autowired
    private FactoryTenementFloorMapper factoryTenementFloorMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private EquipmentStationInfoMapper infoMapper;


    @Resource
    private Executor asyncTaskExecutor;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoom(RoomSaveDTO dto) {
        this.beforeAssert(dto);


        FactoryRoom factoryRoom = FactoryRoomConverter.INSTANCE.convert2DO(dto);
        factoryRoom.setEnable(Boolean.FALSE);
        factoryRoom.setStatus(RoomStatusEnum.BE_CLEANED.getCode());
        SysUser loginUser = SysUserHolder.getUser();
        factoryRoom.setOperateId(loginUser.getUserId());
        factoryRoom.setOperator(loginUser.getLoginName() + "-" + loginUser.getUserName());
        factoryRoom.setOperateTime(LocalDateTime.now());
        factoryRoomMapper.insert(factoryRoom);
        // 数据权限保存
        resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                .deptIds(dto.getDeptIds()).resourceId(factoryRoom.getId()).build());
    }

    private void beforeAssert(RoomSaveDTO dto) {
        // 校验模型是否存在
        if (!factoryModuleRepository.existsById(dto.getModuleId())) {
            throw new BmosException(PlatformResponseCode.FACTORY_MODULE_NOT_EXIST);
        }
        // 校验code是否重复
        if (factoryRoomMapper.existsByCode(dto.getCode())) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_CODE_REPEAT);
        }

        if (dto.getFloorId() != null) {
            if (dto.getTenementId() == null) {
                throw new BmosException(PlatformResponseCode.TENEMENT_NOT_EXISTS);
            }
        }

        FactoryTenement factoryTenement;
        if (dto.getTenementId() != null) {
            factoryTenement = factoryTenementMapper.selectById(dto.getTenementId());
            if (factoryTenement == null) {
                throw new BmosException(PlatformResponseCode.TENEMENT_NOT_EXISTS);
            }
            if (dto.getFloorId() != null) {
                FactoryTenementFloor factoryTenementFloor = factoryTenementFloorMapper.selectById(dto.getFloorId());
                if (factoryTenementFloor == null) {
                    throw new BmosException(PlatformResponseCode.TENEMENT_FLOOR_NOT_EXISTS);
                } else if (!factoryTenementFloor.getStatus().equals(TenementFloorStatusEnums.ENABLE)) {
                    throw new BmosException(PlatformResponseCode.TENEMENT_FLOOR_NOT_ENABLE, factoryTenementFloor.getCode());
                } else if (!factoryTenementFloor.getTenementId().equals(dto.getTenementId())) {
                    throw new BmosException(PlatformResponseCode.TENEMENT_FLOOR_BELONG_TO_TENEMENT, factoryTenementFloor.getCode(), factoryTenement.getCode());
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoom(RoomUpdateDTO dto) {
        // 校验房间是否存在
        FactoryRoom factoryRoom = factoryRoomMapper.selectById(dto.getId());
        if (Objects.isNull(factoryRoom)) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_EXIST);
        }
        this.beforeAssert(dto);
        doUpdateRoom(dto, factoryRoom);
    }

    /**
     * 更新房间
     *
     * @param dto
     * @param factoryRoom
     */
    private void doUpdateRoom(RoomUpdateDTO dto, FactoryRoom factoryRoom) {
        factoryRoom.setName(dto.getName());
        factoryRoom.setTimeLimit((long) (Double.parseDouble(dto.getTimeLimit()) * GlobalConstants.HOUR_OF_SECOND));
        factoryRoom.setDescription(dto.getDescription());
        SysUser loginUser = SysUserHolder.getUser();
        factoryRoom.setOperateId(loginUser.getUserId());
        factoryRoom.setOperator(loginUser.getLoginName() + "-" + loginUser.getUserName());
        factoryRoom.setOperateTime(LocalDateTime.now());
        // 更新数据权限
        resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                .deptIds(dto.getDeptIds()).resourceId(factoryRoom.getId()).build());
        factoryRoomMapper.updateById(factoryRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoom(Long id) {
        // 判断房间是否存在
        FactoryRoom factoryRoom = factoryRoomMapper.selectById(id);
        if (Objects.isNull(factoryRoom)) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_EXIST);
        }
        // 判断房间是否为在用状态
        if (Objects.equals(RoomStatusEnum.OCCUPATION.getCode(), factoryRoom.getStatus())) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_ALREADY_OCCUPATION);
        }
        // 判断房间是否停用
        if (factoryRoom.getEnable()) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_ALREADY_ENABLE);
        }

        this.validDeleteRoom(factoryRoom);

        factoryRoomMapper.deleteById(id);
        // 删除数据权限
        resourcePermissionService.deleteByResourceId(id);
        // 删除对应的绑定关系
        // 删除与工位的绑定关系
        roomStationMapper.deleteByRoomId(id);
        // 删除与产线的绑定关系
        lineRepository.deleteRelationByRoomId(id);
    }

    @Override
    public void enableRoom(RoomEnableDTO dto) {
        FactoryRoom factoryRoom = factoryRoomMapper.selectById(dto.getId());
        if (Objects.isNull(factoryRoom)) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_EXIST);
        }
        // 判断房间是否为在用状态
        if (Objects.equals(RoomStatusEnum.OCCUPATION.getCode(), factoryRoom.getStatus())) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_ALREADY_OCCUPATION);
        }

        // 房间停用校验
        if (!dto.getEnable()) {
            this.validDeleteRoom(factoryRoom);
        }
        factoryRoom.setEnable(dto.getEnable());
        SysUser loginUser = SysUserHolder.getUser();
        factoryRoom.setOperateId(loginUser.getUserId());
        factoryRoom.setOperator(loginUser.getLoginName() + "-" + loginUser.getUserName());
        factoryRoom.setOperateTime(LocalDateTime.now());
        factoryRoomMapper.updateById(factoryRoom);
    }

    /**
     * 校验房间能否被删除
     *
     * @param factoryRoom
     */
    private void validDeleteRoom(FactoryRoom factoryRoom) {
        // 房间是否被业务配置绑定
        if (factoryRoom.getUseCount() > 0) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_ALREADY_USE);
        }
        // 房间需判断房间是否与产线关联
        if (lineRepository.existByRoomId(factoryRoom.getId())) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_ALREADY_BIND_LINE);
        }
        // 房间是否绑定工位
        if (roomStationMapper.existByRoomId(factoryRoom.getId())) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_ALREADY_BIND_STATION);
        }
    }

    @Override
    public CommonPage<RoomPageVO> getRoomPage(RoomPageDTO dto) {
        RoomParam roomParam = FactoryRoomConverter.INSTANCE.convert2Param(dto);
        if (Objects.nonNull(dto.getModuleId())) {
            List<Long> moduleIdList = factoryModuleRepository.getAllChildModuleId(dto.getModuleId(), FactoryModuleEnum.ROOM.getType());
            roomParam.setModuleIdList(moduleIdList);
            roomParam.setModuleId(null);
        }
        // 数据权限
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollectionUtil.isEmpty(deptIds)) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        roomParam.setDeptIdList(deptIds);
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<FactoryRoom> factoryRoomList = factoryRoomMapper.selectByParam(roomParam);
        if (CollectionUtil.isEmpty(factoryRoomList)) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        CommonPage<FactoryRoom> roomPage = CommonPage.convertPage(factoryRoomList);
        List<Long> roomIdList = factoryRoomList.stream().map(FactoryRoom::getId).collect(Collectors.toList());
        List<FactoryRoomStation> roomStations = roomStationMapper.selectByRoomIdList(roomIdList);
        Map<Long, List<Long>> roomStationIdMap = roomStations.stream().collect(Collectors.groupingBy(FactoryRoomStation::getRoomId, Collectors.mapping(FactoryRoomStation::getStationId, Collectors.toList())));
        Set<Long> tenementIds = factoryRoomList.stream().map(FactoryRoom::getTenementId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> floorIds = factoryRoomList.stream().map(FactoryRoom::getFloorId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, FactoryTenement> tenementMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(tenementIds)) {
            List<FactoryTenement> factoryTenements = factoryTenementMapper.selectBatchIds(tenementIds);
            tenementMap = factoryTenements.stream().collect(Collectors.toMap(FactoryTenement::getId, Function.identity()));
        }
        Map<Long, FactoryTenementFloor> floorMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(floorIds)) {
            List<FactoryTenementFloor> factoryTenementFloors = factoryTenementFloorMapper.selectBatchIds(floorIds);
            floorMap = factoryTenementFloors.stream().collect(Collectors.toMap(FactoryTenementFloor::getId, Function.identity()));
        }
        List<RoomPageVO> pageVOList = FactoryModuleConverter.INSTANCE.convert2RoomPageVO(factoryRoomList, roomStationIdMap, tenementMap, floorMap);
        return CommonPage.CommonPage(pageVOList, Long.valueOf(roomPage.getTotal()), dto);
    }

    @Override
    public RoomInfoVO getRoomInfo(Long id) {
        FactoryRoom factoryRoom = factoryRoomMapper.selectById(id);
        if (Objects.isNull(factoryRoom)) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_EXIST);
        }
        // 查询房间下所有的工位信息
        List<FactoryRoomStation> roomStations = roomStationMapper.selectByRoomId(id);
        List<EquipmentStation> stationList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(roomStations)) {
            stationList = factoryStationRepository.selectByStationIdList(roomStations.stream().map(FactoryRoomStation::getStationId).collect(Collectors.toList()));
        }
        RoomInfoVO roomInfoVO = FactoryModuleConverter.INSTANCE.convert2RoomVO(factoryRoom, stationList.stream().map(item -> {
            CodeNameVO codeNameVO = new CodeNameVO();
            codeNameVO.setName(item.getName());
            codeNameVO.setCode(item.getCode());
            return codeNameVO;
        }).collect(Collectors.toList()));
        if (Objects.equals(TreeUtil.parentId, factoryRoom.getModuleId())) {
            return roomInfoVO;
        }
        FactoryModule factoryModule = factoryModuleRepository.selectById(factoryRoom.getModuleId());
        roomInfoVO.setModuleName(factoryModule.getName());
        // 楼层信息
        if (Objects.nonNull(factoryRoom.getFloorId())) {
            FactoryTenementFloor factoryTenementFloor = factoryTenementFloorMapper.selectById(factoryRoom.getFloorId());
            roomInfoVO.setFloorName(factoryTenementFloor.getName());
        }
        // 楼栋信息
        if (Objects.nonNull(factoryRoom.getTenementId())) {
            FactoryTenement factoryTenement = factoryTenementMapper.selectById(factoryRoom.getTenementId());
            roomInfoVO.setTenementName(factoryTenement.getName());
        }
        // 查询环境参数
        List<RoomEnvPropertyWithAcquitPointDTO> envPropertyWithAcquitPointDTOS = roomEnvPropertyMapper.selectByRoomIds(Lists.newArrayList(id));
        Map<Long, List<RoomEnvPropertyWithAcquitPointDTO>> envRoomLists = envPropertyWithAcquitPointDTOS.stream().collect(Collectors.groupingBy(RoomEnvPropertyWithAcquitPointDTO::getRoomId));
        List<DictVO> envDictVOS = dictService.queryDictDetailByCode(ROOM_ENV_PROPERTY_DICT_CODE);
        Map<String, String> envDictMap = envDictVOS.stream().collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        if (envRoomLists.containsKey(roomInfoVO.getId())) {
            roomInfoVO.setRoomEnvPropertyDTOList(envRoomLists.get(roomInfoVO.getId()));
            roomInfoVO.getRoomEnvPropertyDTOList().forEach(roomEnvPropertyWithAcquitPointDTO -> {
                roomEnvPropertyWithAcquitPointDTO.setEnvPropertyName(envDictMap.get(roomEnvPropertyWithAcquitPointDTO.getEnvPropertyCode()));
            });
        }
        // 产线信息
        List<FactoryLine> factoryLines = lineRepository.selectLineByRoomId(id);
        if (CollectionUtil.isNotEmpty(factoryLines)) {
            roomInfoVO.setLineInfoList(BeanUtil.copyToList(factoryLines, CodeNameVO.class));
        }
        return roomInfoVO;
    }

    @Override
    public CommonPage<RoomMobilePageFeignVO> getRoomMobilePage(RoomMobilePageFeignDTO dto) {
        RoomParam roomParam = FactoryRoomConverter.INSTANCE.convert2Param(dto);
        // 数据权限
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollectionUtil.isEmpty(deptIds)) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        roomParam.setDeptIdList(deptIds);
        roomParam.setEnable(Boolean.TRUE);
        roomParam.setMobile(true);
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<FactoryRoom> factoryRoomList = factoryRoomMapper.selectByParam(roomParam);
        CommonPage<FactoryRoom> roomCommonPage = CommonPage.convertPage(factoryRoomList);
        return CommonPage.CommonPage(FactoryModuleConverter.INSTANCE.convert2RoomMobilePageVO(factoryRoomList), Long.valueOf(roomCommonPage.getTotal()), dto);
    }

    @Override
    public void operateRoomStatus(MobileChangeRoomStatusFeignDTO dto) {
        // 手动更改房间状态 房间状态流转
        RoomStatusOperateData operateData = FactoryRoomConverter.INSTANCE.convert2Data(dto, RoomStatusOperateTypeEnum.MANUAL_INPUT);
        // 数据校验
        FactoryRoom room = validateOperateRoomStatus(operateData);
        // 状态流转
        handlerRoomStatus(room, operateData);
        // 通知任务 房间状态变更
        this.sendTaskMessageAsync(room);
        // 若房间状态变更为已清洁 并进行过期自动变更
        if (!Objects.equals(RoomStatusEnum.CLEANED.getCode(), operateData.getStatus())) {
            return;
        }
        // 进行过期自动变更
        this.sendExpireMessage(room);

    }

    @Override
    public RoomInfoMobileFeignVO getMobileRoomInfo(Long id) {
        FactoryRoom factoryRoom = factoryRoomMapper.selectById(id);
        if (Objects.isNull(factoryRoom)) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_EXIST);
        }
        // 判断房间有没有激活
        if (!factoryRoom.getEnable()) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_ENABLE);
        }
        // 判断当前人是否具有房间的权限
        List<Long> deptIds = resourcePermissionService.getDeptListByResourceId(factoryRoom.getId());
        if (CollectionUtil.isEmpty(deptIds) || !CollectionUtil.containsAny(deptIds, platformApiAdaptor.deptIds())) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_PERMISSION);
        }
        FactoryRoomStatusLog factoryRoomStatusLog = null;
        if (ObjectUtil.equals(RoomStatusEnum.OCCUPATION.getCode(), factoryRoom.getStatus())) {
            // 查询操作记录
            factoryRoomStatusLog = roomLogRepository.selectStatusLogByRoomIdAndStatus(factoryRoom.getId(), RoomStatusEnum.OCCUPATION.getCode());
        }
        return FactoryRoomConverter.INSTANCE.convert2MobileVO(factoryRoom, factoryRoomStatusLog);
    }

    @Override
    public RoomCleanInfoFeignVO getRoomCleanInfoByRoomId(Long roomId) {
        FactoryRoom factoryRoom = factoryRoomMapper.selectById(roomId);
        if (Objects.isNull(factoryRoom)) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_EXIST);
        }
        RoomCleanInfoFeignVO roomCleanInfoFeignVO = FactoryRoomConverter.INSTANCE.convert2RoomCleanFeignVO(factoryRoom);
        // 查询当前房间最近一次的清场日志
        FactoryCleanRoomLog roomLog = roomLogRepository.selectLatestRoomLog(factoryRoom.getId());
        if (Objects.isNull(roomLog)) {
            return roomCleanInfoFeignVO;
        }
        List<User> userList = userService.getByUserIds(Sets.newHashSet(roomLog.getOperatorId(), roomLog.getVerifyId()));
        Map<String, User> userMap = userList.stream().collect(Collectors.toMap(User::getUserId, Function.identity()));
        roomCleanInfoFeignVO.setBeginTime(roomLog.getBeginTime());
        roomCleanInfoFeignVO.setEndTime(roomLog.getEndTime());
        roomCleanInfoFeignVO.setExpireDate(roomLog.getExpireTime());
        roomCleanInfoFeignVO.setOperatorId(roomLog.getOperatorId());
        ;
        roomCleanInfoFeignVO.setVerifyId(roomLog.getVerifyId());
        roomCleanInfoFeignVO.setVerifyTime(roomLog.getVerifyTime());
        roomCleanInfoFeignVO.setOperator(userMap.get(roomLog.getOperatorId()).getUserName());
        roomCleanInfoFeignVO.setVerifier(userMap.get(roomLog.getVerifyId()).getUserName());
        return roomCleanInfoFeignVO;
    }

    @Override
    public void changeRoomStatus(ChangeRoomStatusFeignDTO dto) {
        // 自动更改房间状态 房间状态流转
        RoomStatusOperateData operateData = FactoryRoomConverter.INSTANCE.convert2Data(dto, RoomStatusOperateTypeEnum.AUTO_RECOGNITION);
        // 数据校验
        FactoryRoom room = validateOperateRoomStatus(operateData);
        // 状态流转
        handlerRoomStatus(room, operateData);
        // 通知任务 房间状态变更
        this.sendTaskMessageAsync(room);
        // 若房间状态变更为已清洁 并进行过期自动变更
        if (ObjectUtil.equals(RoomStatusEnum.CLEANED.getCode(), operateData.getStatus())) {
            this.sendExpireMessage(room);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindStation(RoomBindStationDTO dto) {
        // 先删除之前的绑定关系 防止重复绑定
        roomStationMapper.deleteByRoomId(dto.getId());
        if (CollectionUtil.isEmpty(dto.getStationIdList())) {
            return;
        }
        // 校验工位是否绑定在其他房间下
        Map<Long, FactoryRoom> roomStationMap = roomRepository.stationBindRoom(dto.getStationIdList());
        // 查询工位与产线的绑定关系
        Map<Long, FactoryLine> lineStationMap = lineRepository.stationBindLine(dto.getStationIdList());
        Set<Long> equipmentIdSet = new HashSet<>();
        if (CollUtil.isNotEmpty(lineStationMap)) {
            equipmentIdSet.addAll(lineStationMap.keySet());
        }
        if (CollUtil.isNotEmpty(roomStationMap)) {
            equipmentIdSet.addAll(roomStationMap.keySet());
        }
        List<EquipmentStation> equipmentStations = new ArrayList<>();
        if (CollUtil.isNotEmpty(equipmentIdSet)) {
            equipmentStations = equipmentStationMapper.selectBatchIds(equipmentIdSet);
        }

        for (EquipmentStation equipmentStation : equipmentStations) {
            FactoryRoom factoryRoom = roomStationMap.get(equipmentStation.getId());
            FactoryLine factoryLine = lineStationMap.get(equipmentStation.getId());
            if (Objects.nonNull(factoryLine)) {
                throw new BmosException(PlatformResponseCode.FACTORY_STATION_ALREADY_BIND_LINE_TEMPLATE, equipmentStation.getCode() + "-" + equipmentStation.getName(), factoryLine.getName());
            }
            if (Objects.nonNull(factoryRoom)) {
                throw new BmosException(PlatformResponseCode.FACTORY_STATION_ALREADY_BIND_ROOM_TEMPLATE, equipmentStation.getCode() + "-" + equipmentStation.getName(), factoryRoom.getName());
            }
        }
        List<FactoryRoomStation> factoryRoomStationList = FactoryRoomConverter.INSTANCE.convert2BindDO(dto);
        roomStationMapper.insertBatch(factoryRoomStationList);
    }

    @Override
    public List<RoomTreeNodeVO> getRoomTree() {
        // 查询所有房间模型
        List<FactoryModule> factoryRoomList = factoryModuleRepository.selectListByType(FactoryModuleEnum.ROOM.getType());
        if (CollectionUtil.isEmpty(factoryRoomList)) {
            return null;
        }
        // 查询房间模型下所有房间
        List<Long> moduleIdList = factoryRoomList.stream().map(FactoryModule::getId).collect(Collectors.toList());
        // 数据权限
        List<Long> deptList = platformApiAdaptor.deptIds();
        List<FactoryRoom> roomList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(deptList)) {
            roomList = factoryRoomMapper.selectByParam(RoomParam.builder().deptIdList(deptList).moduleIdList(moduleIdList).enable(Boolean.TRUE).build());
        }
        Map<Long, List<FactoryRoom>> roomMap = roomList.stream().collect(Collectors.groupingBy(FactoryRoom::getModuleId));
        List<RoomTreeNodeVO> roomTreeNodeVOList = FactoryModuleConverter.INSTANCE.convert2RoomTreeNodeVO(factoryRoomList, roomMap);
        List<RoomTreeNodeVO> roomTreeNodeVOS = TreeUtil.buildTree(roomTreeNodeVOList, false);
        return weedOutNoRoom(roomTreeNodeVOS);
    }

    @Override
    public List<RoomInfoFeignVO> getRoomInfoByLineId(Long lineId, boolean findStation) {
        // 根据产线id查询产线下的所有房间信息
        List<FactoryLineRoom> lineRoomList = lineRepository.selectByLineId(lineId);
        if (CollectionUtil.isEmpty(lineRoomList)) {
            return new ArrayList<>();
        }
        List<Long> roomIdList = lineRoomList.stream().map(FactoryLineRoom::getRoomId).collect(Collectors.toList());
        List<RoomInfoFeignVO> roomInfoFeignVOS = new ArrayList<>();
        if (!findStation) {
            List<FactoryRoom> roomList = factoryRoomMapper.selectBatchIds(roomIdList);
            // 查询产线下绑定的工位
            roomInfoFeignVOS = FactoryRoomConverter.INSTANCE.convert2RoomFeignVOList(roomList);
            // 查询所有房间的数据权限
            Map<Long, List<Long>> permissionMap = getRoomPermission(roomIdList);
            for (RoomInfoFeignVO roomInfoFeignVO : roomInfoFeignVOS) {
                roomInfoFeignVO.setPermisionIdList(permissionMap.get(roomInfoFeignVO.getId()));
            }
            List<FactoryLineStation> lineStationList = lineRepository.selectStationByLineId(lineId);
            if (CollectionUtil.isNotEmpty(lineStationList)) {
                List<Long> stationIdList = lineStationList.stream().map(FactoryLineStation::getStationId).collect(Collectors.toList());
                List<EquipmentStation> stationList = factoryStationRepository.selectByStationIdList(stationIdList);
                // 新增一个虚拟房间 将直接绑定的工位放入虚拟房间中
                RoomInfoFeignVO roomInfoFeignVO = new RoomInfoFeignVO();
                roomInfoFeignVO.setCode(RoomInfoFeignVO.VIRTUAL_ROOM);
                roomInfoFeignVO.setStationFeignVOList(FactoryStationConverter.INSTANCE.convert2FeignVO(stationList));
                roomInfoFeignVOS.add(roomInfoFeignVO);
            }
            return roomInfoFeignVOS;
        }
        roomInfoFeignVOS = this.selectByRoomIds(roomIdList);
        return roomInfoFeignVOS;
    }

    /**
     * 查询房间的数据权限
     *
     * @param roomIdList
     * @return
     */
    private Map<Long, List<Long>> getRoomPermission(List<Long> roomIdList) {
        List<ResourcePermission> resourcePermissions = resourcePermissionService.getDeptListByResourceIdList(roomIdList);
        if (CollUtil.isEmpty(resourcePermissions)) {
            return new HashMap<>();
        }
        return resourcePermissions.stream().collect(Collectors.groupingBy(ResourcePermission::getResourceId, Collectors.mapping(ResourcePermission::getDeptId, Collectors.toList())));
    }

    @Override
    public List<RoomInfoFeignVO> selectByRoomIds(List<Long> roomIdList) {
        // 查询房间信息
        if (CollectionUtil.isEmpty(roomIdList)) {
            return new ArrayList<>();
        }
        List<FactoryRoom> roomList = factoryRoomMapper.selectBatchIds(roomIdList);
        // 根据房间id查询房间下绑定的所有工位
        List<FactoryRoomStation> factoryRoomStationList = roomStationMapper.selectByRoomIdList(roomIdList);
        Map<Long, List<FactoryRoomStation>> roomFacotryStationMap = factoryRoomStationList.stream().collect(Collectors.groupingBy(FactoryRoomStation::getRoomId));
        Map<Long, List<EquipmentStation>> roomStationMap = new HashMap<>();
        Map<Long, List<Long>> permissionMap = getRoomPermission(roomIdList);
        if (CollectionUtil.isEmpty(factoryRoomStationList)) {
            return FactoryRoomConverter.INSTANCE.convert2RoomFeignVOList(roomList, roomStationMap, permissionMap);
        }
        List<Long> stationIdList = factoryRoomStationList.stream().map(FactoryRoomStation::getStationId).collect(Collectors.toList());
        List<EquipmentStation> stationList = factoryStationRepository.selectByStationIdList(stationIdList);
        Map<Long, EquipmentStation> stationMap = stationList.stream().collect(Collectors.toMap(EquipmentStation::getId, station -> station));
        for (FactoryRoom factoryRoom : roomList) {
            List<FactoryRoomStation> roomStationList = roomFacotryStationMap.get(factoryRoom.getId());
            if (CollectionUtil.isEmpty(roomStationList)) {
                continue;
            }
            for (FactoryRoomStation factoryRoomStation : roomStationList) {
                if (!stationMap.containsKey(factoryRoomStation.getStationId())) {
                    continue;
                }
                roomStationMap.computeIfAbsent(factoryRoom.getId(), k -> new ArrayList<>()).add(stationMap.get(factoryRoomStation.getStationId()));
            }
        }
        return FactoryRoomConverter.INSTANCE.convert2RoomFeignVOList(roomList, roomStationMap, permissionMap);
    }

    @Override
    public RoomPrintVO printRoom(Long roomId) {
        FactoryRoom factoryRoom = factoryRoomMapper.selectById(roomId);
        if (Objects.isNull(factoryRoom)) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_EXIST);
        }
        if (!factoryRoom.getEnable()) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_ENABLE);
        }
        return FactoryRoomConverter.INSTANCE.convert2PrintVO(factoryRoom);
    }

    @Override
    public List<RoomModuleTreeNodeFeignVO> getRoomFeignTree() {
        List<RoomTreeNodeVO> roomTreeList = this.getRoomTree();
        return FactoryRoomConverter.INSTANCE.convert2RoomModeuleTreeNodeFeignVO(roomTreeList);
    }

    @Override
    public RoomInfoMobileFeignVO getMobileRoomInfoByCode(String code) {
        FactoryRoom factoryRoom = factoryRoomMapper.selectByCode(code);
        if (Objects.isNull(factoryRoom)) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_EXIST);
        }
        // 判断房间有没有激活
        if (!factoryRoom.getEnable()) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_ENABLE);
        }
        // 判断当前人是否具有房间的权限
        List<Long> deptIds = resourcePermissionService.getDeptListByResourceId(factoryRoom.getId());
        if (CollectionUtil.isEmpty(deptIds) || !CollectionUtil.containsAny(deptIds, platformApiAdaptor.deptIds())) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_PERMISSION);
        }
        FactoryRoomStatusLog factoryRoomStatusLog = null;
        if (ObjectUtil.equals(RoomStatusEnum.OCCUPATION.getCode(), factoryRoom.getStatus())) {
            // 查询操作记录
            factoryRoomStatusLog = roomLogRepository.selectStatusLogByRoomIdAndStatus(factoryRoom.getId(), RoomStatusEnum.OCCUPATION.getCode());
        }
        return FactoryRoomConverter.INSTANCE.convert2MobileVO(factoryRoom, factoryRoomStatusLog);
    }

    @Override
    public List<FactoryRoomFeignVO> queryRoomListByRoomIds(List<Long> roomIds) {
        if (CollUtil.isEmpty(roomIds)) {
            return new ArrayList<>();
        }
        return roomRepository.queryRoomListByRoomIds(roomIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRoomEnvProperty(List<RoomEnvPropertyDTO> roomEnvPropertyDTO) {
        // 房间是否存在
        Set<Long> roomIdList = roomEnvPropertyDTO.stream().map(RoomEnvPropertyDTO::getRoomId).collect(Collectors.toSet());
        LambdaQueryWrapper<FactoryRoom> roomQl = new QueryWrapper<FactoryRoom>().lambda();
        roomQl.in(FactoryRoom::getId, roomIdList);
        List<FactoryRoom> factoryRooms = factoryRoomMapper.selectList(roomQl);
        // 对房间id区差集
        roomIdList.removeAll(factoryRooms.stream().map(FactoryRoom::getId).collect(Collectors.toSet()));
        if (CollUtil.isNotEmpty(roomIdList)) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_EXIST);
        }
        // 设备是否存在
        Set<Long> equipmentIdList = roomEnvPropertyDTO.stream().map(RoomEnvPropertyDTO::getEquipmentId).collect(Collectors.toSet());
        List<EquipmentInfo> equipmentInfos = equipmentInfoMapper.selectBatchIds(equipmentIdList);
        equipmentIdList.removeAll(equipmentInfos.stream().map(EquipmentInfo::getId).collect(Collectors.toSet()));
        if (CollUtil.isNotEmpty(equipmentIdList)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_EXIST);
        }
        equipmentIdList = roomEnvPropertyDTO.stream().map(RoomEnvPropertyDTO::getEquipmentId).collect(Collectors.toSet());
        // 设备是否存在该设备数据
        List<EquipmentPropertyInfo> equipmentPropertyInfos = equipmentPropertyInfoMapper.queryEquipmentPropertyByEquipmentIdListAndType(Lists.newArrayList(equipmentIdList), PropertyTypeEnum.TAG_DATA_PROPERTY.getCode());
        Map<Long, List<EquipmentPropertyInfo>> equipmentProperty = equipmentPropertyInfos.stream().collect(Collectors.groupingBy(EquipmentPropertyInfo::getEquipmentId));
        Map<Long, List<RoomEnvPropertyDTO>> envPropertyMap = roomEnvPropertyDTO.stream().collect(Collectors.groupingBy(RoomEnvPropertyDTO::getEquipmentId));
        Map<Long, String> equipmentCodeMap = equipmentInfos.stream().collect(Collectors.toMap(EquipmentInfo::getId, EquipmentInfo::getCode));
        envPropertyMap.forEach((k, v) -> {
            Set<String> envPropertyEquipDataCodeSet = v.stream().map(RoomEnvPropertyDTO::getEquipmentDataPropertyCode).collect(Collectors.toSet());
            List<EquipmentPropertyInfo> equipmentPropertyInfoList = equipmentProperty.get(k);
            if (CollectionUtil.isEmpty(equipmentPropertyInfoList)) {
                throw new BmosException(PlatformResponseCode.EQUIPMENT_DATA_PROPERTY_NOT_EXIST, equipmentCodeMap.get(k), String.join(",", envPropertyEquipDataCodeSet));
            }
            Set<String> equipmentPropertyDataCodeSet = equipmentPropertyInfoList.stream().map(EquipmentPropertyInfo::getPropertyCode).collect(Collectors.toSet());
            // 找到设备不存在的设备数据
            envPropertyEquipDataCodeSet.removeAll(equipmentPropertyDataCodeSet);
            if (CollectionUtil.isNotEmpty(envPropertyEquipDataCodeSet)) {
                throw new BmosException(PlatformResponseCode.EQUIPMENT_DATA_PROPERTY_NOT_EXIST, equipmentCodeMap.get(k), String.join(",", envPropertyEquipDataCodeSet));
            }
        });
        // 判断提交的数据有没有重复
        if (CollUtil.isNotEmpty(roomEnvPropertyDTO)) {
            Map<String, List<RoomEnvPropertyDTO>> map = roomEnvPropertyDTO.stream().collect(Collectors.groupingBy(item -> item.getRoomId() + "#" + item.getEnvPropertyCode() + "#" + item.getEquipmentDataPropertyCode() + "#" + item.getEquipmentId()));
            List<Map.Entry<String, List<RoomEnvPropertyDTO>>> entryList = map.entrySet().stream().filter(entry -> entry.getValue().size() > 1).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(entryList)) {
                String msg = entryList.stream().map(entry -> entry.getKey().split("#")[2]).collect(Collectors.joining(","));
                throw new BmosException(PlatformResponseCode.FACTORY_ROOM_ENV_PROPERTY_REPEAT, msg);
            }
        }
        // 先删除
        LambdaQueryWrapper<FactoryRoomEnvProperty> ql = new QueryWrapper<FactoryRoomEnvProperty>().lambda();
        ql.in(FactoryRoomEnvProperty::getRoomId, roomEnvPropertyDTO.stream().map(RoomEnvPropertyDTO::getRoomId).collect(Collectors.toList()));
        List<FactoryRoomEnvProperty> envProperties = roomEnvPropertyMapper.selectList(ql);
        if (CollUtil.isNotEmpty(envProperties)) {
            roomEnvPropertyMapper.deleteBatchIds(envProperties.stream().map(FactoryRoomEnvProperty::getId).collect(Collectors.toList()));
        }
        List<FactoryRoomEnvProperty> factoryRoomEnvProperties = BeanUtil.copyToList(roomEnvPropertyDTO, FactoryRoomEnvProperty.class);
        factoryRoomEnvProperties.forEach(factoryRoomEnvProperty -> factoryRoomEnvProperty.setId(IdUtils.getSnowflake()));
        roomEnvPropertyMapper.insertBatch(factoryRoomEnvProperties);
    }


    /**
     * 查询房间列表
     *
     * @param roomListQueryDTO 查询条件
     * @param page             分页条件
     * @return 查询结果
     */
    @Override
    public CommonPage<FactoryRoomDTO> page(RoomListQueryDTO roomListQueryDTO, BasePage page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), PageUtils.getOrderByOrDefaultByUpdateTimeDesc(page));
        List<FactoryRoomDTO> factoryRoomDTOS = factoryRoomMapper.list(roomListQueryDTO);
        if (CollUtil.isEmpty(factoryRoomDTOS)) {
            return new CommonPage<>();
        }
        Set<Long> roomIds = factoryRoomDTOS.stream().map(FactoryRoomDTO::getId).collect(Collectors.toSet());
        List<RoomEnvPropertyWithAcquitPointDTO> envPropertyWithAcquitPointDTOS = roomEnvPropertyMapper.selectByRoomIds(roomIds);
        Map<Long, List<RoomEnvPropertyWithAcquitPointDTO>> envRoomLists = envPropertyWithAcquitPointDTOS.stream().collect(Collectors.groupingBy(RoomEnvPropertyWithAcquitPointDTO::getRoomId));
        factoryRoomDTOS.forEach(factoryRoomDTO -> {
            if (envRoomLists.containsKey(factoryRoomDTO.getId())) {
                factoryRoomDTO.setRoomEnvPropertyDTOList(envRoomLists.get(factoryRoomDTO.getId()));
            }
        });
        return CommonPage.convertPage(factoryRoomDTOS);
    }

    @Override
    public List<FactoryRoomDTO> list(RoomListQueryDTO queryDTO) {
        List<FactoryRoomDTO> factoryRoomDTOS = factoryRoomMapper.list(queryDTO);
        if (CollUtil.isEmpty(factoryRoomDTOS)) {
            return new ArrayList<>();
        }
        Set<Long> roomIds = factoryRoomDTOS.stream().map(FactoryRoomDTO::getId).collect(Collectors.toSet());
        List<RoomEnvPropertyWithAcquitPointDTO> envPropertyWithAcquitPointDTOS = roomEnvPropertyMapper.selectByRoomIds(roomIds);
        Map<Long, List<RoomEnvPropertyWithAcquitPointDTO>> envRoomLists = envPropertyWithAcquitPointDTOS.stream().collect(Collectors.groupingBy(RoomEnvPropertyWithAcquitPointDTO::getRoomId));
        factoryRoomDTOS.forEach(factoryRoomDTO -> {
            if (envRoomLists.containsKey(factoryRoomDTO.getId())) {
                factoryRoomDTO.setRoomEnvPropertyDTOList(envRoomLists.get(factoryRoomDTO.getId()));
            }
        });
        return factoryRoomDTOS;
    }

    @Override
    public CommonPage<RoomAppPageVO> getRoomPageByLineId(RoomAppPageDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        // 根据产线id查询产线下的所有房间信息
        List<FactoryLineRoom> lineRoomList = lineRepository.selectByLineId(dto.getProductionLineId());
        if (CollUtil.isEmpty(lineRoomList)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, dto);
        }
        CommonPage<FactoryLineRoom> page = CommonPage.convertPage(lineRoomList);
        List<RoomInfoFeignVO> roomInfoByLineId = this.selectByRoomIds(lineRoomList.stream().map(FactoryLineRoom::getRoomId).collect(Collectors.toList()));
        CommonPage<RoomInfoFeignVO> roomInfoFeignVOCommonPage = CommonPage.CommonPage(roomInfoByLineId, page.getTotal().longValue(), dto);
        return FactoryRoomConverter.INSTANCE.convert2RoomAppPageVO(roomInfoFeignVOCommonPage);
    }

    /**
     * 发送过期消息
     *
     * @param room
     */
    private void sendExpireMessage(FactoryRoom room) {
        if (Objects.isNull(room.getExpireTime())) {
            return;
        }
        ExpireMessageProperty expireMessageProperty = new ExpireMessageProperty();
        expireMessageProperty.setTag(ExpireListenerConstants.ROOM_EXPIRE);
        ExpireMessage expireMessage = new ExpireMessage();
        expireMessage.setUniqueId(room.getId());
        expireMessage.setExpireTime(convert2TimeStamp(room.getExpireTime()));
        expireMessageProperty.setExpireMessage(expireMessage);
        expireMessageProducer.sendAndWeedDuplicates(expireMessageProperty);
    }

    Long convert2TimeStamp(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        return instant.getEpochSecond();
    }

    /**
     * 通知任务进行状态变更
     *
     * @param room
     */
    private void sendTaskMessageAsync(FactoryRoom room) {
        StateEvent stateEvent = new StateEvent();
        stateEvent.setId(room.getId());
        stateEvent.setState(String.valueOf(room.getStatus()));
        stateEvent.setType(StateEventTypeEnum.ROOM.getCode());
        SysUser user = SysUserHolder.getUser();
        CompletableFuture.runAsync(() -> {
                    SysUserHolder.setUser(user);
                    CommonFeignClient feignClient = commonFeignClientFactory.getFeignClient(MES_SERVICE_NAME);
                    feignClient.conditionUpdate(stateEvent);
                    SysUserHolder.remove();
                },
                asyncTaskExecutor);
    }

    /**
     * 状态流转 + 日志记录
     *
     * @param room        房间信息
     * @param operateData 操作详情
     */
    private void handlerRoomStatus(FactoryRoom room, RoomStatusOperateData operateData) {
        // 进行状态流转
        Integer preStatus = room.getStatus();
        room.setStatus(operateData.getStatus());
        room.setExpireTime(operateData.getExpireTime());
        factoryRoomMapper.updateById(room);
        Integer curStatus = room.getStatus();
        // 查询账户信息
        List<User> userList = userService.getByUserIds(Sets.newHashSet(operateData.getOperateId(), operateData.getVerifierId()));
        Map<String, User> userMap = userList.stream().collect(Collectors.toMap(User::getUserId, Function.identity()));
        // 状态如果没有变更则不记录状态变更日志
        // 状态流转记录房间状态变更日志
        if (!Objects.equals(preStatus, curStatus)) {
            roomLogRepository.saveStatusLog(FactoryRoomConverter.INSTANCE.convert2StatusLog(room, operateData, userMap, preStatus, curStatus));
        }
        if (!Objects.equals(operateData.getStatus(), RoomStatusEnum.CLEANED.getCode())) {
            return;
        }
        // 则记录日志 并进行过期自动变更
        roomLogRepository.saveCleanLog(FactoryRoomConverter.INSTANCE.convert2Log(room, operateData, userMap));
    }

    /**
     * 房间状态更改数据校验
     *
     * @param operateData
     * @return
     */
    private FactoryRoom validateOperateRoomStatus(RoomStatusOperateData operateData) {
        FactoryRoom room = factoryRoomMapper.selectById(operateData.getId());
        // 房间存在性校验
        if (Objects.isNull(room)) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_EXIST);
        }
        // 房间启用状态校验
        if (!room.getEnable()) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_ENABLE);
        }
        if (Objects.isNull(operateData.getOperateId()) || Objects.isNull(operateData.getVerifierId())) {
            // 操作人和复核人不能为空
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_STATUS_CHANGE_PARAM_ERROR);
        }
        if (Objects.equals(operateData.getStatus(), RoomStatusEnum.BE_CLEANED.getCode())) {
            // 变更为待清场时 无需在做更多校验
            return room;
        }
        // 房间状态变更为占用时
        if (Objects.equals(operateData.getStatus(), RoomStatusEnum.OCCUPATION.getCode())) {
            return room;
        }
        // 当房间为已清场时 清洁开始时间和结束时间不能为空
        if (Objects.isNull(operateData.getBeginTime()) || Objects.isNull(operateData.getEndTime())) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_STATUS_CHANGE_PARAM_ERROR);
        }
        // 当为手动变更时描述不能为空
        if (Objects.equals(operateData.getOperateTypeEnum().getCode(), RoomStatusOperateTypeEnum.AUTO_RECOGNITION.getCode())) {
            return room;
        }
        if (StrUtil.isBlank(operateData.getDesc())) {
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_STATUS_CHANGE_PARAM_ERROR);
        }
        return room;
    }

    private List<RoomTreeNodeVO> weedOutNoRoom(List<RoomTreeNodeVO> roomTreeNodeVOS) {
        List<RoomTreeNodeVO> res = new ArrayList<>();
        for (RoomTreeNodeVO roomTreeNodeVO : roomTreeNodeVOS) {
            if (CollUtil.isEmpty(roomTreeNodeVO.getInfoList()) && CollUtil.isEmpty(roomTreeNodeVO.getChildren())) {
                continue;
            }
            if (CollUtil.isNotEmpty(roomTreeNodeVO.getInfoList())) {
                res.add(roomTreeNodeVO);
            }
            if (CollUtil.isNotEmpty(roomTreeNodeVO.getChildren())) {
                roomTreeNodeVO.setChildren(weedOutNoRoom(roomTreeNodeVO.getChildren()));
                if (CollUtil.isNotEmpty(roomTreeNodeVO.getChildren()) && CollUtil.isEmpty(roomTreeNodeVO.getInfoList())) {
                    res.add(roomTreeNodeVO);
                }
            }
        }
        return res;
    }

    @Override
    public void save3DModel(Long roomId, String modelId) {
        LambdaQueryWrapper<FactoryRoom> ql = new QueryWrapper<FactoryRoom>().lambda();
        ql.eq(FactoryRoom::getThreeDModelId, modelId);
        ql.ne(FactoryRoom::getId, roomId);
        FactoryRoom factoryRoom = factoryRoomMapper.selectOne(ql);
        if (Objects.nonNull(factoryRoom)) {
            throw new BmosException(PlatformResponseCode.ROOM_3D_MODEL_EXIST, factoryRoom.getCode(), factoryRoom.getName());
        }
        FactoryRoom update = factoryRoomMapper.selectById(roomId);
        if (Objects.isNull(update)) {
            throw new BmosException(PlatformResponseCode.ROOM_NOT_EXIST);
        }
        update.setId(roomId);
        update.setThreeDModelId(modelId);
        factoryRoomMapper.updateById(update);
    }

    @Override
    public RoomInfoVO getBy3DModel(String modelId) {
        LambdaQueryWrapper<FactoryRoom> ql = new QueryWrapper<FactoryRoom>().lambda();
        ql.eq(FactoryRoom::getThreeDModelId, modelId);
        ql.eq(FactoryRoom::getDeleted, false);
        FactoryRoom factoryRoom = factoryRoomMapper.selectOne(ql);
        if (Objects.isNull(factoryRoom)) {
            return null;
        }
        return this.getRoomInfo(factoryRoom.getId());
    }


    /**
     * 获取楼层的设备统计信息
     *
     * @param id 楼层id
     * @return 统计信息结果
     */
    @Override
    public TenementFloorEquipmentStatisticsDTO getFloorEquipmentStatistics(Long id) {
        // 1. 查询楼层的房间
        List<FactoryRoom> rooms = factoryRoomMapper.selectList(new QueryWrapper<FactoryRoom>().lambda().eq(FactoryRoom::getFloorId, id));
        TenementFloorEquipmentStatisticsDTO tenementFloorEquipmentStatisticsDTO = new TenementFloorEquipmentStatisticsDTO();
        if (CollUtil.isEmpty(rooms)) {
            return tenementFloorEquipmentStatisticsDTO;
        }
        List<Long> roomIds = rooms.stream().map(FactoryRoom::getId).collect(Collectors.toList());
        // 查询房间绑定的设备
        List<FactoryRoomStation> roomStations = roomStationMapper.selectList(new QueryWrapper<FactoryRoomStation>().lambda().in(FactoryRoomStation::getRoomId, roomIds));
        if (CollUtil.isEmpty(roomStations)) {
            return tenementFloorEquipmentStatisticsDTO;
        }
        List<Long> stationIds = roomStations.stream().map(FactoryRoomStation::getStationId).collect(Collectors.toList());
        List<EquipmentStationInfo> equipmentStationInfos = infoMapper.queryStationInfoByStationIdList(stationIds);
        if (CollUtil.isEmpty(equipmentStationInfos)) {
            return tenementFloorEquipmentStatisticsDTO;
        }
        Set<Long> equipmentIds = equipmentStationInfos.stream().map(EquipmentStationInfo::getEquipmentId).collect(Collectors.toSet());
        List<EquipmentInfo> equipmentInfos = equipmentInfoMapper.selectBatchIds(equipmentIds);
        if (CollUtil.isEmpty(equipmentInfos)) {
            return tenementFloorEquipmentStatisticsDTO;
        }
        equipmentInfos.forEach(equipment -> {
            if (EquipmentStatusCodeEnum.AVAILABLE.getCode().equals(equipment.getStatus())) {
                tenementFloorEquipmentStatisticsDTO.setAvailable(tenementFloorEquipmentStatisticsDTO.getAvailable() + 1);
            }
            if (EquipmentStatusCodeEnum.UNAVAILABLE.getCode().equals(equipment.getStatus())) {
                tenementFloorEquipmentStatisticsDTO.setUnavailable(tenementFloorEquipmentStatisticsDTO.getUnavailable() + 1);
            }
            if (EquipmentStatusCodeEnum.FAULT.getCode().equals(equipment.getStatus())) {
                tenementFloorEquipmentStatisticsDTO.setFault(tenementFloorEquipmentStatisticsDTO.getFault() + 1);
            }
            if (EquipmentStatusCodeEnum.OCCUPY.getCode().equals(equipment.getStatus())) {
                tenementFloorEquipmentStatisticsDTO.setInProduction(tenementFloorEquipmentStatisticsDTO.getInProduction() + 1);
            }
        });
        return tenementFloorEquipmentStatisticsDTO;
    }
}
