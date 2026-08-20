package com.bmos.platform.service.material.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import serializer.I18nSerializer;

@Getter
@Setter
@ApiModel("子业务")
public class ChildBusinessVO {

    @ApiModelProperty("子业务名称")
    @JsonSerialize(using = I18nSerializer.class)
    private String childName;

    @ApiModelProperty("子业务码")
    private Integer childCode;
}
