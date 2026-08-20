package com.bmos.platform.service.message.dto;

import com.bmos.platform.facade.notify.dto.LismsMaterialExpireWarningMessage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zht
 * @date 2025-01-25
 */
@Data
@ApiModel("集中化物料到期预警预警消息上下文")
public class LismsMaterialExpireWarningMessageContext extends MessageContextDTO {

    private LocalDateTime time;

    private List<LismsMaterialExpireWarningMessage.MaterialBatchInfo> materialBatchInfos;

    @Data
    public static class MaterialBatchInfo {
        private String materialName;
        private String batchNo;
    }
}
