package com.bmos.lims2.web.inspect.parameter.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 分析方法树节点响应VO（分类+方法），节点显示为“编码-名称”
 * @Author: yigaohui
 * @Date: 2025/10/29 00:00
 */
@Getter
@Setter
@ApiModel("分析方法树节点响应VO")
public class InspectMethodTreeNodeRespVO {

    @ApiModelProperty("节点ID：分类为虚拟ID，方法为真实ID")
    private Long id;

    @ApiModelProperty("父节点ID：根分类为null")
    private Long parentId;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("显示名称：编码-名称")
    private String showName;

    @ApiModelProperty("节点类型：1-分类，2-方法")
    private Integer nodeType;

    @ApiModelProperty("子节点")
    private List<InspectMethodTreeNodeRespVO> children;
}


