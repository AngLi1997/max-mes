package com.bmos.mes.service.facotry.converter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.bmos.mes.service.facotry.controller.vo.*;
import com.bmos.mes.service.facotry.service.data.FactoryRoomInfo;
import com.bmos.mes.service.facotry.service.dto.ChangeRoomStatusDTO;
import com.bmos.mes.service.facotry.service.dto.FactoryRoomCleanDTO;
import com.bmos.mes.service.facotry.service.dto.RoomMobilePageDTO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.platform.facade.factory.dto.ChangeRoomStatusFeignDTO;
import com.bmos.platform.facade.factory.dto.MobileChangeRoomStatusFeignDTO;
import com.bmos.platform.facade.factory.dto.RoomMobilePageFeignDTO;
import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import com.bmos.platform.facade.factory.vo.*;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface FactoryConverter {

    FactoryConverter INSTANCE = Mappers.getMapper(FactoryConverter.class);


    FactoryRoomInfoVO convert2RoomInfoVO(RoomInfoFeignVO roomInfoFeignVO);

    default FactoryRoomInfo convert2FactoryInfo(BusinessDataHandleBaseDTO dto, RoomCleanInfoFeignVO roomCleanInfoFeignVO, Plan plan, ProcedureModel procedureModel){
        FactoryRoomInfo factoryRoomInfo = new FactoryRoomInfo();
        factoryRoomInfo.setRoomId(roomCleanInfoFeignVO.getId());
        factoryRoomInfo.setRoomName(roomCleanInfoFeignVO.getName());
        factoryRoomInfo.setRoomCode(roomCleanInfoFeignVO.getCode());
        factoryRoomInfo.setProductNo(plan.getProductMergeCode());
        factoryRoomInfo.setProductName(plan.getProductName());
        factoryRoomInfo.setProcedureId(procedureModel.getId());
        factoryRoomInfo.setProcedureName(procedureModel.getName());
        factoryRoomInfo.setOperatorId(roomCleanInfoFeignVO.getOperatorId());
        factoryRoomInfo.setOperator(roomCleanInfoFeignVO.getOperator());
        factoryRoomInfo.setBeginTime(LocalDateTimeUtil.format(roomCleanInfoFeignVO.getBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        factoryRoomInfo.setEndTime(LocalDateTimeUtil.format(roomCleanInfoFeignVO.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        factoryRoomInfo.setCleanDate(LocalDateTimeUtil.format(roomCleanInfoFeignVO.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        factoryRoomInfo.setVerifierId(roomCleanInfoFeignVO.getVerifyId());
        factoryRoomInfo.setVerifier(roomCleanInfoFeignVO.getVerifier());
        factoryRoomInfo.setVerifyDate(LocalDateTimeUtil.format(roomCleanInfoFeignVO.getVerifyTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        factoryRoomInfo.setExpireDate(LocalDateTimeUtil.format(roomCleanInfoFeignVO.getExpireDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        factoryRoomInfo.setComponentId(dto.getComponentId());
        factoryRoomInfo.setProductPlanId(dto.getProductPlanId());
        factoryRoomInfo.setBatchNo(dto.getBatchNo());
        factoryRoomInfo.setProcessId(dto.getProcessId());
        factoryRoomInfo.setProcessVersion(dto.getProcessVersion());
        factoryRoomInfo.setRecordItemId(dto.getRecordItemId());
        factoryRoomInfo.setRecordVersionId(dto.getRecordVersionId());
        factoryRoomInfo.setProcedureStepId(dto.getProcedureStepId());
        factoryRoomInfo.setProcedureStepModelId(dto.getProcedureStepModelId());
        factoryRoomInfo.setReuse(dto.getReuse());
        factoryRoomInfo.setExpireTime(roomCleanInfoFeignVO.getExpireDate());
        factoryRoomInfo.setCopyVersion(dto.getCopyVersion());
        return factoryRoomInfo;
    }

    default ChangeRoomStatusFeignDTO convert2RoomStatusFeignDTO(BusinessDataHandleBaseDTO dto, RoomCleanInfoFeignVO cleanInfoVO,
                                                                ProcedureStepModel procedureStepModel, ProcedureModel procedureModel, String operateId){
        ChangeRoomStatusFeignDTO changeRoomStatusFeignDTO = new ChangeRoomStatusFeignDTO();
        changeRoomStatusFeignDTO.setId(cleanInfoVO.getId());
        changeRoomStatusFeignDTO.setStatus(RoomStatusEnum.OCCUPATION.getCode());
        changeRoomStatusFeignDTO.setBatchNo(dto.getBatchNo());
        changeRoomStatusFeignDTO.setProductName(procedureStepModel.getName());
        changeRoomStatusFeignDTO.setProcedureId(procedureStepModel.getProcedureModelId());
        changeRoomStatusFeignDTO.setProcedureName(procedureModel.getName());
        changeRoomStatusFeignDTO.setOperateId(operateId);
        changeRoomStatusFeignDTO.setProcedureId(procedureModel.getId());
        changeRoomStatusFeignDTO.setProcedureName(procedureModel.getName());
        changeRoomStatusFeignDTO.setVerifyId(cleanInfoVO.getVerifyId());
        return changeRoomStatusFeignDTO;
    }

    default ChangeRoomStatusFeignDTO convert2RoomStatusFeignDTO(FactoryRoomCleanDTO dto){
        ChangeRoomStatusFeignDTO changeRoomStatusFeignDTO = new ChangeRoomStatusFeignDTO();
        changeRoomStatusFeignDTO.setId(dto.getRoomId());
        changeRoomStatusFeignDTO.setStatus(RoomStatusEnum.CLEANED.getCode());
        changeRoomStatusFeignDTO.setBeginTime(LocalDateTimeUtil.parse(dto.getBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        changeRoomStatusFeignDTO.setEndTime(LocalDateTimeUtil.parse(dto.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        changeRoomStatusFeignDTO.setExpireTime(LocalDateTimeUtil.parse(dto.getExpireTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        changeRoomStatusFeignDTO.setBatchNo(dto.getBatchNo());
        changeRoomStatusFeignDTO.setProductName(dto.getProductName());
        changeRoomStatusFeignDTO.setProcedureId(dto.getProcedureModelId());
        changeRoomStatusFeignDTO.setProcedureName(dto.getProcedureName());
        changeRoomStatusFeignDTO.setOperateId(dto.getOperatorId());
        changeRoomStatusFeignDTO.setVerifyId(dto.getVerifierId());
        changeRoomStatusFeignDTO.setVerifyTime(LocalDateTime.now());
        return changeRoomStatusFeignDTO;
    }

    List<FactoryLineInfoVO> convert2LineInfoVO(List<FactoryLineFeignVO> data);

    RoomMobilePageFeignDTO convert2FactoryFeignDTO(RoomMobilePageDTO dto);

    default List<RoomMobilePageVO> convert2MobilePageVO(List<RoomMobilePageFeignVO> list){
        List<RoomMobilePageVO> res = new ArrayList<>();
        if (CollUtil.isEmpty(list)){
            return res;
        }
        for (RoomMobilePageFeignVO roomMobilePageFeignVO : list) {
            RoomMobilePageVO roomMobilePageVO = new RoomMobilePageVO();
            roomMobilePageVO.setId(roomMobilePageFeignVO.getId());
            roomMobilePageVO.setCode(roomMobilePageFeignVO.getCode());
            roomMobilePageVO.setName(roomMobilePageFeignVO.getName());
            roomMobilePageVO.setStatus(CommonEnum.getEnumByValue(RoomStatusEnum.class, roomMobilePageFeignVO.getStatus()));
            roomMobilePageVO.setExpireTime(roomMobilePageFeignVO.getExpireTime());
            res.add(roomMobilePageVO);
        }
        return res;
    }

    MobileChangeRoomStatusFeignDTO convertChangeRoomFeignDTO(ChangeRoomStatusDTO dto);

    default RoomInfoMobileVO convert2RoomInfoMobileVO(RoomInfoMobileFeignVO data){
        if (ObjectUtil.isNull(data)){
            return null;
        }
        RoomInfoMobileVO roomInfoMobileVO = new RoomInfoMobileVO();
        roomInfoMobileVO.setId(data.getId());
        roomInfoMobileVO.setCode(data.getCode());
        roomInfoMobileVO.setName(data.getName());
        roomInfoMobileVO.setStatus(CommonEnum.getEnumByValue(RoomStatusEnum.class, data.getStatus()));
        roomInfoMobileVO.setProductName(data.getProductName());
        roomInfoMobileVO.setProcedureName(data.getProcedureName());
        roomInfoMobileVO.setBatchNo(data.getBatchNo());
        roomInfoMobileVO.setExpireTime(data.getExpireTime());
        return roomInfoMobileVO;
    }

    default List<RoomInfoMobileVO> convert2RoomInfoMobileVO(List<RoomInfoFeignVO> roomInfoMobileFeignVOS){
        List<RoomInfoMobileVO> res = new ArrayList<>();
        if (CollUtil.isEmpty(roomInfoMobileFeignVOS)){
            return res;
        }
        for (RoomInfoFeignVO data : roomInfoMobileFeignVOS) {
            RoomInfoMobileVO roomInfoMobileVO = new RoomInfoMobileVO();
            roomInfoMobileVO.setId(data.getId());
            roomInfoMobileVO.setCode(data.getCode());
            roomInfoMobileVO.setName(data.getName());
            roomInfoMobileVO.setStatus(CommonEnum.getEnumByValue(RoomStatusEnum.class, data.getStatus())); // 使用新添加的映射方法
            roomInfoMobileVO.setProductName(data.getProductName());
            roomInfoMobileVO.setProcedureName(data.getProcedureName());
            roomInfoMobileVO.setBatchNo(data.getBatchNo());
            roomInfoMobileVO.setExpireTime(data.getExpireTime());
            res.add(roomInfoMobileVO);
        }
        return res;
    }

    default List<FactoryRoomVO> convert2FactoryRoomTree(List<FactoryLineDetailFeignVO> data){
        return data.stream().map(e->{
            FactoryRoomVO productionLine = new FactoryRoomVO();
            productionLine.setId(e.getId());
            productionLine.setCode(e.getCode());
            productionLine.setName(e.getName());
            productionLine.setShowName(e.getCode() + StrUtil.DASHED + e.getName());
            productionLine.setChildren(convert2FactoryRoom(e.getRoomInfoFeignVOList(), e.getId()));
            productionLine.setRoomIdPath(String.valueOf(e.getId()));
            return productionLine;
        }).collect(Collectors.toList());
    }

    default List<FactoryRoomVO> convert2FactoryRoom(List<RoomInfoFeignVO> list, Long productionLineId){
        if (CollUtil.isEmpty(list)){
            return new ArrayList<>();
        }
        return list.stream().map(e->{
            FactoryRoomVO room = new FactoryRoomVO();
            room.setId(e.getId());
            room.setCode(e.getCode());
            room.setName(e.getName());
            room.setShowName(e.getCode() + StrUtil.DASHED + e.getName());
            room.setRoomFlag(true);
            room.setRoomIdPath(productionLineId + StrUtil.DASHED + e.getId());
            return room;
        }).collect(Collectors.toList());
    }

    default List<FactoryLineModuleTreeVO> convert2LineModuleInfoVO(List<LineModuleTreeNodeFeignVO> data){
        if (CollUtil.isEmpty(data)){
            return new ArrayList<>();
        }
        List<FactoryLineModuleTreeVO> moduleTreeVOList = new ArrayList<>();
        for (LineModuleTreeNodeFeignVO feignVO : data) {
            FactoryLineModuleTreeVO factoryLineModuleTreeVO = new FactoryLineModuleTreeVO();
            factoryLineModuleTreeVO.setId(feignVO.getId());
            factoryLineModuleTreeVO.setName(feignVO.getName());
            factoryLineModuleTreeVO.setParentId(feignVO.getParentId());
            factoryLineModuleTreeVO.setCode(feignVO.getCode());
            factoryLineModuleTreeVO.setLineFlag(false);
            factoryLineModuleTreeVO.setShowName(feignVO.getCode() + StrUtil.DASHED + feignVO.getName());
            List<FactoryLineModuleTreeVO> factoryLineModuleTreeVOS = convert2FactoryLineInfoVO(feignVO.getInfoList(), feignVO.getId());
            factoryLineModuleTreeVOS.addAll(convert2LineModuleInfoVO(feignVO.getChildren()));
            factoryLineModuleTreeVO.setChildren(factoryLineModuleTreeVOS);
            moduleTreeVOList.add(factoryLineModuleTreeVO);
        }
        return moduleTreeVOList;
    }

    default List<FactoryLineModuleTreeVO> convert2FactoryLineInfoVO(List<FactoryLineFeignVO> infoList, Long parentId){
        if (CollUtil.isEmpty(infoList)){
            return new ArrayList<>();
        }
        List<FactoryLineModuleTreeVO> factoryLineModuleTreeVOS = new ArrayList<>();
        for (FactoryLineFeignVO factoryLineFeignVO : infoList) {
            FactoryLineModuleTreeVO factoryLineModuleTreeVO = new FactoryLineModuleTreeVO();
            factoryLineModuleTreeVO.setId(factoryLineFeignVO.getId());
            factoryLineModuleTreeVO.setParentId(parentId);
            factoryLineModuleTreeVO.setName(factoryLineFeignVO.getName());
            factoryLineModuleTreeVO.setLineFlag(true);
            factoryLineModuleTreeVO.setCode(factoryLineFeignVO.getCode());
            factoryLineModuleTreeVO.setShowName(factoryLineFeignVO.getCode() + StrUtil.DASHED + factoryLineFeignVO.getName());
            factoryLineModuleTreeVOS.add(factoryLineModuleTreeVO);
        }
        return factoryLineModuleTreeVOS;
    }

    List<FactoryRoomAuthUserVO> convert2AuthUserVO(List<FeignUserVO> feignUserVOS);
}
