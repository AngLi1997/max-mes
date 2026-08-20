package com.bmos.platform.service.system.menu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("终端类型VO")
@Getter
@Setter
@ToString
public class TerminalTypeVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("TAB名称")
    private String terminalName;

}
