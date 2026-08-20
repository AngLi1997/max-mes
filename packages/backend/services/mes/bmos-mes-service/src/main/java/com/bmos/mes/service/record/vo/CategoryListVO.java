package com.bmos.mes.service.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "查询分类集合")
public class CategoryListVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "子集")
    private List<CategoryListVO> itemList;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
