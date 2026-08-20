package com.bmos.mes.service.record.model;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BatchRecordComponentBO extends BatchRecordComponent implements TreeNode<BatchRecordComponentBO, Long, Long> {

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
    private Long formulaPrecision;

    @ApiModelProperty(value = "公式内容（存放单选多选字段）")
    private String componentDetail;

    @ApiModelProperty(value = "标记该组件是否是一个计算结果（0否1是，默认0）")
    private Integer isResult;

    @ApiModelProperty(value = "公式id")
    private Long formulaId;

    @ApiModelProperty(value = "公式实际参数字段JSON")
    private String formulaField;

    @ApiModelProperty(value = "公式表达式")
    private String formulaExpression;

    @ApiModelProperty(value = "公式类型")
    private String formulaType;

    @ApiModelProperty(value = "修约公式code")
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

    private List<BatchRecordComponentBO> children;

    @Override
    public Long sort() {
        return getId();
    }
}
