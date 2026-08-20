package com.bmos.platform.service.factory.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.factory.model.FactoryRoomStation;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 房间与工位的绑定关系(BpFactoryRoomStation)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-21 10:17:22
 */
@Mapper
public interface FactoryRoomStationMapper extends BaseMapperX<FactoryRoomStation> {


    /**
     * 根据房间集合查询房间与工位的绑定关系
     * @param roomIdList
     * @return
     */
    default List<FactoryRoomStation> selectByRoomIdList(Collection<Long> roomIdList){
        return selectList(new LambdaQueryWrapperX<FactoryRoomStation>()
                .in(FactoryRoomStation::getRoomId,roomIdList));
    }

    /**
     * 查询房间下的工位信息
     * @param roomId
     * @return
     */
    default List<FactoryRoomStation> selectByRoomId(Long roomId){
        return selectList(new LambdaQueryWrapperX<FactoryRoomStation>()
                .eq(FactoryRoomStation::getRoomId,roomId));
    }

    /**
     * 删除房间与工位的绑定关系
     * @param roomId
     */
    default void deleteByRoomId(Long roomId){
        delete(new LambdaQueryWrapperX<FactoryRoomStation>()
                .eq(FactoryRoomStation::getRoomId,roomId));
    }

    /**
     * 校验工位是否绑定在其他房间下
     * @param stationIdList
     * @return
     */
    default boolean existByStationIdList(List<Long> stationIdList){
        return exists(new LambdaQueryWrapperX<FactoryRoomStation>()
                .in(FactoryRoomStation::getStationId,stationIdList));
    }

    /**
     * 判定工位是否绑定房间
     * @param stationId
     * @return
     */
    default boolean existsByStationId(Long stationId){
        return exists(new LambdaQueryWrapperX<FactoryRoomStation>()
                .eq(FactoryRoomStation::getStationId,stationId));
    }

    /**
     * 判定房间是否绑定工位
     * @param id
     * @return
     */
    default boolean existByRoomId(Long id){
        return exists(new LambdaQueryWrapperX<FactoryRoomStation>()
                .eq(FactoryRoomStation::getRoomId,id));
    }

    /**
     * 根据工位id查询房间与工位的绑定关系
     * @param stationId
     * @return
     */
    default FactoryRoomStation selectByStationId(Long stationId){
        return selectOne(new LambdaQueryWrapperX<FactoryRoomStation>()
                .eq(FactoryRoomStation::getStationId,stationId)
                .last("limit 1"));
    }

    /**
     * 根据工位id集合查询房间与工位的绑定关系
     * @param stationIdList
     * @return
     */
    default List<FactoryRoomStation> selectByStationIdList(List<Long> stationIdList){
        return selectList(new LambdaQueryWrapperX<FactoryRoomStation>()
                .in(FactoryRoomStation::getStationId,stationIdList));
    }

    default void deleteByStationId(Long stationId){
        delete(new LambdaQueryWrapperX<FactoryRoomStation>()
                .eq(FactoryRoomStation::getStationId,stationId));
    }

}

