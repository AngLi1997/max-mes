package com.bmos.wms.inspect.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 请验结果回传（与 mes-feign 同款）
 */
@Getter
@Setter
public class InspectResultCallBackDTO {

    /**
     * 请验单号
     */
    private String inspectNo;

    /**
     * 汇总结果
     */
    private String result;

    /**
     * 是否最后一次 InspectResultCloseEnum
     */
    private String closed;

    /**
     * 检项结果集合
     */
    private List<InspectResultItemDTO> inspectResultItemDTOS;
}
