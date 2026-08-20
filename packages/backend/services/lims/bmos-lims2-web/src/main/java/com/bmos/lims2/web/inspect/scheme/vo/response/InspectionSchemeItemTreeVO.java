package com.bmos.lims2.web.inspect.scheme.vo.response;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 检验项目-分析项树节点VO
 */
@Getter
@Setter
@ToString
@ApiModel("检验项目-分析项树节点")
public class InspectionSchemeItemTreeVO implements TreeNode<InspectionSchemeItemTreeVO, Long, String> {

    @ApiModelProperty("节点ID（检验项目ID 或 分析项ID）")
    private Long id;

    @ApiModelProperty("父节点ID")
    private Long parentId;

    @ApiModelProperty("节点名称")
    private String name;

    @ApiModelProperty("节点编码")
    private String code;

    @ApiModelProperty("节点类型：ITEM-检验项目，PARAMETER-分析项")
    private String nodeType;

    @ApiModelProperty("子节点")
    private List<InspectionSchemeItemTreeVO> children;

    @Override
    public String sort() {
        return getCode();
    }
}
