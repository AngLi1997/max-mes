package com.bmos.mes.service.process.vo;

import com.bmos.common.tree.TreeNode;
import com.bmos.mes.service.record.vo.ComponentListVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
@ToString
@ApiModel("记录项组件配置详情VO")
public class ComponentConfigDetailVO implements TreeNode<ComponentConfigDetailVO, Long, Long> {

    @ApiModelProperty("id")
    private Long id;

    /**
     * 配置信息JSON
     */
    @ApiModelProperty("配置信息JSON")
    @NotBlank
    private String configInfo;

    /**
     * 组件id
     */
    @ApiModelProperty("组件id")
    @NotNull
    private Long fieldId;

    @ApiModelProperty(value = "记录项id")
    private Long recordItemId;

    @ApiModelProperty(value = "组件类型")
    private String componentType;

    @ApiModelProperty(value = "组件名称")
    private String componentName;

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

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("子集")
    private List<ComponentConfigDetailVO> children;

    @ApiModelProperty("是否使用")
    private Boolean used;

    @ApiModelProperty("是否有权限")
    private Boolean hasRight;

    @Override
    public Long sort() {
        return id;
    }
}
