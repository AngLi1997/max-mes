package com.bmos.lims2.server.inspect.scheme.dto.request;

import lombok.Data;

import java.util.List;

/**
 * 保存方案版本检验项目-分析项DTO
 * 前端直接选择分析项传入，后台按检验项目去重分组保存
 */
@Data
public class InspectionSchemeVersionSaveItemsDTO {

    private Long schemeId;

    private Long versionId;

    private List<ParameterItemDTO> parameters;

    @Data
    public static class ParameterItemDTO {
        private Long inspectItemId;
        private Long parameterId;
        private String standardRule;
        private Boolean isReportable;
        private Boolean isExecutable;
    }
}
