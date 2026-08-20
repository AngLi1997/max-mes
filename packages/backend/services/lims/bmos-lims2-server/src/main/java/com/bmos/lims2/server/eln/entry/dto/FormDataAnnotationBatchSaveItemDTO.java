package com.bmos.lims2.server.eln.entry.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Description: 异常批注批量保存-数据项DTO
 * @Author: yigaohui
 * @Date: 2025/12/05 00:00
 */
@Getter
@Setter
@ToString
public class FormDataAnnotationBatchSaveItemDTO {

    @NotEmpty
    @ApiModelProperty(value = "批注值", required = true)
    private String value;

    @ApiModelProperty(value = "批注值扩展")
    private String valueExtension;

    @ApiModelProperty(value = "组件id", required = true)
    @NotNull
    private Long fieldId;

    @NotEmpty
    @ApiModelProperty(value = "组件类型", required = true)
    private String componentType;

    @NotEmpty
    @ApiModelProperty(value = "操作类型", required = true)
    private String operationType;

    @NotNull
    @ApiModelProperty(value = "操作时间", required = true)
    private LocalDateTime operationTime;

    @NotEmpty
    @ApiModelProperty(value = "操作人", required = true)
    private String operationUser;

    @ApiModelProperty(value = "备注")
    private String remark;
}


