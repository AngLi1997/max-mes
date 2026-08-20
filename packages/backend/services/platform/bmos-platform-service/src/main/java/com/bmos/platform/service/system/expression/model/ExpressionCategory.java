package com.bmos.platform.service.system.expression.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

@Getter
@Setter
@SuperBuilder
@ToString
@TableName("bp_sys_expression_category")
public class ExpressionCategory extends BaseDO {
    @Tolerate
    public ExpressionCategory() {}
    /**
     * 父级id，默认0
     */
    private Long parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 名称
     */
    private String ancestorName;

    /**
     * 分类层级列表
     */
    private String ancestors;
}
