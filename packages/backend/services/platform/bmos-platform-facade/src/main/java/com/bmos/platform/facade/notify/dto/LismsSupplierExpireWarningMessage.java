package com.bmos.platform.facade.notify.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zht
 * @date 2025-01-25
 */
@Data
@ApiModel("集中化供应商到期预警预警消息")
public class LismsSupplierExpireWarningMessage {

    private LocalDateTime time;

    private List<String> supplierCnShortNamelList;
}
