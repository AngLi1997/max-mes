package com.bmos.mes.service.audit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "流程追溯dto")
public class AuditHistoryDTO {

    @ApiModelProperty("分类code")
    @NotBlank
    private String categoryCode;

    @ApiModelProperty("流程模型id")
    private Long id;

    @ApiModelProperty("开始时间")
    @NotBlank
    private String startTime;

    @ApiModelProperty("结束时间")
    @NotBlank
    private String endTime;

    @ApiModelProperty("流程发起人")
    private String startName;

    @ApiModelProperty(
            value = "页码，从 1 开始",
            required = true,
            example = "1"
    )
    private Integer pageNum = 1;
    @ApiModelProperty(
            value = "每页条数，最大值为 100",
            required = true,
            example = "10"
    )
    private @Max(
            value = 100L,
            message = "每页条数最大值为 100"
    ) Integer pageSize = 20;

    private String orderBy;

    private String dir;
}
