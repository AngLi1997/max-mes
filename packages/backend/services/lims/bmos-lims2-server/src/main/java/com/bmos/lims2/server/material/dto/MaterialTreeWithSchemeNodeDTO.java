package com.bmos.lims2.server.material.dto;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Description: 物料树（含检验方案）节点DTO
 * @Author: yigaohui
 * @Date: 2025/09/05 10:00
 */
@Getter
@Setter
@ToString
@ApiModel("物料树（含检验方案）节点DTO")
public class MaterialTreeWithSchemeNodeDTO implements TreeNode<MaterialTreeWithSchemeNodeDTO, Long, String> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父节点ID")
    private Long parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty("展示名称")
    private String showName;

    @ApiModelProperty("节点类型：1-分类；2-物料；3-检验方案")
    private Integer nodeType;

    @ApiModelProperty("是否为分类节点")
    private boolean categoryFlag;

    @ApiModelProperty("是否叶子节点")
    private Boolean leaf;

    @ApiModelProperty("子节点")
    private List<MaterialTreeWithSchemeNodeDTO> children;

    // 物料可选字段
    @ApiModelProperty("物料编码")
    private String materialCode;

    // 方案可选字段
    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("方案编码")
    private String schemeCode;

    @ApiModelProperty("方案版本号（当前生效）")
    private String schemeVersion;

    @Override
    public String sort() {
        // 分类/物料节点按合并编码/编码排序；方案节点按名称排序
        if (Boolean.TRUE.equals(categoryFlag)) {
            return mergeCode;
        }
        if (nodeType != null && nodeType == NodeType.SCHEME) {
            return name;
        }
        return code;
    }

    public static class NodeType {
        public static final int CATEGORY = 1;
        public static final int MATERIAL = 2;
        public static final int SCHEME = 3;
    }
}
