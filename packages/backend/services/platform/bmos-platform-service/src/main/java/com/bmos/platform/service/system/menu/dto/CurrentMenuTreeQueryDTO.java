package com.bmos.platform.service.system.menu.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.platform.common.enums.TerminalTypeEnums;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("查询当前登录人的菜单树")
public class CurrentMenuTreeQueryDTO {

    @ApiModelProperty("顶层菜单编码")
    private String rootMenuCode;

    @ApiModelProperty("是否包含功能菜单")
    private Boolean containsFunc;

    @ApiModelProperty("是否首页")
    private Boolean isFirst;

    @ApiModelEnumProperty(value = "终端类型",enumClass = TerminalTypeEnums.class)
    @EnumValidate(value = TerminalTypeEnums.class)
    private Integer terminalType;
}
