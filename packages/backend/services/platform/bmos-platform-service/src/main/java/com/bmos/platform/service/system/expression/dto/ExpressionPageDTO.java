package com.bmos.platform.service.system.expression.dto;

import com.bmos.mybatis.page.BasePage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("ExpressionPageDTO:公式分页查询DTO")
public class ExpressionPageDTO extends BasePage {

    @ApiModelProperty("物料分类id")
    private Long expressionCategoryId;

    @ApiModelProperty("名称")
    private String name;

    @JsonIgnore
    @ApiModelProperty("物料分类id")
    private List<Long> expressionCategoryIds;
}
