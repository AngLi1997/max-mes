package com.bmos.lims2.server.material.dto;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物料检品分类和检品树形结构DTO
 */
@Getter
@Setter
@ToString
@ApiModel("物料检品分类和检品树节点DTO")
public class MaterialInspectTreeNodeDTO implements TreeNode<MaterialInspectTreeNodeDTO, Long, String> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父节点ID")
    private Long parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("规格")
    private String specification;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty("展示名称")
    private String showName;

    @ApiModelProperty("节点类型：1-检品分类，2-检品")
    private Integer nodeType;

    @ApiModelProperty("是否为分类节点")
    private boolean categoryFlag;

    @ApiModelProperty("描述/备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("子节点")
    private List<MaterialInspectTreeNodeDTO> children;

    // 检品分类特有字段
    @ApiModelProperty("所属的业务分类")
    private Integer categoryType;

    @ApiModelProperty("平台物料分类id")
    private Long platformCategoryId;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @Override
    public String sort() {
        // 分类节点使用mergeCode排序，检品项目使用code排序
        return categoryFlag ? getMergeCode() : getCode();
    }

    /**
     * 节点类型枚举
     */
    public static class NodeType {
        public static final int CATEGORY = 1;  // 检品分类
        public static final int MATERIAL = 2;  // 检品
    }
}