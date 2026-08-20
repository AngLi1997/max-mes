package com.bmos.platform.service.system.code.vo;

import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.platform.common.enums.system.code.VersionStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@ApiModel("CodeRuleVersionPageVO:编码规则版本列表分页")
public class CodeRuleVersionPageVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("版本号")
    private String version;
    @ApiModelProperty("版本描述")
    private String description;
    @ApiModelProperty("编辑 EDIT 确认 CONFIRM")
    private VersionStatusEnum versionStatus;
    @ApiModelProperty("启用 停用")
    private StatusEnum status;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Integer statusIntValue;

    public void setStatusIntValue(Integer statusIntValue) {
        this.statusIntValue = statusIntValue;
        if (Objects.equals(statusIntValue, 1)){
            this.status = StatusEnum.ON;
        }else {
            this.status = StatusEnum.OFF;
        }
    }
}
