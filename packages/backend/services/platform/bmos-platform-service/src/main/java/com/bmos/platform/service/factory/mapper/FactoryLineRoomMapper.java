package com.bmos.platform.service.factory.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.factory.model.FactoryLineRoom;
import com.bmos.platform.service.factory.model.FactoryLineStation;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 产线与房间的绑定关系(BpFactoryLineRoom)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-21 17:05:26
 */
@Mapper
public interface FactoryLineRoomMapper extends BaseMapperX<FactoryLineRoom> {

    /**
     * 判断房间是否与产线有关联
     * @param roomId
     * @return
     */
    default boolean existByRoomId(Long roomId){
        return exists(new LambdaQueryWrapperX<FactoryLineRoom>()
                .eq(FactoryLineRoom::getRoomId, roomId));
    }

    /**
     * 校验产线下是否绑定有房间
     * @param lineId
     * @return
     */
    default boolean existsByLineId(Long lineId){
        return exists(new LambdaQueryWrapperX<FactoryLineRoom>()
                .eq(FactoryLineRoom::getLineId, lineId));
    }

    /**
     * 删除之前的绑定关系
     * @param lineId
     */
    default void deleteByLineId(Long lineId){
        delete(new LambdaQueryWrapperX<FactoryLineRoom>()
                .eq(FactoryLineRoom::getLineId, lineId));
    }

    /**
     * 根据产线id查询绑定关系
     * @param lineId
     * @return
     */
    default List<FactoryLineRoom> selectByLineId(Long lineId){
        return selectList(new LambdaQueryWrapperX<FactoryLineRoom>()
                .eq(FactoryLineRoom::getLineId, lineId));
    }

    default List<FactoryLineRoom> selectByLineIdList(Collection<Long> lineIdList){
        return selectList(new LambdaQueryWrapperX<FactoryLineRoom>()
                .in(FactoryLineRoom::getLineId, lineIdList));
    }

    default List<FactoryLineRoom> selectByRoomId(Long roomId){
        return selectList(new LambdaQueryWrapperX<FactoryLineRoom>()
                .eq(FactoryLineRoom::getRoomId, roomId));
    }

    default List<FactoryLineRoom> selectByRoomIdList(List<Long> roomIdList){
        return selectList(new LambdaQueryWrapperX<FactoryLineRoom>()
                .in(FactoryLineRoom::getRoomId, roomIdList));
    }

    default void deleteByRoomId(Long roomId){
        delete(new LambdaQueryWrapperX<FactoryLineRoom>()
                .eq(FactoryLineRoom::getRoomId, roomId));
    }
}

