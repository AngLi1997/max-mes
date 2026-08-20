package com.bmos.platform.service.factory.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.factory.model.FactoryLineStation;
import com.bmos.platform.service.factory.model.FactoryRoomStation;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * 产线与工位的直接绑定的关系(BpFactoryLineStation)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-21 17:05:46
 */
@Mapper
public interface FactoryLineStationMapper extends BaseMapperX<FactoryLineStation> {

    /**
     * 判断产线下是否绑定工位
     * @param lineId
     * @return
     */
    default boolean existsByLineId(Long lineId){
        return exists(new LambdaQueryWrapperX<FactoryLineStation>()
                .eq(FactoryLineStation::getLineId, lineId));
    }


    default void deleteByLineId(Long id){
        delete(new LambdaQueryWrapperX<FactoryLineStation>()
                .eq(FactoryLineStation::getLineId, id));
    }

    /**
     * 查询产线下绑定的工位
     * @param lineId
     * @return
     */
    default List<FactoryLineStation> selectByLineId(Long lineId){
        return selectList(new LambdaQueryWrapperX<FactoryLineStation>()
                .eq(FactoryLineStation::getLineId, lineId));
    }

    default List<FactoryLineStation> selectByLineIdList(Collection<Long> lineIdList){
        return selectList(new LambdaQueryWrapperX<FactoryLineStation>()
                .in(FactoryLineStation::getLineId, lineIdList));
    }

    default boolean existsByStationIdList(List<Long> stationIdList){
        return exists(new LambdaQueryWrapperX<FactoryLineStation>()
                .in(FactoryLineStation::getStationId, stationIdList));
    }

    /**
     * 工位是否绑定产线
     * @param stationId
     * @return
     */
    default boolean existStationBindLine(Long stationId){
        return exists(new LambdaQueryWrapperX<FactoryLineStation>()
                .eq(FactoryLineStation::getStationId, stationId));
    }

    /**
     * 根据工位id查询绑定产线
     * @param stationId
     * @return
     */
    default FactoryLineStation selectByStationId(Long stationId){
        return selectOne(new LambdaQueryWrapperX<FactoryLineStation>()
                .eq(FactoryLineStation::getStationId, stationId)
                .last("limit 1"));
    }

    /**
     * 根据工位id查询绑定产线
     * @param stationIdList
     * @return
     */
    default List<FactoryLineStation> selectByStationIdList(List<Long> stationIdList){
        return selectList(new LambdaQueryWrapperX<FactoryLineStation>()
                .in(FactoryLineStation::getStationId, stationIdList));
    }

    default void deleteByStationId(Long stationId){
        delete(new LambdaQueryWrapperX<FactoryLineStation>()
                .eq(FactoryLineStation::getStationId, stationId));
    }
}

