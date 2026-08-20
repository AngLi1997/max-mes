package com.bmos.lims2.server.eln.record.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.lims2.server.eln.record.entity.formula.ComponentFormulaConfig;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@TableName(value = "bm_batch_record_component", autoResultMap = true)
public class BatchRecordComponent extends BaseDO {

    @ApiModelProperty(value = "记录项id")
    private Long recordItemId;

    @ApiModelProperty(value = "时间类型")
    private String dateType;

    @ApiModelProperty(value = "组件类型")
    private String componentType;

    @ApiModelProperty(value = "组件名称")
    private String componentName;

    @ApiModelProperty(value = "空格标识")
    private Long fieldId;

    @ApiModelProperty(value = "组件关联表格最大下标值")
    private Long componentNumber;

    @ApiModelProperty(value = "精度")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long formulaPrecision;

    @ApiModelProperty(value = "公式内容（存放单选多选字段）")
    @TableField(exist = false)
    private String componentDetail;

    @ApiModelProperty(value = "标记该组件是否是一个计算结果（0否1是，默认0）")
    private Integer isResult;

    @ApiModelProperty(value = "公式id")
    private Long formulaId;

    @ApiModelProperty(value = "公式实际参数字段JSON")
    @TableField(exist = false)
    private String formulaField;

    @ApiModelProperty(value = "公式表达式")
    private String formulaExpression;

    @ApiModelProperty(value = "公式类型")
    private String formulaType;

    @ApiModelProperty(value = "修约公式code")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String roundCode;

    @ApiModelProperty(value = "记录版本id")
    private Long recordVersionId;

    @ApiModelProperty(value = "记录版本")
    private String recordVersion;

    @ApiModelProperty(value = "记录id")
    private Long recordId;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("是否已使用")
    private Boolean used;

    /**
     * 公式配置json
     * 后续公式配置往该json中放置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ComponentFormulaConfig formulaConfig;
}
