package com.bmos.mes.service.storage.manage.job;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialManageService;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialManageBatchVO;
import com.bmos.platform.facade.notify.MessageNotifyFeign;
import com.bmos.platform.facade.notify.dto.MaterialForeWarningMessage;
import com.bmos.scheduler.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料临期提醒定时任务
 *
 * @className: MaterialExpireForeWarningJob
 * @author: yigaohui
 * @date: 2025/1/9 14:33
 * @Version: 1.0
 * @description:
 */

@Component
@Slf4j
public class MaterialExpireForeWarningJob {


    @Autowired
    private IStorageMaterialManageService storageMaterialManageService;

    @Autowired
    private MessageNotifyFeign messageNotifyFeign;

    @XxlJob("MaterialExpireForeWarning")
    public void sendWarningMessage() {
        log.info("物料临期提醒任务执行");
        // 查询临期物料
        List<StorageMaterialManageBatchVO> expireMaterial = storageMaterialManageService.queryExpireWarningList();
        if (CollectionUtil.isEmpty(expireMaterial)) {
            log.info("没有需要临期提醒的物料批次");
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        expireMaterial.forEach(item -> {
            // 发送临期提醒
            MaterialForeWarningMessage materialForeWarningMessage = new MaterialForeWarningMessage();
            materialForeWarningMessage.setMaterialCode(item.getMergeCode());
            materialForeWarningMessage.setBatchNo(item.getMaterialBatchNo());
            materialForeWarningMessage.setMaterialName(item.getMaterialName());
            materialForeWarningMessage.setTime(now);
            messageNotifyFeign.materialExpireForeWarning(materialForeWarningMessage);
        });
        // 更新临期提醒标志
        List<Long> batchIds = expireMaterial.stream().map(StorageMaterialManageBatchVO::getStorageMaterialBatchId).collect(Collectors.toList());
        storageMaterialManageService.updateBatchExpireFlag(batchIds,true);
        log.info("物料临期提醒任务执行完毕");
    }
}
