package com.bmos.lims2.server.stability.scheme.dto.request;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.common.enums.ItemDurationUnitEnum;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 稳定性方案检验项目配置保存DTO
 */
@Data
public class StabilitySchemeItemSaveDTO {

    @NotNull(message = "版本ID不能为空")
    private Long versionId;

    @Valid
    private List<ItemDTO> items;

    @Data
    public static class ItemDTO {

        /** 检验项目配置记录ID（有值则更新，无值则新增） */
        private Long itemConfigId;

        @NotNull(message = "检验项目ID不能为空")
        private Long inspectItemId;

        private Integer duration;

        private ItemDurationUnitEnum timeUnit;

        private List<Long> teams;

        private Boolean isRequired;

        private String remark;

        @Valid
        private List<ParameterDTO> inspectionParameters;
    }

    @Data
    public static class ParameterDTO {

        /** 分析项配置记录ID（有值则更新，无值则新增） */
        private Long parameterConfigId;

        @NotNull(message = "分析项ID不能为空")
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
