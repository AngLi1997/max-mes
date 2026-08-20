package com.bmos.mes.service.platform.expression.vo;

import com.bmos.common.tree.TreeNode;
import com.bmos.expression.pojo.KeyValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("公式树节点VO")
public class ExpressionTreeNodeVO implements TreeNode<ExpressionTreeNodeVO, Long, LocalDateTime> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("公式表达式")
    private String expression;

    @ApiModelProperty("结果")
    private String result;

    @ApiModelProperty("是否是分类")
    private Boolean categoryFlag;

    @ApiModelProperty("表达式解析值")
    List<KeyValue<String, String>> expressionParse;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("子集")
    private List<ExpressionTreeNodeVO> children;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("是否不定参数")
    private Boolean indefiniteParam;

    @ApiModelProperty("是否已被取消绑定")
    private boolean cancelBound;


    @Override
    public LocalDateTime sort() {
        return createTime;
    }
}
