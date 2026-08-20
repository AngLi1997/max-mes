package com.bmos.mes.service.lotrelease.template.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 批签发模板分页查询参数
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 14:15
 */
@Data
@ApiModel("批签发模板分页查询参数")
@EqualsAndHashCode(callSuper = true)
public class LotReleaseTemplatePageQuery extends BasePage {

    @ApiModelProperty(value = "模板分类id", example = "1")
    private Long categoryId;

    @ApiModelProperty(value = "模板名称", example = "模板名称")
    @Length(max = 100)
    private String name;
}
