package com.bmos.mes.service.weigh.centre.task.service;

import com.bmos.mes.service.weigh.centre.execute.dto.WeighExecuteTaskPageQuery;
import com.bmos.mes.service.weigh.centre.execute.vo.WeighExecuteTaskPageVO;
import com.bmos.mes.service.weigh.centre.requirement.vo.WeighRequirementVO;
import com.bmos.mes.service.weigh.centre.task.dto.WeighTaskEditDTO;
import com.bmos.mes.service.weigh.centre.task.dto.WeighTaskInfoListQuery;
import com.bmos.mes.service.weigh.centre.task.dto.WeighTaskPageQuery;
import com.bmos.mes.service.weigh.centre.task.vo.WeighTaskAndRequirementPageVO;
import com.bmos.mes.service.weigh.centre.task.vo.WeighTaskPageVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/8 15:13
 */
public interface IWeighTaskService {

    /**
     * 手动规划
     * @param requirementIds 需求id
     */
    void programManual(List<Long> requirementIds);

    /**
     * 自动规划
     */
    void programAuto();

    /**
     * 分页查询
     * @param pageQuery 分页查询参数
     * @return
     */
    CommonPage<WeighTaskPageVO> queryPage(WeighTaskPageQuery pageQuery);

    /**
     * 查询带有称量需求分页的任务详情
     * @param pageQuery 分页查询参数
     * @return
     */
    WeighTaskAndRequirementPageVO queryRequirementListByTaskId(WeighTaskInfoListQuery pageQuery);

    /**
     * 查询称量任务对应的物料、称量中心、单位详情相同的未规划的称量需求列表
     * @param taskId 任务id
     * @return
     */
    List<WeighRequirementVO> queryUnPlanedRequirementListByTaskId(Long taskId);

    /**
     * 保存编辑
     * @param editDTO 编辑参数
     */
    void edit(WeighTaskEditDTO editDTO);

    /**
     * 确认任务
     * @param taskId 任务id
     */
    void makeSure(Long taskId);

    /**
     * 下发任务
     * @param taskId 任务id
     */
    void send(Long taskId);

    /**
     * 取消任务
     * @param taskId 任务id
     */
    void cancel(Long taskId);

    /**
     * 查询待执行任务分页
     * @param pageQuery 分页查询参数
     * @return 待执行任务分页
     */
    CommonPage<WeighExecuteTaskPageVO> queryExecuteTaskPage(WeighExecuteTaskPageQuery pageQuery);


    /**
     * 查询历史任务分页
     * @param pageQuery
     * @return
     */
    CommonPage<WeighExecuteTaskPageVO> queryHistoryTaskPage(WeighExecuteTaskPageQuery pageQuery);
}
