package com.bmos.lims2.server.recordprint.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 可打印检验单分页查询请求DTO
 * @Author: yigaohui
 * @Date: 2025/11/25 10:20
 */
@Getter
@Setter
public class RecordPrintPageReqDTO {

    /**
     * 检验单号
     */
    private String orderNo;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 检品ID
     */
    private Long materialId;

    /**
     * 检品ID集合（用于分类查询展开）
     */
    private java.util.List<Long> materialIds;

    /**
     * 请验时间-开始（映射到lm_inspection_order.create_time）
     */
    private LocalDateTime inspectionRequestTimeStart;

    /**
     * 请验时间-结束（映射到lm_inspection_order.create_time）
     */
    private LocalDateTime inspectionRequestTimeEnd;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 20;
}


