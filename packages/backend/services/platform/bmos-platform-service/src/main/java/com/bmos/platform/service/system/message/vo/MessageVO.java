package com.bmos.platform.service.system.message.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * @author renjinguang
 */
@Setter
@Getter
@Builder
@ToString
@ApiModel(value = "代办任务数量返回vo")
public class MessageVO {
    @Tolerate
    public MessageVO() {}
    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("分类code")
    private String categoryCode;
}
