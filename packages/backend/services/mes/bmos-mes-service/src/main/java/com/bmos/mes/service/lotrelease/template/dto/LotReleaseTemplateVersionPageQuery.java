package com.bmos.mes.service.lotrelease.template.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 批签发模板版本分页查询参数
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 14:15
 */
@Data
@ApiModel("批签发模板版本分页查询参数")
@EqualsAndHashCode(callSuper = true)
public class LotReleaseTemplateVersionPageQuery extends BasePage {

    @NotNull
    @ApiModelProperty(value = "模板id", example = "1")
    private Long logReleaseTemplateId;
}