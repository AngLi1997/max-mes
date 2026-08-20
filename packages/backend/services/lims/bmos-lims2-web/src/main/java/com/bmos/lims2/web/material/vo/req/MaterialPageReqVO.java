package com.bmos.lims2.web.material.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "检品管理列表分页查询参数")
public class MaterialPageReqVO extends BasePage {

    @ApiModelProperty("检品名称")
    private String name;

    @ApiModelProperty("检品编码")
    private String mergeCode;

    @ApiModelProperty(value = "分类id", required = true)
    private Long categoryId;

    @ApiModelProperty(value = "是否为配置项目")
    private Boolean packageFlag;

}
