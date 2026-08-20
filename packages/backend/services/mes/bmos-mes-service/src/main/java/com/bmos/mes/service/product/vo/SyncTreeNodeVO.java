package com.bmos.mes.service.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("同步物料分类VO")
public class SyncTreeNodeVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty("展示名")
    private String showName;

    @ApiModelProperty("是否为分类节点")
    private boolean categoryFlag;

    @ApiModelProperty("子集")
    private List<SyncTreeNodeVO> children;
}
