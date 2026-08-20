package com.bmos.lims2.server.inspect.parameter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 分析方法树节点DTO（分类+方法），用于按分析项ID返回绑定方法树
 * @Author: yigaohui
 * @Date: 2025/10/29 00:00
 */
@Getter
@Setter
@ApiModel("分析方法树节点DTO")
public class InspectMethodTreeNodeDTO {

    @ApiModelProperty("节点ID：分类为虚拟ID，方法为真实ID")
    private Long id;

    @ApiModelProperty("父节点ID：根分类为null")
    private Long parentId;

    @ApiModelProperty("方法编码或分类编码（以方法编码分组）")
    private String code;

    @ApiModelProperty("名称：分类为‘方法’，方法为标准/版本/编码")
    private String name;

    @ApiModelProperty("展示名称：编码-名称")
    private String showName;

    @ApiModelProperty("节点类型：1-分类，2-方法")
    private Integer nodeType;

    @ApiModelProperty("子节点")
    private List<InspectMethodTreeNodeDTO> children;

    public static class NodeType {
        public static final int CATEGORY = 1;
        public static final int METHOD = 2;
    }
}


