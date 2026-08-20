package com.bmos.lims2.server.eln.record.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.tree.TreeNode;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.lims2.server.eln.record.entity.formula.ComponentFormulaConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "查询组件list")
public class ComponentListVO implements TreeNode<ComponentListVO, Long, Long> {

    @ApiModelProperty(value = "主键表id")
    private Long id;

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

    @ApiModelProperty(value = "公式实际参数字段JSON",hidden = true)
    @JsonIgnore
    private String formulaField;

    @ApiModelProperty(value = "公式表达式")
    private String formulaExpression;

    @ApiModelProperty(value = "公式对象",hidden = true)
    @JsonIgnore
    private FormulaVO formulaVO;

    @ApiModelProperty(value = "公式类型")
    private String formulaType;

    @ApiModelProperty(value = "修约公式code")
    private String roundCode;

    @ApiModelProperty(value = "公式详情VO")
    private List<FormulaParameterVO> formulaDetailList;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("子集")
    private List<ComponentListVO> children;

    @ApiModelProperty("是否使用")
    private Boolean used;

    @ApiModelProperty("组件公式配置")
    private ComponentFormulaConfig formulaConfig;

    public List<FormulaParameterVO> getFormulaDetailList(){
        if (StrUtil.isNotBlank(formulaField)){
            return JsonUtils.parseArray(formulaField,FormulaParameterVO.class);
        }
        return Collections.emptyList();
    }
    @Override
    public Long sort() {
        return id;
    }
}
