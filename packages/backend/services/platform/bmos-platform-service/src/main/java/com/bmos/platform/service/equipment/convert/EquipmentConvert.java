package com.bmos.platform.service.equipment.convert;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.user.SysUser;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import com.bmos.platform.common.enums.equipment.PropertyTypeEnum;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusLogEnum;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.facade.equipment.enums.TagEquipmentStatusCodeEnum;
import com.bmos.platform.service.equipment.controller.vo.*;
import com.bmos.platform.service.equipment.model.*;
import com.bmos.platform.service.equipment.service.data.*;
import com.bmos.platform.service.equipment.service.dto.EquipmentPropertyDTO;
import com.bmos.platform.service.equipment.service.dto.EquipmentStatusDTO;
import com.bmos.platform.service.factory.model.EquipmentStation;
import com.bmos.platform.service.utils.UserUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper
public interface EquipmentConvert {

    String pattern = "yyyy-MM-dd HH:mm:ss";

    EquipmentConvert INSTANCE = Mappers.getMapper(EquipmentConvert.class);

    List<TagVO> convertTagVOList(List<EquipmentTag> equipmentTagList);

    EquipmentStatusVO convertEquipmentStatusVO(EquipmentTagProperty equipmentTagProperty);

    EquipmentPropertyVO convertEquipmentPropertyVO(EquipmentTagProperty equipmentTagProperty);

    default EquipmentPropertyInfo convert2EquipmentPropertyInfo(EquipmentPropertyDTO propertyDTO, Long equipmentId) {
        EquipmentPropertyInfo equipmentPropertyInfo = new EquipmentPropertyInfo();
        equipmentPropertyInfo.setEquipmentId(equipmentId);
        equipmentPropertyInfo.setPropertyCode(propertyDTO.getCode());
        equipmentPropertyInfo.setName(propertyDTO.getName());
        equipmentPropertyInfo.setValue(propertyDTO.getValue());
        equipmentPropertyInfo.setEmbed(propertyDTO.getEmbed());
        equipmentPropertyInfo.setRequired(propertyDTO.getRequired());
        return equipmentPropertyInfo;
    }

    default EquipmentPropertyInfo convert2EquipmentPropertyInfo(EquipmentStatusDTO statusDTO, Long equipmentId) {
        EquipmentPropertyInfo equipmentPropertyInfo = new EquipmentPropertyInfo();
        equipmentPropertyInfo.setEquipmentId(equipmentId);
        equipmentPropertyInfo.setPropertyType(PropertyTypeEnum.EQUIPMENT_STATUS.getCode());
        equipmentPropertyInfo.setFinishStatus(statusDTO.getFinishStatus());
        equipmentPropertyInfo.setPropertyCode(statusDTO.getCode());
        equipmentPropertyInfo.setName(statusDTO.getName());
        equipmentPropertyInfo.setValue(statusDTO.getValue());
        equipmentPropertyInfo.setEmbed(statusDTO.getEmbed());
        equipmentPropertyInfo.setRequired(Boolean.TRUE);
        return equipmentPropertyInfo;
    }

    EquipmentPropertyInfo convert2EquipmentPropertyInfo(EquipmentStatusDTO statusDTO);

    EquipmentTagStatusData convertEquipmentStatusData(EquipmentPropertyInfo equipmentPropertyInfo);

    List<TagData> convertTagDataList(List<EquipmentTag> equipmentTagList);

    List<EquipmentOperateLogVO> convert2OperateLogVO(List<EquipmentOperateLog> equipmentOperateLogs);

    List<EquipmentStatusLogVO> convert2StatusLogVO(List<EquipmentStatusLog> equipmentOperateLogs);

    default EquipmentTagStatusData convertEquipmentTagStatusData(EquipmentPropertyInfo equipmentPropertyInfo) {
        EquipmentTagStatusData equipmentTagStatusData = new EquipmentTagStatusData();
        equipmentTagStatusData.setId(equipmentPropertyInfo.getId());
        equipmentTagStatusData.setCode(equipmentPropertyInfo.getPropertyCode());
        equipmentTagStatusData.setName(equipmentPropertyInfo.getName());
        equipmentTagStatusData.setEmbed(equipmentPropertyInfo.getEmbed());
        equipmentTagStatusData.setValue(equipmentPropertyInfo.getValue());
        equipmentTagStatusData.setRequired(equipmentPropertyInfo.getRequired());
        equipmentTagStatusData.setFinishStatus(equipmentPropertyInfo.getFinishStatus());
        equipmentTagStatusData.setExpireDateTime(StrUtil.isNotEmpty(equipmentPropertyInfo.getActualValue()) ?
                LocalDateTimeUtil.parse(equipmentPropertyInfo.getActualValue(), pattern) : null);
        return equipmentTagStatusData;
    }

    default EquipmentPropertyData convert2EquipmentPropertyData(EquipmentPropertyInfo equipmentPropertyInfo) {
        EquipmentPropertyData equipmentPropertyData = new EquipmentPropertyData();
        equipmentPropertyData.setCode(equipmentPropertyInfo.getPropertyCode());
        equipmentPropertyData.setName(equipmentPropertyInfo.getName());
        equipmentPropertyData.setRequired(equipmentPropertyInfo.getRequired());
        equipmentPropertyData.setValue(equipmentPropertyInfo.getValue());
        equipmentPropertyData.setEmbed(equipmentPropertyInfo.getEmbed());
        return equipmentPropertyData;

    }

    default EquipmentOperateLog convertEquipmentOperateLog(EquipmentOperateLogData operateLogData, SysUser loginUser) {
        EquipmentOperateLog equipmentOperateLog = new EquipmentOperateLog();
        equipmentOperateLog.setEquipmentId(operateLogData.getEquipmentId());
        equipmentOperateLog.setEquipmentCode(operateLogData.getCode());
        equipmentOperateLog.setEquipmentName(operateLogData.getEquipmentName());
        equipmentOperateLog.setBatchNo(operateLogData.getBatchNo());
        equipmentOperateLog.setProductName(operateLogData.getProductName());
        equipmentOperateLog.setBeginTime(operateLogData.getOperateTime());
        equipmentOperateLog.setChangeType(operateLogData.getChangeType());
        equipmentOperateLog.setOperateContent(operateLogData.getOperateContent());
        equipmentOperateLog.setEndTime(operateLogData.getEndTime());
        if (Objects.nonNull(loginUser) && Objects.nonNull(loginUser.getUserId())) {
            equipmentOperateLog.setBeginOperator(loginUser.getUserId());
            equipmentOperateLog.setBeginOperatorName(loginUser.getUserName() + "-" + loginUser.getLoginName());
            equipmentOperateLog.setEndOperator(operateLogData.getOperator());
            equipmentOperateLog.setEndOperatorName(loginUser.getUserName() + "-" + loginUser.getLoginName());
        }
        if (Objects.nonNull(operateLogData.getReviewer())) {
            BaseUserDO user = UserUtils.getUser(operateLogData.getReviewer());
            equipmentOperateLog.setReviewer(operateLogData.getReviewer());
            equipmentOperateLog.setReviewerName(user.getUserName() + "-" + user.getLoginName());
        }
        equipmentOperateLog.setTemplateId(operateLogData.getTemplateId());
        return equipmentOperateLog;
    }

    default EquipmentStatusLog convertEquipmentStatusLog(EquipmentStatusLogData statusLogData, SysUser loginUser) {
        EquipmentStatusLog equipmentStatusLog = new EquipmentStatusLog();
        equipmentStatusLog.setEquipmentId(statusLogData.getEquipmentId());
        equipmentStatusLog.setEquipmentCode(statusLogData.getEquipmentCode());
        equipmentStatusLog.setEquipmentName(statusLogData.getEquipmentName());
        equipmentStatusLog.setPosition(statusLogData.getPosition());
        equipmentStatusLog.setChangeType(statusLogData.getChangeType());
        equipmentStatusLog.setOperateName(statusLogData.getOperateName());
        equipmentStatusLog.setPreStatusName(statusLogData.getPreStatusName());
        equipmentStatusLog.setStatusName(statusLogData.getStatusName());
        equipmentStatusLog.setExpireDateTime(statusLogData.getExpireDateTime());
        equipmentStatusLog.setOperateTime(statusLogData.getOperateTime());
        if (Objects.nonNull(loginUser) && StrUtil.isNotEmpty(loginUser.getUserId())) {
            equipmentStatusLog.setOperator(loginUser.getUserId());
            equipmentStatusLog.setOperatorName(loginUser.getUserName() + "-" + loginUser.getLoginName());
        }
        return equipmentStatusLog;
    }

    default EquipmentOperateLogData convertEquipmentOperateLogData(EquipmentInfo equipmentInfo,
                                                                   EquipmentStatusLogChangeType changeType) {
        EquipmentOperateLogData equipmentOperateLogData = new EquipmentOperateLogData();
        equipmentOperateLogData.setEquipmentId(equipmentInfo.getId());
        equipmentOperateLogData.setCode(equipmentInfo.getCode());
        equipmentOperateLogData.setChangeType(changeType.getValue());
        equipmentOperateLogData.setEquipmentName(equipmentInfo.getName());
        equipmentOperateLogData.setApplyStationId(equipmentInfo.getApplyStationId());
        equipmentOperateLogData.setBatchNo(equipmentInfo.getBatchNo());
        equipmentOperateLogData.setProductName(equipmentInfo.getProductName());
        equipmentOperateLogData.setOperateTime(LocalDateTime.now());
        equipmentOperateLogData.setOperateLogId(equipmentInfo.getOperateLogId());
        return equipmentOperateLogData;
    }

    default EquipmentStatusLogData convertEquipmentStatusLogData(EquipmentInfo equipmentInfo, String preStatus,
                                                                 EquipmentStatusCodeEnum equipmentStatusCodeEnum,
                                                                 EquipmentStatusLogChangeType changeType) {
        EquipmentStatusLogData equipmentStatusLogData = new EquipmentStatusLogData();
        equipmentStatusLogData.setEquipmentId(equipmentInfo.getId());
        equipmentStatusLogData.setEquipmentCode(equipmentInfo.getCode());
        equipmentStatusLogData.setEquipmentName(equipmentInfo.getName());
        equipmentStatusLogData.setChangeType(changeType);
        equipmentStatusLogData.setOperateName(equipmentStatusCodeEnum.getOperateCode());
        equipmentStatusLogData.setPreStatusName(preStatus);
        equipmentStatusLogData.setStatusName(equipmentStatusCodeEnum.getStatusLogCode());
        equipmentStatusLogData.setOperateTime(LocalDateTime.now());
        equipmentStatusLogData.setExpireDateTime(equipmentInfo.getExpireDateTime());
        return equipmentStatusLogData;

    }

    default EquipmentStatusLogData convertStatusLogData(EquipmentInfo equipmentInfo,
                                                        EquipmentPropertyInfo equipmentPropertyInfo,
                                                        TagEquipmentStatusCodeEnum tagEquipmentStatusCodeEnum,
                                                        Boolean finished,
                                                        EquipmentStatusLogChangeType changeType) {
        EquipmentStatusLogData equipmentStatusLogData = new EquipmentStatusLogData();
        equipmentStatusLogData.setEquipmentId(equipmentInfo.getId());
        equipmentStatusLogData.setEquipmentCode(equipmentInfo.getCode());
        equipmentStatusLogData.setEquipmentName(equipmentInfo.getName());
        equipmentStatusLogData.setChangeType(changeType);
        equipmentStatusLogData.setOperateName(tagEquipmentStatusCodeEnum.getOperateCode());
        equipmentStatusLogData.setPreStatusName(finished ? tagEquipmentStatusCodeEnum.getNoFinishName() :
                tagEquipmentStatusCodeEnum.getFinishName());
        equipmentStatusLogData.setStatusName(finished ? tagEquipmentStatusCodeEnum.getFinishName() :
                tagEquipmentStatusCodeEnum.getNoFinishName());
        equipmentStatusLogData.setExpireDateTime(LocalDateTimeUtil.parse(equipmentPropertyInfo.getActualValue(),
                pattern));
        equipmentStatusLogData.setOperateTime(LocalDateTime.now());
        return equipmentStatusLogData;
    }


    List<EquipmentOperateExportLogVO> convertExportOperateLog(List<EquipmentOperateLog> equipmentOperateLogList);

    List<EquipmentStatusExportLogVO> convertExportStatusLog(List<EquipmentStatusLog> equipmentOperateLogList);

    default List<EquipmentAppStationVO> convert2AppStationVO(List<EquipmentStation> equipmentStationList) {
        List<EquipmentAppStationVO> equipmentAppStationVOS = new ArrayList<>();
        if (CollectionUtil.isEmpty(equipmentStationList)) {
            return equipmentAppStationVOS;
        }
        equipmentStationList.forEach(equipmentStation -> {
            EquipmentAppStationVO equipmentAppStationVO = new EquipmentAppStationVO();
            equipmentAppStationVO.setStationId(equipmentStation.getId());
            equipmentAppStationVO.setName(equipmentStation.getName());
            equipmentAppStationVO.setStationCode(equipmentStation.getCode());
            equipmentAppStationVOS.add(equipmentAppStationVO);
        });
        return equipmentAppStationVOS;
    }

    EquipmentOperateLogVO convert2OperateLogVO(EquipmentOperateLog log);
}
