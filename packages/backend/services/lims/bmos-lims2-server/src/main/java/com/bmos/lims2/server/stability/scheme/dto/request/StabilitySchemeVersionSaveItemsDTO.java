package com.bmos.lims2.server.stability.scheme.dto.request;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.common.enums.ItemDurationUnitEnum;
import lombok.Data;

import java.util.List;

/**
 * 保存稳定性方案版本检验项目-分析项DTO
 * 前端直接选择分析项传入（平铺），后台按 inspectItemId 分组保存
 */
@Data
public class StabilitySchemeVersionSaveItemsDTO {

    private Long versionId;

    private List<ParameterItemDTO> parameters;

    @Data
    public static class ParameterItemDTO {
        /** 检验项目ID（用于分组） */
        private Long inspectItemId;
        private Integer duration;
        private ItemDurationUnitEnum timeUnit;
        private List<Long> teams;
        private Boolean isRequired;
        private String remark;

        /** 分析项ID */
        private Long parameterId;
        private String standardRule;
        private Boolean isExecutable;
        private Boolean isReportable;
        private ExecuteMethodEnum executeMethod;
        private String finalExpression;
        private Long recordId;
        private String recordCode;
        private Long recordVersionId;
        private Long recordItemId;
    }
}
