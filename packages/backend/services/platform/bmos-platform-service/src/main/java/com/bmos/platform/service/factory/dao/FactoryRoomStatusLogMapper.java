package com.bmos.platform.service.factory.dao;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.factory.model.FactoryRoomStatusLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 房间状态变更日志(BpFactoryRoomStatusLog)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-17 18:42:09
 */
@Mapper
public interface FactoryRoomStatusLogMapper extends BaseMapperX<FactoryRoomStatusLog> {
    default FactoryRoomStatusLog selectByRoomIdAndStatus(Long roomId, Integer status){
        return selectOne(new LambdaQueryWrapperX<FactoryRoomStatusLog>()
                .eq(FactoryRoomStatusLog::getRoomId, roomId)
                .eq(FactoryRoomStatusLog::getStatus, status)
                .orderByDesc(FactoryRoomStatusLog::getCreateTime)
                .last("limit 1"));
    }
}

