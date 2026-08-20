package com.bmos.platform.service.factory.mapper;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.factory.model.FactoryCleanRoomLog;
import com.bmos.platform.service.factory.service.dto.RoomLogPageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 房间清场日志(BpFactoryRoomLog)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-21 10:16:25
 */
@Mapper
public interface FactoryRoomLogMapper extends BaseMapperX<FactoryCleanRoomLog> {

    /**
     * 查询最新的房间日志
     * @param roomId
     * @return
     */
    default FactoryCleanRoomLog selectLatestByRoomId(Long roomId){
        return selectOne(new LambdaQueryWrapperX<FactoryCleanRoomLog>()
                .eq(FactoryCleanRoomLog::getRoomId, roomId)
                .orderByDesc(FactoryCleanRoomLog::getCreateTime)
                .last("limit 1"));
    }

    default List<FactoryCleanRoomLog> selectByParam(RoomLogPageDTO dto){
        LambdaQueryWrapperX<FactoryCleanRoomLog> wrapperX = new LambdaQueryWrapperX<>();
        if (StrUtil.isNotEmpty(dto.getRoomCode())){
            wrapperX.likeIfPresent(FactoryCleanRoomLog::getRoomCode, dto.getRoomCode());
        }
        if (StrUtil.isNotEmpty(dto.getRoomName())){
            wrapperX.likeIfPresent(FactoryCleanRoomLog::getRoomName, dto.getRoomName());
        }
        if (StrUtil.isNotEmpty(dto.getProductName())){
            wrapperX.likeIfPresent(FactoryCleanRoomLog::getProductName, dto.getProductName());
        }
        if (StrUtil.isNotEmpty(dto.getBatchNo())){
            wrapperX.likeIfPresent(FactoryCleanRoomLog::getBatchNo, dto.getBatchNo());
        }
        if (StrUtil.isNotEmpty(dto.getStartTime())){
            wrapperX.geIfPresent(FactoryCleanRoomLog::getBeginTime, LocalDateTimeUtil.parseDate(dto.getStartTime()).atStartOfDay());
        }
        if (StrUtil.isNotEmpty(dto.getEndTime())){
            wrapperX.leIfPresent(FactoryCleanRoomLog::getBeginTime, LocalDateTimeUtil.endOfDay(LocalDateTimeUtil.parseDate(dto.getEndTime()).atStartOfDay()));
        }
        if (StrUtil.isEmpty(dto.getOrderSql())){
            wrapperX.orderByDesc(FactoryCleanRoomLog::getEndTime);
        }
        return selectList(wrapperX);
    }
}

