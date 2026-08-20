package com.bmos.wms.inspect.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 检验退回（与 mes-feign 同款，复制以避免跨仓 mes-feign 依赖）
 */
@Getter
@Setter
public class InspectRejectDTO {

    /**
     * 请验单号
     */
    private String inspectNo;

    /**
     * 退回原因
     */
    private String reason;
}
