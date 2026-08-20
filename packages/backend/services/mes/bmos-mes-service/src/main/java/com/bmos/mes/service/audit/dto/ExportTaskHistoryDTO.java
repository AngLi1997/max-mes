package com.bmos.mes.service.audit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@ApiModel(value = "节点任务导出dto")
public class ExportTaskHistoryDTO {

    @ApiModelProperty("实例id")
    @NotBlank
    private String processInstanceId;

    @ApiModelProperty("分类编码")
    @NotBlank
    private String categoryCode;
}
