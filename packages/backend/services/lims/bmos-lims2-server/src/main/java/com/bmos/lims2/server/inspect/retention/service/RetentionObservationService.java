package com.bmos.lims2.server.inspect.retention.service;

import com.bmos.lims2.server.inspect.retention.dto.BatchRetentionObservationSubmitDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationSubmitDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationTaskListDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationTaskPageQueryDTO;
import com.bmos.mybatis.page.CommonPage;

import java.time.LocalDate;

/**
 * @Description: 留样观察Service接口
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
public interface RetentionObservationService {

    /**
     * 为留样样品生成观察任务
     * @param sampleId 样品ID
     */
    void generateObservationTasks(Long sampleId);

    /**
     * 分页查询留样观察任务列表
     * @param queryDTO 查询条件
     * @return 留样观察任务列表分页数据
     */
    CommonPage<RetentionObservationTaskListDTO> getObservationTaskPageList(RetentionObservationTaskPageQueryDTO queryDTO);

    /**
     * 提交观察结果（单个）
     * @param submitDTO 提交数据
     */
    void submitObservation(RetentionObservationSubmitDTO submitDTO);

    /**
     * 批量提交观察结果
     * @param batchSubmitDTO 批量提交数据
     */
    void batchSubmitObservation(BatchRetentionObservationSubmitDTO batchSubmitDTO);

    /**
     * 查询临期任务数量（指定天数内到期的任务）
     * @param days 天数
     * @return 临期任务数量
     */
    Long countUpcomingTasks(Integer days);

    /**
     * 在延期时生成额外的观察任务
     * @param sampleId 样品ID
     * @param oldExpiryDate 原留样期限
     * @param newExpiryDate 新留样期限
     */
    void generateAdditionalTasksForExtension(Long sampleId, LocalDate oldExpiryDate, LocalDate newExpiryDate);
}
