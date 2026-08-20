package com.bmos.platform.service.equipment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.cache.redis.RedisService;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.enums.PlatformRedisKeyDefine;
import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import com.bmos.platform.common.enums.equipment.PropertyTypeEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.equipment.dto.EquipmentApplyHeartDTO;
import com.bmos.platform.facade.equipment.dto.EquipmentQueryDTO;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.facade.equipment.enums.EquipmentTagCodeEnum;
import com.bmos.platform.facade.equipment.enums.TagEquipmentPropertyCodeEnum;
import com.bmos.platform.facade.equipment.vo.EquipmentVO;
import com.bmos.platform.facade.factory.vo.FactoryRoomFeignVO;
import com.bmos.platform.service.equipment.controller.vo.*;
import com.bmos.platform.service.equipment.convert.EquipmentConvert;
import com.bmos.platform.service.equipment.convert.EquipmentInfoConvert;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import com.bmos.platform.service.equipment.mapper.*;
import com.bmos.platform.service.equipment.model.*;
import com.bmos.platform.service.equipment.service.*;
import com.bmos.platform.service.equipment.service.data.EquipmentStatusLogData;
import com.bmos.platform.service.equipment.service.data.EquipmentTagData;
import com.bmos.platform.service.equipment.service.data.EquipmentTagStatusData;
import com.bmos.platform.service.equipment.service.data.TagData;
import com.bmos.platform.service.equipment.service.dto.*;
import com.bmos.platform.service.factory.mapper.*;
import com.bmos.platform.service.factory.model.*;
import com.bmos.platform.service.factory.service.EquipmentStationInfoService;
import com.bmos.platform.service.factory.service.EquipmentStationUserService;
import com.bmos.platform.service.factory.service.FactoryStationService;
import com.bmos.platform.service.factory.service.LineService;
import com.bmos.platform.service.factory.service.impl.EquipmentStationUserServiceImpl;
import com.bmos.platform.service.system.user.converter.DateConverter;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Service
public class EquipmentInfoServiceImpl implements EquipmentInfoService {

    private static final String DICT_CODE_EQUIPMENT_DATA = "DeviceDataFields";
    private static final String DICT_CODE_EQUIPMENT_INFO = "DeviceInformationFields";

    @Autowired
    private EquipmentInfoMapper infoMapper;

    @Autowired
    private EquipmentTagService tagService;

    @Autowired
    private EquipmentCategoryMapper categoryMapper;

    @Autowired
    private EquipmentStationInfoMapper equipmentStationInfoMapper;

    @Autowired
    private EquipmentStationInfoService stationInfoService;

    @Autowired
    private EquipmentPropertyInfoMapper propertyInfoMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    EquipmentTagPropertyMapper tagPropertyMapper;


    @Autowired
    EquipmentTagService equipmentTagService;

    @Autowired
    EquipmentLogService equipmentLogService;

    @Autowired
    LineService lineService;


    @Autowired
    EquipmentTagInfoMapper equipmentTagInfoMapper;

    @Autowired
    EquipmentStatusHandler equipmentStatusHandler;


    @Autowired
    EquipmentPropertyInfoMapper equipmentPropertyInfoMapper;

    @Autowired
    private AcquisitionPointService acquisitionPointService;

    @Autowired
    private EquipmentUseTemplateMapper equipmentUseTemplateMapper;

    @Autowired
    private FactoryStationService factoryStationService;

    @Autowired
    private EquipmentStationInfoService equipmentStationInfoService;

    @Autowired
    private FactoryRoomStationMapper roomStationMapper;

    @Autowired
    private FactoryRoomMapper roomMapper;

    @Autowired
    private FactoryLineStationMapper lineStationMapper;

    @Autowired
    private FactoryLineRoomMapper lineRoomMapper;

    @Autowired
    private FactoryLineMapper lineMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveEquipment(EquipmentSaveDTO dto) {
        List<EquipmentInfo> infoList = infoMapper.selectInfoList();
        if (CollectionUtils.convertList(infoList, EquipmentInfo::getCode).contains(dto.getCode())) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_CODE_ERROR);
        }
        EquipmentInfo equipmentInfo = EquipmentInfoConvert.INSTANCE.convertToInfo(dto);
        equipmentInfo.setId(IdUtils.getSnowflake());
        this.saveEquipmentTag(equipmentInfo, dto.getTagIdList(), dto.getTagEquipmentStatusDTOList(),
                dto.getEquipmentPropertyDTOList(), dto.getEquipmentDataPropertyDTOList());
        infoMapper.saveOrUpdateInfo(equipmentInfo);
        return equipmentInfo.getId();
    }

    private void saveEquipmentTag(EquipmentInfo equipmentInfo, List<Long> tagIdList,
                                  List<EquipmentStatusDTO> tagEquipmentStatusDTOList,
                                  List<EquipmentPropertyDTO> equipmentPropertyDTOList,
                                  List<EquipmentPropertyDTO> equipmentDataPropertyDTOList) {
        // 保存设备状态与标签之间的关联关系
        // 先删除设备与标签之间的关联关系
        equipmentTagInfoMapper.deleteByEquipmentId(equipmentInfo.getId());
        List<EquipmentTagInfo> equipmentTagInfoList = new ArrayList<>();
        for (Long tagId : tagIdList) {
            EquipmentTagInfo equipmentTagInfo = new EquipmentTagInfo();
            equipmentTagInfo.setEquipmentId(equipmentInfo.getId());
            equipmentTagInfo.setTagId(tagId);
            equipmentTagInfoList.add(equipmentTagInfo);
        }
        if (CollectionUtil.isNotEmpty(equipmentTagInfoList)) {
            equipmentTagInfoMapper.insertBatch(equipmentTagInfoList);
        }
        // 删除设备与属性之间的关联关系
        equipmentPropertyInfoMapper.deleteByEquipmentId(equipmentInfo.getId());
        // 保存设备与属性(包含设备状态)之间的关联关系且根据规则计算设备最终状态与设别有效期
        if (CollectionUtil.isNotEmpty(equipmentPropertyDTOList)) {
            this.saveEquipmentDataProperty(equipmentInfo, equipmentPropertyDTOList,
                    PropertyTypeEnum.TAG_PROPERTY);
        }
        if (CollectionUtil.isNotEmpty(equipmentDataPropertyDTOList)) {
            this.saveEquipmentDataProperty(equipmentInfo, equipmentDataPropertyDTOList,
                    PropertyTypeEnum.TAG_DATA_PROPERTY);
        }
        if (CollectionUtil.isNotEmpty(tagEquipmentStatusDTOList)) {
            this.saveEquipmentStatusProperty(equipmentInfo, tagEquipmentStatusDTOList);
        } else {
            equipmentInfo.setStatus(EquipmentStatusCodeEnum.AVAILABLE.getCode());
        }
    }

    private void saveEquipmentDataProperty(EquipmentInfo equipmentInfo,
                                           List<EquipmentPropertyDTO> equipmentDataPropertyDTOList,
                                           PropertyTypeEnum propertyTypeEnum) {
        // 通过配置状态的code分组
        Map<String, List<EquipmentPropertyDTO>> codeGroup =
                equipmentDataPropertyDTOList.stream().collect(Collectors.groupingBy(EquipmentPropertyDTO::getCode));
        List<String> repeatCodeList =
                codeGroup.entrySet().stream().filter(entry -> entry.getValue().size() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(repeatCodeList)) {
            throw new BmosException(PlatformResponseCode.PROPERTY_CODE_REPEAT,
                    String.join(",", repeatCodeList));
        }
        List<EquipmentPropertyInfo> equipmentPropertyInfoList = new ArrayList<>();
        equipmentDataPropertyDTOList.forEach(propertyDTO -> {
            EquipmentPropertyInfo equipmentPropertyInfo =
                    EquipmentConvert.INSTANCE.convert2EquipmentPropertyInfo(propertyDTO, equipmentInfo.getId());
            equipmentPropertyInfo.setPropertyType(propertyTypeEnum.getCode());
            equipmentPropertyInfoList.add(equipmentPropertyInfo);
        });
        equipmentPropertyInfoMapper.insertBatch(equipmentPropertyInfoList);
    }


    private void saveEquipmentStatusProperty(EquipmentInfo equipmentInfo,
                                             List<EquipmentStatusDTO> tagEquipmentStatusDTOList) {
        // 通过配置状态的code分组
        Map<String, List<EquipmentStatusDTO>> codeGroup =
                tagEquipmentStatusDTOList.stream().collect(Collectors.groupingBy(EquipmentStatusDTO::getCode));
        List<String> repeatCodeList =
                codeGroup.entrySet().stream().filter(entry -> entry.getValue().size() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(repeatCodeList)) {
            throw new BmosException(PlatformResponseCode.PROPERTY_CODE_REPEAT,
                    String.join(",", repeatCodeList));
        }

        List<EquipmentPropertyInfo> equipmentPropertyInfoList = new ArrayList<>();
        List<EquipmentTagStatusData> equipmentTagStatusDataList = new ArrayList<>();
        for (EquipmentStatusDTO equipmentStatusDTO : tagEquipmentStatusDTOList) {
            EquipmentPropertyInfo equipmentPropertyInfo =
                    EquipmentConvert.INSTANCE.convert2EquipmentPropertyInfo(equipmentStatusDTO,
                            equipmentInfo.getId());
            EquipmentTagStatusData equipmentTagStatusData =
                    EquipmentConvert.INSTANCE.convertEquipmentStatusData(equipmentPropertyInfo);
            if (equipmentPropertyInfo.getFinishStatus()) {
                // 计算有效期
                String value = equipmentStatusDTO.getValue();
                String[] split = value.split(",");
                Duration duration =
                        Duration.ofDays(Long.parseLong(split[0]))
                                .plusHours(Long.parseLong(split[1]))
                                .plusMinutes(Long.parseLong(split[2]))
                                .plusSeconds(Long.parseLong(split[3]));
                LocalDateTime expireDate = LocalDateTime.now().plus(duration);
                equipmentTagStatusData.setExpireDateTime(expireDate);
                equipmentPropertyInfo.setActualValue(LocalDateTimeUtil.format(expireDate, EquipmentConvert.pattern));
            }
            equipmentTagStatusDataList.add(equipmentTagStatusData);
            equipmentPropertyInfoList.add(equipmentPropertyInfo);
        }
        // 计算设备状态
        equipmentStatusHandler.analyzeEffectiveReleaseEquipment(equipmentTagStatusDataList, equipmentInfo);
        equipmentPropertyInfoMapper.insertBatch(equipmentPropertyInfoList);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEquipment(EquipmentUpdateDTO dto) {
        List<EquipmentInfo> infoList = infoMapper.selectInfoList();
        List<EquipmentInfo> infos = infoList.stream()
                .filter(info -> StrUtil.equals(info.getCode(), dto.getCode())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(infos) && !infos.stream().findFirst().get().getId().equals(dto.getId())) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NAME_ERROR);
        }
        EquipmentInfo equipmentInfo = EquipmentInfoConvert.INSTANCE.convertToUpdateInfo(dto);
        this.saveEquipmentTag(equipmentInfo, dto.getTagIdList(), dto.getTagEquipmentStatusDTOList(),
                dto.getEquipmentPropertyDTOList(), dto.getEquipmentDataPropertyDTOList());
        infoMapper.saveOrUpdateInfo(equipmentInfo);
    }


    @Override
    public void deleteEquipment(Long id) {
        EquipmentInfo equipmentInfo = infoMapper.queryInfoById(id);
        if (equipmentInfo.getEnable()) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_DELETE_ERROR);
        }
        infoMapper.deleteEquipment(id);
        tagService.deleteByEquipmentId(id);
    }

    @Override
    public EquipmentInfoVO getEquipmentInfo(Long id) {
        EquipmentInfo equipmentInfo = infoMapper.queryInfoById(id);
        if (ObjectUtil.isEmpty(equipmentInfo)) {
            return null;
        }
        EquipmentInfoVO vo = EquipmentInfoConvert.INSTANCE.convertToInfoVo(equipmentInfo);
        EquipmentTagData equipmentProperty = tagService.getEquipmentProperty(id);
        if (ObjectUtil.isNotEmpty(equipmentProperty)) {
            vo.setTagIdList(EquipmentInfoConvert.INSTANCE.convertTagList(equipmentProperty.getEquipmentTagDataList()));
            vo.setStatusPropertyList(BeanUtil.copyToList(equipmentProperty.getStatusPropertyList(),
                    EquipmentStatusVO.class));
            vo.setInfoPropertyList(BeanUtil.copyToList(equipmentProperty.getInfoPropertyList(),
                    EquipmentPropertyVO.class));
            vo.setDataPropertyList(BeanUtil.copyToList(equipmentProperty.getDataPropertyList(),
                    EquipmentPropertyAcquisitionPointVO.class));
        }
        return vo;
    }

    @Override
    public CommonPage<EquipmentInfoVO> getEquipmentPage(EquipmentPageDTO dto) {
        if (ObjectUtil.isNotNull(dto.getCategoryId())) {
            List<EquipmentCategory> categoryList = categoryMapper.selectCategoryList();
            List<Long> categoryIdList = CollectionUtils.convertList(categoryList, EquipmentCategory::getId,
                    category -> category.getTreeCode().contains(String.valueOf(dto.getCategoryId())));
            dto.setCategoryIdList(categoryIdList);
        }
        if (ObjectUtil.isNotNull(dto.getTagId())) {
            ArrayList<Long> tagIds = new ArrayList<>();
            tagIds.add(dto.getTagId());
            List<EquipmentTagDTO> equipmentTagDTOS = tagService.selectChildren(dto.getTagId());
            if (CollUtil.isNotEmpty(equipmentTagDTOS)) {
                tagIds.addAll(equipmentTagDTOS.stream().map(EquipmentTagDTO::getId).collect(Collectors.toList()));
            }
            dto.setTagIds(tagIds);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), StrUtil.toCamelCase(dto.getOrderSql()));
        List<EquipmentInfoVO> list = infoMapper.getEquipmentPage(dto);
        if (CollUtil.isEmpty(list)) {
            BasePage page = new BasePage();
            page.setPageNum(dto.getPageNum());
            page.setPageSize(dto.getPageSize());
            return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
        }
        Map<Long, EquipmentTagData> equipmentTagDataMap =
                tagService.getEquipmentTagDataByEquipmentIdList(list.stream().map(EquipmentInfoVO::getId).collect(Collectors.toList()));
        if (CollUtil.isNotEmpty(equipmentTagDataMap)) {
            list.forEach(vo -> {
                EquipmentTagData equipmentProperty = equipmentTagDataMap.get(vo.getId());
                if (ObjectUtil.isNotEmpty(equipmentProperty)) {
                    vo.setTagIdList(EquipmentInfoConvert.INSTANCE.convertTagList(equipmentProperty.getEquipmentTagDataList()));
                    vo.setStatusPropertyList(BeanUtil.copyToList(equipmentProperty.getStatusPropertyList(),
                            EquipmentStatusVO.class));
                    vo.setInfoPropertyList(BeanUtil.copyToList(equipmentProperty.getInfoPropertyList(),
                            EquipmentPropertyVO.class));
                    vo.setDataPropertyList(BeanUtil.copyToList(equipmentProperty.getDataPropertyList(),
                            EquipmentPropertyAcquisitionPointVO.class));
                }
            });
        }
        return CommonPage.convertPage(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableEquipment(EquipmentEnableDTO dto) {
        List<EquipmentStationInfo> stationInfoList = stationInfoService.queryStationInfoByEquipmentId(dto.getId());
        if (CollUtil.isNotEmpty(stationInfoList)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ENABLE_ERROR);
        }
        EquipmentInfo equipmentInfo = infoMapper.selectById(dto.getId());
        if (Objects.isNull(equipmentInfo)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_EXIST);
        }
        equipmentInfo.setEnable(dto.getEnable());
        infoMapper.saveOrUpdateInfo(equipmentInfo);
    }

    @Override
    public List<EquipmentInfo> queryInfoListByCategoryIdAndEnable(Long id, Boolean enable) {
        return infoMapper.queryInfoListByCategoryIdAndEnable(id, enable);
    }

    @Override
    public CommonPage<EquipmentAppPageVO> getEquipmentAppList(EquipmentAppPageDTO dto) {
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<Long> infoIdList = stationInfoService.queryInfoIdListByUserId(SysUserHolder.getUser().getUserId());
            if (CollUtil.isEmpty(infoIdList)) {
                BasePage page = new BasePage();
                page.setPageSize(dto.getPageSize());
                page.setPageNum(dto.getPageNum());
                return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
            }
            dto.setInfoIdList(infoIdList);
        }
        List<EquipmentAppPageVO> equipmentAppList = infoMapper.getEquipmentAppList(dto);
        return CommonPage.convertPage(equipmentAppList);
    }

    @Override
    public EquipmentAppInfoVO equipmentAppInfo(Long id) {
        EquipmentTagData equipmentProperty = tagService.getEquipmentProperty(id);
        EquipmentInfo equipmentInfo = infoMapper.queryInfoById(id);
        List<EquipmentStation> stationNameList = stationInfoService.queryStationNameByEquipmentId(id);
        EquipmentAppInfoVO appInfoVO = EquipmentInfoConvert.INSTANCE.convertToAppInfoVo(equipmentInfo);
        appInfoVO.setEquipmentStatusAppVOList(BeanUtil.copyToList(equipmentProperty.getStatusPropertyList(),
                EquipmentStatusAppVO.class));
        appInfoVO.setInfoPropertyList(BeanUtil.copyToList(equipmentProperty.getInfoPropertyList(), EquipmentPropertyVO.class));
        appInfoVO.setDataPropertyList(BeanUtil.copyToList(equipmentProperty.getDataPropertyList(),
                EquipmentPropertyAcquisitionPointVO.class));
        appInfoVO.setTagVOList(BeanUtil.copyToList(equipmentProperty.getEquipmentTagDataList(), TagVO.class));
        if (CollUtil.isNotEmpty(stationNameList)) {
            // 设置工位信息
            appInfoVO.setStationNameList(BeanUtil.copyToList(stationNameList, CodeNameVO.class));
            // 设置房间信息
            List<Long> stationIdList = stationNameList.stream().map(EquipmentStation::getId).collect(Collectors.toList());
            List<FactoryRoomStation> roomStationList = roomStationMapper.selectByStationIdList(stationIdList);
            if (CollUtil.isNotEmpty(roomStationList)) {
                List<FactoryRoomFeignVO> factoryRoomFeignVOS = roomMapper.queryRoomListByRoomIds(roomStationList.stream().map(FactoryRoomStation::getRoomId).collect(Collectors.toList()));
                appInfoVO.setRoomNameList(BeanUtil.copyToList(factoryRoomFeignVOS, CodeNameVO.class));
            }
            // 设置产线信息
            // 工位产线
            HashSet<Long> lineIds = new HashSet<>();
            // 房间产线
            if (CollectionUtil.isNotEmpty(roomStationList)) {
                List<FactoryLineRoom> factoryLineRooms = lineRoomMapper.selectByRoomIdList(roomStationList.stream().map(FactoryRoomStation::getRoomId).collect(Collectors.toList()));
                if (CollUtil.isNotEmpty(factoryLineRooms)) {
                    lineIds.addAll(factoryLineRooms.stream().map(FactoryLineRoom::getLineId).collect(Collectors.toList()));
                }
            }
            List<FactoryLineStation> lineStationList = lineStationMapper.selectByStationIdList(stationIdList);
            if (CollUtil.isNotEmpty(lineStationList)) {
                lineIds.addAll(lineStationList.stream().map(FactoryLineStation::getLineId).collect(Collectors.toList()));
            }
            if (CollUtil.isNotEmpty(lineIds)) {
                List<FactoryLine> factoryLineList = lineMapper.selectBatchIds(lineIds);
                appInfoVO.setProductionLineNameList(BeanUtil.copyToList(factoryLineList, CodeNameVO.class));
            }
        }
        return appInfoVO;
    }

    @Override
    public EquipmentInfoVO getConfigByEquipmentId(Long equipmentId) {
        EquipmentInfo equipmentInfo = infoMapper.queryInfoById(equipmentId);
        EquipmentInfoVO vo = EquipmentInfoConvert.INSTANCE.convertToFeignInfoVo(equipmentInfo);
        if (ObjectUtil.isEmpty(vo)) {
            return vo;
        }
        handleEquipmentVo(Collections.singletonList(vo));
        return vo;
    }

    @Override
    public List<EquipmentInfoVO> getConfigByStationId(Long stationId) {
        List<EquipmentInfoVO> list =
                infoMapper.getConfigByStationId(Collections.singletonList(String.valueOf(stationId)));
        handleEquipmentVo(list);
        return list;
    }

    @Override
    public List<EquipmentInfoVO> getEquipmentByTagCode(String tagCode) {
        List<EquipmentInfoVO> equipmentByTagCode = infoMapper.getEquipmentByTagCode(tagCode);
        handleEquipmentVo(equipmentByTagCode);
        return equipmentByTagCode;
    }

    @Override
    public EquipmentInfoVO getEquipmentByEquipmentCode(String equipmentCode) {
        EquipmentInfo equipment = infoMapper.getEquipmentByEquipmentCode(equipmentCode);
        if (ObjectUtil.isEmpty(equipment)) {
            return null;
        }
        EquipmentInfoVO feignVo = BeanUtil.toBean(equipment, EquipmentInfoVO.class);
        handleEquipmentVo(Collections.singletonList(feignVo));
        // 查询当前设备属于哪些工位
        List<EquipmentStationInfo> stationInfos = equipmentStationInfoService.queryStationInfoByEquipmentId(equipment.getId());
        if (CollUtil.isNotEmpty(stationInfos)) {
            feignVo.setStationIdList(stationInfos.stream().map(EquipmentStationInfo::getStationId).collect(Collectors.toList()));
        }
        return feignVo;
    }

    @Override
    public List<AppEquipmentInfoVO> listEquipmentInfo() {
        List<EquipmentInfoVO> feignVoList = getEquipmentByTagCode(EquipmentTagCodeEnum.PRINTER_12022.getCode());
        if (CollUtil.isEmpty(feignVoList)) {
            return Collections.emptyList();
        }
        return BeanUtil.copyToList(feignVoList, AppEquipmentInfoVO.class);
    }

    @Override
    public CommonPage<EquipmentAppPageVO> getEquipmentInfoByStationId(EquipmentStationPageDTO stationPageDTO) {
        PageHelper.startPage(stationPageDTO.getPageNum(), stationPageDTO.getPageSize());
        // 根据工位id查询工位绑定的设备信息
        String stationIdStr = stationPageDTO.getStationIdStr();
        List<String> list = Arrays.asList(stationIdStr.split(StrUtil.COMMA));
        stationPageDTO.setStationIdList(list.stream().map(Long::valueOf).collect(Collectors.toList()));
        List<EquipmentStationInfo> equipmentStationInfoInfos =
                equipmentStationInfoMapper.getPageByStationIdList(stationPageDTO);
        List<Long> equipmentIdList = CollectionUtils.convertList(equipmentStationInfoInfos,
                EquipmentStationInfo::getEquipmentId);
        if (CollUtil.isEmpty(equipmentIdList)) {
            BasePage page = new BasePage();
            page.setPageSize(stationPageDTO.getPageSize());
            page.setPageNum(stationPageDTO.getPageNum());
            return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
        }

        List<EquipmentInfo> equipmentInfoPage = infoMapper.selectListByIdListAndStatus(equipmentIdList, Arrays.asList(
                EquipmentStatusCodeEnum.AVAILABLE.getCode(), EquipmentStatusCodeEnum.OCCUPY.getCode()));
        return CommonPage.convertPage(equipmentInfoPage, EquipmentInfoConvert.INSTANCE::convert2EquipmentAppPapgeVO);
    }

    @Override
    public List<EquipmentPropertyAppVO> listEquipmentProperty(String stationId) {
        List<EquipmentInfoVO> equipmentInfoList = infoMapper.getConfigByStationId(StrUtil.split(stationId,
                StrUtil.C_COMMA));
        if (CollUtil.isEmpty(equipmentInfoList)) {
            return Collections.emptyList();
        }
        List<EquipmentPropertyInfo> equipmentPropertyInfoList =
                propertyInfoMapper.queryEquipmentPropertyByEquipmentIdListAndType(
                        CollectionUtils.convertList(equipmentInfoList, EquipmentInfoVO::getId),
                        PropertyTypeEnum.TAG_PROPERTY.getCode());
        return BeanUtil.copyToList(equipmentPropertyInfoList, EquipmentPropertyAppVO.class);
    }

    @Override
    public void applyEquipmentHeart(EquipmentApplyHeartDTO equipmentApplyHeartDTO) {
        // 1. 查询是否有该设备
        EquipmentInfo equipmentInfo = infoMapper.selectById(equipmentApplyHeartDTO.getId());
        if (Objects.isNull(equipmentInfo)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_EXIST);
        }
        // 2. 设备若没有被占用 则占用该设备
        if (Objects.isNull(equipmentInfo.getBatchNo())) {
            tagService.applyEquipment(EquipmentInfoConvert.INSTANCE.convert2OperateDTO(equipmentApplyHeartDTO),
                    EquipmentStatusLogChangeType.BUSINESS);
        }
        // 3. 若设备被占用 则判断是否是该批次的设备
        if (Objects.nonNull(equipmentInfo.getBatchNo()) && !equipmentInfo.getBatchNo().equals(equipmentApplyHeartDTO.getBatchNo())) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ALREADY_OCCUPY);
        }
        // 4. 进行心跳延续
        if (Objects.nonNull(equipmentInfo.getBatchNo())) {
            redisService.delete(String.valueOf(equipmentInfo.getId()), PlatformRedisKeyDefine.EQUIPMENT_APPLY_HEART);
        }
        // 5. 设置心跳
        redisService.set(String.valueOf(equipmentApplyHeartDTO.getId()),
                String.valueOf(equipmentApplyHeartDTO.getId()), PlatformRedisKeyDefine.EQUIPMENT_APPLY_HEART);
    }

    @Override
    public void applyEquipment(Long equipmentId) {
        // 1. 查询是否有该设备
        EquipmentInfo equipmentInfo = infoMapper.selectById(equipmentId);
        if (Objects.isNull(equipmentInfo)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_EXIST);
        }
        EquipmentApplyOperateDTO dto = new EquipmentApplyOperateDTO();
        dto.setId(equipmentId);
        tagService.applyEquipment(dto, EquipmentStatusLogChangeType.BUSINESS);
    }

    @Override
    public Long queryEquipmentIdByEquipmentCode(String equipmentCode) {
        EquipmentInfo equipment = infoMapper.getEquipmentByEquipmentCode(equipmentCode);
        if (ObjectUtil.isEmpty(equipment)) {
            return null;
        }
        return equipment.getId();
    }

    @Override
    public List<EquipmentInfoVO> getConfigByStationIdList(List<Long> stationIdList) {
        if (CollUtil.isEmpty(stationIdList)) {
            return Collections.emptyList();
        }
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<String> rightStation = factoryStationService.getStationIdsByUserId(SysUserHolder.getUser().getUserId());
            stationIdList = stationIdList.stream().filter(item -> rightStation.contains(String.valueOf(item))).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(stationIdList)) {
            return Collections.emptyList();
        }
        List<EquipmentInfoVO> list = infoMapper.getConfigByStationIdList(stationIdList);
        // 过滤掉当前用户没有权限的工位,admin除外
        List<EquipmentInfoVO> res = new ArrayList<>();
        // 一样的设备只返回一个
        Map<Long, List<EquipmentInfoVO>> map = list.stream().collect(Collectors.groupingBy(EquipmentInfoVO::getId));
        map.forEach((k, v) -> {
            if (CollectionUtil.isNotEmpty(v)) {
                res.add(v.get(0));
            }
        });
        handleEquipmentVo(res);
        return res;
    }

    @Override
    public List<EquipmentInfoVO> getConfigByProductionLineId(Long productionLineId) {
        List<Long> stationIdList = lineService.selectStationIdByLineId(productionLineId);
        // 过滤掉当前用户没有权限的工位,admin除外
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<String> rightStation = factoryStationService.getStationIdsByUserId(SysUserHolder.getUser().getUserId());
            stationIdList = stationIdList.stream().filter(item -> rightStation.contains(String.valueOf(item))).collect(Collectors.toList());
            if (CollUtil.isEmpty(stationIdList)) {
                return Collections.emptyList();
            }
        }
        List<EquipmentInfoVO> list = infoMapper.getConfigByStationIdList(stationIdList);
        //id重复的只返回一个
        List<EquipmentInfoVO> res = new ArrayList<>();
        // 一样的设备只返回一个
        Map<Long, List<EquipmentInfoVO>> map = list.stream().collect(Collectors.groupingBy(EquipmentInfoVO::getId));
        map.forEach((k, v) -> {
            if (CollectionUtil.isNotEmpty(v)) {
                res.add(v.get(0));
            }
        });
        handleEquipmentVo(res);
        return res;
    }

    @Override
    public List<EquipmentInfoVO> getEquipmentByParam(EquipmentQueryDTO queryDTO) {
        List<EquipmentInfoVO> equipmentInfoFeignVOS = new ArrayList<>();
        if (Objects.isNull(queryDTO) || (CollectionUtil.isEmpty(queryDTO.getStationIdList()) && Objects.isNull(queryDTO.getTagCode()))) {
            return equipmentInfoFeignVOS;
        }
        Set<Long> stationEquipmentIdList = new HashSet<>();
        if (CollectionUtil.isNotEmpty(queryDTO.getStationIdList())) {
            List<EquipmentStationInfo> equipmentStationInfos =
                    equipmentStationInfoMapper.queryStationInfoByStationIdList(queryDTO.getStationIdList());
            if (CollectionUtil.isNotEmpty(equipmentStationInfos)) {
                stationEquipmentIdList.addAll(equipmentStationInfos.stream().map(EquipmentStationInfo::getEquipmentId).collect(Collectors.toList()));
            }
        }

        List<Long> tagEquipmentIdList = new ArrayList<>();
        if (Objects.nonNull(queryDTO.getTagCode())) {
            tagEquipmentIdList = tagService.getEquipmentIdByTagCode(queryDTO.getTagCode());
        }
        Set<Long> needEquipmentIdList = new HashSet<>();
        if (CollectionUtil.isNotEmpty(queryDTO.getStationIdList()) && Objects.nonNull(queryDTO.getTagCode())) {
            // 取交集
            for (Long equipmentId : tagEquipmentIdList) {
                if (!stationEquipmentIdList.contains(equipmentId)) {
                    continue;
                }
                needEquipmentIdList.add(equipmentId);
            }
        } else if (CollectionUtil.isNotEmpty(queryDTO.getStationIdList())) {
            needEquipmentIdList.addAll(stationEquipmentIdList);
        } else {
            needEquipmentIdList.addAll(tagEquipmentIdList);
        }
        if (CollectionUtil.isEmpty(needEquipmentIdList)) {
            return equipmentInfoFeignVOS;
        }
        // 根据id查询设备详情
        List<EquipmentInfo> equipmentInfoList = infoMapper.selectBatchIds(needEquipmentIdList);
        // 剔除停用的设备
        equipmentInfoList = equipmentInfoList.stream().filter(EquipmentInfo::getEnable).collect(Collectors.toList());
        equipmentInfoFeignVOS = BeanUtil.copyToList(equipmentInfoList, EquipmentInfoVO.class);
        handleEquipmentVo(equipmentInfoFeignVOS);
        return equipmentInfoFeignVOS;
    }

    @Override
    public List<EquipmentPrintVO> getPrintEquipment() {
        List<EquipmentInfoVO> equipmentByTagCode =
                getEquipmentByTagCode(EquipmentTagCodeEnum.PRINTER_12022.getCode());
        List<EquipmentPrintVO> equipmentPrintVOS = new ArrayList<>();
        if (CollectionUtil.isEmpty(equipmentByTagCode)) {
            return equipmentPrintVOS;
        }
        for (EquipmentInfoVO equipmentInfoFeignVO : equipmentByTagCode) {
            String ip = null;
            String port = null;
            String dpi = null;
            for (EquipmentPropertyVO equipmentPropertyFeignVO : equipmentInfoFeignVO.getInfoPropertyList()) {
                if (StrUtil.equals(equipmentPropertyFeignVO.getCode(),
                        TagEquipmentPropertyCodeEnum.IP_ADDRESS.getCode())) {
                    ip = equipmentPropertyFeignVO.getValue();
                } else if (StrUtil.equals(equipmentPropertyFeignVO.getCode(),
                        TagEquipmentPropertyCodeEnum.PORT.getCode())) {
                    port = equipmentPropertyFeignVO.getValue();
                } else if (StrUtil.equals(equipmentPropertyFeignVO.getCode(),
                        TagEquipmentPropertyCodeEnum.PRINTER_DPI.getCode())) {
                    dpi = equipmentPropertyFeignVO.getValue();
                }
            }
            EquipmentPrintVO equipmentPrintVO = new EquipmentPrintVO();
            equipmentPrintVO.setId(equipmentInfoFeignVO.getId());
            equipmentPrintVO.setName(equipmentInfoFeignVO.getName());
            equipmentPrintVO.setIp(ip);
            equipmentPrintVO.setPort(port);
            equipmentPrintVO.setDpi(dpi);
            equipmentPrintVOS.add(equipmentPrintVO);
        }
        return equipmentPrintVOS;
    }

    @Override
    public void analyseEquipmentStatus(Long equipmentId, EquipmentStatusLogChangeType changeType) {
        EquipmentInfo equipmentInfo = infoMapper.selectById(equipmentId);
        EquipmentStatusCodeEnum preStatusEnum = EquipmentStatusCodeEnum.getByCode(equipmentInfo.getStatus());
        equipmentTagService.analyseEquipmentStatus(equipmentInfo);
        infoMapper.updateById(equipmentInfo);
        // 若有设备状态变更则记录操作日志
        EquipmentStatusCodeEnum curStatusEnum = EquipmentStatusCodeEnum.getByCode(equipmentInfo.getStatus());
        if (ObjectUtil.equals(preStatusEnum, curStatusEnum)) {
            return;
        }
        EquipmentStatusLogData equipmentStatusLog =
                EquipmentConvert.INSTANCE.convertEquipmentStatusLogData(equipmentInfo, preStatusEnum.getStatusLogCode(),
                        curStatusEnum, changeType);
        equipmentLogService.saveStatusLog(equipmentStatusLog);
    }

    @Override
    public List<EquipmentInfoVO> selectEquipmentByIdList(Collection<Long> equipmentIdList) {
        List<EquipmentInfo> equipmentInfos = infoMapper.selectBatchIds(equipmentIdList);
        Map<Long, EquipmentTagData> tagDataMap =
                tagService.getEquipmentTagDataByEquipmentIdList(equipmentIdList.stream().collect(Collectors.toList()));
        return EquipmentInfoConvert.INSTANCE.convertToInfoVoListWithTag(equipmentInfos, tagDataMap);
    }

    private void handleEquipmentVo(List<EquipmentInfoVO> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<Long> equipmentIdList = CollectionUtils.convertList(list, EquipmentInfoVO::getId);
        Map<Long, EquipmentTagData> map = tagService.getEquipmentTagDataByEquipmentIdList(equipmentIdList);
        list.forEach(item -> {
            EquipmentTagData equipmentTagData = map.get(item.getId());
            if (equipmentTagData == null) {
                return;
            }
            if (CollUtil.isNotEmpty(equipmentIdList)) {
                item.setStatusPropertyList(BeanUtil.copyToList(equipmentTagData.getStatusPropertyList(),
                        EquipmentStatusVO.class));
                item.setInfoPropertyList(BeanUtil.copyToList(equipmentTagData.getInfoPropertyList(),
                        EquipmentPropertyVO.class));
                item.setDataPropertyList(BeanUtil.copyToList(equipmentTagData.getDataPropertyList(),
                        EquipmentPropertyAcquisitionPointVO.class));
            }
            List<String> tagNames =
                    equipmentTagData.getEquipmentTagDataList().stream().map(TagData::getName).collect(Collectors.toList());
            item.setTagNames(tagNames);
            item.setTagIdList(BeanUtil.copyToList(equipmentTagData.getEquipmentTagDataList(),
                    TagVO.class));
        });

    }

    @Override
    public EquipmentPrintInfoVO printEquipmentTagInfo(EquipmentPrintTagDTO dto) {
        EquipmentInfo equipmentInfo = infoMapper.selectById(dto.getEquipmentId());
        EquipmentPrintInfoVO equipmentPrintInfoVO = EquipmentInfoConvert.INSTANCE.convertToPrintInfo(equipmentInfo);
        equipmentPrintInfoVO.setPrintDate(LocalDateTimeUtil.format(LocalDate.now(), DateConverter.pattern));
        return equipmentPrintInfoVO;
    }


    /**
     * 绑定采集点
     *
     * @param equipmentId              设备id
     * @param acquisitionPlatform      数采平台
     * @param equipmentPropertyDTOList 点位数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindDataPropertyAcquisitionPoint(Long equipmentId,
                                                 AcquisitionPlatformEnum acquisitionPlatform, List<EquipmentPropertyDTO> equipmentPropertyDTOList) {
        EquipmentInfo equipmentInfo = infoMapper.selectById(equipmentId);
        // 更新设备的数采平台
        equipmentInfo.setAcquisitionPlatform(acquisitionPlatform);
        infoMapper.updateById(equipmentInfo);
        if (CollectionUtil.isEmpty(equipmentPropertyDTOList)) {
            return;
        }
        List<EquipmentPropertyInfo> dataTagProperty = propertyInfoMapper.selectByEquipmentId(equipmentId,
                PropertyTypeEnum.TAG_DATA_PROPERTY.getCode());
        if (CollectionUtil.isEmpty(dataTagProperty)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_DATA_TAG_EMPTY);
        }
        Map<String, EquipmentPropertyDTO> dataPropertyMap =
                equipmentPropertyDTOList.stream().collect(Collectors.toMap(EquipmentPropertyDTO::getCode,
                        Function.identity()));
        Set<String> exits =
                dataTagProperty.stream().map(EquipmentPropertyInfo::getPropertyCode).collect(Collectors.toSet());
        // 计算数据存在的和接口传入的code的差值
        Set<String> codeSet = dataPropertyMap.keySet();
        List<String> notExits = codeSet.stream().filter(code -> !exits.contains(code)).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(notExits)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_DATA_TAG_NOT_EXIST,
                    String.join(",", notExits));
        }
        // 查询采集点是否能够绑定数据
        List<AcquisitionPointDTO> enableList = acquisitionPointService.listEnableByEquipmentDataProperty(codeSet, acquisitionPlatform);
        List<EquipmentPropertyDTO> notEnableDataPoint =
                equipmentPropertyDTOList.stream().filter(item -> ObjectUtil.isNotNull(item.getValue())).filter(item -> enableList.stream()
                        .noneMatch(dataPoint -> dataPoint.getId().equals(Long.valueOf(item.getValue()))))
                        .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(notEnableDataPoint)) {
            throw new BmosException(PlatformResponseCode.ACQUISITION_NOT_ENABLE,
                    notEnableDataPoint.stream().map(EquipmentPropertyDTO::getCode).collect(Collectors.joining(",")));
        }
        // 采集点是否属于指定的数采平台
        List<AcquisitionPointDTO> acquisitionPointList =
                enableList.stream().filter(item -> !item.getAcquisitionPlatform().equals(acquisitionPlatform))
                        .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(acquisitionPointList)) {
            throw new BmosException(PlatformResponseCode.ACQUISITION_PLATFORM_NOT_MATCH,
                    acquisitionPointList.stream().map(AcquisitionPointDTO::getCode).collect(Collectors.joining(",")));
        }
        ArrayList<EquipmentPropertyInfo> needUpdate = new ArrayList<>();
        dataTagProperty.forEach(item -> {
            EquipmentPropertyDTO equipmentPropertyDTO = dataPropertyMap.get(item.getPropertyCode());
            if (equipmentPropertyDTO == null) {
                item.setValue(null);
            }
            item.setValue(equipmentPropertyDTO.getValue());
            needUpdate.add(item);
        });
        if (CollectionUtil.isEmpty(needUpdate)) {
            return;
        }
        propertyInfoMapper.updateBatch(needUpdate);
        // 如果采集点没有绑定设备数据，需要更新采集点关联设备数据表
        HashMap<Long, String> dataPointEquipmentDataMap = new HashMap<>();
        equipmentPropertyDTOList.stream().filter(item -> ObjectUtil.isNotNull(item.getValue())).forEach(item -> {
            dataPointEquipmentDataMap.put(Long.valueOf(item.getValue()), item.getCode());
        });
        if (CollectionUtil.isNotEmpty(dataPointEquipmentDataMap)) {
            acquisitionPointService.bindEquipmentData(dataPointEquipmentDataMap);
        }
    }

    @Override
    public List<EquipmentTagUseTemplateDTO> getUseLogTemplate(Long equipmentId) {
        EquipmentInfo equipmentInfo = infoMapper.selectById(equipmentId);
        if (equipmentInfo == null) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_EXIST);
        }
        List<EquipmentTagInfo> equipmentTagInfos = equipmentTagInfoMapper.selectByEquipmentId(equipmentId);
        if (CollectionUtil.isNotEmpty(equipmentTagInfos)) {
            List<Long> tagIdList = equipmentTagInfos.stream().map(EquipmentTagInfo::getTagId).collect(Collectors.toList());
            List<EquipmentTagUseTemplate> templateList = equipmentUseTemplateMapper.selectByTagIds(tagIdList);
            return BeanUtil.copyToList(templateList, EquipmentTagUseTemplateDTO.class);
        }
        return new ArrayList<>();
    }

    /**
     * 根据产线id和工位id查询设备列表
     *
     * @param productionLineId 产线id
     * @param stationIdList    工位id
     * @return 查询结果
     */
    @Override
    public List<EquipmentInfoVO> getConfigByByProductionIdStationIdList(Long productionLineId, List<Long> stationIdList) {
        List<Long> productionStationIdList = lineService.selectStationIdByLineId(productionLineId);
        // 如果传入的工位id为空，直接返回根据产线id查询的设备列表
        if (CollectionUtil.isEmpty(stationIdList)) {
            return this.getConfigByProductionLineId(productionLineId);
        }
        // 根据传入的工位id过滤
        stationIdList = stationIdList.stream().filter(productionStationIdList::contains).collect(Collectors.toList());
        return this.getConfigByStationIdList(stationIdList);
    }

    @Override
    public List<EquipmentVO> getDeleteEquipment(List<Long> equipmentIdList) {
        if (CollUtil.isEmpty(equipmentIdList)) {
            return new ArrayList<>();
        }
        return infoMapper.getDeleteEquipment(equipmentIdList);
    }

    @Override
    public List<EquipmentInfoVO> getConfigByProductionLineIdWithNoPermission(Long productionLineId) {
        List<Long> stationIdList = lineService.selectStationIdByLineId(productionLineId);
        List<EquipmentInfoVO> list = infoMapper.getConfigByStationIdList(stationIdList);
        //id重复的只返回一个
        List<EquipmentInfoVO> res = new ArrayList<>();
        // 一样的设备只返回一个
        Map<Long, List<EquipmentInfoVO>> map = list.stream().collect(Collectors.groupingBy(EquipmentInfoVO::getId));
        map.forEach((k, v) -> {
            if (CollectionUtil.isNotEmpty(v)) {
                res.add(v.get(0));
            }
        });
        handleEquipmentVo(res);
        return res;
    }

    @Override
    public CommonPage<EquipmentInfoVO> getEquipmentPageByLineId(EquipmentPageByLineDTO dto) {
        List<Long> stationIdList = lineService.selectStationIdByLineId(dto.getProductionLineId());
        // 过滤掉当前用户没有权限的工位,admin除外
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<String> rightStation = factoryStationService.getStationIdsByUserId(SysUserHolder.getUser().getUserId());
            stationIdList = stationIdList.stream().filter(item -> rightStation.contains(String.valueOf(item))).collect(Collectors.toList());
            if (CollUtil.isEmpty(stationIdList)) {
                return CommonPage.CommonPage(new ArrayList<>(), 0L, dto);
            }
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<EquipmentInfoVO> list = infoMapper.getDistinctConfigByStationIdList(stationIdList);
        return CommonPage.convertPage(list);
    }
}
