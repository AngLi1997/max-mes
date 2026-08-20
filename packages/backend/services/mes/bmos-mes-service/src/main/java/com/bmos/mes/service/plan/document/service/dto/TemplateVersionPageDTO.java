package com.bmos.mes.service.plan.document.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 批记录模板版本分页DTO
 */
@Getter
@Setter
@ApiModel("批记录模板版本分页DTO")
public class TemplateVersionPageDTO extends BasePage {

    /**
     * 模板信息id
     */
    @ApiModelProperty("模板信息id")
    private Long templateInfoId;

}
