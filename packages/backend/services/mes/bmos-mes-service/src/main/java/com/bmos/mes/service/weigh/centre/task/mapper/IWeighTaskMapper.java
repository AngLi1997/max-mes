package com.bmos.mes.service.weigh.centre.task.mapper;

import com.bmos.mes.service.weigh.centre.execute.dto.WeighExecuteTaskPageQuery;
import com.bmos.mes.service.weigh.centre.execute.vo.WeighExecuteTaskDetailVO;
import com.bmos.mes.service.weigh.centre.execute.vo.WeighExecuteTaskPageVO;
import com.bmos.mes.service.weigh.centre.task.dto.WeighTaskPageQuery;
import com.bmos.mes.service.weigh.centre.task.model.WeighTask;
import com.bmos.mes.service.weigh.centre.task.vo.WeighTaskPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 称量任务mapper
 * @author liang
 * @version 1.0.0
 * @date 2024/7/8 15:12
 */
@Mapper
public interface IWeighTaskMapper extends BaseMapperX<WeighTask> {

    /**
     * 根据部门id查询称量任务
     * @param pageQuery
     * @param deptIds
     * @return
     */
    List<WeighTaskPageVO> queryPage(@Param("pageQuery") WeighTaskPageQuery pageQuery, @Param("deptIds") List<Long> deptIds);

    /**
     * 根据部门id和工位id查询准备执行的称量任务
     * @param pageQuery
     * @param deptIds
     * @param stationIds
     * @param taskStatus 任务状态
     * @return
     */
    List<WeighExecuteTaskPageVO> queryExecuteTaskPageWithDeptAndStation(@Param("pageQuery") WeighExecuteTaskPageQuery pageQuery,
                                                                        @Param("deptIds") List<Long> deptIds,
                                                                        @Param("stationIds") List<Long> stationIds,
                                                                        @Param("taskStatus") Integer taskStatus
                                                                        );

    /**
     * 根据任务id查询称量任务详情
     * @param taskId 任务id
     * @return
     */
    WeighExecuteTaskDetailVO selectWeighExecuteTaskDetailById(@Param("taskId") Long taskId);
}
