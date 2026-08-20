package com.bmos.lims2.server.task.util;

import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * 任务状态流转校验器
 *
 * @author yigaohui
 * @since 2025/09/19 11:00
 */
public final class TaskTransitionGuard {

    private static final Map<TaskStatusEnum, Set<TaskStatusEnum>> ALLOWED = new EnumMap<>(TaskStatusEnum.class);

    static {
        // 分配/领取 → 待完成
        ALLOWED.put(TaskStatusEnum.PENDING_ASSIGNMENT, EnumSet.of(TaskStatusEnum.PENDING_COMPLETION));

        // 待完成 → 进行中
        ALLOWED.put(TaskStatusEnum.PENDING_COMPLETION, EnumSet.of(TaskStatusEnum.IN_PROGRESS, TaskStatusEnum.TERMINATED));

        // 进行中 → 待复核
        ALLOWED.put(TaskStatusEnum.IN_PROGRESS, EnumSet.of(TaskStatusEnum.TO_REVIEW, TaskStatusEnum.TERMINATED));

        // 待复核 → 复核通过/不通过
        ALLOWED.put(TaskStatusEnum.TO_REVIEW, EnumSet.of(TaskStatusEnum.REVIEW_PASSED, TaskStatusEnum.REVIEW_REJECTED, TaskStatusEnum.TERMINATED));

        // 复核不通过 → 回到待完成/进行中（以及可再次终止）
        ALLOWED.put(TaskStatusEnum.REVIEW_REJECTED, EnumSet.of(TaskStatusEnum.PENDING_COMPLETION, TaskStatusEnum.IN_PROGRESS, TaskStatusEnum.TERMINATED));

        // 复核通过 → 待样品审核
        ALLOWED.put(TaskStatusEnum.REVIEW_PASSED, EnumSet.of(TaskStatusEnum.SAMPLE_AUDIT_PENDING, TaskStatusEnum.TERMINATED));

        // 待样品审核 → 完成 / 样品审核不通过
        ALLOWED.put(TaskStatusEnum.SAMPLE_AUDIT_PENDING, EnumSet.of(TaskStatusEnum.COMPLETED, TaskStatusEnum.SAMPLE_AUDIT_REJECTED, TaskStatusEnum.TERMINATED));

        // 样品审核不通过 → 可回流编辑/继续执行/直接完成（以及可终止）
        ALLOWED.put(TaskStatusEnum.SAMPLE_AUDIT_REJECTED, EnumSet.of(TaskStatusEnum.PENDING_COMPLETION, TaskStatusEnum.IN_PROGRESS, TaskStatusEnum.COMPLETED, TaskStatusEnum.TERMINATED));

        // 任意 → 已终止（这里在调用处单独判断允许）
        ALLOWED.put(TaskStatusEnum.TERMINATED, Collections.emptySet());

        // 已完成：不允许再迁移
        ALLOWED.put(TaskStatusEnum.COMPLETED, Collections.emptySet());
    }

    private TaskTransitionGuard() {}

    public static boolean isAllowed(TaskStatusEnum from, TaskStatusEnum to) {
        if (from == null || to == null) {
            return false;
        }
        if (to == TaskStatusEnum.TERMINATED) {
            return true;
        }
        Set<TaskStatusEnum> next = ALLOWED.get(from);
        return next != null && next.contains(to);
    }

    public static void checkOrThrow(TaskStatusEnum from, TaskStatusEnum to) {
        if (!isAllowed(from, to)) {
            throw new BmosException(LimsResponseCode.TASK_STATUS_CHANGE_ERROR,
                    from == null ? "null" : from.getName(),
                    to == null ? "null" : to.getName());
        }
    }
}


