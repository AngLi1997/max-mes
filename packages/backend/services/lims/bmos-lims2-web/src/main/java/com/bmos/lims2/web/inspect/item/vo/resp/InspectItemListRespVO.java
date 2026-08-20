package com.bmos.lims2.web.inspect.item.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 检验项目列表响应VO - 用于下拉选择
 * @author system
 */
@Getter
@Setter
@ApiModel("检验项目列表响应VO")
public class InspectItemListRespVO {

    /**
     * 检验项目id
     */
    @ApiModelProperty(value = "检验项目id")
    private Long id;

    /**
     * 检验项目编码
     */
    @ApiModelProperty(value = "检验项目编码")
    private String code;

    /**
     * 检验项目名称
     */
    @ApiModelProperty(value = "检验项目名称")
    private String name;
}