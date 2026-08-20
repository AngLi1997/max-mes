package com.bmos.platform.service.factory.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.enums.factory.FactoryModuleEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.factory.dto.LineUseDTO;
import com.bmos.platform.facade.factory.vo.FactoryLineDetailFeignVO;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import com.bmos.platform.facade.factory.vo.LineModuleTreeNodeFeignVO;
import com.bmos.platform.service.factory.controller.FactoryResourceUserVO;
import com.bmos.platform.service.factory.controller.vo.*;
import com.bmos.platform.service.factory.convert.FactoryLineConverter;
import com.bmos.platform.service.factory.convert.FactoryModuleConverter;
import com.bmos.platform.service.factory.convert.FactoryRoomConverter;
import com.bmos.platform.service.factory.convert.FactoryStationConverter;
import com.bmos.platform.service.factory.mapper.*;
import com.bmos.platform.service.factory.mapper.param.LineParam;
import com.bmos.platform.service.factory.model.*;
import com.bmos.platform.service.factory.repository.FactoryStationRepository;
import com.bmos.platform.service.factory.repository.FactoryModuleRepository;
import com.bmos.platform.service.factory.repository.LineRepository;
import com.bmos.platform.service.factory.repository.RoomRepository;
import com.bmos.platform.service.factory.service.LineService;
import com.bmos.platform.service.factory.service.dto.*;
import com.bmos.platform.service.permission.service.ResourcePermissionService;
import com.bmos.platform.service.permission.service.dto.ResourcePermissionSaveDTO;
import com.bmos.platform.service.system.dept.service.DeptService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LineServiceImpl implements LineService {

    @Autowired
    private FactoryLineMapper factoryLineMapper;

    @Autowired
    private FactoryLineRoomMapper factoryLineRoomMapper;

    @Autowired
    private FactoryLineStationMapper factoryLineStationMapper;

    @Autowired
    FactoryStationRepository factoryStationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FactoryModuleRepository factoryModuleRepository;

    @Autowired
    private FactoryRoomMapper factoryRoomMapper;

    @Autowired
    EquipmentStationMapper equipmentStationMapper;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    ResourcePermissionService resourcePermissionService;

    @Autowired
    PlatformApiAdaptor platformApiAdaptor;

    @Autowired
    private DeptService deptService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLine(LineSaveDTO dto) {
        // 1.校验产线编码是否重复
        if (factoryLineMapper.existsByCode(dto.getCode())){
            throw new BmosException(PlatformResponseCode.FACTORY_LINE_CODE_EXISTS);
        }
        FactoryLine factoryLine = FactoryLineConverter.INSTANCE.convert2DO(dto);
        factoryLine.setEnable(Boolean.FALSE);
        SysUser loginUser = SysUserHolder.getUser();
        factoryLine.setOperateId(loginUser.getUserId());
        factoryLine.setOperator(loginUser.getLoginName() + "-" + loginUser.getUserName());
        factoryLine.setOperateTime(LocalDateTime.now());
        factoryLineMapper.insert(factoryLine);

        // 数据权限保存
        resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                .deptIds(dto.getDeptIds()).resourceId(factoryLine.getId()).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLine(LineUpdateDTO dto) {
        FactoryLine factoryLine = factoryLineMapper.selectById(dto.getId());
        if (Objects.isNull(factoryLine)){
            throw new BmosException(PlatformResponseCode.FACTORY_LINE_NOT_EXISTS);
        }
        factoryLine.setName(dto.getName());
        factoryLine.setDescription(dto.getDescription());
        SysUser loginUser = SysUserHolder.getUser();
        factoryLine.setOperateId(loginUser.getUserId());
        factoryLine.setOperator(loginUser.getLoginName() + "-" + loginUser.getUserName());
        factoryLine.setOperateTime(LocalDateTime.now());
        factoryLineMapper.updateById(factoryLine);
        // 更新数据权限
        resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                .deptIds(dto.getDeptIds()).resourceId(factoryLine.getId()).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLine(Long id) {
        // 1.校验产线是否存在
        FactoryLine factoryLine = factoryLineMapper.selectById(id);
        if (Objects.isNull(factoryLine)){
            throw new BmosException(PlatformResponseCode.FACTORY_LINE_NOT_EXISTS);
        }
        // 2.校验产线是否为停用状态
        if (factoryLine.getEnable()){
            throw new BmosException(PlatformResponseCode.FACTORY_LINE_ENABLE);
        }
        // 3. 校验产线使用状态
        this.validDeleteLine(factoryLine);

        SysUser loginUser = SysUserHolder.getUser();
        factoryLine.setOperateId(loginUser.getUserId());
        factoryLine.setOperator(loginUser.getLoginName() + "-" + loginUser.getUserName());
        factoryLine.setOperateTime(LocalDateTime.now());
        factoryLineMapper.deleteById(id);
        // 删除数据权限
        resourcePermissionService.deleteByResourceId(factoryLine.getId());
        // 删除与房间的绑定管
        factoryLineRoomMapper.deleteByLineId(id);
        // 删除与工位的绑定关系
        factoryLineStationMapper.deleteByLineId(id);
    }

    @Override
    public void enableLine(LineEnableDTO dto) {
        FactoryLine factoryLine = factoryLineMapper.selectById(dto.getId());
        if (Objects.isNull(factoryLine)){
            throw new BmosException(PlatformResponseCode.FACTORY_LINE_NOT_EXISTS);
        }
        factoryLine.setEnable(dto.getEnable());

        if (!dto.getEnable()){
            this.validDeleteLine(factoryLine);
        }
        SysUser loginUser = SysUserHolder.getUser();
        factoryLine.setOperateId(loginUser.getUserId());
        factoryLine.setOperator(loginUser.getLoginName() + "-" + loginUser.getUserName());
        factoryLine.setOperateTime(LocalDateTime.now());
        factoryLineMapper.updateById(factoryLine);
    }

    private void validDeleteLine(FactoryLine factoryLine) {
        if (factoryLineStationMapper.existsByLineId(factoryLine.getId()) || factoryLineRoomMapper.existsByLineId(factoryLine.getId())){
            throw new BmosException(PlatformResponseCode.FACTORY_LINE_BIND_EXISTS);
        }
        if (factoryLine.getUseCount() > 0){
            throw new BmosException(PlatformResponseCode.FACTORY_LINE_PROCESS_EXISTS);
        }
    }

    @Override
    public CommonPage<LinePageVO> getLinePage(LinePageDTO dto) {
        LineParam lineParam = FactoryLineConverter.INSTANCE.convert2Param(dto);
        if (Objects.nonNull(dto.getModuleId())){
            List<Long> moduleIdList = factoryModuleRepository.getAllChildModuleId(dto.getModuleId(), FactoryModuleEnum.LINE.getType());
            lineParam.setModuleIdList(moduleIdList);
            lineParam.setModuleId(null);
        }
        // 数据权限
        List<Long> deptIds = new ArrayList<>();
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())){
            deptIds = platformApiAdaptor.deptIds();
            if (CollUtil.isEmpty(deptIds)){
                return CommonPage.convertPage(new ArrayList<>());
            }
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        lineParam.setDeptIds(deptIds);
        List<FactoryLine> factoryLineList = factoryLineMapper.selectByParam(lineParam);
        if (CollectionUtil.isEmpty(factoryLineList)){
            return CommonPage.convertPage(new ArrayList<>());
        }
        CommonPage<FactoryLine> factoryLineCommonPage = CommonPage.convertPage(factoryLineList);
        List<Long> lineIdList = factoryLineList.stream().map(FactoryLine::getId).collect(Collectors.toList());
        List<FactoryLineStation> lineStations = factoryLineStationMapper.selectByLineIdList(lineIdList);
        Map<Long, List<Long>> lineStationIdMap = lineStations.stream().collect(Collectors.groupingBy(FactoryLineStation::getLineId, Collectors.mapping(FactoryLineStation::getStationId, Collectors.toList())));

        List<FactoryLineRoom> lineRoomList = factoryLineRoomMapper.selectByLineIdList(lineIdList);
        Map<Long, List<Long>> lineRoomIdMap = lineRoomList.stream().collect(Collectors.groupingBy(FactoryLineRoom::getLineId, Collectors.mapping(FactoryLineRoom::getRoomId, Collectors.toList())));
        List<LinePageVO> pageVOList = FactoryModuleConverter.INSTANCE.convert2LinePageVO(factoryLineList, lineStationIdMap, lineRoomIdMap);
        return CommonPage.CommonPage(pageVOList, Long.valueOf(factoryLineCommonPage.getTotal()), dto);
    }

    @Override
    public LineInfoVO getLineInfo(Long id) {
        FactoryLine factoryLine = factoryLineMapper.selectById(id);
        if (Objects.isNull(factoryLine)){
            throw new BmosException(PlatformResponseCode.FACTORY_LINE_NOT_EXISTS);
        }
        // 查询该产线下所绑定的所有工位
        List<FactoryLineStation> factoryLineStationList = factoryLineStationMapper.selectByLineId(id);
        List<CodeNameVO> stationNameList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(factoryLineStationList)){
            // 查询该产线下所绑定的所有工位名称
            List<EquipmentStation> equipmentStations = factoryStationRepository.selectByStationIdList(
                    factoryLineStationList.stream().map(FactoryLineStation::getStationId).collect(Collectors.toList()));
            stationNameList = equipmentStations.stream().map(item->BeanUtil.copyProperties(item,CodeNameVO.class)).collect(Collectors.toList());
        }
        // 查询该查险下所绑定的所有房间
        List<FactoryLineRoom> factoryLineRoomList = factoryLineRoomMapper.selectByLineId(id);
        List<CodeNameVO> roomNameList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(factoryLineRoomList)){
            // 查询该产线下所绑定的所有房间名称
            List<FactoryRoom> roomList = roomRepository.selectByIdList(
                    factoryLineRoomList.stream().map(FactoryLineRoom::getRoomId).collect(Collectors.toList()));
            roomNameList = roomList.stream().map(item->BeanUtil.copyProperties(item,CodeNameVO.class)).collect(Collectors.toList());
        }
        LineInfoVO lineInfoVO = FactoryLineConverter.INSTANCE.convert2LineVO(factoryLine, stationNameList, roomNameList);
        if (Objects.equals(TreeUtil.parentId, factoryLine.getModuleId())){
            return lineInfoVO;
        }
        FactoryModule factoryModule = factoryModuleRepository.selectById(factoryLine.getModuleId());
        lineInfoVO.setModuleName(factoryModule.getName());
        lineInfoVO.setModuleCode(factoryModule.getCode());
        return lineInfoVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindRoom(LineBindRoomDTO dto) {
        // 先删除之前的绑定关系
        factoryLineRoomMapper.deleteByLineId(dto.getId());
        if (CollectionUtil.isEmpty(dto.getRoomIdList())){
            return ;
        }
        // 绑定房间
        List<FactoryLineRoom> factoryLineRoomList = FactoryLineConverter.INSTANCE.convert2BindDO(dto);
        if (CollectionUtil.isEmpty(factoryLineRoomList)){
            return ;
        }
        factoryLineRoomMapper.insertBatch(factoryLineRoomList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindStation(LineBindStationDTO dto) {
        // 先删除之前的绑定关系
        factoryLineStationMapper.deleteByLineId(dto.getId());
        // 绑定工位
        if (CollectionUtil.isEmpty(dto.getStationIdList())){
            return ;
        }
        // 校验工位是否绑在另一个产线下/另一个房间下
        Map<Long, FactoryRoom> roomStationMap = roomRepository.stationBindRoom(dto.getStationIdList());
        // 查询工位与产线的绑定关系
        Map<Long, FactoryLine> lineStationMap = lineRepository.stationBindLine(dto.getStationIdList());
        Set<Long> equipmentIdSet = new HashSet<>();
        if (CollUtil.isNotEmpty(lineStationMap)){
            equipmentIdSet.addAll(lineStationMap.keySet());
        }
        if (CollUtil.isNotEmpty(roomStationMap)){
            equipmentIdSet.addAll(roomStationMap.keySet());
        }
        List<EquipmentStation> equipmentStations = new ArrayList<>();
        if (CollUtil.isNotEmpty(equipmentIdSet)){
            equipmentStations = equipmentStationMapper.selectBatchIds(equipmentIdSet);
        }

        for (EquipmentStation equipmentStation : equipmentStations) {
            FactoryRoom factoryRoom = roomStationMap.get(equipmentStation.getId());
            FactoryLine factoryLine = lineStationMap.get(equipmentStation.getId());
            if (Objects.nonNull(factoryLine)){
                throw new BmosException(PlatformResponseCode.FACTORY_STATION_ALREADY_BIND_LINE_TEMPLATE, equipmentStation.getCode() + "-" + equipmentStation.getName(), factoryLine.getName());
            }
            if (Objects.nonNull(factoryRoom)){
                throw new BmosException(PlatformResponseCode.FACTORY_STATION_ALREADY_BIND_ROOM_TEMPLATE, equipmentStation.getCode() + "-" + equipmentStation.getName(), factoryRoom.getName());
            }
        }
        List<FactoryLineStation> factoryLineStationList = FactoryLineConverter.INSTANCE.convert2BindDO(dto);
        if (CollectionUtil.isEmpty(factoryLineStationList)){
            return ;
        }
        factoryLineStationMapper.insertBatch(factoryLineStationList);
    }

    @Override
    public List<FactoryLineFeignVO> getLineByCondition(String name) {
        LineParam lineParam = new LineParam();
        lineParam.setName(name);
        lineParam.setEnable(true);
        lineParam.setMobile(true);
        List<FactoryLine> factoryLineList = factoryLineMapper.selectByParam(lineParam);
        return FactoryLineConverter.INSTANCE.convert2FeignVO(factoryLineList);
    }

    @Override
    public List<FactoryLineDetailFeignVO> getLineDetailByLineIds(Collection<Long> lineIds, boolean stationFlag) {
        // 查询产线集合
        if (CollectionUtil.isEmpty(lineIds)){
            return new ArrayList<>();
        }
        List<FactoryLine> factoryLineList = factoryLineMapper.selectBatchIds(lineIds);
        if (CollectionUtil.isEmpty(factoryLineList)){
            return new ArrayList<>();
        }
        // 查询产线下绑定的房间信息
        List<FactoryLineRoom> factoryLineRoomList = factoryLineRoomMapper.selectByLineIdList(lineIds);
        List<Long> roomIdList = new ArrayList<>();
        // 产线下绑定的房间信息
        Map<Long, List<FactoryRoom>> lineRoomMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(factoryLineRoomList)){
            roomIdList = factoryLineRoomList.stream().map(FactoryLineRoom::getRoomId).collect(Collectors.toList());
            List<FactoryRoom> roomList = roomRepository.selectByIdList(roomIdList);
            Map<Long, List<FactoryLineRoom>> roomLineMap = factoryLineRoomList.stream().collect(Collectors.groupingBy(FactoryLineRoom::getRoomId));
            for (FactoryRoom factoryRoom : roomList) {
                if (!roomLineMap.containsKey(factoryRoom.getId())){
                    continue;
                }
                List<FactoryLineRoom> lineRoomList = roomLineMap.get(factoryRoom.getId());
                for (FactoryLineRoom factoryLineRoom : lineRoomList) {
                    if (!lineRoomMap.containsKey(factoryLineRoom.getLineId())){
                        lineRoomMap.put(factoryLineRoom.getLineId(), Lists.newArrayList(factoryRoom));
                    } else {
                        lineRoomMap.get(factoryLineRoom.getLineId()).add(factoryRoom);
                    }
                }
            }
        }
        // 产线下绑定的工位信息
        Map<Long, List<EquipmentStation>> lineStationMap = new HashMap<>();
        // 产线下绑定的房间与工位的关系
        Map<Long, List<EquipmentStation>> roomStationMap = new HashMap<>();
        if (!stationFlag){
            return FactoryLineConverter.INSTANCE.convert2LineDetailVO(factoryLineList, lineRoomMap, lineStationMap, roomStationMap);
        }
        // 产线与工位之间的关系
        List<FactoryLineStation> factoryLineStationList = factoryLineStationMapper.selectByLineIdList(lineIds);
        // 房间与工位的关系
        List<FactoryRoomStation> roomStationList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(roomIdList)){
            roomStationList = roomRepository.selectBindStationByRoomIdList(roomIdList);
        }
        if (CollectionUtil.isEmpty(factoryLineStationList) && CollectionUtil.isEmpty(roomStationList)){
            return FactoryLineConverter.INSTANCE.convert2LineDetailVO(factoryLineList, lineRoomMap, lineStationMap, roomStationMap);
        }
        Map<Long, FactoryLineStation> stationLineMap = new HashMap<>();
        List<Long> stationIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(factoryLineStationList)){
            stationIdList.addAll(factoryLineStationList.stream().map(FactoryLineStation::getStationId).collect(Collectors.toList()));
            stationLineMap = factoryLineStationList.stream().collect(Collectors.toMap(FactoryLineStation::getStationId, Function.identity()));
        }
        Map<Long, FactoryRoomStation> stationRoomMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(roomStationList)){
            stationIdList.addAll(roomStationList.stream().map(FactoryRoomStation::getStationId).collect(Collectors.toList()));
            stationRoomMap = roomStationList.stream().collect(Collectors.toMap(FactoryRoomStation::getStationId, Function.identity()));
        }
        List<EquipmentStation> equipmentStationList = factoryStationRepository.selectByStationIdList(stationIdList);
        for (EquipmentStation equipmentStation : equipmentStationList) {
            if (stationLineMap.containsKey(equipmentStation.getId())){
                FactoryLineStation factoryLineStation = stationLineMap.get(equipmentStation.getId());
                lineStationMap.computeIfAbsent(factoryLineStation.getLineId(), k -> new ArrayList<>()).add(equipmentStation);
            }
            if (stationRoomMap.containsKey(equipmentStation.getId())){
                FactoryRoomStation factoryRoomStation = stationRoomMap.get(equipmentStation.getId());
                roomStationMap.computeIfAbsent(factoryRoomStation.getRoomId(), k -> new ArrayList<>()).add(equipmentStation);
            }
        }
        return FactoryLineConverter.INSTANCE.convert2LineDetailVO(factoryLineList, lineRoomMap, lineStationMap, roomStationMap);
    }

    @Override
    public List<Long> selectStationIdByLineId(Long lineId) {
        // 查询产线下绑定的房间信息
        List<Long> stationIdList = new ArrayList<>();
        List<FactoryLineRoom> factoryLineRoomList = factoryLineRoomMapper.selectByLineId(lineId);
        if (CollectionUtil.isNotEmpty(factoryLineRoomList)){
            List<Long> roomIdList = factoryLineRoomList.stream().map(FactoryLineRoom::getRoomId).collect(Collectors.toList());
            List<FactoryRoomStation> roomStationList = roomRepository.selectBindStationByRoomIdList(roomIdList);
            if (CollectionUtil.isNotEmpty(roomStationList)){
                stationIdList.addAll(roomStationList.stream().map(FactoryRoomStation::getStationId).collect(Collectors.toList()));
            }
        }
        List<FactoryLineStation> factoryLineStationList = factoryLineStationMapper.selectByLineId(lineId);
        if (CollectionUtil.isNotEmpty(factoryLineStationList)){
            stationIdList.addAll(factoryLineStationList.stream().map(FactoryLineStation::getStationId).collect(Collectors.toList()));
        }
        return stationIdList;
    }

    @Override
    @DistributedLock(key = "roomBindUseCount")
    @Transactional(rollbackFor = Exception.class)
    public void bindUseCount(LineUseDTO dto) {
        // 产线绑定
        if (CollectionUtil.isNotEmpty(dto.getLineUseMap())){
            this.useLineCount(dto.getLineUseMap());
        }
        // 房间绑定
        if (CollectionUtil.isNotEmpty(dto.getRoomUseMap())){
            roomRepository.useRoomCount(dto.getRoomUseMap());
        }
        // 工位绑定
        if (CollectionUtil.isNotEmpty(dto.getStationUseMap())){
            factoryStationRepository.useStationCount(dto.getStationUseMap());
        }
    }

    @Override
    public List<LineModuleTreeNodeFeignVO> getLineModuleTreeVO() {
        List<FactoryModule> factoryModules = factoryModuleRepository.selectListByType(FactoryModuleEnum.LINE.getType());
        if (CollectionUtil.isEmpty(factoryModules)){
            return new ArrayList<>();
        }
        List<Long> moduleIdList = factoryModules.stream().map(FactoryModule::getId).collect(Collectors.toList());
        List<FactoryLine> lineList = factoryLineMapper.selectByParam(LineParam.builder().moduleIdList(moduleIdList).build());
        Map<Long, List<FactoryLine>> lineMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(lineList)){
            lineMap = lineList.stream().collect(Collectors.groupingBy(FactoryLine::getModuleId));
        }
        return FactoryLineConverter.INSTANCE.convert2LineModuleTreeFeignVO(factoryModules, lineMap);
    }

    @Override
    public List<FactoryLineDetailFeignVO> queryLineDetailListByLineIds(List<Long> lineIdList) {
        // 查询产线集合
        if (CollectionUtil.isEmpty(lineIdList)){
            return new ArrayList<>();
        }
        List<FactoryLine> factoryLineList = factoryLineMapper.selectBatchIds(lineIdList);
        if (CollectionUtil.isEmpty(factoryLineList)){
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(factoryLineList,FactoryLineDetailFeignVO.class);
    }

    @Override
    public List<FactoryLineFeignVO> queryLineListByLineIds(List<Long> lineIdList) {
        if (CollUtil.isEmpty(lineIdList)){
            return new ArrayList<>();
        }
        return factoryLineMapper.queryLineListByLineIds(lineIdList);
    }

    @Override
    public FactoryResourceUserVO getLineByUser(String userId) {
        // 根据用户id获取当前用户所在部门及其所有子部门
        List<Long> deptIdList = deptService.getDeptByUserId(userId);
        if (CollUtil.isEmpty(deptIdList)){
            return new FactoryResourceUserVO(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }
        // 查询具有数据权限的产线信息
        List<FactoryTreeNodeVO> lineInfoVOS = this.findAuthLineInfo(deptIdList);
        Map<Long, FactoryTreeNodeVO> lineInfoMap = lineInfoVOS.stream().collect(Collectors.toMap(FactoryTreeNodeVO::getId, Function.identity()));
        // 获取具有数据权限的房间信息
        List<FactoryTreeNodeVO> userRoomInfoVOS = this.findAuthRoomLineInfo(deptIdList);
        // 查询房间与产线的绑定关系
        Map<Long, List<Long>> roomLineMap = this.findRoomLine(userRoomInfoVOS);

        List<FactoryTreeNodeVO> notInLineRoomInfoVOS = new ArrayList<>();
        // 工位与产线之间的绑定关系
        Map<Long, Long> staionLineMap = new HashMap<>();
        Set<Long> lineIdList = new HashSet<>();
        if (CollUtil.isNotEmpty(lineInfoVOS)){
            lineIdList = lineInfoVOS.stream().map(FactoryTreeNodeVO::getId).collect(Collectors.toSet());
            List<FactoryLineStation> lineStationList = lineRepository.selectByLineIdList(lineIdList);
            staionLineMap = CollectionUtils.convertMap(lineStationList, FactoryLineStation::getStationId, FactoryLineStation::getLineId);
        }
        // 获取前端需要展示的所有的房间信息
        Map<Long, FactoryTreeNodeVO> userRoomInfoVOMap = new HashMap<>();
        for (FactoryTreeNodeVO userRoomInfoVO : userRoomInfoVOS) {
            if (!roomLineMap.containsKey(userRoomInfoVO.getId())){
                // 代表当前房间没有绑定任何的产线
                userRoomInfoVOMap.put(userRoomInfoVO.getId(), userRoomInfoVO);
                notInLineRoomInfoVOS.add(userRoomInfoVO);
                continue;
            }
            List<Long> curRoomLineIdList = roomLineMap.get(userRoomInfoVO.getId());
            for (Long curRoomLineId : curRoomLineIdList) {
                if (!lineIdList.contains(curRoomLineId)){
                    continue;
                }
                FactoryTreeNodeVO userLineInfoVO = lineInfoMap.get(curRoomLineId);
                if (Objects.isNull(userLineInfoVO)){
                    continue;
                }
                userRoomInfoVOMap.put(userRoomInfoVO.getId(), userRoomInfoVO);
                List<FactoryTreeNodeVO> roomInfoVOList = Objects.nonNull(userLineInfoVO.getChildren()) ? userLineInfoVO.getChildren() : new ArrayList<>();
                roomInfoVOList.add(userRoomInfoVO);
                userLineInfoVO.setChildren(roomInfoVOList);
            }
        }
        Map<Long, Long> staionRoomMap = new HashMap<>();
        if (CollUtil.isNotEmpty(userRoomInfoVOMap)){
            List<FactoryRoomStation> factoryRoomStations = roomRepository.selectBindStationByRoomIdList(new ArrayList<>(userRoomInfoVOMap.keySet()));
            staionRoomMap = CollectionUtils.convertMap(factoryRoomStations, FactoryRoomStation::getStationId, FactoryRoomStation::getRoomId);
        }
        List<Long> stationIdList = Lists.newArrayList(staionRoomMap.keySet());
        stationIdList.addAll(staionLineMap.keySet());
        List<EquipmentStation> equipmentStations = factoryStationRepository.selectByStationIdList(stationIdList);
        equipmentStations.sort(Comparator.comparing(EquipmentStation::getCreateTime).reversed());
        // 把工位填充进房间
        List<Long> allStationIdList = new ArrayList<>();
        for (EquipmentStation equipmentStation : equipmentStations) {
            if (!equipmentStation.getEnable()){
                // 不可用工位不展示
                continue;
            }
            if (staionRoomMap.containsKey(equipmentStation.getId())){
                // 代表当前工位在房间下
                FactoryTreeNodeVO roomInfoVO = userRoomInfoVOMap.get(staionRoomMap.get(equipmentStation.getId()));
                if (Objects.nonNull(roomInfoVO)){
                     List<FactoryTreeNodeVO> children = Objects.nonNull(roomInfoVO.getChildren()) ? roomInfoVO.getChildren() : new ArrayList<>();
                    children.add(FactoryStationConverter.INSTANCE.convert2StationTreeNodeVO(equipmentStation));
                    roomInfoVO.setChildren(children);
                }
                allStationIdList.add(equipmentStation.getId());
            } else if (staionLineMap.containsKey(equipmentStation.getId())){
                FactoryTreeNodeVO lineInfoVO = lineInfoMap.get(staionLineMap.get(equipmentStation.getId()));
                if (Objects.isNull(lineInfoVO)){
                    continue;
                }
                List<FactoryTreeNodeVO> children = Objects.nonNull(lineInfoVO.getChildren()) ? lineInfoVO.getChildren() : new ArrayList<>();
                children.add(FactoryStationConverter.INSTANCE.convert2StationTreeNodeVO(equipmentStation));
                lineInfoVO.setChildren(children);
                allStationIdList.add(equipmentStation.getId());
            }
        }
        List<FactoryTreeNodeVO> collect = notInLineRoomInfoVOS.stream().filter(item -> CollUtil.isNotEmpty(item.getChildren())).collect(Collectors.toList());
        // 为每一个FactoryTreeNodeVO赋予一个唯一标识
        List<FactoryTreeNodeVO> weedOutLineList = weedOutNotStationLine(lineInfoVOS);
        List<FactoryTreeNodeVO> uniqueLineList = fillUnique(weedOutLineList, null);
        List<FactoryTreeNodeVO> uniqueRoomList = fillUnique(collect, null);
        return new FactoryResourceUserVO(uniqueLineList, uniqueRoomList, allStationIdList);
    }

    private List<FactoryTreeNodeVO> fillUnique(List<FactoryTreeNodeVO> weedOutLineList, FactoryTreeNodeVO root) {
        if (CollUtil.isEmpty(weedOutLineList)){
            return new ArrayList<>();
        }
        List<FactoryTreeNodeVO> copyList = JSON.parseArray(JSON.toJSONString(weedOutLineList), FactoryTreeNodeVO.class);
        for (FactoryTreeNodeVO factoryTreeNodeVO : copyList) {
            if (Objects.isNull(root)){
                factoryTreeNodeVO.setUniqueId(StrUtil.toString(factoryTreeNodeVO.getId()));
            }else {
                factoryTreeNodeVO.setUniqueId(StrUtil.toString(root.getUniqueId() + StrUtil.DASHED + factoryTreeNodeVO.getId()));
            }
            List<FactoryTreeNodeVO> newChild = fillUnique(factoryTreeNodeVO.getChildren(), factoryTreeNodeVO);
            factoryTreeNodeVO.setChildren(newChild);
        }
        return copyList;
    }

    private List<FactoryTreeNodeVO> weedOutNotStationLine(List<FactoryTreeNodeVO> lineInfoVOS) {
        if (CollUtil.isEmpty(lineInfoVOS)){
            return new ArrayList<>();
        }
        List<FactoryTreeNodeVO> result = new ArrayList<>();
        for (FactoryTreeNodeVO lineInfoVO : lineInfoVOS) {
            List<FactoryTreeNodeVO> children = lineInfoVO.getChildren();
            if (CollUtil.isEmpty(children)){
                continue;
            }
            for (FactoryTreeNodeVO child : children) {
                if (CollUtil.isNotEmpty(child.getChildren()) || FactoryModuleEnum.STATION.getType().equals(child.getType())){
                    result.add(lineInfoVO);
                    break;
                }
            }
        }
        return result;
    }

    private Map<Long, List<Long>> findRoomLine(List<FactoryTreeNodeVO> userRoomInfoVOS) {
        Map<Long, List<Long>> lineRoomMap = new HashMap<>();
        if (CollUtil.isEmpty(userRoomInfoVOS)){
            return lineRoomMap;
        }
        // 获取这些房间与产线的绑定关系
        List<Long> roomIdList = userRoomInfoVOS.stream().map(FactoryTreeNodeVO::getId).collect(Collectors.toList());
        List<FactoryLineRoom> factoryLineRooms = lineRepository.selectRelationByRoomIdList(roomIdList);
        return CollectionUtils.convertMultiMap(factoryLineRooms, FactoryLineRoom::getRoomId, FactoryLineRoom::getLineId);
    }

    /**
     * 获取具有数据权限的房间信息
     * @param deptIdList
     * @return
     */
    private List<FactoryTreeNodeVO> findAuthRoomLineInfo(List<Long> deptIdList) {
        List<FactoryRoom> roomList = roomRepository.selectByDeptIdList(deptIdList);
        // 剔除禁用状态的房间
        roomList = roomList.stream().filter(FactoryRoom::getEnable).collect(Collectors.toList());
        roomList.sort(Comparator.comparing(FactoryRoom::getCreateTime).reversed());
        return FactoryRoomConverter.INSTANCE.convert2UserRoomInfoVOList(roomList);
    }

    /**
     * 查询具有数据权限的产线信息
     * @param deptIdList
     * @return
     */
    private List<FactoryTreeNodeVO> findAuthLineInfo(List<Long> deptIdList) {
        // 获取具有数据权限的所有产线信息
        List<FactoryLine> factoryLines = factoryLineMapper.selectByParam(LineParam.builder().deptIds(deptIdList).build());
        // 剔除禁用状态的产线
        factoryLines = factoryLines.stream().filter(FactoryLine::getEnable).collect(Collectors.toList());
        // 根据factoryLines中的createTime进行倒序排序
        factoryLines.sort(Comparator.comparing(FactoryLine::getCreateTime).reversed());
        return FactoryLineConverter.INSTANCE.convert2UserLineInfoVOList(factoryLines);
    }

    /**
     * 绑定/解绑产线
     * @param lineUseMap
     */
    private void useLineCount(Map<Long, Boolean> lineUseMap) {
        Set<Long> lineIds = lineUseMap.keySet();
        List<FactoryLine> factoryLineList = factoryLineMapper.selectBatchIds(lineIds);
        if (CollectionUtil.isEmpty(factoryLineList)){
            throw new BmosException(PlatformResponseCode.FACTORY_LINE_NOT_EXISTS);
        }
        for (FactoryLine factoryLine : factoryLineList) {
            Boolean curUse = lineUseMap.get(factoryLine.getId());
            if (factoryLine.getUseCount() > 0 && !curUse){
                factoryLine.setUseCount(factoryLine.getUseCount() - 1);
            } else {
                factoryLine.setUseCount(factoryLine.getUseCount() + 1);
            }
        }
        factoryLineMapper.updateBatch(factoryLineList);
    }
}
