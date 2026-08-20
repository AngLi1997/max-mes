package com.bmos.mes.service.weigh.centre.config.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentreStation;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 称量中心工位关联表
 * @author liang
 * @version 1.0.0
 * @date 2024/7/3 19:05
 */
@Mapper
public interface IWeighCentreStationMapper extends BaseMapperX<WeighCentreStation> {

    default List<Long> queryStationIdsByCentreId(Long weighCentreId){
        if (weighCentreId == null){
            return new ArrayList<>();
        }
        List<WeighCentreStation> list = selectList(new LambdaQueryWrapper<WeighCentreStation>()
                .eq(WeighCentreStation::getWeighCentreId, weighCentreId)
        );
        if (CollectionUtil.isEmpty(list)){
            return new ArrayList<>();
        }
        return list.stream().map(WeighCentreStation::getStationId).collect(Collectors.toList());
    }

    /**
     * 绑定工位
     * @param weighCentreId 称量中心id
     * @param stationIds 货位ids
     */
    default void bind(Long weighCentreId, List<Long> stationIds){
        if (weighCentreId == null){
            return;
        }
        // 解绑之前的
        unbind(weighCentreId);
        if (CollectionUtil.isEmpty(stationIds)){
            return;
        }
        List<WeighCentreStation> weighCentreStations = stationIds.stream()
                .map(stationId -> {
                    WeighCentreStation weighCentreStation = new WeighCentreStation();
                    weighCentreStation.setWeighCentreId(weighCentreId);
                    weighCentreStation.setStationId(stationId);
                    return weighCentreStation;
                })
                .collect(Collectors.toList());
        insertBatch(weighCentreStations);
    }

    /**
     * 解绑工位
     * @param weighCentreId 称量中心id
     */
    default void unbind(Long weighCentreId){
        if (weighCentreId == null){
            return;
        }
        delete(new LambdaQueryWrapper<WeighCentreStation>()
                .eq(WeighCentreStation::getWeighCentreId, weighCentreId)
        );
    }

    default List<WeighCentreStation> queryCentreIdsByStationIds(List<Long> stationIdList){
        if (CollectionUtil.isEmpty(stationIdList)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighCentreStation>()
                .in(WeighCentreStation::getStationId, stationIdList)
        );
    }
}
