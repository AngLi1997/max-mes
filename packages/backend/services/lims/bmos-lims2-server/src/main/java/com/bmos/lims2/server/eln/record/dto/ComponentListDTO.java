package com.bmos.lims2.server.eln.record.dto;

import com.bmos.lims2.server.eln.record.vo.FormulaVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "组件DTO")
public class ComponentListDTO {

    @ApiModelProperty(value = "主键表id")
    private Long id;

    @ApiModelProperty(value = "记录项id")
    private Long recordItemId;

    @ApiModelProperty(value = "时间类型")
    private String dateType;

    @ApiModelProperty(value = "组件类型")
    private String componentType;

    @ApiModelProperty(value = "版本id")
    private Long recordVersionId;

    @ApiModelProperty(value = "版本号")
    private String recordVersion;

    @ApiModelProperty(value = "记录id")
    private Long recordId;

    @ApiModelProperty(value = "组件名称")
    private String componentName;

    @ApiModelProperty(value = "空格标识")
    private String fieldId;

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

    @ApiModelProperty(value = "公式对象")
    private FormulaVO formulaVO;

    @ApiModelProperty(value = "公式类型")
    private String formulaType;

    @ApiModelProperty(value = "修约公式code")
    private String roundCode;

    @ApiModelProperty("子集")
    private List<ComponentListDTO> children;

    @ApiModelProperty("是否已使用")
    private Boolean used;

    @ApiModelProperty(hidden = true)
    private Long parentId;
}
