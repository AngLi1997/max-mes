package com.bmos.mes.service.inspect.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * 发起请验信息的DTO
 */
@Setter
@Getter
@ApiModel(value = "发起请验信息的DTO")
public class InitiateInspectInfoDTO {

    /**
     * 请验单配置数据id
     */
    @ApiModelProperty("请验单配置数据id")
    @NotNull
    private Long inspectConfigDataId;

    /**
     * 展示名称
     */
    @ApiModelProperty("展示名称")
    @NotNull
    private String showName;

    @ApiModelProperty("请验单数据code")
    private String code;

    @ApiModelProperty("请验单数据名称")
    private String dataName;

    @ApiModelProperty("是否必填")
    private Boolean required;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("排序 请验详情时前端显示的排序")
    private Integer sort;

}
