package com.bmos.mes.service.lotsummary.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 批次摘要分页查询参数
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 10:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("批次摘要分页查询参数")
public class LotSummaryPageQuery extends BasePage {

    @ApiModelProperty(value = "产品分类id", example = "1")
    private Long productCategoryId;

    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "批次摘要名称", example = "批次摘要名称")
    @Length(max = 100)
    private String name;
}
