package com.bmos.mes.service.audit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Setter
@Getter
@ToString
@ApiModel(value = "流程导出dto")
public class AuditHistoryExportDTO {

    @ApiModelProperty("分类code")
    @NotBlank
    private String categoryCode;

    @ApiModelProperty("模型id")
    private Long id;

    @ApiModelProperty("开始时间")
    @NotBlank
    private String startTime;

    @ApiModelProperty("结束时间")
    @NotBlank
    private String endTime;

    @ApiModelProperty("流程名称")
    @NotBlank
    private String name;

    @ApiModelProperty("发起人")
    private String startName;

    @ApiModelProperty("流程实例id集合")
    private List<String> instanceIdList;
}
