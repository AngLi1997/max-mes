package com.bmos.mes.service.plan.document.job;

import com.bmos.mes.service.plan.document.service.BatchRecordArchiveService;
import com.bmos.scheduler.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BatchArchiveJob {

    @Autowired
    BatchRecordArchiveService batchRecordArchiveService;

    /**
     * 定时任务
     * 定时删除批记录验证所产生的文件数据
     */
    @XxlJob("removeVerifyArchive")
    public void updateStorageBatchAvailable() {
        log.info("[定时任务] - 删除批记录验证所产生的文件数据 - 开始");
        batchRecordArchiveService.removeVerifyArchive();
        log.info("[定时任务] - 删除批记录验证所产生的文件数据 - 结束");
    }

}
