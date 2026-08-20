package com.bmos.mes.service.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.process.model.ProcessDashboardConfigData;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/12/31 17:05
 */
@Mapper
public interface ProcessDashboardConfigDataMapper extends BaseMapperX<ProcessDashboardConfigData> {

    default List<ProcessDashboardConfigData> selectByDashboardConfigId(Long dashboardConfigId){
        return selectList(new LambdaQueryWrapper<ProcessDashboardConfigData>()
                .eq(ProcessDashboardConfigData::getDashboardConfigId, dashboardConfigId)
        );
    }

    default void deleteByConfigId(Long dashboardConfigId){
        delete(new LambdaQueryWrapper<ProcessDashboardConfigData>().eq(ProcessDashboardConfigData::getDashboardConfigId, dashboardConfigId));
    }
}
