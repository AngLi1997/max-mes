package com.bmos.mes.inspect.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 检验退回
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
