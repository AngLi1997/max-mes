package com.bmos.platform.service.material.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("下发业务列表")
public class IssueBusinessVO {
    @ApiModelProperty("业务平台名称")
    private String platformName;

    @ApiModelProperty("子业务列表")
    private List<ChildBusinessVO> children;
}
