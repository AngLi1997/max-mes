package com.bmos.platform.service.system.expression.vo;

import com.bmos.expression.pojo.KeyValue;
import com.bmos.platform.common.enums.expression.ExpressionStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("ExpressionPageVo:公式表达分页查询VO")
public class ExpressionPageVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("分类id")
    private Long expressionCategoryId;

    @ApiModelProperty("分类名称")
    private String expressionCategoryName;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("计算结果")
    private String result;

    @ApiModelProperty("公式表达式")
    private String expression;

    @ApiModelProperty("公式表达式解析结果")
    private List<KeyValue<String, String>> expressionParse;

    @ApiModelProperty("确认状态")
    private Integer confirmStatus;

    public ExpressionStatusEnum getConfirmStatus() {
        return ExpressionStatusEnum.getByValue(confirmStatus);
    }
}
