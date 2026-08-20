package com.bmos.platform.service.system.expression.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.expression.pojo.KeyValue;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.service.typeHandler.KeyValueListTypeHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@ToString
@TableName(value = "bp_sys_expression", autoResultMap = true)
public class Expression extends BaseDO {
    @Tolerate
    public Expression() {}
    /**
     * 分类id
     *
     * @see ExpressionCategory#getId()
     */
    private Long expressionCategoryId;
    /**
     * 名称
     */
    private String name;
    /**
     * 计算结果
     */
    private String result;
    /**
     * 公式表达式
     */
    private String expression;

    /**
     * 公式表达式解析结果
     */
    @TableField(typeHandler = KeyValueListTypeHandler.class)
    private List<KeyValue<String, String>> expressionParse;

    /**
     * 删除字段 与业务字段判断唯一一致性 默认 0 代表未删除
     */
    private Long delNameFlag;

    /**
     * 确认状态 编辑->验证通过->确认
     * {@link com.bmos.platform.common.enums.expression.ExpressionStatusEnum}
     */
    private Integer confirmStatus;
}
