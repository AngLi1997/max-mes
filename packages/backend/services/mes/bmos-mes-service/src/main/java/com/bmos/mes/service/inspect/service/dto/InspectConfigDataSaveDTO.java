package com.bmos.mes.service.inspect.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 请验单配置数据保存请求参数
 */
@Getter
@Setter
@ApiModel("请验单配置数据保存请求参数")
public class InspectConfigDataSaveDTO {

    /**
     * 请验单数据code
     */
    @ApiModelProperty("请验单数据code")
    private String code;

    /**
     * 请验单展示数据名称
     */
    @ApiModelProperty("请验单展示数据名称")
    private String showName;

    /**
     * 请验单数据名称
     */
    @ApiModelProperty("请验单数据名称")
    private String dataName;

    /**
     * 是否必填
     */
    @ApiModelProperty("是否必填")
    private Boolean required;

    /**
     * 默认值
     */
    @ApiModelProperty("默认值")
    private String defaultValue;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

}
