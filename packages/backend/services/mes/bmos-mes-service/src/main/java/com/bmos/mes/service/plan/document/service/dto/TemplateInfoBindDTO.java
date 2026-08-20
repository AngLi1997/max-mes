package com.bmos.mes.service.plan.document.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 模板信息绑定工艺DTO
 */
@Getter
@Setter
@ApiModel("模板信息绑定工艺DTO")
public class TemplateInfoBindDTO {

    /**
     * 模板信息id
     */
    @ApiModelProperty(value = "模板信息id", required = true)
    @NotNull
    private Long templateInfoId;

    /**
     * 工艺id
     */
    @ApiModelProperty(value = "工艺id", required = true)
    private List<Long> processIds;

}
