package com.bmos.lims2.server.eln.entry.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 异常批注批量保存DTO
 * @Author: yigaohui
 * @Date: 2025/12/05 00:00
 */
@Getter
@Setter
@ToString
public class FormDataAnnotationBatchSaveDTO {

    @ApiModelProperty(value = "请验单id", required = true)
    @NotNull
    private Long inspectionOrderId;

    @NotEmpty
    @ApiModelProperty(value = "批号", required = true)
    private String batchNo;

    @ApiModelProperty(value = "方案id", required = true)
    @NotNull
    private Long schemeId;

    @ApiModelProperty(value = "方案版id", required = true)
    @NotNull
    private Long schemeVersionId;

    @ApiModelProperty(value = "记录项id", required = true)
    @NotNull
    private Long recordItemId;

    @ApiModelProperty(value = "方法id", required = true)
    private Long recordId;

    @ApiModelProperty(value = "任务id", required = true)
    @NotNull
    private Long taskId;

    @ApiModelProperty(value = "检验项目Id")
    private Long itemId;

    @ApiModelProperty(value = "检验项目配置id")
    private Long itemConfigId;

    @ApiModelProperty(value = "检验分析项id")
    private Long parameterId;

    @ApiModelProperty(value = "检验分析项配置id")
    private Long parameterConfigId;

    @ApiModelProperty(value = "方法版本id", required = true)
    @NotNull
    private Long recordVersionId;

    @ApiModelProperty(value = "批注集合", required = true)
    @NotEmpty
    @Valid
    private List<FormDataAnnotationBatchSaveItemDTO> items;
}


