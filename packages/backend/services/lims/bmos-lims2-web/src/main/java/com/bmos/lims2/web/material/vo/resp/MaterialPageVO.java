package com.bmos.lims2.web.material.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("检品管理列表 分页查询结果返回")
public class MaterialPageVO {

    @ApiModelProperty("检品当前系统id")
    private Long id;

    @ApiModelProperty("检品名称")
    private String name;

    @ApiModelProperty("检品编码")
    private String code;

    @ApiModelProperty("规格")
    private String specification;

    @ApiModelProperty("启停状态")
    private Boolean status;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("分类级联名称")
    private String categoryName;

}
