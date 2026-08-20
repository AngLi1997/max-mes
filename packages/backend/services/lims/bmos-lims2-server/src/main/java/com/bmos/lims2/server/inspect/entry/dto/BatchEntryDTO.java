package com.bmos.lims2.server.inspect.entry.dto;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 批量录入DTO
 *
 * @author system
 * @since 2025/01/30
 */
@Getter
@Setter
@ApiModel("批量录入数据对象")
public class BatchEntryDTO {

    @ApiModelProperty("录入项列表")
    @NotEmpty(message = "录入项列表不能为空")
    @Valid
    private List<EntryItemDTO> entryItems;

    /**
     * 录入项DTO
     */
    @Getter
    @Setter
    @ApiModel("录入项数据对象")
    public static class EntryItemDTO {

        @ApiModelProperty("录入记录ID（更新时需要）")
        private Long id;

        @ApiModelProperty("分析项任务ID")
        @NotNull(message = "任务ID不能为空")
        private Long taskId;

        @ApiModelProperty("数据点配置ID")
        @NotNull(message = "数据点配置ID不能为空")
        private Long dataPointConfigId;

        @ApiModelProperty("方案实验包配置ID")
        private Long packageId;

        @ApiModelProperty("方案检验项目配置ID")
        private Long itemConfigId;

        @ApiModelProperty("方案分析项配置ID")
        private Long parameterConfigId;

        @ApiModelProperty("数据点ID")
        private Long dataPointId;

        @ApiModelProperty("数据点名称")
        @NotBlank(message = "数据点名称不能为空")
        private String dataPointName;

        @ApiModelProperty("数据点类型")
        @NotNull(message = "数据点类型不能为空")
        private DataPointTypeEnum pointType;

        @ApiModelProperty("文本值/选项值")
        private String valueText;

        @ApiModelProperty("数值型结果（字符串）")
        private String valueNumber;

        @ApiModelProperty("检验时间")
        private LocalDateTime testTime;

        @ApiModelProperty("备注")
        private String remark;

        @ApiModelProperty("修改原因（更新必填）")
        private String modifyReason;
    }
}
