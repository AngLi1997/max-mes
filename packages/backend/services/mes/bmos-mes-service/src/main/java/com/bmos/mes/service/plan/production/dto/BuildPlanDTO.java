package com.bmos.mes.service.plan.production.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName BuildPlanDTO
 * @Description 生成生产计划dto
 * @Author Ren Jin Guang
 * @Date 2024/8/28 10:28
 */
@Setter
@Getter
@ToString
@ApiModel("生成生产计划dto")
public class BuildPlanDTO {

    @ApiModelProperty("生产计划数量")
    @NotNull
    private Integer planNumber;

    @ApiModelProperty("生产计划模板id")
    @NotNull
    private Long templateId;

    @ApiModelProperty("首批生产日期")
    @NotBlank
    private String planFirstDate;

    @ApiModelProperty("间隔时长")
    @NotNull
    private Integer duration;

    @ApiModelProperty("模板确定状态")
    private Boolean confirmed;
}
