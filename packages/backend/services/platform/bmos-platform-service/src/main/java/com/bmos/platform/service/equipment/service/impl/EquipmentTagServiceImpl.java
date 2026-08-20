package com.bmos.platform.service.equipment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.id.IdUtils;
import com.bmos.expire.producer.ExpireMessageProducer;
import com.bmos.expire.properties.ExpireMessage;
import com.bmos.expire.properties.ExpireMessageProperty;
import com.bmos.mq.listener.Event.StateEvent;
import com.bmos.mq.listener.enums.StateEventTypeEnum;
import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import com.bmos.platform.common.enums.expire.ExpireListenerConstants;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.common.enums.equipment.PropertyTypeEnum;
import com.bmos.platform.facade.equipment.enums.TagEquipmentStatusCodeEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.equipment.convert.EquipmentConvert;
import com.bmos.platform.service.equipment.mapper.*;
import com.bmos.platform.service.equipment.model.*;
import com.bmos.platform.service.equipment.service.EquipmentLogService;
import com.bmos.platform.service.equipment.service.EquipmentStatusHandler;
import com.bmos.platform.service.equipment.service.EquipmentTagService;
import com.bmos.platform.service.equipment.service.data.*;
import com.bmos.platform.service.equipment.service.dto.*;
import com.bmos.platform.service.feign.CommonFeignClient;
import com.bmos.platform.service.feign.CommonFeignClientFactory;
import com.bmos.platform.service.message.dto.EquipmentFaultMessageContext;
import com.bmos.platform.service.message.sender.EquipmentFaultMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class EquipmentTagServiceImpl extends ServiceImpl<EquipmentTagMapper, EquipmentTag> implements EquipmentTagService {
    private final String MES_SERVICE_NAME = "bmos-mes-service";

    @Autowired
    EquipmentTagInfoMapper equipmentTagInfoMapper;

    @Autowired
    EquipmentTagMapper tagMapper;

    @Autowired
    EquipmentInfoMapper equipmentInfoMapper;

    @Autowired
    EquipmentTagPropertyMapper tagPropertyMapper;

    @Autowired
    EquipmentPropertyInfoMapper equipmentPropertyInfoMapper;

    @Autowired
    EquipmentLogService equipmentLogService;

    @Autowired
    EquipmentStatusHandler equipmentStatusHandler;

    @Autowired
    ExpireMessageProducer expireMessageProducer;

    @Autowired
    private CommonFeignClientFactory commonFeignClientFactory;

    @Autowired
    private EquipmentUseTemplateMapper equipmentUseTemplateMapper;

    @Autowired
    private EquipmentFaultMessageSender equipmentFaultNotify;

    @Resource
    private Executor asyncTaskExecutor;

    @Override
    public EquipmentTagData getEquipmentProperty(Long equipmentId) {
        EquipmentTagData equipmentTagData = new EquipmentTagData();
        equipmentTagData.setEquipmentId(equipmentId);
        // 获取设备下的设备标签
        List<EquipmentTagInfo> equipmentTagInfoList = equipmentTagInfoMapper.selectByEquipmentId(equipmentId);
        List<EquipmentTag> equipmentTagList =
                tagMapper.selectByIdList(equipmentTagInfoList.stream().map(EquipmentTagInfo::getTagId).collect(Collectors.toList()));
        // 获取设备下的属性
        List<EquipmentPropertyInfo> equipmentPropertyInfoList =
                equipmentPropertyInfoMapper.selectPropertyInfoListByEquipmentId(equipmentId);
        List<TagData> tagDataList = EquipmentConvert.INSTANCE.convertTagDataList(equipmentTagList);
        equipmentTagData.setEquipmentTagDataList(tagDataList);
        // 状态列表
        List<EquipmentPropertyInfo> statusList =
                equipmentPropertyInfoList.stream().filter(equipmentPropertyInfo -> PropertyTypeEnum.EQUIPMENT_STATUS.getCode()
                        .equals(equipmentPropertyInfo.getPropertyType())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(statusList)) {
            List<EquipmentTagStatusData> tagStatusData =
                    statusList.stream().map(EquipmentConvert.INSTANCE::convertEquipmentTagStatusData).collect(Collectors.toList());
            equipmentTagData.setStatusPropertyList(tagStatusData);
        }
        // 信息列表
        List<EquipmentPropertyInfo> infoList =
                equipmentPropertyInfoList.stream().filter(equipmentPropertyInfo -> PropertyTypeEnum.TAG_PROPERTY.getCode()
                        .equals(equipmentPropertyInfo.getPropertyType())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(infoList)) {
            List<EquipmentPropertyData> equipmentPropertyDataList =
                    infoList.stream().map(EquipmentConvert.INSTANCE::convert2EquipmentPropertyData).collect(Collectors.toList());
            equipmentTagData.setInfoPropertyList(equipmentPropertyDataList);
        }
        // 数据列表
        List<EquipmentPropertyInfo> dataList =
                equipmentPropertyInfoList.stream().filter(equipmentPropertyInfo -> PropertyTypeEnum.TAG_DATA_PROPERTY.getCode()
                        .equals(equipmentPropertyInfo.getPropertyType())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(dataList)) {
            List<EquipmentPropertyData> equipmentPropertyDataList =
                    dataList.stream().map(EquipmentConvert.INSTANCE::convert2EquipmentPropertyData).collect(Collectors.toList());
            equipmentTagData.setDataPropertyList(equipmentPropertyDataList);
        }
        return equipmentTagData;
    }

    @Override
    public Map<Long, EquipmentTagData> getEquipmentTagDataByEquipmentIdList(List<Long> equipmentIdList) {
        if (CollectionUtil.isEmpty(equipmentIdList)) {
            return new HashMap<>();
        }
        Map<Long, EquipmentTagData> equipmentTagDataMap = new HashMap<>();
        for (Long equipmentId : equipmentIdList) {
            equipmentTagDataMap.put(equipmentId, getEquipmentProperty(equipmentId));
        }
        return equipmentTagDataMap;
    }

    @Override
    public List<Long> getEquipmentIdByTagCode(String tagCode) {
        EquipmentTag equipmentTag = tagMapper.selectByTagCode(tagCode);
        if (Objects.isNull(equipmentTag)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_TAG_NOT_EXIST);
        }
        List<EquipmentTagInfo> equipmentTagList = equipmentTagInfoMapper.selectByTagCode(equipmentTag.getId());
        return CollectionUtil.isEmpty(equipmentTagList) ? new ArrayList<>() :
                equipmentTagList.stream().map(EquipmentTagInfo::getEquipmentId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyEquipment(EquipmentApplyOperateDTO dto, EquipmentStatusLogChangeType changeType) {
        EquipmentInfo equipmentInfo = equipmentInfoMapper.selectById(dto.getId());
        if (Objects.isNull(equipmentInfo)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_EXIST);
        }
        if (StrUtil.isNotEmpty(equipmentInfo.getBatchNo())) {
            // 当前设备已被批次占用，无法再次占用
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ALREADY_OCCUPY);
        }
        // 执行操作
        doApplyEquipment(equipmentInfo, dto, changeType);
        // 设备总状态变更则进行通知任务
        this.sendTaskMessageAsync(equipmentInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releaseEquipment(EquipmentOperateDTO dto, EquipmentStatusLogChangeType changeType) {
        EquipmentInfo equipmentInfo = equipmentInfoMapper.selectById(dto.getId());
        if (Objects.isNull(equipmentInfo)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_EXIST);
        }
        // 设备状态是否为占用
        if (!Objects.equals(EquipmentStatusCodeEnum.OCCUPY.getCode(), equipmentInfo.getStatus())) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_STATUS_NOT_APLLY);
        }
        // 执行操作
        doReleaseEquipment(equipmentInfo, changeType);
        // 设备总状态变更则进行通知任务
        this.sendTaskMessageAsync(equipmentInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void faultEquipment(EquipmentOperateDTO dto) {
        EquipmentInfo equipmentInfo = equipmentInfoMapper.selectById(dto.getId());
        if (Objects.isNull(equipmentInfo)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_EXIST);
        }
        // 当前设备若已故障 无需重复操作
        if (Objects.equals(EquipmentStatusCodeEnum.FAULT.getCode(), equipmentInfo.getStatus())) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_STATUS_ALREADY_FAULT);
        }
        doFaultEquipment(equipmentInfo);
        afterStatusChange(equipmentInfo);

    }

    private void afterStatusChange(EquipmentInfo equipmentInfo) {
        CompletableFuture.runAsync(() -> {
                    // 设备总状态变更则进行通知任务
                    this.sendTaskMessageAsync(equipmentInfo);
                    // 发送消息通知
                    this.sendMessage(equipmentInfo);
                },
                asyncTaskExecutor);
    }

    private void sendMessage(EquipmentInfo equipmentInfo) {
        EquipmentFaultMessageContext equipmentFaultMessageContext = new EquipmentFaultMessageContext();
        equipmentFaultMessageContext.setEquipmentCode(equipmentInfo.getCode());
        equipmentFaultMessageContext.setEquipmentName(equipmentInfo.getName());
        equipmentFaultMessageContext.setTime(LocalDateTime.now());
        equipmentFaultNotify.send(equipmentFaultMessageContext);
    }

    /**
     * 故障操作
     *
     * @param equipmentInfo
     */
    private void doFaultEquipment(EquipmentInfo equipmentInfo) {
        EquipmentStatusCodeEnum preStatusCodeEnum = EquipmentStatusCodeEnum.getByCode(equipmentInfo.getStatus());
        String preStatusName = Objects.requireNonNull(preStatusCodeEnum).getStatusLogCode();
        equipmentInfo.setStatus(EquipmentStatusCodeEnum.FAULT.getCode());
        equipmentInfo.setOperateLogId(null);
        equipmentInfo.setBatchNo(null);
        equipmentInfo.setProductName(null);
        equipmentInfo.setApplyStationId(null);
        equipmentInfoMapper.updateById(equipmentInfo);
        // 记录设备状态变更日志
        EquipmentStatusLogData statusLog = EquipmentConvert.INSTANCE.convertEquipmentStatusLogData(equipmentInfo,
                preStatusName, EquipmentStatusCodeEnum.FAULT, EquipmentStatusLogChangeType.MANUAL);
        equipmentLogService.saveStatusLog(statusLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operateEquipmentProperty(EquipmentPropertyOperateDTO dto) {
        EquipmentPropertyInfo equipmentPropertyInfo = equipmentPropertyInfoMapper.selectById(dto.getId());
        Boolean preFinished = equipmentPropertyInfo.getFinishStatus();
        equipmentPropertyInfo.setFinishStatus(dto.getFinishStatus());
        equipmentPropertyInfo.setActualValue(LocalDateTimeUtil.format(dto.getExpireDateTime(), EquipmentConvert.pattern));
        equipmentPropertyInfoMapper.updateById(equipmentPropertyInfo);
        EquipmentInfo equipmentInfo = equipmentInfoMapper.selectById(equipmentPropertyInfo.getEquipmentId());

        EquipmentStatusCodeEnum preStatusEnum = EquipmentStatusCodeEnum.getByCode(equipmentInfo.getStatus());
        // 计算设备状态
        this.analyseEquipmentStatus(equipmentInfo);
        // 发送消息
        this.sendExpireMessage(equipmentPropertyInfo);

        EquipmentStatusCodeEnum curStatusEnum = EquipmentStatusCodeEnum.getByCode(equipmentInfo.getStatus());
        equipmentInfoMapper.updateById(equipmentInfo);
        // 日志记录
        if (Objects.equals(preFinished, dto.getFinishStatus())) {
            return;
        }
        // 代表有状态变更
        TagEquipmentStatusCodeEnum tagEquipmentStatusCodeEnum =
                TagEquipmentStatusCodeEnum.getByCode(equipmentPropertyInfo.getPropertyCode());
        EquipmentStatusLogData statusLog = EquipmentConvert.INSTANCE.convertStatusLogData(equipmentInfo,
                equipmentPropertyInfo, tagEquipmentStatusCodeEnum, dto.getFinishStatus(),
                EquipmentStatusLogChangeType.MANUAL);
        equipmentLogService.saveStatusLog(statusLog);
        // 记录是否有设备总状态变更
        if (Objects.equals(preStatusEnum, curStatusEnum)) {
            return;
        }
        // 设备总状态变更则进行通知任务
        this.sendTaskMessageAsync(equipmentInfo);
        // 记录设备总状态变更日志
        EquipmentStatusLogData equipmentStatusLog =
                EquipmentConvert.INSTANCE.convertEquipmentStatusLogData(equipmentInfo, preStatusEnum.getStatusLogCode(),
                        curStatusEnum, EquipmentStatusLogChangeType.MANUAL);
        equipmentLogService.saveStatusLog(equipmentStatusLog);
    }

    /**
     * 设备总状态变更则进行通知任务
     * <p>
     * 状态变更改为异步发送信息
     *
     * @param equipmentInfo
     */
    private void sendTaskMessageAsync(EquipmentInfo equipmentInfo) {
        StateEvent stateEvent = new StateEvent();
        stateEvent.setId(equipmentInfo.getId());
        stateEvent.setState(String.valueOf(equipmentInfo.getStatus()));
        stateEvent.setType(StateEventTypeEnum.EQUIPMENT.getCode());
        SysUser user = SysUserHolder.getUser();
        CompletableFuture.runAsync(() -> {
                    SysUserHolder.setUser(user);
                    CommonFeignClient feignClient = commonFeignClientFactory.getFeignClient(MES_SERVICE_NAME);
                    feignClient.conditionUpdate(stateEvent);
                    SysUserHolder.remove();
                },
                asyncTaskExecutor);
    }

    private void sendExpireMessage(EquipmentPropertyInfo equipmentPropertyInfo) {
        if (!equipmentPropertyInfo.getFinishStatus()) {
            return;
        }
        String actualValue = equipmentPropertyInfo.getActualValue();
        if (StrUtil.isEmpty(actualValue)) {
            return;
        }
        ExpireMessageProperty expireMessageProperty = new ExpireMessageProperty();
        expireMessageProperty.setTag(ExpireListenerConstants.EQUIPMENT_EXPIRE);
        ExpireMessage expireMessage = new ExpireMessage();
        expireMessage.setUniqueId(equipmentPropertyInfo.getId());
        expireMessage.setExpireTime(convert2TimeStamp(LocalDateTimeUtil.endOfDay(LocalDateTimeUtil.parse(actualValue,
                EquipmentConvert.pattern))));
        expireMessageProperty.setExpireMessage(expireMessage);
        expireMessageProducer.sendAndWeedDuplicates(expireMessageProperty);
    }

    Long convert2TimeStamp(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        return instant.getEpochSecond();
    }

    @Override
    public void recoverEquipment(EquipmentOperateDTO dto) {
        EquipmentInfo equipmentInfo = equipmentInfoMapper.selectById(dto.getId());
        if (Objects.isNull(equipmentInfo)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_EXIST);
        }
        // 判断设备是否为故障
        if (!Objects.equals(equipmentInfo.getStatus(), EquipmentStatusCodeEnum.FAULT.getCode())) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_STATUS_NOT_FAULT);
        }
        // 执行恢复
        doRecoverEquipment(equipmentInfo);
        // 设备总状态变更则进行通知任务
        this.sendTaskMessageAsync(equipmentInfo);
    }

    @Override
    public void deleteByEquipmentId(Long equipmentId) {
        // 删除设备与属性之间的关联关系
        equipmentPropertyInfoMapper.deleteByEquipmentId(equipmentId);
        // 先删除设备与标签之间的关联关系
        equipmentTagInfoMapper.deleteByEquipmentId(equipmentId);
    }

    @Override
    public void analyseEquipmentStatus(EquipmentInfo equipmentInfo) {
        List<EquipmentPropertyInfo> equipmentPropertyInfoList =
                equipmentPropertyInfoMapper.selectByEquipmentId(equipmentInfo.getId(),
                        PropertyTypeEnum.EQUIPMENT_STATUS.getCode());
        List<EquipmentTagStatusData> equipmentTagStatusDataLis = new ArrayList<>();
        for (EquipmentPropertyInfo propertyInfo : equipmentPropertyInfoList) {
            equipmentTagStatusDataLis.add(EquipmentConvert.INSTANCE.convertEquipmentTagStatusData(propertyInfo));
        }
        equipmentStatusHandler.analyzeEffectiveReleaseEquipment(equipmentTagStatusDataLis, equipmentInfo);
    }

    /**
     * 执行占用操作
     *
     * @param equipmentInfo
     * @param dto
     */
    private void doApplyEquipment(EquipmentInfo equipmentInfo, EquipmentApplyOperateDTO dto,
                                  EquipmentStatusLogChangeType changeType) {
        // 变更前状态名称
        EquipmentStatusCodeEnum preStatusCodeEnum = EquipmentStatusCodeEnum.getByCode(equipmentInfo.getStatus());
        String preStatusName = Objects.requireNonNull(preStatusCodeEnum).getStatusLogCode();
        equipmentInfo.setStatus(EquipmentStatusCodeEnum.OCCUPY.getCode());
        equipmentInfo.setBatchNo(dto.getBatchNo());
        equipmentInfo.setProductName(dto.getProductName());
        equipmentInfo.setApplyStationId(dto.getStationId());
        // 记录设备操作日志
        EquipmentOperateLogData operateLogData =
                EquipmentConvert.INSTANCE.convertEquipmentOperateLogData(equipmentInfo, changeType);
        Long logId = equipmentLogService.saveOperateLog(operateLogData);
        if (Objects.isNull(equipmentInfo.getOperateLogId())) {
            equipmentInfo.setOperateLogId(logId);
        }
        equipmentInfoMapper.updateById(equipmentInfo);
        // 记录设备状态变更日志
        EquipmentStatusLogData statusLog = EquipmentConvert.INSTANCE.convertEquipmentStatusLogData(equipmentInfo,
                preStatusName, EquipmentStatusCodeEnum.OCCUPY, changeType);
        equipmentLogService.saveStatusLog(statusLog);
    }

    /**
     * 执行释放操作
     *
     * @param equipmentInfo
     */
    private void doReleaseEquipment(EquipmentInfo equipmentInfo, EquipmentStatusLogChangeType changeType) {

        // 先默认可用 再根据状态重新计算计算设备状态
        equipmentInfo.setStatus(EquipmentStatusCodeEnum.AVAILABLE.getCode());
        equipmentInfo.setBatchNo(null);
        equipmentInfo.setProductName(null);
        equipmentInfo.setApplyStationId(null);
        // 计算设备状态
        this.analyseEquipmentStatus(equipmentInfo);

        // 记录操作日志
        EquipmentOperateLogData operateLogData =
                EquipmentConvert.INSTANCE.convertEquipmentOperateLogData(equipmentInfo, changeType);
        equipmentLogService.saveOperateLog(operateLogData);
        // 记录状态变更日志
        EquipmentStatusCodeEnum statusCodeEnum = EquipmentStatusCodeEnum.getByCode(equipmentInfo.getStatus());
        EquipmentStatusLogData statusLog = EquipmentConvert.INSTANCE.convertEquipmentStatusLogData(equipmentInfo,
                EquipmentStatusCodeEnum.OCCUPY.getStatusLogCode(), statusCodeEnum, changeType);
        equipmentLogService.saveStatusLog(statusLog);
        equipmentInfo.setOperateLogId(null);
        equipmentInfoMapper.updateById(equipmentInfo);
    }

    /**
     * 执行恢复操作
     *
     * @param equipmentInfo
     */
    private void doRecoverEquipment(EquipmentInfo equipmentInfo) {
        // 先默认可用 再根据状态重新计算计算设备状态
        equipmentInfo.setStatus(EquipmentStatusCodeEnum.AVAILABLE.getCode());

        // 重新计算设备状态
        this.analyseEquipmentStatus(equipmentInfo);

        equipmentInfoMapper.updateById(equipmentInfo);
        // 记录状态变更日志
        EquipmentStatusCodeEnum statusCodeEnum = EquipmentStatusCodeEnum.getByCode(equipmentInfo.getStatus());
        EquipmentStatusLogData statusLog = EquipmentConvert.INSTANCE.convertEquipmentStatusLogData(equipmentInfo,
                EquipmentStatusCodeEnum.FAULT.getStatusLogCode(), statusCodeEnum, EquipmentStatusLogChangeType.MANUAL);
        equipmentLogService.saveStatusLog(statusLog);
    }

    /**
     * 添加设备类型
     *
     * @param equipmentTagDTO 设备类型
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(EquipmentTagDTO equipmentTagDTO) {
        // 查询名称或者编码是否在数据库中已经存在
        LambdaQueryWrapper<EquipmentTag> ql = new QueryWrapper<EquipmentTag>().lambda();
        ql.eq(EquipmentTag::getName, equipmentTagDTO.getName()).or().eq(EquipmentTag::getCode,
                equipmentTagDTO.getCode());
        boolean exists = this.exists(ql);
        if (exists) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_TAG_NAME_OR_CODE_EXISTS);
        }
        // 保存
        EquipmentTag equipmentTag = BeanUtil.copyProperties(equipmentTagDTO, EquipmentTag.class);
        equipmentTag.setId(IdUtils.getSnowflake());
        equipmentTag.setEmbed(false);
        this.save(equipmentTag);
        this.saveProperty(equipmentTagDTO, equipmentTag);
        this.saveUseTemplate(equipmentTagDTO.getUseTemplateList(), equipmentTag);
    }

    @Override
    @Transactional
    public void modify(EquipmentTagDTO equipmentTagDTO) {
        LambdaQueryWrapper<EquipmentTag> ql = new QueryWrapper<EquipmentTag>().lambda();
        ql.eq(EquipmentTag::getName, equipmentTagDTO.getName()).or().eq(EquipmentTag::getCode,
                equipmentTagDTO.getCode());
        boolean exists = this.exists(ql);
        if (exists) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_TAG_NAME_OR_CODE_EXISTS);
        }
        EquipmentTag equipmentTag = BeanUtil.copyProperties(equipmentTagDTO, EquipmentTag.class);
        this.updateById(equipmentTag);
        this.saveProperty(equipmentTagDTO, equipmentTag);
        this.saveUseTemplate(equipmentTagDTO.getUseTemplateList(), equipmentTag);
    }


    @Override
    public List<EquipmentTagDTO> listWithPropertyAndUseTemplate() {
        List<EquipmentTag> list = this.list();
        List<EquipmentTagProperty> equipmentTagPropertyList = tagPropertyMapper.selectList();
        List<EquipmentTagUseTemplate> equipmentTagUseTemplates = equipmentUseTemplateMapper.selectList();
        return list.stream().map(item -> {
            EquipmentTagDTO equipmentTagDTO = BeanUtil.copyProperties(item, EquipmentTagDTO.class);
            List<EquipmentTagProperty> statusProperties =
                    equipmentTagPropertyList.stream().filter(property -> property.getTagId().equals(item.getId()) && property.getPropertyType()
                            .equals(PropertyTypeEnum.EQUIPMENT_STATUS.getCode())).collect(Collectors.toList());
            List<EquipmentTagProperty> dataProperties =
                    equipmentTagPropertyList.stream().filter(property -> property.getTagId().equals(item.getId()) && property.getPropertyType()
                            .equals(PropertyTypeEnum.TAG_DATA_PROPERTY.getCode())).collect(Collectors.toList());
            List<EquipmentTagProperty> infoProperties =
                    equipmentTagPropertyList.stream().filter(property -> property.getTagId().equals(item.getId()) && property.getPropertyType()
                            .equals(PropertyTypeEnum.TAG_PROPERTY.getCode())).collect(Collectors.toList());
            List<EquipmentTagUseTemplateDTO> equipmentTagUseTemplateDTOS =
                    BeanUtil.copyToList(equipmentTagUseTemplates.stream().filter(useTemplate -> useTemplate.getTagId().equals(item.getId()))
                            .collect(Collectors.toList()), EquipmentTagUseTemplateDTO.class);
            equipmentTagDTO.setUseTemplateList(equipmentTagUseTemplateDTOS);
            equipmentTagDTO.setStatusPropertyList(BeanUtil.copyToList(statusProperties, EquipmentTagPropertyDTO.class));
            equipmentTagDTO.setDataPropertyList(BeanUtil.copyToList(dataProperties, EquipmentTagPropertyDTO.class));
            equipmentTagDTO.setInfoPropertyList(BeanUtil.copyToList(infoProperties, EquipmentTagPropertyDTO.class));
            return equipmentTagDTO;
        }).collect(Collectors.toList());
    }


    @Override
    public List<EquipmentTagDTO> selectChildren(Long tagId) {
        List<EquipmentTagDTO> equipmentTagDTOS = new ArrayList<>();
        List<EquipmentTag> equipmentTags = tagMapper.selectList();
        List<EquipmentTagDTO> filterChildren = this.filterChildren(equipmentTags, tagId);
        if (CollectionUtil.isEmpty(filterChildren)) {
            return equipmentTagDTOS;
        }
        filterChildren.forEach(item -> {
            EquipmentTagDTO equipmentTagDTO = BeanUtil.copyProperties(item, EquipmentTagDTO.class);
            equipmentTagDTOS.add(equipmentTagDTO);
            List<EquipmentTagDTO> children = this.filterChildren(equipmentTags, item.getId());
            if (CollectionUtil.isNotEmpty(children)) {
                equipmentTagDTOS.addAll(children);
            }
        });
        return equipmentTagDTOS;
    }

    private List<EquipmentTagDTO> filterChildren(List<EquipmentTag> equipmentTags, Long parentId) {
        if (CollectionUtil.isEmpty(equipmentTags)) {
            return new ArrayList<>();
        }
        return equipmentTags.stream().filter(item -> item.getParentId().equals(parentId)).map(item -> BeanUtil.copyProperties(item, EquipmentTagDTO.class)).collect(Collectors.toList());
    }

    /**
     * 删除设备类型
     * <p>
     * 有子分类不能删除
     * 系统默认不能删除
     *
     * @param id id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EquipmentTag equipmentTag = this.getById(id);
        Optional.ofNullable(equipmentTag).orElseThrow(() -> new BmosException(PlatformResponseCode.EQUIPMENT_TAG_NOT_EXISTS));
        if (equipmentTag.getEmbed()) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_TAG_EMBED_CANNOT_DELETE);
        }
        // 查找数据库是否存在子级
        if (this.exists(new QueryWrapper<EquipmentTag>().lambda().eq(EquipmentTag::getParentId, id))) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_TAG_HAS_CHILDREN);
        }
        this.removeById(id);
        equipmentUseTemplateMapper.delete(new QueryWrapper<EquipmentTagUseTemplate>().lambda().eq(EquipmentTagUseTemplate::getTagId, equipmentTag.getId()));
        LambdaQueryWrapper<EquipmentTagProperty> lambda = new QueryWrapper<EquipmentTagProperty>().lambda();
        lambda.eq(EquipmentTagProperty::getTagId, equipmentTag.getId());
        tagPropertyMapper.delete(lambda);
    }


    private void saveUseTemplate(List<EquipmentTagUseTemplateDTO> useTemplateList, EquipmentTag equipmentTag) {
        equipmentUseTemplateMapper.delete(new QueryWrapper<EquipmentTagUseTemplate>().lambda().eq(EquipmentTagUseTemplate::getTagId, equipmentTag.getId()));
        if (CollectionUtil.isEmpty(useTemplateList)) {
            return;
        }
        List<EquipmentTagUseTemplate> tagUseTemplates = BeanUtil.copyToList(useTemplateList,
                EquipmentTagUseTemplate.class);
        tagUseTemplates.forEach(item -> {
            item.setTagId(equipmentTag.getId());
        });
        if (CollectionUtil.isEmpty(tagUseTemplates)) {
            return;
        }
        equipmentUseTemplateMapper.insertBatch(tagUseTemplates);
    }

    private void saveProperty(EquipmentTagDTO equipmentTagDTO, EquipmentTag equipmentTag) {
        LambdaQueryWrapper<EquipmentTagProperty> lambda = new QueryWrapper<EquipmentTagProperty>().lambda();
        lambda.eq(EquipmentTagProperty::getTagId, equipmentTag.getId());
        tagPropertyMapper.delete(lambda);
        List<EquipmentTagProperty> equipmentTagPropertyList = new ArrayList<>();
        // 设备状态
        List<EquipmentTagPropertyDTO> statusCodeList = equipmentTagDTO.getStatusPropertyList();
        if (CollectionUtil.isNotEmpty(statusCodeList)) {
            equipmentTagPropertyList.addAll(this.saveEquipmentStatus(equipmentTag, statusCodeList));
        }
        // 设备类型数据
        List<EquipmentTagPropertyDTO> dataPropertyList = equipmentTagDTO.getDataPropertyList();
        if (CollectionUtil.isNotEmpty(dataPropertyList)) {
            equipmentTagPropertyList.addAll(this.saveEquipmentDataProperty(equipmentTag, dataPropertyList));
        }
        // 设备类型属性
        List<EquipmentTagPropertyDTO> statusPropertyList = equipmentTagDTO.getInfoPropertyList();
        if (CollectionUtil.isNotEmpty(statusPropertyList)) {
            equipmentTagPropertyList.addAll(this.saveEquipmentProperty(equipmentTag, statusPropertyList));
        }
        if (CollectionUtil.isNotEmpty(equipmentTagPropertyList)) {
            tagPropertyMapper.insertBatch(equipmentTagPropertyList);
        }
    }

    private List<EquipmentTagProperty> saveEquipmentDataProperty(EquipmentTag equipmentTag,
                                                                 List<EquipmentTagPropertyDTO> dataPropertyList) {
        // 校验code是否重复
        Map<String, List<EquipmentTagPropertyDTO>> listMap =
                dataPropertyList.stream().collect(Collectors.groupingBy(EquipmentTagPropertyDTO::getCode));
        List<String> repeatCode =
                listMap.entrySet().stream().filter(entry -> entry.getValue().size() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(repeatCode)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_TAG_DATA_PROPERTY_CODE_REPEAT,
                    String.join(",", repeatCode));
        }
        return dataPropertyList.stream().map(property -> {
            EquipmentTagProperty equipmentTagProperty = BeanUtil.copyProperties(property, EquipmentTagProperty.class);
            equipmentTagProperty.setPropertyType(PropertyTypeEnum.TAG_DATA_PROPERTY.getCode());
            equipmentTagProperty.setTagId(equipmentTag.getId());
            equipmentTagProperty.setEmbed(false);
            return equipmentTagProperty;
        }).collect(Collectors.toList());
    }

    private List<EquipmentTagProperty> saveEquipmentProperty(EquipmentTag equipmentTag,
                                                             List<EquipmentTagPropertyDTO> dataPropertyList) {
        // 校验code是否重复
        Map<String, List<EquipmentTagPropertyDTO>> listMap =
                dataPropertyList.stream().collect(Collectors.groupingBy(EquipmentTagPropertyDTO::getCode));
        List<String> repeatCode =
                listMap.entrySet().stream().filter(entry -> entry.getValue().size() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(repeatCode)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_TAG_PROPERTY_CODE_REPEAT,
                    String.join(",", repeatCode));
        }
        return dataPropertyList.stream().map(property -> {
            EquipmentTagProperty equipmentTagProperty = BeanUtil.copyProperties(property, EquipmentTagProperty.class);
            equipmentTagProperty.setPropertyType(PropertyTypeEnum.TAG_PROPERTY.getCode());
            equipmentTagProperty.setTagId(equipmentTag.getId());
            equipmentTagProperty.setEmbed(false);
            return equipmentTagProperty;
        }).collect(Collectors.toList());
    }

    private List<EquipmentTagProperty> saveEquipmentStatus(EquipmentTag equipmentTag,
                                                           List<EquipmentTagPropertyDTO> statusCodeList) {
        // 校验code是否重复
        Map<String, List<EquipmentTagPropertyDTO>> listMap =
                statusCodeList.stream().collect(Collectors.groupingBy(EquipmentTagPropertyDTO::getCode));
        List<String> repeatCode =
                listMap.entrySet().stream().filter(entry -> entry.getValue().size() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(repeatCode)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_TAG_STATUS_PROPERTY_CODE_REPEAT,
                    String.join(",", repeatCode));
        }
        return statusCodeList.stream().map(property -> {
            EquipmentTagProperty equipmentTagProperty = BeanUtil.copyProperties(property, EquipmentTagProperty.class);
            equipmentTagProperty.setPropertyType(PropertyTypeEnum.EQUIPMENT_STATUS.getCode());
            equipmentTagProperty.setTagId(equipmentTag.getId());
            equipmentTagProperty.setEmbed(false);
            return equipmentTagProperty;
        }).collect(Collectors.toList());
    }
}