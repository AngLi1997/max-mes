package com.bmos.platform.service.factory.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.platform.common.GlobalConstants;
import com.bmos.platform.facade.factory.enums.RoomStatusOperateTypeEnum;
import com.bmos.platform.facade.factory.vo.BatchRoomCleanInfoVO;
import com.bmos.platform.service.factory.controller.vo.RoomLogExportVO;
import com.bmos.platform.service.factory.controller.vo.RoomLogPageVO;
import com.bmos.platform.service.factory.model.FactoryCleanRoomLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface RoomLogConverter {
    RoomLogConverter INSTANCE = Mappers.getMapper(RoomLogConverter.class);

    default List<RoomLogPageVO> convert2PageVO(List<FactoryCleanRoomLog> factoryCleanRoomLogs){
        List<RoomLogPageVO> roomLogPageVOList = new ArrayList<>();
        if (CollectionUtil.isEmpty(factoryCleanRoomLogs)){
            return roomLogPageVOList;
        }
        for (FactoryCleanRoomLog factoryCleanRoomLog : factoryCleanRoomLogs) {
            RoomLogPageVO roomLogPageVO = new RoomLogPageVO();
            roomLogPageVO.setId(factoryCleanRoomLog.getId());
            roomLogPageVO.setRoomCode(factoryCleanRoomLog.getRoomCode());
            roomLogPageVO.setRoomName(factoryCleanRoomLog.getRoomName());
            roomLogPageVO.setType(CommonEnum.getEnumByValue(RoomStatusOperateTypeEnum.class, factoryCleanRoomLog.getType()));
            roomLogPageVO.setBatchNo(factoryCleanRoomLog.getBatchNo());
            roomLogPageVO.setProductName(factoryCleanRoomLog.getProductName());
            roomLogPageVO.setProcedureName(factoryCleanRoomLog.getProcedureName());
            roomLogPageVO.setBeginTime(LocalDateTimeUtil.format(factoryCleanRoomLog.getBeginTime(), GlobalConstants.DATE_TIME_MINUTE_FORMAT));
            roomLogPageVO.setEndTime(LocalDateTimeUtil.format(factoryCleanRoomLog.getEndTime(), GlobalConstants.DATE_TIME_MINUTE_FORMAT));
            roomLogPageVO.setExpireTime(LocalDateTimeUtil.format(factoryCleanRoomLog.getExpireTime(), GlobalConstants.DATE_TIME_MINUTE_FORMAT));
            roomLogPageVO.setOperator(factoryCleanRoomLog.getOperator());
            roomLogPageVO.setVerifier(factoryCleanRoomLog.getVerifier());
            roomLogPageVO.setDescription(factoryCleanRoomLog.getDescription());
            roomLogPageVOList.add(roomLogPageVO);
        }
        return roomLogPageVOList;
    }

    default List<RoomLogExportVO> convert2ExportVO(List<FactoryCleanRoomLog> roomLogs){
        List<RoomLogExportVO> roomLogExportVOList = new ArrayList<>();
        if (CollectionUtil.isEmpty(roomLogs)){
            return roomLogExportVOList;
        }
        for (FactoryCleanRoomLog factoryCleanRoomLog : roomLogs) {
            RoomLogExportVO roomLogExportVO = new RoomLogExportVO();
            roomLogExportVO.setRoomCode(factoryCleanRoomLog.getRoomCode());
            roomLogExportVO.setRoomName(factoryCleanRoomLog.getRoomName());
            roomLogExportVO.setType(CommonEnum.getEnumByValue(RoomStatusOperateTypeEnum.class, factoryCleanRoomLog.getType()));
            roomLogExportVO.setBatchNo(factoryCleanRoomLog.getBatchNo());
            roomLogExportVO.setProductName(factoryCleanRoomLog.getProductName());
            roomLogExportVO.setProcedureName(factoryCleanRoomLog.getProcedureName());
            roomLogExportVO.setBeginTime(LocalDateTimeUtil.format(factoryCleanRoomLog.getBeginTime(), GlobalConstants.DATE_TIME_FORMAT));
            roomLogExportVO.setEndTime(LocalDateTimeUtil.format(factoryCleanRoomLog.getEndTime(), GlobalConstants.DATE_TIME_FORMAT));
            roomLogExportVO.setExpireTime(LocalDateTimeUtil.format(factoryCleanRoomLog.getExpireTime(), GlobalConstants.DATE_TIME_FORMAT));
            roomLogExportVO.setOperator(factoryCleanRoomLog.getOperator());
            roomLogExportVO.setVerifier(factoryCleanRoomLog.getVerifier());
            roomLogExportVO.setDescription(factoryCleanRoomLog.getDescription());
            roomLogExportVOList.add(roomLogExportVO);
        }
        return roomLogExportVOList;
    }

    default List<BatchRoomCleanInfoVO> convert2RoomCleanInfoFeignVO(List<FactoryCleanRoomLog> factoryCleanRoomLogs){
        List<BatchRoomCleanInfoVO> roomCleanInfoFeignVOList = new ArrayList<>();
        if (CollUtil.isEmpty(factoryCleanRoomLogs)){
            return roomCleanInfoFeignVOList;
        }
        for (FactoryCleanRoomLog factoryCleanRoomLog : factoryCleanRoomLogs) {
            BatchRoomCleanInfoVO roomCleanInfoFeignVO = new BatchRoomCleanInfoVO();
            roomCleanInfoFeignVO.setRoomName(factoryCleanRoomLog.getRoomName());
            roomCleanInfoFeignVO.setRoomCode(factoryCleanRoomLog.getRoomCode());
            roomCleanInfoFeignVO.setProcedureName(factoryCleanRoomLog.getProcedureName());
            roomCleanInfoFeignVO.setCleanStartTime(factoryCleanRoomLog.getBeginTime());
            roomCleanInfoFeignVO.setCleanEndTime(factoryCleanRoomLog.getEndTime());
            roomCleanInfoFeignVO.setValidTime(factoryCleanRoomLog.getExpireTime());
            roomCleanInfoFeignVO.setOperator(factoryCleanRoomLog.getOperator());
            roomCleanInfoFeignVO.setOperationType(factoryCleanRoomLog.getType());
            roomCleanInfoFeignVO.setVerifier(factoryCleanRoomLog.getVerifier());
            roomCleanInfoFeignVO.setOperateTime(factoryCleanRoomLog.getCreateTime());
            roomCleanInfoFeignVO.setDesc(factoryCleanRoomLog.getDescription());
            roomCleanInfoFeignVOList.add(roomCleanInfoFeignVO);
        }
        return roomCleanInfoFeignVOList;
    }
}
