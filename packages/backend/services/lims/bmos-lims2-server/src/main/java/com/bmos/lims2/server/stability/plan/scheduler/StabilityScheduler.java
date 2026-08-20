package com.bmos.lims2.server.stability.plan.scheduler;

import com.bmos.lims2.server.stability.plan.service.StabilityInspectPlanService;
import com.bmos.scheduler.core.context.XxlJobHelper;
import com.bmos.scheduler.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 稳定性考察计划定时任务（XXL-Job）
 *
 * <p>在 XXL-Job 管理后台添加如下调度任务：</p>
 * <ul>
 *   <li>JobHandler: stabilityTriggerDueTimepointTasks — 每天凌晨 02:00 触发</li>
 * </ul>
 */
@Component
@Slf4j
public class StabilityScheduler {

    @Autowired
    private StabilityInspectPlanService stabilityInspectPlanService;

    /**
     * 触发到期时间点任务：将 planned_date <= 今日的 NOT_STARTED 任务创建检验单
     *
     * XXL-Job Handler Name: stabilityTriggerDueTimepointTasks
     */
    @XxlJob("stabilityTriggerDueTimepointTasks")
    public void triggerDueTimepointTasks() {
        XxlJobHelper.log("开始触发到期时间点任务");
        int count = stabilityInspectPlanService.triggerDueTimepointTasks();
        XxlJobHelper.log("触发到期时间点任务完成，共处理 {} 条", count);
        log.info("[稳定性定时] 触发到期时间点任务完成，共处理 {} 条", count);
    }

}
