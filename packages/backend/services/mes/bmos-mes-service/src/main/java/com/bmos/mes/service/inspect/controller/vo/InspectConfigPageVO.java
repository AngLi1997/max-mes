package com.bmos.mes.service.inspect.controller.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 请验单配置详情
 */
@Getter
@Setter
@ApiModel("请验单配置分页VO")
public class InspectConfigPageVO {

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
     * 请验单创建时间
     */
    @ApiModelProperty("请验单创建时间")
    private LocalDateTime createTime;

    /**
     * 最后更新人
     */
    @ApiModelProperty("最后更新人")
    private String updateShowName;

    /**
     * 最后更新时间
     */
    @ApiModelProperty("最后更新时间")
    private LocalDateTime updateTime;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean enable;

}
