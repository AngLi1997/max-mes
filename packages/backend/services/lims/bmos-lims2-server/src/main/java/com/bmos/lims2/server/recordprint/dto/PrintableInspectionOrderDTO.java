package com.bmos.lims2.server.recordprint.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 可打印的检验单信息
 * @Author: yigaohui
 * @Date: 2025/11/25 10:20
 */
@Getter
@Setter
public class PrintableInspectionOrderDTO {

    /**
     * 检验单ID
     */
    private Long id;

    /**
     * 检验单号
     */
    private String orderNo;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 请验时间
     */
    private LocalDateTime inspectionRequestTime;
}


