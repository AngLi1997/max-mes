package com.bmos.wms.service.job;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.scheduler.core.handler.annotation.XxlJob;
import com.bmos.wms.service.inventory.mapper.IInventoryBatchMapper;
import com.bmos.wms.service.inventory.model.InventoryBatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/10 21:17
 */
@Component
@Slf4j
public class ScheduleJob {

    @Resource
    private IInventoryBatchMapper inventoryBatchMapper;

    /**
     * 刷新批次可用状态
     */
    @XxlJob("refreshInventoryBatchAvailable")
    public void refreshInventoryBatchAvailable() {
        log.info("刷新批次可用状态");
        List<InventoryBatch> inventoryBatches = inventoryBatchMapper.queryPendingRefreshAvailableBatch(LocalDate.now());
        if (CollectionUtil.isEmpty(inventoryBatches)) {
            return;
        }
        inventoryBatches.forEach(item -> item.setAvailable(false));
        inventoryBatchMapper.updateBatch(inventoryBatches);
    }
}
