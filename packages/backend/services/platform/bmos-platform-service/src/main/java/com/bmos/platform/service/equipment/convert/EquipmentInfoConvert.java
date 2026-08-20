package com.bmos.platform.service.equipment.convert;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.platform.facade.equipment.dto.EquipmentApplyHeartDTO;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentPropertyFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentStatusFeignVO;
import com.bmos.platform.service.equipment.controller.vo.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.bmos.platform.service.equipment.model.EquipmentInfo;
import com.bmos.platform.service.equipment.model.EquipmentTagInfo;
import com.bmos.platform.service.equipment.service.data.EquipmentPropertyData;
import com.bmos.platform.service.equipment.service.data.EquipmentTagData;
import com.bmos.platform.service.equipment.service.data.EquipmentTagStatusData;
import com.bmos.platform.service.equipment.service.data.TagData;
import com.bmos.platform.service.equipment.service.dto.EquipmentApplyOperateDTO;
import com.bmos.platform.service.equipment.service.dto.EquipmentEnableDTO;
import com.bmos.platform.service.equipment.service.dto.EquipmentSaveDTO;
import com.bmos.platform.service.equipment.service.dto.EquipmentUpdateDTO;
import com.bmos.platform.service.system.user.converter.DateConverter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Mapper
public interface EquipmentInfoConvert {

    EquipmentInfoConvert INSTANCE = Mappers.getMapper(EquipmentInfoConvert.class);

    EquipmentInfo convertToInfo(EquipmentSaveDTO dto);

    EquipmentInfo convertToUpdateInfo(EquipmentUpdateDTO dto);

    EquipmentInfoVO convertToInfoVo(EquipmentInfo info);

    List<TagVO> convertTagList(List<TagData> list);

    EquipmentAppInfoVO convertToAppInfoVo(EquipmentInfo info);

    EquipmentInfoVO convertToFeignInfoVo(EquipmentInfo info);

    default EquipmentPrintInfoVO convertToPrintInfo(EquipmentInfo equipmentInfo){
        EquipmentPrintInfoVO equipmentPrintInfoVO = new EquipmentPrintInfoVO();
        equipmentPrintInfoVO.setEquipmentId(equipmentInfo.getId());
        equipmentPrintInfoVO.setEquipmentCode(equipmentInfo.getCode());
        equipmentPrintInfoVO.setEquipmentName(equipmentInfo.getName());
        equipmentPrintInfoVO.setPrintDate(LocalDateTimeUtil.format(LocalDate.now(), DateConverter.pattern));
        return equipmentPrintInfoVO;
    }

    List<EquipmentAppPageVO> convert2EquipmentAppPapgeVO(List<EquipmentInfo> equipmentInfos);

    EquipmentApplyOperateDTO convert2OperateDTO(EquipmentApplyHeartDTO equipmentApplyHeartDTO);

    default List<EquipmentInfoVO> convertToInfoVoListWithTag(List<EquipmentInfo> equipmentInfoList, Map<Long, EquipmentTagData> tagDataMap){
        List<EquipmentInfoVO> equipmentInfoFeignVOS = new ArrayList<>();
        if (CollectionUtil.isEmpty(equipmentInfoList)){
            return equipmentInfoFeignVOS;
        }
        for (EquipmentInfo equipmentInfo : equipmentInfoList) {
            EquipmentInfoVO equipmentInfoFeignVO = convertToInfoVo(equipmentInfo);
            EquipmentTagData equipmentTagData = tagDataMap.get(equipmentInfo.getId());
            if (ObjectUtil.isEmpty(equipmentTagData)){
                continue;
            }
            equipmentInfoFeignVO.setInfoPropertyList(convert2PropertyVOList(equipmentTagData.getInfoPropertyList()));
            equipmentInfoFeignVO.setStatusPropertyList(convert2StatusDTOList(equipmentTagData.getStatusPropertyList()));
            equipmentInfoFeignVO.setDataPropertyList(convert2DataPropertyVOList(equipmentTagData.getDataPropertyList()));
            if (CollectionUtil.isEmpty(equipmentTagData.getEquipmentTagDataList())){
                continue;
            }
            equipmentInfoFeignVO.setTagNames(equipmentTagData.getEquipmentTagDataList().stream().map(TagData::getName).collect(Collectors.toList()));
            equipmentInfoFeignVOS.add(equipmentInfoFeignVO);
        }
        return equipmentInfoFeignVOS;
    }

    List<EquipmentStatusVO> convert2StatusDTOList(List<EquipmentTagStatusData> equipmentTagStatusDataList);

    List<EquipmentPropertyVO> convert2PropertyVOList(List<EquipmentPropertyData> equipmentPropertyDataList);
    List<EquipmentPropertyAcquisitionPointVO> convert2DataPropertyVOList(List<EquipmentPropertyData> equipmentPropertyDataList);
}
