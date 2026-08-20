package com.bmos.lims2.server.inspect.retention.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.constants.InspectItemConstants;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.mapper.AuditOperationLogMapper;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.order.mapper.SampleMapper;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationSubmitDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationTaskListDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationTaskPageQueryDTO;
import com.bmos.lims2.server.inspect.retention.entity.RetentionObservationLedger;
import com.bmos.lims2.server.inspect.retention.entity.RetentionObservationTask;
import com.bmos.lims2.server.inspect.retention.mapper.RetentionObservationLedgerMapper;
import com.bmos.lims2.server.inspect.retention.mapper.RetentionObservationTaskMapper;
import com.bmos.lims2.server.inspect.retention.service.RetentionObservationService;
import com.bmos.lims2.server.material.entity.Material;
import com.bmos.lims2.server.material.mapper.MaterialMapper;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 留样观察Service实现类
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@Service
@Slf4j
public class RetentionObservationServiceImpl implements RetentionObservationService {

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private RetentionObservationTaskMapper retentionObservationTaskMapper;

    @Autowired
    private RetentionObservationLedgerMapper retentionObservationLedgerMapper;

    @Autowired
    private AuditOperationLogMapper auditOperationLogMapper;

    @Autowired
    private UnitCache unitCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateObservationTasks(Long sampleId) {
        if (sampleId == null) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_ID_REQUIRED);
        }

        // 查询样品信息
        Sample sample = sampleMapper.selectById(sampleId);
        if (sample == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }

        // 验证是否是留样样品
        if (sample.getInspectItemId() == null ||
            !sample.getInspectItemId().equals(InspectItemConstants.RETENTION_INSPECT_ITEM_ID)) {
            log.warn("样品{}不是留样样品，不生成观察任务", sample.getSampleNo());
            return;
        }

        // 验证样品是否已接收
        if (!Boolean.TRUE.equals(sample.getReceived()) || sample.getReceiveTime() == null) {
            log.warn("样品{}未接收，不生成观察任务", sample.getSampleNo());
            return;
        }

        // 验证是否有留样期限
        if (sample.getRetentionExpiryDate() == null) {
            log.warn("样品{}没有留样期限，不生成观察任务", sample.getSampleNo());
            return;
        }

        // 检查是否已经生成过任务
        Long existingTaskCount = retentionObservationTaskMapper.selectCount(
            new LambdaQueryWrapper<RetentionObservationTask>()
                .eq(RetentionObservationTask::getSampleId, sampleId)
        );
        if (existingTaskCount > 0) {
            log.info("样品{}已存在观察任务，跳过生成", sample.getSampleNo());
            return;
        }

        // 计算需要生成多少年的观察任务
        LocalDate receiveDate = sample.getReceiveTime().toLocalDate();
        LocalDate expiryDate = sample.getRetentionExpiryDate();

        int years = expiryDate.getYear() - receiveDate.getYear();
        if (expiryDate.isBefore(receiveDate.plusYears(years))) {
            years--;
        }

        if (years <= 0) {
            log.warn("样品{}的留样期限小于1年，不生成观察任务", sample.getSampleNo());
            return;
        }

        // 生成每年的观察任务
        List<RetentionObservationTask> tasks = new ArrayList<>();
        for (int year = 1; year <= years; year++) {
            RetentionObservationTask task = new RetentionObservationTask();
            task.setSampleId(sampleId);
            task.setSampleNo(sample.getSampleNo());
            task.setObservationYear(year);
            // 最后一个任务的到期日期使用实际的留样到期日期，其他任务使用接收日期+年数
            if (year == years) {
                task.setDueDate(expiryDate);
            } else {
                task.setDueDate(receiveDate.plusYears(year));
            }
            task.setCompleted(false);
            tasks.add(task);
        }

        // 批量插入任务
        for (RetentionObservationTask task : tasks) {
            retentionObservationTaskMapper.insert(task);
        }

        log.info("为样品{}生成了{}个观察任务", sample.getSampleNo(), tasks.size());
    }

    @Override
    public CommonPage<RetentionObservationTaskListDTO> getObservationTaskPageList(RetentionObservationTaskPageQueryDTO queryDTO) {
        // 设置分页参数
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 查询列表
        List<RetentionObservationTaskListDTO> list = retentionObservationTaskMapper.selectObservationTaskPageList(queryDTO);

        // 使用全局缓存填充单位名称
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                if (item.getUnitId() != null) {
                    item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
                }
            });
        }

        // 返回分页数据
        return CommonPage.convertPage(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitObservation(RetentionObservationSubmitDTO submitDTO) {
        if (submitDTO == null || submitDTO.getTaskId() == null) {
            throw new BmosException(LimsResponseCode.RETENTION_OBSERVATION_TASK_ID_REQUIRED);
        }

        // 查询任务信息
        RetentionObservationTask task = retentionObservationTaskMapper.selectById(submitDTO.getTaskId());
        if (task == null) {
            throw new BmosException(LimsResponseCode.RETENTION_OBSERVATION_TASK_NOT_EXIST);
        }

        // 检查任务是否已完成
        if (Boolean.TRUE.equals(task.getCompleted())) {
            throw new BmosException(LimsResponseCode.RETENTION_OBSERVATION_TASK_ALREADY_COMPLETED);
        }

        // 检查是否存在更早的未完成任务
        List<RetentionObservationTask> earlierTasks = retentionObservationTaskMapper.selectEarlierUncompletedTasks(
            task.getSampleId(), task.getId()
        );
        if (!CollectionUtils.isEmpty(earlierTasks)) {
            throw new BmosException(LimsResponseCode.RETENTION_OBSERVATION_EARLIER_TASK_UNCOMPLETED);
        }

        // 查询样品信息
        Sample sample = sampleMapper.selectById(task.getSampleId());
        if (sample == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }

        // 查询检验单信息
        InspectionOrder order = inspectionOrderMapper.selectById(sample.getInspectionOrderId());

        // 查询物料信息
        Material material = null;
        if (order != null && order.getMaterialId() != null) {
            material = materialMapper.selectById(order.getMaterialId());
        }

        // 更新任务状态
        LocalDateTime now = LocalDateTime.now();
        String currentUserId = SysUserHolder.getUser().getUserId();
        String currentUserName = SysUserHolder.getUser().getUserName();

        task.setCompleted(true);
        task.setObservationResult(submitDTO.getObservationResult());
        task.setObservationRemark(submitDTO.getObservationRemark());
        task.setObserverId(currentUserId);
        task.setObserverName(currentUserName);
        task.setObservationTime(now);
        retentionObservationTaskMapper.updateById(task);

        // 记录观察台账
        RetentionObservationLedger ledger = new RetentionObservationLedger();
        ledger.setTaskId(task.getId());
        ledger.setSampleNo(sample.getSampleNo());
        ledger.setBatchNo(order != null ? order.getBatchNo() : null);
        if (material != null) {
            ledger.setMaterialId(material.getId());
            ledger.setMaterialName(material.getName());
            ledger.setMaterialCode(material.getCode());
            ledger.setMaterialSpec(material.getSpecification());
        }
        ledger.setQuantity(sample.getCurrentQuantity());
        ledger.setUnitId(sample.getUnitId());
        ledger.setObservationResult(submitDTO.getObservationResult());
        ledger.setObservationRemark(submitDTO.getObservationRemark());
        ledger.setObserverId(currentUserId);
        ledger.setObserverName(currentUserName);
        ledger.setObservationTime(now);
        retentionObservationLedgerMapper.insert(ledger);

        // 记录操作历史
        StringBuilder detailBuilder = new StringBuilder();
        detailBuilder.append("{");
        detailBuilder.append("\"observationYear\":").append(task.getObservationYear()).append(",");
        detailBuilder.append("\"observationResult\":").append(submitDTO.getObservationResult());
        if (submitDTO.getObservationRemark() != null && !submitDTO.getObservationRemark().isEmpty()) {
            detailBuilder.append(",\"observationRemark\":\"").append(submitDTO.getObservationRemark()).append("\"");
        }
        detailBuilder.append("}");

        AuditOperationLogEntity logEntity = AuditOperationLogEntity.builder()
            .businessId(task.getSampleId())
            .module(AuditBusinessModule.RETENTION_SAMPLE_MANAGE.name())
            .operationType(OperationType.RETENTION_OBSERVATION.getValue())
            .detail(detailBuilder.toString())
            .remark("")
            .build();
        auditOperationLogMapper.insert(logEntity);

        log.info("留样观察任务{}提交成功，样品编号：{}，观察结果：{}",
            task.getId(), sample.getSampleNo(), submitDTO.getObservationResult());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSubmitObservation(com.bmos.lims2.server.inspect.retention.dto.BatchRetentionObservationSubmitDTO batchSubmitDTO) {
        if (batchSubmitDTO == null || batchSubmitDTO.getTasks() == null || batchSubmitDTO.getTasks().isEmpty()) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "观察任务列表不能为空");
        }

        log.info("开始批量提交留样观察结果，任务数量：{}", batchSubmitDTO.getTasks().size());

        int successCount = 0;
        int failureCount = 0;
        StringBuilder errorMessages = new StringBuilder();

        for (com.bmos.lims2.server.inspect.retention.dto.BatchRetentionObservationSubmitDTO.ObservationTaskItem item : batchSubmitDTO.getTasks()) {
            try {
                // 为每个任务创建独立的提交DTO
                RetentionObservationSubmitDTO submitDTO = new RetentionObservationSubmitDTO();
                submitDTO.setTaskId(item.getTaskId());
                submitDTO.setObservationResult(item.getObservationResult());
                submitDTO.setObservationRemark(item.getObservationRemark());

                // 调用单个提交方法
                submitObservation(submitDTO);
                successCount++;
            } catch (Exception e) {
                failureCount++;
                errorMessages.append("任务ID ").append(item.getTaskId()).append(" 提交失败: ")
                    .append(e.getMessage()).append("; ");
                log.error("批量提交留样观察任务失败，任务ID：{}，错误信息：{}", item.getTaskId(), e.getMessage(), e);
            }
        }

        log.info("批量提交留样观察结果完成，成功：{}，失败：{}", successCount, failureCount);

        if (failureCount > 0) {
            throw new BmosException(LimsResponseCode.RETENTION_OBSERVATION_BATCH_SUBMIT_PARTIAL_FAILED,
                String.valueOf(successCount), String.valueOf(failureCount), errorMessages.toString());
        }
    }

    @Override
    public Long countUpcomingTasks(Integer days) {
        if (days == null || days <= 0) {
            days = 7; // 默认查询7天内
        }
        return retentionObservationTaskMapper.countUpcomingTasks(days);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateAdditionalTasksForExtension(Long sampleId, LocalDate oldExpiryDate, LocalDate newExpiryDate) {
        if (sampleId == null) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_ID_REQUIRED);
        }
        if (oldExpiryDate == null || newExpiryDate == null) {
            log.warn("样品{}的原期限或新期限为空，不生成额外观察任务", sampleId);
            return;
        }

        // 查询样品信息
        Sample sample = sampleMapper.selectById(sampleId);
        if (sample == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }

        // 验证样品是否已接收
        if (!Boolean.TRUE.equals(sample.getReceived()) || sample.getReceiveTime() == null) {
            log.warn("样品{}未接收，不生成额外观察任务", sample.getSampleNo());
            return;
        }

        // 计算接收日期
        LocalDate receiveDate = sample.getReceiveTime().toLocalDate();

        // 计算原期限对应的年数
        int oldYears = oldExpiryDate.getYear() - receiveDate.getYear();
        if (oldExpiryDate.isBefore(receiveDate.plusYears(oldYears))) {
            oldYears--;
        }

        // 计算新期限对应的年数
        int newYears = newExpiryDate.getYear() - receiveDate.getYear();
        if (newExpiryDate.isBefore(receiveDate.plusYears(newYears))) {
            newYears--;
        }

        // 如果新期限没有增加年数，不需要生成额外任务
        if (newYears <= oldYears) {
            log.info("样品{}延期后年数未增加（原{}年，新{}年），无需生成额外观察任务",
                sample.getSampleNo(), oldYears, newYears);
            // 但仍需要更新最后一个任务的到期日期为新的实际留样期限
            if (oldYears > 0) {
                RetentionObservationTask lastTask = retentionObservationTaskMapper.selectOne(
                    new LambdaQueryWrapper<RetentionObservationTask>()
                        .eq(RetentionObservationTask::getSampleId, sampleId)
                        .eq(RetentionObservationTask::getObservationYear, oldYears)
                );
                if (lastTask != null && !lastTask.getDueDate().equals(newExpiryDate)) {
                    lastTask.setDueDate(newExpiryDate);
                    retentionObservationTaskMapper.updateById(lastTask);
                    log.info("更新样品{}第{}年任务的到期日期为{}", sample.getSampleNo(), oldYears, newExpiryDate);
                }
            }
            return;
        }

        // 生成额外的观察任务（从 oldYears+1 到 newYears）
        List<RetentionObservationTask> additionalTasks = new ArrayList<>();
        for (int year = oldYears + 1; year <= newYears; year++) {
            RetentionObservationTask task = new RetentionObservationTask();
            task.setSampleId(sampleId);
            task.setSampleNo(sample.getSampleNo());
            task.setObservationYear(year);
            // 最后一个任务的到期日期使用新的实际留样到期日期，其他任务使用接收日期+年数
            if (year == newYears) {
                task.setDueDate(newExpiryDate);
            } else {
                task.setDueDate(receiveDate.plusYears(year));
            }
            task.setCompleted(false);
            additionalTasks.add(task);
        }

        // 批量插入任务
        for (RetentionObservationTask task : additionalTasks) {
            retentionObservationTaskMapper.insert(task);
        }

        log.info("样品{}延期从{}年延长到{}年，新增了{}个观察任务",
            sample.getSampleNo(), oldYears, newYears, additionalTasks.size());
    }
}
