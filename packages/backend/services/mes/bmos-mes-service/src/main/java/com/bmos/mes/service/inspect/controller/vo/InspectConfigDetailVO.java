package com.bmos.mes.service.inspect.controller.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 请验单配置详情
 */
@Getter
@Setter
@ApiModel("请验单配置详情")
public class InspectConfigDetailVO {

    /**
     * 请验单id
     */
    @ApiModelProperty("请验单id")
    private Long id;

    /**
     * 请验单名称
     */
    @ApiModelProperty("请验单名称")
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 请验单数据
     */
    @ApiModelProperty("请验单数据")
    private List<InspectConfigDataVO> dataList;

}
