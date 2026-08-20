package com.bmos.mes.service.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.mes.service.process.model.ProcessDashboardConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/12/31 17:05
 */
@Mapper
public interface IProcessDashboardConfigMapper extends BaseMapper<ProcessDashboardConfig> {

    default ProcessDashboardConfig selectDashboardConfigByProcessId(Long processId){
        return selectOne(new LambdaQueryWrapper<ProcessDashboardConfig>()
                .eq(ProcessDashboardConfig::getProcessId, processId)
        );
    }
}
