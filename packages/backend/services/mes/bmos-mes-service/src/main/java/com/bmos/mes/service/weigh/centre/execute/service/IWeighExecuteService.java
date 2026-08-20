package com.bmos.mes.service.weigh.centre.execute.service;

import com.bmos.mes.service.weigh.centre.execute.dto.*;
import com.bmos.mes.service.weigh.centre.execute.vo.*;

import java.util.Collection;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 16:15
 */
public interface IWeighExecuteService {

    /**
     * 根据任务id查询任务详情
     * @param taskId 任务id
     * @return 任务详情
     */
    WeighExecuteTaskDetailVO queryTaskById(Long taskId);

    /**
     * 根据需求id查询需求详情
     * @param requirementId 需求id
     * @return 需求详情
     */
    WeighExecuteRequirementDetailVO queryRequirementById(Long requirementId);

    /**
     * 根据任务id查询任务下未称量的需求列表
     * @param taskId 任务id
     * @return 任务下未称量的需求列表
     */
    List<WeighExecutePendingRequirementSimpleVO> queryPendingRequirementListByTaskIds(Long taskId);

    /**
     * 确认称量并添加称量消耗物料件
     * @param makeSureWeighDTO 确认称量参数
     */
    void makeSureWeigh(WeighExecuteMakeSureWeighDTO makeSureWeighDTO);

    /**
     * 添加称量消耗物料件
     * @param dto 参数
     */
    void addConsumeStorageMaterial(WeighExecuteAddConsumeMaterialWeighDTO dto);

    /**
     * 称量打码参数
     * @param weighAndPrintDTO 称量打码参数
     * @return 称量结果
     */
    WeighExecuteWeighResult weighAndPrint(WeighExecuteWeighAndPrintDTO weighAndPrintDTO);

    /**
     * 更换批次
     * @param dto
     */
    void changeBatch(WeighExecuteChangeBatchDTO dto);

    /**
     * 完成称量
     * @param weighFinishDTO 完成称量参数
     */
    void finish(WeighExecuteWeighFinishDTO weighFinishDTO);

    /**
     * 更换称量人员
     * @param dto 更换称量人员参数
     * @param validSignStatus 是否校验签名状态
     */
    void changeWeigher(WeighExecuteChangeWeigherDTO dto, boolean validSignStatus);

    /**
     * 签名
     * @param signDTO 签名参数
     */
    void sign(WeighExecuteWeighSignDTO signDTO);

    /**
     * 根据任务id查询称量结果列表
     * @param taskId 任务id
     * @return 称量结果列表
     */
    WeighExecuteWeighRecordListVO queryRecordResultByTaskId(Long taskId);

    /**
     * 刷新任务状态
     * @param taskIds 任务ids
     */
    void refreshTaskStatus(Collection<Long> taskIds);
}
