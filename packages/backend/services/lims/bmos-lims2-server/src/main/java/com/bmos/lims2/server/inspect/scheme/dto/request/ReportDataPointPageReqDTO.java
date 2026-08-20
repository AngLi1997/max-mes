package com.bmos.lims2.server.inspect.scheme.dto.request;

import lombok.Data;

/**
 * 报告数据点分页查询请求DTO
 */
@Data
public class ReportDataPointPageReqDTO {

    /**
     * 方案ID（基于方案合并不同版本下相同数据点）
     */
    private Long schemeId;

    /**
     * 可选：检验项目ID（基础配置ID）
     */
    private Long inspectItemId;

    /**
     * 可选：分析项ID（基础配置ID）
     */
    private Long parameterId;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;
}


