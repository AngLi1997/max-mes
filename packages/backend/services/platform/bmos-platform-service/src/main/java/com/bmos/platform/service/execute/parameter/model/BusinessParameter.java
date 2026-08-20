package com.bmos.platform.service.execute.parameter.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.common.enums.execute.parameter.BusinessTypeEnum;
import com.bmos.platform.common.enums.execute.parameter.ValueTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName("bp_business_parameter")
public class BusinessParameter extends BaseDO {
    /**
    * 编码
    */
    private String code;

    /**
    * 值
    */
    private String value;

    /**
     * 参数名称
     */
    private String name;

    /**
    * 值类型 1STRING 2 NUMBER 3BOOLEAN 4 ENUM 5 json
    */
    private ValueTypeEnum valueType;

    /**
    * 业务类型 1 业务 2 系统
    */
    private BusinessTypeEnum businessType;

    /**
    * 所属应用 中文
    */
    private String belong;

    /**
    * 描述
    */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String description;

    /**
    * 排序
    */
    private Integer sort;

    /**
    * 取值范围
    */
    private String valueRange;

    @TableField("is_display")
    private Boolean display;
}
