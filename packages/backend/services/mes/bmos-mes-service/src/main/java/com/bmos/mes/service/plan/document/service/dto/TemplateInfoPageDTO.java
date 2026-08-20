package com.bmos.mes.service.plan.document.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 批记录模板信息分页查询DTO
 */
@Getter
@Setter
@ApiModel("批记录模板信息DTO")
public class TemplateInfoPageDTO extends BasePage {

    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String name;

    @ApiModelProperty(value = "分类id", required = true)
    private Long categoryId;

}
